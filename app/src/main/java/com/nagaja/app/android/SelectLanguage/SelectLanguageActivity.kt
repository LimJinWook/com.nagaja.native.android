package com.nagaja.app.android.SelectLanguage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Splash.SplashActivity
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class SelectLanguageActivity : NagajaActivity(), SelectLanguageFragment.OnSelectLanguageFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_language)

        getDynamicLinkUrl()
        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = SelectLanguageFragment::class.java.name
        val selectLanguageFragment = SelectLanguageFragment.newInstance()
        fragmentTransaction.add(R.id.activity_select_language_container, selectLanguageFragment, tag).commit()
    }

    private fun showSplashScreen() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

        finish()
        overridePendingTransition(0, 0)
    }

    private fun getDynamicLinkUrl() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener {
                var dynamicLink: Uri? = null
                if (it != null) {
                    dynamicLink = it.link

                    NagajaLog().d("wooks, dynamicLink = $dynamicLink")
                    NagajaLog().d("wooks, dynamicLink Path = ${dynamicLink!!.path}")

                    if (!TextUtils.isEmpty(dynamicLink.toString())) {
                        MAPP.DYNAMIC_LINK_URL = dynamicLink.toString().substring(dynamicLink.toString().lastIndexOf("=") + 1)
                        NagajaLog().d("wooks, MAPP.DYNAMIC_LINK_URL = ${MAPP.DYNAMIC_LINK_URL}")
                    }
                } else {
                }
            }
            .addOnFailureListener {
            }
    }

    override fun onFinish() {
        finish()
    }

    override fun onSplashScreen() {
        showSplashScreen()
    }
}