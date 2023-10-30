package com.nagaja.app.android.StoreList

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_USED_MARKET
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.Job.JobRegisterFragment
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.StoreDetail.StoreDetailActivity
import com.nagaja.app.android.UsedMarketDetail.UsedMarketDetailActivity
import com.nagaja.app.android.UsedMarketRegister.UsedMarketRegisterActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class StoreListActivity : NagajaActivity(), StoreListFragment.OnStoreListFragmentListener,
    UsedMarketListFragment.OnUsedMarketListFragmentListener {

    private var categoryUid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_list)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@StoreListActivity)!!, false)

        val data = intent.getSerializableExtra(INTENT_DATA_STORE_LIST_DATA) as MainMenuItemData

        categoryUid = data.getCategoryUid()

        if (data.getCategoryUid() == COMPANY_TYPE_USED_MARKET) {
            initUsedMarket(data)
        } else {
            init(data)
        }
    }

    private fun init(data: MainMenuItemData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = StoreListFragment::class.java.name
        val storeListFragment = StoreListFragment.newInstance(data)
        fragmentTransaction.add(R.id.activity_store_list_container, storeListFragment, tag).commit()
    }

    private fun initUsedMarket(data: MainMenuItemData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = UsedMarketListFragment::class.java.name
        val usedMarketListFragment = UsedMarketListFragment.newInstance(data)
        fragmentTransaction.add(R.id.activity_store_list_container, usedMarketListFragment, tag).commit()
    }

    private fun showStoreDetailScreen(companyUid: Int) {
        val intent = Intent(this@StoreListActivity, StoreDetailActivity::class.java)
        intent.putExtra(INTENT_DATA_STORE_UID_DATA, companyUid)
        startActivityForResult(intent, INTENT_DATA_STORE_LIST_REQUEST_CODE)
    }

    private fun showUsedMarketDetailScreen(itemUid: Int) {
        val intent = Intent(this@StoreListActivity, UsedMarketDetailActivity::class.java)
        intent.putExtra(INTENT_DATA_USED_MARKET_DATA, itemUid)
        startActivityForResult(intent, INTENT_USED_MARKET_REQUEST_CODE)
    }

    private fun showUsedMarketRegisterScreen() {
        val intent = Intent(this@StoreListActivity, UsedMarketRegisterActivity::class.java)
        startActivityForResult(intent, INTENT_USED_MARKET_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@StoreListActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@StoreListActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@StoreListActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onLocationScreen() {
        setResult(RESULT_OK)
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 100)
    }

    override fun onStoreDetailScreen(companyUid: Int) {
        showStoreDetailScreen(companyUid)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onBackPressed() {
        if (categoryUid == COMPANY_TYPE_USED_MARKET) {
            super.onBackPressed()
        } else {
            val storeListFragment: StoreListFragment? = supportFragmentManager.findFragmentById(R.id.activity_store_list_container) as StoreListFragment?
            if (!storeListFragment!!.spinnerCheck()) {
                super.onBackPressed()
            }
        }
    }

    override fun onFinish() {
        finish()
    }

    override fun onUsedMarketDetailScreen(itemUid: Int) {
        showUsedMarketDetailScreen(itemUid)
    }

    override fun onUsedMarketRegisterScreen() {
        showUsedMarketRegisterScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_USED_MARKET_REQUEST_CODE -> {
                    val usedMarketListFragment: UsedMarketListFragment? = supportFragmentManager.findFragmentById(R.id.activity_store_list_container) as UsedMarketListFragment?
                    usedMarketListFragment!!.getStoreCategoryData(true)
                }

                INTENT_DATA_STORE_LIST_REQUEST_CODE -> {
                    INTENT_DATA_STORE_UID_DATA
                    val companyUid = data?.getIntExtra(INTENT_DATA_STORE_UID_DATA, 0) as Int
                    if (companyUid > 0) {
                        val isStatus = data.getBooleanExtra(INTENT_DATA_IS_STORE_STATUS_DATA, false) as Boolean
                        val storeListFragment: StoreListFragment? = supportFragmentManager.findFragmentById(R.id.activity_store_list_container) as StoreListFragment?
                        storeListFragment!!.onItemUpdate(companyUid, isStatus)
                    }
                }
            }
        }
    }
}