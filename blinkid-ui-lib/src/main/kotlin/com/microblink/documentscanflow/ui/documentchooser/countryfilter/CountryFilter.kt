package com.microblink.documentscanflow.ui.documentchooser.countryfilter

import android.os.Parcelable
import com.microblink.documentscanflow.country.Country

interface CountryFilter : Parcelable {

    fun filter(countries: List<Country>): List<Country>

}