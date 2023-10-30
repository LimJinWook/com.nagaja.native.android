package com.nagaja.app.android.Search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.SearchData
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.StoreDetail.StoreDetailActivity
import com.nagaja.app.android.UsedMarketDetail.UsedMarketDetailActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.util.*

class SearchActivity : NagajaActivity(), SearchFragment.OnSearchFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@SearchActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = SearchFragment::class.java.name
        val searchFragment = SearchFragment.newInstance()
        fragmentTransaction.add(R.id.activity_search_container, searchFragment, tag).commit()
    }

    private fun showItemDetailScreen(searchData: SearchData) {
        // Root Category Uid
        // 3 : 기업, 1: 중고마켓
        if (searchData.getRootCategoryUid() == 3) {
            val intent = Intent(this@SearchActivity, StoreDetailActivity::class.java)
            intent.putExtra(INTENT_DATA_STORE_UID_DATA, searchData.getUid())
            startActivity(intent)
        } else {
            val intent = Intent(this@SearchActivity, UsedMarketDetailActivity::class.java)
            intent.putExtra(INTENT_DATA_USED_MARKET_DATA, searchData.getUid())
            startActivity(intent)
        }
    }

    private fun showChangeLocationAndNotificationScreen(isChangeLocation: Boolean) {
        val intent: Intent = if (isChangeLocation) {
            Intent(this@SearchActivity, ChangeLocationActivity::class.java)
        } else {
            Intent(this@SearchActivity, NotificationActivity::class.java)
        }
        startActivity(intent)
    }


    override fun onFinish() {
        finish()
    }

    override fun onItemDetailScreen(searchData: SearchData) {
        showItemDetailScreen(searchData)
    }

    override fun onChangeLocationAndNotificationScreen(isChangeLocation: Boolean) {
        showChangeLocationAndNotificationScreen(isChangeLocation)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

}