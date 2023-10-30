package com.nagaja.app.android.PhoneAuth

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_FIND_ID
import com.nagaja.app.android.Data.PhoneAuthData
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class PhoneAuthActivity : NagajaActivity(), PhoneAuthFragment.OnPhoneAuthFragmentListener {

    private var mPhoneAuthType = 0x01

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@PhoneAuthActivity)!!, false)

        mPhoneAuthType = intent.getIntExtra(INTENT_DATA_PHONE_AUTH_TYPE_DATA, -1)

        init(mPhoneAuthType)
    }

    private fun init(phoneAuthType: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = PhoneAuthFragment::class.java.name
        val phoneAuthFragment = PhoneAuthFragment.newInstance(phoneAuthType)
        fragmentTransaction.add(R.id.activity_phone_auth_container, phoneAuthFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }

    override fun onConfirm(value: String) {
        if (mPhoneAuthType == PHONE_AUTH_TYPE_FIND_ID) {
            intent.putExtra(INTENT_DATA_PHONE_AUTH_FIND_ID, value)
        } else if (mPhoneAuthType == PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER) {
            intent.putExtra(INTENT_DATA_PHONE_NUMBER_CHANGE_DATA, value)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onSignUpConfirm(data: PhoneAuthData) {
        intent.putExtra(INTENT_DATA_PHONE_AUTH_DATA, data)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onSuccessFindID(findID: String) {
        intent.putExtra(INTENT_DATA_PHONE_AUTH_FIND_ID, findID)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onSuccessFindPassword(data: PhoneAuthData) {
        intent.putExtra(INTENT_DATA_PHONE_AUTH_FIND_PASSWORD, data)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onSuccessWithdrawal() {
        finish()
    }

}