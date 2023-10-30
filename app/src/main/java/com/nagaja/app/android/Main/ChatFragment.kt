package com.nagaja.app.android.Main

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NetworkProvider.Companion.CHAT_LIST_URL
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View


    private lateinit var mWebView: WebView

    companion object {
        lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_chat, container, false)

        init()

        return mContainer
    }

    private fun init() {

        if (MAPP.IS_NON_MEMBER_SERVICE || TextUtils.isEmpty(SharedPreferencesUtil().getSecureKey(requireActivity()))) {
            return
        }

        mSwipeRefreshLayout = mContainer.fragment_chat_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {

            if (NetworkManager.checkNetworkState(requireActivity())) {
                mSwipeRefreshLayout.isRefreshing = true
                mWebView.reload()
            } else {
                mSwipeRefreshLayout.isRefreshing = false
            }
        }

        // Web View
        mWebView = mContainer.fragment_chat_web_view

        mWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(mWebView, true)
        cookieManager.setCookie("https://www.nagaja.com/", "nagaja")

        mWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.domStorageEnabled = true
        mWebView.settings.javaScriptCanOpenWindowsAutomatically = true

        mWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        mWebView.settings.loadsImagesAutomatically = true
        mWebView.settings.setSupportMultipleWindows(true)
        mWebView.settings.loadWithOverviewMode = true
        mWebView.settings.useWideViewPort = true
        mWebView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
//        mWebView.addJavascriptInterface(WebInterface(this@MainActivity, this), "nagaja_web")

        mWebView.setInitialScale(100)

        mWebView.webViewClient = CustomWebViewClient()


//        mWebView.loadUrl("https://dev.nagaja.com/mypage/chats/296")



        chatWebViewLoadUrl(mWebView, CHAT_LIST_URL)
    }

    private fun chatWebViewLoadUrl(view: WebView, url: String) {

        NagajaLog().e("wooks, Chat Url = $url")
        NagajaLog().e("wooks, Authorization = Bearer ${MAPP.USER_DATA.getSecureKey()}")

        val device: MutableMap<String, String> = HashMap()
        device["Authorization"] = "Bearer ${MAPP.USER_DATA.getSecureKey()}"
        view.loadUrl(url, device)
    }

    class CustomWebViewClient() : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            mSwipeRefreshLayout.isRefreshing = false
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?,
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?,
        ): WebResourceResponse? {
            return super.shouldInterceptRequest(view, request)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {

            NagajaLog().d("wooks, WebView ====== shouldOverrideUrlLoading :${request?.url.toString()}")

            return super.shouldOverrideUrlLoading(view, request)
        }

    }

}