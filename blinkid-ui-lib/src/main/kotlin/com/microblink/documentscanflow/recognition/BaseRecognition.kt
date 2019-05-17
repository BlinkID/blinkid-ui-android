package com.microblink.documentscanflow.recognition

import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.buildId2VerticalCardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.resultentry.ResultEntry
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.processors.imageReturn.ImageReturnProcessor
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkbarcode.pdf417.Pdf417Recognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer
import com.microblink.entities.recognizers.blinkid.imageoptions.FaceImageOptions
import com.microblink.entities.recognizers.blinkid.imageoptions.FullDocumentImageOptions
import com.microblink.entities.recognizers.blinkid.imageoptions.SignatureImageOptions
import com.microblink.entities.recognizers.blinkid.imageresult.CombinedFullDocumentImageResult
import com.microblink.entities.recognizers.blinkid.imageresult.FaceImageResult
import com.microblink.entities.recognizers.blinkid.imageresult.FullDocumentImageResult
import com.microblink.entities.recognizers.blinkid.imageresult.SignatureImageResult
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdDocumentType
import com.microblink.entities.recognizers.blinkid.mrtd.MrtdRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult
import com.microblink.entities.recognizers.blinkid.passport.PassportRecognizer
import com.microblink.entities.recognizers.detector.DetectorRecognizer
import com.microblink.entities.settings.GlareDetectorOptions
import com.microblink.image.Image
import com.microblink.results.date.Date
import com.microblink.results.date.DateResult

sealed class BaseRecognition(val isFullySupported: Boolean = true) {

    protected val resultEntries: MutableSet<ResultEntry<*>> = LinkedHashSet()
    protected var shouldValidate = false

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

    fun extractResult(shouldValidate: Boolean): RecognitionResult {
        resultEntries.clear()
        entryBuilder = ResultEntry.Builder()
        this.shouldValidate = shouldValidate

        val title = extractData()

        extractImages()

        val result = RecognitionResult(title ?: "",
                resultEntries.toList().sortedBy { it.key.ordinal },
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

    open fun getCombinedRecognizer(): Recognizer<*>? = null

    abstract fun getSingleSideRecognizers(): List<Recognizer<*>>

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

     private fun getAllRecognizers(): List<Recognizer<*>> {
        val allRecognizers = mutableListOf<Recognizer<*>>()
        allRecognizers.addAll(getSingleSideRecognizers())
        if (getCombinedRecognizer() != null) {
            allRecognizers.add(getCombinedRecognizer()!!)
        }

        return allRecognizers
    }

    protected abstract fun extractData(): String?

    protected fun add(key: ResultKey, value: String?) {
        if (!value.isNullOrEmpty()) {
            resultEntries.add(entryBuilder.build(key, value))
        }
    }

    protected fun add(key: ResultKey, value: Boolean) {
        resultEntries.add(entryBuilder.build(key, value))
    }

    protected fun add(key: ResultKey, value: Int, unit: String) {
        resultEntries.add(entryBuilder.build(key, value, unit))
    }

    protected fun add(key: ResultKey, value: Date?) {
        if (value != null) {
            resultEntries.add(entryBuilder.build(key, value))
        }
    }

    protected fun add(key: ResultKey, value: DateResult?) {
        if (value != null ) {
            add(key, value.date)
        }
    }

    protected fun addDateOfExpiry(dateResult: DateResult) = addDateOfExpiry(dateResult.date)

    protected fun addDateOfExpiry(date: Date?) {
        if (date != null) {
            resultEntries.add(entryBuilder.build(ResultKey.DATE_OF_EXPIRY,
                    date,
                    ResultEntry.Builder.DateCheckType.IN_FUTURE))
        }
    }

    protected fun extractMrzResult(mrzResult: MrzResult) {
        add(PRIMARY_ID, mrzResult.primaryId)
        add(SECONDARY_ID, mrzResult.secondaryId)
        add(DATE_OF_BIRTH, mrzResult.dateOfBirth.date)
        add(SEX, mrzResult.gender)
        add(NATIONALITY, mrzResult.nationality)
        add(DOCUMENT_CODE, mrzResult.documentCode)
        add(ISSUER, mrzResult.issuer)
        addDateOfExpiry(mrzResult.dateOfExpiry.date)
        add(OPTIONAL_FIELD_2, mrzResult.opt2)
        add(MRZ_TEXT, mrzResult.mrzText)

        if (mrzResult.documentType == MrtdDocumentType.MRTD_TYPE_GREEN_CARD) {
            add(ALIEN_NUMBER, mrzResult.alienNumber)
            add(APPLICATION_RECEIPT_NUMBER, mrzResult.applicationReceiptNumber)
            add(IMMIGRANT_CASE_NUMBER, mrzResult.immigrantCaseNumber)
        } else {
            add(DOCUMENT_NUMBER, mrzResult.documentNumber)
            add(OPTIONAL_FIELD_1, mrzResult.opt1)
        }
    }

    protected fun buildMrtdTitle(result: MrzResult): String = result.primaryId + " " + result.secondaryId

}

abstract class SingleSideRecognition<FrontResult : Recognizer.Result>
    (isFullySupported: Boolean = true) : BaseRecognition(isFullySupported) {

    abstract val recognizer: Recognizer<FrontResult>

    val result by lazy { recognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> = listOf(recognizer)

    override fun extractData() =
        if (result.isEmpty()) null
        else extractData(result)

    abstract fun extractData(result: FrontResult): String?

}

abstract class SingleSideWithId1CardDetectorRecognition<FrontResult : Recognizer.Result>
    (isFullySupported: Boolean = true) : SingleSideRecognition<FrontResult>(isFullySupported) {

    private val cardDetectorRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf(recognizer, cardDetectorRecognizer)
}

abstract class TwoSideRecognition<FrontResult : Recognizer.Result, BackResult : Recognizer.Result>
    (isFullySupported: Boolean = true) : BaseRecognition(isFullySupported) {

    abstract val frontRecognizer: Recognizer<FrontResult>
    abstract val backRecognizer: Recognizer<BackResult>

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> = listOf(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        if (shouldValidate) {
            val isValid = createValidator().isResultValid
            if (!isValid) {
                throw ResultMergeException()
            }
        }
        return extractData(frontResult, backResult)
    }

    abstract fun extractData(frontResult: FrontResult, backResult: BackResult): String?

    internal open fun createValidator() = ResultValidator()

}

abstract class CombinedRecognition<FrontResult : Recognizer.Result,
        BackResult : Recognizer.Result,
        CombinedResult: Recognizer.Result>
    (isFullySupported: Boolean = true) : BaseRecognition(isFullySupported) {

    abstract val frontRecognizer: Recognizer<FrontResult>
    abstract val backRecognizer: Recognizer<BackResult>
    abstract val combRecognizer: Recognizer<CombinedResult>

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }
    val combinedResult by lazy { combRecognizer.result }

    override fun getSingleSideRecognizers() = listOf(frontRecognizer, backRecognizer)

    override fun getCombinedRecognizer() = combRecognizer

    override fun extractData(): String? {
        if (shouldValidate) {
            val isValid = (combinedResult as com.microblink.entities.recognizers.blinkid.CombinedResult).isDocumentDataMatch
            if (!isValid) {
                throw ResultMergeException()
            }
        }
        return when {
            combinedResult.isNotEmpty() -> extractCombinedResult(combinedResult)
            frontResult.isNotEmpty() -> extractFrontResult(frontResult)
            backResult.isNotEmpty() -> extractBackResult(backResult)
            else -> null
        }
    }

    abstract fun extractCombinedResult(combinedResult: CombinedResult): String?

    abstract fun extractFrontResult(frontResult: FrontResult): String?

    abstract fun extractBackResult(backResult: BackResult): String?

}

abstract class BaseTwoSideRecognition
    : BaseRecognition() {

    override fun extractData(): String? {
        if (shouldValidate) {
            val validator = createValidator()
            if (!validator.isResultValid) {
                throw ResultMergeException()
            }
        }

        extractFields()
        return getResultTitle()
    }

    internal abstract fun createValidator(): ResultValidator

    abstract fun extractFields()

    abstract fun getResultTitle(): String?

}

class GenericRecognition(isFullySupported: Boolean, private val recognizerProvider: RecognizerProvider) : BaseRecognition(isFullySupported) {

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
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
            if (recognizer.result.isNotEmpty()) {
                when (recognizer) {
                    is MrtdRecognizer -> {
                        extractMrzResult(recognizer.result.mrzResult)
                        result = buildMrtdTitle(recognizer.result.mrzResult)
                    }
                    is PassportRecognizer -> {
                        extractMrzResult(recognizer.result.mrzResult)
                        result = buildMrtdTitle(recognizer.result.mrzResult)
                    }
                    is Pdf417Recognizer -> add(ResultKey.BARCODE_DATA, recognizer.result.stringData)
                }
            }
        }
        return result
    }

