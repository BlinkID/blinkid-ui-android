package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.czechia.CzechiaIdFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*

class CzechIdRecognition : CombinedRecognition<CzechiaIdFrontRecognizer.Result, CzechiaIdBackRecognizer.Result, CzechiaCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { CzechiaIdFrontRecognizer() }
    override val backRecognizer by lazy { CzechiaIdBackRecognizer() }
    override val combinedRecognizer by lazy { CzechiaCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: CzechiaCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.surname)
        add(FIRST_NAME, combinedResult.givenNames)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(ADDRESS, combinedResult.permanentStay)
        add(PERSONAL_NUMBER, combinedResult.personalNumber)
        add(AUTHORITY, combinedResult.authority)
        add(NATIONALITY, combinedResult.nationality)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        return FormattingUtils.formatResultTitle(combinedResult.givenNames, combinedResult.surname)
    }

    override fun extractFrontResult(frontResult: CzechiaIdFrontRecognizer.Result): String? {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(DOCUMENT_NUMBER, frontResult.documentNumber)
        add(SEX, frontResult.sex)
        add(PLACE_OF_BIRTH, frontResult.placeOfBirth)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(DATE_OF_ISSUE, frontResult.dateOfIssue)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
        return FormattingUtils.formatResultTitle(frontResult.givenNames, frontResult.surname)
    }

    override fun extractBackResult(backResult: CzechiaIdBackRecognizer.Result): String? {
        extractMrzResult(backResult.mrzResult)
        add(ADDRESS, backResult.permanentStay)
        add(PERSONAL_NUMBER, backResult.personalNumber)
        add(ISSUING_AUTHORITY, backResult.authority)
        return buildMrtdTitle(backResult.mrzResult)
    }
    
}