package com.microblink.documentscanflow.recognition.resultentry

import com.microblink.documentscanflow.R
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys.*

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
    EXTENDED_NRIC_NUMBER(R.string.keyExtendedNricNumber),
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
    DRIVER_RESTRICTIONS(R.string.keyDriverRestrictions),
    ISSUING_AGENCY(R.string.keyIssuingAgency),
    LICENCE_CATEGORIES(R.string.keyLicenceCategories),
    DATE_OF_ISSUE_FOR_B_CATEGORY(R.string.keyDateOfIssueBCategory),

    SIGNATURE(R.string.keySignature),

    //USDL specific
    DOCUMENT_TYPE(R.string.keyDocumentType),
    STANDARD_VERSION_NUMBER(R.string.keyStandardVersionNumber),
    ADDRESS_STREET(R.string.keyAddressStreet),
    ADDRESS_STREET_2(R.string.keyAddressStreet2),
    ADDRESS_CITY(R.string.keyAddressCity),
    ADDRESS_JURISDICTION_CODE(R.string.keyJurisdictionCode),
    ADDRESS_POSTAL_CODE(R.string.keyPostalCode),
    FULL_ADDRESS(R.string.keyFullAddress),
    HEIGHT_IN(R.string.keyHeightIn),
    HEIGHT_CM(R.string.keyHeightCm),
    MIDDLE_NAME(R.string.keyMiddleName),
    HAIR_COLOR(R.string.keyHairColor),
    NAME_PREFIX(R.string.keyNamePrefix),
    NAME_SUFFIX(R.string.keyNameSuffix),
    AKA_FULL_NAME(R.string.keyAkaFullName),
    AKA_FAMILY_NAME(R.string.keyAkaFamilyName),
    AKA_GIVEN_NAME(R.string.keyAkaGivenName),
    AKA_SUFFIX_NAME(R.string.keyAkaSuffixName),
    AKA_PREFIX_NAME(R.string.keyAkaPrefixName),
    AKA_MIDDLE_NAME(R.string.keyAkaMiddleName),
    WEIGHT_RANGE(R.string.keyWeightRange),
    WEIGHT_POUNDS(R.string.keyWeightPounds),
    WEIGHT_KILOGRAMS(R.string.keyWeightKg),
    FAMILY_NAME_TRUNCATION(R.string.keyFamilyNameTruncation),
    FIRST_NAME_TRUNCATION(R.string.keyFirstNameTruncation),
    MIDDLE_NAME_TRUNCATION(R.string.keyMiddleNameTruncation),
    RACE_ETHNICITY(R.string.keyRaceEthnicity),
    COUNTRY_IDENTIFICATION(R.string.keyCountryIdentification),
    RESIDENCE_STREET_ADDRESS(R.string.keyResidenceStreetAddress),
    RESIDENCE_STREET_ADDRESS_2(R.string.keyResidenceStreetAddress2),
    RESIDENCE_CITY(R.string.keyResidenceCity),
    RESIDENCE_JURISDICTION_CODE(R.string.keyResidenceJurisdictionCode),
    RESIDENCE_FULL_ADDRESS(R.string.keyResidenceFullAddress),
    UNDER_18(R.string.keyUnder18),
    UNDER_19(R.string.keyUnder19),
    UNDER_21(R.string.keyUnder21),
    SOCIAL_SECURITY_NUMBER(R.string.keySocialSecurityNumber),
    AKA_SOCIAL_SECURITY_NUMBER(R.string.keyAkaSocialSecurityNumber),
    ORGAN_DONOR(R.string.keyOrganDonor),
    VETERAN(R.string.keyVeteran),
    AKA_DATE_OF_BIRTH(R.string.keyAkaDateOfBirth),
    ISSUER_ID_NUMBER(R.string.keyIssuerIdNumber),
    JURISDICTION_VERSION_NUMBER(R.string.keyJurisdictionVersionNumber),
    JURISDICTION_VEHICLE_CLASS(R.string.keyJurisdictionVehicleClass),
    JURISDICTION_RESTRICTION_CODES(R.string.keyJurisdictionRestrictionCodes),
    JURISDICTION_ENDORSEMENT_CODES(R.string.keyJurisdictionEndorsementCodes),
    FEDERAL_COMMERCIAL_VEHICLE_CODES(R.string.keyFederalCommercialVehicleCodes),
    ISSUING_JURISDICTION(R.string.keyIssuingJurisdiction),
    STANDARD_VEHICLE_CLASSIFICATION(R.string.keyStandardVehicleClassification),
    ISSUING_JURISDICTION_NAME(R.string.keyIssuingJurisdictionName),
    STANDARD_ENDORSEMENT_CODES(R.string.keyStandardEndorsmentCodes),
    STANDARD_RESTRICTION_CODES(R.string.keyStandardRestrictionCodes),
    JURISDICTION_VEHICLE_CLASSIFICATION_DESCRIPTION(R.string.keyJurisdictionVehicleClassificationDescription),
    JURISDICTION_ENDORSEMENT_CODE_DESCRIPTION(R.string.keyJurisdictionEndorsementCodeDescription),
    JURISDICTION_RESTRICTION_CODE_DESCRIPTION(R.string.keyJurisdictionRestrictionCodeDescription),
    INVENTORY_CONTROL_NUMBER(R.string.keyInventoryControlNumber),
    CARD_REVISION_DATE(R.string.keyCardRevisionDate),
    DOCUMENT_DISCRIMINATOR(R.string.keyDocumentDiscriminator),
    LIMITED_DURATION_DOCUMENT(R.string.keyLimitedDurationDocument),
    AUDIT_INFORMATION(R.string.keyAuditInformation),
    COMPLIANCE_TYPE(R.string.keyComplianceType),
    ISSUE_TIMESTAMP(R.string.keyIssueTimestamp),
    PERMIT_EXPIRATION_DATE(R.string.keyPermitExpirationDate),
    PERMIT_IDENTIFIER(R.string.keyPermitIdentifier),
    PERMIT_ISSUE_DATE(R.string.keyPermitIssueDate),
    NUMBER_OF_DUPLICATES(R.string.keyNumberOfDuplicates),
    HAZMAT_EXPIRATION_DATE(R.string.keyHazmatExpirationDate),
    MEDICAL_INDICATOR(R.string.keyMedicalIndicator),
    NON_RESIDENT(R.string.keyNonResident),
    UNIQUE_CUSTOMER_ID(R.string.keyUniqueCustomerId),
    DATA_DISCRIMINATOR(R.string.keyDataDiscriminator),
    DOCUMENT_EXPIRATION_MONTH(R.string.keyDocumentExpirationMonth),
    DOCUMENT_NON_EXPIRING(R.string.keyDocumentNonExpiring),
    SECURITY_VERSION(R.string.keySecurityVersion)
    ;

    companion object {
        fun fromUsdlKey(usdlKeys: UsdlKeys): ResultKey {
            return when(usdlKeys) {
                LimitedDurationDocument -> LIMITED_DURATION_DOCUMENT
                AuditInformation -> AUDIT_INFORMATION
                ComplianceType -> COMPLIANCE_TYPE
                IssueTimestamp -> ISSUE_TIMESTAMP
                PermitExpirationDate -> PERMIT_EXPIRATION_DATE
                PermitIdentifier -> PERMIT_IDENTIFIER
                PermitIssueDate -> PERMIT_ISSUE_DATE
                NumberOfDuplicates -> NUMBER_OF_DUPLICATES
                HAZMATExpirationDate -> HAZMAT_EXPIRATION_DATE
                MedicalIndicator -> MEDICAL_INDICATOR
                NonResident -> NON_RESIDENT
                UniqueCustomerId -> UNIQUE_CUSTOMER_ID
                DataDiscriminator -> DATA_DISCRIMINATOR
                DocumentExpirationMonth -> DOCUMENT_EXPIRATION_MONTH
                DocumentNonexpiring -> DOCUMENT_NON_EXPIRING
                SecurityVersion -> SECURITY_VERSION
                JurisdictionVehicleClassificationDescription -> JURISDICTION_VEHICLE_CLASSIFICATION_DESCRIPTION
                JurisdictionEndorsmentCodeDescription -> JURISDICTION_ENDORSEMENT_CODE_DESCRIPTION
                JurisdictionRestrictionCodeDescription -> JURISDICTION_RESTRICTION_CODE_DESCRIPTION
                InventoryControlNumber -> INVENTORY_CONTROL_NUMBER
                CardRevisionDate -> CARD_REVISION_DATE
                DocumentDiscriminator -> DOCUMENT_DISCRIMINATOR
                FederalCommercialVehicleCodes -> FEDERAL_COMMERCIAL_VEHICLE_CODES
                IssuingJurisdiction -> ISSUING_JURISDICTION
                StandardVehicleClassification -> STANDARD_VEHICLE_CLASSIFICATION
                IssuingJurisdictionName -> ISSUING_JURISDICTION_NAME
                StandardEndorsementCode -> STANDARD_ENDORSEMENT_CODES
                StandardRestrictionCode -> STANDARD_RESTRICTION_CODES
                JurisdictionVersionNumber -> JURISDICTION_VERSION_NUMBER
                JurisdictionVehicleClass -> JURISDICTION_VEHICLE_CLASS
                JurisdictionRestrictionCodes -> JURISDICTION_RESTRICTION_CODES
                JurisdictionEndorsementCodes -> JURISDICTION_ENDORSEMENT_CODES
                DocumentIssueDate -> DATE_OF_ISSUE
                SocialSecurityNumber -> SOCIAL_SECURITY_NUMBER
                AKASocialSecurityNumber -> AKA_SOCIAL_SECURITY_NUMBER
                OrganDonor -> ORGAN_DONOR
                Veteran -> VETERAN
                AKADateOfBirth -> AKA_DATE_OF_BIRTH
                IssuerIdentificationNumber -> ISSUER_ID_NUMBER
                DocumentExpirationDate -> DATE_OF_EXPIRY
                CountryIdentification -> COUNTRY_IDENTIFICATION
                ResidenceStreetAddress -> RESIDENCE_STREET_ADDRESS
                ResidenceStreetAddress2 -> RESIDENCE_STREET_ADDRESS_2
                ResidenceCity -> RESIDENCE_CITY
                ResidenceJurisdictionCode -> RESIDENCE_JURISDICTION_CODE
                ResidencePostalCode -> ADDRESS_POSTAL_CODE
                ResidenceFullAddress -> RESIDENCE_FULL_ADDRESS
                Under18 -> UNDER_18
                Under19 -> UNDER_19
                Under21 -> UNDER_19
                WeightRange -> WEIGHT_RANGE
                WeightPounds -> WEIGHT_POUNDS
                WeightKilograms -> WEIGHT_KILOGRAMS
                CustomerIdNumber -> IDENTITY_NUMBER
                FamilyNameTruncation -> FAMILY_NAME_TRUNCATION
                FirstNameTruncation -> FIRST_NAME_TRUNCATION
                MiddleNameTruncation -> MIDDLE_NAME_TRUNCATION
                AddressStreet2 -> ADDRESS_STREET_2
                RaceEthnicity -> RACE_ETHNICITY
                NamePrefix -> NAME_PREFIX
                DocumentType -> DOCUMENT_TYPE
                StandardVersionNumber -> STANDARD_VERSION_NUMBER
                CustomerFamilyName -> FAMILY_NAME
                CustomerFirstName -> FIRST_NAME
                CustomerFullName -> FULL_NAME
                DateOfBirth -> DATE_OF_BIRTH
                Sex -> SEX
                EyeColor -> EYE_COLOR
                AddressStreet -> ADDRESS_STREET
                AddressCity -> ADDRESS_CITY
                AddressJurisdictionCode -> ADDRESS_JURISDICTION_CODE
                AddressPostalCode -> ADDRESS_POSTAL_CODE
                FullAddress -> FULL_ADDRESS
                Height -> HEIGHT
                HeightIn -> HEIGHT_IN
                HeightCm -> HEIGHT_CM
                CustomerMiddleName -> MIDDLE_NAME
                HairColor -> HAIR_COLOR
                NameSuffix -> NAME_SUFFIX
                AKAFullName -> AKA_FULL_NAME
                AKAFamilyName -> AKA_FAMILY_NAME
                AKAGivenName -> AKA_GIVEN_NAME
                AKASuffixName -> AKA_GIVEN_NAME
                AKAMiddleName -> AKA_MIDDLE_NAME
                AKAPrefixName -> AKA_PREFIX_NAME
                PlaceOfBirth -> PLACE_OF_BIRTH
            }
        }
    }

}