package com.nagaja.app.android.Utils

import android.content.Context
import android.webkit.*


class CustomWebViewClient1(context: Context) : WebViewClient() {

    private var mContext: Context

    companion object {
        private const val MIN_CLICK_INTERVAL: Long  = 1000
        private var mLastClickTime: Long            = 0
    }

    init {
        this.mContext = context
    }

    //    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//        NagajaLog().d("wooks, WebView ====== onPageStarted : $url")
//        super.onPageStarted(view, url, favicon)
//    }
//
    override fun onPageFinished(view: WebView?, url: String?) {
        NagajaLog().d("wooks, WebView ====== onPageFinished : $url")

        CookieManager.getInstance().flush()
        super.onPageFinished(view, url)
    }

    //
//    override fun onLoadResource(view: WebView?, url: String?) {
//        NagajaLog().d("wooks, WebView ====== onLoadResource : $url")
//        super.onLoadResource(view, url)
//    }
//
//    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
//        NagajaLog().d("wooks, WebView ====== onReceivedError : ${error?.description.toString()}")
//        super.onReceivedError(view, request, error)
//    }
//
//    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
//        NagajaLog().d("wooks, WebView ====== shouldInterceptRequest : ${request?.url.toString()}")
//        return super.shouldInterceptRequest(view, request)
//    }
//
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        NagajaLog().d("wooks, WebView ====== shouldOverrideUrlLoading :${request?.url.toString()}")
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        super.onReceivedHttpError(view, request, errorResponse)

        NagajaLog().d("wooks, WebView ====== onReceivedHttpError :${errorResponse!!.statusCode}")

//        if (errorResponse.statusCode == 404) {
//            if (SystemClock.elapsedRealtime() - mLastClickTime < MIN_CLICK_INTERVAL) {
//                return
//            }
//            mLastClickTime = SystemClock.elapsedRealtime()
//
//            val intent = Intent(mContext, ErrorPageActivity::class.java)
//            mContext.startActivity(intent)
//        }
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)

        NagajaLog().d("wooks, WebView ====== onReceivedError 1 :${error!!.errorCode}")

        when (error.errorCode) {
            ERROR_AUTHENTICATION -> {
                // 서버에서 사용자 인증 실패
            }

            ERROR_BAD_URL -> {
                // 잘못된 URL
            }

            ERROR_CONNECT -> {
                // 서버로 연결 실패
            }

            ERROR_FAILED_SSL_HANDSHAKE -> {
                // SSL Handshake 실패
            }

            ERROR_FILE -> {
                // 일반 파일 오류
            }

            ERROR_FILE_NOT_FOUND -> {
                // 파일을 찾을 수 없습니다
            }

            ERROR_HOST_LOOKUP -> {
                // 서버 또는 프록시 호스트 이름 조회 실패
            }

            ERROR_IO -> {
                // 서버에서 읽거나 쓰기 실패
            }

            ERROR_PROXY_AUTHENTICATION -> {
                // 프록시에서 사용자 인증 실패
            }

            ERROR_REDIRECT_LOOP -> {
                // 너무 많은 리디렉션
            }

            ERROR_TIMEOUT -> {
                // 연결 시간 초과
            }

            ERROR_TOO_MANY_REQUESTS -> {
                // 페이지 로드중 너무 많은 요청 발생
            }

            ERROR_UNKNOWN -> {
                // 일반 오류
            }

            ERROR_UNSUPPORTED_AUTH_SCHEME -> {
                // 지원되지 않는 인증 체계
            }

            ERROR_UNSUPPORTED_SCHEME -> {
            }
        }

//        MainActivity().urlLoadError()
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        super.onReceivedError(view, errorCode, description, failingUrl)

        NagajaLog().d("wooks, WebView ====== onReceivedError 2 :$errorCode")
    }
}