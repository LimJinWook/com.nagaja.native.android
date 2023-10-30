package com.nagaja.app.android.SelectMap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Store.StoreFragment
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_select_map.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*


class SelectMapFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mMainAddressTextView: TextView
    private lateinit var mSubAddressTextView: TextView
    private lateinit var mLocationTextView: TextView
    private lateinit var mAddressTextView: TextView

    private lateinit var mListener: OnSelectMapFragmentListener

    private var mNation = ""
    private var mAdminArea = ""
    private var mLocality = ""
    private var mSubLocality = ""
    private var mThoroughfare = ""
    private var mSubThoroughfare = ""

    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    private val mLatLng: LatLng? = null
    private var mMapMarker: Marker? = null

    interface OnSelectMapFragmentListener {
        fun onFinish()
        fun onRegionSetting(data: SelectMapData)
    }

    companion object {

        fun newInstance(): SelectMapFragment {
            val args = Bundle()
            val fragment = SelectMapFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_select_map, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_input_region_information)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Scroll View
        mScrollView = mContainer.fragment_select_map_scroll_view

        // Main Address Text View
        mMainAddressTextView = mContainer.fragment_select_map_main_address_text_view

        // Sub Address Text View
        mSubAddressTextView = mContainer.fragment_select_map_sub_address_text_view

        // My Location View
        val myLocationView = mContainer.fragment_select_map_my_location_view
        myLocationView.setOnClickListener {
            if (TextUtils.isEmpty(getUserLocation())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
                return@setOnClickListener
            }

            val lat = getUserLocation().substring(0, getUserLocation().indexOf(","))
            val lon = getUserLocation().substring(getUserLocation().indexOf(",") + 1, getUserLocation().length)

            showMapAndMarker(lat.toDouble(), lon.toDouble(), false )
        }

        // Location Text View
        mLocationTextView = mContainer.fragment_select_map_location_text_view

        // Map View
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragment_select_map_view) as SupportMapFragment
        mMapFragment.getMapAsync {
            val seoul = LatLng(37.494870, 126.960763)
            val location = CameraUpdateFactory.newLatLngZoom(seoul, 17f)
            it.moveCamera(location)
            it.animateCamera(location)
        }

        // Address Text View
        mAddressTextView = mContainer.fragment_select_map_address_text_view

        // Region Setting Text View
        val regionSettingTextView = mContainer.fragment_select_map_region_setting_text_view
        regionSettingTextView.setOnClickListener {
            val selectMapData = SelectMapData()
            selectMapData.setMainAddress(mMainAddressTextView.text.toString())
            selectMapData.setSubAddress(mSubAddressTextView.text.toString())
            selectMapData.setLocation(mLocationTextView.text.toString())

            mListener.onRegionSetting(selectMapData)
        }

        setMap()
    }

    @SuppressLint("SetTextI18n")
    private fun setMap() {

        mMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
            mMap = googleMap

            googleMap.setOnMapClickListener {
                val screenPoint = mMap!!.projection.toScreenLocation(it)
                val clickLocation = mMap!!.projection.fromScreenLocation(screenPoint)

                val lat = clickLocation.latitude
                val lon = clickLocation.longitude

                showMapAndMarker(lat, lon, true)
            }

            if (null != mMapMarker) {
                mMapMarker!!.remove()
                mMapMarker = null
            }

            if (TextUtils.isEmpty(getUserLocation())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
                return@OnMapReadyCallback
            }

            val lat = getUserLocation().substring(0, getUserLocation().indexOf(","))
            val lon = getUserLocation().substring(getUserLocation().indexOf(",") + 1, getUserLocation().length)

            showMapAndMarker(lat.toDouble(), lon.toDouble(), false)
        })
    }

    private fun showMapAndMarker(latitude: Double, longitude: Double, isClick: Boolean) {
        mMap!!.clear()

        val marker = LatLng(latitude, longitude)
        val location = CameraUpdateFactory.newLatLngZoom(marker, 17f)

        mMapMarker = mMap!!.addMarker(
            MarkerOptions()
                .position(marker)
                .title(mActivity.resources.getString(R.string.text_my_location))
                .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_my_location))
        )
        mMapMarker!!.showInfoWindow()
        if (!isClick) {
            mMap!!.moveCamera(location)
            mMap!!.animateCamera(location)
        }

        mLocationTextView.text = "$latitude, $longitude"
        mAddressTextView.text = getAddress(latitude, longitude)
    }

    private fun getUserLocation(): String {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                StoreFragment.PERMISSION_REQUEST_CODE
            )

            NagajaLog().e("wooks, Location Permission Denied")

            return ""
        } else {
            val gpsTracker = GpsTracker(mActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

            return "$latitude,$longitude"
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    @SuppressLint("SetTextI18n")
    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(mActivity, Locale.getDefault())
        (mActivity).runOnUiThread(
            Runnable {
                try {
                    val addressList = geocoder.getFromLocation(lan, lon, 1)
                    if (addressList!!.size != 0) {
                        address = addressList[0].getAddressLine(0)
//                        mNationCode = addressList[0].countryCode
//                        mPostalCode = addressList[0].postalCode

                        if (address.substring(0, 4) == "대한민국") {
                            address = address.substring(5)
                        }

                        val englishGeocoder = Geocoder(mActivity, Locale.US)
                        val englishAddressList = englishGeocoder.getFromLocation(lan, lon, 1)

                        mNation = englishAddressList!![0].countryName
                        if (null != englishAddressList[0].adminArea) {
                            mAdminArea = englishAddressList[0].adminArea
                        }
                        if (null != englishAddressList[0].locality) {
                            mLocality = englishAddressList[0].locality
                        }
                        if (null != englishAddressList[0].subLocality) {
                            mSubLocality = englishAddressList[0].subLocality
                        }
                        if (null != englishAddressList[0].thoroughfare) {
                            mThoroughfare = englishAddressList[0].thoroughfare
                        }
                        if (null != englishAddressList[0].subThoroughfare) {
                            mSubThoroughfare = englishAddressList[0].subThoroughfare
                        }

                        NagajaLog().d("wooks, Address mNation = $mNation")
                        NagajaLog().d("wooks, Address mAdminArea = $mAdminArea")
                        NagajaLog().d("wooks, Address mLocality = $mLocality")
                        NagajaLog().d("wooks, Address mSubLocality = $mSubLocality")
                        NagajaLog().d("wooks, Address mThoroughfare = $mThoroughfare")
                        NagajaLog().d("wooks, Address mSubThoroughfare = $mSubThoroughfare")
                        NagajaLog().d("wooks, Address address = ${englishAddressList[0].getAddressLine(0).toString()}")

                        val subLocality = if (!TextUtils.isEmpty(mSubLocality)) "$mSubLocality, " else ""
                        val adminArea = if (!TextUtils.isEmpty(mAdminArea)) "$mAdminArea, " else ""
                        mMainAddressTextView.text = subLocality + adminArea + mNation

                        val subThoroughfare = if (!TextUtils.isEmpty(mSubThoroughfare)) "$mSubThoroughfare, " else ""
                        val thoroughfare = if (!TextUtils.isEmpty(mThoroughfare)) "$mThoroughfare, " else ""
                        mSubAddressTextView.text = subThoroughfare + thoroughfare

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
        return address
    }



    fun dispatchTouchEvent() {
        mScrollView.requestDisallowInterceptTouchEvent(true)
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
        if (context is OnSelectMapFragmentListener) {
            mActivity = context as Activity

            if (context is OnSelectMapFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnSelectMapFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnSelectMapFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnSelectMapFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}