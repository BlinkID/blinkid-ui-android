package com.microblink.documentscanflow.recognition.config

interface RecognitionConfig {

    /**
     * Returns timeout after which partial results will be returned or scanning will be reset
     * depending on isPartialResultAllowed().
     * Timeout starts when the first field is read from the card and resets when document side is changed.
     */
    fun getRecognitionTimeoutSeconds(): Int

    /**
     * Returns true if scanning will finish on timeout even if not all fields were read,
     * false if scanning will restart until all fields were read.
     */
    fun isPartialResultAllowed(): Boolean

}