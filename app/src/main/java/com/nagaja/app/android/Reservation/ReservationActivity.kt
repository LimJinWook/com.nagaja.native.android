package com.nagaja.app.android.Reservation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.ChatView.ChatViewActivity
import com.nagaja.app.android.CompanyReservation.CompanyReservationFragment
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.StoreDetail.StoreDetailActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class ReservationActivity : NagajaActivity(), ReservationFragment.OnReportFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@ReservationActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ReservationFragment::class.java.name
        val reservationFragment = ReservationFragment.newInstance()
        fragmentTransaction.add(R.id.activity_reservation_container, reservationFragment, tag).commit()
    }

    private fun showStoreDetailScreen(companyUid: Int) {
        val intent = Intent(this@ReservationActivity, StoreDetailActivity::class.java)
        intent.putExtra(NagajaActivity.INTENT_DATA_STORE_UID_DATA, companyUid)
        startActivity(intent)
    }

    private fun showChatViewScreen(chatClass: String, chatData: String) {
        val intent = Intent(this@ReservationActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, chatClass)
        intent.putExtra(INTENT_DATA_CHAT_DATA, chatData)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@ReservationActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@ReservationActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@ReservationActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStoreDetailScreen(companyUid: Int) {
        showStoreDetailScreen(companyUid)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onChatViewScreen(chatClass: String, chatData: String) {
        showChatViewScreen(chatClass, chatData)
    }

    override fun onFinish() {
        onBackPressed()
    }

    override fun onBackPressed() {
        try {
            val reservationFragment: ReservationFragment? = supportFragmentManager.findFragmentById(R.id.activity_reservation_container) as ReservationFragment?
            if (reservationFragment!!.isCheckInformationView()) {
                reservationFragment.hideInformationView()
            } else {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

}