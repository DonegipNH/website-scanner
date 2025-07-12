package com.vts.websitescanner.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vts.websitescanner.model.ScanRequest;
import com.vts.websitescanner.model.ScanResponse;
import com.vts.websitescanner.service.ExportService;
import com.vts.websitescanner.service.ScannerService;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "*")
public class WebsiteScannerController {

    @Autowired
    private ScannerService scannerService;

    @Autowired
    private ExportService exportService;

    @PostMapping("/scan")
    public ResponseEntity<ScanResponse> scanWebsite(@RequestBody ScanRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            // Scan the website
            var allMetaTags = scannerService.scanWebsite(request.getUrl());

            // Apply filters if provided
            var matchingMetaTags = scannerService.filterMetaTags(allMetaTags, request.getFilters());

            long scanTime = System.currentTimeMillis() - startTime;

            ScanResponse response = new ScanResponse(request.getUrl(), allMetaTags, matchingMetaTags, scanTime);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            ScanResponse errorResponse = new ScanResponse();
            errorResponse.setUrl(request.getUrl());
            errorResponse.setError("Failed to scan website: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/export")
    public ResponseEntity<String> exportResults(@RequestBody ScanRequest request) {
        try {
            // Scan the website
            var allMetaTags = scannerService.scanWebsite(request.getUrl());

            // Apply filters if provided
            var matchingMetaTags = scannerService.filterMetaTags(allMetaTags, request.getFilters());

            String exportData;
            String contentType;
            String filename;

            if ("csv".equalsIgnoreCase(request.getOutputFormat())) {
                exportData = exportService.exportToCsv(matchingMetaTags);
                contentType = "text/csv";
                filename = "meta-tags-" + System.currentTimeMillis() + ".csv";
            } else {
                exportData = exportService.exportToJson(matchingMetaTags);
                contentType = "application/json";
                filename = "meta-tags-" + System.currentTimeMillis() + ".json";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(exportData);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to export results: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Meta Tag Scanner is running!");
    }
}