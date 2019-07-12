package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaIdFrontRecognizer

class SloveniaIdRecognition: CombinedRecognition<SloveniaIdFrontRecognizer.Result, SloveniaIdBackRecognizer.Result, SloveniaCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { SloveniaIdFrontRecognizer() }
    override val backRecognizer by lazy { SloveniaIdBackRecognizer() }
    override val combinedRecognizer by lazy { SloveniaCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: SloveniaCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.surname)
        add(FIRST_NAME, combinedResult.givenNames)
        add(IDENTITY_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(ADDRESS, combinedResult.address)
        add(NATIONALITY, combinedResult.nationality)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        add(ISSUING_AUTHORITY, combinedResult.administrativeUnit)
        return FormattingUtils.formatResultTitle(combinedResult.givenNames, combinedResult.surname)
    }

    override fun extractFrontResult(frontResult: SloveniaIdFrontRecognizer.Result): String? {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(SEX, frontResult.sex)
        add(NATIONALITY, frontResult.nationality)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
        return FormattingUtils.formatResultTitle(frontResult.givenNames, frontResult.surname)
    }

    override fun extractBackResult(backResult: SloveniaIdBackRecognizer.Result): String? {
        extractMrzResult(backResult.mrzResult)
        add(ADDRESS, backResult.address)
        add(AUTHORITY, backResult.administrativeUnit)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
        return backResult.mrzResult.buildTitle()
    }

}