package com.nagaja.app.android.Utils

import androidx.multidex.MultiDexApplication
import com.nagaja.app.android.Data.*

class MAPP : MultiDexApplication() {

    companion object {

        // Dynamic Link Url
        var DYNAMIC_LINK_URL: String? = ""

        // SNS Login Profile Image Url
        var SNS_PROFILE_IMAGE_URL: String? = ""

        // User FCM Token
        var USER_FCM_TOKEN: String = ""

        // User Data
        var USER_DATA = UserData()

        // User Location
        var USER_LOCATION: String? = ""

        // Company Sales Data
        var COMPANY_SALES_DATA = CompanySalesData()

        // Company Sales Data
        var MEMBER_PROFILE_DATA = MemberProfileData()

        // Company Sales Data
        var COMPANY_MANAGER_LIST_DATA = ArrayList<CompanyManagerData>()

        // Used Market List Data
        var USED_MARKET_LIST_DATA = ArrayList<UsedMarketData>()

        // Event List Data
        var MAIN_BANNER_EVENT_LIST_DATA = ArrayList<BannerEventData>()
        var SUB_BANNER_EVENT_LIST_DATA = ArrayList<BannerEventData>()

        // Location Area List Data
        var LOCATION_AREA_LIST_DATA = ArrayList<LocationAreaMainData>()

        // Select Nation Name
        var SELECT_NATION_NAME: String = ""

        // Non-Member
        var IS_NON_MEMBER_SERVICE: Boolean = false

        // Select Language Code
        var SELECT_LANGUAGE_CODE: String = ""

        // Push Data
        var PUSH_DATA: PushData? = null

        // Is Start App
        var IS_START_APP: Boolean = true
    }
}