package com.nagaja.app.android.ChangeLocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.*
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
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Main.LocationFragment
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.Splash.SplashFragment
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_company_job.view.*
import kotlinx.android.synthetic.main.fragment_location.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ChangeLocationFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

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

    private lateinit var mListener: OnChangeLocationFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    interface OnChangeLocationFragmentListener {
        fun onFinish()
        fun onSuccessChangeLocation()
    }

    companion object {

        const val SELECT_SERVICE_ALL                  = 0x01
        const val SELECT_SERVICE_AREA                 = 0x02

        fun newInstance(): ChangeLocationFragment {
            val args = Bundle()
            val fragment = ChangeLocationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainNationAdapter = MainNationAdapter(mActivity)
        mSubNationAdapter = SubNationAdapter(mActivity)
        mServiceAreaAdapter = ServiceAreaAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_setting_location)

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
        mMainNationRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

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
        mSubNationRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

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
            setTabView(LocationFragment.SELECT_SERVICE_ALL)
        }

        // Service All Under Line View
        mServiceAllUnderLineView = mContainer.fragment_location_service_all_under_line_view

        // Service Area Text View
        mServiceAreaTextView = mContainer.fragment_location_service_area_text_view
        mServiceAreaTextView.setOnClickListener {
            setTabView(LocationFragment.SELECT_SERVICE_AREA)
        }

        // Service Area Under Line View
        mServiceAreaUnderLineView = mContainer.fragment_location_service_area_under_line_view

        // Service Area View
        mServiceAreaView = mContainer.fragment_location_service_area_view

        // Service Area Recycler View
        mServiceAreaRecyclerView = mContainer.fragment_location_service_area_recycler_view
        mServiceAreaRecyclerView.setHasFixedSize(true)
        mServiceAreaRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

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

                    SharedPreferencesUtil().setSaveNationCode(mActivity, mNationUid)
                    SharedPreferencesUtil().setSaveMainAreaCode(mActivity, mMainNationUid)
                    SharedPreferencesUtil().setSaveSubAreaCode(mActivity, mSubNationUid)

                    SharedPreferencesUtil().setSaveLocation(mActivity, "${data.getCategoryLatitude()},${data.getCategoryLongitude()}")



                    SharedPreferencesUtil().setLocationName(mActivity, location)
                    MAPP.SELECT_NATION_NAME = location

                    showSuccessChangeLocationCustomPopup()
                } else {
                    getMyLocationSetting(null, data, true)
                }
            }
        })
        mServiceAreaRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mServiceAreaRecyclerView.adapter = mServiceAreaAdapter




        getLocationNation()




        NagajaLog().d("wooks, getSaveLocation = ${SharedPreferencesUtil().getSaveLocation(mActivity)}")
        NagajaLog().d("wooks, getSaveNationCode = ${SharedPreferencesUtil().getSaveNationCode(mActivity)}")
        NagajaLog().d("wooks, getSaveMainAreaCode = ${SharedPreferencesUtil().getSaveMainAreaCode(mActivity)}")
        NagajaLog().d("wooks, getSaveSubAreaCode = ${SharedPreferencesUtil().getSaveSubAreaCode(mActivity)}")

    }

    private fun setTabView(selectType: Int) {

        when (selectType) {
            LocationFragment.SELECT_SERVICE_ALL -> {
                mServiceAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mServiceAllTextView.setTypeface(mServiceAllTextView.typeface, Typeface.BOLD)
                mServiceAllUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mServiceAreaTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mServiceAreaTextView.setTypeface(mServiceAreaTextView.typeface, Typeface.NORMAL)
                mServiceAreaUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mIsSelectServiceAll = true

                mServiceAllView.visibility = View.VISIBLE
                mServiceAreaView.visibility = View.GONE
            }

            LocationFragment.SELECT_SERVICE_AREA -> {
                mServiceAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mServiceAllTextView.setTypeface(mServiceAllTextView.typeface, Typeface.NORMAL)
                mServiceAllUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mServiceAreaTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mServiceAreaTextView.setTypeface(mServiceAreaTextView.typeface, Typeface.BOLD)
                mServiceAreaUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mIsSelectServiceAll = false

                mServiceAllView.visibility = View.GONE
                mServiceAreaView.visibility = View.VISIBLE

            }
        }
    }


    private fun setSelectNation(data: LocationAreaSubData) {

        if (mMainNationUid == 0) {
            mMainNationUid = SharedPreferencesUtil().getSaveMainAreaCode(mActivity)
        }

//        val location = "$mMainNationName - $mSubNationName"
        val location = mSubNationName

        SharedPreferencesUtil().setSaveNationCode(mActivity, mNationUid)
        SharedPreferencesUtil().setSaveMainAreaCode(mActivity, mMainNationUid)
        SharedPreferencesUtil().setSaveSubAreaCode(mActivity, mSubNationUid)

        SharedPreferencesUtil().setSaveLocation(mActivity, "${data.getCategoryLatitude()},${data.getCategoryLongitude()}")



        SharedPreferencesUtil().setLocationName(mActivity, location)
        MAPP.SELECT_NATION_NAME = location

        showSuccessChangeLocationCustomPopup()

//        displayServiceAreaLocation()
    }

    private fun setSubNationData(listData: ArrayList<LocationAreaSubData>) {

        for (i in listData.indices) {
            if (SharedPreferencesUtil().getSaveSubAreaCode(mActivity) == listData[i].getCategoryUid()) {
                listData[i].setIsSelect(true)
            } else {
                listData[i].setIsSelect(false)
            }
        }

        mSubNationAdapter.setData(listData)
    }

    private fun getLocation(): String {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                SplashFragment.PERMISSION_REQUEST_CODE
            )

            return ""
        } else {
            val gpsTracker = GpsTracker(mActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

            SharedPreferencesUtil().setSaveLocation(mActivity, "$latitude,$longitude")
            return "$latitude,$longitude"
        }
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(mActivity, Locale.getDefault())

        (mActivity as Activity).runOnUiThread {
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
                iconSpinnerItems.add(IconSpinnerItem(mNationListData[i].getLocationName(), mActivity.resources.getDrawable(R.drawable.flag_english)))
            }

            if (mNationListData[i].getNationUid() == 82) {
                iconSpinnerItems.add(IconSpinnerItem(mNationListData[i].getLocationName(), mActivity.resources.getDrawable(R.drawable.flag_korean)))
            }

            if (mNationListData[i].getNationUid() == 63) {
                iconSpinnerItems.add(IconSpinnerItem(mNationListData[i].getLocationName(), mActivity.resources.getDrawable(R.drawable.flag_filipino)))
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

//        NagajaLog().e("wooks, SharedPreferencesUtil().getSaveNationCode(mActivity) = ${SharedPreferencesUtil().getSaveNationCode(mActivity)}")
//
//        if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 1) {
//            mSpinnerSelectionIndex = 0
//        } else if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 82) {
//            mSpinnerSelectionIndex = 1
//        } else if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 63) {
//            mSpinnerSelectionIndex = 2
//        } else {
//            mSpinnerSelectionIndex = 2
//        }

        mSelectNationSpinner.selectItemByIndex(mSpinnerSelectionIndex)

        var selectDefaultPosition = 0

        for (i in mNationListData.indices) {
            if (SharedPreferencesUtil().getSaveNationCode(mActivity) == mNationListData[i].getNationUid()) {
                selectDefaultPosition = i
                mLocationUid = mNationListData[i].getLocationUid()
                break
            }
        }

        mSelectNationSpinner.selectItemByIndex(selectDefaultPosition)

        if (mLocationAreaMainListData.isNotEmpty()) {
            var position = 0
            for (i in mLocationAreaMainListData.indices) {
                if (SharedPreferencesUtil().getSaveMainAreaCode(mActivity) == mLocationAreaMainListData[i].getCategoryUid()) {
                    position = i
                    mMainNationAdapter.setLocationAreaMainData(i)
                }
            }

            for (i in mLocationAreaMainListData[position].getLocationAreaSubListData().indices) {
                if (SharedPreferencesUtil().getSaveSubAreaCode(mActivity) == mLocationAreaMainListData[position].getLocationAreaSubListData()[i].getCategoryUid()) {
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
            if (SharedPreferencesUtil().getSaveSubAreaCode(mActivity) == mServiceAreaLocationData[i].getCategoryUid()) {
                mServiceAreaLocationData[i].setIsSelect(true)
            } else {
                mServiceAreaLocationData[i].setIsSelect(false)
            }
        }

        mServiceAreaAdapter.setData(mServiceAreaLocationData)
    }

    private fun showSuccessChangeLocationCustomPopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_noti),
            mActivity.resources.getString(R.string.text_message_change_my_location),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()
                mListener.onSuccessChangeLocation()
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
        if (context is OnChangeLocationFragmentListener) {
            mActivity = context as Activity

            if (context is OnChangeLocationFragmentListener) {
                mListener = context;
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnChangeLocationFragmentListener"
                );
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnChangeLocationFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnChangeLocationFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
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

                    SharedPreferencesUtil().setLocationName(mActivity, resultData)

                    val activity: Activity = mActivity
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
                        disConnectUserToken(mActivity)
                    }
                }
            },
            SharedPreferencesUtil().getSelectLanguage(mActivity)!!,
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
                        disConnectUserToken(mActivity)
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
            if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 1) {
                selectNation = 1
            } else if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 82) {
                selectNation = 2
            } else if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 63) {
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
                        disConnectUserToken(mActivity)
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
            when (SharedPreferencesUtil().getSelectLanguage(mActivity)) {
                SELECT_LANGUAGE_EN -> {
                    latitude = NagajaActivity.CURRENT_LOCATION_EN.substring(0, NagajaActivity.CURRENT_LOCATION_EN.indexOf(","))
                    longitude = NagajaActivity.CURRENT_LOCATION_EN.substring(NagajaActivity.CURRENT_LOCATION_EN.indexOf(",") + 1, NagajaActivity.CURRENT_LOCATION_EN.length)

                    MAPP.USER_LOCATION = NagajaActivity.CURRENT_LOCATION_EN
                }

                SELECT_LANGUAGE_KO -> {
                    latitude = NagajaActivity.CURRENT_LOCATION_KO.substring(0, NagajaActivity.CURRENT_LOCATION_KO.indexOf(","))
                    longitude = NagajaActivity.CURRENT_LOCATION_KO.substring(NagajaActivity.CURRENT_LOCATION_KO.indexOf(",") + 1, NagajaActivity.CURRENT_LOCATION_KO.length)

                    MAPP.USER_LOCATION = NagajaActivity.CURRENT_LOCATION_KO
                }

                SELECT_LANGUAGE_FIL -> {
                    latitude = NagajaActivity.CURRENT_LOCATION_FIL.substring(0, NagajaActivity.CURRENT_LOCATION_FIL.indexOf(","))
                    longitude = NagajaActivity.CURRENT_LOCATION_FIL.substring(NagajaActivity.CURRENT_LOCATION_FIL.indexOf(",") + 1, NagajaActivity.CURRENT_LOCATION_FIL.length)

                    MAPP.USER_LOCATION = NagajaActivity.CURRENT_LOCATION_FIL
                }

                SELECT_LANGUAGE_ZH -> {
                    latitude = NagajaActivity.CURRENT_LOCATION_ZH.substring(0, NagajaActivity.CURRENT_LOCATION_ZH.indexOf(","))
                    longitude = NagajaActivity.CURRENT_LOCATION_ZH.substring(NagajaActivity.CURRENT_LOCATION_ZH.indexOf(",") + 1, NagajaActivity.CURRENT_LOCATION_ZH.length)

                    MAPP.USER_LOCATION = NagajaActivity.CURRENT_LOCATION_ZH
                }

                SELECT_LANGUAGE_JA -> {
                    latitude = NagajaActivity.CURRENT_LOCATION_JA.substring(0, NagajaActivity.CURRENT_LOCATION_JA.indexOf(","))
                    longitude = NagajaActivity.CURRENT_LOCATION_JA.substring(NagajaActivity.CURRENT_LOCATION_JA.indexOf(",") + 1, NagajaActivity.CURRENT_LOCATION_JA.length)

                    MAPP.USER_LOCATION = NagajaActivity.CURRENT_LOCATION_JA
                }
            }
            SharedPreferencesUtil().setSaveLocation(mActivity, MAPP.USER_LOCATION)
        } else {
            MAPP.USER_LOCATION = getLocation()
            SharedPreferencesUtil().setSaveLocation(mActivity, getLocation())

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

                    SharedPreferencesUtil().setSaveLocation(mActivity, resultData.getVirtualLatitude().toString() + "," + resultData.getVirtualLongitude().toString())

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
                    SharedPreferencesUtil().setSaveNationCode(mActivity, nationUid)

                    SharedPreferencesUtil().setSaveMainAreaCode(mActivity, resultData.getCategoryUid())
                    SharedPreferencesUtil().setSaveSubAreaCode(mActivity, resultData.getCategoryAreaUid())
//                    SharedPreferencesUtil().setLocationName(mActivity, resultData.getCategoryName() + " - " + resultData.getCategoryAreaName())
                    SharedPreferencesUtil().setLocationName(mActivity, /*resultData.getCategoryName() + " - " + */resultData.getCategoryAreaName())

//                    if (activity is MainActivity) {
//                        val mainActivity: MainActivity = activity as MainActivity
//                        mainActivity.displayCurrentLocation(SharedPreferencesUtil().getLocationName(mActivity)!!)
//                        showToast(mActivity, mActivity.resources.getString(R.string.text_message_change_my_location))
//                    }

                    showSuccessChangeLocationCustomPopup()

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
                        disConnectUserToken(mActivity)
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
                        disConnectUserToken(mActivity)
                    }
                }
            },
            mLocationUid,
            SharedPreferencesUtil().getNationPhoneCode(mActivity).toString()
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

                            SharedPreferencesUtil().setSaveNationCode(mActivity, mNationUid)
                            SharedPreferencesUtil().setSaveMainAreaCode(mActivity, mMainNationUid)
                            SharedPreferencesUtil().setSaveSubAreaCode(mActivity, mSubNationUid)

                            SharedPreferencesUtil().setSaveLocation(mActivity,
                                "${serviceAreaLocationData.getCategoryLatitude()},${serviceAreaLocationData.getCategoryLongitude()}")

                            mIsDefault = true
                            getLocationNation()

                            SharedPreferencesUtil().setLocationName(mActivity, location)
                            MAPP.SELECT_NATION_NAME = location

                            showSuccessChangeLocationCustomPopup()
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



        val latitude = SharedPreferencesUtil().getSaveLocation(mActivity)!!.substring(0, SharedPreferencesUtil().getSaveLocation(mActivity)!!.indexOf(","))
        val longitude = SharedPreferencesUtil().getSaveLocation(mActivity)!!.substring(SharedPreferencesUtil().getSaveLocation(mActivity)!!.indexOf(",")
                + 1, SharedPreferencesUtil().getSaveLocation(mActivity)!!.length)



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
                        disConnectUserToken(mActivity)
                    }
                }
            },
            mSelectNationPosition,
            SharedPreferencesUtil().getSaveMainAreaCode(mActivity),
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            latitude,
            longitude,

        )
    }*/
}