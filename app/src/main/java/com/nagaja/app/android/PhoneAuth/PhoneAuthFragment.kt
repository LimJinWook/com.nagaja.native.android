package com.nagaja.app.android.PhoneAuth

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.PhoneAuthData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXISTING_PHONE_NUMBER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_EXPIRED_CODE
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_CONFIRM_EMAIL
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_FOUND_USER
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_WRONG_CODE
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_EXPIRED_TOKEN
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import ir.samanjafari.easycountdowntimer.CountDownInterface
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview
import kotlinx.android.synthetic.main.fragment_phone_auth.view.*
import kotlinx.android.synthetic.main.layout_title.view.*


class PhoneAuthFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mAuthConfirmView: View
    private lateinit var mPhoneNumberInputView: View

    private lateinit var mPhoneNumberEditText: EditText
    private lateinit var mAuthCodeEditText: EditText

    private lateinit var mAuthNumberSendTextView: TextView
    private lateinit var mPhoneAuthConfirmTextView: TextView

    private lateinit var mTimerTextView: EasyCountDownTextview

    private lateinit var mPhoneNationCodeSpinner: PowerSpinnerView

    private lateinit var mListener: OnPhoneAuthFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mPhoneNationCode = "+82"

    private var mIsTimerFinish = false

    private lateinit var mPhoneAuthData: PhoneAuthData

    interface OnPhoneAuthFragmentListener {
        fun onFinish()
        fun onConfirm(value: String)
        fun onSignUpConfirm(data: PhoneAuthData)
        fun onSuccessFindID(findID: String)
        fun onSuccessFindPassword(data: PhoneAuthData)
        fun onSuccessWithdrawal()
    }

    companion object {
        const val ARGS_PHONE_NUMBER_AUTH_TYPE = "args_phone_number_auth_type"

        fun newInstance(phoneNumberAuthType: Int): PhoneAuthFragment {
            val args = Bundle()
            val fragment = PhoneAuthFragment()
            args.putInt(ARGS_PHONE_NUMBER_AUTH_TYPE, phoneNumberAuthType)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getPhoneNumberAuthType(): Int? {
        val arguments = arguments
        return arguments?.getInt(ARGS_PHONE_NUMBER_AUTH_TYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_phone_auth, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_phone_auth)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Phone Number Input View
        mPhoneNumberInputView = mContainer.fragment_phone_auth_phone_number_input_view

        // Phone Nation Code Spinner
        mPhoneNationCodeSpinner = mContainer.fragment_phone_auth_nation_code_spinner
        mPhoneNationCodeSpinner.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_nation_phone_code_korea), iconRes = R.drawable.flag_korean),
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_nation_phone_code_philippine), iconRes = R.drawable.flag_filipino),
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_nation_phone_code_america), iconRes = R.drawable.flag_english),
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_nation_phone_code_china), iconRes = R.drawable.flag_chinese),
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_nation_phone_code_japan), iconRes = R.drawable.flag_japanese)))
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            selectItemByIndex(0)
            lifecycleOwner = this@PhoneAuthFragment
        }
