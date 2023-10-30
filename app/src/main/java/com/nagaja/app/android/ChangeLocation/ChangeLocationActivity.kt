package com.nagaja.app.android.ChangeLocation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.util.*


class ChangeLocationActivity : NagajaActivity(), ChangeLocationFragment.OnChangeLocationFragmentListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location)


        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@ChangeLocationActivity)!!, false)
        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ChangeLocationFragment::class.java.name
        val changeLocationFragment: ChangeLocationFragment = ChangeLocationFragment.newInstance()
        fragmentTransaction.add(R.id.activity_change_location_container, changeLocationFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }

    override fun onSuccessChangeLocation() {
//        setResult(RESULT_OK)
//        finish()

        val intent = Intent(this@ChangeLocationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

        finish()
    }

}