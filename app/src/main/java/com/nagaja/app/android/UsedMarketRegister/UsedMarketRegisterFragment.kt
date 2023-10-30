package com.nagaja.app.android.UsedMarketRegister

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.ImageDecoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.NativeImageUploadAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.R
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_report.view.*
import kotlinx.android.synthetic.main.fragment_used_market_register.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UsedMarketRegisterFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mCompanyNameView: View

    private lateinit var mCompanyNameTextView: TextView
    private lateinit var mCurrencyDollarTextView: TextView
    private lateinit var mCurrencyWonTextView: TextView
    private lateinit var mCurrencyPesoTextView: TextView
    private lateinit var mCurrencyYuanTextView: TextView
    private lateinit var mCurrencyYenTextView: TextView

    private lateinit var mAreaDescEditText: EditText
    private lateinit var mTitleEditText: EditText
    private lateinit var mPriceEditText: EditText
    private lateinit var mContentEditText: EditText

    private lateinit var mCategorySpinner: PowerSpinnerView
    private lateinit var mMainAreaSpinner: PowerSpinnerView
    private lateinit var mSubAreaSpinner: PowerSpinnerView
    private lateinit var mCurrencySpinner: PowerSpinnerView

    private lateinit var mImageUploadRecyclerView: RecyclerView

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter

    private var mCategoryListData = ArrayList<StoreCategoryData>()
    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()

    private lateinit var mListener: OnUsedMarketRegisterFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mNationListData = ArrayList<LocationNationData>()
    private var mLocationAreaListData = ArrayList<LocationAreaMainData>()
    private var mCurrencyListData = ArrayList<CurrencyData>()

    private val mCategoryNameListData = ArrayList<String>()
    private var mMainAreaNameListData = ArrayList<String>()
    private var mSubAreaNameListData = ArrayList<String>()
    private var mDeleteImageUidListData = ArrayList<String>()

    private var mCategorySelectPosition = -1
    private var mMainAreaSelectPosition = -1
    private var mSubAreaSelectPosition = -1
    private var mCurrencySelectPosition = -1

    private var mCurrencySelectItem = ""
    private var mCurrentPhotoPath = ""
    private var mLatitude = ""
    private var mLongitude = ""


    private var mIsSelectUSD = false
    private var mIsSelectKRW = false
    private var mIsSelectPHP = false
    private var mIsSelectCNY = false
    private var mIsSelectJPY = false

    private lateinit var mUsedMarketData: UsedMarketData

    interface OnUsedMarketRegisterFragmentListener {
        fun onFinish()
        fun onMapScreen()
        fun onGalleryImageSelect()
        fun onCameraImage()
        fun onSuccessRegister()
        fun onSuccessModify()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_MY_USED_MARKET_ITEM_UID                = "args_my_used_market_item_uid"
        const val ARGS_MY_USED_MARKET_COMPANY_UID             = "args_my_used_market_company_uid"

        fun newInstance(itemUid: Int, companyUid: Int): UsedMarketRegisterFragment {
            val args = Bundle()
            val fragment = UsedMarketRegisterFragment()
            args.putInt(ARGS_MY_USED_MARKET_ITEM_UID, itemUid)
            args.putInt(ARGS_MY_USED_MARKET_COMPANY_UID, companyUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getItemUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_MY_USED_MARKET_ITEM_UID) as Int
    }

    private fun getCompanyUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_MY_USED_MARKET_COMPANY_UID) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_used_market_register, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
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
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_used_market_register)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Company Name View
        mCompanyNameView = mContainer.fragment_used_market_register_company_name_view

        // Company Name Text View
        mCompanyNameTextView = mContainer.fragment_used_market_register_company_name_text_view

        // Category Spinner
        mCategorySpinner = mContainer.fragment_used_market_register_category_spinner
