package com.nagaja.app.android.Store

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Map.MapActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class StoreActivity : NagajaActivity(), StoreFragment.OnStoreFragmentListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@StoreActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = StoreFragment::class.java.name
        val storeFragment = StoreFragment.newInstance()
        fragmentTransaction.add(R.id.activity_store_container, storeFragment, tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showMapScreen(location: String) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra(INTENT_DATA_LOCATION, location)
        startActivity(intent)
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onMapScreen(location: String) {
        showMapScreen(location)
    }

    override fun onFinish() {
        finish()
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//
//        val storeFragment: StoreFragment? = supportFragmentManager.findFragmentById(R.id.activity_store_container) as StoreFragment?
//        storeFragment!!.dispatchTouchEvent()
//
//        return super.dispatchTouchEvent(ev)
//    }

}