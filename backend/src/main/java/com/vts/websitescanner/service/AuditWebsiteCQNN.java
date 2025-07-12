package com.vts.websitescanner.service;

import com.vts.websitescanner.model.audit.websitecqnn.input.AuditChecklist3Input;
import com.vts.websitescanner.model.audit.websitecqnn.output.AuditChecklist3Output;
import org.jsoup.nodes.Document;

public interface AuditWebsiteCQNN {
    AuditChecklist3Output auditChecklist3(AuditChecklist3Input checkWebsite);
}
