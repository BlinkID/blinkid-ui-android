package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.croatia.CroatiaIdFrontRecognizer

class CroatiaIdRecognition
    : BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { CroatiaIdFrontRecognizer() }
    private val backRecognizer by lazy { CroatiaIdBackRecognizer() }
    private val combinedRecognizer by lazy { CroatiaCombinedRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }
    private val combinedResult by lazy { combinedRecognizer.result }

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
            extractCombinedResult(combinedResult)
        }

        if (backResult.isNotEmpty()) {
            extractBackSide(backResult)
        }

        if (frontResult.isNotEmpty()) {
            extractFrontSide(frontResult)
        }
    }

    private fun extractCombinedResult(combinedResult: CroatiaCombinedRecognizer.Result) {
        add(R.string.keyLastName, combinedResult.lastName)
        add(R.string.keyFirstName, combinedResult.firstName)
        add(R.string.keyPersonalNumber, combinedResult.personalIdentificationNumber)
        add(R.string.keyDocumentNumber, combinedResult.identityCardNumber)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keyAddress, combinedResult.address)
        add(R.string.keyCitizenship, combinedResult.citizenship)
        add(R.string.keyIssuingAuthority, combinedResult.issuingAuthority)
        add(R.string.keyIssueDate, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry)
    }

    private fun extractFrontSide(frontResult: CroatiaIdFrontRecognizer.Result) {
        add(R.string.keyLastName, frontResult.lastName)
        add(R.string.keyFirstName, frontResult.firstName)
        add(R.string.keyDocumentNumber, frontResult.documentNumber)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyCitizenship, frontResult.citizenship)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
        addDateOfExpiry(frontResult.dateOfExpiry.date)
    }

    private fun extractBackSide(backResult: CroatiaIdBackRecognizer.Result) {
        add(R.string.keyAddress, backResult.residence)
        add(R.string.keyIssuingAuthority, backResult.issuedBy)
        add(R.string.keyIssueDate, backResult.dateOfIssue)
        extractMrzResult(backResult.mrzResult)
    }

    override fun getResultTitle(): String? {
        if (combinedResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
        }

        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }

        if (frontResult.isNotEmpty()) {
            return FormattingUtils.formatResultTitle(frontResult.firstName, frontResult.lastName)
        }

        return null
    }

}