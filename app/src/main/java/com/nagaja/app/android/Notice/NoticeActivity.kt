package com.nagaja.app.android.Notice

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.Data.NoticeData
import com.nagaja.app.android.FAQ.FAQDetailFragment
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class NoticeActivity : NagajaActivity(), NoticeFragment.OnNoticeFragmentListener, FAQDetailFragment.OnFAQDetailFragmentListener {

    companion object {
        var mMainMenuItemData = MainMenuItemData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@NoticeActivity)!!, false)

        mMainMenuItemData = intent.getSerializableExtra(INTENT_DATA_STORE_LIST_DATA) as MainMenuItemData

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NoticeFragment::class.java.name
        val noticeFragment = NoticeFragment.newInstance(mMainMenuItemData.getCategoryUid(), mMainMenuItemData.getIsEvent(), mMainMenuItemData.getEventCategoryUid(), mMainMenuItemData.getEventTargetUid())
        fragmentTransaction.add(R.id.activity_notice_container, noticeFragment, tag).commit()
    }

    private fun showNoticeDetailScreen(data: NoticeData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = FAQDetailFragment::class.java.name
        val faqDetailFragment = FAQDetailFragment.newInstance(data)
//        fragmentTransaction.replace(R.id.activity_notice_container, faqDetailFragment, tag).addToBackStack(tag).commit()
        fragmentTransaction.add(R.id.activity_notice_container, faqDetailFragment, tag).addToBackStack(tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@NoticeActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@NoticeActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@NoticeActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onNoticeDetailScreen(data: NoticeData) {
        showNoticeDetailScreen(data)
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onFinish() {
        finish()
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

}