//        mCategorySpinner.isFocusableInTouchMode = true
        mCategorySpinner.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                mCategorySpinner.dismiss()
            }
        }
        mCategorySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mCategorySelectPosition = newIndex
        })

        // Main Area Spinner
        mMainAreaSpinner = mContainer.fragment_used_market_register_main_area_spinner
        mMainAreaSpinner.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                mMainAreaSpinner.dismiss()
            }
        }
        mMainAreaSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mMainAreaSelectPosition = newIndex
        })

        // Sub Area Spinner
        mSubAreaSpinner = mContainer.fragment_used_market_register_sub_area_spinner
        mSubAreaSpinner.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                mSubAreaSpinner.dismiss()
            }
        }
        mSubAreaSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSubAreaSelectPosition = newIndex
        })

        // Currency Spinner
        mCurrencySpinner = mContainer.fragment_used_market_register_currency_spinner
        mCurrencySpinner.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                mCurrencySpinner.dismiss()
            }
        }
        mCurrencySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mCurrencySelectPosition = newIndex
            autoSelectCurrency(newItem!!)
        })

        // Area Desc Edit Text
        mAreaDescEditText = mContainer.fragment_used_market_register_area_desc_edit_text
        mAreaDescEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Address Image View
        val addressImageView = mContainer.fragment_used_market_register_address_image_view
        addressImageView.setOnClickListener {
            mListener.onMapScreen()
        }

        // Title Edit Text
        mTitleEditText = mContainer.fragment_used_market_register_title_edit_text
        mTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Price Edit Text
        mPriceEditText = mContainer.fragment_used_market_register_price_edit_text
        mPriceEditText.filters = arrayOf(Util().blankSpaceFilter)
        mPriceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (mPriceEditText.text.length == 1) {
                    if (mPriceEditText.text.toString() == "0") {
                        mPriceEditText.setText("")
                    }
                }
            }
        })

        // Currency Dollar Text View
        mCurrencyDollarTextView = mContainer.fragment_used_market_register_currency_dollar_text_view
        mCurrencyDollarTextView.setOnClickListener {
            mIsSelectUSD = !mIsSelectUSD
            selectAddFeatures(mCurrencyDollarTextView, mIsSelectUSD)
        }

        // Currency Won Text View
        mCurrencyWonTextView = mContainer.fragment_used_market_register_currency_won_text_view
        mCurrencyWonTextView.setOnClickListener {
            mIsSelectKRW = !mIsSelectKRW
            selectAddFeatures(mCurrencyWonTextView, mIsSelectKRW)
        }

        // Currency Peso Text View
        mCurrencyPesoTextView = mContainer.fragment_used_market_register_currency_peso_text_view
        mCurrencyPesoTextView.setOnClickListener {
            mIsSelectPHP = !mIsSelectPHP
            selectAddFeatures(mCurrencyPesoTextView, mIsSelectPHP)
        }

        // Currency Yuan Text View
        mCurrencyYuanTextView = mContainer.fragment_used_market_register_currency_yuan_text_view
        mCurrencyYuanTextView.setOnClickListener {
            mIsSelectCNY = !mIsSelectCNY
            selectAddFeatures(mCurrencyYuanTextView, mIsSelectCNY)
        }

        // Currency Yen Text View
        mCurrencyYenTextView = mContainer.fragment_used_market_register_currency_yen_text_view
        mCurrencyYenTextView.setOnClickListener {
            mIsSelectJPY = !mIsSelectJPY
            selectAddFeatures(mCurrencyYenTextView, mIsSelectJPY)
        }

        // Content Edit Text
        mContentEditText = mContainer.fragment_used_market_register_content_edit_text
        mContentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        mContentEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.fragment_used_market_register_content_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        // Image Upload Recycler View
        mImageUploadRecyclerView = mContainer.fragment_used_market_register_image_upload_recycler_view
        mImageUploadRecyclerView.setHasFixedSize(true)
        mImageUploadRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mNativeImageUploadAdapter.setOnItemClickListener(object : NativeImageUploadAdapter.OnItemClickListener {
            override fun onClick(data: NativeImageUploadData, position: Int) {
                mNativeImageUploadListData.removeAt(position)
                mNativeImageUploadAdapter.setData(mNativeImageUploadListData)

                if (mImageUploadListData.isNotEmpty()) {
                    if (mNativeImageUploadListData.size <= position) {
                        mImageUploadListData.removeAt((position - mNativeImageUploadListData.size))
                    }
                }

                if (getItemUid() != 0) {
                    if (data.getImageUid() != 0) {
                        mDeleteImageUidListData.add(data.getImageUid().toString())
                    }
                }
            }

            override fun onSelectImage() {
                var imageCount = 0
                for (i in 0 until mNativeImageUploadListData.size) {
                    if (mNativeImageUploadListData[i].getViewType() == 0x01) {
                        imageCount++
                    }
                }

                NagajaLog().d("wooks, imageCount = $imageCount")
                if (imageCount > 4) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_image_upload_count_5))
                    return
                }

                showCustomImageUploadDialog()
            }

        })
        mImageUploadRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageUploadRecyclerView.adapter = mNativeImageUploadAdapter

        // Register Text View
        val registerTextView = mContainer.fragment_used_market_register_text_view
        registerTextView.setOnClickListener {
            registerCheck()
        }

        // Loading View
        mLoadingView = mContainer.fragment_used_market_register_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_used_market_register_loading_lottie_view



        setDefaultSelect()
        getStoreCategoryData()

        if (getCompanyUid() > 0) {
            getCompanyDefaultData()
        }
    }

    private fun registerCheck() {
        if (TextUtils.isEmpty(mTitleEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_board_title))
            return
        } else if (TextUtils.isEmpty(mContentEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_contents))
            return
        } else if (mCategorySelectPosition == -1) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_category_type))
            return
        } else if (mCurrencySelectPosition == -1) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_default_currency))
            return
        } else if (TextUtils.isEmpty(mPriceEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_sale_price))
            return
        } else if (TextUtils.isEmpty(mPriceEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_sale_price))
            return
        } else if (mMainAreaSelectPosition == -1) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_main_area))
            return
        } else if (mSubAreaSelectPosition == -1) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_sub_area))
            return
        } else if (TextUtils.isEmpty(mAreaDescEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_input_area_description))
            return
        }

        if (getItemUid() == 0) {
            getUsedMarketRegister()
        } else {
            getUsedMarketRegisterModify()
        }
    }

    private fun setDefaultSelect() {
        val imageUploadData = NativeImageUploadData()
        imageUploadData.setViewType(0x00)

        mNativeImageUploadListData.add(imageUploadData)

        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)
    }

    private fun selectAddFeatures(view: TextView, isSelect: Boolean) {
        if (isSelect) {
            view.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
            view.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_222222))
        } else {
            view.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_222222))
            view.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_color_ffffff))
        }
    }

    private fun setAreaSpinner() {

        for (i in mLocationAreaListData.indices) {
            mMainAreaNameListData.add(mLocationAreaListData[i].getCategoryName())

            if (mLocationAreaListData[i].getCategoryUid() == SharedPreferencesUtil().getSaveMainAreaCode(mActivity)) {
                mMainAreaSelectPosition = i
            }

            if (mLocationAreaListData[i].getCategoryUid() == SharedPreferencesUtil().getSaveMainAreaCode(mActivity)) {
                for (k in mLocationAreaListData[i].getLocationAreaSubListData().indices) {
                    mSubAreaNameListData.add(mLocationAreaListData[i].getLocationAreaSubListData()[k].getCategoryName())

                    if (mLocationAreaListData[i].getLocationAreaSubListData()[k].getCategoryUid() == SharedPreferencesUtil().getSaveSubAreaCode(mActivity)) {
                        mSubAreaSelectPosition = k
                    }
                }
            }
        }

        if (mMainAreaNameListData.size > 0 && mSubAreaNameListData.size > 0) {
            mMainAreaSpinner.setItems(mMainAreaNameListData)
            mMainAreaSpinner.selectItemByIndex(mMainAreaSelectPosition)

            mSubAreaSpinner.setItems(mSubAreaNameListData)
            mSubAreaSpinner.selectItemByIndex(mSubAreaSelectPosition)
        }

        setLoadingView(false)
    }

    private fun autoSelectCurrency(selectCurrency: String) {
        when (selectCurrency) {
            CURRENCY_TYPE_CNY -> {
                mIsSelectCNY = !mIsSelectCNY
                selectAddFeatures(mCurrencyYuanTextView, mIsSelectCNY)
            }

            CURRENCY_TYPE_JPY -> {
                mIsSelectJPY = !mIsSelectJPY
                selectAddFeatures(mCurrencyYenTextView, mIsSelectJPY)
            }

            CURRENCY_TYPE_KRW -> {
                mIsSelectKRW = !mIsSelectKRW
                selectAddFeatures(mCurrencyWonTextView, mIsSelectKRW)
            }

            CURRENCY_TYPE_PHP -> {
                mIsSelectPHP = !mIsSelectPHP
                selectAddFeatures(mCurrencyPesoTextView, mIsSelectPHP)
            }

            CURRENCY_TYPE_USD -> {
                mIsSelectUSD = !mIsSelectUSD
                selectAddFeatures(mCurrencyDollarTextView, mIsSelectUSD)
            }
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
            }, 1000)
        }
    }

    private fun showCustomImageUploadDialog() {
        val customDialogBottom = CustomDialogBottom(mActivity, isImage = true, isProfileModify = false)
        customDialogBottom.setOnCustomDialogBottomListener(object : CustomDialogBottom.OnCustomDialogBottomListener {
            override fun onCamera() {
                mListener.onCameraImage()
                customDialogBottom.dismiss()
            }

            override fun onGallery() {
                mListener.onGalleryImageSelect()
                customDialogBottom.dismiss()
            }

            override fun onSelectFile() {
            }

            override fun onDeleteProfileImage() {
            }

            override fun onCancel() {
                customDialogBottom.dismiss()
            }
        })
        customDialogBottom.show(parentFragmentManager, "Custom Dialog")
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

    private fun setMyMarketDisplay() {

        // Category Set
        for (i in mCategoryListData.indices) {
            if (mCategoryListData[i].getCategoryUid() == mUsedMarketData.getCategoryUid()) {
                mCategorySpinner.selectItemByIndex(i)
                break
            }
        }

        // Location Set
//        for (i in mNationListData.indices) {
//            if () {
//
//            }
//        }

        // Location Detail
        if (!TextUtils.isEmpty(mUsedMarketData.getLocationDetail())) {
            mAreaDescEditText.setText(mUsedMarketData.getLocationDetail())
        }

        // Title
        if (!TextUtils.isEmpty(mUsedMarketData.getItemSubject())) {
            mTitleEditText.setText(mUsedMarketData.getItemSubject())
        }

        // Price
        if (!TextUtils.isEmpty(Util().getTwoDecimalPlaces(mUsedMarketData.getItemPrice()))) {
            mPriceEditText.setText(Util().getTwoDecimalPlaces(mUsedMarketData.getItemPrice()))
        }

        // Currency
        if (!TextUtils.isEmpty(mUsedMarketData.getItemCurrencyCode())) {
            for (i in mCurrencyListData.indices) {
                if (mCurrencyListData[i].getCurrencyCode() == mUsedMarketData.getItemCurrencyCode()) {
                    mCurrencySpinner.selectItemByIndex(i)
                    break
                }
            }
        }

        // Select Currency
        if (mUsedMarketData.getCurrencyListData().size > 0) {
            for (i in mUsedMarketData.getCurrencyListData().indices) {
                when (mUsedMarketData.getCurrencyListData()[i].getCurrencyCode()) {

                    CURRENCY_TYPE_USD -> {
                        mIsSelectUSD = true
                        selectAddFeatures(mCurrencyDollarTextView, mIsSelectUSD)
                    }

                    CURRENCY_TYPE_KRW -> {
                        mIsSelectKRW = true
                        selectAddFeatures(mCurrencyWonTextView, mIsSelectKRW)
                    }

                    CURRENCY_TYPE_PHP -> {
                        mIsSelectPHP = true
                        selectAddFeatures(mCurrencyPesoTextView, mIsSelectPHP)
                    }

                    CURRENCY_TYPE_CNY -> {
                        mIsSelectCNY = true
                        selectAddFeatures(mCurrencyYuanTextView, mIsSelectCNY)
                    }

                    CURRENCY_TYPE_JPY -> {
                        mIsSelectJPY = true
                        selectAddFeatures(mCurrencyYenTextView, mIsSelectJPY)
                    }
                }
            }
        }

        // Content
        if (!TextUtils.isEmpty(mUsedMarketData.getItemContent())) {
            mContentEditText.setText(mUsedMarketData.getItemContent())
        }

        // Item Image List
        if (mUsedMarketData.getImageListData().size > 0) {
            for (i in mUsedMarketData.getImageListData().indices) {
                val nativeImageUploadData = NativeImageUploadData()
                nativeImageUploadData.setViewType(0x01)
                nativeImageUploadData.setIsDeviceImage(false)
                nativeImageUploadData.setImageUrl(mUsedMarketData.getImageListData()[i].getItemImageName())
                nativeImageUploadData.setImageUid(mUsedMarketData.getImageListData()[i].getItemImageUid())

                mNativeImageUploadListData.add(nativeImageUploadData)
            }

            mNativeImageUploadAdapter.setData(mNativeImageUploadListData)
        }

        // Location
        if (!TextUtils.isEmpty(mUsedMarketData.getLatitude()) && !TextUtils.isEmpty(mUsedMarketData.getLatitude())) {
            mLatitude = mUsedMarketData.getLatitude()
            mLongitude = mUsedMarketData.getLongitude()
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = mActivity.contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return Uri.decode(result)
    }

    fun selectMap(address: AddressData) {
        NagajaLog().d("wooks, 1 = ${address.getAddress()}")
        NagajaLog().d("wooks, 1 = ${address.getLatitude()}")
        NagajaLog().d("wooks, 1 = ${address.getLongitude()}")

        if (!TextUtils.isEmpty(address.getAddress())) {
            mAreaDescEditText.setText(address.getAddress())
        }

        if (address.getLatitude() > 0.0) {
            mLatitude = address.getLatitude().toString()
        }

        if (address.getLongitude() > 0.0) {
            mLongitude = address.getLongitude().toString()
        }
    }

    fun spinnerCheck(): Boolean {
        var isSpinnerShow = false

        if (mCategorySpinner.isShowing || mMainAreaSpinner.isShowing || mSubAreaSpinner.isShowing) {
            mCategorySpinner.dismiss()
            mMainAreaSpinner.dismiss()
            mSubAreaSpinner.dismiss()

            isSpinnerShow = true
        }

        return isSpinnerShow
    }

    @SuppressLint("Range")
    fun selectImage(uri: Uri) {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = mActivity.contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }

        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }

//        val bitmap = MediaStore.Images.Media.getBitmap(mActivity.contentResolver, uri)
//        val fileName = Uri.decode(result).substring(0, Uri.decode(result).lastIndexOf(".")) + ".png"

//        NagajaLog().d("wooks, Select File Name = $fileName")
//        NagajaLog().d("wooks, bitmap = $bitmap")




        val imageUploadData = ImageUploadData()
        try {
            var bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(mActivity.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(mActivity.contentResolver, uri)
            }

            imageUploadData.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        NagajaLog().d("wooks, Select File Name = ${Uri.decode(result)}")
        var fileName = Uri.decode(result)
        if (fileName.contains(".heic")) {
            fileName = fileName.replace(".heic", ".png")
        }
        imageUploadData.setFileName(fileName)

        mImageUploadListData.add(imageUploadData)

        val file = File(Util().getPathFromUri(mActivity, uri)!!)
        val fileSize: Long = (file.length() / 1024)
        NagajaLog().d("wooks, Select File Size = $fileSize")

        if (fileSize > 30000) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_30mb_less))
            return
        }

        val nativeImageUploadData = NativeImageUploadData()
        nativeImageUploadData.setViewType(0x01)
        nativeImageUploadData.setIsDeviceImage(true)
        nativeImageUploadData.setImageUri(uri)




        mNativeImageUploadListData.add(nativeImageUploadData)
        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)




