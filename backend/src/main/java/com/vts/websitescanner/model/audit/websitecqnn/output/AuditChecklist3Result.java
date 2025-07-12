package com.vts.websitescanner.model.audit.websitecqnn.output;

import com.vts.websitescanner.utils.Checklist3Utils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Data
@Getter
@Setter
public class AuditChecklist3Result {
    private String website;
    private HashMap<String, Boolean> checklist = Checklist3Utils.generateChecklist3Result();
    private boolean accessStatus = false;
}
