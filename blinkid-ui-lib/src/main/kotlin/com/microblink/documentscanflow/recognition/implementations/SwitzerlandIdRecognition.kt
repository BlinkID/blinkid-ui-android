package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandIdFrontRecognizer

class SwitzerlandIdRecognition: TwoSideRecognition<SwitzerlandIdFrontRecognizer.Result, SwitzerlandIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { SwitzerlandIdFrontRecognizer() }
    override val backRecognizer by lazy { SwitzerlandIdBackRecognizer() }

    override fun createValidator() = ResultValidator().match(frontResult.dateOfBirth.date, backResult.mrzResult.dateOfBirth.date)

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }
    
    private fun extractFrontSide() {
        add(LAST_NAME, frontResult.surname)
        add(FIRST_NAME, frontResult.givenName)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
    }
    
    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(PLACE_OF_ORIGIN, backResult.placeOfOrigin)
        add(AUTHORITY, backResult.authority)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
    }
    
    override fun getResultTitle(): String? {
        var firstName: String? = ""
        var lastName: String? = ""
        when {
            isTwoSideScan(frontResult, backResult) -> {
                val stringCombiner = StringCombiner(StringCombiner.Country.SWITZERLAND)
                firstName = stringCombiner.combineMRZString(backResult.mrzResult.secondaryId, frontResult.givenName)
                lastName = stringCombiner.combineMRZString(backResult.mrzResult.primaryId, frontResult.surname)
            }
            frontResult.isNotEmpty() -> {
                firstName = frontResult.givenName
                lastName = frontResult.surname
            }
            backResult.isNotEmpty() -> {
                firstName = backResult.mrzResult.secondaryId
                lastName = backResult.mrzResult.primaryId
            }
        }
        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

    private fun isTwoSideScan(frontResult: Recognizer.Result, backResult: Recognizer.Result) =
        frontResult.isNotEmpty() && backResult.isNotEmpty()

}