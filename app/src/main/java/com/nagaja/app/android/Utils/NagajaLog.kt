package com.nagaja.app.android.Utils

import android.util.Log

class NagajaLog {

    private val mIsDebug = true

    // wooks. Log (String)
    fun d(msg: String) {
        if (mIsDebug) {
            Log.d("NaGaJa_Log", "wooks ====================	 : $msg")
        }
    }

    // wooks. Log (Integer)
    fun d(msg: Int) {
        if (mIsDebug) {
            Log.d("NaGaJa_Log", "wooks ====================	 : $msg")
        }
    }

    // wooks. Log (boolean)
    fun d(msg: Boolean) {
        if (mIsDebug) {
            Log.d("NaGaJa_Log", "wooks ====================	 : $msg")
        }
    }

    // wooks. Log Error
    fun e(msg: String) {
        if (mIsDebug) {
            Log.e("NaGaJa_Log", "wooks ====================	 : $msg")
        }
    }

    // wooks. Log I (String)
    fun i(api: String, msg: String) {
        if (mIsDebug) {
            Log.i("NaGaJa_Log", "wooks ====================	 : $api ===== $msg")
        }
    }

    // wooks. Log I (integer)
    fun i(api: String, msg: Int) {
        if (mIsDebug) {
            Log.i("NaGaJa_Log", "wooks ====================	 : $api ===== $msg")
        }
    }
}