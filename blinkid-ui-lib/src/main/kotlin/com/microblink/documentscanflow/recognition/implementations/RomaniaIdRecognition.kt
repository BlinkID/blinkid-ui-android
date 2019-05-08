package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.romania.RomaniaIdFrontRecognizer

class RomaniaIdRecognition: BaseRecognition() {

    val recognizer by lazy { RomaniaIdFrontRecognizer() }

    override fun setupRecognizers() {
        recognizer.setExtractAddress(false)
        recognizer.setExtractFirstName(false)
        recognizer.setExtractSurname(false)
        recognizer.setExtractIssuedBy(false)
        recognizer.setExtractPlaceOfBirth(false)
        recognizer.setExtractDateOfIssue(false)
        recognizer.setExtractDateOfExpiry(false)
        recognizer.setExtractSex(false)
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        extractMrzResult(result.mrzResult)
        return buildMrtdTitle(result.mrzResult)
    }

}