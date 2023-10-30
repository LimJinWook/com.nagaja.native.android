package com.nagaja.app.android.Map

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class MapActivity : NagajaActivity(), MapFragment.OnMapFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@MapActivity)!!, false)

        val location = intent.getStringExtra(INTENT_DATA_LOCATION)
        val address = intent.getStringExtra(INTENT_DATA_ADDRESS_DATA)
        val storeName = intent.getStringExtra(INTENT_DATA_STORE_NAME_DATA)

        init(location!!, address!!, storeName!!)
    }

    private fun init(location: String, address: String, storeName: String) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = MapFragment::class.java.name
        val mapFragment = MapFragment.newInstance(location, address, storeName)
        fragmentTransaction.add(R.id.activity_map_container, mapFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }

    override fun onConfirm() {
        finish()
    }

    override fun onSelectLocation(latitude: String, longitude: String) {
        intent.putExtra(INTENT_DATA_SELECT_LATITUDE_DATA, latitude)
        intent.putExtra(INTENT_DATA_SELECT_LONGITUDE_DATA, longitude)
        setResult(RESULT_OK, intent)
        finish()
    }

}