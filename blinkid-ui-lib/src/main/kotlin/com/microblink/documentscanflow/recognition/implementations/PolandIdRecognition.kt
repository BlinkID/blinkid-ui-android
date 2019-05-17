package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.poland.PolandCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandIdFrontRecognizer

class PolandIdRecognition: CombinedRecognition<PolandIdFrontRecognizer.Result, PolandIdBackRecognizer.Result, PolandCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { PolandIdFrontRecognizer() }
    override val backRecognizer by lazy { PolandIdBackRecognizer() }
    override val combinedRecognizer by lazy { PolandCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: PolandCombinedRecognizer.Result): String? {
        add(LAST_NAME, combinedResult.surname)
        add(FIRST_NAME, combinedResult.givenNames)
        add(FAMILY_NAME, combinedResult.familyName)
        add(PARENT_NAMES, combinedResult.parentsGivenNames)
        add(PERSONAL_NUMBER, combinedResult.personalNumber)
        add(DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(SEX, combinedResult.sex)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(NATIONALITY, combinedResult.nationality)
        add(ISSUER, combinedResult.issuedBy)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)
        return FormattingUtils.formatResultTitle(combinedResult.givenNames, combinedResult.surname)
    }

    override fun extractFrontResult(frontResult: PolandIdFrontRecognizer.Result): String? {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenNames)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        return FormattingUtils.formatResultTitle(frontResult.givenNames, frontResult.surname)
    }

    override fun extractBackResult(backResult: PolandIdBackRecognizer.Result): String? {
        extractMrzResult(backResult.mrzResult)
        return buildMrtdTitle(backResult.mrzResult)
    }

}