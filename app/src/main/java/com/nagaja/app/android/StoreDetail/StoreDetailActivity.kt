package com.nagaja.app.android.StoreDetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.ChatView.ChatViewActivity
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.Data.StoreDetailData
import com.nagaja.app.android.Data.StoreDetailReviewData
import com.nagaja.app.android.Data.WriteReviewData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Map.MapActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Report.ReportActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.StoreReservation.StoreReservationActivity
import com.nagaja.app.android.WriteReview.WriteReviewActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class StoreDetailActivity : NagajaActivity(), StoreDetailFragment.OnStoreDetailFragmentListener {

    var mCompanyUid = 0

    companion object {
        var mChangeStatus = false
        var mIsStatus = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@StoreDetailActivity)!!, false)

        mCompanyUid = intent.getIntExtra(INTENT_DATA_STORE_UID_DATA, 0)

        init(mCompanyUid)
    }

    private fun init(companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = StoreDetailFragment::class.java.name
        val storeDetailFragment = StoreDetailFragment.newInstance(companyUid)
        fragmentTransaction.add(R.id.activity_store_detail_container, storeDetailFragment, tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this@StoreDetailActivity, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showMapScreen(location: String, address: String, storeName: String) {
        val intent = Intent(this@StoreDetailActivity, MapActivity::class.java)
        intent.putExtra(INTENT_DATA_LOCATION, location)
        intent.putExtra(INTENT_DATA_ADDRESS_DATA, address)
        intent.putExtra(INTENT_DATA_STORE_NAME_DATA, storeName)
        startActivity(intent)
    }

    private fun showWriteReviewScreen(writeReviewData: WriteReviewData) {
        val intent = Intent(this@StoreDetailActivity, WriteReviewActivity::class.java)
        intent.putExtra(INTENT_DATA_WRITE_REVIEW_DATA, writeReviewData)
        intent.putExtra(INTENT_DATA_WRITE_REVIEW_IS_MODIFY_DATA, false)
        startActivityForResult(intent, INTENT_WRITE_REVIEW_SUCCESS_REQUEST_CODE)
    }

    private fun showWriteReviewScreen(data: StoreDetailReviewData) {
        val intent = Intent(this@StoreDetailActivity, WriteReviewActivity::class.java)
        intent.putExtra(INTENT_DATA_WRITE_REVIEW_DATA, data)
        intent.putExtra(INTENT_DATA_WRITE_REVIEW_IS_MODIFY_DATA, true)
        startActivityForResult(intent, INTENT_WRITE_REVIEW_SUCCESS_REQUEST_CODE)
    }

    private fun showReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        val intent = Intent(this@StoreDetailActivity, ReportActivity::class.java)
        intent.putExtra(INTENT_DATA_REPORT_TYPE_DATA, reportType)
        intent.putExtra(INTENT_DATA_REPORT_UID_DATA, reviewUid)
        intent.putExtra(INTENT_DATA_REPORT_STORE_NAME_DATA, storeName)
        startActivity(intent)
    }

    private fun showStoreReservationScreen(data: StoreDetailData) {
        val intent = Intent(this@StoreDetailActivity, StoreReservationActivity::class.java)
        intent.putExtra(INTENT_DATA_STORE_DETAIL_DATA, data)
        startActivity(intent)
    }

    private fun showChatViewScreen(chatClass: String, chatData: String) {
        val intent = Intent(this@StoreDetailActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, chatClass)
        intent.putExtra(INTENT_DATA_CHAT_DATA, chatData)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@StoreDetailActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@StoreDetailActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@StoreDetailActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFinish() {
        if (mChangeStatus) {
            intent.putExtra(INTENT_DATA_STORE_UID_DATA, mCompanyUid)
            intent.putExtra(INTENT_DATA_IS_STORE_STATUS_DATA, mIsStatus)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        onFinish()
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onMapScreen(location: String, address: String, storeName: String) {
        showMapScreen(location, address, storeName)
    }

    override fun onWriteReviewScreen(writeReviewData: WriteReviewData) {
        showWriteReviewScreen(writeReviewData)
    }

    override fun onWriteReviewScreen(data: StoreDetailReviewData) {
        showWriteReviewScreen(data)
    }

    override fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        showReportScreen(reportType, reviewUid, storeName)
    }

    override fun onStoreReservationScreen(data: StoreDetailData) {
        showStoreReservationScreen(data)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_WRITE_REVIEW_SUCCESS_REQUEST_CODE -> {
                    val storeDetailFragment: StoreDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_store_detail_container) as StoreDetailFragment?
                    storeDetailFragment!!.getStoreReviewData(true)
                }
            }
        }
    }
}