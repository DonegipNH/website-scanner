package com.vts.websitescanner.service.impl;

import com.vts.websitescanner.model.MetaTag;
import com.vts.websitescanner.model.audit.websitecqnn.input.AuditChecklist3Input;
import com.vts.websitescanner.model.audit.websitecqnn.output.AuditChecklist3Output;
import com.vts.websitescanner.model.audit.websitecqnn.output.AuditChecklist3Result;
import com.vts.websitescanner.service.AuditWebsiteCQNN;
import com.vts.websitescanner.utils.Checklist3Utils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class AuditWebsiteCQNNImpl implements AuditWebsiteCQNN {

    @Value("${max_thread}")
    Integer maxThread;

    @Override
    public AuditChecklist3Output auditChecklist3(AuditChecklist3Input checkWebsite) {
        AuditChecklist3Output output = new AuditChecklist3Output();
        String url = checkWebsite.getWebsite();
        String domain = checkWebsite.getDomain();
        Document mainWebsite = checkWebsite.getDocument();
        // 1. Quet tat ca cac the <a href.. de tim cac path lien quan den main website
        Set<String> pathLinks = new HashSet<>();
        Elements links = mainWebsite.select("a[href]");
        for (Element link : links) {
            String href = link.absUrl("href");
            if (Checklist3Utils.isValidUrl(href) && href.contains(domain)) {
                pathLinks.add(href);
            }
        }

        // 2. Tong hop tat ca cac url can check
        pathLinks.add(url);

        // 3. Kiem tra checklist
        List<AuditChecklist3Result> checkListResults = new ArrayList<>();
        int numOfThreads = Math.min(maxThread, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        List<Future<AuditChecklist3Result>> futures = new ArrayList<>();
        for (String pathLink : pathLinks) {
            Callable<AuditChecklist3Result> task = () -> doChecklist3(pathLink);
            futures.add(executor.submit(task));
        }
        for (Future<AuditChecklist3Result> future : futures) {
            try {
                AuditChecklist3Result checkResult = future.get();
                checkListResults.add(checkResult);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        output.setListPath(checkListResults);
        return output;
    }

    private AuditChecklist3Result doChecklist3(String url) {
        AuditChecklist3Result result = new AuditChecklist3Result();
        result.setWebsite(url);
        result.setAccessStatus(true);
        HashMap<String, Boolean> checkListResult = Checklist3Utils.generateChecklist3Result();
        WebDriver driver = null;
        try {
            // 1. Setup WebDriverManager for ChromeDriver
            WebDriverManager.chromedriver().setup();

            // 2. Configure headless mode
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new"); // Use "new" for Chrome 109+
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // 3. Launch browser in headless mode
            driver = new ChromeDriver(options);

            driver.get(url);

            String html = driver.getPageSource();

            Document doc = Jsoup.parse(html);

            Elements metaTags = doc.select("meta");

            for (Element element : metaTags) {
                String name = element.attr("name");
                String content = element.attr("content");
                if (Checklist3Utils.isNullOrEmpty(name) || Checklist3Utils.isNullOrEmpty(content)) {
                    continue;
                }
                if (checkListResult.containsKey(name)) {
                    checkListResult.put(name, true);
                }
            }
            result.setChecklist(checkListResult);
        } catch (Exception e) {
            result.setAccessStatus(false);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

        return result;
    }
}
