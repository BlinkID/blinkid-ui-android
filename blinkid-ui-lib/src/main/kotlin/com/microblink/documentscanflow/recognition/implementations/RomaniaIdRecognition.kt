package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.recognition.SingleSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.entities.recognizers.blinkid.romania.RomaniaIdFrontRecognizer

class RomaniaIdRecognition: SingleSideRecognition<RomaniaIdFrontRecognizer.Result>() {

    override val recognizer by lazy { RomaniaIdFrontRecognizer() }

    override fun setupRecognizers() {
        recognizer.apply {
            setExtractAddress(false)
            setExtractFirstName(false)
            setExtractSurname(false)
            setExtractIssuedBy(false)
            setExtractPlaceOfBirth(false)
            setExtractDateOfIssue(false)
            setExtractDateOfExpiry(false)
            setExtractSex(false)
        }
    }

    override fun extractData(result: RomaniaIdFrontRecognizer.Result): String? {
        extract(result.mrzResult)
        return result.mrzResult.buildTitle()
    }

}