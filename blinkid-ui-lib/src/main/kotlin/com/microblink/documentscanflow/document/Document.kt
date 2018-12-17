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

    private fun getDocumentNameStringId() = country.getDocumentNameStringId(documentType)

}