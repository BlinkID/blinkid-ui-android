package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.document.DocumentDescriptionBuilder
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.document.DocumentDescription

class GenericCountry(override val code: String) : Country {

    override val documentDescriptions: Map<DocumentType, DocumentDescription> = DocumentDescriptionBuilder().build()

}