package com.microblink.documentscanflow.document

enum class DocumentType(val typeName : String) {
    // always add new type as the last enum entry
    ID("ID"),
    OLD_ID("OLD_ID"),
    DL("DL"),
    PASSPORT("PASSPORT"),
    TRAVEL_DOCUMENT_VISA("TRAVEL_DOCUMENT_VISA"),
    RESIDENCE_PERMIT("RESIDENCE_PERMIT"),
    IMMIGRATOR_ID("IMMIGRATOR_ID"),
    MILITARY_ID("MILITARY_ID"),
    NEW_ID("NEW_ID");

}