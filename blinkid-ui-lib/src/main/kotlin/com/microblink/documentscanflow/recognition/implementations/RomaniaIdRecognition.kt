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
        recognizer.setExtractLastName(false)
        recognizer.setExtractIssuedBy(false)
        recognizer.setExtractPlaceOfBirth(false)
        recognizer.setExtractValidFrom(false)
        recognizer.setExtractValidUntil(false)
        recognizer.setExtractNonMRZSex(false)
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        extractMrtdResult(result)
        return buildMrtdTitle(result)
    }

}