//        mPhoneNationCodeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
//            mPhoneNationCode = newItem
//            NagajaLog().d("wooks, mPhoneNationCode = $mPhoneNationCode")
//        })

        mPhoneNationCodeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<IconSpinnerItem?> { oldIndex, oldItem, newIndex, newItem ->
            mPhoneNationCode = newItem!!.text.toString()
            NagajaLog().d("wooks, mPhoneNationCode = $mPhoneNationCode")
        })

        // Phone Number Edit Text
        mPhoneNumberEditText = mContainer.fragment_phone_auth_phone_number_edit_text
        mPhoneNumberEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(20))
        mPhoneNumberEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        mPhoneNumberEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mPhoneNumberEditText.length() > 9) {
                    changeSendButton(true, isAuthCode = false)
                } else {
                    changeSendButton(false, isAuthCode = false)
                }
            }
        })

        // Auth Number Send Text View
        mAuthNumberSendTextView = mContainer.fragment_phone_auth_number_send_text_view
        mAuthNumberSendTextView.isEnabled = false
        mAuthNumberSendTextView.setOnClickListener {

            hideKeyboard(mActivity)

            val nationPhoneCode = mPhoneNationCode.replace("+", "")
            val phone = mPhoneNumberEditText.text.toString().replace("-", "")

            if (getPhoneNumberAuthType() == PHONE_AUTH_TYPE_SIGN_UP || getPhoneNumberAuthType() == PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER) {
                getRequestPhoneAuthCode(nationPhoneCode, phone)
            } else if (getPhoneNumberAuthType() == PHONE_AUTH_TYPE_FIND_ID || getPhoneNumberAuthType() == PHONE_AUTH_TYPE_FIND_PASSWORD || getPhoneNumberAuthType() == PHONE_AUTH_TYPE_MEMBER_WITHDRAWAL) {
                getRequestMemberPhoneAuthCode(nationPhoneCode, phone)
            }
        }

        // Auth Confirm View
        mAuthConfirmView = mContainer.fragment_phone_auth_confirm_view

        // Auth Code Edit Text
        mAuthCodeEditText = mContainer.fragment_phone_auth_code_edit_text
        mAuthCodeEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(6))
        mAuthCodeEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mAuthCodeEditText.length() > 5) {
                    changeSendButton(true, isAuthCode = true)
                } else {
                    changeSendButton(false, isAuthCode = true)
                }
            }
        })

        // Timer Text View
        mTimerTextView = mContainer.fragment_phone_auth_timer_text_view
        mTimerTextView.setOnTick(object : CountDownInterface {
            override fun onTick(time: Long) {
            }

            override fun onFinish() {
                showInputTimeOutDialog()
                cancelCountTimer()
            }

        })

        // Phone Auth Confirm Text View
        mPhoneAuthConfirmTextView = mContainer.fragment_phone_auth_confirm_text_view
        mPhoneAuthConfirmTextView.isEnabled = false
        mPhoneAuthConfirmTextView.setOnClickListener {
            if (mIsTimerFinish) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_auth_time_out))
                return@setOnClickListener
            }

            val nationPhoneCode = mPhoneNationCode.replace("+", "")
            val phone = mPhoneNumberEditText.text.toString().replace("-", "")

