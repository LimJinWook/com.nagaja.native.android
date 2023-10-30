package com.nagaja.app.android.Util

import androidx.multidex.MultiDexApplication

class ErrorRequest : MultiDexApplication() {

    companion object {

        /**
         * Token Validity
         */
        // Expired Tokens
        var ERROR_EXPIRED_TOKEN                         = 91

        // Invalid Token
        var ERROR_INVALID_TOKEN                         = 97

        // Tokens Do Not Exist
        var ERROR_DO_NOT_EXIST_TOKEN                    = 99


        /**
         * ID, Password, SignUp, Login
         * */
        // Login - Not Confirm Email
        var ERROR_CODE_NOT_CONFIRM_EMAIL                                     = 20

        // Login - Block Member
        var ERROR_CODE_BLOCK_MEMBER                                          = 23

        // Login - Human Account
        var ERROR_CODE_HUMAN_ACCOUNT                                         = 27

        // Login - Withdrawal Request
        var ERROR_CODE_WITHDRAWAL_REQUEST                                    = 28

        // Login - Withdrawal Complete
        var ERROR_CODE_WITHDRAWAL_COMPLETE                                   = 29

        // Not Found USER
        var ERROR_CODE_NOT_FOUND_USER                                        = 42

        // Not Found ID
        var ERROR_CODE_NOT_FOUND_ID                                          = 51

        // Not Found Password
        var ERROR_CODE_NOT_FOUND_PASSWORD                                    = 52

        // Not Found Machine Information
        var ERROR_CODE_NOT_FOUND_MACHINE_INFORMATION                         = 53

        // Existing Data
        var ERROR_CODE_EXISTING_DATA                                         = 60

        // Existing Password
        var ERROR_CODE_EXISTING_PASSWORD                                     = 61

        // Existing ID
        var ERROR_CODE_EXISTING_ID                                           = 62

        // Existing Nick Name
        var ERROR_CODE_EXISTING_NICK_NAME                                    = 63

        // Existing Email
        var ERROR_CODE_EXISTING_EMAIL                                        = 64

        // Existing Manager
        var ERROR_CODE_EXISTING_MANAGER                                      = 65

        // Existing Phone Number
        var ERROR_CODE_EXISTING_PHONE_NUMBER                                 = 66

        // Old Password
        var ERROR_CODE_OLD_PASSWORD                                          = 67

        // Do Not Match ID
        var ERROR_CODE_DO_NOT_MATCH_ID                                       = 68

        // Do Not Match Password
        var ERROR_CODE_DO_NOT_MATCH_PASSWORD                                 = 69

        // Required Agree Check
        var ERROR_CODE_REQUIRED_AGREE_CHECK                                  = 70

        // Duplicated Login
        var ERROR_CODE_DUPLICATED_LOGIN                                      = 71

        // No Member
        var ERROR_CODE_NO_MEMBER                                             = 72

        // Different Type Member
        var ERROR_CODE_DIFFERENT_TYPE_SIGN_UP                                = 73

        // Wrong Format Password
        var ERROR_CODE_WRONG_FORMAT_PASSWORD                                 = 82



        /**
         * Phone Number Auth Code
         * */
        // Wrong Code
        var ERROR_CODE_WRONG_CODE                                            = 101

        // Expired Code
        var ERROR_CODE_EXPIRED_CODE                                          = 102

        // Not Confirm Email
        var ERROR_CODE_NOT_CONFIRM_EMAIL_SNS                                 = 103

        /**
         * No Recommend
         * */
        // No Recommend
        var ERROR_CODE_NO_RECOMMEND                                          = 229

        /**
         * Not enough points
         * */
        // Not enough points
        var ERROR_CODE_NOT_ENOUGH_POINT                                      = 300

        /**
         * Reservation
         * */
        var ERROR_CODE_RESERVATION_DATE_SELECT_ERROR                         = 18



        /**
         * Company Member Register
         * */
        var ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_WRONG_GRANT             = 32
        var ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_MANAGER_LIMIT           = 33
    }
}