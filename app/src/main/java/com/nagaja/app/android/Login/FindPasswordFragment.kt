package com.nagaja.app.android.Login

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.PhoneAuthData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_find_id.view.*
import kotlinx.android.synthetic.main.fragment_find_password.view.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_select_language.view.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*

class FindPasswordFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mInputPasswordView: View
    private lateinit var mEmailView: View

    private lateinit var mEmailEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mPasswordConfirmEditText: EditText

    private lateinit var mPhoneAuthTextView: TextView
    private lateinit var mEmailTextView: TextView
    private lateinit var mPasswordErrorMessageTextView: TextView
    private lateinit var mPasswordConfirmErrorMessageTextView: TextView

    private var mIsFindPasswordSuccess = false

    private lateinit var mListener: OnFindPasswordFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mPhoneAuthData: PhoneAuthData

    interface OnFindPasswordFragmentListener {
        fun onBack()
        fun onPhoneAuthScreen(phoneAuthType: Int)
    }

    companion object {
        fun newInstance(): FindPasswordFragment {
            val args = Bundle()
            val fragment = FindPasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_find_password, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.setOnClickListener {
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_find_password)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Email View
        mEmailView = mContainer.fragment_find_password_email_view

        // Email Edit Text
        mEmailEditText = mContainer.fragment_find_password_input_email_edit_text
        mEmailEditText.filters = arrayOf(Util().blankSpaceFilter)
        mEmailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Email Text View
        mEmailTextView = mContainer.fragment_find_password_email_text_view

        // Input Password View
        mInputPasswordView = mContainer.fragment_find_password_input_password_view

        // Phone Number Text View
        mPhoneAuthTextView = mContainer.fragment_find_password_phone_auth_text_view
        mPhoneAuthTextView.setOnClickListener {
//            if (!mIsFindPasswordSuccess) {
//                mListener.onPhoneAuthScreen(PHONE_AUTH_TYPE_FIND_PASSWORD)
//            } else {
//                // TODO: Implement Change Password API.
//                showToast(mActivity, "// TODO: Implement Change Password API.")
//                val customDialog = CustomDialog(
//                    mActivity,
//                    DIALOG_TYPE_NO_CANCEL,
//                    mActivity.resources.getString(R.string.text_title_success_change_password),
//                    mActivity.resources.getString(R.string.text_message_success_change_password),
//                    "",
//                    mActivity.resources.getString(R.string.text_confirm)
//                )
//
//                customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
//                    override fun onCancel() {
//                    }
//
//                    override fun onConfirm() {
//                        customDialog.dismiss()
//                        mListener.onBack()
//                    }
//                })
//                customDialog.show()
//            }

            if (!mIsFindPasswordSuccess) {
                if (TextUtils.isEmpty(mEmailEditText.text)) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_email))
                    return@setOnClickListener
                } else if (!Util().checkEmail(mEmailEditText.text.toString())) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_valid_email))
                    return@setOnClickListener
                }

                getMemberFindPasswordEmailCheck()
            } else {
                checkPassword()
            }
        }

        // Password Edit Text
        mPasswordEditText = mContainer.fragment_find_password_edit_text
        mPasswordEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(15))
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

        // Password Error Message Text View
        mPasswordErrorMessageTextView = mContainer.fragment_find_password_error_text_view

        // Password Confirm Edit Text
        mPasswordConfirmEditText = mContainer.fragment_find_password_confirm_edit_text
        mPasswordConfirmEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(15))
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

        // Password Confirm Error Message Text View
        mPasswordConfirmErrorMessageTextView = mContainer.fragment_find_password_confirm_error_text_view
    }

    private fun checkPassword() {
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

        getMemberChangePassword()

    }

    fun phoneAuthSuccess(phoneAuthData: PhoneAuthData) {
        mEmailView.visibility = View.GONE
        mInputPasswordView.visibility = View.VISIBLE

        mPhoneAuthTextView.text = mActivity.resources.getString(R.string.text_change_password)
        mIsFindPasswordSuccess = true

        if (!TextUtils.isEmpty(phoneAuthData.getMemberId())) {
            mEmailTextView.text = phoneAuthData.getMemberId()
        }

        mPhoneAuthData = phoneAuthData

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
        if (context is OnFindPasswordFragmentListener) {
            mActivity = context as Activity

            if (context is OnFindPasswordFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnFindPasswordFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnFindPasswordFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFindPasswordFragmentListener"
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
    private fun getMemberFindPasswordEmailCheck() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMemberFindPasswordEmailCheck(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mListener.onPhoneAuthScreen(PHONE_AUTH_TYPE_FIND_PASSWORD)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_CODE_NOT_FOUND_ID) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_not_found_email))
                    } else if (errorCode == ErrorRequest.ERROR_CODE_DIFFERENT_TYPE_SIGN_UP) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_social_member_register))
                    }
                }
            },
            mEmailEditText.text.toString()
        )
    }

    /**
     * API. Get Request Phone Auth Code
     */
    private fun getMemberChangePassword() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMemberChangePassword(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    val customDialog = CustomDialog(
                        mActivity,
                        DIALOG_TYPE_NO_CANCEL,
                        mActivity.resources.getString(R.string.text_change_password),
                        mActivity.resources.getString(R.string.text_message_success_change_password),
                        "",
                        mActivity.resources.getString(R.string.text_confirm)
                    )

                    customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
                        override fun onCancel() {
                        }

                        override fun onConfirm() {
                            customDialog.dismiss()
                            mListener.onBack()
                        }
                    })
                    customDialog.show()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_CODE_DO_NOT_MATCH_ID) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_do_not_match_id))
                    } else if (errorCode == ErrorRequest.ERROR_CODE_WRONG_FORMAT_PASSWORD) {
                        mPasswordErrorMessageTextView.visibility = View.VISIBLE
                        mPasswordErrorMessageTextView.text = mActivity.resources.getString(R.string.text_input_password_message)

                        mPasswordEditText.post(Runnable {
                            mPasswordEditText.isFocusableInTouchMode = true
                            mPasswordEditText.requestFocus()
                            val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                            imm!!.showSoftInput(mPasswordEditText, 0)
                        })
                    } else if (errorCode == ErrorRequest.ERROR_CODE_OLD_PASSWORD) {
                        showToast(mActivity, mActivity.resources.getString(R.string.text_error_message_old_password))

                        mPasswordEditText.post(Runnable {
                            mPasswordEditText.isFocusableInTouchMode = true
                            mPasswordEditText.requestFocus()
                            val imm: InputMethodManager? = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                            imm!!.showSoftInput(mPasswordEditText, 0)
                        })
                    }
                }
            },
            if (!TextUtils.isEmpty(mPhoneAuthData.getMemberId())) mPhoneAuthData.getMemberId() else "",
            if (!TextUtils.isEmpty(mPhoneAuthData.getSecureCode())) mPhoneAuthData.getSecureCode() else "",
            if (!TextUtils.isEmpty(mPhoneAuthData.getPhoneNationCode())) mPhoneAuthData.getPhoneNationCode() else "",
            if (!TextUtils.isEmpty(mPhoneAuthData.getPhoneNumber())) mPhoneAuthData.getPhoneNumber() else "",
            mPasswordEditText.text.toString()
        )
    }
}