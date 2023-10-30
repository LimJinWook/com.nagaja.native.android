package com.nagaja.app.android.Base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.android.gms.security.ProviderInstaller
import com.kakao.sdk.common.KakaoSdk
import com.nagaja.app.android.R

import org.conscrypt.Conscrypt
import java.security.Security


class MyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = applicationContext()

        Security.insertProviderAt(Conscrypt.newProvider(), 1)

        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        updateAndroidSecurityProvider()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: Exception) {
            e.message
        }
    }
}
