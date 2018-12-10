package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.document.*
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.implementations.*
import com.microblink.entities.recognizers.blinkid.eudl.EudlCountry

enum class SupportedCountry(override val code: String, override val recognitionsByDocumentType: Map<DocumentType, BaseRecognition>)
    : Country {

    AUSTRALIA("au",
            recognitions {
                id = UNSUPPORTED
            }),

    AUSTRIA("at",
            recognitions {
                id = AustriaIdRecognition()
                drivingLicence = AustriaDlRecognition()
                passport = AustriaPassportRecognition()
            }),

    CANADA("ca",
            recognitions {
                id = UsdlRecognition()
                drivingLicence = UsdlRecognition()
            }),

    COLOMBIA("co",
            recognitions {
                id = ColombiaIdRecognition()
                drivingLicence = ColombiaDlRecognition()
            }),

    CROATIA("hr",
            recognitions {
                id = CroatiaIdRecognition()
                drivingLicence = GenericRecognition.faceId1(true)
                residencePermit = GenericRecognition.residencePermit
            }),

    CYPRUS("cy",
            recognitions {
                id = CyprusIdRecognition()
            }),

    CZECHIA("cz",
            recognitions {
                id = CzechIdRecognition()
                drivingLicence = GenericRecognition.faceId1(true)
                oldId = GenericRecognition.mrtdId2Vertical(true)
            }),

    EGYPT("eg",
            recognitions {
                id = EgyptIdRecognition()
            }),

    FRANCE("fr",
            recognitions {
                id = GenericRecognition.mrtdId1(false)
                residencePermit = GenericRecognition.residencePermit
            }),

    GERMANY("de",
            recognitions {
                id = GermanyIdRecognition()
                drivingLicence = GermanyDlRecognition()
                oldId = GermanyOldIdRecognition()
                residencePermit = GenericRecognition.residencePermit
            }),

    HONG_KONG("hk",
            recognitions {
                id = HongKongIdRecognition()
            }),

    INDONESIA("id",
            recognitions {
                id = IndonesiaIdRecognition()
                drivingLicence = GenericRecognition.faceId1(true)
            }),

    IRELAND("ie",
            recognitions {
                drivingLicence = IrelandDlRecognition()
            }),

    ITALY("it",
            recognitions {
                id = GenericRecognition.mrtdId1(false)
                drivingLicence = ItalyDlRecognition()
            }),

    JORDAN("jo",
            recognitions {
                newId = JordanIdRecognition()
            }),

    KUWAIT("kw",
            recognitions {
                id = KuwaitIdRecognition()
            }),

    MALAYSIA("my",
            recognitions {
                id = MalaysiaMyKadRecognition()
                drivingLicence = MalaysiaDlRecognition()
                passport = GenericRecognition.mrtd(true)
                immigratorId = MalaysiaIkadRecognition()
                militaryId = MalaysiaTenteraRecognition()
            }),

    MEXICO("mx",
            recognitions {
                id = MexicoIdRecognition()
            }),

    MOROCCO("ma",
            recognitions {
                id = MoroccoIdRecognition()
            }),

    NEW_ZEALAND("nz",
            recognitions {
                drivingLicence = NewZealandDlRecognition()
            }),

    NIGERIA(
            "ng",
            recognitions {
                drivingLicence = UsdlRecognition()
            }
    ),

    POLAND("pl",
            recognitions {
                id = PolandIdRecognition()
                drivingLicence = GenericRecognition.faceId1(true)
                residencePermit = GenericRecognition.residencePermit
            }),

    QATAR("qa",
            recognitions {
                id = GenericRecognition.faceId1(false)
            }),

    ROMANIA("ro",
            recognitions {
                id = RomaniaIdRecognition()
            }),

    SERBIA("rs",
            recognitions {
                id = SerbiaIdRecognition()
            }),

    SWEDEN("se",
            recognitions {
                drivingLicence = SwedenDlRecognition()
            }),

    SINGAPORE("sg",
            recognitions {
                id = SingaporeIdRecognition()
                drivingLicence = SingaporeDlRecognition()
            }),

    SLOVAKIA("sk",
            recognitions {
                id = SlovakiaIdRecognition()
                drivingLicence = GenericRecognition.faceId1(true)
            }),

    SLOVENIA("si",
            recognitions {
                id = SloveniaIdRecognition()
                drivingLicence = GenericRecognition.faceId1(true)
                residencePermit = GenericRecognition.residencePermit
            }),

    SOUTH_AFRICA("za",
            recognitions {
                id = GenericRecognition.facePdf417(true)
                drivingLicence = GenericRecognition.facePdf417(false)
                passport =GenericRecognition.mrtd(true)
                visa = GenericRecognition.id1(false)
            }),

    SPAIN("es",
            recognitions {
                drivingLicence = SpainDlRecognition()
            }),

    SWITZERLAND("ch",
            recognitions {
                id = SwitzerlandIdRecognition()
                drivingLicence = SwitzerlandDlRecognition()
                passport = SwitzerlandPassportRecognition()
                residencePermit = GenericRecognition.residencePermit
            }),

    UNITED_ARAB_EMIRATES("ae",
            recognitions {
                id = UnitedArabEmiratesIdRecognition()
                drivingLicence = UnitedArabEmiratesDlRecognition()
            }),

    UNITED_KINGDOM("gb",
            recognitions {
                drivingLicence = EudlRecognition(EudlCountry.EUDL_COUNTRY_UK)
                residencePermit = GenericRecognition.residencePermit
                id = UNSUPPORTED
            }),

    UNITED_STATES("us",
            recognitions {
                id = UsdlRecognition()
                drivingLicence = UsdlRecognition()
            })
}

private val UNSUPPORTED = null

private fun recognitions(init: RecognitionsBuilder.() -> Unit): Map<DocumentType, BaseRecognition> {
    val recognitionsBuilder = RecognitionsBuilder()
    recognitionsBuilder.init()
    return recognitionsBuilder.build()
}