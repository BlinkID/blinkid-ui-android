package com.microblink.documentscanflow.document

import android.content.Context
import com.microblink.documentscanflow.country.Country

class Document(val country: Country, val documentType: DocumentType) {

    init {
        if (documentType !in country.getSupportedDocumentTypes()) {
            throw IllegalArgumentException("${country.getLocalisedName()} does not support $documentType")
        }
    }

    fun isFullySupported() = getDescription().isFullySupported

    fun getRecognition() = getDescription().recognition

    fun getTitle(context : Context): String {
        val documentDescription = getDescription()
        return country.getLocalisedName() + ", " + context.getString(documentDescription.documentNameResourceID)
    }

    private fun getDescription(): DocumentDescription = country.getDocumentDescription(documentType)

}