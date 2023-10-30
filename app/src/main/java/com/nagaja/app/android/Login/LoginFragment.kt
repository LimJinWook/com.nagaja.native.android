package com.nagaja.app.android.Login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.*
import android.text.*
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.BuildConfig
import com.nagaja.app.android.Data.JwtTokenRefreshData
import com.nagaja.app.android.Data.SnsUserData
import com.nagaja.app.android.Data.UserData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.R
import com.nagaja.app.android.Splash.SplashFragment
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_BLOCK_MEMBER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_HUMAN_ACCOUNT
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_CONFIRM_EMAIL
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_CONFIRM_EMAIL_SNS
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_FOUND_PASSWORD
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_WITHDRAWAL_COMPLETE
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_WITHDRAWAL_REQUEST
import com.nagaja.app.android.Utils.*
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_select_language.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.security.MessageDigest
import java.util.*


class LoginFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mKakaoImageView: ImageView
    private lateinit var mNaverImageView: ImageView
    private lateinit var mGoogleImageView: ImageView
    private lateinit var mFacebookImageView: ImageView
    private lateinit var mPasswordShowAndHideImageView: ImageView

    private lateinit var mErrorMessageTextView: TextView

    private lateinit var mIdEditText: EditText
    private lateinit var mPasswordEditText: EditText

    private var mLoginType = ""

    private lateinit var mRequestQueue: RequestQueue

    // Naver Login
    private var mOAuthLoginModule: OAuthLogin? = null

    // Google Login
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    // Facebook Login
    private lateinit var mCallbackManager: CallbackManager
    lateinit var mFacebookLoginManager: LoginManager

    private var mIsPasswordShow = false

    private lateinit var mListener: OnLoginFragmentListener

    interface OnLoginFragmentListener {
        fun onFinish()
        fun onFindIDScreen()
        fun onFindPasswordScreen(isAfterFindID: Boolean)
        fun onSignUpScreen(snsUserData: SnsUserData)
        fun onMainScreen()
    }

    companion object {

        // KAKAO Login
        private const val KAKAO                     = 1001

        // Google Login
        private const val RC_SIGN_IN                = 9001
        private const val RC_GET_TOKEN              = 9002

        // Facebook Login
        const val FB_SIGN_IN                = 64206

        lateinit var mFacebookCallbackManager: CallbackManager

        fun newInstance(): LoginFragment {
            val args = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(mActivity)
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true)
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        }

        mCallbackManager = CallbackManager.Factory.create()
        mFacebookCallbackManager = CallbackManager.Factory.create()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_login, container, false)

        init()


        // Facebook Key Hash
        /*try {
            val info: PackageInfo = mActivity.packageManager.getPackageInfo(
                mActivity.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.getEncoder().encodeToString(md.digest()))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }*/

//        getKeyHash()
//        getKeyHash("80:25:07:BD:77:C0:CB:9A:45:8F:17:83:B2:64:A5:79:F4:DF:67:92")

