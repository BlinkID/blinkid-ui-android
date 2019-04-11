package com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.fragments

import android.annotation.SuppressLint
import android.app.ListFragment
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.country.CountryFactory
import com.microblink.documentscanflow.getThemeColor
import com.microblink.documentscanflow.setVisible
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.AllowAllCountryFilter
import com.microblink.documentscanflow.ui.documentchooser.countryfilter.CountryFilter
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.adapters.AlphabeticListAdapter
import kotlinx.android.synthetic.main.mb_fragment_country_list.*
import java.util.*


class CountryListFragment : ListFragment() {

    private var mAdapter: AlphabeticListAdapter? = null
    private var mSideIndexHeight: Int = 0
    private var mIndexAlphabet: Array<String>? = null

    /**
     * In this list, for each index letter, will be stored index of the first adapter section that
     * belongs to letter with corresponding index (position = letter index; value = index of the
     * first adapter section)
     */
    private var mIndexSections: MutableList<Int>? = null
    /**
     * In this list, for each adapter section, will be stored index of the letter that this section
     * belongs to
     */
    private var mSectionIndexes: Array<Int>? = null

    private val mIndexTextViews: MutableList<TextView> = ArrayList()
    private var mVisibleSections: MutableSet<Int> = HashSet()
    private var mIndexHeightPx: Int = 0

    private var shouldShowIndexerSidebar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentCountryIndex = -1
        var countryFilter : CountryFilter = AllowAllCountryFilter()

        if (arguments != null) {
            shouldShowIndexerSidebar = arguments.getBoolean(EXTRA_SHOW_INDEXER_SIDEBAR)
            val countryCode = arguments.getString(EXTRA_CURRENT_COUNTRY_CODE)
            countryFilter = arguments.getParcelable(EXTRA_COUNTRY_FILTER) ?: AllowAllCountryFilter()
            val countries = CountryFactory.getSortedCountries(countryFilter)
            for ((index, countrySett) in countries.withIndex()) {
                if (countrySett.code == countryCode) {
                    currentCountryIndex = index
                    break
                }
            }
        }

