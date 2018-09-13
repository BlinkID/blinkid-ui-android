package com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.fragments

import android.app.ListFragment
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.microblink.documentscanflow.country.CountryFactory
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.AllowAllCountryFilter
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.CountryFilter
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.adapters.FilterListAdapter

class CountryFindFragment : ListFragment() {

    private val mAdapter: FilterListAdapter = FilterListAdapter(CountryFactory.getSortedCountries(AllowAllCountryFilter()))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.divider = null
        listView.dividerHeight = 0
        listView.setDrawSelectorOnTop(true)
        listAdapter = mAdapter
    }

    override fun onListItemClick(list: ListView?, view: View?, position: Int, id: Long) {
        val countrySett = mAdapter.getItem(position)
        onCountryClick(activity, countrySett.code)
    }

    fun find(countryFilter: CountryFilter, matchStr: String) {
        mAdapter.filter(countryFilter, matchStr)
    }

}