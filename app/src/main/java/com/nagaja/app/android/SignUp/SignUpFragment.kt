package com.nagaja.app.android.SignUp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Data.PhoneAuthData
import com.nagaja.app.android.Data.SnsUserData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXISTING_ID
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXISTING_NICK_NAME
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern

class SignUpFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mPostalCodeView: View
    private lateinit var mPasswordView: View
    private lateinit var mEmailDuplicateCheckView: View
    private lateinit var mNickNameDuplicateCheckView: View

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mNameEditText: EditText
    private lateinit var mIDEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mPasswordConfirmEditText: EditText
    private lateinit var mNickNameEditText: EditText
    private lateinit var mEmailEditText: EditText
    private lateinit var mPhoneNumberEditText: EditText
    private lateinit var mAddressDetailEditText: EditText
    private lateinit var mReferralEditText: EditText

    private lateinit var mAllAgreeCheckImageView: ImageView
    private lateinit var mTermsCheckImageView: ImageView
    private lateinit var mPolicyCheckImageView: ImageView
    private lateinit var mThirdPartyCheckImageView: ImageView
    private lateinit var mLocationInformationCheckImageView: ImageView
    private lateinit var mEventCheckImageView: ImageView
    private lateinit var mServiceCheckImageView: ImageView
    private lateinit var mAddressIconImageView: ImageView
    private lateinit var mEmailDuplicateCheckImageView: ImageView
    private lateinit var mNickNameDuplicateCheckImageView: ImageView
    private lateinit var mSnsIconImageView: ImageView

    private lateinit var mSuccessSignUpTextView: TextView
    private lateinit var mAuthTextView: TextView
    private lateinit var mNameErrorMessageTextView: TextView
    private lateinit var mIDErrorMessageTextView: TextView
    private lateinit var mPasswordErrorMessageTextView: TextView
    private lateinit var mPasswordConfirmErrorMessageTextView: TextView
    private lateinit var mNickNameErrorMessageTextView: TextView
    private lateinit var mEmailErrorMessageTextView: TextView
    private lateinit var mPostalCodeTextView: TextView
    private lateinit var mAddressTextView: TextView
    private lateinit var mEmailDuplicateCheckTextView: TextView
    private lateinit var mNickNameDuplicateCheckTextView: TextView

    private lateinit var mSelectNationSpinner: PowerSpinnerView

    private lateinit var mPhoneAuthData: PhoneAuthData

    private lateinit var mRequestQueue: RequestQueue

    private var mIsPhoneNumberAuth = false
    private var mIsAllCheckAgree = false
    private var mIsTermsAgree = false
    private var mIsPolicyAgree = false
    private var mIsThirdPartyAgree = false
    private var mIsLocationInformationAgree = false
    private var mIsEventAgree = false
    private var mIsServiceAgree = false
    private var mIsSuccessAgree = false

    private var mIsSuccessConfirm = false
    private var mIsSnsSignUp = false

    private var mIsEmailCheck = false
    private var mIsNickNameCheck = false

    private var mIsPasswordShow = false
    private var mIsPasswordConfirmShow = false

    private var mAddressCode = 0

    private lateinit var mListener: OnSignUpFragmentListener

    interface OnSignUpFragmentListener {
        fun onFinish()
        fun onPhoneAuthScreen(phoneAuthType: Int)
        fun onSuccessSignUp()
        fun onAddressScreen(isKorea: Boolean)
        fun onTermsWebViewScreen(selectType: Int, url: String)
    }

    companion object {
        const val AGREE_TYPE_TERMS                       = 1
        const val AGREE_TYPE_POLICY                      = 2
        const val AGREE_TYPE_THIRD_PARTY                 = 3
        const val AGREE_TYPE_LOCATION_INFORMATION        = 4

        private const val ARG_SNS_USER_DATA              = "arg_sns_user_data"

        fun newInstance(snsUserData: SnsUserData): SignUpFragment {
            val args = Bundle()
            val fragment = SignUpFragment()
            args.putSerializable(ARG_SNS_USER_DATA, snsUserData)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getSnsUserData(): SnsUserData {
        val arguments = arguments
        return arguments?.getSerializable(ARG_SNS_USER_DATA) as SnsUserData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_signup, container, false)

        mIsSnsSignUp = getSnsUserData().getLoginType() != LOGIN_TYPE_EMAIL

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.setOnClickListener {
            mListener.onFinish()
        }

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

        // Scroll View
        mScrollView = mContainer.fragment_signup_scroll_view

        // Name Edit Text
        mNameEditText = mContainer.fragment_signup_name_edit_text
        mNameEditText.filters = arrayOf(Util().blankSpaceFilter)
        mNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mNameEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (TextUtils.isEmpty(mNameEditText.text)) {
                    mNameErrorMessageTextView.visibility = View.VISIBLE
                    mNameErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_name)
                } else {
                    mNameErrorMessageTextView.visibility = View.INVISIBLE
                }
            }
        }

        // Name Error Message Text View
        mNameErrorMessageTextView = mContainer.fragment_signup_name_error_text_view

        // ID Edit Text
