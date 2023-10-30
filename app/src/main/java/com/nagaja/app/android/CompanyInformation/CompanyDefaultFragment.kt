package com.nagaja.app.android.CompanyInformation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_FILE_UPLOAD_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.Dialog.CustomDialogProductBottom
import com.nagaja.app.android.Dialog.CustomDialogSalesBottom
import com.nagaja.app.android.Dialog.CustomDialogSalesBottom.Companion.SALES_DIALOG_TYPE_BREAK_TIME
import com.nagaja.app.android.Dialog.CustomDialogSalesBottom.Companion.SALES_DIALOG_TYPE_BUSINESS_TIME
import com.nagaja.app.android.Dialog.CustomDialogSalesBottom.Companion.SALES_DIALOG_TYPE_DAY_OFF
import com.nagaja.app.android.Dialog.CustomDialogSalesBottom.Companion.SALES_DIALOG_TYPE_PERSON_SETTING
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_company_default.view.*
import kotlinx.android.synthetic.main.fragment_job.view.*
import kotlinx.android.synthetic.main.fragment_job_register.view.*
import kotlinx.android.synthetic.main.fragment_news_detail.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store_write.view.*
import kotlinx.android.synthetic.main.layout_company_default_information.view.*
import kotlinx.android.synthetic.main.layout_company_product_information.view.*
import kotlinx.android.synthetic.main.layout_company_sales_information.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.util.*

class CompanyDefaultFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mLicenseImageView: SimpleDraweeView

    private lateinit var mContainer: View
    private lateinit var mDefaultLayoutView: View
    private lateinit var mSalesLayoutView: View
    private lateinit var mProductLayoutView: View
    private lateinit var mLoadingView: View

    private lateinit var mDefaultInformationView: View
    private lateinit var mSalesInformationView: View
    private lateinit var mProductInformationView: View
    private lateinit var mDefaultInformationLineView: View
    private lateinit var mSalesInformationLineView: View
    private lateinit var mProductInformationLineView: View

    private lateinit var mSalesMasterView: View
    private lateinit var mSalesManagerView: View
    private lateinit var mSalesFrontView: View
    private lateinit var mSalesWaiterView: View
    private lateinit var mSalesDefaultStaffView: View
    private lateinit var mSalesPosSystemView: View
    private lateinit var mSalesEtcView: View

    private lateinit var mCompanyEnglishNameEditText: EditText
    private lateinit var mCompanyKoreaNameEditText: EditText
    private lateinit var mCompanyFilipinoNameEditText: EditText
    private lateinit var mCompanyChineseNameEditText: EditText
    private lateinit var mCompanyJapaneseNameEditText: EditText
    private lateinit var mAddressDetailEditText: EditText
    private lateinit var mManagerNameEditText: EditText
    private lateinit var mPhoneNumberEditText: EditText
    private lateinit var mEmailEditText: EditText
    private lateinit var mFacebookEditText: EditText
    private lateinit var mKakaoEditText: EditText
    private lateinit var mLineEditText: EditText
    private lateinit var mPersonLimitEditText: EditText
    private lateinit var mCompanyDescriptionEditText: EditText

    private lateinit var mCompanyAddressTextView: TextView
    private lateinit var mModifyAddressTextView: TextView
    private lateinit var mLocationTextView: TextView
    private lateinit var mDefaultInformationTextView: TextView
    private lateinit var mSalesInformationTextView: TextView
    private lateinit var mProductInformationTextView: TextView
    private lateinit var mProductCountTextView: TextView
    private lateinit var mBottomButtonTextView: TextView
    private lateinit var mLicenseFileNameTextView: TextView
    private lateinit var mSalesDeliveryTextView: TextView
    private lateinit var mSalesReservationTextView: TextView
    private lateinit var mSalesPickUpTextView: TextView
    private lateinit var mSalesParkingTextView: TextView
    private lateinit var mSalesPetTextView: TextView
    private lateinit var mRegularHolidayTextView: TextView
    private lateinit var mBusinessTimeTextView: TextView
    private lateinit var mBreakTimeTextView: TextView
    private lateinit var mPersonLimitSettingTextView: TextView
    private lateinit var mLicenseNumberTextView: TextView
    private lateinit var mOwnerNameTextView: TextView
    private lateinit var mManagerNameTextView: TextView

    private lateinit var mCurrencyDollarTextView: TextView
    private lateinit var mCurrencyWonTextView: TextView
    private lateinit var mCurrencyPesoTextView: TextView
    private lateinit var mCurrencyYuanTextView: TextView
    private lateinit var mCurrencyYenTextView: TextView
    private lateinit var mCommentWordCountTextView: TextView


    private lateinit var mLicenseLinkImageView: ImageView
    private lateinit var mLicenseFullScreenView: View

    // Default Information
    private lateinit var mCompanyImageRecyclerView: RecyclerView
    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter
    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()
    private var mDeleteImageUidListData = ArrayList<String>()
    private lateinit var mFileUploadRecyclerView: RecyclerView
    private lateinit var mFileUploadAdapter: FileUploadAdapter

    // Product
    private lateinit var mProductItemRecyclerView: RecyclerView
    private lateinit var mCompanyProductItemAdapter: CompanyProductItemAdapter

    // Sales Manager
    private lateinit var mSalesMasterRecyclerView: RecyclerView
    private lateinit var mCompanySalesMasterAdapter: CompanySalesManagerAdapter
    private lateinit var mSalesManagerRecyclerView: RecyclerView
    private lateinit var mCompanySalesManagerAdapter: CompanySalesManagerAdapter
    private lateinit var mSalesFrontRecyclerView: RecyclerView
    private lateinit var mCompanySalesFrontAdapter: CompanySalesManagerAdapter
    private lateinit var mSalesWaiterRecyclerView: RecyclerView
    private lateinit var mCompanySalesWaiterAdapter: CompanySalesManagerAdapter
    private lateinit var mSalesDefaultStaffRecyclerView: RecyclerView
    private lateinit var mCompanySalesDefaultStaffAdapter: CompanySalesManagerAdapter
    private lateinit var mSalesPosSystemRecyclerView: RecyclerView
    private lateinit var mCompanySalesPosSystemAdapter: CompanySalesManagerAdapter
    private lateinit var mSalesEtcRecyclerView: RecyclerView
    private lateinit var mCompanySalesEtcAdapter: CompanySalesManagerAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private var mCurrentPhotoPath = ""
    private var mLatitude = ""
    private var mLongitude = ""

    private var mSelectType = 0

    private var mCompanyInformationData = CompanyInformationData()
    private var mFileUriListData = ArrayList<FileUploadData>()

    private lateinit var mListener: OnCompanyDefaultFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    interface OnCompanyDefaultFragmentListener {
        fun onBack()
        fun onGalleryImageSelect(isFile: Boolean)
        fun onCameraImage(isFile: Boolean)
        fun onMapScreen()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
    }

    companion object {

        const val TAB_TYPE_DEFAULT                              = 0x01
        const val TAB_TYPE_SALES                                = 0x02
        const val TAB_TYPE_PRODUCT                              = 0x03

        const val SELECT_ADD_FEATURES_POSITION_DELIVERY         = 0x01
        const val SELECT_ADD_FEATURES_POSITION_RESERVATION      = 0x02
        const val SELECT_ADD_FEATURES_POSITION_PICK_UP          = 0x03
        const val SELECT_ADD_FEATURES_POSITION_PARKING          = 0x04
        const val SELECT_ADD_FEATURES_POSITION_PET              = 0x05

        const val SELECT_ADD_CURRENCY_POSITION_DOLLAR           = 0x01
        const val SELECT_ADD_CURRENCY_POSITION_WON              = 0x02
        const val SELECT_ADD_CURRENCY_POSITION_PESO             = 0x03
        const val SELECT_ADD_CURRENCY_POSITION_YUAN             = 0x04
        const val SELECT_ADD_CURRENCY_POSITION_YEN              = 0x05

        const val CURRENCY_UID_DOLLAR                           = 840
        const val CURRENCY_UID_WON                              = 410
        const val CURRENCY_UID_PESO                             = 608
        const val CURRENCY_UID_YUAN                             = 156
        const val CURRENCY_UID_YEN                              = 392


        const val OFF_DAY_TYPE_1                                = "1"
        const val OFF_DAY_TYPE_2                                = "2"
        const val OFF_DAY_TYPE_3                                = "3"
        const val OFF_DAY_TYPE_4                                = "4"
        const val OFF_DAY_TYPE_5                                = "5"
        const val OFF_DAY_TYPE_6                                = "6"
        const val OFF_DAY_TYPE_7                                = "7"
        const val OFF_DAY_TYPE_8                                = "8"
        const val OFF_DAY_TYPE_9                                = "9"
        const val OFF_DAY_TYPE_10                               = "10"
        const val OFF_DAY_TYPE_11                               = "11"
        const val OFF_DAY_TYPE_12                               = "12"
        const val OFF_DAY_TYPE_13                               = "13"
        const val OFF_DAY_TYPE_14                               = "14"


        private const val ARG_COMPANY_DEFAULT_DATA              = "arg_company_default_data"
        private const val ARG_COMPANY_MANAGEMENT_SELECT_TYPE    = "arg_company_management_select_type"

        fun newInstance(data: CompanyDefaultData, selectType: Int): CompanyDefaultFragment {
            val args = Bundle()
            val fragment = CompanyDefaultFragment()
            args.putSerializable(ARG_COMPANY_DEFAULT_DATA, data)
            args.putInt(ARG_COMPANY_MANAGEMENT_SELECT_TYPE, selectType)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCompanyDefaultData(): CompanyDefaultData {
        val arguments = arguments
        return arguments?.getSerializable(ARG_COMPANY_DEFAULT_DATA) as CompanyDefaultData
    }

    private fun getSelectManagementType(): Int {
        val arguments = arguments
        return arguments?.getInt(ARG_COMPANY_MANAGEMENT_SELECT_TYPE) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)
        mCompanyProductItemAdapter = CompanyProductItemAdapter(mActivity)
        mCompanySalesMasterAdapter = CompanySalesManagerAdapter(mActivity)
        mCompanySalesManagerAdapter = CompanySalesManagerAdapter(mActivity)
        mCompanySalesFrontAdapter = CompanySalesManagerAdapter(mActivity)
        mCompanySalesWaiterAdapter = CompanySalesManagerAdapter(mActivity)
        mCompanySalesDefaultStaffAdapter = CompanySalesManagerAdapter(mActivity)
        mCompanySalesPosSystemAdapter = CompanySalesManagerAdapter(mActivity)
        mCompanySalesEtcAdapter = CompanySalesManagerAdapter(mActivity)
        mFileUploadAdapter = FileUploadAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_company_default, container, false)

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
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_company_default_information)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Company Default Image View
        val companyDefaultImageView = mContainer.fragment_company_default_image_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getCompanyMainImageUrl())) {
            companyDefaultImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + getCompanyDefaultData().getCompanyMainImageUrl()))
        }

        // Company Name Text View
        val companyNameTextView = mContainer.fragment_company_default_name_text_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getCompanyName())) {
            companyNameTextView.text = getCompanyDefaultData().getCompanyName()
        }

        // Company Type Text View
        val companyTypeTextView = mContainer.fragment_company_default_type_text_view
        companyTypeTextView.text = Util().getCompanyType(mActivity, getCompanyDefaultData().getCategoryUid())

        // Company Manager Name Text View
        val companyManagerNameTextView = mContainer.fragment_company_default_manager_text_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getManageName())) {
            companyManagerNameTextView.text = getCompanyDefaultData().getManageName()
        }

        // Company Address Text View
        mCompanyAddressTextView = mContainer.fragment_company_default_address_text_view
        if (!TextUtils.isEmpty(getCompanyDefaultData().getCompanyAddress())) {
            mCompanyAddressTextView.text = getCompanyDefaultData().getCompanyAddress() + " " + getCompanyDefaultData().getCompanyAddressDetail()
        }


        // Default Information View
        mDefaultInformationView = mContainer.fragment_company_default_information_view
        mDefaultInformationView.setOnClickListener {
            setDefaultTab(TAB_TYPE_DEFAULT)
        }

        // Default Information Text View
        mDefaultInformationTextView = mContainer.fragment_company_default_information_text_view

        // Default Information Line View
        mDefaultInformationLineView = mContainer.fragment_company_default_information_line_view



        // Sales Information View
        mSalesInformationView = mContainer.fragment_company_default_sales_information_view
        mSalesInformationView.setOnClickListener {
            setDefaultTab(TAB_TYPE_SALES)
        }

        // Sales Information Text View
        mSalesInformationTextView = mContainer.fragment_company_default_sales_information_text_view

        // Sales Information Line View
        mSalesInformationLineView = mContainer.fragment_company_default_sales_information_line_view



        // Product Information View
        mProductInformationView = mContainer.fragment_company_default_product_information_view
        mProductInformationView.setOnClickListener {
            setDefaultTab(TAB_TYPE_PRODUCT)
        }

        // Product Information Text View
        mProductInformationTextView = mContainer.fragment_company_default_product_information_text_view

        // Product Information Line View
        mProductInformationLineView = mContainer.fragment_company_default_product_information_line_view





        // Default Layout View
        mDefaultLayoutView = mContainer.fragment_company_default_layout_view

        // Sales Layout View
        mSalesLayoutView = mContainer.fragment_company_sales_layout_view

        // Product Layout View
        mProductLayoutView = mContainer.fragment_company_product_layout_view




        // Company English Name Edit Text
        mCompanyEnglishNameEditText = mContainer.layout_company_default_information_company_english_name_edit_text
        mCompanyEnglishNameEditText.filters = arrayOf(InputFilter.LengthFilter(30))
        mCompanyEnglishNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Company Korea Name Edit Text
        mCompanyKoreaNameEditText = mContainer.layout_company_default_information_company_korea_name_edit_text
        mCompanyKoreaNameEditText.filters = arrayOf(InputFilter.LengthFilter(30))
        mCompanyKoreaNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Company Filipino Name Edit Text
        mCompanyFilipinoNameEditText = mContainer.layout_company_default_information_company_filipino_name_edit_text
        mCompanyFilipinoNameEditText.filters = arrayOf(InputFilter.LengthFilter(30))
        mCompanyFilipinoNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Company Chinese Name Edit Text
        mCompanyChineseNameEditText = mContainer.layout_company_default_information_company_chinese_name_edit_text
        mCompanyChineseNameEditText.filters = arrayOf(InputFilter.LengthFilter(30))
        mCompanyChineseNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Company Japanese Name Edit Text
        mCompanyJapaneseNameEditText = mContainer.layout_company_default_information_company_japanese_name_edit_text
        mCompanyJapaneseNameEditText.filters = arrayOf(InputFilter.LengthFilter(30))
        mCompanyJapaneseNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Company Image Recycler View
        mCompanyImageRecyclerView = mContainer.layout_company_default_information_company_image_recycler_view
        mCompanyImageRecyclerView.setHasFixedSize(true)
        mCompanyImageRecyclerView.layoutManager = LinearLayoutManager(
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

                if (data.getImageUid() != 0) {
                    mDeleteImageUidListData.add(data.getImageUid().toString())
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
                if (imageCount > 9) {
                    showToast(mActivity, mActivity.resources.getString(R.string.text_error_image_upload_count_10))
                    return
                }

                showCustomImageUploadDialog()
            }

        })
        mCompanyImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCompanyImageRecyclerView.adapter = mNativeImageUploadAdapter

        // Modify Address Text View
        mModifyAddressTextView = mContainer.layout_company_default_information_address_text_view
        mModifyAddressTextView.setOnClickListener {
            mListener.onMapScreen()
        }

        // Address Detail Edit Text
        mAddressDetailEditText = mContainer.layout_company_default_information_address_detail_edit_text

        // Address Map Icon Image View
        val mapIconImageView = mContainer.fragment_application_company_member_map_image_view
        mapIconImageView.setOnClickListener {
            mListener.onMapScreen()
        }

        // Location Text View
        mLocationTextView = mContainer.layout_company_default_information_location_edit_text

        // Manager Name Edit Text
        mManagerNameEditText = mContainer.layout_company_default_information_manager_name_edit_text

        // Phone Number Edit Text
        mPhoneNumberEditText = mContainer.layout_company_default_information_phone_number_edit_text

        // Email Edit Text
        mEmailEditText = mContainer.layout_company_default_information_email_edit_text

        // Facebook Edit Text
        mFacebookEditText = mContainer.layout_company_default_information_facebook_edit_text

        // Kakao Edit Text
        mKakaoEditText = mContainer.layout_company_default_information_kakao_edit_text

        // Line Edit Text
        mLineEditText = mContainer.layout_company_default_information_line_edit_text

        // Company Description Edit Text
        mCompanyDescriptionEditText = mContainer.layout_company_default_information_company_description_edit_text
        mCompanyDescriptionEditText.filters = arrayOf(InputFilter.LengthFilter(300))
        mCompanyDescriptionEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mCompanyDescriptionEditText.text.length > 300) {
//                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_company_description_length))
                    return
                }

