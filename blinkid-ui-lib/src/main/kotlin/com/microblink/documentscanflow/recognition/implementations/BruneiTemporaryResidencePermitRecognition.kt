package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.brunei.*
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer

class BruneiTemporaryResidencePermitRecognition: BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { BruneiTemporaryResidencePermitFrontRecognizer() }
    private val backRecognizer by lazy { BruneiTemporaryResidencePermitBackRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }

    override fun createValidator() = ResultValidator()

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFront(frontResult)
        }
        if (backResult.isNotEmpty()) {
            extractBack(backResult)
        }
    }

    private fun extractFront(result: BruneiTemporaryResidencePermitFrontRecognizer.Result) {
        add(R.string.keyFullName, result.fullName)
        add(R.string.keyDocumentNumber, result.documentNumber)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyPlaceOfBirth, result.placeOfBirth)
        add(R.string.keySex, result.sex)
        add(R.string.keyAddress, result.address)
    }

    private fun extractBack(result: BruneiTemporaryResidencePermitBackRecognizer.Result) {
        extractMrzResult(result.mrzResult)
        add(R.string.keyIssueDate, result.dateOfIssue)
        add(R.string.keyPassportNumber, result.passportNumber)
        add(R.string.keyEmployerAddress, result.address)
    }

    override fun getResultTitle(): String? {
        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }
        if (frontResult.isNotEmpty()) {
            return frontResult.fullName
        }
        return null
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> = listOf(frontRecognizer, backRecognizer)


}