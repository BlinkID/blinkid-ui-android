package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.document.*
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.implementations.*
import com.microblink.entities.recognizers.blinkid.eudl.EudlCountry

enum class SupportedCountry(override val code: String,
                            override val recognitionsByDocumentType: Map<DocumentType, BaseRecognition>,
                            override val documentNameOverrides: Map<DocumentType, Int> = LinkedHashMap(),
                            override val documentPriorityOverride: Array<DocumentType> = emptyArray())
    : Country {

    AUSTRALIA("au",
            recognitions {
                id = UNSUPPORTED
                victoriaDl = VictoriaDlRecognition()
            }),

    AUSTRIA("at",
            recognitions {
                id = AustriaIdRecognition()
                drivingLicence = AustriaDlRecognition()
                passport = AustriaPassportRecognition()
            }),

    BRUNEI("bn",
            recognitions {
                id = BruneiIdRecognition()
                residencePermit = BruneiResidencePermitRecognition()
                temporaryResidencePermit = BruneiTemporaryResidencePermitRecognition()
                militaryId = BruneiMilitaryIdRecognition()
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
                oldId = CyprusOldIdRecognition()
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
                oldId = GenericRecognition.mrtdId1(false)
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
                temporaryResidentId = MalaysiaKasRecognition()
                permanentResidentId = MalaysiaPrRecognition()
            },
            documentNameOverrides =  mapOf(
                    DocumentType.ID to R.string.mb_custom_mykad,
                    DocumentType.IMMIGRATOR_ID to R.string.mb_custom_ikad,
                    DocumentType.MILITARY_ID to R.string.mb_custom_mytentera,
                    DocumentType.TEMPORARY_RESIDENT_ID to R.string.mb_custom_mykas,
                    DocumentType.PERMANENT_RESIDENT_ID to R.string.mb_custom_mypr
            )),

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
                drivingLicence = NigeriaDlRecognition()
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
    SWEDEN("se",
            recognitions {
                drivingLicence = SwedenDlRecognition()
            }),

    SINGAPORE("sg",
            recognitions {
                id = SingaporeIdRecognition()
                workPass = GenericRecognition.faceId1(false)
                drivingLicence = SingaporeDlRecognition()
            },
            documentNameOverrides =  mapOf(
                    DocumentType.ID to R.string.mb_custom_id_blue_pink
            ),
            documentPriorityOverride = arrayOf(
                    DocumentType.ID,
                    DocumentType.WORK_PASS
            )),

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