//                if (TextUtils.isEmpty(mCompanyDescriptionEditText.text)) {
//                    mCommentWordCountTextView.text = "0"
//                } else {
//                    mCommentWordCountTextView.text = mCompanyDescriptionEditText.text.length.toString()
//                }
            }
        })
        mCompanyDescriptionEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.layout_company_default_information_company_description_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        // Company License Image View
        mLicenseImageView = mContainer.layout_company_default_information_license_image_view

        // License Full Screen View
        mLicenseFullScreenView = mContainer.layout_company_default_information_license_full_screen_view

        // Comment Word Count Text View
        mCommentWordCountTextView = mContainer.layout_company_default_information_company_description_edit_text

        // License File Name Text View
        mLicenseFileNameTextView = mContainer.layout_company_default_information_license_file_name_text_view

        // License Link Image View
        mLicenseLinkImageView = mContainer.layout_company_default_information_license_link_text_view
        mLicenseLinkImageView.setOnClickListener {
            if (mFileUriListData.size == 3) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_upload_file_count))
            } else {
                fileUpload()
            }
        }

        // License Number Text View
        mLicenseNumberTextView = mContainer.layout_company_default_information_license_number_text_view

        // Owner Name Text View
        mOwnerNameTextView = mContainer.layout_company_default_information_owner_name_text_view

        // Manager Name Text View
        mManagerNameTextView = mContainer.layout_company_default_information_manager_name_text_view





        // Sales Master View
        mSalesMasterView = mContainer.layout_company_sales_information_master_view

        // Sales Master Recycler View
        mSalesMasterRecyclerView = mContainer.layout_company_sales_information_master_recycler_view
        mSalesMasterRecyclerView.setHasFixedSize(true)
        mSalesMasterRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanySalesMasterAdapter.setOnItemClickListener(object : CompanySalesManagerAdapter.OnItemClickListener {
            override fun onClick(data: CompanyManagerData) {
            }
        })
        mSalesMasterRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSalesMasterRecyclerView.adapter = mCompanySalesMasterAdapter



        // Sales Manager View
        mSalesManagerView = mContainer.layout_company_sales_information_manager_view

        // Sales Manager Recycler View
        mSalesManagerRecyclerView = mContainer.layout_company_sales_information_manager_recycler_view
        mSalesManagerRecyclerView.setHasFixedSize(true)
        mSalesManagerRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanySalesManagerAdapter.setOnItemClickListener(object : CompanySalesManagerAdapter.OnItemClickListener {
            override fun onClick(data: CompanyManagerData) {
            }
        })
        mSalesManagerRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSalesManagerRecyclerView.adapter = mCompanySalesManagerAdapter



        // Sales Front View
        mSalesFrontView = mContainer.layout_company_sales_information_front_view

        // Sales Front Recycler View
        mSalesFrontRecyclerView = mContainer.layout_company_sales_information_front_recycler_view
        mSalesFrontRecyclerView.setHasFixedSize(true)
        mSalesFrontRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanySalesFrontAdapter.setOnItemClickListener(object : CompanySalesManagerAdapter.OnItemClickListener {
            override fun onClick(data: CompanyManagerData) {
            }
        })
        mSalesFrontRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSalesFrontRecyclerView.adapter = mCompanySalesFrontAdapter




        // Sales Waiter View
        mSalesWaiterView = mContainer.layout_company_sales_information_waiter_view

        // Sales Waiter Recycler View
        mSalesWaiterRecyclerView = mContainer.layout_company_sales_information_waiter_recycler_view
        mSalesWaiterRecyclerView.setHasFixedSize(true)
        mSalesWaiterRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanySalesWaiterAdapter.setOnItemClickListener(object : CompanySalesManagerAdapter.OnItemClickListener {
            override fun onClick(data: CompanyManagerData) {
            }
        })
        mSalesWaiterRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSalesWaiterRecyclerView.adapter = mCompanySalesWaiterAdapter




        // Sales Default Staff View
        mSalesDefaultStaffView = mContainer.layout_company_sales_information_default_staff_view

        // Sales Default Staff View
        mSalesDefaultStaffRecyclerView = mContainer.layout_company_sales_information_default_staff_recycler_view
        mSalesDefaultStaffRecyclerView.setHasFixedSize(true)
        mSalesDefaultStaffRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanySalesDefaultStaffAdapter.setOnItemClickListener(object : CompanySalesManagerAdapter.OnItemClickListener {
            override fun onClick(data: CompanyManagerData) {
            }
        })
        mSalesDefaultStaffRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSalesDefaultStaffRecyclerView.adapter = mCompanySalesDefaultStaffAdapter





        // Sales Pos System View
        mSalesPosSystemView = mContainer.layout_company_sales_information_pos_system_view

        // Sales Pos System Recycler View
        mSalesPosSystemRecyclerView = mContainer.layout_company_sales_information_pos_system_recycler_view
        mSalesPosSystemRecyclerView.setHasFixedSize(true)
        mSalesPosSystemRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanySalesPosSystemAdapter.setOnItemClickListener(object : CompanySalesManagerAdapter.OnItemClickListener {
            override fun onClick(data: CompanyManagerData) {
            }
        })
        mSalesPosSystemRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSalesPosSystemRecyclerView.adapter = mCompanySalesPosSystemAdapter

        // Sales ETC View
        mSalesEtcView = mContainer.layout_company_sales_information_etc_view

        // Sales ETC Recycler View
        mSalesEtcRecyclerView = mContainer.layout_company_sales_information_etc_recycler_view
        mSalesEtcRecyclerView.setHasFixedSize(true)
        mSalesEtcRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanySalesEtcAdapter.setOnItemClickListener(object : CompanySalesManagerAdapter.OnItemClickListener {
            override fun onClick(data: CompanyManagerData) {
            }
        })
        mSalesEtcRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mSalesEtcRecyclerView.adapter = mCompanySalesEtcAdapter

        // Person Setting Text View
        val personSettingTextView = mContainer.layout_company_sales_information_person_setting_text_view
        personSettingTextView.setOnSingleClickListener {
            val customDialogSalesBottom = CustomDialogSalesBottom(mActivity, SALES_DIALOG_TYPE_PERSON_SETTING)
            customDialogSalesBottom.setOnCustomDialogBottomListener(object : CustomDialogSalesBottom.OnCustomDialogBottomListener {
                override fun onRegister() {
                }

                override fun onDisConnectUserToken() {
                    disConnectUserToken(mActivity)
                }

                override fun onSuccessManager() {
                    getCompanyManagerData()
                    customDialogSalesBottom.dismiss()
                }

                override fun onSuccessBusinessTime() {
                }

                override fun onSuccessBreakTime() {
                }
            })
            customDialogSalesBottom.show(parentFragmentManager, "Custom Dialog")
        }




        // Sales Delivery Text View
        mSalesDeliveryTextView = mContainer.layout_company_sales_information_delivery_text_view
        mSalesDeliveryTextView.setOnClickListener {
            getCompanyChangeAddFeatures(SELECT_ADD_FEATURES_POSITION_DELIVERY, !MAPP.COMPANY_SALES_DATA.getIsCompanyDeliveryUseYn())
        }

        // Sales Reservation Text View
        mSalesReservationTextView = mContainer.layout_company_sales_information_reservation_text_view
        mSalesReservationTextView.setOnClickListener {
            getCompanyChangeAddFeatures(SELECT_ADD_FEATURES_POSITION_RESERVATION, !MAPP.COMPANY_SALES_DATA.getIsCompanyReservationUseYn())
        }

        // Sales Pick Up Text View
        mSalesPickUpTextView = mContainer.layout_company_sales_information_pickup_text_view
        mSalesPickUpTextView.setOnClickListener {
            getCompanyChangeAddFeatures(SELECT_ADD_FEATURES_POSITION_PICK_UP, !MAPP.COMPANY_SALES_DATA.getIsCompanyPickupUseYn())
        }

        // Sales Parking Text View
        mSalesParkingTextView = mContainer.layout_company_sales_information_parking_text_view
        mSalesParkingTextView.setOnClickListener {
            getCompanyChangeAddFeatures(SELECT_ADD_FEATURES_POSITION_PARKING, !MAPP.COMPANY_SALES_DATA.getIsCompanyParkingUseYn())
        }

        // Sales Pet Text View
        mSalesPetTextView = mContainer.layout_company_sales_information_pet_text_view
        mSalesPetTextView.setOnClickListener {
            getCompanyChangeAddFeatures(SELECT_ADD_FEATURES_POSITION_PET, !MAPP.COMPANY_SALES_DATA.getIsCompanyPetUseYn())
        }





        // Regular Holiday Text View
        mRegularHolidayTextView = mContainer.layout_company_sales_information_regular_holiday_text_view

        // Day Off Setting Text View
        val dayOffSettingTextView = mContainer.layout_company_sales_information_day_off_setting_text_view
        dayOffSettingTextView.setOnSingleClickListener {
            val customDialogSalesBottom = CustomDialogSalesBottom(mActivity, SALES_DIALOG_TYPE_DAY_OFF)
            customDialogSalesBottom.setOnCustomDialogBottomListener(object : CustomDialogSalesBottom.OnCustomDialogBottomListener {
                override fun onRegister() {
                    setRegularHolidayTypeToText()
                    customDialogSalesBottom.dismiss()
                }

                override fun onDisConnectUserToken() {
                    disConnectUserToken(mActivity)
                }

                override fun onSuccessManager() {
                }

                override fun onSuccessBusinessTime() {
                }

                override fun onSuccessBreakTime() {
                }

            })
            customDialogSalesBottom.show(parentFragmentManager, "Custom Dialog")
        }

        // Business Time Text View
        mBusinessTimeTextView = mContainer.layout_company_sales_information_business_time_text_view

        // Business Time Setting Text View
        val businessTimeSettingTextView = mContainer.layout_company_sales_information_business_time_setting_text_view
        businessTimeSettingTextView.setOnSingleClickListener {
            val customDialogSalesBottom = CustomDialogSalesBottom(mActivity, SALES_DIALOG_TYPE_BUSINESS_TIME)
            customDialogSalesBottom.setOnCustomDialogBottomListener(object : CustomDialogSalesBottom.OnCustomDialogBottomListener {
                override fun onRegister() {
                    setRegularHolidayTypeToText()
                    customDialogSalesBottom.dismiss()
                }

                override fun onDisConnectUserToken() {
                    disConnectUserToken(mActivity)
                }

                override fun onSuccessManager() {
                }

                override fun onSuccessBusinessTime() {
                    displayBusinessTime()
                    customDialogSalesBottom.dismiss()
                }

                override fun onSuccessBreakTime() {
                }
            })
            customDialogSalesBottom.show(parentFragmentManager, "Custom Dialog")
        }

        // Break Time Text View
        mBreakTimeTextView = mContainer.layout_company_sales_information_break_time_text_view

        // Break Time Setting Text View
        val breakTimeSettingTextView = mContainer.layout_company_sales_information_break_time_setting_text_view
        breakTimeSettingTextView.setOnSingleClickListener {
            val customDialogSalesBottom = CustomDialogSalesBottom(mActivity, SALES_DIALOG_TYPE_BREAK_TIME)
            customDialogSalesBottom.setOnCustomDialogBottomListener(object : CustomDialogSalesBottom.OnCustomDialogBottomListener {
                override fun onRegister() {
                    setRegularHolidayTypeToText()
                    customDialogSalesBottom.dismiss()
                }

                override fun onDisConnectUserToken() {
                    disConnectUserToken(mActivity)
                }

                override fun onSuccessManager() {
                }

                override fun onSuccessBusinessTime() {
                }

                override fun onSuccessBreakTime() {
                    displayBreakTime()
                    customDialogSalesBottom.dismiss()
                }

            })
            customDialogSalesBottom.show(parentFragmentManager, "Custom Dialog")
        }

        // Person Limit Edit Text
        mPersonLimitEditText = mContainer.layout_company_sales_information_reservation_person_limit_edit_text
        mPersonLimitEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(99))
        mPersonLimitEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        // Person Limit Setting Text View
        mPersonLimitSettingTextView = mContainer.layout_company_sales_information_person_limit_setting_text_view
        mPersonLimitSettingTextView.setOnClickListener {
            hideKeyboard(mActivity)
            if (!TextUtils.isEmpty(mPersonLimitEditText.text)) {
                if (mPersonLimitEditText.text.toString().toInt() >= 0) {
                    getCompanyReservationPersonLimit(mPersonLimitEditText.text.toString().toInt())
                    mPersonLimitEditText.clearFocus()
                }
            }
        }







        // Product Count Text View
        mProductCountTextView = mContainer.layout_company_product_information_count_text_view

        // Product Item Recycler View
        mProductItemRecyclerView = mContainer.layout_company_product_information_recycler_view
        mProductItemRecyclerView.setHasFixedSize(true)
        mProductItemRecyclerView.layoutManager = LinearLayoutManager(
            mActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mCompanyProductItemAdapter.setOnItemClickListener(object : CompanyProductItemAdapter.OnItemClickListener {
            override fun onModify(data: CompanyProductData) {
                val customDialogProductBottom = CustomDialogProductBottom(mActivity, data, getCompanyDefaultData().getCompanyUid(), getCompanyDefaultData().getCategoryUid(), true)
                customDialogProductBottom.setonCustomDialogProductBottomListener(object : CustomDialogProductBottom.onCustomDialogProductBottomListener {
                    override fun onSuccessRegister() {
                        getCompanyProductItemData()
                        customDialogProductBottom.dismiss()
                    }

                })
                customDialogProductBottom.show(parentFragmentManager, "Custom Dialog")
            }

            override fun onDelete(data: CompanyProductData) {
                showProductRemovePopup(data.getItemName(), data.getItemUid(), data.getCompanyUid())
            }

        })
        mProductItemRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mProductItemRecyclerView.adapter = mCompanyProductItemAdapter



        // Currency Dollar Text View
        mCurrencyDollarTextView = mContainer.layout_company_sales_information_currency_dollar_text_view
        mCurrencyDollarTextView.setOnClickListener {
            getCompanyUseCurrency(SELECT_ADD_CURRENCY_POSITION_DOLLAR)
        }

        // Currency Won Text View
        mCurrencyWonTextView = mContainer.layout_company_sales_information_currency_won_text_view
        mCurrencyWonTextView.setOnClickListener {
            getCompanyUseCurrency(SELECT_ADD_CURRENCY_POSITION_WON)
        }

        // Currency Peso Text View
        mCurrencyPesoTextView = mContainer.layout_company_sales_information_currency_peso_text_view
        mCurrencyPesoTextView.setOnClickListener {
            getCompanyUseCurrency(SELECT_ADD_CURRENCY_POSITION_PESO)
        }

        // Currency Yuan Text View
        mCurrencyYuanTextView = mContainer.layout_company_sales_information_currency_yuan_text_view
        mCurrencyYuanTextView.setOnClickListener {
            getCompanyUseCurrency(SELECT_ADD_CURRENCY_POSITION_YUAN)
        }

        // Currency Yen Text View
        mCurrencyYenTextView = mContainer.layout_company_sales_information_currency_yen_text_view
        mCurrencyYenTextView.setOnClickListener {
            getCompanyUseCurrency(SELECT_ADD_CURRENCY_POSITION_YEN)
        }

        // File Upload Recycler View
        mFileUploadRecyclerView = mContainer.layout_company_default_information_file_upload_recycler_view
        mFileUploadRecyclerView.setHasFixedSize(true)
        mFileUploadRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mFileUploadAdapter.setOnItemClickListener(object : FileUploadAdapter.OnItemClickListener {
            override fun onDelete(data: FileUploadData, position: Int) {
                mFileUploadAdapter.deleteItem(position)

                mFileUriListData.removeAt(position)
            }
        })
        mFileUploadRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mFileUploadRecyclerView.adapter = mFileUploadAdapter




        // Bottom Button Text View
        mBottomButtonTextView = mContainer.fragment_company_default_bottom_button_text_view
        mBottomButtonTextView.setOnClickListener {
            when (mSelectType) {
                TAB_TYPE_DEFAULT -> {
                    checkModifyInformation()
                }

                TAB_TYPE_SALES -> {

                }

                TAB_TYPE_PRODUCT -> {
                    val customDialogProductBottom = CustomDialogProductBottom(mActivity, null, getCompanyDefaultData().getCompanyUid(), getCompanyDefaultData().getCategoryUid(), false)
                    customDialogProductBottom.setonCustomDialogProductBottomListener(object : CustomDialogProductBottom.onCustomDialogProductBottomListener {
                        override fun onSuccessRegister() {
                            getCompanyProductItemData()
                            customDialogProductBottom.dismiss()
                        }
                    })
                    customDialogProductBottom.show(parentFragmentManager, "Custom Dialog")
                }
            }
        }



        // Loading View
        mLoadingView = mContainer.fragment_company_default_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_company_default_loading_lottie_view


        setDefaultTab(getSelectManagementType())





    }

    private fun setDefaultTab(type: Int) {

        when (type) {
            TAB_TYPE_DEFAULT -> {
                mDefaultInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mDefaultInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mSalesInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mSalesInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mProductInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mProductInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mDefaultLayoutView.visibility = View.VISIBLE
                mSalesLayoutView.visibility = View.GONE
                mProductLayoutView.visibility = View.GONE

                mBottomButtonTextView.visibility = View.VISIBLE
                mBottomButtonTextView.text = mActivity.resources.getString(R.string.text_company_default_change_information)

                mSelectType = TAB_TYPE_DEFAULT

                getCompanyInformation()
            }

            TAB_TYPE_SALES -> {
                mDefaultInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mDefaultInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mSalesInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mSalesInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mProductInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mProductInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mDefaultLayoutView.visibility = View.GONE
                mSalesLayoutView.visibility = View.VISIBLE
                mProductLayoutView.visibility = View.GONE

                mBottomButtonTextView.visibility = View.GONE

                mSelectType = TAB_TYPE_SALES

                getCompanyManagerData()
            }

            TAB_TYPE_PRODUCT -> {
                mDefaultInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mDefaultInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mSalesInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mSalesInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mProductInformationTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mProductInformationLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mDefaultLayoutView.visibility = View.GONE
                mSalesLayoutView.visibility = View.GONE
                mProductLayoutView.visibility = View.VISIBLE

                mBottomButtonTextView.visibility = View.VISIBLE
                mBottomButtonTextView.text = mActivity.resources.getString(R.string.text_register_product)

                mSelectType = TAB_TYPE_PRODUCT

                getCompanyProductItemData()
            }
        }
    }

    private fun setRegularHolidayTypeToText() {
        if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceWeekOffDays())) {
            var dayOff = ""
            when (MAPP.COMPANY_SALES_DATA.getServiceWeekOffDays()) {
                OFF_DAY_TYPE_1 -> {
                    dayOff = mActivity.resources.getString(R.string.text_every_sunday)
                }

                OFF_DAY_TYPE_2 -> {
                    dayOff = mActivity.resources.getString(R.string.text_every_monday)
                }

                OFF_DAY_TYPE_3 -> {
                    dayOff = mActivity.resources.getString(R.string.text_every_tuesday)
                }

                OFF_DAY_TYPE_4 -> {
                    dayOff = mActivity.resources.getString(R.string.text_every_wednesday)
                }

                OFF_DAY_TYPE_5 -> {
                    dayOff = mActivity.resources.getString(R.string.text_every_thursday)
                }

                OFF_DAY_TYPE_6 -> {
                    dayOff = mActivity.resources.getString(R.string.text_every_friday)
                }

                OFF_DAY_TYPE_7 -> {
                    dayOff = mActivity.resources.getString(R.string.text_every_saturday)
                }
            }

            mRegularHolidayTextView.text = dayOff

        } else if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceWeekDoubleOffDays())) {
            var dayOff = ""
            when (MAPP.COMPANY_SALES_DATA.getServiceWeekDoubleOffDays()) {
                OFF_DAY_TYPE_1 -> {
                    dayOff = mActivity.resources.getString(R.string.text_first_third_sunday_of_every_month)
                }

                OFF_DAY_TYPE_2 -> {
                    dayOff = mActivity.resources.getString(R.string.text_first_third_monday_of_every_month)
                }

                OFF_DAY_TYPE_3 -> {
                    dayOff = mActivity.resources.getString(R.string.text_first_third_tuesday_of_every_month)
                }

                OFF_DAY_TYPE_4 -> {
                    dayOff = mActivity.resources.getString(R.string.text_first_third_wednesday_of_every_month)
                }

                OFF_DAY_TYPE_5 -> {
                    dayOff = mActivity.resources.getString(R.string.text_first_third_thursday_of_every_month)
                }

                OFF_DAY_TYPE_6 -> {
                    dayOff = mActivity.resources.getString(R.string.text_first_third_friday_of_every_month)
                }

                OFF_DAY_TYPE_7 -> {
                    dayOff = mActivity.resources.getString(R.string.text_first_third_saturday_of_every_month)
                }

                OFF_DAY_TYPE_8 -> {
                    dayOff = mActivity.resources.getString(R.string.text_second_fourth_sunday_of_every_month)
                }

                OFF_DAY_TYPE_9 -> {
                    dayOff = mActivity.resources.getString(R.string.text_second_fourth_monday_of_every_month)
                }

                OFF_DAY_TYPE_10 -> {
                    dayOff = mActivity.resources.getString(R.string.text_second_fourth_tuesday_of_every_month)
                }

                OFF_DAY_TYPE_11 -> {
                    dayOff = mActivity.resources.getString(R.string.text_second_fourth_wednesday_of_every_month)
                }

                OFF_DAY_TYPE_12 -> {
                    dayOff = mActivity.resources.getString(R.string.text_second_fourth_thursday_of_every_month)
                }

                OFF_DAY_TYPE_13 -> {
                    dayOff = mActivity.resources.getString(R.string.text_second_fourth_friday_of_every_month)
                }

                OFF_DAY_TYPE_14 -> {
                    dayOff = mActivity.resources.getString(R.string.text_second_fourth_saturday_of_every_month)
                }
            }
            mRegularHolidayTextView.text = dayOff
        } else if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceMonthOffDays())) {
            mRegularHolidayTextView.text = String.format(mActivity.resources.getString(R.string.text_day_of_every_month), MAPP.COMPANY_SALES_DATA.getServiceMonthOffDays())
        } else {
            mRegularHolidayTextView.text = mActivity.resources.getString(R.string.text_empty)
        }

        displayBusinessTime()
    }

    private fun checkModifyInformation() {

        if (TextUtils.isEmpty(mCompanyEnglishNameEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_company_name))
            return
        } else if (mImageUploadListData.isEmpty() && mCompanyInformationData.getCompanyImageListData().isEmpty()) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_register_one_company_image))
            return
        } else if (TextUtils.isEmpty(mCompanyAddressTextView.text.toString())) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_company_address))
            return
        }

        showCustomModifyDefaultInformation()
    }

    @SuppressLint("SetTextI18n")
    private fun displayBusinessTime() {
        if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceBeginTime()) && MAPP.COMPANY_SALES_DATA.getServiceBeginTime() != "null"
            && !TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceEndTime()) && MAPP.COMPANY_SALES_DATA.getServiceEndTime() != "null") {

            var startTime = MAPP.COMPANY_SALES_DATA.getServiceBeginTime()
            var endTime = MAPP.COMPANY_SALES_DATA.getServiceEndTime()

            if (startTime.length > 5) {
                startTime = startTime.substring(0, 5)
            }

            if (endTime.length > 5) {
                endTime = endTime.substring(0, 5)
            }

            mBusinessTimeTextView.text = "$startTime ~ $endTime"
        }

        displayBreakTime()
    }

    private fun displayBreakTime() {

        var breakTime = ""


        var firstBreakStartTime = if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime()) && MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime() != "null") {
            MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime()
        } else {
            ""
        }

        var firstBreakEndTime = if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime()) && MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime() != "null") {
            MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime()
        } else {
            ""
        }

        var secondBreakStartTime = if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime()) && MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime() != "null") {
            MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime()
        } else {
            ""
        }

        var secondBreakEndTime = if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime()) && MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime() != "null") {
            MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime()
        } else {
            ""
        }

        if (!TextUtils.isEmpty(firstBreakStartTime) && !TextUtils.isEmpty(firstBreakEndTime)) {

            if (firstBreakStartTime.length > 5) {
                firstBreakStartTime = firstBreakStartTime.substring(0, 5)
            }

            if (firstBreakEndTime.length > 5) {
                firstBreakEndTime = firstBreakEndTime.substring(0, 5)
            }

            breakTime = "$firstBreakStartTime ~ $firstBreakEndTime"
        }

        if (!TextUtils.isEmpty(secondBreakStartTime) && !TextUtils.isEmpty(secondBreakEndTime)) {

            if (secondBreakStartTime.length > 5) {
                secondBreakStartTime = secondBreakStartTime.substring(0, 5)
            }

            if (secondBreakEndTime.length > 5) {
                secondBreakEndTime = secondBreakEndTime.substring(0, 5)
            }

            if (!TextUtils.isEmpty(breakTime)) {
                breakTime = "$breakTime\n$secondBreakStartTime ~ $secondBreakEndTime"
            } else {
                breakTime = "$secondBreakStartTime ~ $secondBreakEndTime"
            }
        }

        NagajaLog().d("wooks, breakTime = $breakTime")

        mBreakTimeTextView.text = if (!TextUtils.isEmpty(breakTime)) breakTime else mActivity.resources.getString(R.string.text_empty)

        displayPersonLimitSetting()
    }

    private fun displayPersonLimitSetting() {
        if (MAPP.COMPANY_SALES_DATA.getReservationPersonLimit() > 0) {
            mPersonLimitEditText.setText(MAPP.COMPANY_SALES_DATA.getReservationPersonLimit().toString())
        }

        displayCurrencySetting()
    }

    private fun displayCurrencySetting() {

        for (i in MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList().indices) {
            if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_DOLLAR) {
                val isSelect = MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()

                selectAddFeatures(mCurrencyDollarTextView, isSelect)
            } else if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_WON) {
                val isSelect = MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()

                selectAddFeatures(mCurrencyWonTextView, isSelect)
            } else if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_PESO) {
                val isSelect = MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()

                selectAddFeatures(mCurrencyPesoTextView, isSelect)
            } else if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_YUAN) {
                val isSelect = MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()

                selectAddFeatures(mCurrencyYuanTextView, isSelect)
            } else if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_YEN) {
                val isSelect = MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()

                selectAddFeatures(mCurrencyYenTextView, isSelect)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayCompanyDefaultInformation(data: CompanyInformationData) {
         if (data.getCompanyNameListData().size > 0) {
             var englishName = ""
             var koreaName = ""
             var filipinoName = ""
             var chineseName = ""
             var japaneseName = ""

             for (i in data.getCompanyNameListData().indices) {
                 if (data.getCompanyNameListData()[i].getNationUid() == 1) {
                     englishName = data.getCompanyNameListData()[i].getCompanyName()
                     if (!TextUtils.isEmpty(englishName) && englishName != "null") {
                         mCompanyEnglishNameEditText.setText(englishName)
                     }
                 }

                 if (data.getCompanyNameListData()[i].getNationUid() == 82) {
                     koreaName = data.getCompanyNameListData()[i].getCompanyName()
                     if (!TextUtils.isEmpty(koreaName) && koreaName != "null") {
                         mCompanyKoreaNameEditText.setText(koreaName)
                     }
                 }

                 if (data.getCompanyNameListData()[i].getNationUid() == 63) {
                     filipinoName = data.getCompanyNameListData()[i].getCompanyName()
                     if (!TextUtils.isEmpty(filipinoName) && filipinoName != "null") {
                         mCompanyFilipinoNameEditText.setText(filipinoName)
                     }
                 }

                 if (data.getCompanyNameListData()[i].getNationUid() == 86) {
                     chineseName = data.getCompanyNameListData()[i].getCompanyName()
                     if (!TextUtils.isEmpty(chineseName) && chineseName != "null") {
                         mCompanyChineseNameEditText.setText(chineseName)
                     }
                 }

                 if (data.getCompanyNameListData()[i].getNationUid() == 81) {
                     japaneseName = data.getCompanyNameListData()[i].getCompanyName()
                     if (!TextUtils.isEmpty(japaneseName) && japaneseName != "null") {
                         mCompanyJapaneseNameEditText.setText(japaneseName)
                     }
                 }
             }
         }

        if (mNativeImageUploadListData.size > 0) {
            mNativeImageUploadListData.clear()
        }

        val nativeImageUploadData = NativeImageUploadData()
        nativeImageUploadData.setViewType(0x00)
        mNativeImageUploadListData.add(nativeImageUploadData)

        if (data.getCompanyImageListData().size > 0) {
            for (i in data.getCompanyImageListData().indices) {
                val imageUploadData = NativeImageUploadData()
                imageUploadData.setViewType(0x01)
                imageUploadData.setImageUrl(data.getCompanyImageListData()[i].getCompanyImageUrl())
                imageUploadData.setImageUid(data.getCompanyImageListData()[i].getCompanyImageUid())

                mNativeImageUploadListData.add(imageUploadData)
            }
        }
        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)

        if (!TextUtils.isEmpty(data.getCompanyAddress())) {
            mModifyAddressTextView.text = data.getCompanyAddress()
            mAddressDetailEditText.setText(data.getCompanyAddressDetail())
        }

        if (!TextUtils.isEmpty(data.getCompanyLatitude().toString()) && !TextUtils.isEmpty(data.getCompanyLongitude().toString())) {
            mLocationTextView.text = data.getCompanyLatitude().toString() + ", " + data.getCompanyLongitude().toString()
            mLatitude = data.getCompanyLatitude().toString()
            mLongitude = data.getCompanyLongitude().toString()
        }

        if (!TextUtils.isEmpty(data.getCompanyManagerName()) && data.getCompanyManagerName() != "null") {
            mManagerNameEditText.setText(data.getCompanyManagerName())
        }

        if (!TextUtils.isEmpty(data.getCompanyPhone()) && data.getCompanyPhone() != "null") {
            mPhoneNumberEditText.setText(data.getCompanyPhone())
        }

        if (!TextUtils.isEmpty(data.getCompanyEmail()) && data.getCompanyEmail() != "null") {
            mEmailEditText.setText(data.getCompanyEmail())
        }

        if (!TextUtils.isEmpty(data.getCompanyFacebookUrl()) && data.getCompanyFacebookUrl() != "null") {
            mFacebookEditText.setText(data.getCompanyFacebookUrl())
        }

        if (!TextUtils.isEmpty(data.getCompanyKakaoUrl()) && data.getCompanyKakaoUrl() != "null") {
            mKakaoEditText.setText(data.getCompanyKakaoUrl())
        }

        if (!TextUtils.isEmpty(data.getCompanyLineUrl()) && data.getCompanyLineUrl() != "null") {
            mLineEditText.setText(data.getCompanyLineUrl())
        }

        if (!TextUtils.isEmpty(data.getCompanyDescription()) && data.getCompanyDescription() != "null") {
            mCompanyDescriptionEditText.setText(data.getCompanyDescription())
        }

        if (!TextUtils.isEmpty(data.getCompanyLicenseImageUrl())) {
            if (!data.getCompanyLicenseImageUrl().contains(".pdf")) {
                mLicenseImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getCompanyLicenseImageUrl()))
            }
        }

        mLicenseFullScreenView.setOnClickListener {
            if (!data.getCompanyLicenseImageUrl().contains(".pdf")) {
                val imageList = ArrayList<String>()
                imageList.add(DEFAULT_IMAGE_DOMAIN + data.getCompanyLicenseImageUrl())

                mListener.onFullScreenImage(imageList, 0)
            }
        }

        if (!TextUtils.isEmpty(data.getCompanyLicenseNumber())) {
            mLicenseNumberTextView.text = data.getCompanyLicenseNumber()
        }

        if (!TextUtils.isEmpty(data.getCompanyLicenseImageOrigin()) && data.getCompanyLicenseImageOrigin() != "null") {
            mLicenseFileNameTextView.text = data.getCompanyLicenseImageOrigin()
        }

        if (!TextUtils.isEmpty(data.getCompanyLicenseMasterName())) {
            mOwnerNameTextView.text = data.getCompanyLicenseMasterName()
        }

        if (!TextUtils.isEmpty(data.getCompanyManagerName())) {
            mManagerNameTextView.text = data.getCompanyManagerName()
        }