    companion object {

        val residencePermit = faceMrtd(true)

        val id = faceMrtd(false)

        val drivingLicence = faceId1(false)

        val passport = passport()

        val visa = mrtd(true)

        private fun passport(): GenericRecognition {
            return GenericRecognition(true, object: RecognizerProvider() {
                override fun createRecognizers() = listOf(PassportRecognizer())
            })
        }

        fun mrtd(isFullySupported: Boolean): GenericRecognition {
            return GenericRecognition(isFullySupported, object: RecognizerProvider() {
                override fun createRecognizers() = listOf(MrtdRecognizer())
            })
        }

        fun id1(isFullySupported: Boolean): GenericRecognition {
            return GenericRecognition(isFullySupported, object: RecognizerProvider() {
                override fun createRecognizers() =
                    listOf(buildId1CardDetectorRecognizer())
            })
        }

        fun mrtdId1(isFullySupported: Boolean): GenericRecognition {
            return GenericRecognition(isFullySupported, object: RecognizerProvider() {
                override fun createRecognizers() = listOf(
                    MrtdRecognizer(),
                    buildId1CardDetectorRecognizer()
                )
            })
        }

        fun mrtdId2Vertical(isFullySupported: Boolean): GenericRecognition {
            return GenericRecognition(isFullySupported, object: RecognizerProvider() {
                override fun createRecognizers() = listOf(MrtdRecognizer(), buildId2VerticalCardDetectorRecognizer())
            })
        }

        fun faceMrtd(isFullySupported: Boolean): GenericRecognition {
            return GenericRecognition(isFullySupported, object: RecognizerProvider() {
                override fun createRecognizers(): List<Recognizer<*>> {
                    return listOf(DocumentFaceRecognizer(), MrtdRecognizer())
                }
            })
        }

        fun facePdf417(isFullySupported: Boolean): GenericRecognition {
            return GenericRecognition(isFullySupported, object: RecognizerProvider() {
                override fun createRecognizers() = listOf(DocumentFaceRecognizer(), Pdf417Recognizer())
            })
        }

        fun faceId1(isFullySupported: Boolean): GenericRecognition {
            return GenericRecognition(isFullySupported, object: RecognizerProvider() {
                override fun createRecognizers() = listOf(
                    DocumentFaceRecognizer(),
                    buildId1CardDetectorRecognizer()
                )
            })
        }
    }
}