package com.nagaja.app.android.SignUp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_success_sign_up.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class SuccessSignUpFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mListener: OnSuccessSignUpFragmentListener

    interface OnSuccessSignUpFragmentListener {
        fun onFinish()
    }

    companion object {
        fun newInstance(): SuccessSignUpFragment {
            val args = Bundle()
            val fragment = SuccessSignUpFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_success_sign_up, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
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

        // Confirm Text View
        val confirmTextView = mContainer.fragment_success_sign_up_confirm_text_view
        confirmTextView.setOnClickListener {
            mListener.onFinish()
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
        if (context is OnSuccessSignUpFragmentListener) {
            mActivity = context as Activity

            if (context is OnSuccessSignUpFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnSuccessSignUpFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnSuccessSignUpFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnSuccessSignUpFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}