package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys

open class UsdlRecognition : BaseRecognition() {

    private val recognizer by lazy { UsdlRecognizer() }
    private val faceRecognizer by lazy { DocumentFaceRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(faceRecognizer, recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(FIRST_NAME, result.firstName)
        add(LAST_NAME, result.lastName)
        add(FULL_NAME, result.fullName)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(DOCUMENT_NUMBER, result.getField(UsdlKeys.CustomerIdNumber))
        add(ADDRESS, result.address)
        add(SEX, result.sex)
        add(VEHICLE_CLASS, result.vehicleClass)
        add(ENDORSEMENTS, result.endorsements)
        add(DRIVER_RESTRICTIONS, result.restrictions)
        add(BARCODE_DATA, result.rawStringData)

        return result.fullName
    }

}