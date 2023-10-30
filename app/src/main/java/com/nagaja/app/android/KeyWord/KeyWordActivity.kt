package com.nagaja.app.android.KeyWord

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class KeyWordActivity : NagajaActivity(), KeyWordFragment.OnKeyWordFragmentListener, KeyWordRegisterFragment.OnKeyWordRegisterFragmentListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_word)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@KeyWordActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = KeyWordFragment::class.java.name
        val keyWordFragment = KeyWordFragment.newInstance()
        fragmentTransaction.add(R.id.activity_key_word_container, keyWordFragment, tag).commit()
    }

    private fun showKeyWordRegisterScreen() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = KeyWordRegisterFragment::class.java.name
        val keyWordRegisterFragment = KeyWordRegisterFragment.newInstance()
        fragmentTransaction.replace(R.id.activity_key_word_container, keyWordRegisterFragment, tag).addToBackStack(tag).commit()
    }

    override fun onKeyWordRegisterScreen() {
        showKeyWordRegisterScreen()
    }

    override fun onFinish() {
        finish()
    }

    override fun onBack() {
        onBackPressed()
    }

}