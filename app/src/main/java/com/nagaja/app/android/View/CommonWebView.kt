package com.nagaja.app.android.View

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.webkit.*
import com.nagaja.app.android.Utils.NagajaLog


class CommonWebView(context: Context, attrs: AttributeSet?, defStyle: Int) : WebView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        with(settings) {
            // UserAgent 설정
//            userAgentString += "Android_inApp"
            userAgentString = getReplaceUserAgent("nagaja")

            // 자바스크립트 사용 설정
            javaScriptEnabled = true

            // window.open() 동작 설정
            javaScriptCanOpenWindowsAutomatically = true

            // wide viewport 사용 설정
            useWideViewPort = true

            // 컨텐츠가 웹뷰보다 클 때, 스크린 크기에 맞추기
            loadWithOverviewMode = true

            // 줌 컨트롤 사용 여부 설정
            builtInZoomControls = false

            // Web Contents Debugging
            // chrome://inspect/#devices
            setWebContentsDebuggingEnabled(true)

            setSupportZoom(false)

            // 캐시 설정
            cacheMode = WebSettings.LOAD_NO_CACHE

            isFocusable = true
            isFocusableInTouchMode = true

            // 앱 내부 캐시 사용 여부 설정
//            setAppCacheEnabled(false)

            // 로컬 스토리지, 세션 스토리지 사용 여부 설정
            domStorageEnabled = true

            // 파일 접근 허용 설정
            allowFileAccess = true

            // 인코딩 설정
            defaultTextEncodingName = "UTF-8"

            // 멀티윈도우 지원 여부 설정
            setSupportMultipleWindows(true)

            // Database Storage API 사용 여부 설정
            databaseEnabled = true

            // https, http 호환 여부(https에서 http컨텐츠도 보여질수 있도록 함)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webViewClient = CommonWebViewClient()
        webChromeClient = CommonWebChromeClient()
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        requestDisallowInterceptTouchEvent(true)
//        return super.onTouchEvent(event)
//    }

    private fun getReplaceUserAgent(defaultAgent: String): String {
        var userAgent = Regex(" Build/.+; wv").replace(defaultAgent,"")
        userAgent = Regex("Version/.+? ").replace(userAgent,"")
        return userAgent
    }

    private var mWebViewListener: WebViewListener? = null

    fun setWebViewListener(webViewListener: WebViewListener) {
        mWebViewListener = webViewListener
    }

    inner class CommonWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            mWebViewListener?.onPageStarted(url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            mWebViewListener?.onPageFinished(url)
            super.onPageFinished(view, url)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            mWebViewListener?.onLoadResource(url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            mWebViewListener?.shouldOverrideUrlLoading(request)
            return false
//            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            mWebViewListener?.onReceivedError(request, error)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            NagajaLog().d("wooks, WebView ====== shouldOverrideUrlLoading :$url")
            return super.shouldOverrideUrlLoading(view, url)
        }
    }

    inner class CommonWebChromeClient : WebChromeClient() {

        override fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
            mWebViewListener?.onShowFileChooser(view, filePath, fileChooserParams)
            return true
        }

//        override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
//            val newWebView = WebView(context)
//            newWebView.settings.javaScriptEnabled = true
//            newWebView.settings.setSupportZoom(true)
//            newWebView.settings.builtInZoomControls = true
//            newWebView.settings.pluginState = PluginState.ON
//            newWebView.settings.setSupportMultipleWindows(true)
//            view.addView(newWebView)
//            val transport = resultMsg.obj as WebViewTransport
//            transport.webView = newWebView
//            resultMsg.sendToTarget()
//
//            newWebView.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                    view.loadUrl(url)
//                    return true
//                }
//            }
//
//            return true
//
//        }



        //        override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
//            val newWebView = WebView(context)
//            newWebView.settings.javaScriptEnabled = true
//            val dialog = Dialog(context).apply {
//                setContentView(newWebView)
//            }
//            dialog.show()
//            val lp = WindowManager.LayoutParams().apply {
//                copyFrom(dialog.window!!.attributes)
//                width = WindowManager.LayoutParams.MATCH_PARENT
//                height = WindowManager.LayoutParams.MATCH_PARENT
//            }
//            dialog.window!!.attributes = lp
//            newWebView.webChromeClient = object : WebChromeClient() {
//                override fun onCloseWindow(window: WebView) {
//                    dialog.dismiss()
//                }
//            }
//            (resultMsg.obj as WebViewTransport).webView = newWebView
//            resultMsg.sendToTarget()
//            return true
//        }



//        override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
//            val newWebView = WebView(context).apply {
//                settings.javaScriptEnabled = true
//            }
//
//            val dialog = Dialog(context)
//
//            dialog.setContentView(newWebView)
//
//            val params = dialog.window!!.attributes.apply {
//                width = ViewGroup.LayoutParams.MATCH_PARENT
//                height = ViewGroup.LayoutParams.MATCH_PARENT
//            }
//
//            dialog.window!!.attributes = params
//            dialog.show()
//
//            newWebView.webChromeClient = object : WebChromeClient() {
//                override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
//                    super.onJsAlert(view, url, message, result)
//                    return true
//                }
//
//                override fun onCloseWindow(window: WebView?) {
//                    dialog.dismiss()
//                }
//            }
//
//            (resultMsg!!.obj as WebView.WebViewTransport).webView = newWebView
//            resultMsg.sendToTarget()
//
//            return true
//        }




        /*lateinit var dialog : Dialog

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateWindow(view: WebView, isDialog: Boolean,
                                    isUserGesture: Boolean, resultMsg: Message): Boolean {
            // 웹뷰 만들기
            var childWebView = WebView(view.context)
            // 부모 웹뷰와 동일하게 웹뷰 설정
            childWebView.run {
                settings.run {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    setSupportMultipleWindows(true)
                }
                layoutParams = view.layoutParams
                webViewClient = view.webViewClient
                webChromeClient = view.webChromeClient
            }

            dialog = Dialog(view.context).apply {
                setContentView(childWebView)
                window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
                window!!.attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
                show()
            }

            // TODO: 화면 추가 이외에 onBackPressed() 와 같이
            //       사용자의 내비게이션 액션 처리를 위해
            //       별도 웹뷰 관리를 권장함
            //   ex) childWebViewList.add(childWebView)

            // 웹뷰 간 연동
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = childWebView
            resultMsg.sendToTarget()

            return true
        }

        override fun onCloseWindow(window: WebView) {
            super.onCloseWindow(window)
            dialog.dismiss()
            // 화면에서 제거하기
            // TODO: 화면 제거 이외에 onBackPressed() 와 같이
            //       사용자의 내비게이션 액션 처리를 위해
            //       별도 웹뷰 array 관리를 권장함
            //   ex) childWebViewList.remove(childWebView)
        }*/
    }

    interface WebViewListener {
        fun onPageStarted(url: String?, favicon: Bitmap?)
        fun onPageFinished(url: String?)
        fun onLoadResource(url: String?)
        fun shouldOverrideUrlLoading(request: WebResourceRequest?)
        fun onReceivedError(request: WebResourceRequest?, error: WebResourceError?)
        fun shouldOverrideUrlLoading(url: String?)
        fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams)
    }
}