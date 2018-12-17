package com.microblink.documentscanflow.document

import android.content.Context
import android.support.annotation.StringRes
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.country.Country
import com.microblink.documentscanflow.country.SupportedCountry

class Document(val country: Country, val documentType: DocumentType) {

    init {
        if (documentType !in country.getSupportedDocumentTypes()) {
            throw IllegalArgumentException("${country.getLocalisedName()} does not support $documentType")
        }
    }

    fun isFullySupported() = getRecognition().isFullySupported

    fun getRecognition() = country.getRecognition(documentType)

    fun getTitle(context : Context): String {
        return country.getLocalisedName() + ", " + context.getString(getDocumentNameStringId())
    }

    private fun getDocumentNameStringId(): Int {
        return getDocumentNameStringId(country, documentType)
    }

    companion object {
        fun getDocumentNameStringId(country: Country, documentType: DocumentType): Int {
            return when(documentType) {
                DocumentType.ID -> R.string.mb_id_card
                DocumentType.OLD_ID -> R.string.mb_old_id_card
                DocumentType.DL -> R.string.mb_driver_license
                DocumentType.PASSPORT -> R.string.mb_passport
                DocumentType.TRAVEL_DOCUMENT_VISA -> R.string.mb_visa
                DocumentType.RESIDENCE_PERMIT -> R.string.mb_residence_permit
                DocumentType.IMMIGRATOR_ID -> {
                    if (country == SupportedCountry.MALAYSIA) {
                        R.string.mb_custom_ikad
                    } else {
                        R.string.mb_immigrator_id
                    }
                }
                DocumentType.MILITARY_ID -> {
                    if (country == SupportedCountry.MALAYSIA) {
                        R.string.mb_custom_mytentera
                    } else {
                        R.string.mb_military_id
                    }
                }
                DocumentType.NEW_ID -> R.string.mb_new_id_card
            }
        }
    }

}