package com.microblink.documentscanflow

import android.annotation.TargetApi
import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.RectF
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.support.annotation.AnyThread
import android.support.annotation.CallSuper
import android.support.annotation.UiThread
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.microblink.documentscanflow.country.CountryFactory
import com.microblink.documentscanflow.document.Document
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.ChooseCountryActivity
import com.microblink.documentscanflow.recognition.RecognitionError
import com.microblink.documentscanflow.recognition.RecognizerManager
import com.microblink.documentscanflow.recognition.ResultMergeException
import com.microblink.documentscanflow.recognition.framelistener.FrameGrabberMode
import com.microblink.documentscanflow.recognition.framelistener.FrameListener
import com.microblink.documentscanflow.ui.InstructionsHandler
import com.microblink.documentscanflow.ui.scanlineanimator.DefaultScanLineAnimator
import com.microblink.documentscanflow.ui.TorchButtonHandler
import com.microblink.documentscanflow.ui.documentchooser.DefaultDocumentChooser
import com.microblink.documentscanflow.ui.documentchooser.DocumentChooser
import com.microblink.documentscanflow.ui.scanlineanimator.ScanLineAnimator
import com.microblink.documentscanflow.ui.scansoundplayer.ScanSuccessPlayer
import com.microblink.documentscanflow.ui.scansoundplayer.SoundPoolScanSuccessPlayer
import com.microblink.documentscanflow.ui.scantimeouthandler.DefaultScanTimeoutHandler
import com.microblink.documentscanflow.ui.scantimeouthandler.ScanTimeoutHandler
import com.microblink.documentscanflow.ui.splashoverlay.InvisibleSplashOverlaySettings
import com.microblink.documentscanflow.ui.splashoverlay.SplashOverlaySettings
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer
import com.microblink.entities.recognizers.detector.DetectorRecognizer
import com.microblink.entities.recognizers.framegrabber.FrameCallback
import com.microblink.entities.recognizers.successframe.SuccessFrameGrabberRecognizer
import com.microblink.hardware.orientation.Orientation
import com.microblink.image.Image
import com.microblink.metadata.MetadataCallbacks
import com.microblink.metadata.glare.GlareCallback
import com.microblink.recognition.RecognitionSuccessType
import com.microblink.util.CameraPermissionManager
import com.microblink.util.Log
import com.microblink.view.BaseCameraView
import com.microblink.view.CameraAspectMode
import com.microblink.view.CameraEventsListener
import com.microblink.view.ocrResult.OcrResultDotsView
import com.microblink.view.recognition.ScanResultListener
import kotlinx.android.synthetic.main.mb_activity_scan_document.*
import kotlinx.android.synthetic.main.mb_include_splash_overlay.*
import kotlinx.android.synthetic.main.mb_include_scan_bottom_container.*
import kotlinx.android.synthetic.main.mb_view_scan_frame.view.*
import java.util.*
import kotlin.math.max

abstract class BaseDocumentScanActivity : AppCompatActivity(), ScanResultListener {

    protected val cameraPermissionManager
            by lazy { CameraPermissionManager(this) }
    protected val scanSuccessPlayer
            by lazy { createScanSuccessSoundPlayer() }

    private val scanFlowListener by lazy {createScanFlowListener()}
    private val frameListener by lazy {createFrameListener()}
    private val splashOverlaySettings by lazy { createSplashOverlaySettings() }
    private val documentChooser by lazy { createDocumentChooser() }
    private val scanTimeoutHandler by lazy { createScanTimeoutHandler() }
    private val torchButtonHandler = TorchButtonHandler()
    private val recognizerManager by lazy { RecognizerManager(getFrameGrabberMode(), createFrameCallback()) }
    private val cameraErrorHandler by lazy { CameraErrorHandler(this) {finish()} }
    private val instructionsHandler by lazy { InstructionsHandler(this, currentDocument, scanFrameLayout.scanInstructionsTv, scanFrameLayout.flipCardView) }
    private val scanLineAnimator by lazy { createScanLineAnimator() }

    private val handler = Handler()
    private val localeBroadcastReceiver by lazy {createLocaleBroadcastReceiver()}

    private val ocrView by lazy { OcrResultDotsView(this, recognizerView.hostScreenOrientation, recognizerView.initialOrientation) }

    private var scanFlowState: ScanFlowState = ScanFlowState.NOT_STARTED
    private lateinit var currentDocument: Document
    private var recognitionError = RecognitionError.NONE

