package com.microblink.documentscanflow.recognition.util

import com.microblink.documentscanflow.recognition.RecognitionResult
import com.microblink.image.Image

/**
 * Utility you can use when you want to pass images to another activity but don't want to save them to storage
 */
object ImageStorage {

    @JvmStatic
    var frontSideDocumentImage: Image? = null
        set(value) {
            field?.dispose()
            field = value
        }

    @JvmStatic
    var backSideDocumentImage: Image? = null
        set(value) {
            field?.dispose()
            field = value
        }

    @JvmStatic
    var faceImage: Image? = null
        set(value) {
            field?.dispose()
            field = value
        }

    @JvmStatic
    var signatureImage: Image? = null
        set(value) {
            field?.dispose()
            field = value
        }

    @JvmStatic
    fun addImages(recognitionResult: RecognitionResult) {
        backSideDocumentImage = recognitionResult.backSideDocumentImage
        frontSideDocumentImage = recognitionResult.frontSideDocumentImage
        faceImage = recognitionResult.faceImage
        signatureImage = recognitionResult.signatureImage
    }

    @JvmStatic
    fun clearImages() {
        frontSideDocumentImage?.dispose()
        frontSideDocumentImage = null

        backSideDocumentImage?.dispose()
        backSideDocumentImage = null

        faceImage?.dispose()
        faceImage = null

        signatureImage?.dispose()
        signatureImage = null
    }

}
