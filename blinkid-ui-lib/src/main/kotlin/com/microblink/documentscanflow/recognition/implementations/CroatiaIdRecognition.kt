package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaIdFrontRecognizer

class CroatiaIdRecognition
    : CombinedRecognition<CroatiaIdFrontRecognizer.Result, CroatiaIdBackRecognizer.Result, CroatiaCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { CroatiaIdFrontRecognizer() }
    override val backRecognizer by lazy { CroatiaIdBackRecognizer() }
    override val combinedRecognizer by lazy { CroatiaCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: CroatiaCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.lastName)
        add(FIRST_NAME, combinedResult.firstName)
        add(PERSONAL_NUMBER, combinedResult.oib)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(ADDRESS, combinedResult.residence)
        add(CITIZENSHIP, combinedResult.citizenship)
        add(ISSUING_AUTHORITY, combinedResult.issuedBy)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)

        return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
    }

    override fun extractFrontResult(frontResult: CroatiaIdFrontRecognizer.Result): String? {
        add(LAST_NAME, frontResult.lastName)
        add(FIRST_NAME, frontResult.firstName)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(SEX, frontResult.sex)
        add(CITIZENSHIP, frontResult.citizenship)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        addDateOfExpiry(frontResult.dateOfExpiry.date)

        return FormattingUtils.formatResultTitle(frontResult.firstName, frontResult.lastName)
    }

    override fun extractBackResult(backResult: CroatiaIdBackRecognizer.Result): String? {
        add(ADDRESS, backResult.residence)
        add(ISSUING_AUTHORITY, backResult.issuedBy)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
        extractMrzResult(backResult.mrzResult)

        return buildMrtdTitle(backResult.mrzResult)
    }

}