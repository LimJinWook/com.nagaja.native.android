package com.nagaja.app.android.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Address.AddressActivity
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Data.PhoneAuthData
import com.nagaja.app.android.Data.SnsUserData
import com.nagaja.app.android.PhoneAuth.PhoneAuthActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.TermsWebView.TermsWebViewActivity
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class SignUpActivity : NagajaActivity(), SignUpFragment.OnSignUpFragmentListener,
    SuccessSignUpFragment.OnSuccessSignUpFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val snsUserData = intent.getSerializableExtra(INTENT_DATA_IS_SNS_LOGIN_DATA) as SnsUserData

        init(snsUserData)
    }

    private fun init(snsUserData: SnsUserData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = SignUpFragment::class.java.name
        val signUpFragment = SignUpFragment.newInstance(snsUserData)
        fragmentTransaction.add(R.id.activity_signup_container, signUpFragment, tag).commit()
    }

    private fun showPhoneAuthScreen(phoneAuthType: Int) {
        val intent = Intent(this, PhoneAuthActivity::class.java)
        when (phoneAuthType) {
            NagajaFragment.PHONE_AUTH_TYPE_SIGN_UP -> {
                intent.putExtra(INTENT_DATA_PHONE_AUTH_TYPE_DATA, NagajaFragment.PHONE_AUTH_TYPE_SIGN_UP)
                startActivityForResult(intent, INTENT_PHONE_AUTH_SIGN_UP_REQUEST_CODE)
            }
            NagajaFragment.PHONE_AUTH_TYPE_FIND_ID -> {
                intent.putExtra(INTENT_DATA_PHONE_AUTH_TYPE_DATA, NagajaFragment.PHONE_AUTH_TYPE_FIND_ID)
                startActivityForResult(intent, INTENT_PHONE_AUTH_FIND_ID_REQUEST_CODE)
            }
            NagajaFragment.PHONE_AUTH_TYPE_FIND_PASSWORD -> {
                startActivityForResult(intent, INTENT_PHONE_AUTH_FIND_PASSWORD_REQUEST_CODE)
            }
            NagajaFragment.PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER -> {
                startActivityForResult(intent, INTENT_PHONE_AUTH_CHANGE_PHONE_NUMBER_REQUEST_CODE)
            }
        }
    }

    private fun showSuccessSignUp() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = SuccessSignUpFragment::class.java.name
        val successSignUpFragment = SuccessSignUpFragment.newInstance()
        fragmentTransaction.replace(R.id.activity_signup_container, successSignUpFragment, tag).addToBackStack(tag).commit()
    }

    private fun showAddressScreen(isKorea: Boolean) {
        val intent = Intent(this, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, isKorea)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showTermsWebViewScreen(selectType: Int, url: String) {
        val intent = Intent(this, TermsWebViewActivity::class.java)
        intent.putExtra(INTENT_DATA_SELECT_TERMS_TYPE_DATA, selectType)
        intent.putExtra(INTENT_DATA_SELECT_TERMS_URL_DATA, url)
        startActivity(intent)
    }

    override fun onFinish() {
        finish()
    }

    override fun onPhoneAuthScreen(phoneAuthType: Int) {
        showPhoneAuthScreen(phoneAuthType)
    }

    override fun onSuccessSignUp() {
        showSuccessSignUp()
    }

    override fun onAddressScreen(isKorea: Boolean) {
        showAddressScreen(isKorea)
    }

    override fun onTermsWebViewScreen(selectType: Int, url: String) {
        showTermsWebViewScreen(selectType, url)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_PHONE_AUTH_SIGN_UP_REQUEST_CODE -> {
                    val phoneAuthData = data?.getSerializableExtra(INTENT_DATA_PHONE_AUTH_DATA) as PhoneAuthData
                    val signUpFragment: SignUpFragment? = supportFragmentManager.findFragmentById(R.id.activity_signup_container) as SignUpFragment?
                    signUpFragment!!.phoneAuthSuccess(phoneAuthData)
                }

                INTENT_ADDRESS_REQUEST_CODE -> {
                    val addressData = data?.getSerializableExtra(INTENT_DATA_ADDRESS_DATA) as AddressData
                    val signUpFragment: SignUpFragment? = supportFragmentManager.findFragmentById(R.id.activity_signup_container) as SignUpFragment?
                    signUpFragment!!.getAddress(addressData)
                }
            }
        }
    }
}