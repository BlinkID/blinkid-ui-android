package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.RecognizerProvider
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkbarcode.pdf417.Pdf417Recognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer

class GenericRecognition(private val recognizerProvider: RecognizerProvider) : BaseRecognition() {

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return recognizerProvider.recognizers
    }

    override fun setupRecognizers() {
        for (recognizer in recognizerProvider.recognizers) {
            if (recognizer is MrtdRecognizer) {
                recognizer.isAllowUnverifiedResults = true
            }
        }
    }

    override fun extractData(): String? {
        var result: String? = null
        for (recognizer in recognizerProvider.recognizers) {
            if (recognizer is MrtdRecognizer && recognizer.result.isNotEmpty()) {
                extractMrzResult(recognizer.result.mrzResult)
                result = buildMrtdTitle(recognizer.result.mrzResult)
            }
            if (recognizer is Pdf417Recognizer && recognizer.result.isNotEmpty()) {
                add(R.string.keyBarcodeString, recognizer.result.stringData)
            }
        }
        return result
    }

    companion object {
        fun mrtd(): GenericRecognition {
            return GenericRecognition(object: RecognizerProvider() {
                override fun createRecognizers() = listOf(MrtdRecognizer())
            })
        }

        fun id1(): GenericRecognition {
            return GenericRecognition(object: RecognizerProvider() {
                override fun createRecognizers() =
                        listOf(buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD))
            })
        }

        fun mrtdId1(): GenericRecognition {
            return GenericRecognition(object: RecognizerProvider() {
                override fun createRecognizers() = listOf(MrtdRecognizer(),
                        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD))
            })
        }

        fun mrtdId2Vertical(): GenericRecognition {
            return GenericRecognition(object: RecognizerProvider() {
                override fun createRecognizers() = listOf(MrtdRecognizer(),
                        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID2_VERTICAL_CARD))
            })
        }

        fun faceMrtd(): GenericRecognition {
            return GenericRecognition(object: RecognizerProvider() {
                override fun createRecognizers(): List<Recognizer<*, *>> {
                    return listOf(DocumentFaceRecognizer(), MrtdRecognizer())
                }
            })
        }

        fun facePdf417(): GenericRecognition {
            return GenericRecognition(object: RecognizerProvider() {
                override fun createRecognizers() = listOf(DocumentFaceRecognizer(), Pdf417Recognizer())
            })
        }

        fun faceId1(): GenericRecognition {
            return GenericRecognition(object: RecognizerProvider() {
                override fun createRecognizers() = listOf(DocumentFaceRecognizer(),
                        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD))
            })
        }
    }
}
