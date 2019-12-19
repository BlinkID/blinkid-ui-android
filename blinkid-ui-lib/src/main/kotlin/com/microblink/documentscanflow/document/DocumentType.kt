package com.microblink.documentscanflow.document

enum class DocumentType {

    ID,
    OLD_ID,
    DL,
    PASSPORT,
    TRAVEL_DOCUMENT_VISA,
    RESIDENCE_PERMIT,
    TEMPORARY_RESIDENCE_PERMIT,
    IMMIGRATOR_ID,
    CHILD_ID,
    POLICE_ID,
    MILITARY_ID,
    NEW_ID,
    TEMPORARY_RESIDENT_ID,
    PERMANENT_RESIDENT_ID,
    VOTER_ID,
    WORK_PASS,

    // country specific:

    // Australia
    VICTORIA_DL,

    // Singapore
    ENTREPASS,

    // Indonesia
    KITAS,

    // USA
    UNDER_21_ID;

}