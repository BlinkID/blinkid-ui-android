package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandIdFrontRecognizer

class SwitzerlandIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { SwitzerlandIdFrontRecognizer() }
    val backRecognizer by lazy { SwitzerlandIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator().match(frontResult.dateOfBirth.date, backResult.mrzResult.dateOfBirth.date)
    }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }
    
    private fun extractFrontSide() {
        add(R.string.keyLastName, frontResult.surname)
        add(R.string.keyFirstName, frontResult.givenName)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
    }
    
    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(R.string.keyPlaceOfOrigin, backResult.placeOfOrigin)
        add(R.string.keyAuthority, backResult.authority)
        add(R.string.keyIssueDate, backResult.dateOfIssue)
    }
    
    override fun getResultTitle(): String? {
        var firstName: String? = ""
        var lastName: String? = ""
        when {
            isCombinedScan(frontResult, backResult) -> {
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

}