//            if (getPhoneNumberAuthType() == PHONE_AUTH_TYPE_SIGN_UP || getPhoneNumberAuthType() == PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER) {
//                getRequestPhoneAuthCheckCode(nationPhoneCode, phone, mAuthCodeEditText.text.toString())
//            } else {
////                mListener.onConfirm()
//            }

            if (getPhoneNumberAuthType() == PHONE_AUTH_TYPE_SIGN_UP || getPhoneNumberAuthType() == PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER) {
                getRequestPhoneAuthCheckCode(nationPhoneCode, phone, mAuthCodeEditText.text.toString())
            } else if (getPhoneNumberAuthType() == PHONE_AUTH_TYPE_FIND_ID || getPhoneNumberAuthType() == PHONE_AUTH_TYPE_FIND_PASSWORD || getPhoneNumberAuthType() == PHONE_AUTH_TYPE_MEMBER_WITHDRAWAL) {
                getRequestMemberPhoneAuthCheckCode(nationPhoneCode, phone, mAuthCodeEditText.text.toString())
            }

        }
    }

    private fun changeSendButton(isSuccess: Boolean, isAuthCode: Boolean) {
        if (isSuccess) {
            if (isAuthCode) {
                mPhoneAuthConfirmTextView.isEnabled = true
                mPhoneAuthConfirmTextView.setBackgroundResource(R.drawable.bg_box_line)
                mPhoneAuthConfirmTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_222222))
            } else {
                mAuthNumberSendTextView.isEnabled = true
                mAuthNumberSendTextView.setBackgroundResource(R.drawable.bg_box_line)
                mAuthNumberSendTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_222222))
            }
        } else {
            if (isAuthCode) {
                mPhoneAuthConfirmTextView.isEnabled = false
                mPhoneAuthConfirmTextView.setBackgroundResource(R.drawable.bg_disable)
                mPhoneAuthConfirmTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_26000000))
            } else {
                mAuthNumberSendTextView.isEnabled = false
                mAuthNumberSendTextView.setBackgroundResource(R.drawable.bg_disable)
                mAuthNumberSendTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_26000000))
            }
        }
    }

    private fun showSuccessSignUpTypePopup(changePhoneNumber: String) {

        var title = mActivity.resources.getString(R.string.text_title_success_phone_auth)
        var message = mActivity.resources.getString(R.string.text_message_success_phone_auth)

        if (getPhoneNumberAuthType() == PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER) {
            title = mActivity.resources.getString(R.string.text_title_phone_number_change)
            message = mActivity.resources.getString(R.string.text_message_success_phone_number_change)
        }

        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            title,
            message,
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()

                if (getPhoneNumberAuthType() == PHONE_AUTH_TYPE_SIGN_UP) {
                    mListener.onSignUpConfirm(mPhoneAuthData)
                } else {
                    mListener.onConfirm(changePhoneNumber)
                }
            }
        })
        customDialog.show()
    }

    private fun showInputTimeOutDialog() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_noti),
            getString(R.string.text_message_time_out_phone_auth_number),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    private fun startCountTimer() {
        mIsTimerFinish = false
        mTimerTextView.setTime(0, 0, 3, 1)
        mTimerTextView.startTimer()
    }

    private fun cancelCountTimer()  {
        mTimerTextView.stopTimer()
        mIsTimerFinish = true
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelCountTimer()
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
        if (context is OnPhoneAuthFragmentListener) {
            mActivity = context as Activity

            if (context is OnPhoneAuthFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnPhoneAuthFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnPhoneAuthFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnPhoneAuthFragmentListener"
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
     * API. Get Request Phone Auth Code
     */
    private fun getRequestPhoneAuthCode(nationPhoneCode: String, phoneNumber: String) {
        if (TextUtils.isEmpty(nationPhoneCode) || TextUtils.isEmpty(phoneNumber)) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRequestPhoneAuthCode(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mAuthCodeEditText.clearFocus()
                    mPhoneNumberInputView.requestFocus()
                    hideKeyboard(mActivity)

                    mAuthConfirmView.visibility = View.VISIBLE
                    mAuthNumberSendTextView.text = mActivity.resources.getText(R.string.text_resend_auth_number)

                    cancelCountTimer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        startCountTimer()
                    }, 300)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }

                    if (errorCode == ERROR_CODE_EXISTING_PHONE_NUMBER) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_existing_phone_number))
                    }
                }
            },
            nationPhoneCode,
            phoneNumber
        )
    }

    /**
     * API. Get Request Phone Auth Check Code
     */
    private fun getRequestPhoneAuthCheckCode(nationPhoneCode: String, phoneNumber: String, code: String) {
        if (TextUtils.isEmpty(nationPhoneCode) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(code)) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRequestPhoneAuthCheckCode(
            mRequestQueue,
            object : NagajaManager.RequestListener<PhoneAuthData> {
                override fun onSuccess(resultData: PhoneAuthData) {

                    when (getPhoneNumberAuthType()) {
                        PHONE_AUTH_TYPE_SIGN_UP -> {
                            mPhoneAuthData = resultData
                            mPhoneAuthData.setSecureCode(mAuthCodeEditText.text.toString())

                            showSuccessSignUpTypePopup("")
                            cancelCountTimer()
                        }

                        PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER -> {
                            getPhoneNumberChange(nationPhoneCode, phoneNumber, code)
                        }
                    }

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }

                    if (errorCode == ERROR_CODE_WRONG_CODE) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_wrong_auth_code))
                    } else if (errorCode == ERROR_CODE_EXPIRED_CODE) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_expired_auth_code))
                    } else if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_not_confirm_email_auth))
                    }
                }
            },
            nationPhoneCode,
            phoneNumber,
            code
        )
    }

    /**
     * API. Get Request Member Phone Auth Code
     */
    private fun getRequestMemberPhoneAuthCode(nationPhoneCode: String, phoneNumber: String) {
        if (TextUtils.isEmpty(nationPhoneCode) || TextUtils.isEmpty(phoneNumber)) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRequestMemberPhoneAuthCode(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mAuthCodeEditText.clearFocus()
                    mPhoneNumberInputView.requestFocus()
                    hideKeyboard(mActivity)

                    mAuthConfirmView.visibility = View.VISIBLE
                    mAuthNumberSendTextView.text = mActivity.resources.getText(R.string.text_resend_auth_number)

                    cancelCountTimer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        startCountTimer()
                    }, 300)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    } else if (errorCode == ERROR_CODE_NOT_FOUND_USER) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_no_register_phone_number))
                    }
                }
            },
            nationPhoneCode,
            phoneNumber
        )
    }

    /**
     * API. Get Request Member Phone Auth Check Code
     */
    private fun getRequestMemberPhoneAuthCheckCode(nationPhoneCode: String, phoneNumber: String, code: String) {
        if (TextUtils.isEmpty(nationPhoneCode) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(code)) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getRequestMemberPhoneAuthCheckCode(
            mRequestQueue,
            object : NagajaManager.RequestListener<PhoneAuthData> {
                override fun onSuccess(resultData: PhoneAuthData) {

                    cancelCountTimer()

                    when (getPhoneNumberAuthType()) {
                        PHONE_AUTH_TYPE_FIND_ID -> {
                            if (!TextUtils.isEmpty(resultData.getMemberId())) {
                                mListener.onSuccessFindID(resultData.getMemberId())

                                getFindEmailSuccessDisconnect(nationPhoneCode, phoneNumber, code)
                            }
                        }

                        PHONE_AUTH_TYPE_FIND_PASSWORD -> {
                            resultData.setPhoneNationCode(nationPhoneCode)
                            resultData.setPhoneNumber(phoneNumber)
                            resultData.setSecureCode(code)
                            mListener.onSuccessFindPassword(resultData)
                        }

                        PHONE_AUTH_TYPE_MEMBER_WITHDRAWAL -> {
                            getMemberWithdrawal(nationPhoneCode, phoneNumber, code)
                        }
                    }

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }

                    if (errorCode == ERROR_CODE_WRONG_CODE) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_wrong_auth_code))
                    } else if (errorCode == ERROR_CODE_EXPIRED_CODE) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_expired_auth_code))
                    } else if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_not_confirm_email_auth))
                    }
                }
            },
            nationPhoneCode,
            phoneNumber,
            code
        )
    }

    /**
     * API. Get Find Email Success Disconnect
     */
    private fun getFindEmailSuccessDisconnect(nationPhoneCode: String, phoneNumber: String, code: String) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getFindEmailSuccessDisconnect(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            nationPhoneCode,
            phoneNumber,
            code
        )
    }

    /**
     * API. Get Phone Number Change
     */
    private fun getPhoneNumberChange(nationPhoneCode: String, changePhoneNumber: String, code: String) {
        if (TextUtils.isEmpty(nationPhoneCode) || TextUtils.isEmpty(changePhoneNumber) || TextUtils.isEmpty(code)) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getPhoneNumberChange(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    showSuccessSignUpTypePopup(changePhoneNumber)

                    cancelCountTimer()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }

                    if (errorCode == ERROR_CODE_WRONG_CODE) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_wrong_auth_code))
                    } else if (errorCode == ERROR_CODE_EXPIRED_CODE) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_expired_auth_code))
                    } else if (errorCode == ERROR_CODE_NOT_CONFIRM_EMAIL) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_not_confirm_email_auth))
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            nationPhoneCode,
            changePhoneNumber,
            code
        )
    }

    /**
     * API. Get Member Withdrawal
     */
    private fun getMemberWithdrawal(nationPhoneCode: String, phoneNumber: String, authCode: String) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMemberWithdrawal(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    getMemberWithdrawalSendEmail()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            nationPhoneCode,
            phoneNumber,
            authCode
        )
    }

    /**
     * API. Get Member Withdrawal
     */
    private fun getMemberWithdrawalSendEmail() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMemberWithdrawalSendEmail(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    val customDialog = CustomDialog(
                        mActivity,
                        DIALOG_TYPE_NO_CANCEL,
                        mActivity.resources.getString(R.string.text_member_withdrawal),
                        mActivity.resources.getString(R.string.text_message_withdrawal_send_email),
                        "",
                        mActivity.resources.getString(R.string.text_confirm)
                    )

                    customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
                        override fun onCancel() {
                        }

                        override fun onConfirm() {
                            customDialog.dismiss()
                            mListener.onSuccessWithdrawal()
                        }
                    })
                    customDialog.show()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            MAPP.USER_DATA.getEmailId()
        )
    }
}