package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.poland.PolandIdFrontRecognizer

class PolandIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { PolandIdFrontRecognizer() }
    val backRecognizer by lazy { PolandIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { PolandCombinedRecognizer() }
    val combinedResult by lazy { combinedRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun getCombinedRecognizer(): Recognizer<*, *>? {
        return combinedRecognizer
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator().match(combinedResult)
    }

    override fun extractFields() {
        if (combinedResult.isNotEmpty()) {
            extractCombinedResult()
        }
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }

    private fun extractCombinedResult() {
        add(R.string.keyLastName, combinedResult.surname)
        add(R.string.keyFirstName, combinedResult.givenNames)
        add(R.string.keyFamilyName, combinedResult.familyName)
        add(R.string.keyParentNames, combinedResult.parentsGivenNames)
        add(R.string.keyPersonalNumber, combinedResult.personalNumber)
        add(R.string.keyDocumentNumber, combinedResult.documentNumber)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyNationality, combinedResult.nationality)
        add(R.string.keyIssuer, combinedResult.issuer)
        addDateOfExpiry(combinedResult.dateOfExpiry)
    }

    private fun extractFrontSide() {
        add(R.string.keyLastName, frontResult.surname)
        add(R.string.keyFirstName, frontResult.givenNames)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
    }

    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
    }

    override fun getResultTitle(): String? {
        var firstName: String? = ""
        var lastName: String? = ""
        when {
            combinedResult.isNotEmpty() -> {
                firstName = combinedResult.givenNames
                lastName = combinedResult.surname
            }
            frontResult.isNotEmpty() -> {
                firstName = frontResult.givenNames
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