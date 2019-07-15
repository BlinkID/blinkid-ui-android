package com.microblink.documentscanflow.ui.documentchooser

import android.app.Activity
import android.content.Intent
import com.microblink.documentscanflow.BaseDocumentScanActivity
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.country.Country
import com.microblink.documentscanflow.country.CountryFactory
import com.microblink.documentscanflow.document.Document
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.AllowAllCountryFilter
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.ChooseCountryActivity

open class DefaultDocumentChooser(private val scanActivity: Activity) : DocumentChooser {

    override fun shouldShowDocumentTypeTabs() = true

    override fun shouldShowCountryChooser() = true

    override fun getAllowedDocumentTypes(country: Country) = country.getSupportedDocumentTypes()

    override fun geChooseCountryLabel() =
            scanActivity.getString(R.string.mb_supported_countries_label)

    override fun getDefaultDocumentTypeForCountry(country: Country) =
            country.getSupportedDocumentTypes()[0]

    override fun onChooseCountryClick(currentDocument : Document) {
        val intent = Intent(scanActivity, ChooseCountryActivity::class.java).apply {
            putExtra(ChooseCountryActivity.EXTRA_CURRENT_COUNTRY_CODE, currentDocument.country.code)
            putExtra(ChooseCountryActivity.EXTRA_COUNTRY_FILTER, getCountryFilter())
            putExtra(ChooseCountryActivity.EXTRA_SHOW_INDEXER_SIDEBAR, shouldShowAlphabetIndexerSidebar())
            putExtra(ChooseCountryActivity.EXTRA_THEME, getActivityTheme())
        }
        scanActivity.startActivityForResult(intent, BaseDocumentScanActivity.REQ_CODE_CHOOSE_DOC)
    }

    protected open fun getActivityTheme() = R.style.MbChooseCountryLightTheme

    protected open fun getCountryFilter() = AllowAllCountryFilter()

    protected open fun shouldShowAlphabetIndexerSidebar() =
            CountryFactory.getSortedCountries(getCountryFilter()).size > 50

}
