package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.document.*
import com.microblink.documentscanflow.recognition.implementations.*
import com.microblink.entities.recognizers.blinkid.eudl.EudlCountry

enum class SupportedCountry(override val code: String, override val documentDescriptions: Map<DocumentType, DocumentDescription>)
    : Country {

    AUSTRALIA("au",
            documentDescriptions {
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
                remove(DocumentType.ID)
            }),

    AUSTRIA("at",
            documentDescriptions {
                id(AustriaIdRecognition())
                drivingLicence(AustriaDlRecognition())
                passport(AustriaPassportRecognition())
            }),

    CANADA("ca",
            documentDescriptions {
                id(UsdlRecognition())
                drivingLicence(UsdlRecognition())
            }),

    COLOMBIA("co",
            documentDescriptions {
                id(ColombiaIdRecognition())
                drivingLicence(ColombiaDlRecognition())
            }),
    CROATIA("hr",
            documentDescriptions {
                id(CroatiaIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                residencePermit(GenericDocumentDescriptions.residencePermit)
            }),

    CYPRUS("cy",
            documentDescriptions {
                id(CyprusIdRecognition())
            }),

    CZECHIA("cz",
            documentDescriptions {
                id(CzechIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                oldId(GenericRecognition.mrtdId2Vertical())
            }),

    EGYPT("eg",
            documentDescriptions {
                id(EgyptIdRecognition())
            }),

    FRANCE("fr",
            documentDescriptions {
                id(GenericRecognition.mrtdId1(), fullySupported = false)
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
                residencePermit(GenericDocumentDescriptions.residencePermit)
            }),

    GERMANY("de",
            documentDescriptions {
                id(GermanyIdRecognition())
                drivingLicence(GermanyDlRecognition())
                oldId(GermanyOldIdRecognition())
                residencePermit(GenericDocumentDescriptions.residencePermit)
            }),

    HONG_KONG("hk",
            documentDescriptions {
                id(HongKongIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
            }),

    INDONESIA("id",
            documentDescriptions {
                id(IndonesiaIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
            }),

    IRELAND("ie",
            documentDescriptions {
                drivingLicence(IrelandDlRecognition())
            }),

    ITALY("it",
            documentDescriptions {
                id(GenericRecognition.mrtdId1(), fullySupported = false)
                drivingLicence(ItalyDlRecognition())
            }),

    JORDAN("jo",
            documentDescriptions {
                newId(JordanIdRecognition())
            }),

    KUWAIT("kw",
            documentDescriptions {
                id(KuwaitIdRecognition())
            }),

    MALAYSIA("my",
            documentDescriptions {
                id(MalaysiaMyKadRecognition())
                drivingLicence(MalaysiaDlRecognition())
                passport(GenericRecognition.mrtd())
                add(DocumentType.IMMIGRATOR_ID, DocumentDescription(true, R.string.mb_custom_ikad, MalaysiaIkadRecognition()))
                add(DocumentType.MILITARY_ID, DocumentDescription(true, R.string.mb_custom_mytentera, MalaysiaTenteraRecognition()))
            }),

    MEXICO("mx",
            documentDescriptions {
                id(MexicoIdRecognition())
            }),

    MOROCCO("ma",
            documentDescriptions {
                id(MoroccoIdRecognition())
            }),

    NEW_ZEALAND("nz",
            documentDescriptions {
                drivingLicence(NewZealandDlRecognition())
            }),

    POLAND("pl",
            documentDescriptions {
                id(PolandIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                residencePermit(GenericDocumentDescriptions.residencePermit)
            }),

    QATAR("qa",
            documentDescriptions {
                id(GenericRecognition.faceId1(), fullySupported = false)
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
            }),

    ROMANIA("ro",
            documentDescriptions {
                id(RomaniaIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatUnsupported)
            }),

    SERBIA("rs",
            documentDescriptions {
                id(SerbiaIdRecognition())
            }),

    SWEDEN("se",
            documentDescriptions {
                drivingLicence(SwedenDlRecognition())
            }),

    SINGAPORE("sg",
            documentDescriptions {
                id(SingaporeIdRecognition())
                drivingLicence(SingaporeDlRecognition())
            }),

    SLOVAKIA("sk",
            documentDescriptions {
                id(SlovakiaIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
            }),

    SLOVENIA("si",
            documentDescriptions {
                id(SloveniaIdRecognition())
                drivingLicence(GenericDocumentDescriptions.drivingLicenceId1FormatSupported)
                residencePermit(GenericDocumentDescriptions.residencePermit)
            }),

    SOUTH_AFRICA("za",
            documentDescriptions {
                id(GenericRecognition.facePdf417())
                drivingLicence(GenericRecognition.facePdf417(), fullySupported = false)
                passport(GenericRecognition.mrtd())
                visa(DocumentDescription(false, R.string.mb_visa, GenericRecognition.id1()))
            }),

    SPAIN("es",
            documentDescriptions {
                drivingLicence(SpainDlRecognition())
            }),

    SWITZERLAND("ch",
            documentDescriptions {
                id(SwitzerlandIdRecognition())
                drivingLicence(SwitzerlandDlRecognition())
                passport(SwitzerlandPassportRecognition())
                residencePermit(GenericDocumentDescriptions.residencePermit)
            }),

    UNITED_ARAB_EMIRATES("ae",
            documentDescriptions {
                id(UnitedArabEmiratesIdRecognition())
                drivingLicence(UnitedArabEmiratesDlRecognition())
            }),

    UNITED_KINGDOM("gb",
            documentDescriptions {
                drivingLicence(EudlRecognition(EudlCountry.EUDL_COUNTRY_UK))
                residencePermit(GenericDocumentDescriptions.residencePermit)
                remove(DocumentType.ID)
            }),

    UNITED_STATES("us",
            documentDescriptions {
                id(UsdlRecognition())
                drivingLicence(UsdlRecognition())
            })
}