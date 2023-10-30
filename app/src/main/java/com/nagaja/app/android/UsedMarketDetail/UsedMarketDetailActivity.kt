package com.nagaja.app.android.UsedMarketDetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.ChatView.ChatViewActivity
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Map.MapActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Report.ReportActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.UsedMarketRegister.UsedMarketRegisterActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class UsedMarketDetailActivity : NagajaActivity(), UsedMarketDetailFragment.OnUsedMarketFragmentListener {

    private var mIsSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_used_market_detail)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@UsedMarketDetailActivity)!!, false)

        val itemUid = intent.getIntExtra(INTENT_DATA_USED_MARKET_DATA, 0)

        init(itemUid)
    }

    private fun init(itemUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = UsedMarketDetailFragment::class.java.name
        val usedMarketDetailFragment = UsedMarketDetailFragment.newInstance(itemUid)
        fragmentTransaction.add(R.id.activity_used_market_detail_container, usedMarketDetailFragment, tag).commit()
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
        intent.putExtra(INTENT_DATA_ADDRESS_DATA, "")
        intent.putExtra(INTENT_DATA_STORE_NAME_DATA, "")
        startActivity(intent)
    }

    private fun showReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        val intent = Intent(this@UsedMarketDetailActivity, ReportActivity::class.java)
        intent.putExtra(INTENT_DATA_REPORT_TYPE_DATA, reportType)
        intent.putExtra(INTENT_DATA_REPORT_UID_DATA, reviewUid)
        intent.putExtra(INTENT_DATA_REPORT_STORE_NAME_DATA, storeName)
        startActivity(intent)
    }

    private fun showModifyScreen(itemUid: Int) {
        val intent = Intent(this@UsedMarketDetailActivity, UsedMarketRegisterActivity::class.java)
        intent.putExtra(INTENT_DATA_MY_USED_MARKET_MODIFY_DATA, itemUid)
        startActivityForResult(intent, INTENT_USED_MARKET_REQUEST_CODE)
    }

    private fun showChatViewScreen(chatClass: String, chatData: String) {
        val intent = Intent(this@UsedMarketDetailActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, chatClass)
        intent.putExtra(INTENT_DATA_CHAT_DATA, chatData)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@UsedMarketDetailActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@UsedMarketDetailActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@UsedMarketDetailActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onMapScreen(location: String) {
        showMapScreen(location)
    }

    override fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        showReportScreen(reportType, reviewUid, storeName)
    }

    override fun onModifyScreen(itemUid: Int) {
        showModifyScreen(itemUid)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onSuccessDelete() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onSuccessPullUp() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onChatViewScreen(chatClass: String, chatData: String) {
        showChatViewScreen(chatClass, chatData)
    }

    override fun onFinish() {
        finish()
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//
//        val usedMarketDetailFragment: UsedMarketDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_used_market_detail_container) as UsedMarketDetailFragment?
//        usedMarketDetailFragment!!.dispatchTouchEvent()
//
//        return super.dispatchTouchEvent(ev)
//    }

    override fun onBackPressed() {
        if (mIsSuccess) {
            setResult(RESULT_OK)
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_USED_MARKET_REQUEST_CODE -> {
                    val usedMarketDetailFragment: UsedMarketDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_used_market_detail_container) as UsedMarketDetailFragment?
                    usedMarketDetailFragment!!.getUsedMarketDetailData(true)

                    mIsSuccess = true
                }
            }
        }
    }
}