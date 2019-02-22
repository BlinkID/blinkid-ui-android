package com.microblink.documentscanflow.document

import android.content.Context
import android.support.v4.util.ObjectsCompat
import com.microblink.documentscanflow.country.Country

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

    override fun equals(other: Any?): Boolean {
        return other is Document && other.country == this.country && other.documentType == this.documentType
    }

    override fun hashCode() = ObjectsCompat.hash(documentType, country)

}