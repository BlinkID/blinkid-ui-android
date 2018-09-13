package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.slovakia.SlovakiaIdFrontRecognizer

class SlovakiaIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { SlovakiaIdFrontRecognizer() }
    val backRecognizer by lazy { SlovakiaIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { SlovakiaCombinedRecognizer() }
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
        extractFrontSide()
        extractBackSide()
    }

    private fun extractCombinedResult() {
        add(R.string.keyLastName, combinedResult.lastName)
        add(R.string.keyFirstName, combinedResult.firstName)
        add(R.string.keyNationality, combinedResult.nationality)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDocumentNumber, combinedResult.documentNumber)
        add(R.string.keySpecialRemarks, combinedResult.specialRemarks)
        add(R.string.keyPersonalNumber, combinedResult.personalIdentificationNumber)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyPlaceOfBirth, combinedResult.placeOfBirth)
        add(R.string.keySurnameAtBirth, combinedResult.surnameAtBirth)
        add(R.string.keyAddress, combinedResult.address)
        add(R.string.keyIssuingAuthority, combinedResult.issuingAuthority)
        add(R.string.keyIssueDate, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry)
    }

    private fun extractFrontSide() {
        if (frontResult.isEmpty()) {
            return
        }

        add(R.string.keyLastName, frontResult.lastName)
        add(R.string.keyFirstName, frontResult.firstName)
        add(R.string.keyNationality, frontResult.nationality)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
        add(R.string.keyPersonalNumber, frontResult.personalNumber)
        add(R.string.keyDocumentNumber, frontResult.documentNumber)
        add(R.string.keyIssuedBy, frontResult.issuedBy)
        addDateOfExpiry(frontResult.dateOfExpiry)
        add(R.string.keyIssueDate, frontResult.dateOfIssue)
    }

    private fun extractBackSide() {
        if (backResult.isEmpty()) {
            return
        }

        add(R.string.keyAddress, backResult.address)
        add(R.string.keySurnameAtBirth, backResult.surnameAtBirth)
        add(R.string.keyPlaceOfBirth, backResult.placeOfBirth)
        add(R.string.keySpecialRemarks, backResult.specialRemarks)
        extractMrtdResult(backResult)
    }
    
    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
        }
        if (frontResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(frontResult.firstName, frontResult.lastName)
        }
        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult)
        }
        return null
    }

}