package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.belgium.BelgiumCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.belgium.BelgiumIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.belgium.BelgiumIdFrontRecognizer

class BelgiumIdRecognition
    : CombinedRecognition<BelgiumIdFrontRecognizer.Result, BelgiumIdBackRecognizer.Result, BelgiumCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { BelgiumIdFrontRecognizer() }
    override val backRecognizer by lazy { BelgiumIdBackRecognizer() }
    override val combinedRecognizer by lazy { BelgiumCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: BelgiumCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.lastName)
        add(FIRST_NAME, combinedResult.firstName)
        add(DOCUMENT_NUMBER, combinedResult.cardNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(ISSUER, combinedResult.issuedBy)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)

        return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
    }

    override fun extractFrontResult(frontResult: BelgiumIdFrontRecognizer.Result): String? {
        add(DOCUMENT_NUMBER, frontResult.cardNumber)
        return null
    }

    override fun extractBackResult(backResult: BelgiumIdBackRecognizer.Result): String? {
        extract(backResult.mrzResult)
        return backResult.mrzResult.buildTitle()
    }

}