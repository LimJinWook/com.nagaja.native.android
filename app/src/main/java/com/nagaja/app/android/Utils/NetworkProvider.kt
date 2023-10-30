package com.nagaja.app.android.Utils

import com.nagaja.app.android.Search.SearchFragment

class NetworkProvider {

    companion object {

        // Auth Token
        const val AUTH_TOKEN                                    = "Bearer ZGlubm9wbHVzQ29tcGFueTpna3NmYnZtZmh3cHJ4bUBLb3JEYW5jZUFwcA=="

        // URL Domain
        var DOMAIN                                              = "https://www.nagaja.com/"

        // REAL API Domain
        var API_DOMAIN                                          = "https://rapi.nagaja.com/"
        // Dev API Domain
//        var API_DOMAIN                                          = "https://dev-rapi.nagaja.com/"


        // Default REAL Image Domain
        const val DEFAULT_IMAGE_DOMAIN                          = "https://s3.ap-northeast-2.amazonaws.com/s3.nagaja.com/nagaja"
        // Default DEV Image Domain
//        const val DEFAULT_IMAGE_DOMAIN                          = "https://s3.ap-northeast-2.amazonaws.com/dev-s3.nagaja.com/nagaja"


        const val IMAGE_THUMB                                   = "/thumb"
        const val IMAGE_VIEW                                    = "/view"



        // Chat Domain
        const val CHAT_DOMAIN                                   = "https://chat.nagaja.com:3500/"
//        const val CHAT_DOMAIN                                   = "https://dev-chat.nagaja.com:3500/"

        // Chat Room List
        const val CHAT_LIST_URL                                 = CHAT_DOMAIN + "chatrooms"

        // Enter Chat Room
        const val CHAT_ROOM_URL                                 = CHAT_DOMAIN + "chats?room="

        // Request Chat Room
        const val CHAT_ROOM_REQUEST                             = CHAT_DOMAIN + "chats?class="



        // Version Check
        const val VERSION_CHECK                                 = "version/check"

        // Device Information
        const val DEVICE_INFORMATION                            = "machine"

        // Upload Image Test
        const val UPLOAD_IMAGE_TEST                             = "image/board"

        // Upload File Test
        const val UPLOAD_FILE_TEST                              = "file/board"

        // Login
        const val LOGIN                                         = "app/signin"

        // Exist Email Check
        const val EXIST_EMAIL_CHECK                             = "app/exist-email"

        // Phone Auth Code
        const val PHONE_AUTH_CODE                               = "phone/confirm/send/code"

        // Phone Auth Check Code
        const val PHONE_AUTH_CHECK_CODE                         = "phone/confirm/check/code"

        // Location Name
        const val LOCATION_NAME                                 = "location/name"






        /**
         * 이하 GET
         */

        // Application Company Member Type
        const val APPLICATION_COMPANY_MEMBER_TYPE               = "basemenu/cago/menu"

        // Company Member Data
        const val COMPANY_MEMBER                                = "mycompany/manager/member"

        // Company Default Data
        const val COMPANY_DEFAULT                                = "mycompany/detail"

        // Company Manager Data
        const val COMPANY_MANAGER                                = "mycompany/manager"

        // Company Change Service Status
        const val COMPANY_CHANGE_SERVICE_STATUS                  = "mycompany/sale/change-status"

        // Company Product Item
        const val COMPANY_PRODUCT_ITEM                           = "mycompany/item"

        // Company Notification Count
        const val COMPANY_NOTIFICATION_COUNT                     = "mycompany/count"

        // Company Default Information
        const val COMPANY_INFORMATION                            = "mycompany/info"

        // Company Default Information
        const val COMPANY_INFORMATION_DEFAULT_UPDATE             = "mycompany/update"

        // Company Default Information
        const val COMPANY_SALES_DEFAULT_INFORMATION              = "mycompany/sale/detail"

        // Company Change Add Features
        const val COMPANY_SALES_CHANGE_ADD_FEATURES              = "mycompany/sale/change-add"

        // Company And Member Search
        const val COMPANY_AND_MEMBER_SEARCH                      = "note/search"

        // Secure Key Login
        const val SECURE_KEY_LOGIN                               = "app/signin/key"

        // JWT Token Refresh
        const val JWT_TOKEN_REFRESH                              = "member/auth/refresh"

        // JWT Token By Email
        const val JWT_TOKEN_BY_EMAIL                             = "app/social/email"

        // Company Day Off Change
        const val COMPANY_DAY_OFF_CHANGE                         = "mycompany/sale/change-off"

        // Find Member
        const val FIND_MEMBER                                    = "note/search"

        // Company Member Register
        const val COMPANY_MEMBER_REGISTER                        = "mycompany/manager/save"

        // Company Member Remove
        const val COMPANY_MEMBER_REMOVE                          = "mycompany/manager/remove"

        // Company Business Time
        const val COMPANY_BUSINESS_TIME                          = "mycompany/sale/change-servicetime"

        // Company Break Time
        const val COMPANY_BREAK_TIME                             = "mycompany/sale/change-breaktime"

        // Company Reservation Person Limit
        const val COMPANY_RESERVATION_PERSON_LIMIT               = "mycompany/sale/change-limit"

        // Company Use Currency
        const val COMPANY_USE_CURRENCY                           = "mycompany/currency/change-baseyn"

        // Company Use Currency
        const val COMPANY_PRODUCT_UPDATE                         = "mycompany/item/update"

        // Main Menu Item
        const val MAIN_MENU_ITEM                                 = "app/basemenu/menu"

        // Recommend Place
        const val RECOMMEND_PLACE                                = "suggest?nuid="

        // Banner Event
        const val BANNER_EVENT                                   = "banner"

        // Service Information
        const val SERVICE_INFORMATION                            = "service/info"

        // Location Nation
        const val LOCATION_NATION                                = "base/location"

        // Location Area
        const val LOCATION_AREA                                  = "base/location/cago"

        // Default Setting Area
        const val DEFAULT_SETTING_AREA                           = "location/virtual/find-location"

        // Store Category Data
        const val STORE_CATEGORY_DATA                            = "basemenu/cago/menu"

        // Store List Data
        const val STORE_LIST_DATA                                = "app/company"

        // Save User Location
        const val SAVE_USER_LOCATION                             = "location/virtual/save"

        // Store Recommend Save
        const val STORE_RECOMMEND_SAVE                           = "mypage/company/recomm/save"

        // Store Regular Save
        const val STORE_REGULAR_SAVE                             = "mypage/regular/save"

        // Store Detail
        const val STORE_DETAIL                                   = "company/detail"

        // Store Menu
        const val STORE_MENU                                     = "item/company/"

        // Store Review
        const val STORE_REVIEW                                   = "company/review/"

        // Store Review Upload
        const val STORE_REVIEW_UPLOAD_SAVE                       = "app/mypage/company/review/save"

        // Store Review Delete
        const val STORE_REVIEW_DELETE                            = "mypage/company/review/remove"

        // Store Review MODIFY
        const val STORE_REVIEW_MODIFY                            = "app/mypage/company/review/update"

        // Report Upload
        const val REPORT_UPLOAD                                  = "app/mypage/report/save"

        // Company Report Upload
        const val COMPANY_REPORT_UPLOAD                          = "app/mycompany/report/save"

        // Review Recommend Save
        const val REVIEW_RECOMMEND_SAVE                          = "mypage/company/review/recomm/save"

        // Used Market List
        const val USED_MARKET_LIST                               = "item"

        // Company Used Market List
        const val COMPANY_USED_MARKET_LIST                       = "mycompany/secondhand"

        // Used Market Detail
        const val USED_MARKET_DETAIL                             = "item/detail"

        // Used Market Recommend Save
        const val USED_MARKET_RECOMMEND_SAVE                     = "mypage/secondhand/recomm/save"

        // Currency Data
        const val CURRENCY_DATA                                  = "base/currency"

        // Used Market Register
        const val USED_MARKET_REGISTER                           = "app/mypage/secondhand/save"

        // Used Market Register
        const val COMPANY_USED_MARKET_REGISTER                   = "app/mycompany/secondhand/save"

        // Used Market Register Modify
        const val USED_MARKET_REGISTER_MODIFY                    = "app/mypage/secondhand/update"

        // Company Used Market Register Modify
        const val COMPANY_USED_MARKET_REGISTER_MODIFY            = "app/mycompany/secondhand/update"

        // Used Market Delete
        const val USED_MARKET_DELETE                             = "app/mypage/secondhand/remove"

        // Company Used Market Delete
        const val COMPANY_USED_MARKET_DELETE                     = "app/mycompany/secondhand/remove"

        // Notice
        const val NOTICE_LIST                                    = "app/notice"

        // Notice Detail
        const val NOTICE_DETAIL                                  = "notice/detail"

        // Notice Comment
        const val NOTICE_COMMENT                                 = "notice/comment"

        // Notice Comment Write
        const val NOTICE_COMMENT_WRITE                           = "mypage/notice/comment/save"

        // Notice Comment Delete
        const val NOTICE_COMMENT_DELETE                          = "mypage/notice/comment/remove"

        // Board List
        const val BOARD_LIST                                     = "app/board"

        // Board Detail
        const val BOARD_DETAIL                                   = "app/board/detail"

        // Board Comment List
        const val BOARD_COMMENT_LIST                             = "board/comment"

        // Board Comment Delete
        const val BOARD_COMMENT_DELETE                           = "mypage/board/comment/remove"

        // Board Comment Write
        const val BOARD_COMMENT_WRITE                            = "mypage/board/comment/save"

        // Board Recommend Save
        const val BOARD_RECOMMEND_SAVE                           = "mypage/board/recomm/save"

        // Board BOOKMARK SAVE
        const val BOARD_BOOKMARK_SAVE                            = "mypage/board/bookmark/save"

        // Board REGISTER SAVE
        const val BOARD_REGISTER_SAVE                            = "app/mypage/board/save"

        // Company Job REGISTER SAVE
        const val COMPANY_JOB_REGISTER_SAVE                      = "app/mycompany/job/save"

        // Board REGISTER MODIFY
        const val BOARD_REGISTER_MODIFY                          = "app/mypage/board/update"

        // Board Delete
        const val BOARD_DELETE                                   = "app/mypage/board/remove"

        // Free Board Horizontal List
        const val BOARD_HORIZONTAL_LIST                          = "app/board"

        // Reservation Schedule Day
        const val RESERVATION_SCHEDULE_DAY                       = "schedule/day"

        // Reservation Schedule Time
        const val RESERVATION_SCHEDULE_TIME                      = "schedule/time"

        // Reservation Save
        const val RESERVATION_SAVE                               = "mypage/reservate/save"

        // Reservation List
        const val RESERVATION_LIST                               = "mypage/reservate"

        // Company Reservation List
        const val COMPANY_RESERVATION_LIST                       = "mycompany/reservate"

        // Reservation Change Status
        const val RESERVATION_CHANGE_STATUS                      = "mypage/reservate/change-status"

        // Event Data
        const val EVENT_DATA                                     = "app/popup"

        // Phone Number Change
        const val PHONE_NUMBER_CHANGE                            = "app/member/change-phone"

        // Modify Profile Update
        const val MODIFY_PROFILE_UPDATE                          = "app/member/change-profile"

        // Delete Profile Image
        const val DELETE_PROFILE_IMAGE                           = "app/member/delete-profile"

        // Note List
        const val NOTE_LIST                                      = "mypage/note"

        // Company Note List
        const val COMPANY_NOTE_LIST                              = "mycompany/note"

        // Note Detail
        const val NOTE_DETAIL                                    = "mypage/note/detail"

        // Company Note Detail
        const val COMPANY_NOTE_DETAIL                            = "mycompany/note/detail"

        // Note Detail Sender
        const val NOTE_DETAIL_SENDER                             = "mypage/note/sender"

        // Company Note Detail Sender
        const val COMPANY_NOTE_DETAIL_SENDER                     = "mycompany/note/sender"

        // Note Detail Sender
        const val NOTE_COMPANY_STATUS                            = "mypage/note/bar"

        // Company Note Detail Sender
        const val COMPANY_NOTE_COMPANY_STATUS                    = "mycompany/note/bar"

        // Note Delete
        const val NOTE_DELETE                                    = "app/mypage/note/remove"

        // Company Note Delete
        const val COMPANY_NOTE_DELETE                            = "app/mycompany/note/remove"

        // Note Keep
        const val NOTE_KEEP                                      = "app/mypage/note/change-keepyn"

        // Company Note Keep
        const val COMPANY_NOTE_KEEP                              = "app/mycompany/note/change-keepyn"

        // Member Information
        const val MEMBER_INFORMATION                             = "member/info"

        // Company Information
        const val COMPANY_NOTE_INFORMATION                       = "company/info"

        // Note Send Save
        const val NOTE_SEND_SAVE                                 = "app/mypage/note/save"

        // Company Note Send Save
        const val COMPANY_NOTE_SEND_SAVE                         = "app/mycompany/note/save"

        // Regular List
        const val REGULAR_LIST                                   = "mypage/regular"

        // Company Regular List
        const val COMPANY_REGULAR_LIST                           = "mycompany/regular"

        // My Used Market List
        const val MY_USED_MARKET_LIST                            = "mypage/secondhand"

        // Used Market Status Change
        const val USED_MARKET_STATUS_CHANGE                      = "app/mypage/secondhand/change-status"

        // Company Used Market Status Change
        const val COMPANY_USED_MARKET_STATUS_CHANGE              = "app/mycompany/secondhand/change-status"

        // My Job List
        const val MY_JOB_LIST                                    = "mypage/job"

        // Company Job List
        const val COMPANY_JOB_LIST                               = "mycompany/job"

        // My Missing List
        const val MY_MISSING_LIST                                = "mypage/declare"

        // Application Company
        const val APPLICATION_COMPANY                            = "app/mypage/company/save"

        // Modify Company Default Information
        const val MODIFY_COMPANY_DEFAULT_INFORMATION             = "app/mycompany/update"

        // Company Reservation Detail
        const val COMPANY_RESERVATION_DETAIL                     = "mycompany/reservate/detail"

        // Company Reservation Memo Save
        const val COMPANY_RESERVATION_MEMO_SAVE                  = "app/mycompany/reservate/comment/save"

        // Company Reservation Memo List
        const val COMPANY_RESERVATION_MEMO_LIST                  = "mycompany/reservate/comment"

        // Company Job Modify
        const val COMPANY_JOB_MODIFY                             = "app/mycompany/job/update"

        // Company Product Currency
        const val COMPANY_PRODUCT_CURRENCY                       = "mycompany/currency/base"

        // Company Product SAVE
        const val COMPANY_PRODUCT_SAVE                           = "app/mycompany/item/save"

        // Company Product SAVE
        const val COMPANY_PRODUCT_MODIFY                         = "app/mycompany/item/update"

        // Company Product Category
        const val COMPANY_PRODUCT_CATEGORY                       = "mycompany/item/cago"

        // Company Product Remove
        const val COMPANY_PRODUCT_REMOVE                         = "app/mycompany/item/remove"

        // Notification List
        const val NOTIFICATION_LIST                              = "mypage/push"

        // Notification Confirm
        const val NOTIFICATION_CONFIRM                           = "mypage/push/detail"

        // Search
        const val SEARCH                                         = "app/search"

        // Logout
        const val LOGOUT                                         = "member/auth/logout"

        // SNS Email Check
        const val SNS_EMAIL_CHECK                                = "app/member/social/signin"

        // Social Member Sign Up
        const val SOCIAL_MEMBER_SIGN_UP                          = "app/member/social/signup"

        // Member Sign Up
        const val MEMBER_SIGN_UP                                 = "app/signup"

        // Sign Up Send Email
        const val SIGN_UP_SEND_EMAIL                             = "app/member/confirm/send/code"

        // Member Phone Auth Code
        const val MEMBER_PHONE_AUTH_CODE                         = "member/confirm/send/code"

        // Member Phone Auth Check Code
        const val MEMBER_PHONE_AUTH_CHECK_CODE                   = "member/confirm/check/code"

        // Member Find Email Disconnect
        const val MEMBER_FIND_EMAIL_DISCONNECT                   = "member/forgat/email"

        // Member Find Password Email Check
        const val MEMBER_FIND_PASSWORD_EMAIL_CHECK               = "app/member/forgat/password"

        // Member Change Password
        const val MEMBER_CHANGE_PASSWORD                         = "app/member/forgat/password-change"

        // Service Area Location
        const val SERVICE_AREA_LOCATION                          = "base/location/cago/service"

        // Exchange Rate
        const val EXCHANGE_RATE                                  = "currency"

        // Change Address
        const val CHANGE_ADDRESS                                 = "app/member/info/change-address"

        // Sign Up Email Check
        const val SIGN_UP_EMAIL_CHECK                            = "app/member/check-email"

        // Sign Up Nick Name Check
        const val SIGN_UP_NICK_NAME_CHECK                        = "app/member/check-nick"

        // Login Success Device Information
        const val LOGIN_SUCCESS_DEVICE_INFORMATION               = "app/member/machine/update"

        // Company Reservation Status Change
        const val COMPANY_RESERVATION_STATUS_CHANGE              = "app/mycompany/reservate/change-status"

        // Language Change
        const val LANGUAGE_CHANGE                                = "app/member/change-nation"

        // Note Member Receiver
        const val NOTE_MEMBER_RECEIVER                           = "mypage/note/receiver"

        // Note Member Sender
        const val NOTE_MEMBER_SENDER                             = "mypage/note/sender"

        // Note Company Receiver
        const val NOTE_COMPANY_RECEIVER                          = "mycompany/note/receiver"

        // Note Company Sender
        const val NOTE_COMPANY_SENDER                            = "mycompany/note/sender"

        // Member Profile
        const val MEMBER_PROFILE                                 = "member/auth/profile"

        // Change Push Status
        const val CHANGE_PUSH_STATUS                             = "app/member/info/change-pushYn"

        // My Location
        const val MY_LOCATION                                    = "member/location/detail"

        // My Location Setting
        const val MY_LOCATION_SETTING                            = "app/member/location/change-location"

        // Used Market Member Pull Up Count
        const val USED_MARKET_MEMBER_PULL_UP_COUNT               = "mypage/secondhand/upcount"

        // Used Market Company Pull Up Count
        const val USED_MARKET_COMPANY_PULL_UP_COUNT              = "mycompany/secondhand/upcount"

        // Used Market Member Pull Up
        const val USED_MARKET_MEMBER_PULL_UP                     = "mypage/secondhand/upper"

        // Used Market Company Pull Up
        const val USED_MARKET_COMPANY_PULL_UP                    = "mycompany/secondhand/upper"

        // Member Point
        const val MEMBER_POINT                                   = "mypage/point/detail"

        // Company Point
        const val COMPANY_POINT                                  = "mycompany/point/detail"

        // Member Point History
        const val MEMBER_POINT_HISTORY                           = "mypage/point/log"

        // Company Point History
        const val COMPANY_POINT_HISTORY                          = "mycompany/point/log"

        // Member Withdrawal
        const val MEMBER_WITHDRAWAL                              = "app/member/withdraw/request"

    }
}