package com.microblink.documentscanflow.recognition.resultentry

import com.microblink.documentscanflow.R

enum class ResultKey(val stringResId: Int) {

    //Names
    FIRST_NAME(R.string.keyFirstName),
    SECONDARY_ID(R.string.keySecondaryId),
    LAST_NAME(R.string.keyLastName),
    PRIMARY_ID(R.string.keyPrimaryId),
    FAMILY_NAME(R.string.keyFamilyName),
    FULL_NAME(R.string.keyFullName),
    SURNAME_AT_BIRTH(R.string.keySurnameAtBirth),
    PARENT_NAMES(R.string.keyParentNames),

    //Birth date-place
    DATE_OF_BIRTH(R.string.keyDateOfBirth),
    BIRTH_DATA(R.string.keyBirthData),
    COUNTRY_OF_BIRTH(R.string.keyCountryOfBirth),
    PLACE_OF_BIRTH(R.string.keyPlaceOfBirth),
    PLACE_OF_ORIGIN(R.string.keyPlaceOfOrigin),

    //Issued-Expired
    DATE_OF_ISSUE(R.string.keyIssueDate),
    DATE_OF_ISSUE_FOR_B_CATEGORY(R.string.keyDateOfIssueBCategory),
    DATE_OF_EXPIRY(R.string.keyDateOfExpiry),
    DATE_OF_EXPIRY_PERMANENT(R.string.keyDateOfExpiryPermanent),

    //Document numbers
    ALIEN_NUMBER(R.string.keyAlienNumber),
    APPLICATION_RECEIPT_NUMBER(R.string.keyApplicationReceiptNumber),
    ARMY_NUMBER(R.string.keyArmyNumber),
    CIVIL_STATUS_NUMBER(R.string.keyCivilStatusNumber),
    CIVIL_ID_NUMBER(R.string.keyCivilIdNumber),
    CURP(R.string.keyCurp),
    DOCUMENT_NUMBER(R.string.keyDocumentNumber),
    DRIVER_NUMBER(R.string.keyDriverNumber),
    IMMIGRANT_CASE_NUMBER(R.string.keyImmigrantCaseNumber),
    IDENTITY_NUMBER(R.string.keyIdentityNumber),
    LICENCE_NUMBER(R.string.keyLicenceNumber),
    NATIONAL_NUMBER(R.string.keyNationalNumber),
    NRIC_NUMBER(R.string.keyNricNumber),
    SERIAL_NUMBER(R.string.keySerialNumber),
    PERSONAL_NUMBER(R.string.keyPersonalNumber),
    PASSPORT_NUMBER(R.string.keyPassportNumber),

    //Address
    CITY(R.string.keyCity),
    PROVINCE(R.string.keyProvince),
    ADDRESS(R.string.keyAddress),
    PRINCIPAL_RESIDENCE_AT_ISSUANCE(R.string.keyPrincipalResidenceAtIssuance),
    FACULTY_ADDRESS(R.string.keyFacultyAddress),
    RESIDENTIAL_STATUS(R.string.keyResidentialStatus),

    //Nationality
    NATIONALITY(R.string.keyNationality),
    CITIZENSHIP(R.string.keyCitizenship),

    //Issuer
    ISSUER(R.string.keyIssuer),
    AUTHORITY(R.string.keyAuthority),
    ISSUING_AGENCY(R.string.keyIssuingAgency),
    ISSUING_AUTHORITY(R.string.keyIssuingAuthority),
    PLACE_OF_ISSUE(R.string.keyPlaceOfIssue),

    //Personal details
    SEX(R.string.keySex),
    BLOOD_GROUP(R.string.keyBloodGroup),
    EYE_COLOR(R.string.keyEyeColor),
    HEIGHT(R.string.keyHeight),
    MARITAL_STATUS(R.string.keyMaritalStatus),
    RACE(R.string.keyRace),
    RELIGION(R.string.keyReligion),

    DOCUMENT_CODE(R.string.keyDocumentCode),
    LICENCE_CATEGORIES(R.string.keyLicenceCategories),
    CLASS(R.string.keyClass),
    VEHICLE_CLASS(R.string.keyVehicleClass),
    ENDORSEMENTS(R.string.keyEndorsements),
    DRIVER_RESTRICTIONS(R.string.keyDriverRestrictions),
    VEHICLE_CATEGORIES(R.string.keyVehicleCategories),

    //Employment
    EMPLOYER(R.string.keyEmployer),
    EMPLOYER_ADDRESS(R.string.keyEmployerAddress),
    OCCUPATION(R.string.keyOccupation),

    //Other
    BARCODE_DATA(R.string.keyBarcodeString),
    MRZ_TEXT(R.string.keyMRZText),
    OPTIONAL_FIELD_1(R.string.keyOpt1),
    OPTIONAL_FIELD_2(R.string.keyOpt2),
    RANK(R.string.keyRank),
    SECTOR(R.string.keySector),
    SPECIAL_REMARKS(R.string.keySpecialRemarks),
    ;

}