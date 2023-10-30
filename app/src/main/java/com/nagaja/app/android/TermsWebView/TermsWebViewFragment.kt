package com.nagaja.app.android.TermsWebView

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebSettings
import androidx.core.content.ContextCompat.getSystemService
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.R
import kotlinx.android.synthetic.main.fragment_web_view.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*


class TermsTermsWebViewFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mListener: OnTermsWebViewFragmentListener

    interface OnTermsWebViewFragmentListener {
        fun onFinish()
    }

    companion object {

        const val ARGS_SELECT_TERMS_TYPE                = "args_select_terms_type"
        const val ARGS_SELECT_TERMS_URL                 = "args_select_terms_url"

        fun newInstance(type: Int, url: String): TermsTermsWebViewFragment {
            val args = Bundle()
            val fragment = TermsTermsWebViewFragment()
            args.putInt(ARGS_SELECT_TERMS_TYPE, type)
            args.putString(ARGS_SELECT_TERMS_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getSelectTermsType(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_SELECT_TERMS_TYPE) as Int
    }

    private fun getUrl(): String {
        val arguments = arguments
        return arguments?.getString(ARGS_SELECT_TERMS_URL) as String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mContainer = inflater.inflate(R.layout.fragment_terms_web_view, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        var title = ""
        if (getSelectTermsType() == 1) {
            title = mActivity.resources.getString(R.string.text_terms_agree_7)
        } else if (getSelectTermsType() == 2) {
            title = mActivity.resources.getString(R.string.text_terms_agree_8)
        } else if (getSelectTermsType() == 3) {
            title = mActivity.resources.getString(R.string.text_terms_agree_9)
        } else if (getSelectTermsType() == 4) {
            title = mActivity.resources.getString(R.string.text_terms_agree_10)
        }
        titleTextView.text = title

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Web View
        val webView = mContainer.fragment_web_view
//        webView.webViewClient = WebViewClient()
//        webView.settings.javaScriptEnabled = true
//        webView.settings.javaScriptCanOpenWindowsAutomatically = true
//        webView.settings.setSupportMultipleWindows(true)
//        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
//        webView.settings.loadWithOverviewMode = true
//        webView.setInitialScale(100)

        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
//        webView.settings.textZoom = 100
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.settings.supportZoom()
        webView.setInitialScale(100)


        webView.loadUrl(getUrl())
    }

//    private fun getScale(): Int {
//        val display: Display = (mActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager?)!!.defaultDisplay
//        val width: Int = display.getWidth()
//        var `val`: Double = width / PIC_WIDTH
//        `val` = `val` * 100.0
//        return `val`.toInt()
//    }

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
        if (context is OnTermsWebViewFragmentListener) {
            mActivity = context as Activity

            if (context is OnTermsWebViewFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnTermsWebViewFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnTermsWebViewFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnTermsWebViewFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}