    protected abstract fun shouldScanBothDocumentSides(): Boolean
    protected abstract fun createScanFlowListener(): ScanFlowListener
    protected abstract fun getInitialDocument(): Document

    protected open fun onCameraError(exc: Throwable?) {}
    protected open fun getFrameGrabberMode(): FrameGrabberMode = FrameGrabberMode.NOTHING
    protected open fun createFrameListener(): FrameListener = FrameListener.EMPTY
    protected open fun createDocumentChooser() : DocumentChooser = DefaultDocumentChooser(this)
    protected open fun createScanTimeoutHandler() : ScanTimeoutHandler = DefaultScanTimeoutHandler(SCAN_TIMEOUT_MILLIS)
    protected open fun createScanTimeoutListener() = createDefaultScanTimeoutListener()

    protected open fun createSplashOverlaySettings(): SplashOverlaySettings = InvisibleSplashOverlaySettings()
    protected open fun createScanSuccessSoundPlayer(): ScanSuccessPlayer = SoundPoolScanSuccessPlayer(this, R.raw.beep)
    protected open fun createScanLineAnimator(): ScanLineAnimator = DefaultScanLineAnimator(scanFrameLayout)
    protected open fun getLayoutId() = R.layout.mb_activity_scan_document

    protected fun getScanFlowState() = scanFlowState

    protected fun getCurrentDocument() = currentDocument

    @UiThread
    protected fun updateDocument(document: Document) {
        val isSameCountry = currentDocument.country == document.country
        currentDocument = document
        scanFlowListener.onSelectedDocumentChanged(currentDocument)
        if (!isSameCountry) {
            updateDocumentTypeSelectionTabs(document)
            selectedCountryTv.text = currentDocument.country.getLocalisedName()
        }
        startScan()
    }

    @AnyThread
    protected fun pauseScanning() {
        scanTimeoutHandler.stopTimer()
        runOnUiThread {
            recognizerView.pauseScanning()
            scanLineAnimator.onScanPause()
        }
    }

    protected fun resumeScanning() {
        resumeScanning(0L)
    }

    protected fun isScanningPaused() = recognizerView.isScanningPaused

