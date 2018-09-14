package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer

class KuwaitIdRecognition : BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { KuwaitIdFrontRecognizer() }
    private val backRecognizer by lazy { KuwaitIdBackRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }

    override fun setupRecognizers() {
        backRecognizer.setExtractSerialNo(true)
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator()
    }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }

    override fun getResultTitle(): String? {
        if (frontResult.isNotEmpty()) {
            return frontResult.name
        } else if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }

        return null
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(R.string.keySerialNumber, backResult.serialNo)
    }

    private fun extractFrontSide() {
        add(R.string.keyCivilIdNumber, frontResult.civilIdNumber)
        add(R.string.keyFullName, frontResult.name)
        add(R.string.keyNationality, frontResult.nationality)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyDateOfBirth, frontResult.birthDate)
        addDateOfExpiry(frontResult.expiryDate.date)
    }

}