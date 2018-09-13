package com.microblink.documentscanflow.recognition

import com.microblink.documentscanflow.recognition.resultentry.ResultEntry
import com.microblink.image.Image

data class RecognitionResult(val resultTitle: String,
                             val resultEntries: List<ResultEntry<*>>,
                             val frontSideDocumentImage: Image? = null,
                             val backSideDocumentImage: Image? = null,
                             val faceImage: Image? = null,
                             val signatureImage: Image? = null)