//        val imageUploadData = ImageUploadData()
//        imageUploadData.setFileName(Uri.decode(result))
//        if (!isFile) {
//            imageUploadData.setImageBitmap(bitmap!!)
//        }
//        imageUploadData.setFilePath(Util().getPathFromUri(this@MainActivity, uri)!!)
//
//        getUploadImageFile(imageUploadData, isFile, uri)


    }

    @SuppressLint("SimpleDateFormat")
    fun getCameraImageData(uri: Uri, currentPhotoPath: String) {
        mCurrentPhotoPath = currentPhotoPath
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(mActivity.contentResolver, Uri.fromFile(File(mCurrentPhotoPath))))
        } else {
            MediaStore.Images.Media.getBitmap(mActivity.contentResolver, Uri.fromFile(File(mCurrentPhotoPath)))
        }


        if (bitmap != null) {
            val ei = ExifInterface(mCurrentPhotoPath)
            val orientation: Int = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

//                            //사진해상도가 너무 높으면 비트맵으로 로딩
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//            8분의 1크기로 비트맵 객체 생성
//                            options.inSampleSize = 8;
//                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
//                        var rotatedBitmap: Bitmap? = null
//                        when (orientation) {
//                            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90f)
//                            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180f)
//                            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270f)
//                            ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
//                            else -> rotatedBitmap = bitmap
//                        }

            var rotatedBitmap = bitmap

            val timeStamp: String = SimpleDateFormat("yyyy_MM_dd").format(Date(System.currentTimeMillis()))


            val imageUploadData = ImageUploadData()
            imageUploadData.setImageBitmap(bitmap)
            imageUploadData.setFileName(getFileName(uri)!!)

            mImageUploadListData.add(imageUploadData)

            if (null != uri) {
                val nativeImageUploadData = NativeImageUploadData()
                nativeImageUploadData.setViewType(0x01)
                nativeImageUploadData.setIsDeviceImage(true)
                nativeImageUploadData.setImageUri(uri)

                mNativeImageUploadListData.add(nativeImageUploadData)
                mNativeImageUploadAdapter.setData(mNativeImageUploadListData)
            }

        }
    }

    override fun onPause() {
        super.onPause()

        if (mCategorySpinner.isShowing) {
            mCategorySpinner.dismiss()
        }

        if (mMainAreaSpinner.isShowing) {
            mMainAreaSpinner.dismiss()
        }

        if (mSubAreaSpinner.isShowing) {
            mSubAreaSpinner.dismiss()
        }

        if (mCurrencySpinner.isShowing) {
            mCurrencySpinner.dismiss()
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
        if (context is OnUsedMarketRegisterFragmentListener) {
            mActivity = context as Activity

            if (context is OnUsedMarketRegisterFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnUsedMarketRegisterFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnUsedMarketRegisterFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnUsedMarketRegisterFragmentListener"
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
     * API. Get Store Category Data
     */
    private fun getStoreCategoryData() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: ArrayList<StoreCategoryData>) {

                    if (resultData.isEmpty()) {
                        setLoadingView(false)
                        return
                    }

                    mCategoryListData = resultData

                    for (i in resultData.indices) {
                        if (!TextUtils.isEmpty(resultData[i].getCategoryName())) {
                            mCategoryNameListData.add(resultData[i].getCategoryName())
                        }
                    }

                    mCategorySpinner.setItems(mCategoryNameListData)

                    getLocationNation()
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
            COMPANY_TYPE_USED_MARKET.toString(),
            MAPP.SELECT_LANGUAGE_CODE
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
                        setLoadingView(false)
                        return
                    }

                    mNationListData.clear()
                    mNationListData = resultData

                    getLocationArea()
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
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Location Area
     */
    private fun getLocationArea() {

        val selectNation =  if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 1) {
            1
        } else if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 82) {
            2
        } else if (SharedPreferencesUtil().getSaveNationCode(mActivity) == 63) {
            3
        } else {
            3
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLocationArea(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<LocationAreaMainData>> {
                override fun onSuccess(resultData: ArrayList<LocationAreaMainData>) {
                    if (resultData.isEmpty()) {
                        setLoadingView(false)
                        return
                    }

                    mLocationAreaListData = resultData

                    setAreaSpinner()

                    getCurrencyData()
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

    /**
     * API. Get Currency Data
     */
    private fun getCurrencyData() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCurrencyData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CurrencyData>> {
                override fun onSuccess(resultData: ArrayList<CurrencyData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    mCurrencyListData = resultData

                    val currencyList = ArrayList<String>()
                    for (i in resultData.indices) {
                        if (resultData[i].getIsUseYn()) {
                            currencyList.add(resultData[i].getCurrencyCode())
                        }
                    }
                    mCurrencySpinner.setItems(currencyList)

                    if (getItemUid() != 0) {
                        getUsedMarketDetailData()
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
            MAPP.USER_DATA.getSecureKey(),
            1,
            10
        )
    }

    /**
     * API. Get Used Market Register
     */
    private fun getUsedMarketRegister() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketRegister(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)
                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_register))
                    mListener.onSuccessRegister()
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
            mTitleEditText.text.toString(),
            mTitleEditText.text.toString(),
            mContentEditText.text.toString(),
            mCategoryListData[mCategorySelectPosition].getCategoryUid(),
            mCurrencyListData[mCurrencySelectPosition].getCurrencyCode(),
            mPriceEditText.text.toString(),
            MAPP.USER_DATA.getMemberUid(),
            if (mIsSelectKRW) "KRW" else "",
            if (mIsSelectUSD) "USD" else "",
            if (mIsSelectPHP) "PHP" else "",
            if (mIsSelectCNY) "CNY" else "",
            if (mIsSelectJPY) "JPY" else "",
            mLatitude,
            mLongitude,
            mLocationAreaListData[mMainAreaSelectPosition].getCategoryUid(),
            mLocationAreaListData[mMainAreaSelectPosition].getCategoryName(),
            mLocationAreaListData[mMainAreaSelectPosition].getLocationAreaSubListData()[mSubAreaSelectPosition].getCategoryUid(),
            mLocationAreaListData[mMainAreaSelectPosition].getLocationAreaSubListData()[mSubAreaSelectPosition].getCategoryName(),
            mAreaDescEditText.text.toString(),
            mImageUploadListData,
            getCompanyUid()
        )
    }

    /**
     * API. Get Used Market Register Modify
     */
    private fun getUsedMarketRegisterModify() {

        var deleteImageUid = ""
        for (i in mDeleteImageUidListData.indices) {
            deleteImageUid = if (TextUtils.isEmpty(deleteImageUid)) {
                mDeleteImageUidListData[i]
            } else {
                deleteImageUid + "," + mDeleteImageUidListData[i]
            }
        }

        var price = ""
        price = mPriceEditText.text.toString().replace(",", "")

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketRegisterModify(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)
                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_register))
                    mListener.onSuccessModify()
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
            mTitleEditText.text.toString(),
            mTitleEditText.text.toString(),
            mContentEditText.text.toString(),
            mCategoryListData[mCategorySelectPosition].getCategoryUid(),
            mCurrencyListData[mCurrencySelectPosition].getCurrencyCode(),
            price,
            MAPP.USER_DATA.getMemberUid(),
            if (mIsSelectKRW) "KRW" else "",
            if (mIsSelectUSD) "USD" else "",
            if (mIsSelectPHP) "PHP" else "",
            if (mIsSelectCNY) "CNY" else "",
            if (mIsSelectJPY) "JPY" else "",
            mLatitude,
            mLongitude,
            mLocationAreaListData[mMainAreaSelectPosition].getCategoryUid(),
            mLocationAreaListData[mMainAreaSelectPosition].getCategoryName(),
            mLocationAreaListData[mMainAreaSelectPosition].getLocationAreaSubListData()[mSubAreaSelectPosition].getCategoryUid(),
            mLocationAreaListData[mMainAreaSelectPosition].getLocationAreaSubListData()[mSubAreaSelectPosition].getCategoryName(),
            mAreaDescEditText.text.toString(),
            mImageUploadListData,
            getItemUid().toString(),
            deleteImageUid,
            mUsedMarketData.getCompanyUid()
        )
    }

    /**
     * API. Get Review Recommend Save
     */
    private fun getUsedMarketDetailData() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getUsedMarketDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<UsedMarketData> {
                override fun onSuccess(resultData: UsedMarketData) {
                    mUsedMarketData = resultData

                    setMyMarketDisplay()
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
            MAPP.USER_DATA.getSecureKey(),
            getItemUid(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Company Member Data
     */
    private fun getCompanyDefaultData() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyDefaultData(
            mRequestQueue,
            object : NagajaManager.RequestListener<CompanyDefaultData> {
                override fun onSuccess(resultData: CompanyDefaultData) {
                    mCompanyNameView.visibility = View.VISIBLE
                    if (!TextUtils.isEmpty(resultData.getCompanyNameEnglish())) {
                        mCompanyNameTextView.text = resultData.getCompanyNameEnglish()
                    } else {
                        mCompanyNameTextView.text = resultData.getCompanyName()
                    }
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            getCompanyUid()
        )
    }
}