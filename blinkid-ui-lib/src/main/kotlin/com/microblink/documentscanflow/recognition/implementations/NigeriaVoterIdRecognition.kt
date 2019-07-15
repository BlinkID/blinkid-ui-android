package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer
import com.microblink.entities.recognizers.blinkid.nigeria.NigeriaVoterIdBackRecognizer

class NigeriaVoterIdRecognition: TwoSideRecognition<DocumentFaceRecognizer.Result, NigeriaVoterIdBackRecognizer.Result>() {

    override val frontRecognizer = DocumentFaceRecognizer()
    override val backRecognizer = NigeriaVoterIdBackRecognizer()

    override fun extractFields() {
        if (backResult.isEmpty()) return
        add(ResultKey.FIRST_NAME, backResult.firstName)
        add(ResultKey.LAST_NAME, backResult.surname)
        add(ResultKey.ADDRESS, backResult.address)
        add(ResultKey.SEX, backResult.sex)
        add(ResultKey.BARCODE_DATA, backResult.rawBarcodeData)
    }

    override fun getResultTitle() =
            if (backResult.isEmpty()) null
            else FormattingUtils.formatResultTitle(backResult.firstName, backResult.surname)

}