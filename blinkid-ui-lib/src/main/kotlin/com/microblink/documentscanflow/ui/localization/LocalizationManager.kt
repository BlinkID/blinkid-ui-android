package com.microblink.documentscanflow.ui.localization

/**
 * Determines to which languages will the country list be translated to
 * If you want to disable translations for all strings, see https://developer.android.com/studio/build/shrink-code#unused-alt-resources
 */
interface LocalizationManager {

    /**
     * returns true if passed in language is supported
     */
    fun isLanguageSupported(lang: String): Boolean

    /**
     * returns language that should be used if device language is not supported
     */
    fun getDefaultLanguage(): String

}