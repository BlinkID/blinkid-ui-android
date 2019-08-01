package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.ScanFlowState
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.buildId1VerticalCardDetectorRecognizer
import com.microblink.documentscanflow.recognition.extract
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.entities.recognizers.detector.DetectorRecognizer

class UsdlUnder21Recognition : TwoSideRecognition<DetectorRecognizer.Result, UsdlRecognizer.Result>() {

    override val frontRecognizer by lazy { buildId1VerticalCardDetectorRecognizer() }
    override val backRecognizer by lazy { UsdlRecognizer() }

    override fun extractFields() = extract(backResult)

    override fun getResultTitle(): String? {
        return if (backResult.isNotEmpty()) backResult.fullName
        else null
    }

    override fun isForVerticalDocument(scanFlowState: ScanFlowState) = scanFlowState == ScanFlowState.FRONT_SIDE_SCAN

}