package com.nagaja.app.android.PointHistory

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class PointHistoryActivity : NagajaActivity(), PointHistoryFragment.OnPointHistoryFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_history)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@PointHistoryActivity)!!, false)

        val companyUid = intent.getIntExtra(INTENT_DATA_COMPANY_UID_DATA, 0)

        init(companyUid)
    }

    private fun init(companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = PointHistoryFragment::class.java.name
        val pointHistoryFragment = PointHistoryFragment.newInstance(companyUid)
        fragmentTransaction.add(R.id.activity_point_history_container, pointHistoryFragment, tag).commit()
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@PointHistoryActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@PointHistoryActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@PointHistoryActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFinish() {
        finish()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }


}