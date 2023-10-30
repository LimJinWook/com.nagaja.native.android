package com.nagaja.app.android.Dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_EN
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_FIL
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_JA
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_KO
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_ZH
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.custom_dialog_select_language.*

class CustomDialogSelectLanguage(context: Context) : Dialog(context) {

    private var mContext: Context = context

    private lateinit var mListener: OnCustomDialogSelectLanguage

    fun setOnCustomDialogListener(listener: OnCustomDialogSelectLanguage) {
        mListener = listener
    }

    interface OnCustomDialogSelectLanguage {
        fun onSelectLanguage(selectLanguage: String)
    }

//    init {
//        this.mDialogType = dialogType
//        this.mTitle = title
//        this.mMessage = message
//        this.mCancelType = cancelType
//        this.mConfirmType = confirmType
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = window!!.attributes
        val wm = mContext.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lp.width = (wm.defaultDisplay.width * 0.9).toInt()
        window!!.attributes = lp
        window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        setContentView(R.layout.custom_dialog_select_language)

        init()
    }

    private fun init() {

        // English View
        val englishView = custom_dialog_select_language_english_view
        englishView.setOnClickListener {
            mListener.onSelectLanguage(SELECT_LANGUAGE_EN)
        }

        // Korean View
        val koreanView = custom_dialog_select_language_korean_view
        koreanView.setOnClickListener {
            mListener.onSelectLanguage(SELECT_LANGUAGE_KO)
        }

        // Filipino View
        val filipino = custom_dialog_select_language_filipino_view
        filipino.setOnClickListener {
            mListener.onSelectLanguage(SELECT_LANGUAGE_FIL)
        }

        // Chinese View
        val chineseView = custom_dialog_select_language_chinese_view
        chineseView.setOnClickListener {
            mListener.onSelectLanguage(SELECT_LANGUAGE_ZH)
        }

        // Japanese View
        val japaneseView = custom_dialog_select_language_japanese_view
        japaneseView.setOnClickListener {
            mListener.onSelectLanguage(SELECT_LANGUAGE_JA)
        }

    }

}