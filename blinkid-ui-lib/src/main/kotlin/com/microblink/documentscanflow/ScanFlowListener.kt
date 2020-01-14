package com.microblink.documentscanflow

import com.microblink.documentscanflow.document.Document
import com.microblink.documentscanflow.recognition.RecognitionResult
import com.microblink.image.Image

interface ScanFlowListener {

    fun onScanStarted()

    fun onFirstSideScanned(recognitionResult: RecognitionResult, successFrame: Image?)

    fun onEntireDocumentScanned(recognitionResult: RecognitionResult, successFrame: Image?)

    fun onSelectedDocumentChanged(oldDocument: Document, newDocument: Document)

    fun onTimeout()

    fun onDocumentSidesNotMatching()

    fun onDocumentNotSupported()

}