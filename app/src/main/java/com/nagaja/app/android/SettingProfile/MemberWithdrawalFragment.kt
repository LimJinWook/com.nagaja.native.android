package com.nagaja.app.android.SettingProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.SettingProfile.SettingProfileActivity.Companion.mIsSuccessMemberWithdrawal
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_member_withdrawal.view.*
import kotlinx.android.synthetic.main.fragment_modify_profile.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.fragment_success_member_withdrawal.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.text.SimpleDateFormat
import java.util.*

class MemberWithdrawalFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mMemberWithdrawalView: View
    private lateinit var mSuccessMemberWithdrawalView: View

    private lateinit var mEndDateTextView: TextView

    private lateinit var mBackImageView: ImageView
    private lateinit var mCheck1ImageView: ImageView
    private lateinit var mCheck2ImageView: ImageView
    private lateinit var mCheck3ImageView: ImageView

    private var mIsCheck1 = false
    private var mIsCheck2 = false
    private var mIsCheck3 = false

    private lateinit var mListener: OnMemberWithdrawalFragmentListener

    interface OnMemberWithdrawalFragmentListener {
        fun onBack()
        fun onPhoneAuthScreen()
        fun onSuccessMemberWithdrawal()
    }

    companion object {
        fun newInstance(): MemberWithdrawalFragment {
            val args = Bundle()
            val fragment = MemberWithdrawalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_member_withdrawal, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        mBackImageView = mContainer.layout_title_back_image_view
        mBackImageView.visibility = View.VISIBLE
        mBackImageView.setOnClickListener {
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_member_withdrawal)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Member Withdrawal View
        mMemberWithdrawalView = mContainer.fragment_member_withdrawal_view

        // Check 1 Image View
        mCheck1ImageView = mContainer.fragment_member_withdrawal_check_1_image_view
        mCheck1ImageView.setOnClickListener {
            mIsCheck1 = !mIsCheck1
            mCheck1ImageView.setImageResource(if (!mIsCheck1) R.drawable.ic_baseline_check_circle_outline_24 else R.drawable.ic_baseline_check_circle_24)
        }

        // Check 2 Image View
        mCheck2ImageView = mContainer.fragment_member_withdrawal_check_2_image_view
        mCheck2ImageView.setOnClickListener {
            mIsCheck2 = !mIsCheck2
            mCheck2ImageView.setImageResource(if (!mIsCheck2) R.drawable.ic_baseline_check_circle_outline_24 else R.drawable.ic_baseline_check_circle_24)
        }

        // Check 3 Image View
        mCheck3ImageView = mContainer.fragment_member_withdrawal_check_3_image_view
        mCheck3ImageView.setOnClickListener {
            mIsCheck3 = !mIsCheck3
            mCheck3ImageView.setImageResource(if (!mIsCheck3) R.drawable.ic_baseline_check_circle_outline_24 else R.drawable.ic_baseline_check_circle_24)
        }

        // Identity Verification Text View
        val identityVerificationTextView = mContainer.fragment_member_withdrawal_identity_verification_text_view
        identityVerificationTextView.setOnClickListener {

            if (mIsCheck1 && mIsCheck2 && mIsCheck3) {
                val customDialog = CustomDialog(
                    mActivity,
                    DIALOG_TYPE_NORMAL,
                    mActivity.resources.getString(R.string.text_member_withdrawal),
                    mActivity.resources.getString(R.string.text_message_withdrawal_2),
                    mActivity.resources.getString(R.string.text_cancel),
                    mActivity.resources.getString(R.string.text_confirm)
                )

                customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
                    override fun onCancel() {
                        customDialog.dismiss()
                    }

                    override fun onConfirm() {
                        customDialog.dismiss()
                        mListener.onPhoneAuthScreen()
                    }
                })
                customDialog.show()
            } else {
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_withdrawal_agree))
            }
        }

        // Success Member Withdrawal View
        mSuccessMemberWithdrawalView = mContainer.fragment_member_withdrawal_success_view

        // Success Member Withdrawal End Date Text View
        mEndDateTextView = mContainer.fragment_success_member_withdrawal_end_date_text_view

        // Success Member Withdrawal Confirm Text View
        val confirmTextView = mContainer.fragment_success_member_withdrawal_confirm_text_view
        confirmTextView.setOnClickListener {
             mListener.onSuccessMemberWithdrawal()
        }
    }

    fun successMemberWithdrawal() {
        mIsSuccessMemberWithdrawal = true

        mBackImageView.visibility = View.GONE
        mMemberWithdrawalView.visibility = View.GONE
        mSuccessMemberWithdrawalView.visibility = View.VISIBLE

        if (!TextUtils.isEmpty(calculateDate())) {
            mEndDateTextView.text = calculateDate()
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(Exception::class)
    private fun calculateDate(): String? {
        try {
            val currentNow = System.currentTimeMillis()
            val currentDate = Date(currentNow)
            val sdf = SimpleDateFormat("yyyy.MM.dd")

            val dtFormat = SimpleDateFormat("yyyy.MM.dd")
            val cal: Calendar = Calendar.getInstance()
            val dt: Date = dtFormat.parse(sdf.format(currentDate)) as Date

            cal.time = dt
            cal.add(Calendar.DATE, 14)

            return dtFormat.format(cal.time)
        } catch (e: Exception) {
            e.printStackTrace()

            return null
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
        if (context is OnMemberWithdrawalFragmentListener) {
            mActivity = context as Activity

            if (context is OnMemberWithdrawalFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnMemberWithdrawalFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnMemberWithdrawalFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnMemberWithdrawalFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}