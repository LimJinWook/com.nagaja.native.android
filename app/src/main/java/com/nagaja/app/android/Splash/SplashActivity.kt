package com.nagaja.app.android.Splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_CHAT
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_EVENT
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_NOTE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_NOTICE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_REPORT_MISSING
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_RESERVATION
import com.nagaja.app.android.Data.PushData
import com.nagaja.app.android.Login.LoginActivity
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.Permission.PermissionActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import java.util.*


class SplashActivity : NagajaActivity(), SplashFragment.OnSplashFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (intent.getBooleanExtra(INTENT_DATA_IS_PUSH_DATA, false)) {
            val pushType = intent.getStringExtra(INTENT_DATA_PUSH_TYPE_DATA)

            val pushData = PushData()
            if (pushType == PUSH_TYPE_RESERVATION) {
                val reservateType = intent.getIntExtra(INTENT_DATA_PUSH_RESERVATE_TYPE_DATA, -1)
                val reservateCompanyUid = intent.getIntExtra(INTENT_DATA_PUSH_RESERVATE_COMPANY_UID_DATA, 0)

                NagajaLog().e("wooks, !!! reservateType = $reservateType")
                NagajaLog().e("wooks, !!! reservateCompanyUid = $reservateCompanyUid")

                pushData.setType(pushType)
                pushData.setReservateType(reservateType)
                pushData.setReservateCompanyUid(reservateCompanyUid)
            } else if (pushType == PUSH_TYPE_NOTICE || pushType == PUSH_TYPE_EVENT) {
                val topicCategoryUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, 0)
                val topicBoardUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, 0)
                val topicWebLink = intent.getStringExtra(INTENT_DATA_PUSH_WEB_LINK_DATA)

                pushData.setType(pushType)
                pushData.setTopicCategoryUid(topicCategoryUid)
                pushData.setTopicBoardUid(topicBoardUid)
                pushData.setWebLinkUrl(topicWebLink!!)
            } else if (pushType == PUSH_TYPE_REPORT_MISSING) {
                val categoryUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, 0)
                val boardUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, 0)

                pushData.setType(pushType)
                pushData.setTopicCategoryUid(categoryUid)
                pushData.setTopicBoardUid(boardUid)
            } else if (pushType == PUSH_TYPE_NOTE) {
                val categoryUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, 0)
                val boardUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, 0)
                val companyUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_COMPANY_UID_DATA, 0)

                pushData.setType(pushType)
                pushData.setTopicBoardUid(boardUid)
                pushData.setCompanyUid(companyUid)
            } else if (pushType == PUSH_TYPE_CHAT) {
                val boardUid = intent.getIntExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, 0)

                pushData.setType(pushType)
                pushData.setTopicBoardUid(boardUid)
            }

            MAPP.PUSH_DATA = pushData

            NagajaLog().e("wooks, MAPP.PUSH_DATA 11 = ${MAPP.PUSH_DATA!!.getType()}")
        }

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = SplashFragment::class.java.name
        val selectLanguageFragment = SplashFragment.newInstance()
        fragmentTransaction.add(R.id.activity_splash_container, selectLanguageFragment, tag).commit()
    }

    private fun showMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun showPermissionScreen() {
        val intent = Intent(this, PermissionActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun onFinish() {
        finish()
    }

    override fun onPermissionScreen() {
        showPermissionScreen()
    }

    override fun onLoginScreen() {
        showLoginScreen()
    }


    override fun onMainScreen() {
        showMainScreen()
    }

}