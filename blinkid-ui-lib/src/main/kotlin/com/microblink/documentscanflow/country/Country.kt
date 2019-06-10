package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.recognition.BaseRecognition
import java.util.*
import kotlin.Comparator

interface Country {

    val code: String
    val recognitionsByDocumentType: Map<DocumentType, BaseRecognition>
    val documentNameOverrides: Map<DocumentType, Int>
    val documentPriorityOverride: Array<DocumentType>

    /**
     * Returns the name of this country, localized to the current locale (current app language).
     * Returns the empty string if this locale does not correspond to a specific
     * country.
     */
    fun getLocalisedName(): String {
        val locale = Locale("", code)
        return locale.getDisplayCountry(CountryFactory.currentLocale)
    }

    fun getRecognition(documentType: DocumentType): BaseRecognition {
        return recognitionsByDocumentType[documentType]
                ?: throw IllegalArgumentException("This county does not support $documentType")
    }

    fun getSupportedDocumentTypes(): List<DocumentType> {
        if (documentPriorityOverride.isEmpty()) {
            return recognitionsByDocumentType.keys.toList()
        }

        return recognitionsByDocumentType.keys.toList().sortedWith(
                Comparator { documentType1, documentType2 ->
                    // keep in mind that sorting is in ascending order, elements
                    // with higher priority should be at the beginning of sorted list:
                    // if priorities are equal return 0, else if documentType1 has greater priority
                    // return -1, else return 1
                    val index1 = documentPriorityOverride.indexOf(documentType1)
                    val index2 = documentPriorityOverride.indexOf(documentType2)
                    return@Comparator when {
                        index1 == index2 -> 0
                        index1 == -1 -> 1 // document type 1 does not exist in the priority array
                        index2 == -1 -> -1 // document type 2 does not exist in the priority array
                        else -> if (index2 > index1) { // document type on lower index has greater priority
                            -1
                        } else {
                            1
                        }
                    }
                }
        )
    }

    fun getDocumentNameStringId(documentType: DocumentType): Int {
        val overriddenName = documentNameOverrides[documentType]
        if (overriddenName != null) return overriddenName

        return when(documentType) {
            DocumentType.ID -> R.string.mb_id_card
            DocumentType.OLD_ID -> R.string.mb_old_id_card
            DocumentType.DL -> R.string.mb_driver_license
            DocumentType.PASSPORT -> R.string.mb_passport
            DocumentType.TRAVEL_DOCUMENT_VISA -> R.string.mb_visa
            DocumentType.RESIDENCE_PERMIT -> R.string.mb_residence_permit
            DocumentType.TEMPORARY_RESIDENCE_PERMIT -> R.string.mb_temporary_residence_permit
            DocumentType.IMMIGRATOR_ID -> R.string.mb_immigrator_id
            DocumentType.MILITARY_ID -> R.string.mb_military_id
            DocumentType.NEW_ID -> R.string.mb_new_id_card
            DocumentType.TEMPORARY_RESIDENT_ID -> R.string.mb_temporary_resident_id
            DocumentType.PERMANENT_RESIDENT_ID -> R.string.mb_permanent_resident_id
            DocumentType.VICTORIA_DL -> R.string.mb_victoria_dl
            DocumentType.BLUE_ID -> R.string.mb_blue_id_card
            DocumentType.EMPLOYMENT_PASS -> R.string.mb_employment_pass
            DocumentType.S_PASS -> R.string.mb_s_pass

        }
    }

}