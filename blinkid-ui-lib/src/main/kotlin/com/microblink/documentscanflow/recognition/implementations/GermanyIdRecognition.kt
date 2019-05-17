package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.germany.GermanyCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyIdFrontRecognizer

class GermanyIdRecognition : CombinedRecognition<GermanyIdFrontRecognizer.Result, GermanyIdBackRecognizer.Result, GermanyCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { GermanyIdFrontRecognizer() }
    override val backRecognizer by lazy { GermanyIdBackRecognizer() }
    override val combRecognizer by lazy { GermanyCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: GermanyCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.surname)
        add(FIRST_NAME, combinedResult.givenNames)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(EYE_COLOR, combinedResult.colourOfEyes)
        add(HEIGHT, combinedResult.height)
        add(ADDRESS, combinedResult.address)
        add(AUTHORITY, combinedResult.authority)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        return FormattingUtils.formatResultTitle(combinedResult.givenNames, combinedResult.surname)
    }

    override fun extractFrontResult(frontResult: GermanyIdFrontRecognizer.Result): String? {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(NATIONALITY, frontResult.nationality)
        add(PLACE_OF_BIRTH, frontResult.placeOfBirth)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
        return FormattingUtils.formatResultTitle(frontResult.givenNames, frontResult.surname)
    }

    override fun extractBackResult(backResult: GermanyIdBackRecognizer.Result): String? {
        extractMrzResult(backResult.mrzResult)
        add(AUTHORITY, backResult.authority)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
        add(ADDRESS, backResult.fullAddress)
        return buildMrtdTitle(backResult.mrzResult)
    }
    
}