//        val keyHash = Utility.getKeyHash(mActivity)
//        NagajaLog().e("wooks, KeyHash = $keyHash")
        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.visibility = View.GONE

        // Title Image View
        val titleImageView = mContainer.layout_title_title_image_view
        titleImageView.visibility = View.VISIBLE
        titleImageView.setImageResource(R.drawable.img_sing_up)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // ID Edit Text
        mIdEditText = mContainer.fragment_login_id_edit_text
        mIdEditText.filters = arrayOf(Util().blankSpaceFilter)
        mIdEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Password Edit Text
        mPasswordEditText = mContainer.fragment_login_password_edit_text
        mPasswordEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(20))
        mPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Password Show And Hide Image View
        mPasswordShowAndHideImageView = mContainer.fragment_login_password_show_and_hide_image_view
        mPasswordShowAndHideImageView.setOnClickListener {
            mIsPasswordShow = !mIsPasswordShow

            if (mIsPasswordShow) {
                mPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                mPasswordShowAndHideImageView.setImageResource(R.drawable.icon_password_show)
            } else {
                mPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                mPasswordShowAndHideImageView.setImageResource(R.drawable.icon_password_hide)
            }

            mPasswordEditText.setSelection(mPasswordEditText.length())
        }


        // Error Message Text View
        mErrorMessageTextView = mContainer.fragment_login_error_message_text_view

        // Confirm Text View
        val confirmTextView = mContainer.fragment_login_confirm_text_view
        confirmTextView.setOnClickListener {

            if (TextUtils.isEmpty(mIdEditText.text) || !Util().checkEmail(mIdEditText.text.toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_email))
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(mPasswordEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_password))
                return@setOnClickListener
            }

            if (mPasswordEditText.text.length < 6) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_checked_password))
                return@setOnClickListener
            }

            if (!TextUtils.isEmpty(mIdEditText.text) && !TextUtils.isEmpty(mPasswordEditText.text)) {
                getLogin(mIdEditText.text.toString(), mPasswordEditText.text.toString())
            }
        }

        // Non Member Text View
        val nonMemberTextView = mContainer.fragment_login_non_member_login_text_view
        nonMemberTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        nonMemberTextView.setOnSingleClickListener {
            MAPP.IS_NON_MEMBER_SERVICE = true
            MAPP.USER_DATA.setNationPhone(SharedPreferencesUtil().getNationPhoneCode(mActivity)!!)
            mListener.onMainScreen()

            getUserDeviceInformation()
        }

        // Find ID Text View
        val findIdTextView = mContainer.fragment_login_find_id_text_view
        findIdTextView.setOnClickListener {
            mListener.onFindIDScreen()
        }

        // Find Password Text View
        val findPasswordTextView = mContainer.fragment_login_find_password_text_view
        findPasswordTextView.setOnClickListener {
            mListener.onFindPasswordScreen(false)
        }

        // Sign Up Text View
        val signUpTextView = mContainer.fragment_login_sing_up_text_view
        signUpTextView.setOnSingleClickListener {
            val snsUserData = SnsUserData()
            snsUserData.setLoginType(LOGIN_TYPE_EMAIL)

            mListener.onSignUpScreen(snsUserData)
        }

        // SNS Login KAKAO
        mKakaoImageView = mContainer.fragment_login_sns_login_kakao_image_view
        mKakaoImageView.setOnClickListener {

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(mActivity)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(mActivity) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        NagajaLog().e("wooks, KAKAO 로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(mActivity, callback = mCallback) // 카카오 이메일 로그인
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        NagajaLog().d("wooks, KAKAO 로그인 성공 ${token.accessToken}")

                        if (AuthApiClient.instance.hasToken()) {
                            UserApiClient.instance.accessTokenInfo { _, error ->
                                if (error != null) {
                                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                                        //로그인 필요
                                    }
                                    else {
                                        //기타 에러
                                    }
                                } else {
                                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                                    UserApiClient.instance.me { user, error ->
                                        if (error != null) {
                                            NagajaLog().e("wooks, KAKAO 사용자 정보 요청 실패 $error")
                                        } else if (user != null) {
                                            NagajaLog().d("wooks, KAKAO 사용자 정보 요청 성공 : $user")

                                            NagajaLog().d("wooks, KAKAO id : ${user.id}")
                                            NagajaLog().d("wooks, KAKAO Email : ${user.kakaoAccount!!.email}")

                                            if (TextUtils.isEmpty(user.kakaoAccount!!.email)) {
                                                showToast(mActivity, mActivity.resources.getString(R.string.text_error_sign_up_kakao_empty_email))

                                                UserApiClient.instance.unlink { error ->
                                                    if (error != null) {
                                                        NagajaLog().e("wooks, KAKAO 연결 끊기 실패 $error")
                                                    }
                                                    else {
                                                        Log.d("wooks", "Success KAKAO Withdrawal")
                                                    }
                                                }
                                            } else {
                                                mLoginType = LOGIN_TYPE_KAKAO
//                                                if (!TextUtils.isEmpty(user.kakaoAccount?.profile?.profileImageUrl)) {
//                                                    MAPP.SNS_PROFILE_IMAGE_URL = user.kakaoAccount?.profile?.profileImageUrl
//                                                }

                                                val snsUserData = SnsUserData()
                                                snsUserData.setUserEmail(user.kakaoAccount!!.email!!)
                                                snsUserData.setLoginType(LOGIN_TYPE_KAKAO)
                                                snsUserData.setUsedId(user.id.toString())

                                                getSnsEmailCheck(snsUserData)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            //로그인 필요
                        }
                    }
                }
            } else {
//                UserApiClient.instance.loginWithKakaoAccount(mActivity, callback = mCallback) // 카카오 이메일 로그인

                UserApiClient.instance.loginWithKakaoAccount(mActivity) { token, error ->
                    if (AuthApiClient.instance.hasToken()) {
                        UserApiClient.instance.accessTokenInfo { _, error ->
                            if (error != null) {
                                if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                                    //로그인 필요
                                }
                                else {
                                    //기타 에러
                                }
                            }
                            else {
                                //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                                UserApiClient.instance.me { user, error ->
                                    if (error != null) {
                                        NagajaLog().e("사용자 정보 요청 실패 $error")
                                    } else if (user != null) {
                                        NagajaLog().d("사용자 정보 요청 성공 : $user")

                                        NagajaLog().d("Email : ${user.kakaoAccount!!.email}")
//                                        NagajaLog().d("Image : ${user.kakaoAccount?.profile?.profileImageUrl}")

                                        if (TextUtils.isEmpty(user.kakaoAccount!!.email)) {
                                            showToast(mActivity, mActivity.resources.getString(R.string.text_error_sign_up_kakao_empty_email))

                                            UserApiClient.instance.unlink { error ->
                                                if (error != null) {
                                                    NagajaLog().e("연결 끊기 실패 $error")
                                                }
                                                else {
                                                    Log.d("wooks", "Success KAKAO Withdrawal")
                                                }
                                            }
                                        } else {
                                            mLoginType = LOGIN_TYPE_KAKAO
//                                            if (!TextUtils.isEmpty(user.kakaoAccount?.profile?.profileImageUrl)) {
//                                                MAPP.SNS_PROFILE_IMAGE_URL = user.kakaoAccount?.profile?.profileImageUrl
//                                            }

                                            val snsUserData = SnsUserData()
                                            snsUserData.setUserEmail(user.kakaoAccount!!.email!!)
                                            snsUserData.setLoginType(LOGIN_TYPE_KAKAO)
                                            snsUserData.setUsedId(user.id.toString())

                                            getSnsEmailCheck(snsUserData)

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // SNS Login NAVER
        mNaverImageView = mContainer.fragment_login_sns_login_naver_image_view
        mNaverImageView.setOnClickListener {
            mOAuthLoginModule = OAuthLogin.getInstance()
            mOAuthLoginModule!!.init(
                mActivity,
                mActivity.resources.getString(R.string.naver_client_id),
                mActivity.resources.getString(R.string.naver_client_secret),
                mActivity.resources.getString(R.string.naver_client_name)
            )

            mOAuthLoginModule!!.startOauthLoginActivity(mActivity, mOAuthLoginHandler)
        }

        val naverAuthLoginButton = mContainer.fragment_login_sns_login_naver_login_button
        naverAuthLoginButton.setBgResourceId(R.drawable.icon_sns_naver)
        naverAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler)
        mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule!!.init(
            mActivity,
            mActivity.resources.getString(R.string.naver_client_id),
            mActivity.resources.getString(R.string.naver_client_secret),
            mActivity.resources.getString(R.string.naver_client_name)
        )

        // SNS Login Google Image View
        mGoogleImageView = mContainer.fragment_login_sns_login_google_image_view
        mGoogleImageView.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

       /* val facebookLoginButton = mContainer.fragment_login_sns_login_facebook_login_button
        facebookLoginButton.setReadPermissions(listOf("public_profile","email"))
        facebookLoginButton.fragment = this
        facebookLoginButton.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult?> {
            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
            }

            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

        })*/

        // SNS Login Facebook
        mFacebookImageView = mContainer.fragment_login_sns_login_facebook_image_view
        mFacebookImageView.setOnClickListener {

            // com.facebook.katana
            val packageName :  String = "com.facebook.katana"
            val packageManager: PackageManager = mActivity.packageManager
//            facebookLogin()
            if(isAppInstalled(packageName, packageManager)){
                facebookLogin()
            } else {
                //미설치
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_not_install_facebook))
            }
        }











        // 로그아웃 테스트 ===================
        val kakaoLogoutTest = mContainer.kakao_logout_test
        kakaoLogoutTest.setOnClickListener {
            if (!NetworkManager.checkNetworkState(mActivity)) {
                return@setOnClickListener
            }

            UserApiClient.instance.logout { error ->
                if (error != null) {
                    NagajaLog().e("wooks, 로그아웃 실패. SDK에서 토큰 삭제됨$error")
                } else {
                    NagajaLog().e("wooks, 로그아웃 성공. SDK에서 토큰 삭제됨")

//                    deleteShared()

                    MAPP.SNS_PROFILE_IMAGE_URL = ""
//                    startActivity(Intent(this@MainActivity, SplashActivity::class.java))
//                    finish()
                }
            }
        }

        val naverLogoutTest = mContainer.naver_logout_test
        naverLogoutTest.setOnClickListener {
            if (!NetworkManager.checkNetworkState(mActivity)) {
                return@setOnClickListener
            }

            val oAuthLoginModule = OAuthLogin.getInstance()
            oAuthLoginModule.init(
                mActivity,
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.naver_client_name)
            )

            oAuthLoginModule.logout(mActivity)

            NagajaLog().e("wooks, 네이버 로그아웃 성공")

//            deleteShared()

            MAPP.SNS_PROFILE_IMAGE_URL = ""
//            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
//            finish()
        }

        val googleLogoutTest = mContainer.google_logout_test
        googleLogoutTest.setOnClickListener {
            if (!NetworkManager.checkNetworkState(mActivity)) {
                return@setOnClickListener
            }

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso)
            mGoogleSignInClient.signOut().addOnCompleteListener(mActivity, OnCompleteListener<Void?> {
                NagajaLog().d("wooks, Google Logout")
//                deleteShared()

                MAPP.SNS_PROFILE_IMAGE_URL = ""
//                startActivity(Intent(this@MainActivity, SplashActivity::class.java))
//                finish()
            })
        }

        val facebookLogoutTest = mContainer.facebook_logout_test
        facebookLogoutTest.setOnClickListener {
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
            if (isLoggedIn) {
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                NagajaLog().e("wooks, 페이스북 로그아웃")
            } else {
                NagajaLog().e("wooks, 페이스북 세션 없음")
            }
        }







        // 회원 탈퇴 테스트 ===================
        val kakaoWithdrawalTest = mContainer.kakao_withdrawal_test
        kakaoWithdrawalTest.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    NagajaLog().e("wooks, KAKAO 연결 끊기 실패 = $error")
                }
                else {
                    showToast(mActivity, "Success KAKAO Withdrawal")
                }
            }
        }

        val naverWithdrawalTest = mContainer.naver_withdrawal_test
        naverWithdrawalTest.setOnClickListener {
            val oAuthLoginModule = OAuthLogin.getInstance()
            oAuthLoginModule.init(
                mActivity,
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.naver_client_name)
            )

            if (OAuthLoginState.OK == OAuthLogin.getInstance().getState(mActivity)) {
                try {
                    val deleteTokenTask = DeleteTokenTask(mActivity)
                    deleteTokenTask.execute(context)
//                    deleteMemberData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val googleWithdrawalTest = mContainer.google_withdrawal_test
        googleWithdrawalTest.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(mActivity, gso)
            googleSignInClient.revokeAccess().addOnCompleteListener(mActivity) {
//                deleteMemberData()
                showToast(mActivity, "Success GOOGLE Withdrawal")
            }
        }

        val facebookWithdrawalTest = mContainer.facebook_withdrawal_test
        facebookWithdrawalTest.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val accessToken = AccessToken.getCurrentAccessToken()
            if (user != null) {
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                if (isLoggedIn) {
                    val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
                    user.reauthenticate(credential)
                        .addOnSuccessListener { task: Void? ->
                            user.unlink("facebook.com")
                                .addOnSuccessListener { task1: AuthResult? ->
                                    NagajaLog().e("wooks, 페이스북 회원탈퇴")
                                    user.delete()
                                    FirebaseAuth.getInstance().signOut()
                                    LoginManager.getInstance().logOut()
                                }
                        }
                }
            }
        }
    }

    fun facebookLogin(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile","email"))

        LoginManager.getInstance()
            .registerCallback(mFacebookCallbackManager, object : FacebookCallback<LoginResult>{
                override fun onCancel() {

                }

                override fun onError(error: FacebookException) {
//                    TODO("Not yet implemented")
                    NagajaLog().e("wooks, error.toString() = ${error.toString()}")
                }

                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result?.accessToken!!)
                }

            })
    }

