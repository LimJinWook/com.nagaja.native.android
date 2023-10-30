package com.nagaja.app.android.Address

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_EN
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_FIL
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_JA
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_KO
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_ZH
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.View.CommonWebView
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_address.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*


class AddressFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mAddressTextView: TextView

    private lateinit var mAddressData: AddressData

    private var mNationCode = ""
//    private var mPostalCode = ""

    private var mTestLocation = false

    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    private val mLatLng: LatLng? = null
    private var mMapMarker: Marker? = null

    private var mAddressFragment: AddressFragment? = null

    interface OnAddressFragmentListener {
        fun onFinish()
        fun onSearchAddress(addressData: AddressData)
    }

    companion object {
        const val PERMISSION_REQUEST_CODE     = 0x01

        lateinit var mWebView: CommonWebView
        lateinit var mMapView: View

        lateinit var mListener: OnAddressFragmentListener

        private const val ARGS_IS_KOREA_SELECT          = "args_is_korea_select"
        private const val ARGS_IS_APPLICATION_COMPANY   = "args_is_application_company"

        fun newInstance(isKoreaSelect: Boolean, isApplicationCompany: Boolean): AddressFragment {
            val args = Bundle()
            val fragment = AddressFragment()
            args.putBoolean(ARGS_IS_KOREA_SELECT, isKoreaSelect)
            args.putBoolean(ARGS_IS_APPLICATION_COMPANY, isApplicationCompany)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getIsKoreaSelect(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_KOREA_SELECT)!!
    }

    private fun getIsApplicationCompany(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_APPLICATION_COMPANY)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAddressFragment = this

        var locale: Locale? = null
        var language = ""

        when (SharedPreferencesUtil().getSelectLanguage(mActivity)) {
            SELECT_LANGUAGE_EN -> {
                locale = Locale.ENGLISH
            }

            SELECT_LANGUAGE_KO -> {
                locale = Locale.KOREA
            }

            SELECT_LANGUAGE_FIL -> {
                locale = Locale("fil", "PH")
            }

            SELECT_LANGUAGE_JA -> {
                locale = Locale.JAPAN
            }

            SELECT_LANGUAGE_ZH -> {
                locale = Locale.CHINA
            }

            else -> {
                locale = Locale.ENGLISH
            }
        }

        Locale.setDefault(locale!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_address, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_address_search)

        // Title Image View
        val titleImageView = mContainer.layout_title_title_image_view
        titleImageView.visibility = View.GONE

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Current Location View
        val currentLocationView = mContainer.fragment_address_current_location_view
        currentLocationView.setOnClickListener {
            mWebView.visibility = View.GONE
            mMapView.visibility = View.VISIBLE

            getUserLocation()
        }

        // Web View
        mWebView = mContainer.fragment_address_web_view
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        mWebView.settings.useWideViewPort = false
        mWebView.addJavascriptInterface(AndroidBridge(mAddressFragment!!, mActivity), "Android")
        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                mWebView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }
