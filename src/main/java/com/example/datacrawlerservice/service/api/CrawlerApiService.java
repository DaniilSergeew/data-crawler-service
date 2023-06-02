package com.example.datacrawlerservice.service.api;

import com.example.datacrawlerservice.model.Report;
import com.example.datacrawlerservice.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerApiService {
    private final CrawlerService service;

    public Report getReport() {
        log.info("Trying to get report...");
        Report report = service.handleGetReportRequest();
        log.info("Getting was successful");
        return report;
    }
}
