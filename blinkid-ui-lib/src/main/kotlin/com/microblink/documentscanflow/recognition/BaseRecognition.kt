package com.microblink.documentscanflow.recognition

import android.content.Context
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.resultentry.ResultEntry
import com.microblink.documentscanflow.sanitizeMRZString
import com.microblink.entities.processors.imageReturn.ImageReturnProcessor
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.imageoptions.FaceImageOptions
import com.microblink.entities.recognizers.blinkid.imageoptions.FullDocumentImageOptions
import com.microblink.entities.recognizers.blinkid.imageoptions.SignatureImageOptions
import com.microblink.entities.recognizers.blinkid.imageresult.CombinedFullDocumentImageResult
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdDocumentType
import com.microblink.image.Image
import com.microblink.entities.recognizers.blinkid.imageresult.FaceImageResult
import com.microblink.entities.recognizers.blinkid.imageresult.FullDocumentImageResult
import com.microblink.entities.recognizers.blinkid.imageresult.SignatureImageResult
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult
import com.microblink.entities.recognizers.detector.DetectorRecognizer
import com.microblink.entities.settings.GlareDetectorOptions
import com.microblink.recognizers.blinkid.mrtd.MRTDResult
import com.microblink.results.date.Date
import com.microblink.results.date.DateResult

abstract class BaseRecognition {

    protected val resultEntries: MutableSet<ResultEntry<*>> = LinkedHashSet()
    protected var shouldValidate = false
    protected lateinit var context: Context

    private lateinit var entryBuilder: ResultEntry.Builder
    private var faceImage: Image? = null
    private var signatureImage: Image? = null
    private var firstSideDocumentImage: Image? = null
    private var secondSideDocumentImage: Image? = null

    private var setupDone = false

    fun setup() {
        if (setupDone) {
            return
        }

        for (recognizer in getAllRecognizers()) {
            if (recognizer is FullDocumentImageOptions) {
                recognizer.setReturnFullDocumentImage(true)
            }
            if (recognizer is FaceImageOptions) {
                recognizer.setReturnFaceImage(true)
            }
            if (recognizer is SignatureImageOptions) {
                recognizer.setReturnSignatureImage(true)
            }
            if (recognizer is GlareDetectorOptions) {
                recognizer.setDetectGlare(true)
            }
        }

        setupRecognizers()
        setupDone = true
    }

    fun canScanBothSides() = getSingleSideRecognizers().size == 2 || getCombinedRecognizer() != null

    open fun setupRecognizers() {
        // nothing, override if needed
    }

    fun extractResult(context: Context, shouldValidate: Boolean): RecognitionResult {
        this.context = context
        resultEntries.clear()
        entryBuilder = ResultEntry.Builder(context)
        this.shouldValidate = shouldValidate

        val title = extractData()

        extractImages()

        val result = RecognitionResult(title ?: "",
                resultEntries.toList(),
                firstSideDocumentImage,
                secondSideDocumentImage,
                faceImage,
                signatureImage)

        firstSideDocumentImage = null
        secondSideDocumentImage = null
        faceImage = null
        signatureImage = null

        return result
    }

    open fun getCombinedRecognizer(): Recognizer<*, *>? = null

    abstract fun getSingleSideRecognizers(): List<Recognizer<*, *>>

    private fun extractImages() {
        for (recognizer in getAllRecognizers()) {
            val result = recognizer.result
            if (result is SignatureImageResult && signatureImage == null) {
                signatureImage = result.signatureImage
            }
            if (result is FullDocumentImageResult) {
                if (firstSideDocumentImage == null) {
                    firstSideDocumentImage = result.fullDocumentImage
                } else {
                    secondSideDocumentImage = result.fullDocumentImage
                }
            }
            if (result is FaceImageResult && faceImage == null) {
                faceImage = result.faceImage
            }
            if (result is CombinedFullDocumentImageResult) {
                if (firstSideDocumentImage == null) {
                    firstSideDocumentImage = result.fullDocumentFrontImage
                }
                if (secondSideDocumentImage == null) {
                    secondSideDocumentImage = result.fullDocumentBackImage
                }
            }

            if (recognizer is DetectorRecognizer) {
                extractDetectorRecognizerImage(recognizer)
            }
        }
    }

