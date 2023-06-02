package com.example.datacrawlerservice.controller;

import com.example.datacrawlerservice.model.Report;
import com.example.datacrawlerservice.service.api.CrawlerApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с репортами")
public class CrawlerController {
    private final CrawlerApiService service;

    @Operation(
            summary = "Получение репорта, датированного текущим временем"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting successful"),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @GetMapping("/report")
    public ResponseEntity<?> getReport() {
        Report report = service.getReport();
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
