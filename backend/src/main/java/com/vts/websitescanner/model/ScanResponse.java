package com.vts.websitescanner.model;

import java.util.List;

public class ScanResponse {
    private String url;
    private List<MetaTag> allMetaTags;
    private List<MetaTag> matchingMetaTags;
    private String error;
    private long scanTime;

    public ScanResponse() {
    }

    public ScanResponse(String url, List<MetaTag> allMetaTags, List<MetaTag> matchingMetaTags, long scanTime) {
        this.url = url;
        this.allMetaTags = allMetaTags;
        this.matchingMetaTags = matchingMetaTags;
        this.scanTime = scanTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MetaTag> getAllMetaTags() {
        return allMetaTags;
    }

    public void setAllMetaTags(List<MetaTag> allMetaTags) {
        this.allMetaTags = allMetaTags;
    }

    public List<MetaTag> getMatchingMetaTags() {
        return matchingMetaTags;
    }

    public void setMatchingMetaTags(List<MetaTag> matchingMetaTags) {
        this.matchingMetaTags = matchingMetaTags;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getScanTime() {
        return scanTime;
    }

    public void setScanTime(long scanTime) {
        this.scanTime = scanTime;
    }
}