    private fun extractDetectorRecognizerImage(recognizer: DetectorRecognizer) {
        val processorGroup = recognizer.result.templatingClass?.classificationProcessorGroups?.get(0)
        val processor = processorGroup?.processors?.get(0)
        if (processor is ImageReturnProcessor) {
            if (firstSideDocumentImage == null) {
                firstSideDocumentImage = processor.result.rawImage
            } else {
                secondSideDocumentImage = processor.result.rawImage
            }
        }
    }

     private fun getAllRecognizers(): List<Recognizer<*, *>> {
        val allRecognizers = mutableListOf<Recognizer<*, *>>()
        allRecognizers.addAll(getSingleSideRecognizers())
        if (getCombinedRecognizer() != null) {
            allRecognizers.add(getCombinedRecognizer()!!)
        }

        return allRecognizers
    }

    protected abstract fun extractData(): String?

    protected fun add(key: Int, value: String?) {
        if (!value.isNullOrEmpty()) {
            resultEntries.add(entryBuilder.build(key, value))
        }
    }

    protected fun add(key: Int, value: Boolean) {
        resultEntries.add(entryBuilder.build(key, value))
    }

    protected fun add(key: Int, value: Int, unit: String) {
        resultEntries.add(entryBuilder.build(key, value, unit))
    }

    protected fun add(key: Int, value: Date?) {
        if (value != null) {
            resultEntries.add(entryBuilder.build(key, value))
        }
    }

    protected fun add(key: Int, value: DateResult?) {
        if (value != null) {
            resultEntries.add(entryBuilder.build(key, value.date))
        }
    }

    protected fun addDateOfExpiry(date: Date?) {
        if (date != null) {
            resultEntries.add(entryBuilder.build(R.string.keyDateOfExpiry,
                    date,
                    ResultEntry.Builder.DateCheckType.IN_FUTURE))
        }
    }

    protected fun isCombinedScan(frontResult: Recognizer.Result<*>, backResult: Recognizer.Result<*>): Boolean {
        return frontResult.isNotEmpty() && backResult.isNotEmpty()
    }

    protected fun extractMrtdResult(result: MRTDResult) {
        add(R.string.keyPrimaryId, result.primaryId)
        add(R.string.keySecondaryId, result.secondaryId)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keySex, result.sex)
        add(R.string.keyNationality, result.nationality)
        add(R.string.keyDocumentCode, result.documentCode)
        add(R.string.keyIssuer, result.issuer)
        addDateOfExpiry(result.dateOfExpiry)
        add(R.string.keyDocumentNumber, result.documentNumber?.sanitizeMRZString())
        add(R.string.keyOpt1, result.opt1?.sanitizeMRZString())
        val opt2 = result.opt2?.sanitizeMRZString()
        if (!opt2.isNullOrEmpty()) {
            add(R.string.keyOpt2, opt2)
        }
    }

    protected fun extractMrzResult(mrzResult: MrzResult) {
        add(R.string.keyPrimaryId, mrzResult.primaryId)
        add(R.string.keySecondaryId, mrzResult.secondaryId)
        add(R.string.keyDateOfBirth, mrzResult.dateOfBirth.date)
        add(R.string.keySex, mrzResult.gender)
        add(R.string.keyNationality, mrzResult.nationality)
        add(R.string.keyDocumentCode, mrzResult.documentCode)
        add(R.string.keyIssuer, mrzResult.issuer)
        addDateOfExpiry(mrzResult.dateOfExpiry.date)
        add(R.string.keyOpt2, mrzResult.opt2)
        add(R.string.keyMRZText, mrzResult.mrzText)

        if (mrzResult.documentType == MrtdDocumentType.MRTD_TYPE_GREEN_CARD) {
            add(R.string.keyAlienNumber, mrzResult.alienNumber)
            add(R.string.keyApplicationReceiptNumber, mrzResult.applicationReceiptNumber)
            add(R.string.keyImmigrantCaseNumber, mrzResult.immigrantCaseNumber)
        } else {
            add(R.string.keyDocumentNumber, mrzResult.documentNumber)
            add(R.string.keyOpt1, mrzResult.opt1)
        }
    }

    protected fun buildMrtdTitle(result: MRTDResult): String = result.primaryId + " " + result.secondaryId
    protected fun buildMrtdTitle(result: MrzResult): String = result.primaryId + " " + result.secondaryId

}
