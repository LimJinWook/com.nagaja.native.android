package com.nagaja.app.android.WebView

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_select_language.view.*
import kotlinx.android.synthetic.main.fragment_web_view.view.*
import java.util.*


class WebViewFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mListener: OnWebViewFragmentListener

    interface OnWebViewFragmentListener {
        fun onFinish()
    }

    companion object {
        fun newInstance(): WebViewFragment {
            val args = Bundle()
            val fragment = WebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mContainer = inflater.inflate(R.layout.fragment_web_view, container, false)

        init()

        return mContainer
    }

    private fun init() {
        // WebView 설정
        // WebView 설정
        val webView = mContainer.fragment_web_view

        // JavaScript 허용

        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true)

        // JavaScript의 window.open 허용

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
//        webView.addJavascriptInterface(AndroidBridge(), "TestApp")

        // web client 를 chrome 으로 설정

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(WebChromeClient())

        // webview url load. php 파일 주소

        // webview url load. php 파일 주소
        webView.loadUrl("https://postcode.map.daum.net/")
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
        if (context is OnWebViewFragmentListener) {
            mActivity = context as Activity

            if (context is OnWebViewFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnWebViewFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnWebViewFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnWebViewFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}