package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.document.DocumentDescription
import com.microblink.documentscanflow.document.DocumentDescriptionBuilder
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.document.GenericDocumentDescriptions
import com.microblink.documentscanflow.recognition.implementations.*
import com.microblink.entities.recognizers.blinkid.eudl.EudlCountry

enum class SupportedCountry(override val code: String, override val documentDescriptions: Map<DocumentType, DocumentDescription>)
    : Country {

    AUSTRALIA("au",
            DocumentDescriptionBuilder()
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
                    .remove(DocumentType.ID)
                    .build()),

    AUSTRIA("at",
            DocumentDescriptionBuilder()
                    .id(AustriaIdRecognition())
                    .drivingLicence(AustriaDlRecognition())
                    .passport(AustriaPassportRecognition())
                    .build()),

    CANADA("ca",
            DocumentDescriptionBuilder()
                    .id(UsdlRecognition())
                    .drivingLicence(UsdlRecognition())
                    .build()),

    COLOMBIA("co",
            DocumentDescriptionBuilder()
                    .id(ColombiaIdRecognition())
                    .drivingLicence(ColombiaDlRecognition())
                    .build()),

    CROATIA("hr",
            DocumentDescriptionBuilder()
                    .id(CroatiaIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                    .residencePermit(GenericDocumentDescriptions.residencePermit)
                    .build()),

    CYPRUS("cy",
            DocumentDescriptionBuilder()
                    .id(CyprusIdRecognition())
                    .build()),

    CZECHIA("cz",
            DocumentDescriptionBuilder()
                    .id(CzechIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                    .oldId(GenericRecognition.mrtdId2Vertical())
                    .build()),

    EGYPT("eg",
            DocumentDescriptionBuilder()
                    .id(EgyptIdRecognition())
                    .build()),

    FRANCE("fr",
            DocumentDescriptionBuilder()
                    .id(GenericRecognition.mrtdId1(), fullySupported = false)
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
                    .residencePermit(GenericDocumentDescriptions.residencePermit)
                    .build()),

    GERMANY("de",
            DocumentDescriptionBuilder()
                    .id(GermanyIdRecognition())
                    .drivingLicence(EudlRecognition(EudlCountry.EUDL_COUNTRY_GERMANY))
                    .oldId(GermanyOldIdRecognition())
                    .residencePermit(GenericDocumentDescriptions.residencePermit)
                    .build()),

    HONG_KONG("hk",
            DocumentDescriptionBuilder()
                    .id(HongKongIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
                    .build()),

    INDONESIA("id",
            DocumentDescriptionBuilder()
                    .id(IndonesiaIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                    .build()),

    IRELAND("ie",
            DocumentDescriptionBuilder()
                    .drivingLicence(IrelandDlRecognition())
                    .build()),

    ITALY("it",
            DocumentDescriptionBuilder()
                    .id(GenericRecognition.mrtdId1(), fullySupported = false)
                    .drivingLicence(ItalyDlRecognition())
                    .build()),

    JORDAN("jo",
            DocumentDescriptionBuilder()
                    .newId(JordanIdRecognition())
                    .build()),

    KUWAIT("kw",
            DocumentDescriptionBuilder()
                    .id(KuwaitIdRecognition())
                    .build()),

    MALAYSIA("my",
            DocumentDescriptionBuilder()
                    .id(MalaysiaMyKadRecognition())
                    .drivingLicence(MalaysiaDlRecognition())
                    .passport(GenericRecognition.mrtd())
                    .add(DocumentType.IMMIGRATOR_ID, DocumentDescription(true, R.string.mb_custom_ikad, MalaysiaIkadRecognition()))
                    .add(DocumentType.MILITARY_ID, DocumentDescription(true, R.string.mb_custom_mytentera, MalaysiaTenteraRecognition()))
                    .build()),

    MOROCCO("ma",
            DocumentDescriptionBuilder()
                    .id(MoroccoIdRecognition())
                    .build()),

    NEW_ZEALAND("nz",
            DocumentDescriptionBuilder()
                    .drivingLicence(NewZealandDlRecognition())
                    .build()),

    POLAND("pl",
            DocumentDescriptionBuilder()
                    .id(PolandIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                    .residencePermit(GenericDocumentDescriptions.residencePermit)
                    .build()),

    QATAR("qa",
            DocumentDescriptionBuilder()
                    .id(GenericRecognition.faceId1(), fullySupported = false)
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
                    .build()),

    ROMANIA("ro",
            DocumentDescriptionBuilder()
                    .id(RomaniaIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
                    .build()),

    SERBIA("rs",
            DocumentDescriptionBuilder()
                    .id(SerbiaIdRecognition())
                    .build()),

    SWEDEN("se",
            DocumentDescriptionBuilder()
                    .drivingLicence(SwedenDlRecognition())
                    .build()
    ),

    SINGAPORE("sg",
            DocumentDescriptionBuilder()
                    .id(SingaporeIdRecognition())
                    .drivingLicence(SingaporeDlRecognition())
                    .build()),

    SLOVAKIA("sk",
            DocumentDescriptionBuilder()
                    .id(SlovakiaIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                    .build()),

    SLOVENIA("si",
            DocumentDescriptionBuilder()
                    .id(SloveniaIdRecognition())
                    .drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                    .residencePermit(GenericDocumentDescriptions.residencePermit)
                    .build()),

    SOUTH_AFRICA("za",
            DocumentDescriptionBuilder()
                    .id(GenericRecognition.facePdf417())
                    .drivingLicence(GenericRecognition.facePdf417(), fullySupported = false)
                    .passport(GenericRecognition.mrtd())
                    .visa(DocumentDescription(false, R.string.mb_visa, GenericRecognition.id1()))
                    .build()),

    SPAIN("es",
            DocumentDescriptionBuilder()
                    .drivingLicence(SpainDlRecognition())
                    .build()),

    SWITZERLAND("ch",
            DocumentDescriptionBuilder()
                    .id(SwitzerlandIdRecognition())
                    .drivingLicence(SwitzerlandDlRecognition())
                    .passport(SwitzerlandPassportRecognition())
                    .residencePermit(GenericDocumentDescriptions.residencePermit)
                    .build()),

    UNITED_ARAB_EMIRATES("ae",
            DocumentDescriptionBuilder()
                    .id(UnitedArabEmiratesIdRecognition())
                    .drivingLicence(UnitedArabEmiratesDlRecognition())
                    .build()),

    UNITED_KINGDOM("gb",
            DocumentDescriptionBuilder()
                    .drivingLicence(EudlRecognition(EudlCountry.EUDL_COUNTRY_UK))
                    .residencePermit(GenericDocumentDescriptions.residencePermit)
                    .remove(DocumentType.ID)
                    .build()),

    UNITED_STATES("us",
            DocumentDescriptionBuilder()
                    .id(UsdlRecognition())
                    .drivingLicence(UsdlRecognition())
                    .build())
}