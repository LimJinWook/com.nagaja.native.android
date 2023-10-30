package com.nagaja.app.android.Map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Splash.SplashFragment
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.fragment_phone_auth.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*


class MapFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mAddressTextView: TextView
    private lateinit var mSelectTextView: TextView

    private lateinit var mClipBoardImageView: ImageView

    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    private val mLatLng: LatLng? = null
    private var mMapMarker: Marker? = null
    private var mCurrentMapMarker: Marker? = null

    private var mClipboardManager: ClipboardManager? = null
    private var mClipData: ClipData? = null

    private var mLatitude = ""
    private var mLongitude = ""

    private lateinit var mListener: OnMapFragmentListener

    interface OnMapFragmentListener {
        fun onFinish()
        fun onConfirm()
        fun onSelectLocation(latitude: String, longitude: String)
    }

    companion object {
        const val ARGS_LOCATION                     = "args_location"
        const val ARGS_ADDRESS                      = "args_address"
        const val ARGS_STORE_NAME                   = "args_store_name"

        fun newInstance(location: String, address: String, storeName: String): MapFragment {
            val args = Bundle()
            val fragment = MapFragment()
            args.putString(ARGS_LOCATION, location)
            args.putString(ARGS_ADDRESS, address)
            args.putString(ARGS_STORE_NAME, storeName)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getLocation(): String? {
        val arguments = arguments
        return arguments?.getString(ARGS_LOCATION)
    }

    private fun getAddress(): String? {
        val arguments = arguments
        return arguments?.getString(ARGS_ADDRESS)
    }

    private fun getStoreName(): String? {
        val arguments = arguments
        return arguments?.getString(ARGS_STORE_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mClipboardManager = mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_map, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Google Map View
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragment_map_view) as SupportMapFragment
        mMapFragment.getMapAsync {
            val seoul = LatLng(37.494870, 126.960763)
            val location = CameraUpdateFactory.newLatLngZoom(seoul, 17f)
            it.moveCamera(location)
            it.animateCamera(location)
        }

        // Back Image View
        val backImageView = mContainer.fragment_map_back_image_view
        backImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Name Text View
        val nameTextView = mContainer.fragment_map_name_text_view
        if (!TextUtils.isEmpty(getStoreName())) {
            nameTextView.text = getStoreName()
        }

        // Address Text View
        mAddressTextView = mContainer.fragment_map_address_text_view
        if (!TextUtils.isEmpty(getAddress())) {
            mAddressTextView.text = getAddress()
        }

        // Clip Board Image View
        mClipBoardImageView = mContainer.fragment_map_clip_board_image_view
        mClipBoardImageView.setOnClickListener {
            if (!TextUtils.isEmpty(mAddressTextView.text)) {
                mClipData = ClipData.newPlainText("label", mAddressTextView.text.toString())
                mClipboardManager!!.setPrimaryClip(mClipData!!)

                showToast(mActivity, mActivity.resources.getString(R.string.text_copy_address))
            }
        }

        // Select Text View
        mSelectTextView = mContainer.fragment_map_select_text_view
        mSelectTextView.isEnabled = false
        mSelectTextView.setOnClickListener {
            if (!TextUtils.isEmpty(mLatitude) && !TextUtils.isEmpty(mLongitude)) {
                mListener.onSelectLocation(mLatitude, mLongitude)
            }
        }

        setMap()
    }

    @SuppressLint("SetTextI18n")
    private fun setMap() {

        mMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
            mMap = googleMap

            if (TextUtils.isEmpty(getLocation())) {
//                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
//                return@OnMapReadyCallback

                mSelectTextView.isEnabled = true
                mSelectTextView.visibility = View.VISIBLE


                val lat = getDefaultLocation().substring(0, getDefaultLocation().indexOf(","))
                val lon = getDefaultLocation().substring(getDefaultLocation().indexOf(",") + 1, getDefaultLocation().length)

                val marker = LatLng(lat.toDouble(), lon.toDouble())
                val location = CameraUpdateFactory.newLatLngZoom(marker, 17f)
                mMap!!.addMarker(
                    MarkerOptions()
                        .position(marker)
                        .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_4))
                )
                mMap!!.animateCamera(location)
                if (TextUtils.isEmpty(getAddress())) {
                    if (!TextUtils.isEmpty(getAddress(lat.toDouble(), lon.toDouble()))) {
                        mAddressTextView.text = getAddress(lat.toDouble(), lon.toDouble()) +
                                "\n${mActivity.resources.getString(R.string.text_latitude)}: ${Util().getSixDecimalPlaces(lat.toDouble())}, " +
                                "${mActivity.resources.getString(R.string.text_longitude)}}: ${Util().getSixDecimalPlaces(lon.toDouble())}"
                    }

                }

                mLatitude = Util().getSixDecimalPlaces(lat.toDouble())
                mLongitude = Util().getSixDecimalPlaces(lon.toDouble())


                mMap!!.setOnMapClickListener { latLng ->
                    mMap!!.clear()
                    mMap!!.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_4)))
                    if (TextUtils.isEmpty(getAddress())) {
                        if (!TextUtils.isEmpty(getAddress(latLng.latitude, latLng.longitude))) {
                            mAddressTextView.text = getAddress(latLng.latitude, latLng.longitude) +
                                    "\n${mActivity.resources.getString(R.string.text_latitude)}: ${Util().getSixDecimalPlaces(latLng.latitude)}, " +
                                    "${mActivity.resources.getString(R.string.text_longitude)}}: ${Util().getSixDecimalPlaces(latLng.longitude)}"

                            mLatitude = Util().getSixDecimalPlaces(latLng.latitude)
                            mLongitude = Util().getSixDecimalPlaces(latLng.longitude)
                        }
                    }
                }
            } else {
                val lat = getLocation()!!.substring(0, getLocation()!!.indexOf(","))
                val lon = getLocation()!!.substring(getLocation()!!.indexOf(",") + 1, getLocation()!!.length)

                val marker = LatLng(lat.toDouble(), lon.toDouble())
                val location = CameraUpdateFactory.newLatLngZoom(marker, 17f)

                mMapMarker = mMap!!.addMarker(
                    MarkerOptions()
                        .position(marker)
                        .title(if (!TextUtils.isEmpty(getStoreName())) getStoreName() else "")
                        .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_4))
                )
                mMapMarker!!.showInfoWindow()

                val gpsTracker = GpsTracker(mActivity)
                val currentMarker = LatLng(gpsTracker.getLatitude()!!, gpsTracker.getLongitude()!!)

                mMapMarker = mMap!!.addMarker(
                    MarkerOptions()
                        .position(currentMarker)
                        .title(mActivity.resources.getString(R.string.text_my_location))
                        .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_2))
                )

//                mMapMarker!!.showInfoWindow()
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarker, 11.6f))
                mMap!!.animateCamera(location)

                if (TextUtils.isEmpty(getAddress())) {
                    mAddressTextView.text = getAddress(lat.toDouble(), lon.toDouble())
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
//                        mNationCode = addressList[0].countryCode
//                        mPostalCode = addressList[0].postalCode

                        if (address.substring(0, 4) == "대한민국") {
                            address = address.substring(5)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
        return address
    }

    private fun getDefaultLocation(): String {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                SplashFragment.PERMISSION_REQUEST_CODE
            )

            return ""
        } else {
            val gpsTracker = GpsTracker(requireActivity())
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

//            SharedPreferencesUtil().setSaveLocation(requireActivity(), "$latitude,$longitude")
            return "$latitude,$longitude"
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
        if (context is OnMapFragmentListener) {
            mActivity = context as Activity

            if (context is OnMapFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnMapFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnMapFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnMapFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}