//    fun facebookLogin(){
//        LoginManager.getInstance()
//            .logInWithReadPermissions(this, listOf("public_profile","email"))
//
//        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK)
//
//        LoginManager.getInstance()
//            .registerCallback(mFacebookCallbackManager, object : FacebookCallback<LoginResult>{
//                override fun onSuccess(result: LoginResult) {
//                    // 로그인 성공시
//                    handleFacebookAccessToken(result.accessToken)
//                }
//
//                override fun onCancel() {
//                    NagajaLog().e("wooks, Facebook Login Cancel")
//               }
//
//                override fun onError(error: FacebookException) {
//                    NagajaLog().e("wooks, Facebook Login Error = ${error.toString()}")
//                }
//            })
//    }

    private fun showSnsMemberSignUpCustomDialog(snsUserData: SnsUserData) {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_noti),
            mActivity.resources.getString(R.string.text_social_membership_required),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()
                mListener.onSignUpScreen(snsUserData)
            }
        })
        customDialog.show()
    }

    // Naver Withdrawal Test
    internal class DeleteTokenTask(mContext: Context) : AsyncTask<Context?, Void?, Boolean?>() {

        var context: Context

        override fun doInBackground(vararg contexts: Context?): Boolean {
            return OAuthLogin.getInstance().logoutAndDeleteToken(contexts[0])
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            NagajaLog().d("wooks, Naver Withdrawal ==> $result")
        }

        init {
            context = mContext
        }
    }

    val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 접근이 거부 됨(동의 취소)")
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 유효하지 않은 앱")
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 유효하지 않은 인증 수단이 유효하지 않아 인증할 수 없는 상태")
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 요청 파라미터 오류")
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 유효하지 않은 scope ID")
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 설정이 올바르지 않음(android key hash)")
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 서버 내부 에러")
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    NagajaLog().e("wooks, KAKAO Error ==> 앱이 요청 권한이 없음")
                }
                else -> { // Unknown
                    NagajaLog().e("wooks, KAKAO Error ==> 기타 에러 ($error)")
                }
            }
        } else if (token != null) {
            NagajaLog().d("wooks, KAKAO Success ==> 로그인에 성공하였습니다")
        }
    }

    /**
     * Naver Login
     * */
    @SuppressLint("HandlerLeak")
    private var mOAuthLoginHandler: OAuthLoginHandler? = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken: String = mOAuthLoginModule!!.getAccessToken(mActivity)
                val refreshToken: String = mOAuthLoginModule!!.getRefreshToken(mActivity)
                val expiresAt: Long = mOAuthLoginModule!!.getExpiresAt(mActivity)
                val tokenType: String = mOAuthLoginModule!!.getTokenType(mActivity)
                Log.i("LoginData", "accessToken : $accessToken")
                Log.i("LoginData", "refreshToken : $refreshToken")
                Log.i("LoginData", "expiresAt : $expiresAt")
                Log.i("LoginData", "tokenType : $tokenType")

                getUserInfo(accessToken)
            } else {
                val errorCode: String = mOAuthLoginModule!!.getLastErrorCode(mActivity).code
                val errorDesc: String = mOAuthLoginModule!!.getLastErrorDesc(mActivity)

                if (errorCode != "user_cancel") {
                    showToast(mActivity, "errorCode:$errorCode, errorDesc:$errorDesc")
                }
            }
        }
    }

    private fun getUserInfo(token: String) {
        val handler1 = Handler()
        handler1.postDelayed({
            Thread {
                val header = "Bearer $token" // Bearer 다음에 공백 추가
                val apiURL = "https://openapi.naver.com/v1/nid/me"
                val requestHeaders: MutableMap<String, String> = HashMap()
                requestHeaders["Authorization"] = header
                val responseBody: String = get(apiURL, requestHeaders)!!
                try {
                    val obj = JSONObject(responseBody)
                    if (obj.getString("message") == "success") {
                        val profile = JSONObject(obj.getString("response"))

//                        NagajaLog().d("wooks, NAVER Email = " + profile.getString("email"))
//                        NagajaLog().d("wooks, NAVER Profile Image = " + profile.getString("profile_image"))

                        mLoginType = LOGIN_TYPE_NAVER
//                        if (!TextUtils.isEmpty(profile.getString("profile_image"))) {
//                            MAPP.SNS_PROFILE_IMAGE_URL = profile.getString("profile_image")
//                        }

                        val snsUserData = SnsUserData()
                        snsUserData.setUserEmail(profile.getString("email"))
                        snsUserData.setUserName(profile.getString("name"))
                        snsUserData.setLoginType(LOGIN_TYPE_NAVER)
                        snsUserData.setUsedId(profile.getString("id"))

                        NagajaLog().d("wooks, snsUserData = " + snsUserData.getUsedId())

                        getSnsEmailCheck(snsUserData)

                    }
                } catch (e: java.lang.Exception) {
                    NagajaLog().e("wooks, Naver Login Exception = $e")
                }
                //Log.i("getUserInfo","getUserInfo : "+ responseBody);
            }.start()
        }, 1)
    }

    private operator fun get(apiUrl: String, requestHeaders: Map<String, String>): String? {
        val con: HttpURLConnection = connect(apiUrl)!!
        return try {
            con.requestMethod = "GET"

            val map : Map.Entry<String, String>
            for (map in requestHeaders) {
                con.setRequestProperty(map.key, map.value)
            }

            val responseCode = con.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                readBody(con.inputStream)
            } else { // 에러 발생
                readBody(con.errorStream)
            }
        } catch (e: IOException) {
            throw java.lang.RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }

    private fun connect(apiUrl: String): HttpURLConnection? {
        return try {
            val url = URL(apiUrl)
            url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw java.lang.RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
        } catch (e: IOException) {
            throw java.lang.RuntimeException("연결이 실패했습니다. : $apiUrl", e)
        }
    }

    private fun readBody(body: InputStream): String? {
        val streamReader = InputStreamReader(body)
        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()
                var line: String?
                while (lineReader.readLine().also { line = it } != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw java.lang.RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }
    
    /**
     * Google Login
     * */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            NagajaLog().d("wooks, Google Login Email = ${account.email}" )
            NagajaLog().d("wooks, Google profileUrl = ${account.photoUrl.toString()}" )
            NagajaLog().d("wooks, Google displayName = ${account.displayName.toString()}" )
            NagajaLog().d("wooks, Google serverAuthCode = ${account.serverAuthCode}" )


            mLoginType = LOGIN_TYPE_GOOGLE
            if (null != account.photoUrl) {
                if (!TextUtils.isEmpty(account.photoUrl.toString())) {
                    MAPP.SNS_PROFILE_IMAGE_URL = account.photoUrl.toString()
                }
            }

            val snsUserData = SnsUserData()
            snsUserData.setUserEmail(account.email!!)
            snsUserData.setUserProfileUrl(if (!TextUtils.isEmpty(account.photoUrl.toString())) account.photoUrl.toString() else "")
            snsUserData.setUserName(if (!TextUtils.isEmpty(account.displayName.toString())) account.displayName.toString() else "")
            snsUserData.setLoginType(LOGIN_TYPE_GOOGLE)
            snsUserData.setUsedId(if (!TextUtils.isEmpty(account.id.toString())) account.id.toString() else "")

            getSnsEmailCheck(snsUserData)

//            if (!TextUtils.isEmpty(snsUserData.getUserEmail())) {
//                getJwtTokenByEmail(snsUserData)
//            }

            // TODO: Implement Sign-Up
//            getEmailLogin(account.email!!, account.email!!, isEmailLogin = false, isForgotPassword = false)
        } catch (e: ApiException) {
            NagajaLog().d("wooks, GoogleLogin signInResult:failed code = ${e.statusCode}")
        }
    }

    private fun getKeyHash(): String? {
        var hashKey : String? = null
        try {
            val info : PackageInfo = mActivity.packageManager.getPackageInfo(mActivity.packageName, PackageManager.GET_SIGNATURES)

            for (signature in info.signatures) {
                var md : MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                hashKey = String(Base64.encode(md.digest(), 0))

                Log.d("wooks", hashKey)
            }

        } catch (e:Exception){
            Log.e("wooks", e.toString())
        }

        return hashKey
    }

    private fun getKeyHash(sha1: String) {
        val sha1Arr = sha1.split(':')
        var byteArr =byteArrayOf()
        for (hex in sha1Arr) {
            byteArr += Integer.parseInt(hex, 16).toByte()
        }

        NagajaLog().e("wooks, getKeyHash Key Hash : ${Base64.encodeToString(byteArr, Base64.NO_WRAP)}")
    }

    /**
     * Facebook Install Check
     * */
    private fun isAppInstalled(packageName : String, packageManager : PackageManager) : Boolean{
        return try{
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (ex : PackageManager.NameNotFoundException) {
            NagajaLog().e("wooks, isAppInstalled Exception = $ex")
            false
        }
    }

    private fun getLocation(): String {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                SplashFragment.PERMISSION_REQUEST_CODE
            )

            return ""
        } else {
            val gpsTracker = GpsTracker(mActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

//            SharedPreferencesUtil().setSaveLocation(mActivity, "$latitude,$longitude")
            return "$latitude,$longitude"
        }
    }

    /**
     * Facebook
     * */
    private fun handleFacebookAccessToken(token: AccessToken) {

        Log.d("wooks", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        val auth = FirebaseAuth.getInstance()

        auth.signInWithCredential(credential).addOnCompleteListener(mActivity) { task ->

            if (task.isSuccessful) {
                NagajaLog().d("wooks, signInWithCredential:success")
                val user = auth.currentUser
                user?.let {
//                    NagajaLog().d("wooks, ============= Facebook Login Success =============")
//                    NagajaLog().d("wooks, Facebook Name = ${user.displayName}")
//                    NagajaLog().d("wooks, Facebook Email = ${user.email}")
////                    NagajaLog().d("wooks, Facebook PhotoUrl = ${user.photoUrl}")
//                    NagajaLog().d("wooks, Facebook phoneNumber = ${user.phoneNumber}")
//                    NagajaLog().d("wooks, Facebook uid = ${user.uid}")
//                    NagajaLog().d("wooks, Facebook getIdToken = ${user.getIdToken(false)}")
//                    NagajaLog().d("wooks, Facebook token.userId = ${token.userId}")

                    mLoginType = LOGIN_TYPE_FACEBOOK

                    val snsUserData = SnsUserData()
                    snsUserData.setUserEmail(if (!TextUtils.isEmpty(user.email)) user.email!! else "")
                    snsUserData.setUserName(if (!TextUtils.isEmpty(user.displayName)) user.displayName!! else "")
                    snsUserData.setLoginType(LOGIN_TYPE_FACEBOOK)
                    snsUserData.setUsedId(if (!TextUtils.isEmpty(token.userId)) token.userId else "")

                    getSnsEmailCheck(snsUserData)

                }
            } else {
                NagajaLog().d("wooks, signInWithCredential:failure = ${task.exception}")
            }
        }
    }

    private fun showLoginErrorCustomPopup(errorCode: Int, email: String) {

        var title = ""
        var message = ""
        when (errorCode) {
            ERROR_CODE_NOT_CONFIRM_EMAIL,
            ERROR_CODE_NOT_CONFIRM_EMAIL_SNS-> {
                title = mActivity.resources.getString(R.string.text_email_auth)
                message = mActivity.resources.getString(R.string.text_error_message_confirm_email_auth)
            }

            ERROR_CODE_BLOCK_MEMBER -> {
                title = mActivity.resources.getString(R.string.text_block_account)
                message = mActivity.resources.getString(R.string.text_message_block_account)
            }

            ERROR_CODE_HUMAN_ACCOUNT -> {
                title = mActivity.resources.getString(R.string.text_dormant_account)
//                message = mActivity.resources.getString(R.string.text_message_block_account)
            }

            ERROR_CODE_WITHDRAWAL_REQUEST,
            ERROR_CODE_WITHDRAWAL_COMPLETE -> {
                title = mActivity.resources.getString(R.string.text_withdrawal_account)
                message = mActivity.resources.getString(R.string.text_message_withdrawal_account)
            }

            ERROR_CODE_NOT_FOUND_PASSWORD -> {
                title = mActivity.resources.getString(R.string.text_noti)
                message = mActivity.resources.getString(R.string.text_error_not_match_password)
            }
        }

        val customDialog = CustomDialog(
            mActivity,
            if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL || errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL_SNS) DIALOG_TYPE_NORMAL else DIALOG_TYPE_NO_CANCEL,
            title,
            message,
            if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL || errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL_SNS) mActivity.resources.getString(R.string.text_resend) else mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL || errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL_SNS) {
                    getSignUpSendEmail(email)
                }
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get FCM Registration Token
                val token: String = task.result!!
                NagajaLog().d("wooks, FCM Token = $token")

                if (SharedPreferencesUtil().getIsNewInstall(mActivity)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_TYPE_EVENT)
                    FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_TYPE_NOTICE)
                    FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_TYPE_SERVER_CHECK)
                }

                MAPP.USER_FCM_TOKEN = token

                getLoginSuccessDeviceInformation()
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        NagajaLog().d("wooks, Login Fragment onActivityResult:$requestCode:$resultCode:$data")
        when (requestCode) {
            RC_GET_TOKEN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
            FB_SIGN_IN -> {
    //            mCallbackManager.onActivityResult(requestCode, resultCode, data);
                mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)
            }
            KAKAO -> {
            }
        }
    }

    /**
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginFragmentListener) {
            mActivity = context as Activity

            if (context is OnLoginFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnLoginFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnLoginFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnLoginFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // ============================================================================================
    // API.
    // ============================================================================================

    /**
     * API. getLogin
     */
    private fun getLogin(email: String, password: String) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLogin(
            mRequestQueue,
            object : NagajaManager.RequestListener<UserData> {
                override fun onSuccess(resultData: UserData) {
                    NagajaLog().d("wooks, Login Success Email = ${resultData.getEmailId()}")

                    mLoginType = LOGIN_TYPE_EMAIL

                    SharedPreferencesUtil().setSecureKey(mActivity, resultData.getSecureKey())
                    SharedPreferencesUtil().setRefreshKey(mActivity, resultData.getRefreshKey())
                    SharedPreferencesUtil().setLoginType(mActivity, mLoginType)

                    MAPP.USER_DATA = resultData

                    MAPP.IS_NON_MEMBER_SERVICE = false

                    mListener.onMainScreen()

                    getFcmToken()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    NagajaLog().e("wooks, errorCode = $errorCode")
                    if (errorCode == 1) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_checked_email_password))
                    } else {
                        showLoginErrorCustomPopup(errorCode!!, email)
                    }
                }
            },
            email,
            password
        )
    }

    /**
     * API. Get Exist Email Check
     */
