package com.vts.websitescanner.utils;

import com.vts.websitescanner.model.audit.websitecqnn.checklist.Checklist3RequireMetaName;

import java.net.URL;
import java.util.HashMap;

public class Checklist3Utils {
    public static HashMap<String, Boolean> generateChecklist3Result() {
        HashMap<String, Boolean> checklist3Result = new HashMap<>();
        for (Checklist3RequireMetaName checklist3RequireMetaName : Checklist3RequireMetaName.values()) {
            checklist3Result.put(checklist3RequireMetaName.name(), false);
        }
        return checklist3Result;
    }

    public static boolean isValidUrl(String url) {
        if (isNullOrEmpty(url)) {
            return false;
        }
        try {
            new URL(url); // Try to parse it
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
