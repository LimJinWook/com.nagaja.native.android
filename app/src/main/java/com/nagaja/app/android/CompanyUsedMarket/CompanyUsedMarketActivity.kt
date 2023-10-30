package com.nagaja.app.android.CompanyUsedMarket

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Data.CompanyDefaultData
import com.nagaja.app.android.UsedMarketDetail.UsedMarketDetailActivity
import com.nagaja.app.android.UsedMarketRegister.UsedMarketRegisterActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class CompanyUsedMarketActivity : NagajaActivity(), CompanyUsedMarketFragment.OnCompanyUsedMarketFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_used_market)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@CompanyUsedMarketActivity)!!, false)

        val data = intent.getSerializableExtra(INTENT_DATA_COMPANY_DEFAULT_DATA) as CompanyDefaultData

        init(data)
    }

    private fun init(data: CompanyDefaultData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = CompanyUsedMarketFragment::class.java.name
        val companyUsedMarketFragment: CompanyUsedMarketFragment = CompanyUsedMarketFragment.newInstance(data)
        fragmentTransaction.add(R.id.activity_company_used_market_container, companyUsedMarketFragment, tag).commit()
    }

    private fun showUsedMarketDetailScreen(itemUid: Int) {
        val intent = Intent(this@CompanyUsedMarketActivity, UsedMarketDetailActivity::class.java)
        intent.putExtra(INTENT_DATA_USED_MARKET_DATA, itemUid)
        startActivityForResult(intent, INTENT_USED_MARKET_REQUEST_CODE)
    }

    private fun showUsedMarketRegisterScreen(companyUid: Int) {
        val intent = Intent(this@CompanyUsedMarketActivity, UsedMarketRegisterActivity::class.java)
        intent.putExtra(INTENT_DATA_COMPANY_UID_DATA, companyUid)
        startActivityForResult(intent, INTENT_USED_MARKET_REQUEST_CODE)
    }

    override fun onFinish() {
        finish()
    }

    override fun onUsedMarketDetailScreen(itemUid: Int) {
        showUsedMarketDetailScreen(itemUid)
    }

    override fun onUsedMarketRegisterScreen(companyUid: Int) {
        showUsedMarketRegisterScreen(companyUid)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_USED_MARKET_REQUEST_CODE -> {
                    val companyUsedMarketFragment: CompanyUsedMarketFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_used_market_container) as CompanyUsedMarketFragment?
                    companyUsedMarketFragment!!.getCompanyUsedMarketList(true)
                }
            }
        }
    }
}