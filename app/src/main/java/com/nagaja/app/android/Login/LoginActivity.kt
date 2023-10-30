package com.nagaja.app.android.Login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_FIND_ID
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_FIND_PASSWORD
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_SIGN_UP
import com.nagaja.app.android.Data.PhoneAuthData
import com.nagaja.app.android.Data.SnsUserData
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.PhoneAuth.PhoneAuthActivity
import com.nagaja.app.android.SignUp.SignUpActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class LoginActivity : NagajaActivity(), LoginFragment.OnLoginFragmentListener,
    FindIDFragment.OnFindIDFragmentListener, FindPasswordFragment.OnFindPasswordFragmentListener {

    private var mIsNonMemberToMember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@LoginActivity)!!, false)

        mIsNonMemberToMember = intent.getBooleanExtra(INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_DATA, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = LoginFragment::class.java.name
        val loginFragment = LoginFragment.newInstance()
        fragmentTransaction.add(R.id.activity_login_container, loginFragment, tag).commit()
    }

    private fun showFindIDScreen() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = FindIDFragment::class.java.name
        val findIDFragment = FindIDFragment.newInstance()
        fragmentTransaction.replace(R.id.activity_login_container, findIDFragment, tag).addToBackStack(tag).commit()
    }

    private fun showFindPasswordScreen(isAfterFindID: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = FindPasswordFragment::class.java.name
        val findPasswordFragment = FindPasswordFragment.newInstance()
        if (isAfterFindID) {
            fragmentTransaction.replace(R.id.activity_login_container, findPasswordFragment, tag).commit()
        } else {
            fragmentTransaction.replace(R.id.activity_login_container, findPasswordFragment, tag).addToBackStack(tag).commit()
        }
    }

    private fun showPhoneNumberScreen(phoneAuthType: Int) {
        val intent = Intent(this, PhoneAuthActivity::class.java)
        when (phoneAuthType) {
            PHONE_AUTH_TYPE_SIGN_UP -> {
                startActivityForResult(intent, INTENT_PHONE_AUTH_SIGN_UP_REQUEST_CODE)
            }
            PHONE_AUTH_TYPE_FIND_ID -> {
                intent.putExtra(INTENT_DATA_PHONE_AUTH_TYPE_DATA, PHONE_AUTH_TYPE_FIND_ID)
                startActivityForResult(intent, INTENT_PHONE_AUTH_FIND_ID_REQUEST_CODE)
            }
            PHONE_AUTH_TYPE_FIND_PASSWORD -> {
                intent.putExtra(INTENT_DATA_PHONE_AUTH_TYPE_DATA, PHONE_AUTH_TYPE_FIND_PASSWORD)
                startActivityForResult(intent, INTENT_PHONE_AUTH_FIND_PASSWORD_REQUEST_CODE)
            }
            PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER -> {
                startActivityForResult(intent, INTENT_PHONE_AUTH_CHANGE_PHONE_NUMBER_REQUEST_CODE)
            }
        }

    }

    private fun showSignUpScreen(snsUserData: SnsUserData) {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_SNS_LOGIN_DATA, snsUserData)
        startActivity(intent)
    }

    private fun showMainScreen() {
        if (mIsNonMemberToMember) {
            setResult(RESULT_OK)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        finish()
    }

    override fun onFinish() {
        finish()
    }

    override fun onFindIDScreen() {
        showFindIDScreen()
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onPhoneAuthScreen(phoneAuthType: Int) {
        showPhoneNumberScreen(phoneAuthType)
    }

    override fun onFindPasswordScreen(isAfterFindID: Boolean) {
        showFindPasswordScreen(isAfterFindID)
    }

    override fun onSignUpScreen(snsUserData: SnsUserData) {
        showSignUpScreen(snsUserData)
    }

    override fun onMainScreen() {
        showMainScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        NagajaLog().d("wooks, Login Activity onActivityResult:$requestCode:$resultCode:$data")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_PHONE_AUTH_SIGN_UP_REQUEST_CODE -> {

                }

                INTENT_PHONE_AUTH_FIND_ID_REQUEST_CODE -> {
                    val findID = data!!.getStringExtra(INTENT_DATA_PHONE_AUTH_FIND_ID)

                    val findIDFragment: FindIDFragment? = supportFragmentManager.findFragmentById(R.id.activity_login_container) as FindIDFragment?
                    findIDFragment!!.phoneAuthSuccess(findID!!)
                }

                INTENT_PHONE_AUTH_FIND_PASSWORD_REQUEST_CODE -> {
                    val phoneAuthData = data!!.getSerializableExtra(INTENT_DATA_PHONE_AUTH_FIND_PASSWORD) as PhoneAuthData

                    val findPasswordFragment: FindPasswordFragment? = supportFragmentManager.findFragmentById(R.id.activity_login_container) as FindPasswordFragment?
                    findPasswordFragment!!.phoneAuthSuccess(phoneAuthData)
                }

                INTENT_PHONE_AUTH_CHANGE_PHONE_NUMBER_REQUEST_CODE -> {

                }
            }
        }

        /*for (fragment in supportFragmentManager.fragments) {
            //System.out.println("@#@");
            fragment.onActivityResult(requestCode, resultCode, data)
        }*/

    }
}