        mAdapter = AlphabeticListAdapter(CountryFactory.getSortedCountries(countryFilter), currentCountryIndex)
        mIndexAlphabet = mAdapter!!.sections
        mSectionIndexes = Array(mAdapter!!.sections.size) { i -> i}
        mIndexSections = ArrayList(mAdapter!!.sections.size)
        for (i in mAdapter!!.sections.indices) {
            mIndexSections!!.add(i)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.mb_fragment_country_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listAdapter = mAdapter
        listView.setOnScrollListener(object: AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    val focusedView = activity.currentFocus
                    if (focusedView != null) {
                        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                    }
                }
            }

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (mIndexTextViews.size > 0) {
                    updateVisibleSections()
                }
            }

        })
        listView.setSelectionFromTop(mAdapter!!.mSelectedRowIndex, 0)

        alphabetIndexerContainer.setVisible(shouldShowIndexerSidebar)
    }

    override fun onResume() {
        super.onResume()
        setupSideIndex()
    }

    override fun onListItemClick(list: ListView?, view: View?, position: Int, id: Long) {
        val countrySett = mAdapter!!.getItem(position)
        if (countrySett != null) {
            onCountryClick(activity, countrySett.code)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSideIndex() {
        val observer = alphabetIndexer.viewTreeObserver
        if (observer.isAlive) {
            observer.addOnGlobalLayoutListener {
                if (alphabetIndexer != null) {
                    val measuredHeightPx = alphabetIndexer.measuredHeight
                    if (measuredHeightPx != mIndexHeightPx) {
                        // do calculations only if measuredHeight is changed
                        mIndexHeightPx = measuredHeightPx
                        mIndexAlphabet = calculateIndexAlphabet(measuredHeightPx)
                        drawSideIndex()
                        updateVisibleSections()
                    }
                }
            }
        } else {
            // add all sections to side index
            mIndexAlphabet = calculateRestrictedIndexAlphabet(mAdapter!!.sections.size)
            drawSideIndex()
            updateVisibleSections()
        }

        alphabetIndexer.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scrollToSection(event.x, event.y)
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    // to enable fast scrolling use commented line
//                    scrollToItem(event.x, event.y)
                    scrollToSection(event.x, event.y)
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun calculateIndexAlphabet(measuredHeightPx: Int): Array<String> {
        val heightDp = (measuredHeightPx / Resources.getSystem().displayMetrics.density).toInt()
        // added +1 dp because of text view padding
        val maxAllowedIndexElementsNum = (heightDp / (INDEX_TEXT_SIZE_DP + 1)).toInt()
        return if (maxAllowedIndexElementsNum < mAdapter!!.sections.size) {
            calculateRestrictedIndexAlphabet(maxAllowedIndexElementsNum)
        } else {
            // add all sections to side index
            calculateRestrictedIndexAlphabet(mAdapter!!.sections.size)
        }
    }

    private fun calculateRestrictedIndexAlphabet(maxAllowedIndexElementsNum: Int): Array<String> {
        val sectionsNum = mAdapter!!.sections.size
        val surplusNum = (sectionsNum - maxAllowedIndexElementsNum)
        val newAlphabet = ArrayList<String>()
        var nextSectionToAdd = 0
        mIndexSections!!.clear()
        if (surplusNum > 0) {
            // queue of pairs (<start index of sections to remove>, <total number of elements in sections>)
            // removed sections will be sections with indexes:
            // <start index of sections to remove>..<start index of sections to remove> + step - 1
            val queue = PriorityQueue<Pair<Int, Int>>(surplusNum, Comparator<Pair<Int, Int>> { a, b ->
                if (a.second < b.second) {
                    return@Comparator 1
                } else if (a.second > b.second) {
                    return@Comparator -1
                }
                return@Comparator 0
            })
            // step is 2 because if we want to have one section less, we have to replace two sections with '•'
            val step = 2
            for (i in 0 until (sectionsNum - step)) {
                val pair = Pair(i, mAdapter!!.getPositionForSection(i + step) - mAdapter!!.getPositionForSection(i))
                queue.add(pair)
                if (queue.size > surplusNum) {
                    // keep only number of pairs that are relevant to sections which
                    // should be removed from index
                    queue.poll()
                }
            }
            // start indexes of section ranges that should be removed
            val startGroupIndexes = LinkedList<Int>()
            for (pair in queue) {
                startGroupIndexes.add(pair.first)
            }
            // start indexes have to be sorted for later processing
            startGroupIndexes.sort()

            var i = 0
            var firstSectionAfterGroup = -1
            while (i < startGroupIndexes.size) {
                for (sectIndex in nextSectionToAdd until startGroupIndexes[i]) {
                    // add all sections before next section that should be removed
                    newAlphabet.add(mAdapter!!.sections[sectIndex])
                    mIndexSections!!.add(newAlphabet.lastIndex, sectIndex)
                    mSectionIndexes!![sectIndex] = newAlphabet.lastIndex
                }
                if (startGroupIndexes[i] > firstSectionAfterGroup) {
                    // add "•" section only first time for concatenated group
                    // else concatenate groups of sections to remove
                    newAlphabet.add("•")
                }
                for (sectIndex in startGroupIndexes[i] until (startGroupIndexes[i] + step)) {
                    mIndexSections!!.add(newAlphabet.lastIndex, sectIndex)
                    mSectionIndexes!![sectIndex] = newAlphabet.lastIndex
                }
                nextSectionToAdd = startGroupIndexes[i] + step
                firstSectionAfterGroup = nextSectionToAdd
                i++
            }
        }
        for (sectIndex in nextSectionToAdd until mAdapter!!.sections.size) {
            // add all remaining sections
            newAlphabet.add(mAdapter!!.sections[sectIndex])
            mSectionIndexes!![sectIndex] = newAlphabet.lastIndex
            mIndexSections!!.add(newAlphabet.lastIndex, sectIndex)
        }
        return newAlphabet.toTypedArray()
    }

    private fun drawSideIndex() {
        alphabetIndexer.removeAllViews()
        mIndexTextViews.clear()
        for (i in mIndexAlphabet!!.indices) {
            val tvIndex = TextView(activity)
            tvIndex.text = mIndexAlphabet!![i]
            tvIndex.setTextSize(TypedValue.COMPLEX_UNIT_DIP, INDEX_TEXT_SIZE_DP)
            tvIndex.gravity = Gravity.CENTER
            tvIndex.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            setTextViewStyleToNormal(tvIndex)
            alphabetIndexer.addView(tvIndex)
            mIndexTextViews.add(i, tvIndex)
        }
    }

    private fun updateVisibleSections() {
        val firstVisibleSection = mAdapter!!.getSectionForPosition(listView.firstVisiblePosition)
        val lastVisibleSection = mAdapter!!.getSectionForPosition(listView.lastVisiblePosition)

        if (firstVisibleSection and lastVisibleSection in mSectionIndexes!!.indices) {
            for (i in mVisibleSections) {
                if (i !in firstVisibleSection..lastVisibleSection && mSectionIndexes!![i] in mIndexTextViews.indices) {
                    setTextViewStyleToNormal(mIndexTextViews[mSectionIndexes!![i]])
                }
            }
            mVisibleSections.clear()
            for (i in firstVisibleSection..lastVisibleSection) {
                if (mSectionIndexes!![i] in mIndexTextViews.indices) {
                    setTextViewStyleToAccent(mIndexTextViews[mSectionIndexes!![i]])
                }
                mVisibleSections.add(i)
            }
        }
    }

    private fun setTextViewStyleToNormal(tv: TextView) {
        tv.setTextColor(activity.getThemeColor(R.attr.mbCcTextColorIndexLetterNormal))
        tv.setTypeface(null, Typeface.NORMAL)
    }

    private fun setTextViewStyleToAccent(tv: TextView) {
        tv.setTextColor(activity.getThemeColor(R.attr.mbCcTextColorIndexLetterSelected))
        tv.setTypeface(null, Typeface.BOLD)
    }

    private fun scrollToSection(positionX: Float, positionY: Float) {
        mSideIndexHeight = alphabetIndexer.height
        // compute number of pixels for every side index item
        val pixelPerIndexItem = mSideIndexHeight.toDouble() / mIndexAlphabet!!.size

        // compute the item index for given event position belongs to
        val alphabetPosition = (positionY / pixelPerIndexItem).toInt()
        if (alphabetPosition in mIndexAlphabet!!.indices) {
            listView.setSelection(mAdapter!!.getPositionForSection(mIndexSections!![alphabetPosition]))
        }
    }

    companion object {
        const val EXTRA_CURRENT_COUNTRY_CODE = "EXTRA_CURRENT_COUNTRY_CODE"
        const val EXTRA_COUNTRY_FILTER = "EXTRA_COUNTRY_FILTER"
        const val EXTRA_SHOW_INDEXER_SIDEBAR = "EXTRA_SHOW_INDEXER_SIDEBAR"
        private const val INDEX_TEXT_SIZE_DP = 14f
    }

}

