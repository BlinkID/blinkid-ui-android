package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.ScanFlowState

class UsdlUnder21Recognition : UsdlRecognition() {

    override fun isForVerticalDocument(scanFlowState: ScanFlowState) = scanFlowState == ScanFlowState.FRONT_SIDE_SCAN

}