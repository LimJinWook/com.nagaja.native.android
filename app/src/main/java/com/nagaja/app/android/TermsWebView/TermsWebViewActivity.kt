package com.nagaja.app.android.TermsWebView

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class TermsWebViewActivity : NagajaActivity(), TermsTermsWebViewFragment.OnTermsWebViewFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trtms_web_view)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@TermsWebViewActivity)!!, false)

        val selectType = intent.getIntExtra(INTENT_DATA_SELECT_TERMS_TYPE_DATA, 1)
        val url = intent.getStringExtra(INTENT_DATA_SELECT_TERMS_URL_DATA)

        if (!TextUtils.isEmpty(url)) {
            init(selectType, url!!)
        }
    }

    private fun init(selectType: Int, url: String) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = TermsTermsWebViewFragment::class.java.name
        val termsTermsWebViewFragment = TermsTermsWebViewFragment.newInstance(selectType, url)
        fragmentTransaction.add(R.id.activity_terms_web_view_container, termsTermsWebViewFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }

}