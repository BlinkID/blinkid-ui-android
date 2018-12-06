package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.recognition.BaseRecognition
import java.util.*

interface Country {

    val code: String
    val documentDescriptions: Map<DocumentType, BaseRecognition>

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
        return documentDescriptions[documentType]
                ?: throw IllegalArgumentException("This county does not support $documentType")
    }

    fun getSupportedDocumentTypes(): List<DocumentType> {
        return documentDescriptions.keys.toList()
    }

}