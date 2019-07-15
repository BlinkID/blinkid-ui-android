package com.microblink.documentscanflow.recognition

import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdDocumentType
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult

fun BaseRecognition.extract(backResult: UsdlRecognizer.Result) {
    if (backResult.isEmpty()) return

    add(ResultKey.FIRST_NAME, backResult.firstName)
    add(ResultKey.LAST_NAME, backResult.lastName)
    add(ResultKey.FULL_NAME, backResult.fullName)
    add(ResultKey.DATE_OF_BIRTH, backResult.dateOfBirth)
    add(ResultKey.DATE_OF_ISSUE, backResult.dateOfIssue)
    addDateOfExpiry(backResult.dateOfExpiry.date)
    add(ResultKey.DOCUMENT_NUMBER, backResult.getField(UsdlKeys.CustomerIdNumber))
    add(ResultKey.ADDRESS, backResult.address)
    add(ResultKey.SEX, backResult.sex)
    add(ResultKey.VEHICLE_CLASS, backResult.vehicleClass)
    add(ResultKey.ENDORSEMENTS, backResult.endorsements)
    add(ResultKey.DRIVER_RESTRICTIONS, backResult.restrictions)
    add(ResultKey.BARCODE_DATA, backResult.rawStringData)
}

fun BaseRecognition.extract(mrzResult: MrzResult) {
    add(ResultKey.PRIMARY_ID, mrzResult.primaryId)
    add(ResultKey.SECONDARY_ID, mrzResult.secondaryId)
    add(ResultKey.DATE_OF_BIRTH, mrzResult.dateOfBirth.date)
    add(ResultKey.SEX, mrzResult.gender)
    add(ResultKey.NATIONALITY, mrzResult.nationality)
    add(ResultKey.DOCUMENT_CODE, mrzResult.documentCode)
    add(ResultKey.ISSUER, mrzResult.issuer)
    addDateOfExpiry(mrzResult.dateOfExpiry.date)
    add(ResultKey.OPTIONAL_FIELD_2, mrzResult.opt2)
    add(ResultKey.MRZ_TEXT, mrzResult.mrzText)

    if (mrzResult.documentType == MrtdDocumentType.MRTD_TYPE_GREEN_CARD) {
        add(ResultKey.ALIEN_NUMBER, mrzResult.alienNumber)
        add(ResultKey.APPLICATION_RECEIPT_NUMBER, mrzResult.applicationReceiptNumber)
        add(ResultKey.IMMIGRANT_CASE_NUMBER, mrzResult.immigrantCaseNumber)
    } else {
        add(ResultKey.DOCUMENT_NUMBER, mrzResult.documentNumber)
        add(ResultKey.OPTIONAL_FIELD_1, mrzResult.opt1)
    }
}