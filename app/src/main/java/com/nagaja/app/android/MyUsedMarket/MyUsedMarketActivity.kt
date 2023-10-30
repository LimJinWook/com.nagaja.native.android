package com.nagaja.app.android.MyUsedMarket

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.UsedMarketDetail.UsedMarketDetailActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class MyUsedMarketActivity : NagajaActivity(), MyUsedMarketFragment.OnMyUsedMarketListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_used_market)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@MyUsedMarketActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = MyUsedMarketFragment::class.java.name
        val myUsedMarketFragment = MyUsedMarketFragment.newInstance()
        fragmentTransaction.add(R.id.activity_my_used_market_container, myUsedMarketFragment, tag).commit()
    }

    private fun showUsedMarketDetailScreen(itemUid: Int) {
        val intent = Intent(this, UsedMarketDetailActivity::class.java)
        intent.putExtra(INTENT_DATA_USED_MARKET_DATA, itemUid)
//        startActivityForResult(intent, INTENT_USED_MARKET_REQUEST_CODE)
        startActivityForResult(intent, INTENT_USED_MARKET_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@MyUsedMarketActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@MyUsedMarketActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@MyUsedMarketActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onUsedMarketDetailScreen(itemUid: Int) {
        showUsedMarketDetailScreen(itemUid)
    }

    override fun onFinish() {
        finish()
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_USED_MARKET_REQUEST_CODE -> {
                    val myUsedMarketFragment: MyUsedMarketFragment? = supportFragmentManager.findFragmentById(R.id.activity_my_used_market_container) as MyUsedMarketFragment?
                    myUsedMarketFragment!!.getMyUsedMarketList(true)
                }
            }
        }
    }
}