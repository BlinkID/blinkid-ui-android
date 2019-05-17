package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys

open class UsdlRecognition : TwoSideRecognition<UsdlRecognizer.Result, DocumentFaceRecognizer.Result>() {

    override val frontRecognizer by lazy { UsdlRecognizer() }
    override val backRecognizer by lazy { DocumentFaceRecognizer() }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            add(FIRST_NAME, frontResult.firstName)
            add(LAST_NAME, frontResult.lastName)
            add(FULL_NAME, frontResult.fullName)
            add(DATE_OF_BIRTH, frontResult.dateOfBirth)
            add(DATE_OF_ISSUE, frontResult.dateOfIssue)
            addDateOfExpiry(frontResult.dateOfExpiry.date)
            add(DOCUMENT_NUMBER, frontResult.getField(UsdlKeys.CustomerIdNumber))
            add(ADDRESS, frontResult.address)
            add(SEX, frontResult.sex)
            add(VEHICLE_CLASS, frontResult.vehicleClass)
            add(ENDORSEMENTS, frontResult.endorsements)
            add(DRIVER_RESTRICTIONS, frontResult.restrictions)
            add(BARCODE_DATA, frontResult.rawStringData)
        }
    }

    override fun getResultTitle(): String? {
        return if (frontResult.isNotEmpty()) frontResult.fullName
        else null
    }

}