//        mWebView.loadUrl("https://kemikim.github.io/Kakao-Postcode/")
        mWebView.loadUrl("https://carvi-adas.web.app")

        // Map View
        mMapView = mContainer.fragment_address_map_view

        mMapFragment = childFragmentManager.findFragmentById(R.id.fragment_address_map_fragment) as SupportMapFragment
        mMapFragment.getMapAsync {
            val SEOUL = LatLng(37.56, 126.97)
            it.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
            it.animateCamera(CameraUpdateFactory.zoomTo(15f))
        }

        // Address Text View
        mAddressTextView = mContainer.fragment_address_text_view

        // Set Location Text View
        val setLocationTextView = mContainer.fragment_address_set_location_text_view
        setLocationTextView.setOnClickListener {
            if (!TextUtils.isEmpty(mAddressData.getAddress())) {
                mListener.onSearchAddress(mAddressData)
            }
        }

        if (!getIsKoreaSelect()) {
            mWebView.visibility = View.GONE
            mMapView.visibility = View.VISIBLE

            getUserLocation()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getAddressFromLocationName(addressData: AddressData, isCurrentLocation: Boolean) {
        Geocoder(mActivity).getFromLocationName((if (isCurrentLocation) addressData.getAddress() else addressData.getAddress() + addressData.getExtAddress())!!, 5)?.let { addressList ->
            doSomething(addressList, addressData)

            mAddressTextView.text = addressData.getAddress() + addressData.getExtAddress()
        }
    }

    private fun doSomething(addressList: List<Address>, addressData: AddressData) {
        if (addressList.isNotEmpty())
            addressList[0].let { address ->
                addressData.setLatitude(address.latitude)
                addressData.setLongitude(address.longitude)

                mAddressData = addressData

                setMap(addressData)
            }
    }

    private fun setMap(addressData: AddressData) {
            mMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
                mMap = googleMap

                if (null != mMapMarker) {
                    mMapMarker!!.remove()
                    mMapMarker = null
                }

//                val location = LatLng(addressData.getLatitude(), addressData.getLongitude())
//                mMapMarker = mMap!!.addMarker(
//                    MarkerOptions()
//                        .position(location)
////                            .title(getVehicleData().getVehicleNumber())
////                            .icon(bitmapDescriptorFromVector(mActivity, R.drawable.marker))
////                        .draggable(true)
//                )
//
////                mMap!!.setOnMarkerDragListener(object : OnMarkerDragListener{
////                    override fun onMarkerDrag(p0: Marker) {
////                    }
////
////                    override fun onMarkerDragEnd(p0: Marker) {
////
//////                        mMap!!.clear()
//////                        mMap!!.addMarker(MarkerOptions().position(LatLng(p0.position.latitude, p0.position.longitude)))
////                        if (!TextUtils.isEmpty(getAddress(p0.position.latitude, p0.position.longitude))) {
////                            mAddressTextView.text = getAddress(p0.position.latitude, p0.position.longitude)
////                        }
////                    }
////
////                    override fun onMarkerDragStart(p0: Marker) {
////                    }
////
////                })

                val currentMarker = LatLng(addressData.getLatitude(), addressData.getLongitude())
                val location = CameraUpdateFactory.newLatLngZoom(currentMarker, 17f)

                mMapMarker = mMap!!.addMarker(
                    MarkerOptions()
                        .position(currentMarker)
                        .title(mActivity.resources.getString(R.string.text_my_location))
                        .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_2))
                )

                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarker, 11.6f))
                mMap!!.animateCamera(location)

                mMapMarker!!.showInfoWindow()

                Handler(Looper.getMainLooper()).postDelayed({
                    mMap!!.moveCamera(location)
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(17f))
                }, 500)

                mMap!!.setOnMapClickListener { latLng ->
                    mMap!!.clear()
                    mMapMarker = mMap!!.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(mActivity.resources.getString(R.string.text_my_location))
                            .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_2))
                    )

                    mAddressData.setLatitude(latLng.latitude)
                    mAddressData.setLongitude(latLng.longitude)
                    mAddressData.setAddress(getAddress(latLng.latitude, latLng.longitude)!!)

                    if (!TextUtils.isEmpty(getAddress(latLng.latitude, latLng.longitude))) {
                        mAddressTextView.text = getAddress(latLng.latitude, latLng.longitude)
                    }
                }
            })
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(mActivity, Locale.getDefault())
        (mActivity).runOnUiThread(
            Runnable {
                try {
                    val addressList = geocoder.getFromLocation(lan, lon, 1)
                    if (addressList!!.size != 0) {
                        address = addressList[0].getAddressLine(0)
                        mNationCode = addressList[0].countryCode
//                        mPostalCode = addressList[0].postalCode

                        if (address.substring(0, 4) == "대한민국") {
                            address = address.substring(5)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        return address
    }

    private fun getDefaultLocation(location: String) {
        var defaultLocation = ""
        if (TextUtils.isEmpty(location) || location == "0,0") {
            when (SharedPreferencesUtil().getSelectLanguage(mActivity)) {
                SELECT_LANGUAGE_EN -> {
                    getDeniedLocation(CURRENT_LOCATION_EN)
                }

                SELECT_LANGUAGE_KO -> {
                    getDeniedLocation(CURRENT_LOCATION_KO)
                }

                SELECT_LANGUAGE_FIL -> {
                    getDeniedLocation(CURRENT_LOCATION_FIL)
                }

                SELECT_LANGUAGE_ZH -> {
                    getDeniedLocation(CURRENT_LOCATION_ZH)
                }

                SELECT_LANGUAGE_JA -> {
                    getDeniedLocation(CURRENT_LOCATION_JA)
                }

                else -> {
                    getDeniedLocation(CURRENT_LOCATION_FIL)
                }
            }
        } else {
            getDeniedLocation(location)
        }
    }

    private fun getDeniedLocation(location: String) {
//        mLatitude = location.substring(0, location.indexOf(","))
//        mLongitude = location.substring(location.indexOf(",") + 1, location.length)
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )

//            getDefaultLocation("")

            NagajaLog().e("wooks, Location Permission Denied")

        } else {
            val gpsTracker = GpsTracker(mActivity)
            var latitude = gpsTracker.getLatitude()
            var longitude = gpsTracker.getLongitude()

            getDefaultLocation(latitude.toString() + "," + longitude.toString())

            val addressData = AddressData()

            if (mTestLocation) {
                addressData.setLatitude(14.59889)
                addressData.setLongitude(120.98417)
                addressData.setAddress(getAddress(14.59889, 120.98417)!!)
            } else {

//                if (SharedPreferencesUtil().getSaveNationCode(mActivity) == NATION_CODE_PHILIPPINES) {
//                    latitude = CURRENT_LOCATION_FIL.substring(0, CURRENT_LOCATION_FIL.indexOf(",")).toDouble()
//                    longitude = CURRENT_LOCATION_FIL.substring(CURRENT_LOCATION_FIL.indexOf(",") + 1, CURRENT_LOCATION_FIL.length).toDouble()
//                }

                addressData.setLatitude(latitude!!)
                addressData.setLongitude(longitude!!)
                addressData.setAddress(getAddress(latitude, longitude)!!)
            }

//            if (!TextUtils.isEmpty(mPostalCode)) {
//                addressData.setPostalCode(mPostalCode)
//            }

//            mLatitude = latitude.toString()
//            mLongitude = longitude.toString()

            getAddressFromLocationName(addressData, true)
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
        if (context is OnAddressFragmentListener) {
            mActivity = context as Activity

            if (context is OnAddressFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnAddressFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnAddressFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnAddressFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    private class AndroidBridge(addressFragment: AddressFragment, context: Context) {
        private var mContext: Context
        private lateinit var mAddressFragment: AddressFragment

        init {
            this.mContext = context
            this.mAddressFragment = addressFragment
        }

//        @SuppressLint("SetTextI18n")
//        @JavascriptInterface
//        fun postMessage(Zone: String?, Addr: String?, extraAddr: String?) {
//
//            (mContext as Activity).runOnUiThread {
//                NagajaLog().d("wooks, xxxxxxxxxxx Zone = $Zone")
//                NagajaLog().d("wooks, xxxxxxxxxxx Addr = $Addr")
//                NagajaLog().d("wooks, xxxxxxxxxxx extraAddr = $extraAddr")
//
//                val addressData = AddressData()
//
//
//                addressData.setPostalCode((if (!TextUtils.isEmpty(Zone)) Zone else "")!!)
//                addressData.setAddress((if (!TextUtils.isEmpty(Addr)) Addr else "")!!)
//                addressData.setExtAddress((if (!TextUtils.isEmpty(extraAddr)) extraAddr else "")!!)
//
//                mListener.onSearchAddress(addressData)
//
////                addressData.setPostalCode((if (!TextUtils.isEmpty(Zone)) Zone else "")!!)
////                addressData.setAddress((if (!TextUtils.isEmpty(Addr)) Addr else "")!!)
////                addressData.setExtAddress((if (!TextUtils.isEmpty(extraAddr)) extraAddr else "")!!)
////
////                mWebView.visibility = View.GONE
////                mMapView.visibility = View.VISIBLE
////
////                mAddressFragment.getAddressFromLocationName(addressData)
//            }
//        }

        @SuppressLint("SetTextI18n")
        @JavascriptInterface
        fun processDATA(Zone: String?, Addr: String?, extraAddr: String?) {
            (mContext as Activity).runOnUiThread {
                NagajaLog().d("wooks, xxxxxxxxxxx Zone = $Zone")
                NagajaLog().d("wooks, xxxxxxxxxxx Addr = $Addr")
                NagajaLog().d("wooks, xxxxxxxxxxx extraAddr = $extraAddr")

                val addressData = AddressData()


                addressData.setPostalCode((if (!TextUtils.isEmpty(Zone)) Zone else "")!!)
                addressData.setAddress((if (!TextUtils.isEmpty(Addr)) Addr else "")!!)
                addressData.setExtAddress((if (!TextUtils.isEmpty(extraAddr)) extraAddr else "")!!)

                mListener.onSearchAddress(addressData)

//                addressData.setPostalCode((if (!TextUtils.isEmpty(Zone)) Zone else "")!!)
//                addressData.setAddress((if (!TextUtils.isEmpty(Addr)) Addr else "")!!)
//                addressData.setExtAddress((if (!TextUtils.isEmpty(extraAddr)) extraAddr else "")!!)
//
//                mWebView.visibility = View.GONE
//                mMapView.visibility = View.VISIBLE
//
//                mAddressFragment.getAddressFromLocationName(addressData)
            }
        }
    }
}