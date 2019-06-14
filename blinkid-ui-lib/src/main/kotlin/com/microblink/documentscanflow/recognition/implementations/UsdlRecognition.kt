package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys

open class UsdlRecognition : TwoSideRecognition<DocumentFaceRecognizer.Result, UsdlRecognizer.Result>() {

    override val frontRecognizer by lazy { DocumentFaceRecognizer() }
    override val backRecognizer by lazy { UsdlRecognizer() }

    override fun extractFields() {
        if (backResult.isNotEmpty()) {
            add(FIRST_NAME, backResult.firstName)
            add(LAST_NAME, backResult.lastName)
            add(FULL_NAME, backResult.fullName)
            add(DATE_OF_BIRTH, backResult.dateOfBirth)
            add(DATE_OF_ISSUE, backResult.dateOfIssue)
            addDateOfExpiry(backResult.dateOfExpiry.date)
            add(DOCUMENT_NUMBER, backResult.getField(UsdlKeys.CustomerIdNumber))
            add(ADDRESS, backResult.address)
            add(SEX, backResult.sex)
            add(VEHICLE_CLASS, backResult.vehicleClass)
            add(ENDORSEMENTS, backResult.endorsements)
            add(DRIVER_RESTRICTIONS, backResult.restrictions)
            add(BARCODE_DATA, backResult.rawStringData)
        }
    }

    override fun getResultTitle(): String? {
        return if (backResult.isNotEmpty()) backResult.fullName
        else null
    }

}