    /**
     * Resumes scanning with delay if the one of the currently activated recognizers is
     * {@link DetectorRecognizer} of {@link DocumentFaceRecognizer} to prevent immediate scanning done event.
     */
    private fun resumeScanning(minimumDelay : Long) {
        val activeRecognizers = recognizerView.recognizerBundle?.recognizers
        if (activeRecognizers == null) {
            resumeScanningImmediately()
            return
        }

        var delay = minimumDelay
        for (recognizer: Recognizer<*, *> in activeRecognizers) {
            var actualRecognizer = recognizer
            if (actualRecognizer is SuccessFrameGrabberRecognizer) {
                actualRecognizer = actualRecognizer.slaveRecognizer
            }
            if (actualRecognizer is DetectorRecognizer) {
                delay = max(delay, DELAY_DETECTOR_RECOGNIZER_RESUME)
            } else if (actualRecognizer is DocumentFaceRecognizer) {
                delay = max(delay, DELAY_DOCUMENT_FACE_RESUME)
            }
        }

        if (delay > 0L) {
            handler.postDelayed({ resumeScanningImmediately() }, delay)
            return
        } else {
            resumeScanningImmediately()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MbBlinkIdUiTheme_FullScreen)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        setupCountryFactory()

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(getLayoutId())
        setSupportActionBar(toolbarActivityScan)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        countryContainer.setVisible(documentChooser.shouldShowCountryChooser())
        countryContainer.setOnClickListener { documentChooser.onChooseCountryClick(currentDocument) }
        countryLabel.text = documentChooser.geChooseCountryLabel()

        val permissionOverlay = cameraPermissionManager.askPermissionOverlay
        if (permissionOverlay != null) {
            content_root.addView(permissionOverlay)
        }

        initRecognizerRunnerView()
        recognizerView.addChildView(ocrView.view, false)

        volumeControlStream = AudioManager.STREAM_MUSIC

        currentDocument = getInitialDocument()
        updateDocumentTypeSelectionTabs(currentDocument)
        selectedCountryTv.text = currentDocument.country.getLocalisedName()

        scanTimeoutHandler.registerListener(createScanTimeoutListener())

        startScan()
        recognizerView.setLifecycle(lifecycle)

        scanSuccessPlayer.prepare()

        arrowRight.drawable.mutate().setColorFilter(ContextCompat.getColor(this, R.color.mbIconSelectCountry), PorterDuff.Mode.MULTIPLY)

        bottomContainer.addOnLayoutChangeListener(object: View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if (bottomContainer.height > 0) {
                    bottomContainer.translationY = bottomContainer.height.toFloat()
                    bottomContainer.animate()
                            .translationY(0f)
                            .setStartDelay(InstructionsHandler.FIRST_INSTRUCTIONS_DURATION + 100)
                            .setDuration(500)
                            .setInterpolator(AccelerateDecelerateInterpolator())
                            .start()
                    bottomContainer.removeOnLayoutChangeListener(this)
                }
            }

        })
    }

    private fun updateDocumentTypeSelectionTabs(document: Document) {
        if (!documentChooser.shouldShowDocumentTypeTabs()) {
            return
        }

        documentTypeTabs.visibility = View.VISIBLE
        documentTypeTabs.clearOnTabSelectedListeners()
        documentTypeTabs.removeAllTabs()

        val country = document.country
        for (docType in country.getSupportedDocumentTypes()) {
            if (!documentChooser.isDocumentTypeSupportedForCountry(docType, country)) {
                continue
            }

            val documentName = country.getDocumentNameStringId(docType)
            val tab = documentTypeTabs.newTab().setText(documentName).setTag(docType)
            documentTypeTabs.addTab(tab)
            if (docType == document.documentType) {
                // delay to give it enough time to scroll
                handler.postDelayed({ tab.select() }, 200)
            }
        }

        documentTypeTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val docType = tab?.tag as DocumentType
                updateDocument(Document(currentDocument.country, docType))
            }
        })
    }

    @UiThread
    private fun startScan(shouldClearHandlerScanFlowState: Boolean = true) {
        scanFlowListener.onScanStarted()

        scanLineAnimator.onScanStart()

        recognizerManager.clear()
        instructionsHandler.clear(shouldClearHandlerScanFlowState)

        scanFlowState = if(shouldScanBothDocumentSides()) {
            ScanFlowState.FRONT_SIDE_SCAN
        } else {
            ScanFlowState.ANY_SIDE_SCAN
        }

        prepareScanning(currentDocument)
        startScanningNextSide()
    }

    @AnyThread
    private fun resumeScanningImmediately() {
        scanTimeoutHandler.startTimer()

        // resetting combined will revert it to first side scan and we don't want that
        val shouldResetState = !recognizerManager.isCombinedRecognition(scanFlowState)
                || recognitionError == RecognitionError.SIDES_NOT_MATCHING
        onRecognitionErrorHandled()
        recognizerView.resumeScanning(shouldResetState)
        runOnUiThread { scanLineAnimator.onScanResume() }
    }

    private fun onRecognitionErrorHandled() {
        recognitionError = RecognitionError.NONE
    }

    private fun setupCountryFactory() {
        // prepare countries array only for supported locales (translations)
        CountryFactory.setLocale(Locale(getString(R.string.mb_locale_language_code)))
        // when locale settings are changed, sort countries again
        registerReceiver(localeBroadcastReceiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
    }

    override fun onResume() {
        super.onResume()
        // always restart scanning when resuming activity, must be called after recognizer_view.resume()
        restartScanning()
        torchButtonHandler.isTorchEnabled = false

        if (splashOverlaySettings.isEnabled()) {
            splash_overlay.setBackgroundResource(splashOverlaySettings.getBackgroundColorResource())
            splash_logo.setImageResource(splashOverlaySettings.getLogoDrawableResource())
            splash_overlay.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        scanTimeoutHandler.stopTimer()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        unregisterReceiver(localeBroadcastReceiver)
        scanTimeoutHandler.registerListener(null)
        super.onDestroy()
        scanSuccessPlayer.cleanup()
        instructionsHandler.clear(true)
    }

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        recognizerView.changeConfiguration(newConfig)
        ocrView.setHostActivityOrientation(recognizerView.hostScreenOrientation)
    }

    private fun initRecognizerRunnerView() {
        recognizerView.aspectMode = CameraAspectMode.ASPECT_FILL
        recognizerView.setScanResultListener(this)
        recognizerView.cameraEventsListener = createCameraEventsListener()
        recognizerView.initialOrientation = Orientation.ORIENTATION_PORTRAIT
        recognizerView.isPinchToZoomAllowed = true
        recognizerView.setOrientationAllowedListener { it.isVertical }

        val metadataCallbacks = MetadataCallbacks()
        metadataCallbacks.setPointsDetectionCallback { ocrView.addDisplayablePointsDetection(it) }
        metadataCallbacks.setFailedDetectionCallback { ocrView.clearDisplayedContent() }
        metadataCallbacks.setFirstSideRecognitionCallback { onCombinedRecognizerFirstSideDone() }
        metadataCallbacks.glareCallback = GlareCallback { hasGlare ->
            if (hasGlare) {
                runOnUiThread { instructionsHandler.onGlareDetected() }
            }
        }
        recognizerView.setMetadataCallbacks(metadataCallbacks)
    }

    // special case for combined recognizer
    private fun onCombinedRecognizerFirstSideDone() {
        val recognitionResult = currentDocument.getRecognition().extractResult(this, false)
        scanTimeoutHandler.stopTimer()
        scanTimeoutHandler.startTimer()
        scanFlowListener.onFirstSideScanned(recognitionResult, null)

        scanFlowState = ScanFlowState.BACK_SIDE_SCAN
        runOnUiThread {
            val delay = instructionsHandler.updateSideInstructions(currentDocument, scanFlowState)
            scanLineAnimator.onScanPause()
            handler.postDelayed({ scanLineAnimator.onScanResume() }, delay)
        }
    }

    override fun onScanningDone(recognitionSuccessType: RecognitionSuccessType) {
        if (recognitionSuccessType != RecognitionSuccessType.SUCCESSFUL) {
            runOnUiThread { restartScanning() }
            return
        }
        pauseScanning()
        runOnUiThread { onScanSuccess() }
    }

    private fun onScanSuccess() {
        scanSuccessPlayer.play()
        if (canSwitchToSecondSide()) {
            onFirstSideScanned()
        } else {
            onAllSidesScanned()
        }
    }

    private fun canSwitchToSecondSide():Boolean {
        return scanFlowState == ScanFlowState.FRONT_SIDE_SCAN
                && currentDocument.getRecognition().canScanBothSides()
    }

    private fun onAllSidesScanned() {
        scanTimeoutHandler.stopTimer()

        val recognition = currentDocument.getRecognition()
        try {
            val extractorResult = recognition.extractResult(this, shouldScanBothDocumentSides())
            scanFlowListener.onEntireDocumentScanned(extractorResult, recognizerManager.getSuccessFrame(scanFlowState))
            resumeScanningImmediately()
        } catch (e: ResultMergeException) {
            onSidesNotMatching()
        }
    }

    private fun onSidesNotMatching() {
        recognitionError = RecognitionError.SIDES_NOT_MATCHING
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.mb_instructions_sides_not_matching)
                .setPositiveButton(android.R.string.ok, null)
                .setOnDismissListener {
                    restartScanning()
                    resumeScanning()
                }
                .show()
    }

    private fun onFirstSideScanned() {
        scanTimeoutHandler.stopTimer()
        val successFrame = recognizerManager.getSuccessFrame(scanFlowState)
        val recognitionResult = currentDocument.getRecognition().extractResult(this, false)
        scanFlowListener.onFirstSideScanned(recognitionResult, successFrame)
        scanFlowState = ScanFlowState.BACK_SIDE_SCAN

        runOnUiThread {
            val cardFlippingDelay = startScanningNextSide()
            resumeScanning(cardFlippingDelay)
        }
    }

    @Override
    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        cameraPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (scanFlowState == ScanFlowState.BACK_SIDE_SCAN) {
            restartScanning(false)
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mb_menu_scan, menu)
        torchButtonHandler.torchItem = menu.findItem(R.id.action_torch)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_torch -> {
            torchButtonHandler.onTorchButtonClick(recognizerView, handler)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_CHOOSE_DOC) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val countryCode = data.getStringExtra(ChooseCountryActivity.EXTRA_RESULT_COUNTRY_CODE)
                val country = CountryFactory.getCountryForCode(countryCode)
                if (country != null) {
                    val oldDocumentType = currentDocument.documentType
                    val newDocumentType = if (documentChooser.isDocumentTypeSupportedForCountry(oldDocumentType, country)) {
                        oldDocumentType
                    } else {
                        documentChooser.getDefaultDocumentTypeForCountry(country)
                    }

                    updateDocument(Document(country, newDocumentType))
                } else {
                    Log.e(this, "Unknown country code: {}", countryCode)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @UiThread
    private fun restartScanning(shouldClearHandlerScanFlowState: Boolean = true) {
        recognizerView.resetRecognitionState()
        startScan(shouldClearHandlerScanFlowState)
    }

    private fun prepareScanning(document: Document) {
        val recognition = document.getRecognition()

        recognition.setup()
        recognizerManager.addRecognizers(recognition)
    }

    /**
     * Sets up UI and recognizer view for scanning of document's next side.
     */
    @UiThread
    private fun startScanningNextSide(): Long {
        var cardFlippingDelay = 0L
        scanTimeoutHandler.startTimer()
        if (!shouldScanBothDocumentSides()) {
            instructionsHandler.setAnySideInstructions(currentDocument)
        } else {
            cardFlippingDelay = instructionsHandler.updateSideInstructions(currentDocument, scanFlowState)
        }
        updateActiveRecognizers()
        return cardFlippingDelay
    }

    private fun updateActiveRecognizers() {
        val recognizerBundle = recognizerManager.buildRecognizerBundle(scanFlowState)
        if (recognizerView.cameraViewState == BaseCameraView.CameraViewState.STARTED
                || recognizerView.cameraViewState == BaseCameraView.CameraViewState.RESUMED) {
            recognizerView.reconfigureRecognizers(recognizerBundle)
        } else if (recognizerView.cameraViewState == BaseCameraView.CameraViewState.DESTROYED) {
            recognizerView.recognizerBundle = recognizerBundle
        }
    }

    private fun createDefaultScanTimeoutListener(): ScanTimeoutHandler.Listener {
        return object : ScanTimeoutHandler.Listener {
            override fun onTimeout() {
                pauseScanning()

                val dialogBuilder = AlertDialog.Builder(this@BaseDocumentScanActivity).apply {
                    setCancelable(true)
                    setMessage(R.string.mb_timeout_message)
                    setTitle(R.string.mb_timeout_title)
                    setPositiveButton(R.string.mb_timeout_retry) { _, _ -> onRetry() }
                    setNeutralButton(R.string.mb_timeout_change_country) { _, _ ->
                        resumeScanningImmediately()
                        documentChooser.onChooseCountryClick(currentDocument)
                    }
                    setOnCancelListener { onRetry() }
                }

                if (!isFinishing) {
                    dialogBuilder.show()
                }
            }

            fun onRetry() {
                resumeScanningImmediately()
            }
        }
    }

    private fun createLocaleBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                CountryFactory.setLocale(Locale(getString(R.string.mb_locale_language_code)))
            }
        }
    }

    private fun createFrameCallback() = object : FrameCallback {
        override fun writeToParcel(dest: Parcel?, flags: Int) {
        }

        override fun describeContents() = 0

        override fun onFrameAvailable(cameraFrame: Image?, isFocused: Boolean, frameQuality: Double) {
            frameListener.onFrameAvailable(cameraFrame)
        }
    }

    private fun createCameraEventsListener(): CameraEventsListener? {
        return object: CameraEventsListener {

            override fun onCameraPreviewStarted() {
                if (recognizerView.isCameraTorchSupported) {
                    torchButtonHandler.onTorchSupported()
                }

                if (splash_overlay.visibility == View.VISIBLE) {
                    splash_overlay.fadeOut(splashOverlaySettings.getDurationMillis())
                }

                if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                    recognizerView.setMeteringAreas(arrayOf(RectF(0.33f, 0.33f, 0.66f, 0.66f)), true)
                }

                pauseScanning()
                resumeScanning()
            }

            override fun onCameraPreviewStopped() {}

            override fun onCameraPermissionDenied() {
                cameraPermissionManager.askForCameraPermission()
            }

            override fun onError(exc: Throwable) {
                cameraErrorHandler.handleCameraError(exc)
                onCameraError(exc)
            }

            override fun onAutofocusFailed() {}

            override fun onAutofocusStarted(focusAreas: Array<out Rect>?) {}

            override fun onAutofocusStopped(focusAreas: Array<out Rect>?) {}
        }
    }

    companion object {
        /**
         * Resume scanning delay when detector recognizer is activated to prevent immediate scanning done event.
         */
        private const val DELAY_DETECTOR_RECOGNIZER_RESUME = 3_000L
        /**
         * Resume scanning delay when document face recognizer is activated to prevent
         * returning low quality face images when result is returned when camera is not
         * ready (low light images).
         */
        private const val DELAY_DOCUMENT_FACE_RESUME = 500L
        const val REQ_CODE_CHOOSE_DOC = 123

        private const val SCAN_TIMEOUT_MILLIS = 16_000L
    }

}
