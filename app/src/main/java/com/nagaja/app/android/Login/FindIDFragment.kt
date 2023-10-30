package com.nagaja.app.android.Login

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_find_id.view.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_select_language.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*

class FindIDFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mSuccessMessageView: View

    private lateinit var mIdForgotMessageTextView: TextView
    private lateinit var mPhoneAuthTextView: TextView
    private lateinit var mMemberIDTextView: TextView

    private var mIsFindIDSuccess = false

    private lateinit var mListener: OnFindIDFragmentListener

    interface OnFindIDFragmentListener {
        fun onBack()
        fun onPhoneAuthScreen(phoneAuthType: Int)
        fun onFindPasswordScreen(isAfterFindID: Boolean)
    }

    companion object {
        fun newInstance(): FindIDFragment {
            val args = Bundle()
            val fragment = FindIDFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_find_id, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_find_id)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // ID Forgot Message Text View
        mIdForgotMessageTextView = mContainer.fragment_find_id_forgot_message_text_view

        // Success Message View
        mSuccessMessageView = mContainer.fragment_find_id_success_message_view

        // Member ID Text View
        mMemberIDTextView = mContainer.fragment_find_id_member_id_text_view

        // Phone Auth Text View
        mPhoneAuthTextView = mContainer.fragment_find_id_phone_auth_text_view
        mPhoneAuthTextView.setOnClickListener {
            if (!mIsFindIDSuccess) {
                mListener.onPhoneAuthScreen(PHONE_AUTH_TYPE_FIND_ID)
            } else {
                mListener.onFindPasswordScreen(true)
            }
        }
    }

    fun phoneAuthSuccess(findID: String) {
        mIdForgotMessageTextView.visibility = View.GONE
        mSuccessMessageView.visibility = View.VISIBLE

        if (!TextUtils.isEmpty(findID)) {
//            var middleMask = ""
//            middleMask = if (findID.length > 2) {
//                findID.substring(1, findID.length - 1)
//            } else {
//                findID.substring(1, findID.length)
//            }
//            var masking = ""
//            for (i in middleMask.indices) {
//                masking += "*"
//            }
//
//            val rePlaceFindID = (findID.substring(0, 2)
//                    + middleMask.replace(middleMask, masking)
//                    + findID.substring(findID.length - 2, findID.length))



            mMemberIDTextView.text = String.format(mActivity.resources.getString(R.string.text_user_id_find), Util().getEmailMasking(findID))
            mPhoneAuthTextView.text = mActivity.resources.getString(R.string.text_find_password)

            mIsFindIDSuccess = true
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
        if (context is OnFindIDFragmentListener) {
            mActivity = context as Activity

            if (context is OnFindIDFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnFindIDFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnFindIDFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFindIDFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}