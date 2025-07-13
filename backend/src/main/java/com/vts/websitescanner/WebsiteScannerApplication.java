package com.vts.websitescanner;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.vts.websitescanner.model.MetaTag;
import com.vts.websitescanner.model.ScanRequest;
import com.vts.websitescanner.service.ExportService;
import com.vts.websitescanner.service.ScannerService;

@SpringBootApplication
public class WebsiteScannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsiteScannerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            ScannerService scannerService = ctx.getBean(ScannerService.class);
            ExportService exportService = ctx.getBean(ExportService.class);
            Scanner scanner = new Scanner(System.in);

            System.out.println("=== Website Meta Tag Scanner ===");
            System.out.println("Console-based tool for scanning website meta tags");
            System.out.println();

            while (true) {
                System.out.print("Enter website URL to scan (or 'quit' to exit): ");
                String input = scanner.nextLine().trim();

                if ("quit".equalsIgnoreCase(input) || "exit".equalsIgnoreCase(input)) {
                    System.out.println("Goodbye!");
                    break;
                }

                if (input.isEmpty()) {
                    System.out.println("Please enter a valid URL.");
                    continue;
                }

                try {
                    System.out.println("Scanning website: " + input);
                    long startTime = System.currentTimeMillis();

                    // Scan the website
                    List<MetaTag> allMetaTags = scannerService.scanWebsite(input);
                    long scanTime = System.currentTimeMillis() - startTime;

                    System.out.println("Scan completed in " + scanTime + "ms");
                    System.out.println("Found " + allMetaTags.size() + " meta tags:");
                    System.out.println();

                    // Display meta tags
                    for (int i = 0; i < allMetaTags.size(); i++) {
                        MetaTag tag = allMetaTags.get(i);
                        System.out.println((i + 1) + ". Name: " + tag.getName() + 
                                         (tag.getProperty() != null && !tag.getProperty().isEmpty() ? 
                                          ", Property: " + tag.getProperty() : ""));
                    }

                    // Ask if user wants to export
                    System.out.println();
                    System.out.print("Export results? (y/n): ");
                    String exportChoice = scanner.nextLine().trim().toLowerCase();

                    if ("y".equals(exportChoice) || "yes".equals(exportChoice)) {
                        System.out.print("Export format (csv/json): ");
                        String format = scanner.nextLine().trim().toLowerCase();
                        
                        if ("csv".equals(format)) {
                            String csvData = exportService.exportToCsv(allMetaTags);
                            System.out.println("CSV Export:");
                            System.out.println(csvData);
                        } else if ("json".equals(format)) {
                            String jsonData = exportService.exportToJson(allMetaTags);
                            System.out.println("JSON Export:");
                            System.out.println(jsonData);
                        } else {
                            System.out.println("Invalid format. Using JSON as default.");
                            String jsonData = exportService.exportToJson(allMetaTags);
                            System.out.println("JSON Export:");
                            System.out.println(jsonData);
                        }
                    }

                } catch (IOException e) {
                    System.err.println("Error scanning website: " + e.getMessage());
                }

                System.out.println();
                System.out.println("----------------------------------------");
                System.out.println();
            }

            scanner.close();
            System.exit(0);
        };
    }
}