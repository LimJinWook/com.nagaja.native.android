package com.nagaja.app.android.CompanyReservation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.ChatView.ChatViewActivity
import com.nagaja.app.android.Data.CompanyDefaultData
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class CompanyReservationActivity : NagajaActivity(), CompanyReservationFragment.OnCompanyReservationFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_reservation)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@CompanyReservationActivity)!!, false)

        val data = intent.getSerializableExtra(INTENT_DATA_COMPANY_DEFAULT_DATA) as CompanyDefaultData

        init(data)
    }

    private fun init(data: CompanyDefaultData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = CompanyReservationFragment::class.java.name
        val faqFragment: CompanyReservationFragment = CompanyReservationFragment.newInstance(data)
        fragmentTransaction.add(R.id.activity_company_reservation_container, faqFragment, tag).commit()
    }

    private fun showChatViewScreen(chatClass: String, chatData: String) {
        val intent = Intent(this@CompanyReservationActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, chatClass)
        intent.putExtra(INTENT_DATA_CHAT_DATA, chatData)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@CompanyReservationActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@CompanyReservationActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@CompanyReservationActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
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
            val companyReservationFragment: CompanyReservationFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_reservation_container) as CompanyReservationFragment?
            if (companyReservationFragment!!.isCheckInformationView()) {
                companyReservationFragment.hideInformationView()
            } else {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }
}