package com.nagaja.app.android.Permission

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Login.LoginActivity
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class PermissionActivity : NagajaActivity(), PermissionFragment.OnPermissionFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = PermissionFragment::class.java.name
        val permissionFragment = PermissionFragment.newInstance()
        fragmentTransaction.add(R.id.activity_permission_container, permissionFragment, tag).commit()
    }

    private fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun showMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()

//        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@PermissionActivity)!!, true)
    }

    override fun onFinish() {
        finish()
    }

    override fun onLoginScreen() {
        showLoginScreen()
    }

    override fun onMainScreen() {
        showMainScreen()
    }
}