//    private fun getExistEmailCheck(snsUserData: SnsUserData) {
//        val nagajaManager = NagajaManager().getInstance()
//        nagajaManager?.getExistEmailCheck(
//            mRequestQueue,
//            object : NagajaManager.RequestListener<String> {
//                override fun onSuccess(resultData: String) {
//                    NagajaLog().d("wooks, Is User = $resultData")
//                    if (resultData != "false") {
//                        if (!TextUtils.isEmpty(SharedPreferencesUtil().getSaveUserEmail(mActivity)) && !TextUtils.isEmpty(SharedPreferencesUtil().getSaveUserPassword(mActivity))) {
//                            getLogin(SharedPreferencesUtil().getSaveUserEmail(mActivity)!!, SharedPreferencesUtil().getSaveUserPassword(mActivity)!!)
//                        } else {
//                            getLogin(snsUserData.getUserEmail()!!, resultData)
//                        }
//                    } else {
//                        mListener.onSignUpScreen(snsUserData)
//                    }
//                }
//
//                override fun onFail(errorMsg: String?) {
//                    NagajaLog().e("wooks, errorMsg = $errorMsg")
//                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
//                }
//
//                override fun onFail(errorCode: Int?) {
//                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
//                        disConnectUserToken(mActivity)
//                    } else {
//                        showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
//                    }
//                }
//            },
//            snsUserData.getUserEmail()!!
//        )
//    }

    /**
     * API. Get JWT Token By Email
     */
    private fun getJwtTokenByEmail(snsUserData: SnsUserData) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getJwtTokenByEmail(
            mRequestQueue,
            object : NagajaManager.RequestListener<JwtTokenRefreshData> {
                override fun onSuccess(resultData: JwtTokenRefreshData) {

                    SharedPreferencesUtil().setSecureKey(mActivity, resultData.getSecureKey())
                    SharedPreferencesUtil().setRefreshKey(mActivity, resultData.getRefreshKey())

                    getSecureKeyLogin(resultData.getSecureKey()!!)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
//                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
//                        disConnectUserToken(mActivity)
//                    } else {
//                        showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
//                    }
                }
            },
            snsUserData.getUserEmail()!!
        )
    }

    /**
     * API. getSecureKeyLogin
     */
    private fun getSecureKeyLogin(secureKey: String) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSecureKeyLogin(
            mRequestQueue,
            object : NagajaManager.RequestListener<UserData> {
                override fun onSuccess(resultData: UserData) {

                    SharedPreferencesUtil().setSecureKey(mActivity, resultData.getSecureKey())
                    SharedPreferencesUtil().setRefreshKey(mActivity, resultData.getRefreshKey())
                    SharedPreferencesUtil().setLoginType(mActivity, mLoginType)

                    MAPP.USER_DATA = resultData

                    MAPP.IS_NON_MEMBER_SERVICE = false

                    mListener.onMainScreen()
                    getFcmToken()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
//                    if (errorCode == 1) {
//                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_checked_email_password))
//                    }
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        getJwtTokenRefresh()
                    } else {
                        if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL || errorCode == ERROR_CODE_BLOCK_MEMBER || errorCode == ERROR_CODE_HUMAN_ACCOUNT
                            || errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL_SNS || errorCode == ERROR_CODE_WITHDRAWAL_REQUEST || errorCode == ERROR_CODE_WITHDRAWAL_COMPLETE) {
                            showLoginErrorCustomPopup(errorCode, "")
                        }
                    }
                }
            },
            secureKey
        )
    }

    /**
     * API. getSecureKeyLogin
     */
    private fun getJwtTokenRefresh() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getJwtTokenRefresh(
            mRequestQueue,
            object : NagajaManager.RequestListener<JwtTokenRefreshData> {
                override fun onSuccess(resultData: JwtTokenRefreshData) {

                    getSecureKeyLogin(resultData.getSecureKey()!!)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            SharedPreferencesUtil().getRefreshKey(mActivity)!!
        )
    }

    /**
     * API. Get SNS Email Check
     */
    private fun getSnsEmailCheck(snsUserData: SnsUserData) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSnsEmailCheck(
            mRequestQueue,
            object : NagajaManager.RequestListener<UserData> {
                override fun onSuccess(resultData: UserData) {
                    NagajaLog().e("wooks, Success")
                    if (resultData.getErrorStatus() == ErrorRequest.ERROR_CODE_DIFFERENT_TYPE_SIGN_UP) {

                        var memberType = mActivity.resources.getString(R.string.text_email)
                        when (resultData.getDifferentUserType()) {

                            // Email
                            1 -> {
                                memberType = mActivity.resources.getString(R.string.text_email)
                            }

                            // 카카오
                            2 -> {
                                memberType = mActivity.resources.getString(R.string.text_kakao)
                            }

                            // 네이버
                            3 -> {
                                memberType = mActivity.resources.getString(R.string.text_naver)
                            }

                            // 구글
                            4 -> {
                                memberType = mActivity.resources.getString(R.string.text_google)
                            }

                            // 애플
                            5 -> {
                                memberType = mActivity.resources.getString(R.string.text_apple)
                            }

                            // 페이스북
                            6 -> {
                                memberType = mActivity.resources.getString(R.string.text_facebook)
                            }
                        }

                        showToast(mActivity, "$memberType${mActivity.resources.getString(R.string.text_error_message_different_sns)}")
                    } else if (resultData.getErrorStatus() == ErrorRequest.ERROR_CODE_NO_MEMBER) {
                        snsUserData.setSocialUid(resultData.getDifferentUserType())
                        snsUserData.setMemberSocialUid(resultData.getSocialUid())

                        showSnsMemberSignUpCustomDialog(snsUserData)
                    } else {

                        MAPP.IS_NON_MEMBER_SERVICE = false

                        SharedPreferencesUtil().setSecureKey(mActivity, resultData.getSecureKey())
                        SharedPreferencesUtil().setRefreshKey(mActivity, resultData.getRefreshKey())
                        SharedPreferencesUtil().setLoginType(mActivity, mLoginType)

                        MAPP.USER_DATA = resultData

                        mListener.onMainScreen()
                        getFcmToken()
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    NagajaLog().e("wooks, errorCode = $errorCode")

                    if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL || errorCode == ERROR_CODE_BLOCK_MEMBER || errorCode == ERROR_CODE_HUMAN_ACCOUNT
                        || errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL_SNS || errorCode == ERROR_CODE_WITHDRAWAL_REQUEST || errorCode == ERROR_CODE_WITHDRAWAL_COMPLETE) {
                        showLoginErrorCustomPopup(errorCode, snsUserData.getUserEmail()!!)
                    }
                }
            },
            snsUserData
        )
    }

    /**
     * API. Get Sign Up Send Email
     */
    private fun getSignUpSendEmail(email: String) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSignUpSendEmail(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_auth_email))
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            email
        )
    }

    /**
     * API. getLoginSuccessDeviceInformation
     */
    private fun getLoginSuccessDeviceInformation() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLoginSuccessDeviceInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            Util().getAndroidID(mActivity),
            "ANDROID",
            MAPP.USER_FCM_TOKEN
        )
    }

    /**
     * API. Device Information
     * */
    private fun getUserDeviceInformation() {
        var location = ""
        location = if (null == getLocation() || TextUtils.isEmpty(getLocation()) || getLocation() == "null") {
            "0,0"
        } else {
            getLocation()
        }

        if (TextUtils.isEmpty(location) || location == "0,0") {
            when (SharedPreferencesUtil().getSelectLanguage(mActivity)) {
                SELECT_LANGUAGE_EN -> {
                    location = CURRENT_LOCATION_EN
                }

                SELECT_LANGUAGE_KO -> {
                    location = CURRENT_LOCATION_KO
                }

                SELECT_LANGUAGE_FIL -> {
                    location = CURRENT_LOCATION_FIL
                }

                SELECT_LANGUAGE_ZH -> {
                    location = CURRENT_LOCATION_ZH
                }

                SELECT_LANGUAGE_JA -> {
                    location = CURRENT_LOCATION_JA
                }
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUserDeviceInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<String?> {
                override fun onSuccess(resultData: String?) {
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, Splash getUserDeviceInformation API Error = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    NagajaLog().e("wooks, Splash getUserDeviceInformation API Error Code = $errorCode")
                }

            },
            Util().getAndroidID(mActivity),
            "ANDROID",
            Build.MODEL,
            mActivity.packageManager.getPackageInfo(mActivity.packageName, 0).versionName,
            location,
            MAPP.USER_FCM_TOKEN,
            SharedPreferencesUtil().getSelectLanguage(mActivity)!!.lowercase(Locale.getDefault())
        )
    }
}