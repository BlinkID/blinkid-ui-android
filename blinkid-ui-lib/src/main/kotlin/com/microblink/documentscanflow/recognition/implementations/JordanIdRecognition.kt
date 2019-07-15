package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.sanitizeMRZString
import com.microblink.entities.recognizers.blinkid.jordan.JordanCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.jordan.JordanIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.jordan.JordanIdFrontRecognizer

class JordanIdRecognition :
    CombinedRecognition<JordanIdFrontRecognizer.Result, JordanIdBackRecognizer.Result, JordanCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { JordanIdFrontRecognizer() }
    override val backRecognizer by lazy { JordanIdBackRecognizer() }
    override val combinedRecognizer by lazy { JordanCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: JordanCombinedRecognizer.Result): String? {
        add(FULL_NAME, combinedResult.name)
        add(NATIONAL_NUMBER, combinedResult.nationalNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(ISSUER, combinedResult.issuedBy)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        return combinedResult.name
    }

    override fun extractFrontResult(frontResult: JordanIdFrontRecognizer.Result): String? {
        add(FULL_NAME, frontResult.name)
        add(NATIONAL_NUMBER, frontResult.nationalNumber)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        return frontResult.name
    }

    override fun extractBackResult(backResult: JordanIdBackRecognizer.Result): String? {
        add(NATIONALITY, backResult.mrzResult.nationality)
        add(DOCUMENT_NUMBER, backResult.mrzResult.documentNumber.sanitizeMRZString())
        add(ISSUER, backResult.mrzResult.issuer)
        addDateOfExpiry(backResult.mrzResult.dateOfExpiry.date)
        extract(backResult.mrzResult)
        return FormattingUtils.formatResultTitle(backResult.mrzResult.primaryId, backResult.mrzResult.secondaryId)
    }

}