//        mIDEditText = mContainer.fragment_signup_id_edit_text
//        mIDEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(20))
//        mIDEditText.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })
//        mIDEditText.setOnFocusChangeListener { v, hasFocus ->
//            if (!hasFocus) {
//                if (TextUtils.isEmpty(mIDEditText.text)) {
//                    mIDErrorMessageTextView.visibility = View.VISIBLE
//                    mIDErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_id)
//                } else {
//                    mIDErrorMessageTextView.visibility = View.INVISIBLE
//                }
//            }
//        }
//
//        // ID Error Message Text View
//        mIDErrorMessageTextView = mContainer.fragment_signup_id_error_text_view

        // Password View
        mPasswordView = mContainer.fragment_signup_password_view
        if (mIsSnsSignUp) {
            mPasswordView.visibility = View.GONE
        }

        // Password Edit Text
        mPasswordEditText = mContainer.fragment_signup_password_edit_text
        mPasswordEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(20))
        mPasswordEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mPasswordEditText.text.length < 8) {
                    mPasswordErrorMessageTextView.visibility = View.VISIBLE
                    mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_least_8_characters)
                } else {
                    mPasswordErrorMessageTextView.visibility = View.INVISIBLE
                }

                NagajaLog().e("wooks, !!!!!!!!!! = ${Util().passwordRegularExpressionCheck(mPasswordEditText.text.toString())}")
            }
        })
        mPasswordEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (TextUtils.isEmpty(mPasswordEditText.text)) {
                    mPasswordErrorMessageTextView.visibility = View.VISIBLE
                    mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_password)
                } else {
                    mPasswordErrorMessageTextView.visibility = View.INVISIBLE
                }

//                getCheckPassword()
            }
        }

        // Password Show Icon Image View
        val passwordShowIconImageView = mContainer.fragment_signup_password_show_and_hide_image_view
        passwordShowIconImageView.setOnClickListener {
            mIsPasswordShow = !mIsPasswordShow

            if (mIsPasswordShow) {
                mPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordShowIconImageView.setImageResource(R.drawable.icon_password_show)
            } else {
                mPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordShowIconImageView.setImageResource(R.drawable.icon_password_hide)
            }

            mPasswordEditText.setSelection(mPasswordEditText.length())
        }

        // Password Error Message Text View
        mPasswordErrorMessageTextView = mContainer.fragment_signup_password_error_text_view

        // Password Confirm Edit Text
        mPasswordConfirmEditText = mContainer.fragment_signup_password_confirm_edit_text
        mPasswordConfirmEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(20))
        mPasswordConfirmEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mPasswordConfirmEditText.text.length < 8) {
                    mPasswordConfirmErrorMessageTextView.visibility = View.VISIBLE
                    mPasswordConfirmErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_least_8_characters)
                } else {
                    mPasswordConfirmErrorMessageTextView.visibility = View.INVISIBLE
                }

            }
        })
        mPasswordConfirmEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (TextUtils.isEmpty(mPasswordConfirmEditText.text)) {
                    mPasswordConfirmErrorMessageTextView.visibility = View.VISIBLE
                    mPasswordConfirmErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_password)
                } else {
                    mPasswordConfirmErrorMessageTextView.visibility = View.INVISIBLE
                }

