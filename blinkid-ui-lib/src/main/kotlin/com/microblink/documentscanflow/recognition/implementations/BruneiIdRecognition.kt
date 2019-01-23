package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.brunei.BruneiIdFrontRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer

class BruneiIdRecognition: BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { BruneiIdFrontRecognizer() }
    private val backRecognizer by lazy { BruneiIdBackRecognizer() }

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

    private fun extractFront(result: BruneiIdFrontRecognizer.Result) {
        add(R.string.keyFullName, result.fullName)
        add(R.string.keyDocumentNumber, result.documentNumber)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyPlaceOfBirth, result.placeOfBirth)
        add(R.string.keySex, result.sex)
    }

    private fun extractBack(result: BruneiIdBackRecognizer.Result) {
        extractMrzResult(result.mrzResult)
        add(R.string.keyIssueDate, result.dateOfIssue)
        add(R.string.keyAddress, result.address)
        add(R.string.keyRace, result.race)
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