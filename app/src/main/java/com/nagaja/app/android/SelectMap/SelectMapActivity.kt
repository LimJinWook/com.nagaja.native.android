package com.nagaja.app.android.SelectMap

import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Data.SelectMapData
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.util.*

class SelectMapActivity : NagajaActivity(), SelectMapFragment.OnSelectMapFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_map)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@SelectMapActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = SelectMapFragment::class.java.name
        val selectMapFragment = SelectMapFragment.newInstance()
        fragmentTransaction.add(R.id.activity_select_map_container, selectMapFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }

    override fun onRegionSetting(data: SelectMapData) {
        intent.putExtra(INTENT_DATA_LOCATION_SELECT_DATA, data)
        setResult(RESULT_OK, intent)

        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        val selectMapFragment: SelectMapFragment? = supportFragmentManager.findFragmentById(R.id.activity_select_map_container) as SelectMapFragment?
        selectMapFragment!!.dispatchTouchEvent()

        return super.dispatchTouchEvent(ev)
    }
}