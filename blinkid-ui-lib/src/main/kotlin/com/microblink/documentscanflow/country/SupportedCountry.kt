package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.document.RecognitionsBuilder
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.GenericRecognition
import com.microblink.documentscanflow.recognition.implementations.*
import com.microblink.documentscanflow.recognition.implementations.BlinkIdRecognition
import com.microblink.entities.recognizers.blinkid.eudl.EudlCountry

enum class SupportedCountry(override val code: String,
                            override val recognitionsByDocumentType: Map<DocumentType, BaseRecognition>,
                            override val documentNameOverrides: Map<DocumentType, Int> = LinkedHashMap())
    : Country {

    ALBANIA("al",
            recognitions {
                id = BlinkIdRecognition()
            }),

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

    BAHRAIN("bh",
            recognitions {
                id = BlinkIdRecognition()
            }),

    BANGLADESH("bd",
            recognitions {
                id = BlinkIdRecognition()
            }),

    BELGIUM("be",
            recognitions {
                id = BlinkIdRecognition()
            }),

    BOSNIA_AND_HERZEGOVINA("ba",
            recognitions {
                id = BlinkIdRecognition()
            }),

    BRUNEI("bn",
            recognitions {
                id = BlinkIdRecognition()
                residencePermit = BruneiResidencePermitRecognition()
                temporaryResidencePermit = BruneiTemporaryResidencePermitRecognition()
                militaryId = BruneiMilitaryIdRecognition()
            }),

    BULGARIA("bg",
            recognitions {
                id = BlinkIdRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
            }),

    CAMBODIA("kh",
            recognitions {
                id = BlinkIdWithDetectorRecognition()
            }),

    CANADA("ca",
            recognitions {
                id = UsdlRecognition()
                drivingLicence = UsdlRecognition()
            }),

    CHILE("cl",
            recognitions {
                id = BlinkIdRecognition()
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

    DOMINICAN_REPUBLIC("do",
            recognitions {
               id = BlinkIdRecognition()
            }),

    EGYPT("eg",
            recognitions {
                id = EgyptIdRecognition()
            }),

    FRANCE("fr",
            recognitions {
                id = GenericRecognition.mrtdId1(false)
                drivingLicence = BlinkIdWithDetectorRecognition()
                residencePermit = GenericRecognition.residencePermit
            }),

    GEORGIA("ge",
            recognitions {
                id = BlinkIdRecognition()
            }),

    GERMANY("de",
            recognitions {
                id = GermanyIdRecognition()
                drivingLicence = GermanyDlRecognition()
                oldId = GermanyOldIdRecognition()
                residencePermit = BlinkIdRecognition()
            }),

    HONG_KONG("hk",
            recognitions {
                id = HongKongIdRecognition()
            }),

    HUNGARY("hu",
            recognitions {
                id = BlinkIdRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
            }),

    INDONESIA("id",
            recognitions {
                id = BlinkIdWithDetectorRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
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

    KOSOVO("xk",
            recognitions {
                id = BlinkIdRecognition()
            }),

    KUWAIT("kw",
            recognitions {
                id = KuwaitIdRecognition()
            }),

    LITHUANIA("lt",
            recognitions {
                id = BlinkIdRecognition()
            }),

    MALAYSIA("my",
            recognitions {
                id = MalaysiaMyKadRecognition()
                drivingLicence = MalaysiaDlRecognition()
                passport = GenericRecognition.mrtd(true, true)
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

    MALTA("mt",
            recognitions {
                id = BlinkIdRecognition()
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
                drivingLicence = NigeriaDlRecognition()
                voterId = NigeriaVoterIdRecognition()
            }
    ),

    PHILIPPINES("ph",
            recognitions {
                id = BlinkIdWithDetectorRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
            },
            documentNameOverrides = mapOf(
                DocumentType.ID to R.string.mb_multipurpose_id
            )),

    POLAND("pl",
            recognitions {
                id = PolandIdRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
                residencePermit = GenericRecognition.residencePermit
            }),

    PORTUGAL("pt",
            recognitions {
                id = BlinkIdRecognition()
            }),

    QATAR("qa",
            recognitions {
                id = GenericRecognition.faceId1(false)
            }),

    ROMANIA("ro",
            recognitions {
                id = RomaniaIdRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
            }),

    SERBIA("rs",
            recognitions {
                id = BlinkIdRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
            }),

    SINGAPORE("sg",
            recognitions {
                id = SingaporeIdRecognition()
                workPass = BlinkIdWithDetectorRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
            },
            documentNameOverrides =  mapOf(
                    DocumentType.ID to R.string.mb_custom_id_blue_pink
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
                id = BlinkIdRecognition()
                drivingLicence = SpainDlRecognition()
            }),

    SWEDEN("se",
            recognitions {
                id = BlinkIdRecognition()
                drivingLicence = SwedenDlRecognition()
            }),

    SWITZERLAND("ch",
            recognitions {
                id = SwitzerlandIdRecognition()
                drivingLicence = SwitzerlandDlRecognition()
                passport = SwitzerlandPassportRecognition()
                residencePermit = GenericRecognition.residencePermit
            }),

    TURKEY("tr",
            recognitions {
                id = BlinkIdRecognition()
                drivingLicence = BlinkIdWithDetectorRecognition()
            }),

    UNITED_ARAB_EMIRATES("ae",
            recognitions {
                id = UnitedArabEmiratesIdRecognition()
                drivingLicence = UnitedArabEmiratesDlRecognition()
            }),

        
    UNITED_KINGDOM("gb",
            recognitions {
                drivingLicence = EudlRecognition(EudlCountry.EUDL_COUNTRY_UK)
                residencePermit = BlinkIdRecognition()
                id = UNSUPPORTED
            }),

    UNITED_STATES("us",
            recognitions {
                id = UsdlRecognition()
                drivingLicence = UsdlRecognition()
                under21Id = UsdlUnder21Recognition()
            }),

    UKRAINE("ua",
            recognitions {
                id = BlinkIdRecognition()
            })
}

private val UNSUPPORTED = null

private fun recognitions(init: RecognitionsBuilder.() -> Unit): Map<DocumentType, BaseRecognition> {
    val recognitionsBuilder = RecognitionsBuilder()
    recognitionsBuilder.init()
    return recognitionsBuilder.build()
}