//        if (!TextUtils.isEmpty(data.getCompanyLicenseImageUrl()) && data.getCompanyLicenseImageUrl() != "null") {
//            mLicenseLinkImageView.setOnClickListener {
//                try {
//                    val uri = Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getCompanyLicenseImageUrl())
//                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                    startActivity(intent)
//                } catch (e: ActivityNotFoundException) {
//                    NagajaLog().e("wooks, You don't have any browser to open web page")
//                }
//            }
//        }

        // TODO: Implement License Number, Owner Name, Manager Name
//        if (data.get) {
//
//        }
    }

    private fun displayCompanyProductData(dataList: ArrayList<CompanyProductData>) {
        mProductCountTextView.text = String.format(mActivity.resources.getString(R.string.text_product_count), dataList.size)
        mCompanyProductItemAdapter.setData(dataList)
    }

    private fun displayCompanyManagerData(dataList: ArrayList<CompanyManagerData>) {

        val masterListData = ArrayList<CompanyManagerData>()
        val managerListData = ArrayList<CompanyManagerData>()
        val frontListData = ArrayList<CompanyManagerData>()
        val waiterListData = ArrayList<CompanyManagerData>()
        val defaultStaffListData = ArrayList<CompanyManagerData>()
        val posSystemListData = ArrayList<CompanyManagerData>()
        val etcListData = ArrayList<CompanyManagerData>()

        for (i in dataList.indices) {
            if (dataList[i].getCompanyManagerGrant() == 1) {
                masterListData.add(dataList[i])
            }

            if (dataList[i].getCompanyManagerGrant() == 2) {
                managerListData.add(dataList[i])
            }

            if (dataList[i].getCompanyManagerGrant() == 3) {
                frontListData.add(dataList[i])
            }

            if (dataList[i].getCompanyManagerGrant() == 4) {
                waiterListData.add(dataList[i])
            }

            if (dataList[i].getCompanyManagerGrant() == 5) {
                defaultStaffListData.add(dataList[i])
            }

            if (dataList[i].getCompanyManagerGrant() == 6) {
                posSystemListData.add(dataList[i])
            }

            if (dataList[i].getCompanyManagerGrant() == 7) {
                etcListData.add(dataList[i])
            }
        }

        if (masterListData.isNotEmpty()) {
            mSalesMasterView.visibility = View.VISIBLE
            mCompanySalesMasterAdapter.setData(masterListData)
        } else {
            mSalesMasterView.visibility = View.GONE
        }

        if (managerListData.isNotEmpty()) {
            mSalesManagerView.visibility = View.VISIBLE
            mCompanySalesManagerAdapter.setData(managerListData)
        } else {
            mSalesManagerView.visibility = View.GONE
        }

        if (frontListData.isNotEmpty()) {
            mSalesFrontView.visibility = View.VISIBLE
            mCompanySalesFrontAdapter.setData(frontListData)
        } else {
            mSalesFrontView.visibility = View.GONE
        }

        if (waiterListData.isNotEmpty()) {
            mSalesWaiterView.visibility = View.VISIBLE
            mCompanySalesWaiterAdapter.setData(waiterListData)
        } else {
            mSalesWaiterView.visibility = View.GONE
        }

        if (defaultStaffListData.isNotEmpty()) {
            mSalesDefaultStaffView.visibility = View.VISIBLE
            mCompanySalesDefaultStaffAdapter.setData(defaultStaffListData)
        } else {
            mSalesDefaultStaffView.visibility = View.GONE
        }

        if (posSystemListData.isNotEmpty()) {
            mSalesPosSystemView.visibility = View.VISIBLE
            mCompanySalesPosSystemAdapter.setData(posSystemListData)
        } else {
            mSalesPosSystemView.visibility = View.GONE
        }

        if (etcListData.isNotEmpty()) {
            mSalesEtcView.visibility = View.VISIBLE
            mCompanySalesEtcAdapter.setData(etcListData)
        } else {
            mSalesEtcView.visibility = View.GONE
        }

    }

    private fun displayCompanySalesData(data: CompanySalesData) {

        if (data.getIsCompanyDeliveryUseYn()) {
            selectAddFeatures(mSalesDeliveryTextView, true)
        }

        if (data.getIsCompanyReservationUseYn()) {
            selectAddFeatures(mSalesReservationTextView, true)
        }

        if (data.getIsCompanyPickupUseYn()) {
            selectAddFeatures(mSalesPickUpTextView, true)
        }

        if (data.getIsCompanyParkingUseYn()) {
            selectAddFeatures(mSalesParkingTextView, true)
        }

        if (data.getIsCompanyPetUseYn()) {
            selectAddFeatures(mSalesPetTextView, true)
        }

        setRegularHolidayTypeToText()
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

    private fun showCustomImageUploadDialog() {
        val customDialogBottom = CustomDialogBottom(mActivity, isImage = true, isProfileModify = false)
        customDialogBottom.setOnCustomDialogBottomListener(object : CustomDialogBottom.OnCustomDialogBottomListener {
            override fun onCamera() {
                mListener.onCameraImage(false)
                customDialogBottom.dismiss()
            }

            override fun onGallery() {
                mListener.onGalleryImageSelect(false)
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

    private fun showCustomModifyDefaultInformation() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_company_default_change_information),
            mActivity.resources.getString(R.string.text_message_modify_detault_information),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                getModifyCompanyDefaultInformation()
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    private fun showProductRemovePopup(itemName: String, itemUid: Int, companyUid: Int) {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_product_information_remove),
            itemName + "\n" + mActivity.resources.getString(R.string.text_message_product_remove),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getCompanyProductRemove(itemName, itemUid, companyUid)
            }
        })
        customDialog.show()
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

    private fun fileUpload() {
        val customDialogBottom = CustomDialogBottom(mActivity, isImage = false, isProfileModify = false)
        customDialogBottom.setOnCustomDialogBottomListener(object : CustomDialogBottom.OnCustomDialogBottomListener {
            override fun onCamera() {
                customDialogBottom.dismiss()
                mListener.onCameraImage(true)
            }

            override fun onGallery() {
                mListener.onGalleryImageSelect(true)
                customDialogBottom.dismiss()
            }

            override fun onSelectFile() {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
//                intent.type = "image/*"
                intent.type = "application/pdf"

                startActivityForResult(intent, INTENT_FILE_UPLOAD_REQUEST_CODE)

                customDialogBottom.dismiss()
            }

            override fun onDeleteProfileImage() {
            }

            override fun onCancel() {
                customDialogBottom.dismiss()
            }
        })
        customDialogBottom.show(requireActivity().supportFragmentManager, "Custom Dialog")
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

    @SuppressLint("Range")
    fun selectImage(uri: Uri, isFile: Boolean) {
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


        val file = File(Util().getPathFromUri(mActivity, uri)!!)
        val fileSize: Long = (file.length() / 1024)
        NagajaLog().d("wooks, Select File Size = $fileSize")

        if (fileSize > 30000) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_30mb_less))
            return
        }

        if (isFile) {
            val fileUploadData = FileUploadData()
            fileUploadData.setFileName(getFileName(uri)!!)
            fileUploadData.setFileUri(uri)
            mFileUriListData.add(fileUploadData)

            mFileUploadAdapter.setData(mFileUriListData)
        } else {

            imageUploadData.setFileName(fileName)
            mImageUploadListData.add(imageUploadData)

            val nativeImageUploadData = NativeImageUploadData()
            nativeImageUploadData.setViewType(0x01)
            nativeImageUploadData.setIsDeviceImage(true)
            nativeImageUploadData.setImageUri(uri)

            mNativeImageUploadListData.add(nativeImageUploadData)

            mNativeImageUploadAdapter.setData(mNativeImageUploadListData)
        }



