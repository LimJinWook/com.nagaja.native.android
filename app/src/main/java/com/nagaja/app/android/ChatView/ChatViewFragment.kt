package com.nagaja.app.android.ChatView

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.webkit.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_NOTIFICATION
import com.nagaja.app.android.Base.NagajaActivity.Companion.SELECT_HEADER_MENU_TYPE_SEARCH
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.Utils.NetworkProvider.Companion.CHAT_ROOM_REQUEST
import com.nagaja.app.android.Utils.NetworkProvider.Companion.CHAT_ROOM_URL
import kotlinx.android.synthetic.main.fragment_chat_view.view.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class ChatViewFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mWebView: WebView

    private lateinit var mListener: OnChatViewFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    interface OnChatViewFragmentListener {
        fun onFinish()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

        const val ARGS_CHAT_CLASS                = "args_chat_class"
        const val ARGS_CHAT_DATA                 = "args_chat_data"
        const val ARGS_CHAT_ROOM_NUMBER          = "args_chat_room_number"

        fun newInstance(chatClass: String, chatData: String, roomNumber: Int): ChatViewFragment {
            val args = Bundle()
            val fragment = ChatViewFragment()
            args.putString(ARGS_CHAT_CLASS, chatClass)
            args.putString(ARGS_CHAT_DATA, chatData)
            args.putInt(ARGS_CHAT_ROOM_NUMBER, roomNumber)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getChatClass(): String {
        val arguments = arguments
        return arguments?.getString(ARGS_CHAT_CLASS) as String
    }

    private fun getChatData(): String {
        val arguments = arguments
        return arguments?.getString(ARGS_CHAT_DATA) as String
    }

    private fun getRoomNumber(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_CHAT_ROOM_NUMBER) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        inflater.inflate(R.layout.fragment_chat_view, container, false).also { mContainer = it }

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun init() {

        // Header Location View
        val headerLocationView = mContainer.layout_header_location_view
        headerLocationView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION)
        }

        // Header Location Text View
        val locationTextView = mContainer.layout_header_location_text_view
        if (!TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
            locationTextView.text = MAPP.SELECT_NATION_NAME
        }

        // Header Select Language Image View
        val selectLanguageImageView = mContainer.layout_header_select_language_image_view
        selectLanguageImageView.setImageResource(getLanguageIcon(mActivity))
        selectLanguageImageView.setOnClickListener {
            showSelectLanguageCustomDialog(mActivity)
        }

        // Header Search Image View
        val headerSearchImageView = mContainer.layout_header_search_image_view
        headerSearchImageView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_SEARCH)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.setOnSingleClickListener {
            setShareUrl(mActivity, COMPANY_TYPE_NOTICE, COMPANY_TYPE_MAIN)
        }

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view
        headerNotificationImageView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(SELECT_HEADER_MENU_TYPE_NOTIFICATION)
        }

        if (MAPP.IS_NON_MEMBER_SERVICE || TextUtils.isEmpty(SharedPreferencesUtil().getSecureKey(requireActivity()))) {
            return
        }

        mSwipeRefreshLayout = mContainer.fragment_chat_view_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {

            if (NetworkManager.checkNetworkState(requireActivity())) {
                mSwipeRefreshLayout.isRefreshing = true
                mWebView.reload()
            } else {
                mSwipeRefreshLayout.isRefreshing = false
            }
        }

        // Web View
        mWebView = mContainer.fragment_chat_view_web_view

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



        if (getRoomNumber() > 0) {
            chatWebViewLoadUrl(mWebView, CHAT_ROOM_URL + getRoomNumber().toString(), true)
        } else {
            chatWebViewLoadUrl(mWebView, CHAT_ROOM_REQUEST + getChatClass(), false)
        }
    }

    private fun chatWebViewLoadUrl(view: WebView, url: String, isRoom: Boolean) {

        NagajaLog().e("wooks, Chat Url = $url")
        NagajaLog().e("wooks, Chat Data = ${getChatData()}")
        NagajaLog().e("wooks, Chat Room Number = ${getRoomNumber()}")

        val device: MutableMap<String, String> = HashMap()
        device["Authorization"] = "Bearer ${MAPP.USER_DATA.getSecureKey()}"
        if (!isRoom) {
            device["roomMembers"] = getChatData()
        }
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
        if (context is OnChatViewFragmentListener) {
            mActivity = context as Activity

            if (context is OnChatViewFragmentListener) {
                mListener = context;
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnChatViewFragmentListener"
                );
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnChatViewFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnChatViewFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // =================================================================================
    // API
    // =================================================================================

}