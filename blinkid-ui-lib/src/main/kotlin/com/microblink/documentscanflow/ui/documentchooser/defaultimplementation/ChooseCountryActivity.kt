package com.microblink.documentscanflow.ui.documentchooser.defaultimplementation

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.fragments.CountryFindFragment
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.fragments.CountryListFragment
import kotlinx.android.synthetic.main.mb_activity_choose_country.*
import android.view.ViewGroup
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.AllowAllCountryFilter
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.CountryFilter

class ChooseCountryActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var searchView: SearchView? = null
    private val countryListFragment = CountryListFragment()
    private val countryFindFragment = CountryFindFragment()

    private var isFindFragmentActive = false
    private lateinit var countryFilter: CountryFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MbBlinkIdUiTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mb_activity_choose_country)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val listFragmentArgs = Bundle()

            val currentCountryCode = extras.getString(EXTRA_CURRENT_COUNTRY_CODE)
            if (currentCountryCode != null) {
                listFragmentArgs.putString(CountryListFragment.EXTRA_CURRENT_COUNTRY_CODE, currentCountryCode)
            }

            countryFilter = extras.getParcelable(EXTRA_COUNTRY_FILTER) as CountryFilter? ?: AllowAllCountryFilter()
            listFragmentArgs.putParcelable(CountryListFragment.EXTRA_COUNTRY_FILTER, countryFilter)

            val shouldShowIndexerSidebar = extras.getBoolean(EXTRA_SHOW_INDEXER_SIDEBAR)
            listFragmentArgs.putBoolean(CountryListFragment.EXTRA_SHOW_INDEXER_SIDEBAR, shouldShowIndexerSidebar)

            countryListFragment.arguments = listFragmentArgs
        }

        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.contentFragment, countryListFragment)
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mb_menu_choose_country, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.apply {
            isSubmitButtonEnabled = false
            setOnQueryTextListener(this@ChooseCountryActivity)
            maxWidth = Integer.MAX_VALUE
            fixSearchViewPadding(this)
        }

        return true
    }

    private fun fixSearchViewPadding(vg: ViewGroup) {
        val childCount = vg.childCount
        for (i in 0 until childCount) {
            val child = vg.getChildAt(i)
            if (child is ViewGroup) {
                fixSearchViewPadding(child)
            }

            val lp = child.layoutParams
            if (lp is ViewGroup.MarginLayoutParams) {
                lp.leftMargin = 0
            }
            child.setPadding(0, 0, 0, 0)
        }
    }

    private fun setFragment(fragment: Fragment) {
        isFindFragmentActive = if (fragment is CountryFindFragment && !isFindFragmentActive) {
            true
        } else if (fragment is CountryListFragment && isFindFragmentActive) {
            false
        } else {
            return
        }
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.contentFragment, fragment)
        ft.commit()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isEmpty()) {
            setFragment(countryListFragment)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            setFragment(countryFindFragment)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            countryFindFragment.find(countryFilter, newText)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (searchView?.isIconified == true) {
            super.onBackPressed()
        } else {
            searchView?.isIconified = true
        }
    }

    companion object {
        const val EXTRA_CURRENT_COUNTRY_CODE = "EXTRA_CURRENT_COUNTRY_CODE"
        const val EXTRA_RESULT_COUNTRY_CODE = "EXTRA_COUNTRY_CODE"
        const val EXTRA_COUNTRY_FILTER = "EXTRA_COUNTRY_FILTER"
        const val EXTRA_SHOW_INDEXER_SIDEBAR = "EXTRA_SHOW_INDEXER_SIDEBAR"
    }

}
