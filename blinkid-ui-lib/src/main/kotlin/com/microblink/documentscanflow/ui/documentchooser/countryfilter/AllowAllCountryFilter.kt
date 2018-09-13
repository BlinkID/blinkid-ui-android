package com.microblink.documentscanflow.ui.documentchooser.countryfilter

import android.os.Parcel
import android.os.Parcelable
import com.microblink.documentscanflow.country.Country

class AllowAllCountryFilter() : CountryFilter, Parcelable {

    override fun filter(countries: List<Country>) = countries

    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AllowAllCountryFilter> = object : Parcelable.Creator<AllowAllCountryFilter> {
            override fun createFromParcel(source: Parcel): AllowAllCountryFilter = AllowAllCountryFilter(source)
            override fun newArray(size: Int): Array<AllowAllCountryFilter?> = arrayOfNulls(size)
        }
    }
}