package com.nagaja.app.android.Regular

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
import com.nagaja.app.android.StoreDetail.StoreDetailActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class RegularActivity : NagajaActivity(), RegularFragment.OnRegularFragmentListener {

    var mIsCompanyNote = false
    lateinit var mCompanyDefaultData: CompanyDefaultData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regular)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@RegularActivity)!!, false)

        mIsCompanyNote = intent.getBooleanExtra(INTENT_DATA_IS_COMPANY_NOTE_DATA, false)
        if (mIsCompanyNote) {
            mCompanyDefaultData = intent.getSerializableExtra(INTENT_DATA_COMPANY_DEFAULT_DATA) as CompanyDefaultData
        }

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = RegularFragment::class.java.name
        val regularFragment = RegularFragment.newInstance()
        fragmentTransaction.add(R.id.activity_regular_container, regularFragment, tag).commit()
    }

    private fun showStoreScreen(companyUid: Int) {
        val intent = Intent(this, StoreDetailActivity::class.java)
        intent.putExtra(INTENT_DATA_STORE_UID_DATA, companyUid)
        startActivity(intent)
    }

    private fun showChatViewScreen(chatClass: String, chatData: String) {
        val intent = Intent(this@RegularActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, chatClass)
        intent.putExtra(INTENT_DATA_CHAT_DATA, chatData)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@RegularActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@RegularActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@RegularActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onChatViewScreen(chatClass: String, chatData: String) {
        showChatViewScreen(chatClass, chatData)
    }

    override fun onFinish() {
        finish()
    }

    override fun onStoreScreen(companyUid: Int) {
        showStoreScreen(companyUid)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }
}