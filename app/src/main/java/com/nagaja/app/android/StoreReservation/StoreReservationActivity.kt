package com.nagaja.app.android.StoreReservation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Data.StoreDetailData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Reservation.ReservationActivity
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class StoreReservationActivity : NagajaActivity(), StoreReservationFragment.OnStoreReservationFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_reservation)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@StoreReservationActivity)!!, false)

        val storeDetailData = intent.getSerializableExtra(INTENT_DATA_STORE_DETAIL_DATA) as StoreDetailData

        init(storeDetailData)
    }

    private fun init(data: StoreDetailData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = StoreReservationFragment::class.java.name
        val storeReservationFragment = StoreReservationFragment.newInstance(data)
        fragmentTransaction.add(R.id.activity_store_reservation_container, storeReservationFragment, tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showReservationScreen() {
        val intent = Intent(this, ReservationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onReservationScreen() {
        showReservationScreen()
    }

    override fun onFinish() {
        finish()
    }

}