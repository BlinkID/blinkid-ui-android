package com.microblink.documentscanflow.ui.documentchooser

import android.app.Activity
import android.content.Intent
import com.microblink.documentscanflow.BaseDocumentScanActivity
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.country.Country
import com.microblink.documentscanflow.country.CountryFactory
import com.microblink.documentscanflow.document.Document
import com.microblink.documentscanflow.document.DocumentType
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.AllowAllCountryFilter
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.ChooseCountryActivity

open class DefaultDocumentChooser(private val scanActivity: Activity) : DocumentChooser {

    override fun shouldShowDocumentTypeTabs() =
            true

    override fun shouldShowCountryChooser() =
            true

    override fun isDocumentTypeSupportedForCountry(documentType: DocumentType, country: Country) =
            country.getSupportedDocumentTypes().contains(documentType)

    override fun geChooseCountryLabel() =
            scanActivity.getString(R.string.mb_supported_countries_label)!!

    override fun getDefaultDocumentTypeForCountry(country: Country) =
            country.getSupportedDocumentTypes()[0]

    override fun onChooseCountryClick(currentDocument : Document) {
        val intent = Intent(scanActivity, ChooseCountryActivity::class.java)
        intent.putExtra(ChooseCountryActivity.EXTRA_CURRENT_COUNTRY_CODE, currentDocument.country.code)
        intent.putExtra(ChooseCountryActivity.EXTRA_COUNTRY_FILTER, getCountryFilter())
        intent.putExtra(ChooseCountryActivity.EXTRA_SHOW_INDEXER_SIDEBAR, shouldShowAlphabetIndexerSidebar())
        scanActivity.startActivityForResult(intent, BaseDocumentScanActivity.REQ_CODE_CHOOSE_DOC)
    }

    protected open fun getCountryFilter() =
            AllowAllCountryFilter()

    protected open fun shouldShowAlphabetIndexerSidebar() =
        CountryFactory.getSortedCountries(getCountryFilter()).size > 50

}
