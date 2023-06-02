package com.example.datacrawlerservice.controller;

import com.example.datacrawlerservice.model.Report;
import com.example.datacrawlerservice.service.api.CrawlerApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class CrawlerController {
    private final CrawlerApiService service;

    @GetMapping("/report")
    public ResponseEntity<?> getReport() {
        Report report = service.getReport();
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
