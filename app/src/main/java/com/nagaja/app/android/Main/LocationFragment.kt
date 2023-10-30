package com.nagaja.app.android.Main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.MainNationAdapter
import com.nagaja.app.android.Adapter.ServiceAreaAdapter
import com.nagaja.app.android.Adapter.SubNationAdapter
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_EN
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_FIL
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_JA
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_KO
import com.nagaja.app.android.Base.NagajaActivity.Companion.CURRENT_LOCATION_ZH
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Splash.SplashFragment
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.R

import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_location.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class LocationFragment : NagajaFragment() {

    private lateinit var mContainer: View
    private lateinit var mSettingCurrentLocationView: View
    private lateinit var mServiceAllUnderLineView: View
    private lateinit var mServiceAreaUnderLineView: View
    private lateinit var mServiceAllView: View
    private lateinit var mServiceAreaView: View
    
    private lateinit var mServiceAllTextView: TextView
    private lateinit var mServiceAreaTextView: TextView

    private lateinit var mMainNationRecyclerView: RecyclerView
    private lateinit var mSubNationRecyclerView: RecyclerView
    private lateinit var mServiceAreaRecyclerView: RecyclerView

    private lateinit var mSelectNationSpinner: PowerSpinnerView

    private lateinit var mMainNationAdapter: MainNationAdapter
    private lateinit var mSubNationAdapter: SubNationAdapter
    private lateinit var mServiceAreaAdapter: ServiceAreaAdapter

    private var mNationUid = 0
    private var mMainNationUid = 0
    private var mSubNationUid = 0

    private var mLocationUid = 0

    private var mSelectNationPosition = 0
    private var mSpinnerSelectionIndex = 0

    private var mMainNationName = ""
    private var mSubNationName = ""

    private var mIsDefault = true
    private var mIsSelectServiceAll = true

    private var mNationListData = ArrayList<LocationNationData>()
    private var mLocationAreaMainListData = ArrayList<LocationAreaMainData>()
    private var mServiceAreaLocationData = ArrayList<ServiceAreaLocationData>()

    private lateinit var mRequestQueue: RequestQueue

    companion object {
        const val SELECT_SERVICE_ALL                  = 0x01
        const val SELECT_SERVICE_AREA                 = 0x02
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainNationAdapter = MainNationAdapter(requireActivity())
        mSubNationAdapter = SubNationAdapter(requireActivity())
        mServiceAreaAdapter = ServiceAreaAdapter(requireActivity())

        mRequestQueue = Volley.newRequestQueue(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_location, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = requireActivity().resources.getString(R.string.text_setting_location)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Select Nation Spinner
        mSelectNationSpinner = mContainer.fragment_location_select_nation_spinner

        // Service All View
        mServiceAllView = mContainer.fragment_location_service_all_view

        // Main Nation Recycler
        mMainNationRecyclerView = mContainer.fragment_location_main_nation_recycler_view
        mMainNationRecyclerView.setHasFixedSize(true)
        mMainNationRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        mMainNationAdapter.setOnItemClickListener(object : MainNationAdapter.OnItemClickListener {
            override fun onClick(data: LocationAreaMainData, position: Int) {
                if (null == data) {
                    return
                }

                mMainNationUid = data.getCategoryUid()
                mMainNationName = data.getCategoryName()

                mMainNationAdapter.setLocationAreaMainData(position)

                setSubNationData(data.getLocationAreaSubListData())
            }
        })
        mMainNationRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMainNationRecyclerView.adapter = mMainNationAdapter

        // Main Nation Recycler
        mSubNationRecyclerView = mContainer.fragment_location_sub_nation_recycler_view
        mSubNationRecyclerView.setHasFixedSize(true)
        mSubNationRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        mSubNationAdapter.setOnSubNationClickListener(object : SubNationAdapter.OnSubNationClickListener {
            override fun onClick(data: LocationAreaSubData, position: Int) {
                mSubNationUid = data.getCategoryUid()
                mSubNationName = data.getCategoryName()

                mSubNationAdapter.setLocationAreaSubData(position)

                if (MAPP.IS_NON_MEMBER_SERVICE) {
                    setSelectNation(data)
                } else {
                    getMyLocationSetting(data, null, false)
                }
            }
        })
        mSubNationRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSubNationRecyclerView.adapter = mSubNationAdapter

        // Setting Current Location View
        mSettingCurrentLocationView = mContainer.fragment_location_setting_current_location_view
        mSettingCurrentLocationView.setOnClickListener {
            getDefaultSettingArea()
        }
        
        // Service All Text View
        mServiceAllTextView = mContainer.fragment_location_service_all_text_view
        mServiceAllTextView.setOnClickListener {
            setTabView(SELECT_SERVICE_ALL)
        }
        
        // Service All Under Line View
        mServiceAllUnderLineView = mContainer.fragment_location_service_all_under_line_view

        // Service Area Text View
        mServiceAreaTextView = mContainer.fragment_location_service_area_text_view
        mServiceAreaTextView.setOnClickListener {
            setTabView(SELECT_SERVICE_AREA)
        }

        // Service Area Under Line View
        mServiceAreaUnderLineView = mContainer.fragment_location_service_area_under_line_view

        // Service Area View
        mServiceAreaView = mContainer.fragment_location_service_area_view

        // Service Area Recycler View
        mServiceAreaRecyclerView = mContainer.fragment_location_service_area_recycler_view
        mServiceAreaRecyclerView.setHasFixedSize(true)
        mServiceAreaRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        mServiceAreaAdapter.setOnItemClickListener(object : ServiceAreaAdapter.OnItemClickListener {

            override fun onClick(data: ServiceAreaLocationData, position: Int) {

                mServiceAreaAdapter.setServiceAreaLocationData(position)

                mMainNationUid = data.getParentCategoryUid()
                mMainNationName = data.getParentCategoryName()

                mSubNationUid = data.getCategoryUid()
                mSubNationName = data.getCategoryName()

                if (MAPP.IS_NON_MEMBER_SERVICE) {
//                    val location = "$mMainNationName - $mSubNationName"
                    val location = mSubNationName

                    SharedPreferencesUtil().setSaveNationCode(requireActivity(), mNationUid)
                    SharedPreferencesUtil().setSaveMainAreaCode(requireActivity(), mMainNationUid)
                    SharedPreferencesUtil().setSaveSubAreaCode(requireActivity(), mSubNationUid)

                    SharedPreferencesUtil().setSaveLocation(requireActivity(),
                        "${data.getCategoryLatitude()},${data.getCategoryLongitude()}")

                    mIsDefault = true
                    getLocationNation()

                    val activity: Activity = requireActivity()
                    if (activity is MainActivity) {
                        val mainActivity: MainActivity = activity
                        mainActivity.displayCurrentLocation(location)

                        SharedPreferencesUtil().setLocationName(requireActivity(), location)

                        showSuccessChangeLocationCustomPopup()
                    }
                } else {
                    getMyLocationSetting(null, data, true)
                }
            }
        })
        mServiceAreaRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mServiceAreaRecyclerView.adapter = mServiceAreaAdapter
        
        
        

        getLocationNation()




        NagajaLog().d("wooks, getSaveLocation = ${SharedPreferencesUtil().getSaveLocation(requireActivity())}")
        NagajaLog().d("wooks, getSaveNationCode = ${SharedPreferencesUtil().getSaveNationCode(requireActivity())}")
        NagajaLog().d("wooks, getSaveMainAreaCode = ${SharedPreferencesUtil().getSaveMainAreaCode(requireActivity())}")
        NagajaLog().d("wooks, getSaveSubAreaCode = ${SharedPreferencesUtil().getSaveSubAreaCode(requireActivity())}")

    }

    private fun setTabView(selectType: Int) {

        when (selectType) {
            SELECT_SERVICE_ALL -> {
                mServiceAllTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))
                mServiceAllTextView.setTypeface(mServiceAllTextView.typeface, Typeface.BOLD)
                mServiceAllUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))

                mServiceAreaTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_bbbbbb))
                mServiceAreaTextView.setTypeface(mServiceAreaTextView.typeface, Typeface.NORMAL)
                mServiceAreaUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_e2e7ee))

                mIsSelectServiceAll = true

                mServiceAllView.visibility = View.VISIBLE
                mServiceAreaView.visibility = View.GONE
            }

            SELECT_SERVICE_AREA -> {
                mServiceAllTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_color_bbbbbb))
                mServiceAllTextView.setTypeface(mServiceAllTextView.typeface, Typeface.NORMAL)
                mServiceAllUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_e2e7ee))

                mServiceAreaTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))
                mServiceAreaTextView.setTypeface(mServiceAreaTextView.typeface, Typeface.BOLD)
                mServiceAreaUnderLineView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bg_color_0d4d97))

                mIsSelectServiceAll = false

                mServiceAllView.visibility = View.GONE
                mServiceAreaView.visibility = View.VISIBLE

            }
        }
    }


    private fun setSelectNation(data: LocationAreaSubData) {

        if (TextUtils.isEmpty(mMainNationName)) {
//            showToast(requireActivity(), "xxxxxxxxxxxxxxxxxxxxxxxxxx")
        }

        if (mMainNationUid == 0) {
            mMainNationUid = SharedPreferencesUtil().getSaveMainAreaCode(requireActivity())
        }

//        val location = "$mMainNationName - $mSubNationName"
        val location = mSubNationName

        SharedPreferencesUtil().setSaveNationCode(requireActivity(), mNationUid)
        SharedPreferencesUtil().setSaveMainAreaCode(requireActivity(), mMainNationUid)
        SharedPreferencesUtil().setSaveSubAreaCode(requireActivity(), mSubNationUid)

        SharedPreferencesUtil().setSaveLocation(requireActivity(), "${data.getCategoryLatitude()},${data.getCategoryLongitude()}")

        val activity: Activity = requireActivity()
        if (activity is MainActivity) {
            val mainActivity: MainActivity = activity
            mainActivity.displayCurrentLocation(location)

            SharedPreferencesUtil().setLocationName(requireActivity(), location)

            showSuccessChangeLocationCustomPopup()
        }

        displayServiceAreaLocation()
    }

    private fun setSubNationData(listData: ArrayList<LocationAreaSubData>) {

        for (i in listData.indices) {
            if (SharedPreferencesUtil().getSaveSubAreaCode(requireActivity()) == listData[i].getCategoryUid()) {
                listData[i].setIsSelect(true)
            } else {
                listData[i].setIsSelect(false)
            }
        }

        mSubNationAdapter.setData(listData)
    }

    private fun getLocation(): String {
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

            SharedPreferencesUtil().setSaveLocation(requireActivity(), "$latitude,$longitude")
            return "$latitude,$longitude"
        }
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())

        (requireActivity() as Activity).runOnUiThread {
            try {
                val addressList = geocoder.getFromLocation(lan, lon, 1)
                if (addressList!!.size != 0) {
                    address = addressList[0].getAddressLine(0)
                    if (address.substring(0, 4) == "대한민국") {
                        address = address.substring(5)
                    }
                }

                val nation = addressList!![0].countryName
                val adminArea = addressList[0].adminArea
                val locality = addressList[0].locality
                val subLocality = addressList[0].subLocality
                val thoroughfare = addressList[0].thoroughfare
                val subThoroughfare = addressList[0].subThoroughfare
                val featureName = addressList[0].featureName
                val zipCode = addressList[0].postalCode
                val address = addressList[0].getAddressLine(0).toString()

                NagajaLog().d("wooks, Nation (국가명) = $nation")
                NagajaLog().d("wooks, AdminArea (시) = $adminArea")
                NagajaLog().d("wooks, Locality (구 메인) = $locality")
                NagajaLog().d("wooks, SubLocality (구 서브) = $subLocality")
                NagajaLog().d("wooks, Thoroughfare (동) = $thoroughfare")
                NagajaLog().d("wooks, SubThoroughfare (번지) = $subThoroughfare")
                NagajaLog().d("wooks, FeatureName (번지) = $featureName")
                NagajaLog().d("wooks, Zip Code (우편번호) = $zipCode")
                NagajaLog().d("wooks, Address (국가명 시 군 구 동 번지) = $address")

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return address
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun displayNation() {
        val iconSpinnerItems: MutableList<IconSpinnerItem> = ArrayList()
        for (i in mNationListData.indices) {
            if (mNationListData[i].getNationUid() == 1) {
                iconSpinnerItems.add(IconSpinnerItem(mNationListData[i].getLocationName(), requireActivity().resources.getDrawable(R.drawable.flag_english)))
            }

            if (mNationListData[i].getNationUid() == 82) {
                iconSpinnerItems.add(IconSpinnerItem(mNationListData[i].getLocationName(), requireActivity().resources.getDrawable(R.drawable.flag_korean)))
            }

            if (mNationListData[i].getNationUid() == 63) {
                iconSpinnerItems.add(IconSpinnerItem(mNationListData[i].getLocationName(), requireActivity().resources.getDrawable(R.drawable.flag_filipino)))
            }
        }


        val iconSpinnerAdapter = IconSpinnerAdapter(mSelectNationSpinner)
        mSelectNationSpinner.setSpinnerAdapter(iconSpinnerAdapter)
        mSelectNationSpinner.setItems(iconSpinnerItems)
        mSelectNationSpinner.lifecycleOwner = requireActivity()
        mSelectNationSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<IconSpinnerItem?> { oldIndex, oldItem, newIndex, newItem ->

            mSelectNationPosition = newIndex

            for (i in mNationListData.indices) {
                if (i == mSelectNationPosition) {
                    mNationUid = mNationListData[mSelectNationPosition].getNationUid()
                    break
                }
            }

            if (!mIsDefault) {
                getLocationArea()

                mSubNationAdapter.clear()
            }
        })

        getLocationArea()

    }

    private fun setDefaultSetting() {

//        NagajaLog().e("wooks, SharedPreferencesUtil().getSaveNationCode(requireActivity()) = ${SharedPreferencesUtil().getSaveNationCode(requireActivity())}")
//
//        if (SharedPreferencesUtil().getSaveNationCode(requireActivity()) == 1) {
//            mSpinnerSelectionIndex = 0
//        } else if (SharedPreferencesUtil().getSaveNationCode(requireActivity()) == 82) {
//            mSpinnerSelectionIndex = 1
//        } else if (SharedPreferencesUtil().getSaveNationCode(requireActivity()) == 63) {
//            mSpinnerSelectionIndex = 2
//        } else {
//            mSpinnerSelectionIndex = 2
//        }

        mSelectNationSpinner.selectItemByIndex(mSpinnerSelectionIndex)

        var selectDefaultPosition = 0

        for (i in mNationListData.indices) {
            if (SharedPreferencesUtil().getSaveNationCode(requireActivity()) == mNationListData[i].getNationUid()) {
                selectDefaultPosition = i
                mLocationUid = mNationListData[i].getLocationUid()
                break
            }
        }

        mSelectNationSpinner.selectItemByIndex(selectDefaultPosition)

        if (mLocationAreaMainListData.isNotEmpty()) {
            var position = 0
            for (i in mLocationAreaMainListData.indices) {
                if (SharedPreferencesUtil().getSaveMainAreaCode(requireActivity()) == mLocationAreaMainListData[i].getCategoryUid()) {
                    position = i
                    mMainNationAdapter.setLocationAreaMainData(i)
                }
            }

            for (i in mLocationAreaMainListData[position].getLocationAreaSubListData().indices) {
                if (SharedPreferencesUtil().getSaveSubAreaCode(requireActivity()) == mLocationAreaMainListData[position].getLocationAreaSubListData()[i].getCategoryUid()) {
                    mSubNationAdapter.setData(mLocationAreaMainListData[position].getLocationAreaSubListData())

                    if (TextUtils.isEmpty(mMainNationName)) {
                        mMainNationName = mLocationAreaMainListData[position].getCategoryName()
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        mSubNationAdapter.setLocationAreaSubData(i)
                        mSubNationRecyclerView.scrollToPosition(i)
                    }, 100)
                    break
                }
            }

            mIsDefault = false
        }

        getServiceAreaLocation()
    }

    private fun displayServiceAreaLocation() {
        for (i in mServiceAreaLocationData.indices) {
            if (SharedPreferencesUtil().getSaveSubAreaCode(requireActivity()) == mServiceAreaLocationData[i].getCategoryUid()) {
                mServiceAreaLocationData[i].setIsSelect(true)
            } else {
                mServiceAreaLocationData[i].setIsSelect(false)
            }
        }

        mServiceAreaAdapter.setData(mServiceAreaLocationData)
    }

    private fun showSuccessChangeLocationCustomPopup() {
        val customDialog = CustomDialog(
            requireActivity(),
            DIALOG_TYPE_NO_CANCEL,
            requireActivity().resources.getString(R.string.text_noti),
            requireActivity().resources.getString(R.string.text_message_change_my_location),
            "",
            requireActivity().resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    fun spinnerCheck(): Boolean {
        var isSpinnerShow = false

        if (mSelectNationSpinner.isShowing) {
            mSelectNationSpinner.dismiss()

            isSpinnerShow = true
        }

        return isSpinnerShow
    }

    override fun onPause() {
        super.onPause()

        if (mSelectNationSpinner.isShowing) {
            mSelectNationSpinner.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        mIsDefault = true

        NagajaLog().e("wooks, Location Fragment onResume()")
        init()
    }

    // =================================================================================
    // API
    // =================================================================================


    /**
     * API. Get Location Name
     */
    private fun getLocationName(location: String) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLocationName(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    SharedPreferencesUtil().setLocationName(requireActivity(), resultData)

                    val activity: Activity = requireActivity()
                    if (activity is MainActivity) {
                        val mainActivity: MainActivity = activity
                        mainActivity.displayCurrentLocation(resultData)
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            SharedPreferencesUtil().getSelectLanguage(requireActivity())!!,
            location
        )
    }

    /**
     * API. Get Location Nation
     */
    private fun getLocationNation() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLocationNation(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<LocationNationData>> {
                override fun onSuccess(resultData: ArrayList<LocationNationData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    for (i in resultData.indices) {
                        NagajaLog().e("wooks, Nation = ${resultData[i].getLocationName()}")
                    }

                    mNationListData.clear()
                    mNationListData = resultData

                    displayNation()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Location Area
     */
    private fun getLocationArea() {

        var selectNation = 0
        if (mIsDefault) {
            if (SharedPreferencesUtil().getSaveNationCode(requireActivity()) == 1) {
                selectNation = 1
            } else if (SharedPreferencesUtil().getSaveNationCode(requireActivity()) == 82) {
                selectNation = 2
            } else if (SharedPreferencesUtil().getSaveNationCode(requireActivity()) == 63) {
                selectNation = 3
            } else {
                selectNation = 3
            }
        } else {
            selectNation = mNationListData[mSelectNationPosition].getLocationUid()
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLocationArea(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<LocationAreaMainData>> {
                override fun onSuccess(resultData: ArrayList<LocationAreaMainData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    mLocationAreaMainListData = resultData
                    mMainNationAdapter.setData(resultData)

                    if (mIsDefault) {
                        setDefaultSetting()
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            selectNation.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    private fun getDefaultSettingArea() {

        var latitude = ""
        var longitude = ""

        if (TextUtils.isEmpty(getLocation())) {
            when (SharedPreferencesUtil().getSelectLanguage(requireActivity())) {
                SELECT_LANGUAGE_EN -> {
                    latitude = CURRENT_LOCATION_EN.substring(0, CURRENT_LOCATION_EN.indexOf(","))
                    longitude = CURRENT_LOCATION_EN.substring(CURRENT_LOCATION_EN.indexOf(",") + 1, CURRENT_LOCATION_EN.length)

                    MAPP.USER_LOCATION = CURRENT_LOCATION_EN
                }

                SELECT_LANGUAGE_KO -> {
                    latitude = CURRENT_LOCATION_KO.substring(0, CURRENT_LOCATION_KO.indexOf(","))
                    longitude = CURRENT_LOCATION_KO.substring(CURRENT_LOCATION_KO.indexOf(",") + 1, CURRENT_LOCATION_KO.length)

                    MAPP.USER_LOCATION = CURRENT_LOCATION_KO
                }

                SELECT_LANGUAGE_FIL -> {
                    latitude = CURRENT_LOCATION_FIL.substring(0, CURRENT_LOCATION_FIL.indexOf(","))
                    longitude = CURRENT_LOCATION_FIL.substring(CURRENT_LOCATION_FIL.indexOf(",") + 1, CURRENT_LOCATION_FIL.length)

                    MAPP.USER_LOCATION = CURRENT_LOCATION_FIL
                }

                SELECT_LANGUAGE_ZH -> {
                    latitude = CURRENT_LOCATION_ZH.substring(0, CURRENT_LOCATION_ZH.indexOf(","))
                    longitude = CURRENT_LOCATION_ZH.substring(CURRENT_LOCATION_ZH.indexOf(",") + 1, CURRENT_LOCATION_ZH.length)

                    MAPP.USER_LOCATION = CURRENT_LOCATION_ZH
                }

                SELECT_LANGUAGE_JA -> {
                    latitude = CURRENT_LOCATION_JA.substring(0, CURRENT_LOCATION_JA.indexOf(","))
                    longitude = CURRENT_LOCATION_JA.substring(CURRENT_LOCATION_JA.indexOf(",") + 1, CURRENT_LOCATION_JA.length)

                    MAPP.USER_LOCATION = CURRENT_LOCATION_JA
                }
            }
            SharedPreferencesUtil().setSaveLocation(requireActivity(), MAPP.USER_LOCATION)
        } else {
            MAPP.USER_LOCATION = getLocation()
            SharedPreferencesUtil().setSaveLocation(requireActivity(), getLocation())

            latitude = MAPP.USER_LOCATION!!.substring(0, MAPP.USER_LOCATION!!.indexOf(","))
            longitude = MAPP.USER_LOCATION!!.substring(MAPP.USER_LOCATION!!.indexOf(",") + 1, MAPP.USER_LOCATION!!.length)
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getDefaultSettingArea(
            mRequestQueue,
            object : NagajaManager.RequestListener<DefaultSettingAreaData> {
                override fun onSuccess(resultData: DefaultSettingAreaData) {
                    if (null == resultData) {
                        return
                    }

                    SharedPreferencesUtil().setSaveLocation(requireActivity(), resultData.getVirtualLatitude().toString() + "," + resultData.getVirtualLongitude().toString())

                    var nationUid = 0
                    if (resultData.getLocationUid() == 1) {
                        nationUid = 1
                    } else if (resultData.getLocationUid() == 2) {
                        nationUid = 82
                    } else if (resultData.getLocationUid() == 3) {
                        nationUid = 63
                    } else {
                        nationUid = 63
                    }
                    SharedPreferencesUtil().setSaveNationCode(requireActivity(), nationUid)

                    SharedPreferencesUtil().setSaveMainAreaCode(requireActivity(), resultData.getCategoryUid())
                    SharedPreferencesUtil().setSaveSubAreaCode(requireActivity(), resultData.getCategoryAreaUid())
//                    SharedPreferencesUtil().setLocationName(requireActivity(), resultData.getCategoryName() + " - " + resultData.getCategoryAreaName())
                    SharedPreferencesUtil().setLocationName(requireActivity(), /*resultData.getCategoryName() + " - " + */resultData.getCategoryAreaName())

                    if (activity is MainActivity) {
                        val mainActivity: MainActivity = activity as MainActivity
                        mainActivity.displayCurrentLocation(SharedPreferencesUtil().getLocationName(requireActivity())!!)
//                        showToast(requireActivity(), requireActivity().resources.getString(R.string.text_message_change_my_location))

                        showSuccessChangeLocationCustomPopup()
                    }

                    if (!mIsDefault) {
                        mIsDefault = true
                    }

                    getLocationNation()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            latitude,
            longitude,
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Service Area Location
     */
    private fun getServiceAreaLocation() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getServiceAreaLocation(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<ServiceAreaLocationData>> {
                override fun onSuccess(resultData: ArrayList<ServiceAreaLocationData>) {

                    if (resultData.isEmpty()) {
                        return
                    }

                    mServiceAreaLocationData = resultData

                    displayServiceAreaLocation()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            mLocationUid,
            SharedPreferencesUtil().getNationPhoneCode(requireActivity()).toString()
        )
    }

    /**
     * API. Get My Location Setting
     */
    private fun getMyLocationSetting(locationAreaData: LocationAreaSubData?, serviceAreaLocationData: ServiceAreaLocationData?, isServiceArea: Boolean) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMyLocationSetting(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (!isServiceArea) {
                        if (null != locationAreaData) {
                            setSelectNation(locationAreaData)
                        }
                    } else {
                        if (null != serviceAreaLocationData) {
//                            val location = "$mMainNationName - $mSubNationName"
                            val location = mSubNationName

                            SharedPreferencesUtil().setSaveNationCode(requireActivity(), mNationUid)
                            SharedPreferencesUtil().setSaveMainAreaCode(requireActivity(), mMainNationUid)
                            SharedPreferencesUtil().setSaveSubAreaCode(requireActivity(), mSubNationUid)

                            SharedPreferencesUtil().setSaveLocation(requireActivity(),
                                "${serviceAreaLocationData.getCategoryLatitude()},${serviceAreaLocationData.getCategoryLongitude()}")

                            mIsDefault = true
                            getLocationNation()

                            val activity: Activity = requireActivity()
                            if (activity is MainActivity) {
                                val mainActivity: MainActivity = activity
                                mainActivity.displayCurrentLocation(location)

                                SharedPreferencesUtil().setLocationName(requireActivity(), location)

                                showSuccessChangeLocationCustomPopup()
                            }
                        }
                    }

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mMainNationUid,
            mSubNationUid
        )
    }

    /**
     * API. Get Save User Location
     */
    /*private fun getSaveUserLocation() {



        val latitude = SharedPreferencesUtil().getSaveLocation(requireActivity())!!.substring(0, SharedPreferencesUtil().getSaveLocation(requireActivity())!!.indexOf(","))
        val longitude = SharedPreferencesUtil().getSaveLocation(requireActivity())!!.substring(SharedPreferencesUtil().getSaveLocation(requireActivity())!!.indexOf(",")
                + 1, SharedPreferencesUtil().getSaveLocation(requireActivity())!!.length)



        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSaveUserLocation(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            mSelectNationPosition,
            SharedPreferencesUtil().getSaveMainAreaCode(requireActivity()),
            SharedPreferencesUtil().getSaveSubAreaCode(requireActivity()),
            latitude,
            longitude,

        )
    }*/
}