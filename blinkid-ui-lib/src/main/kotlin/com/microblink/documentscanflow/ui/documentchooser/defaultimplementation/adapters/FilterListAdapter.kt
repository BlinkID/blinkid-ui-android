package com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.country.Country
import com.microblink.documentscanflow.inflate
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.CountryFilter
import kotlinx.android.synthetic.main.mb_list_item_country.view.*

class FilterListAdapter(private val elementList: List<Country>) : BaseAdapter() {

    private var mShownElements: List<Country> = elementList.toList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: parent.inflate(R.layout.mb_list_item_country)
        view.countryName.text = mShownElements[position].getLocalisedName()
        return view
    }

    override fun getItem(position: Int): Country {
        return mShownElements[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mShownElements.size
    }

    // Filter Class
    fun filter(countryFilter: CountryFilter, matchStr: String) {
        mShownElements = countryFilter.filter(elementList)
        mShownElements = mShownElements.filter { isMatchingFilter(it.getLocalisedName(), matchStr) }
        notifyDataSetChanged()
    }

    private fun isMatchingFilter(countryName: String, filter: String): Boolean {
        for (word in countryName.split(" "))
            if (word.startsWith(filter, true)) {
                return true
            }
        return false
    }

}