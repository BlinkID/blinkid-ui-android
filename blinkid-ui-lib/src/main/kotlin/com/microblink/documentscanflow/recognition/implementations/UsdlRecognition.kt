package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer

open class UsdlRecognition : TwoSideRecognition<DocumentFaceRecognizer.Result, UsdlRecognizer.Result>() {

    override val frontRecognizer by lazy { DocumentFaceRecognizer() }
    override val backRecognizer by lazy { UsdlRecognizer() }

    override fun extractFields() = extract(backResult)

    override fun getResultTitle(): String? {
        return if (backResult.isNotEmpty()) backResult.fullName
        else null
    }

}