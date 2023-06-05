package com.example.datacrawlerservice.service;

import com.example.datacrawlerservice.model.Brand;
import com.example.datacrawlerservice.model.Crawl;
import com.example.datacrawlerservice.model.Phone;
import com.example.datacrawlerservice.model.Report;
import com.example.datacrawlerservice.repository.CrawlRepository;
import com.example.datacrawlerservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlerService {

    private final CrawlRepository crawlRepository;
    private final ReportRepository reportRepository;

    public Report handleGetReportRequest() {
        Crawl crawl = getCrawl();
        crawlRepository.save(crawl);
        Report report = getReport(crawl);
        reportRepository.save(report);
        return report;
    }

    private Report getReport(Crawl crawl) {
        List<Phone> phones = crawl.getPhones();
        return Report.builder()
                .averagePrice((int) phones.stream().mapToInt(Phone::getPrice).average().orElse(0))
                .maxPrice(phones.stream().mapToInt(Phone::getPrice).max().orElse(0))
                .minPrice(phones.stream().mapToInt(Phone::getPrice).min().orElse(0))
                .inStockFactor((float) phones.stream().filter(Phone::isInStock).count() / phones.size())
                .build();
    }

    private Crawl getCrawl() {
        List<Phone> phones = new ArrayList<>();
        for (int page = 1; page < 12; page++) {
            // Достаем HTML с каждой страницы сайта
            Document document;
            try {
                String url = "https://spb.shop.megafon.ru/mobile?page=" + page + "&archVal=0";
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Вытаскиваем нужные блоки и итерируемся по ним
            Elements titles = document.getElementsByClass("b-good__title-link");
            Elements prices = document.getElementsByClass("b-price-good-list__value b-price__value");
            Elements inStocks = document.getElementsByClass("b-good-cards__status-item pickup ");
            Iterator<Element> titlesIterator = titles.iterator();
            Iterator<Element> pricesIterator = prices.iterator();
            Iterator<Element> inStocksIterator = inStocks.iterator();

            for (int i = 0; i < titles.size(); i++) {
                String title = titlesIterator.next().text();
                Brand brand = findBrand(title);
                /* Todo
                 Для iphone на 1tb необходимы другие регулярки, их всего 1-2 в таблице, поэтому
                 надеюсь в данном контексте их присутствие не принципиально
                 */
                String model = title
                        .replaceAll("\\d+GB.*", "")
                        .replace("Смартфон ", "")
                        .replace(brand + " ", "")
                        .replace("/", "");
                Pattern pattern = Pattern.compile("\\b(\\d+)GB\\b");
                Matcher matcher = pattern.matcher(title);
                int memory = 0;
                while (matcher.find()) {
                    String digit = matcher.group(1);
                    memory = Integer.parseInt(digit);
                }
                int price = Integer.parseInt(pricesIterator.next().text().replace(" ", ""));
                boolean inStock = inStocksIterator.next().text().contains("Есть самовывозом, сегодня");
                Phone phone = Phone.builder()
                        .brand(brand)
                        .model(model)
                        .memory(memory)
                        .price(price)
                        .inStock(inStock)
                        .build();
                phones.add(phone);
            }
        }
        return Crawl.builder()
                .date(Instant.now())
                .phones(phones)
                .build();
    }

    private Brand findBrand(String name) {
        for (Brand brand : Brand.values()) {
            if (name.contains(brand.toString())) {
                return brand;
            }
        }
        return Brand.ZTE;
    }
}