package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.document.RecognitionsBuilder
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.recognition.BaseRecognition

class GenericCountry(override val code: String) : Country {

    override val recognitionsByDocumentType: Map<DocumentType, BaseRecognition> = RecognitionsBuilder().build()

}