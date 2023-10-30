package com.nagaja.app.android.SelectLanguage

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_select_language.view.*
import java.util.*

class SelectLanguageFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mListener: OnSelectLanguageFragmentListener

    interface OnSelectLanguageFragmentListener {
        fun onFinish()
        fun onSplashScreen()
    }

    companion object {
        fun newInstance(): SelectLanguageFragment {
            val args = Bundle()
            val fragment = SelectLanguageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mContainer = inflater.inflate(R.layout.fragment_select_language, container, false)

        init()

        return mContainer
    }

    private fun init() {

        if (!TextUtils.isEmpty(SharedPreferencesUtil().getSelectLanguage(mActivity))) {
            mListener.onSplashScreen()
            return
        }

        // English View
        val englishView = mContainer.fragment_select_language_english_view
        englishView.setOnClickListener {
            SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_EN)
            selectLanguage(SELECT_LANGUAGE_EN)
        }

        // Korean View
        val koreanView = mContainer.fragment_select_language_korean_view
        koreanView.setOnClickListener {
            SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_KO)
            selectLanguage(SELECT_LANGUAGE_KO)
        }

        // Filipino View
        val filipinoView = mContainer.fragment_select_language_filipino_view
        filipinoView.setOnClickListener {
            SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_FIL)
            selectLanguage(SELECT_LANGUAGE_FIL)
        }

        // Chinese View
        val chineseView = mContainer.fragment_select_language_chinese_view
        chineseView.setOnClickListener {
            SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_ZH)
            selectLanguage(SELECT_LANGUAGE_ZH)
        }

        // Japanese View
        val japaneseView = mContainer.fragment_select_language_japanese_view
        japaneseView.setOnClickListener {
            SharedPreferencesUtil().setSelectLanguage(mActivity, SELECT_LANGUAGE_JA)
            selectLanguage(SELECT_LANGUAGE_JA)
        }
    }

    private fun selectLanguage(selectLanguage: String) {

        var locale: Locale? = null
        var language = ""

        if (selectLanguage.equals(SELECT_LANGUAGE_EN, ignoreCase = true)) {
            language = SELECT_LANGUAGE_EN
            locale = Locale.ENGLISH
            SharedPreferencesUtil().setNationPhoneCode(mActivity, "1")
        } else if (selectLanguage.equals(SELECT_LANGUAGE_KO, ignoreCase = true)) {
            language = SELECT_LANGUAGE_KO
            locale = Locale.KOREA
            SharedPreferencesUtil().setNationPhoneCode(mActivity, "82")
        } else if (selectLanguage.equals(SELECT_LANGUAGE_FIL, ignoreCase = true)) {
            language = SELECT_LANGUAGE_FIL
            locale = Locale("fil", "PH")
            SharedPreferencesUtil().setNationPhoneCode(mActivity, "63")
        } else if (selectLanguage.equals(SELECT_LANGUAGE_ZH, ignoreCase = true)) {
            language = SELECT_LANGUAGE_ZH
            locale = Locale.CHINA
            SharedPreferencesUtil().setNationPhoneCode(mActivity, "86")
        } else if (selectLanguage.equals(SELECT_LANGUAGE_JA, ignoreCase = true)) {
            language = SELECT_LANGUAGE_JA
            locale = Locale.JAPAN
            SharedPreferencesUtil().setNationPhoneCode(mActivity, "81")
        } else {
            language = SELECT_LANGUAGE_EN
            locale = Locale.ENGLISH
            SharedPreferencesUtil().setNationPhoneCode(mActivity, "1")
        }



//        val cfg = Configuration()
//        cfg.locale = Locale(language)
//        cfg.setLocale(locale)
//
//        mActivity.resources.updateConfiguration(cfg, null)




//        Handler(Looper.getMainLooper()).postDelayed({
//            val config = mActivity.resources.configuration
//            config.setLocale(locale)
//            config.locale = Locale(language)
//            mActivity.resources.updateConfiguration(config, mActivity.resources.displayMetrics)
//        }, 1000)


        /*Locale.setDefault(locale!!)

        val config1 = mActivity.resources.configuration
        config1.locale = locale
        mActivity.resources.updateConfiguration(
            config1,
            mActivity.resources.displayMetrics
        )*/

        mListener.onSplashScreen()
    }

    /**
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSelectLanguageFragmentListener) {
            mActivity = context as Activity

            if (context is OnSelectLanguageFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnSelectLanguageFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnSelectLanguageFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnSelectLanguageFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}