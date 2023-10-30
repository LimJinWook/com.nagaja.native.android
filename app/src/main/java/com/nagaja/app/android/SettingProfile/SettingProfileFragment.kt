package com.nagaja.app.android.SettingProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Data.UserData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_missing.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*

class SettingProfileFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mPostalCodeView: View
    private lateinit var mLoadingView: View
    private lateinit var mAddressView: View
    private lateinit var mInternationalAddressView: View

    private lateinit var mProfileImageView: SimpleDraweeView

    private lateinit var mSelectNationSpinner: PowerSpinnerView

    private lateinit var mAddressDetailEditText: EditText
    private lateinit var mInternationalAddressEditText: EditText

    private lateinit var mPhoneNumberTextView: TextView
    private lateinit var mModifyPhoneNumberTextView: TextView
    private lateinit var mPostalCodeTextView: TextView
    private lateinit var mAddressTextView: TextView

    private lateinit var mAddressIconImageView: ImageView

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnSettingProfileFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mAddressCode = 0
    private var mLatitude = ""
    private var mLongitude = ""
    private var mZipCode = ""
    private var mAddress = ""

    private var mIsMapScreen = false

    interface OnSettingProfileFragmentListener {
        fun onBack()
        fun onModifyScreen()
        fun onPhoneAuthScreen()
        fun onMemberWithdrawalScreen()
        fun onAddressScreen(isKorea: Boolean)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        fun newInstance(): SettingProfileFragment {
            val args = Bundle()
            val fragment = SettingProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_setting_profile, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Header Location View
        val headerLocationView = mContainer.layout_header_location_view
        headerLocationView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION)
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
            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_SEARCH)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.visibility = View.GONE
//        headerShareImageView.setOnSingleClickListener {
//        }

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view
        headerNotificationImageView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_NOTIFICATION)
            }
        }

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_modify_personal_information)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onBack()
        }

        // Profile Image View
        mProfileImageView = mContainer.fragment_setting_profile_image_view
        mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + MAPP.USER_DATA.getProfileName()))

        // Setting Profile Text View
        val settingProfileTextView = mContainer.fragment_setting_profile_text_view
        settingProfileTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        settingProfileTextView.setOnClickListener {
            mListener.onModifyScreen()
        }

        // Email Text View
        val emailTextView = mContainer.fragment_setting_profile_email_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getEmailId())) {
            emailTextView.text = MAPP.USER_DATA.getEmailId()
        }

        // Name Text View
        val nameTextView = mContainer.fragment_setting_profile_name_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getName())) {
            nameTextView.text = MAPP.USER_DATA.getName()
        }

        // Phone Number Text View
        mPhoneNumberTextView = mContainer.fragment_setting_profile_phone_number_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getPhoneNumber())) {
            mPhoneNumberTextView.text = MAPP.USER_DATA.getPhoneNumber()
        }

        // My Company Count Text View
        val myCompanyCountTextView = mContainer.fragment_setting_profile_my_company_count_text_view
        myCompanyCountTextView.text = if (MAPP.USER_DATA.getCompanyMemberListData().isEmpty()) "0" else MAPP.USER_DATA.getCompanyMemberListData().size.toString()

        // Phone Number Text View
        mModifyPhoneNumberTextView = mContainer.fragment_setting_profile_modify_phone_number_text_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getPhoneNumber())) {
            mModifyPhoneNumberTextView.text = MAPP.USER_DATA.getPhoneNumber()
        }

        // Phone Number Modify Text View
        val phoneNumberModifyTextView = mContainer.fragment_setting_profile_phone_number_modify_text_view
        phoneNumberModifyTextView.setOnClickListener {
            mListener.onPhoneAuthScreen()
        }

        // Select Nation Spinner
        mSelectNationSpinner = mContainer.fragment_setting_profile_select_nation_spinner
