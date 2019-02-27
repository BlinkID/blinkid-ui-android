package com.microblink.documentscanflow.ui.localization

internal class DefaultLocalizationManager: LocalizationManager {

    private val supportedLanguages = setOf("en")

    override fun isLanguageSupported(lang: String) = supportedLanguages.contains(lang)

    override fun getDefaultLanguage() = "en"

}