//        val imageUploadData = ImageUploadData()
//        imageUploadData.setFileName(Uri.decode(result))
//        if (!isFile) {
//            imageUploadData.setImageBitmap(bitmap!!)
//        }
//        imageUploadData.setFilePath(Util().getPathFromUri(this@MainActivity, uri)!!)
//
//        getUploadImageFile(imageUploadData, isFile, uri)


    }

    fun getCameraImageData(uri: Uri, currentPhotoPath: String, isFile: Boolean) {
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

            if (isFile) {
                val fileUploadData = FileUploadData()
                fileUploadData.setFileName(getFileName(uri)!!)
                fileUploadData.setFileUri(uri)
                mFileUriListData.add(fileUploadData)

                mFileUploadAdapter.setData(mFileUriListData)
            } else {
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
    }

    @SuppressLint("SetTextI18n")
    fun selectMap(address: AddressData) {
        if (!TextUtils.isEmpty(address.getAddress())) {
            mModifyAddressTextView.text = address.getAddress()
            mAddressDetailEditText.setText("")
        }

        if (address.getLatitude() > 0.0) {
            mLatitude = address.getLatitude().toString()
        }

        if (address.getLongitude() > 0.0) {
            mLongitude = address.getLongitude().toString()
        }

        if (!TextUtils.isEmpty(mLatitude) && !TextUtils.isEmpty(mLongitude)) {
            mLocationTextView.text = "$mLatitude, $mLongitude"
        }
    }

    override fun onResume() {
        super.onResume()

        setRegularHolidayTypeToText()

        NagajaLog().e("wooks, onResume()")
    }

    override fun onStart() {
        super.onStart()

        NagajaLog().e("wooks, onStart()")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        NagajaLog().d("wooks, Main Activity onActivityResult:$requestCode:$resultCode:$data")

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                INTENT_FILE_UPLOAD_REQUEST_CODE -> {
                    if (null != data) {
                        val uri: Uri = data.data!!
                        NagajaLog().d("wooks, File Name = ${getFileName(uri)}")

                        val fileUploadData = FileUploadData()
                        fileUploadData.setFileName(getFileName(uri)!!)
                        fileUploadData.setFileUri(uri)
                        mFileUriListData.add(fileUploadData)

                        mFileUploadAdapter.setData(mFileUriListData)
                    }
                }
            }
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
        if (context is OnCompanyDefaultFragmentListener) {
            mActivity = context as Activity

            if (context is OnCompanyDefaultFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnCompanyDefaultFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnCompanyDefaultFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnCompanyDefaultFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // ==========================================================================================
    // API
    // ==========================================================================================

    /**
     * API. Get Company Information
     */
    private fun getCompanyInformation() {
        if (TextUtils.isEmpty(MAPP.USER_DATA.getSecureKey())) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<CompanyInformationData> {
                override fun onSuccess(resultData: CompanyInformationData) {
                    if (null == resultData) {
                        return
                    }

                    mCompanyInformationData = resultData
                    displayCompanyDefaultInformation(resultData)
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
            getCompanyDefaultData().getCompanyUid()
        )
    }

    /**
     * API. Get Company Manager Data
     */
    private fun getCompanyManagerData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyManagerData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CompanyManagerData>> {
                override fun onSuccess(resultData: ArrayList<CompanyManagerData>) {
                    MAPP.COMPANY_MANAGER_LIST_DATA = resultData
                    displayCompanyManagerData(resultData)

                    getCompanySalesDefaultInformation()
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
            getCompanyDefaultData().getCompanyUid(),
        )
    }

    /**
     * API. Get Company Manager Data
     */
    private fun getCompanySalesDefaultInformation() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanySalesDefaultInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<CompanySalesData> {
                override fun onSuccess(resultData: CompanySalesData) {

                    MAPP.COMPANY_SALES_DATA = resultData

                    displayCompanySalesData(resultData)
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
            getCompanyDefaultData().getCompanyUid(),
        )
    }

    /**
     * API. Get Company Add Features
     */
    private fun getCompanyChangeAddFeatures(position: Int, isSelect: Boolean) {
        var salesData = MAPP.COMPANY_SALES_DATA
        when (position) {
            SELECT_ADD_FEATURES_POSITION_DELIVERY -> {
                salesData.setIsCompanyDeliveryUseYn(isSelect)
            }

            SELECT_ADD_FEATURES_POSITION_RESERVATION -> {
                salesData.setIsCompanyReservationUseYn(isSelect)
            }

            SELECT_ADD_FEATURES_POSITION_PICK_UP -> {
                salesData.setIsCompanyPickupUseYn(isSelect)
            }

            SELECT_ADD_FEATURES_POSITION_PARKING -> {
                salesData.setIsCompanyParkingUseYn(isSelect)
            }

            SELECT_ADD_FEATURES_POSITION_PET -> {
                salesData.setIsCompanyPetUseYn(isSelect)
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyChangeAddFeatures(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    when (position) {
                        SELECT_ADD_FEATURES_POSITION_DELIVERY -> {
                            MAPP.COMPANY_SALES_DATA.setIsCompanyDeliveryUseYn(isSelect)
                            selectAddFeatures(mSalesDeliveryTextView, isSelect)
                        }

                        SELECT_ADD_FEATURES_POSITION_RESERVATION -> {
                            MAPP.COMPANY_SALES_DATA.setIsCompanyReservationUseYn(isSelect)
                            selectAddFeatures(mSalesReservationTextView, isSelect)
                        }

                        SELECT_ADD_FEATURES_POSITION_PICK_UP -> {
                            MAPP.COMPANY_SALES_DATA.setIsCompanyPickupUseYn(isSelect)
                            selectAddFeatures(mSalesPickUpTextView, isSelect)
                        }

                        SELECT_ADD_FEATURES_POSITION_PARKING -> {
                            MAPP.COMPANY_SALES_DATA.setIsCompanyParkingUseYn(isSelect)
                            selectAddFeatures(mSalesParkingTextView, isSelect)
                        }

                        SELECT_ADD_FEATURES_POSITION_PET -> {
                            MAPP.COMPANY_SALES_DATA.setIsCompanyPetUseYn(isSelect)
                            selectAddFeatures(mSalesPetTextView, isSelect)
                        }
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
            getCompanyDefaultData().getCompanyUid(),
            MAPP.COMPANY_SALES_DATA.getCompanyServiceUid(),
            salesData.getIsCompanyDeliveryUseYn(),
            salesData.getIsCompanyReservationUseYn(),
            salesData.getIsCompanyPickupUseYn(),
            salesData.getIsCompanyParkingUseYn(),
            salesData.getIsCompanyPetUseYn()
        )
    }

    /**
     * API. Get Company Product Item Data
     */
    private fun getCompanyProductItemData() {
        if (TextUtils.isEmpty(MAPP.USER_DATA.getSecureKey())) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyProductItemData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CompanyProductData>> {
                override fun onSuccess(resultData: ArrayList<CompanyProductData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    displayCompanyProductData(resultData)
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
            getCompanyDefaultData().getCompanyUid().toString(),
            getCompanyDefaultData().getCategoryUid().toString()
        )
    }

    /**
     * API. Get Company Reservation Person Limit
     */
    private fun getCompanyReservationPersonLimit(personLimit: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyReservationPersonLimit(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_change_reservation_person_limt))

                    mPersonLimitEditText.setText(personLimit.toString())
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            personLimit,
            MAPP.COMPANY_SALES_DATA.getCompanyServiceUid(),
            MAPP.COMPANY_SALES_DATA.getCompanyUid()
        )
    }

    /**
     * API. Get Company Use Currency
     */
    private fun getCompanyUseCurrency(currency: Int) {

        var currencyUid = 0
        var status = false
        when (currency) {
            SELECT_ADD_CURRENCY_POSITION_DOLLAR -> {
                currencyUid = CURRENCY_UID_DOLLAR
                for (i in MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList().indices) {
                    if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_DOLLAR) {
                        status = !MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()
                        break
                    }
                }
            }

            SELECT_ADD_CURRENCY_POSITION_WON -> {
                currencyUid = CURRENCY_UID_WON
                for (i in MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList().indices) {
                    if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_WON) {
                        status = !MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()
                        break
                    }
                }
            }

            SELECT_ADD_CURRENCY_POSITION_PESO -> {
                currencyUid = CURRENCY_UID_PESO
                for (i in MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList().indices) {
                    if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_PESO) {
                        status = !MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()
                        break
                    }
                }
            }

            SELECT_ADD_CURRENCY_POSITION_YUAN -> {
                currencyUid = CURRENCY_UID_YUAN
                for (i in MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList().indices) {
                    if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_YUAN) {
                        status = !MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()
                        break
                    }
                }
            }

            SELECT_ADD_CURRENCY_POSITION_YEN -> {
                currencyUid = CURRENCY_UID_YEN
                for (i in MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList().indices) {
                    if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == CURRENCY_UID_YEN) {
                        status = !MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()
                        break
                    }
                }
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyUseCurrency(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    for (i in MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList().indices) {
                        if (MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getCurrencyUid() == currencyUid) {
                            status = !MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].getIsCurrencyBase()
                            MAPP.COMPANY_SALES_DATA.getCompanySalesCurrencyDataList()[i].setIsCurrencyBase(status)
                            break
                        }
                    }

                    displayCurrencySetting()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            status,
            MAPP.COMPANY_SALES_DATA.getCompanyUid(),
            currencyUid
        )
    }

    /**
     * API. Get Modify Company Default Information
     */
    private fun getModifyCompanyDefaultInformation() {

        setLoadingView(true)

        var deleteImageUid = ""
        for (i in mDeleteImageUidListData.indices) {
            deleteImageUid = if (TextUtils.isEmpty(deleteImageUid)) {
                mDeleteImageUidListData[i]
            } else {
                deleteImageUid + "," + mDeleteImageUidListData[i]
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getModifyCompanyInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showToast(mActivity, mActivity.resources.getString(R.string.text_message_success_modify_company_information))
                    mListener.onBack()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                }
            },
            mActivity,
            MAPP.USER_DATA.getSecureKey(),
            mCompanyInformationData.getCompanyUid(),
            mCompanyEnglishNameEditText.text.toString(),
            mModifyAddressTextView.text.toString(),
            mAddressDetailEditText.text.toString(),
            mLatitude,
            mLongitude,
            mManagerNameEditText.text.toString(),
            SharedPreferencesUtil().getSaveNationCode(mActivity).toString(),
            mPhoneNumberEditText.text.toString(),
            mEmailEditText.text.toString(),
            mFacebookEditText.text.toString(),
            mKakaoEditText.text.toString(),
            mLineEditText.text.toString(),
            SharedPreferencesUtil().getSaveNationCode(mActivity).toString(),
            MAPP.USER_DATA.getMemberUid(),
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            SharedPreferencesUtil().getSaveMainAreaCode(mActivity),
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            mCompanyInformationData.getCompanyUid(),
            deleteImageUid,
            mCompanyEnglishNameEditText.text.toString(),
            mCompanyKoreaNameEditText.text.toString(),
            mCompanyJapaneseNameEditText.text.toString(),
            mCompanyChineseNameEditText.text.toString(),
            mCompanyFilipinoNameEditText.text.toString(),
            mCompanyDescriptionEditText.text.toString(),
            mImageUploadListData,
            mFileUriListData
        )
    }

    /**
     * API. Get Company Product Remove
     */
    private fun getCompanyProductRemove(itemName: String, itemUid: Int, companyUid: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyProductRemove(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    showToast(mActivity, "$itemName ${mActivity.resources.getString(R.string.text_message_success_product_remove)}")
                    getCompanyProductItemData()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            itemUid,
            companyUid
        )
    }
}