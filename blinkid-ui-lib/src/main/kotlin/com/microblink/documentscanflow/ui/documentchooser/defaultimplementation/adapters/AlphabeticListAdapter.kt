package com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.adapters

import android.graphics.PorterDuff
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SectionIndexer
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.country.Country
import com.microblink.documentscanflow.getThemeColor
import com.microblink.documentscanflow.inflate
import kotlinx.android.synthetic.main.mb_list_item_country.view.*
import java.util.*

class AlphabeticListAdapter(elementList: List<Country>, selectedIndex: Int) : BaseAdapter(), SectionIndexer {

    private val mRows: List<ItemRow>
    // key - section name; value - section position in list
    private val mSectionPositions: HashMap<String, Int>
    private val mSections: List<String>
    // index of the row that should be initially selected (list scrolled to this item)
    var mSelectedRowIndex = 0
        private set

    init {
        mRows = ArrayList()
        mSections = ArrayList()
        mSectionPositions = HashMap()
        var prevChar = '0'
        for ((index, country) in elementList.withIndex()) {
            val countryName = country.getLocalisedName()
            if (countryName.isEmpty()) continue

            val firstChar = countryName[0]
            var sectionNameToShow = ""
            if (prevChar != firstChar) {
                val sectionName = firstChar.toString()
                sectionNameToShow = sectionName
                mSections.add(sectionName)
                mSectionPositions[sectionName] = index
                prevChar = firstChar
            }

            if (index == selectedIndex) {
                mSelectedRowIndex = mRows.count()
            }

            mRows.add(ItemRow(country, index == selectedIndex, sectionNameToShow))
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = mRows[position]
        return row.getView(convertView, parent)
    }

    /**
     * Gets the country settings for the country at the given position in the adapter. Section
     * entries are counted too.
     * @param position Position of country in the list adapter
     * @return country settings for the country at the given position in the adapter, or null
     * if there is no country at the specified position.
     */
    override fun getItem(position: Int): Country? {
        if (position !in mRows.indices) {
            return null
        }
        val row = mRows[position]
        return row.country
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mRows.size
    }

    override fun getSections(): Array<String> {
        return mSections.toTypedArray()
    }

    override fun getSectionForPosition(position: Int): Int {
        if (position !in mRows.indices) {
            return -1
        }

        for (i in position downTo 0) {
            val row = mRows[i]
            val sectionName = row.getSectionName()
            if (!TextUtils.isEmpty(sectionName)) {
                return mSections.indexOf(sectionName)
            }
        }
        return -1
    }

    override fun getPositionForSection(section: Int): Int {
        if (section !in mSections.indices) {
            return -1
        }
        return mSectionPositions[mSections[section]] ?: -1
    }


    class ItemRow(val country: Country, private val active: Boolean, private val sectionNameToShow: String) {
        fun getView(convertView: View?, parent: ViewGroup): View {
            val view: View = convertView ?: parent.inflate(R.layout.mb_list_item_country)
            view.itemSelectedIcon.visibility = View.GONE
            view.countryName.text = country.getLocalisedName()
            if (active) {
                view.itemSelectedIcon.visibility = View.VISIBLE
                view.itemSelectedIcon.drawable.mutate().setColorFilter(view.context.getThemeColor(R.attr.mbCcIconColorCheckMark), PorterDuff.Mode.MULTIPLY)
            }
            if (TextUtils.isEmpty(sectionNameToShow)) {
                view.sectionName.visibility = View.INVISIBLE
            } else {
                view.sectionName.visibility = View.VISIBLE
                view.sectionName.text = sectionNameToShow
            }
            return view
        }

        fun getSectionName() = sectionNameToShow
    }

}
