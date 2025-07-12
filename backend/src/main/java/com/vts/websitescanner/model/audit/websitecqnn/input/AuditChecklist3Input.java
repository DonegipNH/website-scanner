package com.vts.websitescanner.model.audit.websitecqnn.input;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

@Data
@Getter
@Setter
public class AuditChecklist3Input {
    private String domain;
    private String website;
    private Document document;
}
