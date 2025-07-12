package com.vts.websitescanner.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vts.websitescanner.model.MetaTag;

@Service
public class ExportService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String exportToJson(List<MetaTag> metaTags) throws IOException {
        return objectMapper.writeValueAsString(metaTags);
    }

    public String exportToCsv(List<MetaTag> metaTags) throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Name", "Property"));

        for (MetaTag metaTag : metaTags) {
            csvPrinter.printRecord(
                    metaTag.getName() != null ? metaTag.getName() : "",
                    metaTag.getProperty() != null ? metaTag.getProperty() : "");
        }

        csvPrinter.flush();
        csvPrinter.close();
        return writer.toString();
    }
}