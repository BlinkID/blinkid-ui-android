package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.slovenia.SloveniaIdFrontRecognizer

class SloveniaIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { SloveniaIdFrontRecognizer() }
    val backRecognizer by lazy { SloveniaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { SloveniaCombinedRecognizer() }
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
        add(R.string.keyLastName, combinedResult.lastName)
        add(R.string.keyFirstName, combinedResult.firstName)
        add(R.string.keyIdentityNumber, combinedResult.identityCardNumber)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyAddress, combinedResult.address)
        add(R.string.keyCitizenship, combinedResult.citizenship)
        addDateOfExpiry(combinedResult.dateOfExpiry)
        add(R.string.keyIssueDate, combinedResult.dateOfIssue)
        add(R.string.keyIssuingAuthority, combinedResult.issuingAuthority)
    }

    private fun extractFrontSide() {
        add(R.string.keyLastName, frontResult.lastName)
        add(R.string.keyFirstName, frontResult.firstName)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyNationality, frontResult.nationality)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
        addDateOfExpiry(frontResult.dateOfExpiry)
    }

    private fun extractBackSide() {
        extractMrtdResult(backResult)
        add(R.string.keyAddress, backResult.address)
        add(R.string.keyAuthority, backResult.authority)
        add(R.string.keyIssueDate, backResult.dateOfIssue)
    }
    
    override fun getResultTitle(): String? {
        var firstName: String? = ""
        var lastName: String? = ""
        when {
            combinedResult.isNotEmpty() -> {
                firstName = combinedResult.firstName
                lastName = combinedResult.lastName
            }
            frontResult.isNotEmpty() -> {
                firstName = frontResult.firstName
                lastName = frontResult.lastName
            }
            backResult.isNotEmpty() -> {
                firstName = backResult.secondaryId
                lastName = backResult.primaryId
            }
        }
        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}