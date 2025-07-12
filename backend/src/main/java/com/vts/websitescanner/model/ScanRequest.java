package com.vts.websitescanner.model;

import java.util.List;

public class ScanRequest {
    private String url;
    private List<MetaTagFilter> filters;
    private String outputFormat; // "json" or "csv"

    public ScanRequest() {
    }

    public ScanRequest(String url, List<MetaTagFilter> filters, String outputFormat) {
        this.url = url;
        this.filters = filters;
        this.outputFormat = outputFormat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MetaTagFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<MetaTagFilter> filters) {
        this.filters = filters;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}