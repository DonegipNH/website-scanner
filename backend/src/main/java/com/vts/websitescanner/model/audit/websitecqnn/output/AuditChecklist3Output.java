package com.vts.websitescanner.model.audit.websitecqnn.output;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class AuditChecklist3Output {
    private String website;
    private List<AuditChecklist3Result> listPath;
}
