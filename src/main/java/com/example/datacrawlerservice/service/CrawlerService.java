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
        return getReport(crawl);
    }

    private Report getReport(Crawl crawl) {
        List<Phone> phones = crawl.getPhones();
        Report report = new Report();
        report.setAveragePrice((int) phones.stream().mapToInt(Phone::getPrice).average().orElse(0));
        report.setMaxPrice(phones.stream().mapToInt(Phone::getPrice).max().orElse(0));
        report.setMinPrice(phones.stream().mapToInt(Phone::getPrice).min().orElse(0));
        report.setInStockFactor((float) phones.stream().filter(Phone::isInStock).count() / phones.size());
        reportRepository.save(report);
        return report;
    }

    private Crawl getCrawl() {
        List<Phone> data = new ArrayList<>();
        for (int page = 1; page < 12; page++) {
            // Достаем HTML с каждой страницы сайта
            Document document = null;
            try {
                document = Jsoup.connect("https://spb.shop.megafon.ru/mobile?page=" + page + "&archVal=0").get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Вытаскиваем нужные блоки по классам
            Elements names = document.getElementsByClass("b-good__title-link");
            Elements prices = document.getElementsByClass("b-price-good-list__value b-price__value");
            Elements inStocks = document.getElementsByClass("b-good-cards__status-item pickup ");

            // Для каждого блока создаем итератор и идем сразу тремя итераторами, создавая объекты
            Iterator<Element> namesIterator = names.iterator();
            Iterator<Element> pricesIterator = prices.iterator();
            Iterator<Element> inStocksIterator = inStocks.iterator();

            for (int i = 0; i < names.size(); i++) {
                Phone phone = new Phone();
                String name = namesIterator.next().text();
                phone.setBrand(findBrand(name));

                String model = name
                        .replaceAll("\\d+GB.*", "")
                        .replace("Смартфон ", "")
                        .replace(phone.getBrand() + " ", "");

                phone.setModel(model);

                Pattern pattern = Pattern.compile("\\b(\\d+)GB\\b");
                Matcher matcher = pattern.matcher(name);
                while (matcher.find()) {
                    String digit = matcher.group(1);
                    phone.setMemory(Integer.parseInt(digit));
                }

                int price = Integer.parseInt(pricesIterator.next().text().replace(" ", ""));
                phone.setPrice(price);

                phone.setInStock(inStocksIterator.next().text().contains("Есть самовывозом, сегодня"));
                data.add(phone);
            }
        }
        Crawl crawl = new Crawl();
        crawl.setDate(Instant.now());
        crawl.setPhones(data);
        crawlRepository.save(crawl);
        return crawl;
    }

    // Todo: это не дело, вроде бы у Евгения Борисова был доклад про что то подобное и паттерн chain if responsibility
    private Brand findBrand(String name) {
        if (name.contains(Brand.HONOR.toString())) {
            return Brand.HONOR;
        } else if (name.contains(Brand.Itel.toString())) {
            return Brand.Itel;
        } else if (name.contains(Brand.Apple.toString())) {
            return Brand.Apple;
        } else if (name.contains(Brand.HUAWEI.toString())) {
            return Brand.HUAWEI;
        } else if (name.contains(Brand.TCL.toString())) {
            return Brand.TCL;
        } else if (name.contains(Brand.Poco.toString())) {
            return Brand.Poco;
        } else if (name.contains(Brand.realme.toString())) {
            return Brand.realme;
        } else if (name.contains(Brand.Samsung.toString())) {
            return Brand.Samsung;
        } else if (name.contains(Brand.Xiaomi.toString())) {
            return Brand.Xiaomi;
        } else if (name.contains(Brand.Tecno.toString())) {
            return Brand.Tecno;
        } else if (name.contains(Brand.Vivo.toString())) {
            return Brand.Vivo;
        } else if (name.contains(Brand.Wiko.toString())) {
            return Brand.Wiko;
        } else {
            return Brand.ZTE;
        }
    }
}