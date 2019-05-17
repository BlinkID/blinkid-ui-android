package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaIdFrontRecognizer

class SlovakiaIdRecognition:
    CombinedRecognition<SlovakiaIdFrontRecognizer.Result, SlovakiaIdBackRecognizer.Result, SlovakiaCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { SlovakiaIdFrontRecognizer() }
    override val backRecognizer by lazy { SlovakiaIdBackRecognizer() }
    override val combRecognizer by lazy { SlovakiaCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: SlovakiaCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.lastName)
        add(FIRST_NAME, combinedResult.firstName)
        add(NATIONALITY, combinedResult.nationality)
        add(SEX, combinedResult.sex)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SPECIAL_REMARKS, combinedResult.specialRemarks)
        add(PERSONAL_NUMBER, combinedResult.personalNumber)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(SURNAME_AT_BIRTH, combinedResult.surnameAtBirth)
        add(ADDRESS, combinedResult.address)
        add(ISSUING_AUTHORITY, combinedResult.issuedBy)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
    }

    override fun extractFrontResult(frontResult: SlovakiaIdFrontRecognizer.Result): String? {
        add(LAST_NAME, frontResult.lastName)
        add(FIRST_NAME, frontResult.firstName)
        add(NATIONALITY, frontResult.nationality)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(PERSONAL_NUMBER, frontResult.personalNumber)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(ISSUER, frontResult.issuedBy)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
        add(DATE_OF_ISSUE, frontResult.dateOfIssue)
        return FormattingUtils.formatResultTitle(frontResult.firstName, frontResult.lastName)
    }

    override fun extractBackResult(backResult: SlovakiaIdBackRecognizer.Result): String? {
        add(ADDRESS, backResult.address)
        add(PLACE_OF_BIRTH, backResult.placeOfBirth)
        extractMrzResult(backResult.mrzResult)
        return buildMrtdTitle(backResult.mrzResult)
    }

}