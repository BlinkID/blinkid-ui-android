package com.microblink.documentscanflow.recognition.resultentry

import com.microblink.documentscanflow.R

enum class ResultKey(val stringResId: Int) {

    PRIMARY_ID(R.string.keyPrimaryId),
    SECONDARY_ID(R.string.keySecondaryId),
    SEX(R.string.keySex),
    NATIONALITY(R.string.keyNationality),
    DOCUMENT_CODE(R.string.keyDocumentCode),
    DOCUMENT_NUMBER(R.string.keyDocumentNumber),
    ISSUER(R.string.keyIssuer),
    OPTIONAL_FIELD_1(R.string.keyOpt1),
    OPTIONAL_FIELD_2(R.string.keyOpt2),
    MRZ_TEXT(R.string.keyMRZText),
    ALIEN_NUMBER(R.string.keyAlienNumber),
    APPLICATION_RECEIPT_NUMBER(R.string.keyApplicationReceiptNumber),
    IMMIGRANT_CASE_NUMBER(R.string.keyImmigrantCaseNumber),
    RANK(R.string.keyRank),

    FULL_NAME(R.string.keyFullName),
    FIRST_NAME(R.string.keyFirstName),
    LAST_NAME(R.string.keyLastName),
    FAMILY_NAME(R.string.keyFamilyName),
    PARENT_NAMES(R.string.keyParentNames),
    ADDRESS(R.string.keyAddress),
    PROVINCE(R.string.keyProvince),
    CITY(R.string.keyCity),
    DATE_OF_EXPIRY(R.string.keyDateOfExpiry),
    DATE_OF_EXPIRY_PERMANENT(R.string.keyDateOfExpiryPermanent),
    DATE_OF_BIRTH(R.string.keyDateOfBirth),
    DATE_OF_ISSUE(R.string.keyIssueDate),
    PLACE_OF_BIRTH(R.string.keyPlaceOfBirth),
    ISSUING_AUTHORITY(R.string.keyIssuingAuthority),
    AUTHORITY(R.string.keyAuthority),
    HEIGHT(R.string.keyHeight),
    EYE_COLOR(R.string.keyEyeColor),
    RACE(R.string.keyRace),
    PRINCIPAL_RESIDENCE_AT_ISSUANCE(R.string.keyPrincipalResidenceAtIssuance),
    PASSPORT_NUMBER(R.string.keyPassportNumber),
    EMPLOYER_ADDRESS(R.string.keyEmployerAddress),
    PERSONAL_NUMBER(R.string.keyPersonalNumber),
    CITIZENSHIP(R.string.keyCitizenship),
    IDENTITY_NUMBER(R.string.keyIdentityNumber),
    NATIONAL_NUMBER(R.string.keyNationalNumber),
    BIRTH_DATA(R.string.keyBirthData),
    COUNTRY_OF_BIRTH(R.string.keyCountryOfBirth),
    RESIDENTIAL_STATUS(R.string.keyResidentialStatus),
    RELIGION(R.string.keyReligion),
    MARITAL_STATUS(R.string.keyMaritalStatus),
    OCCUPATION(R.string.keyOccupation),
    SERIAL_NUMBER(R.string.keySerialNumber),
    CIVIL_ID_NUMBER(R.string.keyCivilIdNumber),
    CLASS(R.string.keyClass),
    FACULTY_ADDRESS(R.string.keyFacultyAddress),
    SECTOR(R.string.keySector),
    EMPLOYER(R.string.keyEmployer),
    NRIC_NUMBER(R.string.keyNricNumber),
    ARMY_NUMBER(R.string.keyArmyNumber),
    CURP(R.string.keyCurp),
    CIVIL_STATUS_NUMBER(R.string.keyCivilStatusNumber),
    BLOOD_GROUP(R.string.keyBloodGroup),
    SPECIAL_REMARKS(R.string.keySpecialRemarks),
    SURNAME_AT_BIRTH(R.string.keySurnameAtBirth),
    PLACE_OF_ORIGIN(R.string.keyPlaceOfOrigin),
    PLACE_OF_ISSUE(R.string.keyPlaceOfIssue),
    BARCODE_DATA(R.string.keyBarcodeString),

    LICENCE_NUMBER(R.string.keyLicenceNumber),
    DRIVER_NUMBER(R.string.keyDriverNumber),
    VEHICLE_CATEGORIES(R.string.keyVehicleCategories),
    VEHICLE_CLASS(R.string.keyVehicleClass),
    DRIVER_RESTRICTIONS(R.string.keyDriverRestrictions),
    ENDORSEMENTS(R.string.keyEndorsements),
    ISSUING_AGENCY(R.string.keyIssuingAgency),
    LICENCE_CATEGORIES(R.string.keyLicenceCategories),
    DATE_OF_ISSUE_FOR_B_CATEGORY(R.string.keyDateOfIssueBCategory),

    ;

}