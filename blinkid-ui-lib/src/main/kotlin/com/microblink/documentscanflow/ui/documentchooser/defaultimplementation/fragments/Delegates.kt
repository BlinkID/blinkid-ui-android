package com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.fragments

import android.app.Activity
import android.content.Intent
import com.microblink.documentscanflow.ui.documentchooser.defaultimplementation.ChooseCountryActivity

fun onCountryClick(activity: Activity, countryCode: String) {
    val intent = Intent()
    intent.putExtra(ChooseCountryActivity.EXTRA_RESULT_COUNTRY_CODE, countryCode)
    activity.setResult(Activity.RESULT_OK, intent)
    activity.finish()
}