package com.vts.websitescanner.model.audit.websitecqnn.checklist;

public enum Checklist3RequireMetaName {
    DC_TITLE("DC.Title"),
    DC_CREATOR("DC.Creator"),
    DC_DATE("DC.Date"),
    DC_PUBLISHER("DC.Publisher"),
    DC_DESCRIPTION("DC.Description"),
    DC_IDENTIFIER("DC.Identifier"),
    DC_LANGUAGE("DC.Language"),
    DC_SOURCE("DC.Source"),
    DC_CONTRIBUTOR("DC.Contributor"),
    DC_SUBJECT("DC.Subject"),
    DC_COVERAGE("DC.Coverage"),
    DC_TYPE("DC.Type"),
    DC_FORMAT("DC.Format"),
    DC_RELATION("DC.Relation"),
    DC_RIGHTS("DC.Rights");

    private final String value;

    Checklist3RequireMetaName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
