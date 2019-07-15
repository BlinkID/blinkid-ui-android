package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.recognition.BaseRecognition
import java.util.*

interface Country {

    val code: String
    val recognitionsByDocumentType: Map<DocumentType, BaseRecognition>
    val documentNameOverrides: Map<DocumentType, Int>

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

    fun getSupportedDocumentTypes() = recognitionsByDocumentType.keys.toList()

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
            DocumentType.WORK_PASS -> R.string.mb_work_pass
            DocumentType.UNDER_21_ID -> R.string.mb_under_21_id
        }
    }

}