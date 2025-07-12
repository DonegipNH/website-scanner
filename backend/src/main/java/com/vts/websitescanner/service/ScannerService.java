package com.vts.websitescanner.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vts.websitescanner.model.audit.websitecqnn.input.AuditChecklist3Input;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vts.websitescanner.model.MetaTag;
import com.vts.websitescanner.model.MetaTagFilter;

@Service
public class ScannerService {

    @Autowired
    AuditWebsiteCQNN auditWebsiteCQNN;

        public List<MetaTag> scanWebsite(String url) throws IOException {
            // 1. Setup WebDriverManager for ChromeDriver
            WebDriverManager.chromedriver().setup();

            // 2. Configure headless mode
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new"); // Use "new" for Chrome 109+
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // 3. Launch browser in headless mode
            WebDriver driver = new ChromeDriver(options);

            driver.get(url);

            String html = driver.getPageSource();

            Document doc = Jsoup.parse(html);

            AuditChecklist3Input auditChecklist3Input = new AuditChecklist3Input();
            auditChecklist3Input.setWebsite(url);
            auditChecklist3Input.setDocument(doc);
            URL websiteUrl = new URL(url);
            auditChecklist3Input.setDomain(websiteUrl.getHost());
            auditWebsiteCQNN.auditChecklist3(auditChecklist3Input);


                Elements metaTags = doc.select("meta");
                List<MetaTag> metaTagList = new ArrayList<>();

                for (Element element : metaTags) {
                        MetaTag metaTag = new MetaTag();
                        metaTag.setName(element.attr("name"));
                        metaTag.setProperty(element.attr("property"));
                        metaTagList.add(metaTag);
                }

                return metaTagList;
        }

        public List<MetaTag> filterMetaTags(List<MetaTag> metaTags, List<MetaTagFilter> filters) {
                if (filters == null || filters.isEmpty()) {
                        return metaTags;
                }

                return metaTags.stream()
                                .filter(metaTag -> matchesAnyFilter(metaTag, filters))
                                .collect(Collectors.toList());
        }

        private boolean matchesAnyFilter(MetaTag metaTag, List<MetaTagFilter> filters) {
                return filters.stream().anyMatch(filter -> matchesFilter(metaTag, filter));
        }

        private boolean matchesFilter(MetaTag metaTag, MetaTagFilter filter) {
                // Check if name criteria match (case-insensitive)
                boolean nameMatch = filter.getName() == null || filter.getName().isEmpty() ||
                                (metaTag.getName() != null && metaTag.getName().toLowerCase()
                                                .contains(filter.getName().toLowerCase()));

                // Since we removed content field, we only check name match
                return nameMatch;
        }
}