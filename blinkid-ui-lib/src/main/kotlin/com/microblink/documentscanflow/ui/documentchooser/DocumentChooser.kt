package com.microblink.documentscanflow.ui.documentchooser

import com.microblink.documentscanflow.country.Country
import com.microblink.documentscanflow.document.Document
import com.microblink.documentscanflow.document.DocumentType


interface DocumentChooser {

    fun shouldShowCountryChooser() : Boolean

    fun shouldShowDocumentTypeTabs() : Boolean

    fun getDefaultDocumentTypeForCountry(country: Country) : DocumentType

    fun geChooseCountryLabel(): String

    fun onChooseCountryClick(currentDocument : Document)

    fun getAllowedDocumentTypes(country: Country): List<DocumentType>

}