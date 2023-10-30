package com.nagaja.app.android.Address

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class AddressActivity : NagajaActivity(), AddressFragment.OnAddressFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@AddressActivity)!!, false)

        val isKoreaSelect = intent.getBooleanExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, false)
        val isApplicationCompany = intent.getBooleanExtra(INTENT_DATA_IS_APPLICATION_COMPANY_SELECT_DATA, false)

        init(isKoreaSelect, isApplicationCompany)
    }

    private fun init(isKoreaSelect: Boolean, isApplicationCompany: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = AddressFragment::class.java.name
        val addressFragment = AddressFragment.newInstance(isKoreaSelect, isApplicationCompany)
        fragmentTransaction.add(R.id.activity_address_container, addressFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }

    override fun onSearchAddress(addressData: AddressData) {
        intent.putExtra(INTENT_DATA_ADDRESS_DATA, addressData)
        setResult(RESULT_OK, intent)
        finish()
    }
}