//                getCheckPassword()
            }
        }

        // Password Confirm Show Icon Image View
        val passwordConfirmShowIconImageView = mContainer.fragment_signup_password_confirm_show_and_hide_image_view
        passwordConfirmShowIconImageView.setOnClickListener {
            mIsPasswordConfirmShow = !mIsPasswordConfirmShow

            if (mIsPasswordConfirmShow) {
                mPasswordConfirmEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordConfirmShowIconImageView.setImageResource(R.drawable.icon_password_show)
            } else {
                mPasswordConfirmEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordConfirmShowIconImageView.setImageResource(R.drawable.icon_password_hide)
            }

            mPasswordConfirmEditText.setSelection(mPasswordConfirmEditText.length())
        }

        // Password Confirm Error Message Text View
        mPasswordConfirmErrorMessageTextView = mContainer.fragment_signup_password_confirm_error_text_view

        // Nick Name Edit Text
        mNickNameEditText = mContainer.fragment_signup_nick_name_edit_text
        mNickNameEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(20))
        mNickNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                mNickNameDuplicateCheckTextView.visibility = View.VISIBLE
                mNickNameDuplicateCheckImageView.visibility = View.GONE
                mIsNickNameCheck = false
            }
        })
        mNickNameEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (TextUtils.isEmpty(mNickNameEditText.text)) {
                    mNickNameErrorMessageTextView.visibility = View.VISIBLE
                    mNickNameErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_nick_name)
                } else {
                    mNickNameErrorMessageTextView.visibility = View.INVISIBLE
                }
            }
        }

        // Nick Name Error Message Text View
        mNickNameErrorMessageTextView = mContainer.fragment_signup_nick_name_error_text_view

        // Nick Name Duplicate Check View
        mNickNameDuplicateCheckView = mContainer.fragment_signup_nick_name_duplicate_check_view
        mNickNameDuplicateCheckView.setOnClickListener {
            if (mIsNickNameCheck) {
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mNickNameEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_nick_name))
                return@setOnClickListener
            }

            getSignUpEmailAndNickNameCheck(mNickNameEditText.text.toString(), false)
        }

        // Nick Name Duplicate Check Text View
        mNickNameDuplicateCheckTextView = mContainer.fragment_signup_nick_name_duplicate_check_text_view

        // Nick name Duplicate Check Image View
        mNickNameDuplicateCheckImageView = mContainer.fragment_signup_nick_name_duplicate_check_image_view

        // Email Edit Text
        mEmailEditText = mContainer.fragment_signup_email_edit_text
        mEmailEditText.filters = arrayOf(Util().blankSpaceFilter)
        mEmailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mEmailEditText.text.isEmpty()) {
                    mEmailErrorMessageTextView.visibility = View.INVISIBLE
                }

                mEmailDuplicateCheckTextView.visibility = View.VISIBLE
                mEmailDuplicateCheckImageView.visibility = View.GONE
                mIsEmailCheck = false
            }
        })
        mEmailEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (!TextUtils.isEmpty(mEmailEditText.text)) {
                    if (!Util().checkEmail(mEmailEditText.text.toString())) {
                        mEmailErrorMessageTextView.visibility = View.VISIBLE
                        mEmailErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_valid_email)
                    } else {
                        mEmailErrorMessageTextView.visibility = View.INVISIBLE
                    }
                }
            }
        }

        // SNS Icon Image View
        mSnsIconImageView = mContainer.fragment_signup_sns_image_view
        if (getSnsUserData().getLoginType() == LOGIN_TYPE_EMAIL) {
            mSnsIconImageView.visibility = View.GONE
        } else {
            mSnsIconImageView.visibility = View.VISIBLE

            when (getSnsUserData().getLoginType()) {
                LOGIN_TYPE_KAKAO -> {
                    mSnsIconImageView.setImageResource(R.drawable.icon_sns_kakao)
                }

                LOGIN_TYPE_NAVER -> {
                    mSnsIconImageView.setImageResource(R.drawable.icon_sns_naver)
                }

                LOGIN_TYPE_GOOGLE -> {
                    mSnsIconImageView.setImageResource(R.drawable.icon_sns_google)
                }

                LOGIN_TYPE_FACEBOOK -> {
                    mSnsIconImageView.setImageResource(R.drawable.icon_sns_facebook)
                }
            }
        }

        // Email Duplicate Check View
        mEmailDuplicateCheckView = mContainer.fragment_signup_email_duplicate_check_view
        mEmailDuplicateCheckView.setOnClickListener {
            if (mIsEmailCheck) {
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mEmailEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_email))
                return@setOnClickListener
            } else if (!Util().checkEmail(mEmailEditText.text.toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_valid_email))
                return@setOnClickListener
            }

            getSignUpEmailAndNickNameCheck(mEmailEditText.text.toString(), true)
        }

        // Email Duplicate Check Text View
        mEmailDuplicateCheckTextView = mContainer.fragment_signup_email_duplicate_check_text_view

        // Email Duplicate Check Image View
        mEmailDuplicateCheckImageView = mContainer.fragment_signup_email_duplicate_check_image_view


        // Auth Text View
        mAuthTextView = mContainer.fragment_signup_auth_text_view
        mAuthTextView.setOnClickListener {
                mListener.onPhoneAuthScreen(PHONE_AUTH_TYPE_SIGN_UP)
        }

        // Select Nation Spinner
        mSelectNationSpinner = mContainer.fragment_signup_select_nation_spinner
