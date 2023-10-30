package com.nagaja.app.android.Utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil {

    companion object {

        const val PREFERENCE_NAME                                = "com.nagaja.app.android.pref"

        // Select Language
        const val PREF_KEY_SELECT_LANGUAGE                       = "pref_key_select_language"

        // Is New Install
        const val PREF_KEY_IS_NEW_INSTALL                        = "pref_key_is_new_install"

        // Save Location
        const val PREF_KEY_SAVE_LOCATION                         = "pref_key_save_location"

        // Save Location Name
        const val PREF_KEY_SAVE_LOCATION_NAME                    = "pref_key_save_location_name"

        // Secure Key
        const val PREF_KEY_SECURE_KEY                            = "pref_key_secure_key"

        // Refresh Key
        const val PREF_KEY_REFRESH_KEY                           = "pref_key_refresh_key"

        // Exchange Rate Date
        const val PREF_KEY_EXCHANGE_RATE_DATE                    = "pref_key_exchange_rate_date"

        // Save Nation Code
        const val PREF_KEY_SAVE_NATION_CODE                      = "pref_key_save_nation_code"

        // Save Main Area Code
        const val PREF_KEY_SAVE_MAIN_AREA_CODE                   = "pref_key_save_main_area_code"

        // Save Sub Area Code
        const val PREF_KEY_SAVE_SUB_AREA_CODE                    = "pref_key_save_sub_area_code"

        // Do not watch it today
        const val PREF_DO_NOT_WATCH_TODAY                        = "pref_do_not_watch_today"

        // Save Login Type
        const val PREF_KEY_SAVE_LOGIN_TYPE                       = "pref_key_save_login_type"

        // Save Nation Phone Code
        const val PREF_KEY_SAVE_NATION_PHONE_CODE                = "pref_key_save_nation_phone_code"

        // Save Notification
        const val PREF_KEY_SAVE_NOTIFICATION                     = "pref_key_save_notification"
    }

    private fun getSharedPreferences(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(
            PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }

    private fun getSharedPreferencesEditor(context: Context): SharedPreferences.Editor? {
        val preferences = getSharedPreferences(context) ?: return null
        return preferences.edit()
    }

    /**
     * Select Language
     */
    fun setSelectLanguage(context: Context, selectLanguage: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_SELECT_LANGUAGE, selectLanguage)
        editor.commit()
    }

    fun getSelectLanguage(context: Context): String? {
        var defaultValue: String? = null
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_SELECT_LANGUAGE, defaultValue)
        return defaultValue
    }

    /**
     * Is New Install
     */
    fun setIsNewInstall(context: Context, isNewInstall: Boolean) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putBoolean(PREF_KEY_IS_NEW_INSTALL, isNewInstall)
        editor.commit()
    }

    fun getIsNewInstall(context: Context): Boolean {
        var defaultValue = true
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getBoolean(PREF_KEY_IS_NEW_INSTALL, defaultValue)
        return defaultValue
    }

    /**
     * Save Location
     */
    fun setSaveLocation(context: Context, saveLocation: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_SAVE_LOCATION, saveLocation)
        editor.commit()
    }

    fun getSaveLocation(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_SAVE_LOCATION, defaultValue)
        return defaultValue
    }

    /**
     * Save Location Name
     */
    fun setLocationName(context: Context, locationName: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_SAVE_LOCATION_NAME, locationName)
        editor.commit()
    }

    fun getLocationName(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_SAVE_LOCATION_NAME, defaultValue)
        return defaultValue
    }

    /**
     * Secure Key
     */
    fun setSecureKey(context: Context, secureKey: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_SECURE_KEY, secureKey)
        editor.commit()
    }

    fun getSecureKey(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_SECURE_KEY, defaultValue)
        return defaultValue
    }

    /**
     * Refresh Key
     */
    fun setRefreshKey(context: Context, refreshKey: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_REFRESH_KEY, refreshKey)
        editor.commit()
    }

    fun getRefreshKey(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_REFRESH_KEY, defaultValue)
        return defaultValue
    }

    /**
     * Exchange Rate Date
     */
    fun setExchangeRateDate(context: Context, exchangeRateDate: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_EXCHANGE_RATE_DATE, exchangeRateDate)
        editor.commit()
    }

    fun getExchangeRateDate(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_EXCHANGE_RATE_DATE, defaultValue)
        return defaultValue
    }

    /**
     * Save Nation Code
     */
    fun setSaveNationCode(context: Context, saveNationCode: Int) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putInt(PREF_KEY_SAVE_NATION_CODE, saveNationCode)
        editor.commit()
    }

    fun getSaveNationCode(context: Context): Int {
        var defaultValue: Int = 0
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getInt(PREF_KEY_SAVE_NATION_CODE, defaultValue)
        return defaultValue
    }

    /**
     * Save Main Area Code
     */
    fun setSaveMainAreaCode(context: Context, saveMainAreaCode: Int) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putInt(PREF_KEY_SAVE_MAIN_AREA_CODE, saveMainAreaCode)
        editor.commit()
    }

    fun getSaveMainAreaCode(context: Context): Int {
        var defaultValue: Int = 0
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getInt(PREF_KEY_SAVE_MAIN_AREA_CODE, defaultValue)
        return defaultValue
    }

    /**
     * Save Sub Area Code
     */
    fun setSaveSubAreaCode(context: Context, saveSubAreaCode: Int) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putInt(PREF_KEY_SAVE_SUB_AREA_CODE, saveSubAreaCode)
        editor.commit()
    }

    fun getSaveSubAreaCode(context: Context): Int {
        var defaultValue: Int = 0
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getInt(PREF_KEY_SAVE_SUB_AREA_CODE, defaultValue)
        return defaultValue
    }

    /**
     * Event Popup
     */
    fun setDoNotWatchToday(context: Context, day: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_DO_NOT_WATCH_TODAY, day)
        editor.commit()
    }

    fun getDoNotWatchToday(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_DO_NOT_WATCH_TODAY, defaultValue)
        return defaultValue
    }

    /**
     * Save Login Type
     */
    fun setLoginType(context: Context, loginType: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_SAVE_LOGIN_TYPE, loginType)
        editor.commit()
    }

    fun getLoginType(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_SAVE_LOGIN_TYPE, defaultValue)
        return defaultValue
    }

    /**
     * Save Nation Phone Code
     */
    fun setNationPhoneCode(context: Context, nationPhoneCode: String?) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putString(PREF_KEY_SAVE_NATION_PHONE_CODE, nationPhoneCode)
        editor.commit()
    }

    fun getNationPhoneCode(context: Context): String? {
        var defaultValue: String? = ""
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getString(PREF_KEY_SAVE_NATION_PHONE_CODE, defaultValue)
        return defaultValue
    }

    /**
     * Save Notification
     */
    fun setIsNotification(context: Context, isNotification: Boolean) {
        val editor: SharedPreferences.Editor = getSharedPreferencesEditor(context)!!
        editor.putBoolean(PREF_KEY_SAVE_NOTIFICATION, isNotification)
        editor.commit()
    }

    fun getIsNotification(context: Context): Boolean {
        var defaultValue: Boolean = true
        val preferences: SharedPreferences = getSharedPreferences(context)!!
        defaultValue = preferences.getBoolean(PREF_KEY_SAVE_NOTIFICATION, defaultValue)
        return defaultValue
    }

    // 값(ALL Data) 삭제하기
    fun removeAllPreferences(context: Context?) {
        val pref: SharedPreferences = getSharedPreferences(context)!!
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }
}