//        mSelectNationSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
//            mAddressCode = newIndex
//            mPostalCodeView.visibility = if (mAddressCode == 2) View.GONE else View.VISIBLE
//        })
        mSelectNationSpinner.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_domestic_korea), iconRes = R.drawable.flag_kr),
                    IconSpinnerItem(text = mActivity.resources.getString(R.string.text_international), iconRes = R.drawable.ic_default)
                ))
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
            selectItemByIndex(0)
            lifecycleOwner = this@SettingProfileFragment
        }
        mSelectNationSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<IconSpinnerItem?> { oldIndex, oldItem, newIndex, newItem ->
            mAddressCode = newIndex

            if (mAddressCode == 0) {
                mPostalCodeView.visibility = View.VISIBLE
                mAddressIconImageView.visibility = View.GONE

                mAddressView.visibility = View.VISIBLE
                mInternationalAddressView.visibility = View.GONE

            } else {
                mPostalCodeView.visibility = View.GONE
                mAddressIconImageView.visibility = View.VISIBLE

                mAddressView.visibility = View.GONE
                mInternationalAddressView.visibility = View.VISIBLE
            }
        })

        // Postal Code View
        mPostalCodeView = mContainer.fragment_setting_profile_postal_code_view

        // Postal Code Text View
        mPostalCodeTextView = mContainer.fragment_setting_profile_postal_code_text_view
        mPostalCodeTextView.setOnClickListener {
            mListener.onAddressScreen(true)
        }

        // Post Code Search Text View
        val postCodeSearchTextView = mContainer.fragment_setting_profile_post_code_search_text_view
        postCodeSearchTextView.setOnClickListener {
            mListener.onAddressScreen(true)
        }

        // Address View
        mAddressView = mContainer.fragment_setting_profile_address_view

        // Address Text View
        mAddressTextView = mContainer.fragment_setting_profile_address_text_view
        mAddressTextView.setOnClickListener {
            if (mAddressCode == 0) {
                mListener.onAddressScreen(true)
            } else {
                mListener.onAddressScreen(false)
            }
        }

        // International Address View
        mInternationalAddressView = mContainer.fragment_setting_profile_international_address_text

        // International Address Edit Text
        mInternationalAddressEditText = mContainer.fragment_setting_profile_international_address_edit_text

        // Address Detail Edit Text
        mAddressDetailEditText = mContainer.fragment_setting_profile_address_detail_edit_text

        // Address Icon Image View
        mAddressIconImageView = mContainer.fragment_setting_profile_address_image_view
        mAddressIconImageView.setOnClickListener {
            mListener.onAddressScreen(false)
        }

        // Change Address Text View
        val changeAddressTextView = mContainer.fragment_setting_profile_change_address_text_view
        changeAddressTextView.setOnClickListener {
            if (TextUtils.isEmpty(mAddressTextView.text.toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_address_detail))
                return@setOnClickListener
            }

            if (mAddressCode == 1) {
                if (TextUtils.isEmpty(mZipCode) && TextUtils.isEmpty(mLatitude) && TextUtils.isEmpty(mLongitude)) {
                    if (!TextUtils.isEmpty(mInternationalAddressEditText.text)) {
                        mLatitude = geoCoding(mInternationalAddressEditText.text.toString() + " " + mAddressDetailEditText.text.toString()).latitude.toString()
                        mLongitude = geoCoding(mInternationalAddressEditText.text.toString() + " " + mAddressDetailEditText.text.toString()).longitude.toString()
                    }
                }
            }


            getChangeAddress()
        }





        // Member Withdrawal Text View
        val memberWithdrawalTextView = mContainer.fragment_setting_profile_member_withdrawal_text_view
        memberWithdrawalTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        memberWithdrawalTextView.setOnClickListener {
            mListener.onMemberWithdrawalScreen()
        }

        setAddressView()





        // Loading View
        mLoadingView = mContainer.fragment_setting_profile_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_setting_profile_loading_lottie_view
    }

    private fun setAddressView() {
        if (MAPP.USER_DATA.getMemberLocationUid() == 82) {

            mSelectNationSpinner.selectItemByIndex(0)
            mPostalCodeView.visibility = View.VISIBLE

            if (!TextUtils.isEmpty(MAPP.USER_DATA.getAddressCode())) {
                mPostalCodeTextView.text = MAPP.USER_DATA.getAddressCode()
            }

            if (!TextUtils.isEmpty(MAPP.USER_DATA.getAddress())) {
                mAddressTextView.text = MAPP.USER_DATA.getAddress()
            }

            if (!TextUtils.isEmpty(MAPP.USER_DATA.getAddressDetail())) {
                mAddressDetailEditText.setText(MAPP.USER_DATA.getAddressDetail())
            }

        } else {
            mSelectNationSpinner.selectItemByIndex(1)
            mPostalCodeView.visibility = View.GONE

            if (!TextUtils.isEmpty(MAPP.USER_DATA.getAddress())) {
                mInternationalAddressEditText.setText(MAPP.USER_DATA.getAddress())
            }

            if (!TextUtils.isEmpty(MAPP.USER_DATA.getAddressDetail())) {
                mAddressDetailEditText.setText(MAPP.USER_DATA.getAddressDetail())
            }
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

//                val nation = addressList!![0].countryName
//                val adminArea = addressList[0].adminArea
//                val locality = addressList[0].locality
//                val subLocality = addressList[0].subLocality
//                val thoroughfare = addressList[0].thoroughfare
//                val subThoroughfare = addressList[0].subThoroughfare
//                val featureName = addressList[0].featureName
                val zipCode = addressList[0].postalCode
//                val address = addressList[0].getAddressLine(0).toString()

//                NagajaLog().d("wooks, Nation (국가명) = $nation")
//                NagajaLog().d("wooks, AdminArea (시) = $adminArea")
//                NagajaLog().d("wooks, Locality (구 메인) = $locality")
//                NagajaLog().d("wooks, SubLocality (구 서브) = $subLocality")
//                NagajaLog().d("wooks, Thoroughfare (동) = $thoroughfare")
//                NagajaLog().d("wooks, SubThoroughfare (번지) = $subThoroughfare")
//                NagajaLog().d("wooks, FeatureName (번지) = $featureName")
                NagajaLog().d("wooks, Zip Code (우편번호) = $zipCode")
//                NagajaLog().d("wooks, Address (국가명 시 군 구 동 번지) = $address")

                address = addressList[0].postalCode

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return address
    }

    private fun geoCoding(address: String): Location {
        return try {
            Geocoder(mActivity, Locale.US).getFromLocationName(address, 1)?.let{
                Location("").apply {
                    latitude =  it[0].latitude
                    longitude = it[0].longitude
                }
            }?: Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        } catch (e:Exception) {
            e.printStackTrace()
            geoCoding(address)
        }
    }

    fun successPhoneNumberAuth(phoneNumber: String) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            MAPP.USER_DATA.setPhoneNumber(phoneNumber)
            mPhoneNumberTextView.text = phoneNumber
            mModifyPhoneNumberTextView.text = phoneNumber
        }
    }

    fun updateProfileImage() {
        if (null != mProfileImageView) {
            if (!TextUtils.isEmpty(MAPP.USER_DATA.getProfileName())) {
                mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + MAPP.USER_DATA.getProfileName()))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getAddress(addressData: AddressData) {

        NagajaLog().d("wooks, addressData = ${addressData.getAddress()}")
        NagajaLog().d("wooks, addressData zipCode= ${addressData.getPostalCode()}")
        NagajaLog().d("wooks, addressData getLatitude= ${addressData.getLatitude()}")
        NagajaLog().d("wooks, addressData getLongitude= ${addressData.getLongitude()}")
        
        if (addressData.getLatitude() > 0.0) {
            mLatitude = addressData.getLatitude().toString()
        } else {
            mLatitude = geoCoding(addressData.getAddress()!!).latitude.toString()
        }

        if (addressData.getLongitude() > 0.0) {
            mLongitude = addressData.getLongitude().toString()
        } else {
            mLongitude = geoCoding(addressData.getAddress()!!).longitude.toString()
        }

        if (!TextUtils.isEmpty(addressData.getPostalCode()!!)) {
            mZipCode = addressData.getPostalCode()!!
        } else {
            if (/*TextUtils.isEmpty(addressData.getPostalCode()) && */(!TextUtils.isEmpty(addressData.getLatitude().toString()) && !TextUtils.isEmpty(addressData.getLongitude().toString()))) {
                mZipCode = getAddress(addressData.getLatitude(), addressData.getLongitude())
            }
        }

        if (!TextUtils.isEmpty(addressData.getPostalCode())) {
            mPostalCodeTextView.text = addressData.getPostalCode()
        }

        if (!TextUtils.isEmpty(addressData.getAddress())) {
            mAddress = addressData.getAddress() + " "+ addressData.getExtAddress()

            if (mAddressCode == 0) {
                mAddressTextView.text = mAddress
            } else {
                mInternationalAddressEditText.setText(mAddress)
            }

            mAddressDetailEditText.setText("")
        }
    }

    private fun setLoadingView(isLoading: Boolean) {
        if (isLoading) {
            mLoadingView.visibility = View.VISIBLE
            mLoadingLottieView.speed = 2f
            mLoadingLottieView.playAnimation()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                mLoadingView.visibility = View.GONE
                mLoadingLottieView.pauseAnimation()
            }, 500)
        }
    }

    private fun showLoginGuideCustomDialog() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_noti),
            mActivity.resources.getString(R.string.text_message_login_guide),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                mListener.onDefaultLoginScreen()
            }
        })
        customDialog.show()
    }

    override fun onPause() {
        super.onPause()
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
        if (context is OnSettingProfileFragmentListener) {
            mActivity = context as Activity

            if (context is OnSettingProfileFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnSettingProfileFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnSettingProfileFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnSettingProfileFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // ==============================================================================================================================
    // API
    // ==============================================================================================================================

    /**
     * Get Board List
     * */
    private fun getChangeAddress() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getChangeAddress(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    getSecureKeyLogin()

                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_change_address))
                    mListener.onBack()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            if (mAddressCode == 0) "82" else "63",
            mLatitude,
            mLongitude,
            mZipCode,
            mAddress,
            mAddressDetailEditText.text.toString()
        )
    }

    /**
     * API. getSecureKeyLogin
     */
    private fun getSecureKeyLogin() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getSecureKeyLogin(
            mRequestQueue,
            object : NagajaManager.RequestListener<UserData> {
                override fun onSuccess(resultData: UserData) {

                    SharedPreferencesUtil().setSecureKey(mActivity, resultData.getSecureKey())
                    SharedPreferencesUtil().setRefreshKey(mActivity, resultData.getRefreshKey())

                    MAPP.USER_DATA = resultData

                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_change_address))

                    mListener.onBack()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    showToast(mActivity, mActivity.resources.getString(R.string.text_please_try_again_later))
                }

                override fun onFail(errorCode: Int?) {
                }
            },
            MAPP.USER_DATA.getSecureKey()
        )
    }
}