//        mSelectNationSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
//            mAddressCode = newIndex
//            mPostalCodeView.visibility = if (mAddressCode == 2) View.GONE else View.VISIBLE
//        })
        mSelectNationSpinner.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_domestic_korea), iconRes = R.drawable.flag_kr),
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_international), iconRes = R.drawable.ic_default)
                ))
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            selectItemByIndex(0)
            lifecycleOwner = this@SignUpFragment
        }
        mSelectNationSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<IconSpinnerItem?> { oldIndex, oldItem, newIndex, newItem ->
            mAddressCode = newIndex

            if (mAddressCode == 0) {
                mPostalCodeView.visibility = View.VISIBLE
                mAddressIconImageView.visibility = View.GONE
            } else {
                mPostalCodeView.visibility = View.GONE
                mAddressIconImageView.visibility = View.VISIBLE
            }
        })

        // Postal Code View
        mPostalCodeView = mContainer.fragment_signup_postal_code_view

        // Email Error Message Text View
        mEmailErrorMessageTextView = mContainer.fragment_signup_email_error_text_view

        // Postal Code Text View
        mPostalCodeTextView = mContainer.fragment_signup_postal_code_text_view
        mPostalCodeTextView.setOnClickListener {
            mListener.onAddressScreen(true)
        }

        // Post Code Search Text View
        val postCodeSearchTextView = mContainer.fragment_sign_up_post_code_search_text_view
        postCodeSearchTextView.setOnClickListener {
            mListener.onAddressScreen(true)
        }

        // Address Text View
        mAddressTextView = mContainer.fragment_signup_address_text_view
        mAddressTextView.setOnClickListener {
            if (mAddressCode == 0) {
                mListener.onAddressScreen(true)
            } else {
                mListener.onAddressScreen(false)
            }
        }

        // Address Detail Edit Text
        mAddressDetailEditText = mContainer.fragment_signup_address_detail_edit_text

        // Address Icon Image View
        mAddressIconImageView = mContainer.fragment_signup_address_image_view
        mAddressIconImageView.setOnClickListener {
            mListener.onAddressScreen(false)
        }

        // Referral Edit Text
        mReferralEditText = mContainer.fragment_signup_referral_edit_text
        mReferralEditText.filters = arrayOf(Util().blankSpaceFilter)
        mReferralEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // All Check Agree Image View
        mAllAgreeCheckImageView = mContainer.fragment_signup_all_agree_check_image_view
        mAllAgreeCheckImageView.setOnClickListener {
            mIsAllCheckAgree = !mIsAllCheckAgree

            getAgreeCheck(true)
        }

        // Terms Check Image View
        mTermsCheckImageView = mContainer.fragment_signup_terms_agree_check_image_view
        mTermsCheckImageView.setOnClickListener {
            mIsTermsAgree = !mIsTermsAgree
            mTermsCheckImageView.setImageResource(if (mIsTermsAgree) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24)

            getAgreeCheck(false)
        }

        // Terms Show Text View
        val termsShowTextView = mContainer.fragment_signup_terms_show_text_view
        termsShowTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        termsShowTextView.setOnClickListener {
            showHtmlFile(AGREE_TYPE_TERMS)
        }

        // Terms Contents Web View
//        val termsSubContentsWebView = mContainer.expandable_layout_terms_sub_contents_web_view
//        showHtmlFile(termsSubContentsWebView, AGREE_TYPE_TERMS)

        // Policy Check Image View
        mPolicyCheckImageView = mContainer.fragment_signup_policy_agree_check_image_view
        mPolicyCheckImageView.setOnClickListener {
            mIsPolicyAgree = !mIsPolicyAgree
            mPolicyCheckImageView.setImageResource(if (mIsPolicyAgree) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24)

            getAgreeCheck(false)
        }

        // Policy Show Text View
        val policyShowTextView = mContainer.fragment_signup_policy_show_text_view
        policyShowTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        policyShowTextView.setOnClickListener {
            showHtmlFile(AGREE_TYPE_POLICY)
        }

        // Policy Contents Web View
//        val policySubContentsWebView = mContainer.expandable_layout_policy_sub_contents_web_view
//        showHtmlFile(policySubContentsWebView, AGREE_TYPE_POLICY)

        // Third Party Check Image View
        mThirdPartyCheckImageView = mContainer.fragment_signup_third_party_agree_check_image_view
        mThirdPartyCheckImageView.setOnClickListener {
            mIsThirdPartyAgree = !mIsThirdPartyAgree
            mThirdPartyCheckImageView.setImageResource(if (mIsThirdPartyAgree) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24)

            getAgreeCheck(false)
        }

        // Third Party Show Text View
        val thirdPartyShowTextView = mContainer.fragment_signup_third_party_show_text_view
        thirdPartyShowTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        thirdPartyShowTextView.setOnClickListener {
            showHtmlFile(AGREE_TYPE_THIRD_PARTY)
        }

        // Third Party Contents Web View
//        val thirdPartySubContentsWebView = mContainer.expandable_layout_third_party_sub_contents_web_view
//        showHtmlFile(thirdPartySubContentsWebView, AGREE_TYPE_THIRD_PARTY)

        // Location Information Check Image View
        mLocationInformationCheckImageView = mContainer.fragment_signup_location_information_agree_check_image_view
        mLocationInformationCheckImageView.setOnClickListener {
            mIsLocationInformationAgree = !mIsLocationInformationAgree
            mLocationInformationCheckImageView.setImageResource(if (mIsLocationInformationAgree) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24)

            getAgreeCheck(false)
        }

        // Location Information Show Text View
        val locationInformationShowTextView = mContainer.fragment_signup_location_information_show_text_view
        locationInformationShowTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        locationInformationShowTextView.setOnClickListener {
            showHtmlFile(AGREE_TYPE_LOCATION_INFORMATION)
        }

        // Location Information Contents Web View
//        val locationInformationSubContentsWebView = mContainer.expandable_layout_location_information_sub_contents_web_view
//        showHtmlFile(locationInformationSubContentsWebView, AGREE_TYPE_LOCATION_INFORMATION)

        // Event Check Image View
        mEventCheckImageView = mContainer.fragment_signup_event_check_image_view
        mEventCheckImageView.setOnClickListener {
            mIsEventAgree = !mIsEventAgree
            mEventCheckImageView.setImageResource(if (mIsEventAgree) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24)

            getAgreeCheck(false)
        }

        // Service Check Image View
        mServiceCheckImageView = mContainer.fragment_signup_service_check_image_view
        mServiceCheckImageView.setOnClickListener {
            mIsServiceAgree = !mIsServiceAgree
            mServiceCheckImageView.setImageResource(if (mIsServiceAgree) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24)

            getAgreeCheck(false)
        }

        // Success Sign Up Text View
        mSuccessSignUpTextView = mContainer.fragment_signup_success_sign_up_text_view
        mSuccessSignUpTextView.setOnClickListener {
            getCheckSignUp()
        }

        if (getSnsUserData().getLoginType() != LOGIN_TYPE_EMAIL) {
            setSnsUserData()
        }
    }

    private fun setSnsUserData() {
        if (!TextUtils.isEmpty(getSnsUserData().getUserName())) {
            mNameEditText.setText(getSnsUserData().getUserName())
        }

        if (!TextUtils.isEmpty(getSnsUserData().getUserEmail())) {
            mEmailEditText.setText(getSnsUserData().getUserEmail())
        }
    }

    private fun showHtmlFile(view: WebView, type: Int) {

        var webUrlLocal = ""
        when (type) {
            AGREE_TYPE_TERMS -> {
                try {
//                    webUrlLocal = "file:///android_asset/terms_" + SharedPreferencesUtil().getSelectLanguage(mActivity) + ".html"
                    webUrlLocal = "file:///android_asset/terms.html"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            AGREE_TYPE_POLICY -> {
                try {
//                    webUrlLocal = "file:///android_asset/policy_" + SharedPreferencesUtil().getSelectLanguage(mActivity) + ".html"
                    webUrlLocal = "file:///android_asset/policy.html"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            AGREE_TYPE_THIRD_PARTY -> {
                try {
//                    webUrlLocal = "file:///android_asset/third_party_" + SharedPreferencesUtil().getSelectLanguage(mActivity) + ".html"
                    webUrlLocal = "file:///android_asset/third_parties.html"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            AGREE_TYPE_LOCATION_INFORMATION -> {
                try {
//                    webUrlLocal = "file:///android_asset/location_information_" + SharedPreferencesUtil().getSelectLanguage(mActivity) + ".html"
                    webUrlLocal = "file:///android_asset/location.html"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        view.loadUrl(webUrlLocal)
    }

    private fun showHtmlFile(type: Int) {

        var webUrlLocal = ""
        when (type) {
            AGREE_TYPE_TERMS -> {
                webUrlLocal = "file:///android_asset/terms.html"
            }

            AGREE_TYPE_POLICY -> {
                webUrlLocal = "file:///android_asset/policy.html"
            }

            AGREE_TYPE_THIRD_PARTY -> {
                webUrlLocal = "file:///android_asset/third_parties.html"
            }

            AGREE_TYPE_LOCATION_INFORMATION -> {
                webUrlLocal = "file:///android_asset/location.html"
            }
        }

        mListener.onTermsWebViewScreen(type, webUrlLocal)
    }

    private fun getAgreeCheck(isAllCheck: Boolean) {
        if (isAllCheck) {
            if (mIsAllCheckAgree) {
                mTermsCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_24)
                mPolicyCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_24)
                mThirdPartyCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_24)
                mLocationInformationCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_24)
                mEventCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_24)
                mServiceCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_24)
            } else {
                mTermsCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                mPolicyCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                mThirdPartyCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                mLocationInformationCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                mEventCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                mServiceCheckImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
            }

            mIsTermsAgree = mIsAllCheckAgree
            mIsPolicyAgree = mIsAllCheckAgree
            mIsThirdPartyAgree = mIsAllCheckAgree
            mIsLocationInformationAgree = mIsAllCheckAgree
            mIsEventAgree = mIsAllCheckAgree
            mIsServiceAgree = mIsAllCheckAgree
        } else {
            mIsAllCheckAgree = !(!mIsTermsAgree || !mIsPolicyAgree || !mIsThirdPartyAgree || !mIsLocationInformationAgree || !mIsEventAgree || !mIsServiceAgree)
        }

        mIsSuccessAgree = mIsTermsAgree && mIsPolicyAgree && mIsThirdPartyAgree && mIsLocationInformationAgree

        mAllAgreeCheckImageView.setImageResource(if (mIsAllCheckAgree) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24)

//        NagajaLog().d("wooks, mIsSuccessAgree = $mIsSuccessAgree")
    }

    private fun getCheckSignUp() {
        var isSuccess = true

        // Name Check
        if (TextUtils.isEmpty(mNameEditText.text)) {

            mNameErrorMessageTextView.visibility = View.VISIBLE
            mNameErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_name)

            mNameEditText.post(Runnable {
                mNameEditText.isFocusableInTouchMode = true
                mNameEditText.requestFocus()
                val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(mNameEditText, 0)
            })
            return
        } else {
            mNameErrorMessageTextView.visibility = View.INVISIBLE
        }

        // Email Check
        if (TextUtils.isEmpty(mEmailEditText.text) || !Util().checkEmail(mEmailEditText.text.toString())) {
            mEmailErrorMessageTextView.visibility = View.VISIBLE
            mEmailErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_valid_email)

            mEmailEditText.post(Runnable {
                mEmailEditText.isFocusableInTouchMode = true
                mEmailEditText.requestFocus()
                val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(mEmailEditText, 0)
            })
            return
        } else {
            mEmailErrorMessageTextView.visibility = View.INVISIBLE
        }

        if (!mIsSnsSignUp) {

            // Password Check
            if (TextUtils.isEmpty(mPasswordEditText.text) || !Util().passwordRegularExpressionCheck(mPasswordEditText.text.toString())) {
                mPasswordErrorMessageTextView.visibility = View.VISIBLE
                if (TextUtils.isEmpty(mPasswordEditText.text)) {
                    mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_password)
                } else if (!Util().passwordRegularExpressionCheck(mPasswordEditText.text.toString())) {
                    mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_input_password_message)
                }

                mPasswordEditText.post(Runnable {
                    mPasswordEditText.isFocusableInTouchMode = true
                    mPasswordEditText.requestFocus()
                    val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.showSoftInput(mPasswordEditText, 0)
                })
                return
            } else {
                mPasswordErrorMessageTextView.visibility = View.INVISIBLE
            }

            // Password Confirm Check
            if (mPasswordEditText.text.toString() != mPasswordConfirmEditText.text.toString()) {
                mPasswordConfirmErrorMessageTextView.visibility = View.VISIBLE
                mPasswordConfirmErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_not_match_password)

                mPasswordConfirmEditText.post(Runnable {
                    mPasswordConfirmEditText.isFocusableInTouchMode = true
                    mPasswordConfirmEditText.requestFocus()
                    val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.showSoftInput(mPasswordConfirmEditText, 0)
                })
                return
            }

        }


        // Nick Name Check
        if (TextUtils.isEmpty(mNickNameEditText.text) || (mNickNameEditText.text.length < 2)) {
            mNickNameErrorMessageTextView.visibility = View.VISIBLE
            mNickNameErrorMessageTextView.text = mActivity.resources.getString(R.string.text_input_nick_name_message)

            mNickNameEditText.post(Runnable {
                mNickNameEditText.isFocusableInTouchMode = true
                mNickNameEditText.requestFocus()
                val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(mNickNameEditText, 0)
            })
            return
        } else {
            mNickNameErrorMessageTextView.visibility = View.INVISIBLE
        }

        // Phone Number Auth
        if (!mIsPhoneNumberAuth) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_phone_number_auth))
            return
        }

        // Address Check
        if (TextUtils.isEmpty(mAddressTextView.text)) {
            mAddressDetailEditText.post(Runnable {
                mAddressDetailEditText.isFocusableInTouchMode = true
                mAddressDetailEditText.requestFocus()
                val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(mAddressDetailEditText, 0)
            })
            return
        }

        // Agree Check
        if (!mIsSuccessAgree) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_terms_agree))
            return
        }

        // Email Duplicate Check
        if (!mIsEmailCheck) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_email_duplicate_check))
            mScrollView.scrollTo(0, mEmailDuplicateCheckView.top)
            return
        }

        // Nick Name Duplicate Check
        if (!mIsNickNameCheck) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_nick_name_duplicate_check))
            mScrollView.scrollTo(0, mNickNameDuplicateCheckView.bottom)
            return
        }

        getMemberSignUp()
    }

    private fun getCheckPassword(): Boolean {
        // Password Regex Check
        if (TextUtils.isEmpty(mPasswordEditText.text) || mPasswordEditText.text.length < 8) {
            mPasswordErrorMessageTextView.visibility = View.VISIBLE
            mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_least_8_characters)

        } else {
            mPasswordErrorMessageTextView.visibility = View.INVISIBLE
        }

        if (TextUtils.isEmpty(mPasswordConfirmEditText.text) || mPasswordConfirmEditText.text.length < 8) {
            mPasswordConfirmErrorMessageTextView.visibility = View.VISIBLE
            mPasswordConfirmErrorMessageTextView.text = mActivity.resources.getString(R.string.text_error_input_least_8_characters)

        } else {
            mPasswordConfirmErrorMessageTextView.visibility = View.INVISIBLE
        }

        if (!TextUtils.isEmpty(mPasswordEditText.text) && !TextUtils.isEmpty(mPasswordConfirmEditText.text)) {
            val passwordPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$"

            val passwordRegex: Boolean = Pattern.matches(passwordPattern, mPasswordEditText.text.toString())
            val passwordConfirmRegex: Boolean = Pattern.matches(passwordPattern, mPasswordConfirmEditText.text.toString())

            if (passwordRegex) {
                mPasswordErrorMessageTextView.visibility = View.VISIBLE
                mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_input_password_message)

                return false
            } else {
                mPasswordErrorMessageTextView.visibility = View.INVISIBLE
            }

            if (passwordConfirmRegex) {
                mPasswordConfirmErrorMessageTextView.visibility = View.VISIBLE
                mPasswordConfirmErrorMessageTextView.text = mActivity.resources.getString(R.string.text_input_password_message)

                return false
            } else {
                mPasswordConfirmErrorMessageTextView.visibility = View.INVISIBLE
            }

            if (mPasswordEditText.text.equals(mPasswordConfirmEditText.text)) {
                return true
            }
        }

        return false
    }

    @Throws(IOException::class)
    private fun readText(file: String): String? {
        val inputStream: InputStream = mActivity.assets.open(file)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }

    private fun geoCoding(address: String, isDomestic: Boolean): Location {
        return try {
            Geocoder(mActivity, if (isDomestic) Locale.KOREA else Locale.US).getFromLocationName(address, 1)?.let{
                Location("").apply {
                    latitude =  it[0].latitude
                    longitude = it[0].longitude
                }
            }?: Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        } catch (e:Exception) {
            e.printStackTrace()
            geoCoding(address, isDomestic)
        }
    }

    private fun showDuplicateCustomDialog(isEmail: Boolean) {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_noti),
            if (isEmail) mActivity.resources.getString(R.string.text_error_registered_email) else mActivity.resources.getString(R.string.text_error_registered_nick_name),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()

                if (isEmail) {
                    mIsEmailCheck = false
                } else {
                    mIsNickNameCheck = false
                }
            }
        })
        customDialog.show()

    }

    fun phoneAuthSuccess(phoneAuthData: PhoneAuthData) {

        mPhoneAuthData = phoneAuthData
        NagajaLog().e("wooks, !!! = ${mPhoneAuthData.getSecureCode()}")

        mAuthTextView.setBackgroundResource(R.drawable.bg_box_line)
        mAuthTextView.text = mActivity.resources.getString(R.string.text_success_phone_auth)
        mAuthTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_000000))

        mAuthTextView.isEnabled = false
        mIsPhoneNumberAuth = true
    }

    @SuppressLint("SetTextI18n")
    fun getAddress(addressData: AddressData) {

        NagajaLog().d("wooks, addressData = ${addressData.getAddress()}")

        if (!TextUtils.isEmpty(addressData.getPostalCode())) {
            mPostalCodeTextView.text = addressData.getPostalCode()
        }

        if (!TextUtils.isEmpty(addressData.getAddress())) {
            mAddressTextView.text = addressData.getAddress() + " "+ addressData.getExtAddress()
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
        if (context is OnSignUpFragmentListener) {
            mActivity = context as Activity

            if (context is OnSignUpFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnSignUpFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnSignUpFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnSignUpFragmentListener"
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
     * API. getSecureKeyLogin
     */
    private fun getMemberSignUp() {

        val latitude = geoCoding(mAddressTextView.text.toString(), mAddressCode == 0).latitude
        val longitude = geoCoding(mAddressTextView.text.toString(), mAddressCode == 0).longitude

        var socialUid = 1
        when (getSnsUserData().getLoginType()) {
            LOGIN_TYPE_EMAIL -> {
                socialUid = 1
            }

            LOGIN_TYPE_KAKAO -> {
                socialUid = 2
            }

            LOGIN_TYPE_NAVER -> {
                socialUid = 3
            }

            LOGIN_TYPE_GOOGLE -> {
                socialUid = 4
            }

            LOGIN_TYPE_APPLE -> {
                socialUid = 5
            }

            LOGIN_TYPE_FACEBOOK -> {
                socialUid = 6
            }
        }


        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMemberSignUp(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    getSignUpSendEmail()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_CODE_WRONG_FORMAT_PASSWORD) {
                        mPasswordErrorMessageTextView.visibility = View.VISIBLE
                        mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_input_password_message)

                        mPasswordEditText.post(Runnable {
                            mPasswordEditText.isFocusableInTouchMode = true
                            mPasswordEditText.requestFocus()
                            val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                            imm!!.showSoftInput(mPasswordEditText, 0)
                        })
                    } else if (errorCode == ErrorRequest.ERROR_CODE_NO_RECOMMEND) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_no_recommend))
                    }
                }
            },
            mNameEditText.text.toString(),
            mEmailEditText.text.toString(),
            mNickNameEditText.text.toString(),
            mPhoneAuthData.getPhoneNationCode(),
            mPhoneAuthData.getPhoneNumber(),
            SharedPreferencesUtil().getNationPhoneCode(mActivity)!!,
            mReferralEditText.text.toString(),
            if (!TextUtils.isEmpty(latitude.toString())) latitude.toString() else "",
            if (!TextUtils.isEmpty(longitude.toString())) longitude.toString() else "",
            if (!TextUtils.isEmpty(mPostalCodeTextView.text.toString())) mPostalCodeTextView.text.toString() else "",
            mAddressTextView.text.toString(),
            if (!TextUtils.isEmpty(mAddressDetailEditText.text.toString())) mAddressDetailEditText.text.toString() else "",
            mPhoneAuthData.getConfirmUid().toString(),
            mPhoneAuthData.getSecureCode(),
            socialUid.toString(),
            getSnsUserData().getMemberSocialUid().toString(),
            mIsEventAgree,
            mIsServiceAgree,
            mIsSnsSignUp,
            mPasswordEditText.text.toString(),
            if (mAddressCode == 0) "82" else "63"
        )
    }

    /**
     * API. Get Sign Up Send Email
     */
    private fun getSignUpSendEmail() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSignUpSendEmail(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mListener.onSuccessSignUp()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            mEmailEditText.text.toString(),
        )
    }

    /**
     * API. Get Sign Up Email And Nick Name Check
     */
    private fun getSignUpEmailAndNickNameCheck(checkString: String, isEmail: Boolean) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSignUpEmailAndNickNameCheck(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    if (isEmail) {
                        mEmailDuplicateCheckTextView.visibility = View.INVISIBLE
                        mEmailDuplicateCheckImageView.visibility = View.VISIBLE
                        mIsEmailCheck = true
                    } else {
                        mNickNameDuplicateCheckTextView.visibility = View.INVISIBLE
                        mNickNameDuplicateCheckImageView.visibility = View.VISIBLE
                        mIsNickNameCheck = true
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    when (errorCode) {
                        ERROR_CODE_EXISTING_ID,
                        ERROR_CODE_EXISTING_NICK_NAME -> {
                            showDuplicateCustomDialog(isEmail)
                        }
                    }
                }
            },
            checkString,
            isEmail
        )
    }
}