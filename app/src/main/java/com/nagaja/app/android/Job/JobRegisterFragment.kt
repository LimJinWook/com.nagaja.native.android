package com.nagaja.app.android.Job

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.ImageDecoder
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Adapter.FileUploadAdapter
import com.nagaja.app.android.Adapter.NativeImageUploadAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_FILE_UPLOAD_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.Job.JobFragment.Companion.SELECT_TYPE_JOB
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_job_register.view.*
import kotlinx.android.synthetic.main.fragment_missing_write.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class JobRegisterFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mSnsInformationView: View

    private lateinit var mTitleEditText: EditText
    private lateinit var mContentEditText: EditText
    private lateinit var mAreaDescEditText: EditText
    private lateinit var mSnsInformationEditText: EditText

    private lateinit var mCompanyNameTextView: TextView
    private lateinit var mCategoryTextView: TextView
    private lateinit var mStartDateTextView: TextView
    private lateinit var mEndDateTextView: TextView
    private lateinit var mUploadFileNameTextView: TextView

    private lateinit var mCategorySpinner: PowerSpinnerView
    private lateinit var mMainAreaSpinner: PowerSpinnerView
    private lateinit var mSubAreaSpinner: PowerSpinnerView

    private lateinit var mImageUploadRecyclerView: RecyclerView
    private lateinit var mFileUploadRecyclerView: RecyclerView

    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter
    private lateinit var mFileUploadAdapter: FileUploadAdapter

    private lateinit var mListener: OnJobRegisterFragmentListener

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()

    private var mNationListData = ArrayList<LocationNationData>()
    private var mLocationAreaListData = ArrayList<LocationAreaMainData>()
    private var mMainAreaNameListData = ArrayList<String>()
    private var mSubAreaNameListData = ArrayList<String>()
    private var mDeleteImageUidListData = ArrayList<String>()
    private var mFileUriListData = ArrayList<FileUploadData>()
    private var mDeleteFileUidListData = ArrayList<String>()

    private lateinit var mBoardData: BoardData

    private var mSelectCategory = -1
    private var mMainAreaSelectPosition = -1
    private var mSubAreaSelectPosition = -1
    private var mMainAreaUid = 0
    private var mSubAreaUid = 0
    private var mCategory = 0

    private var mCurrentPhotoPath = ""
    private var mLatitude = ""
    private var mLongitude = ""

    var mIsFirstSetting = true

    interface OnJobRegisterFragmentListener {
        fun onBack()
        fun onMapScreen()
        fun onCameraImage(isFile: Boolean)
        fun onGalleryImageSelect(isFile: Boolean)
        fun onCompanyRegisterSuccess()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_JOB_TYPE                                    = "args_job_type"
        const val ARGS_COMPANY_UID_DATA                            = "args_company_uid_data"
        const val ARGS_MODIFY_BOARD_UID                            = "args_modify_board_uid"

        const val ARGS_JOB_SELECT_TYPE_PARENT_JOB                  = 40
        const val ARGS_JOB_SELECT_TYPE_PARENT_JOB_SEARCH           = 41
        const val ARGS_JOB_SELECT_TYPE_SHOT                        = 42
        const val ARGS_JOB_SELECT_TYPE_LONG                        = 43
        const val ARGS_JOB_SEARCH_SELECT_TYPE_SHOT                 = 44
        const val ARGS_JOB_SEARCH_SELECT_TYPE_LONG                 = 45

        fun newInstance(jobType: Int, companyUid: Int, modifyBoardUid: Int): JobRegisterFragment {
            val args = Bundle()
            val fragment = JobRegisterFragment()
            args.putInt(ARGS_JOB_TYPE, jobType)
            args.putInt(ARGS_COMPANY_UID_DATA, companyUid)
            args.putInt(ARGS_MODIFY_BOARD_UID, modifyBoardUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getSelectType(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_JOB_TYPE) as Int
    }

    private fun getCompanyUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_COMPANY_UID_DATA) as Int
    }

    private fun getModifyBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_MODIFY_BOARD_UID) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)
        mFileUploadAdapter = FileUploadAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_job_register, container, false)

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
        headerShareImageView.setOnSingleClickListener {
//            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_SEARCH)
        }

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
        titleTextView.text = mActivity.resources.getString(R.string.text_job_and_job_search)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Company Name Text View
        mCompanyNameTextView = mContainer.fragment_job_register_company_name_text_view

        // Job Category Text View
        mCategoryTextView = mContainer.fragment_job_register_job_and_job_search_text_view
//        jobAndJobSearchTextView.text = if (getSelectType() == SELECT_TYPE_JOB) mActivity.resources.getString(R.string.text_job) else  mActivity.resources.getString(R.string.text_job_search)
        if (getSelectType() == ARGS_JOB_SELECT_TYPE_PARENT_JOB) {
            mCategoryTextView.text = mActivity.resources.getString(R.string.text_job)
        } else if (getSelectType() == ARGS_JOB_SELECT_TYPE_PARENT_JOB_SEARCH) {
            mCategoryTextView.text = mActivity.resources.getString(R.string.text_job_search)
        }

        // Category Spinner
        mCategorySpinner = mContainer.fragment_job_register_category_spinner
        mCategorySpinner.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                mCategorySpinner.dismiss()
            }
        }
        mCategorySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSelectCategory = newIndex
        })

        // Title Edit Text
        mTitleEditText = mContainer.fragment_job_register_title_edit_text
        mTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Start Date View
        val startDateView = mContainer.fragment_job_register_start_date_view
        startDateView.setOnClickListener {
            showDatePickerDialog(mStartDateTextView)
        }

        // Start Date Text View
        mStartDateTextView = mContainer.fragment_job_register_start_date_text_view
        mStartDateTextView.text = Util().getTodayDate()

        // End Date View
        val endDateView = mContainer.fragment_job_register_end_date_view
        endDateView.setOnClickListener {
            showDatePickerDialog(mEndDateTextView)
        }

        // End Date Text View
        mEndDateTextView = mContainer.fragment_job_register_end_date_text_view
        mEndDateTextView.text = Util().getTodayDate()


        // Main Area Spinner
        mMainAreaSpinner = mContainer.fragment_job_register_main_area_spinner
        mMainAreaSpinner.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                mMainAreaSpinner.dismiss()
            }
        }
        mMainAreaSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mMainAreaSelectPosition = newIndex
            mMainAreaUid = mLocationAreaListData[newIndex].getCategoryUid()

            if (!mIsFirstSetting) {
                mSubAreaNameListData.clear()
                mSubAreaSelectPosition = -1
                mSubAreaSpinner.text = mActivity.resources.getString(R.string.text_select)

                for (i in mLocationAreaListData[newIndex].getLocationAreaSubListData().indices) {
                    mSubAreaNameListData.add(mLocationAreaListData[newIndex].getLocationAreaSubListData()[i].getCategoryName())
                }
                mSubAreaSpinner.setItems(mSubAreaNameListData)
            }
        })

        // Sub Area Spinner
        mSubAreaSpinner = mContainer.fragment_job_register_sub_area_spinner
        mSubAreaSpinner.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                mSubAreaSpinner.dismiss()
            }
        }
        mSubAreaSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSubAreaSelectPosition = newIndex
            mSubAreaUid = mLocationAreaListData[mMainAreaSelectPosition].getLocationAreaSubListData()[newIndex].getCategoryUid()
        })

        // Area Desc Edit Text
        mAreaDescEditText = mContainer.fragment_job_register_area_desc_edit_text
        mAreaDescEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Address Image View
        val addressImageView = mContainer.fragment_job_register_address_image_view
        addressImageView.setOnClickListener {
            mListener.onMapScreen()
        }

        // SNS Information View
        mSnsInformationView = mContainer.fragment_job_register_sns_information_view
        mSnsInformationView.visibility = if (getSelectType() == SELECT_TYPE_JOB) View.GONE else View.VISIBLE

        // SNS Information Edit Text
        mSnsInformationEditText = mContainer.fragment_job_register_sns_information_edit_text
        mSnsInformationEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Image Upload Recycler View
        mImageUploadRecyclerView = mContainer.fragment_job_register_image_upload_recycler_view
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

                if (getModifyBoardUid() > 0) {
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

        // File Upload Image View
        val fileUploadImageView = mContainer.fragment_job_register_file_upload_image_view
        fileUploadImageView.setOnClickListener {
            if (mFileUriListData.size == 3) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_upload_file_count))
            } else {
                fileUploadDialog()
            }
        }

        mFileUploadRecyclerView = mContainer.fragment_job_register_file_upload_recycler_view
        mFileUploadRecyclerView.setHasFixedSize(true)
        mFileUploadRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mFileUploadAdapter.setOnItemClickListener(object : FileUploadAdapter.OnItemClickListener {
            override fun onDelete(data: FileUploadData, position: Int) {
                mFileUploadAdapter.deleteItem(position)
                mFileUriListData.removeAt(position)

                if (getModifyBoardUid() > 0) {
                    if (data.getIsUploadImage()) {
                        mDeleteFileUidListData.add(data.getFileUid().toString())
                    }
                }
            }
        })
        mFileUploadRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mFileUploadRecyclerView.adapter = mFileUploadAdapter

        // Content Edit Text
        mContentEditText = mContainer.fragment_job_register_content_edit_text
        mContentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        mContentEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.fragment_job_register_content_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        // Loading View
        mLoadingView = mContainer.fragment_job_register_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_job_register_loading_lottie_view

        // Register Text View
        val registerTextView = mContainer.fragment_job_register_text_view
        registerTextView.setOnClickListener {
            checkRegister()
        }


        setDefaultSelect()
        getStoreCategoryData()


        if (getModifyBoardUid() > 0) {
            getBoardDetail()
        }
    }

    private fun setDefaultSelect() {
        val imageUploadData = NativeImageUploadData()
        imageUploadData.setViewType(0x00)

        mNativeImageUploadListData.add(imageUploadData)

        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)

    }

    private fun checkRegister() {
        if (mSelectCategory < 0) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_category))
            return
        } else if (mMainAreaSelectPosition < 0 || mSubAreaSelectPosition < 0) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_select_area))
            return
        } else if (TextUtils.isEmpty(mTitleEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_board_title))
            return
        } else if (TextUtils.isEmpty(mContentEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_recommend_content))
            return
        }

        getJobRegisterSave()
    }

    private fun displayBoardData(data: BoardData) {

        // Company Name Text View
        if (!TextUtils.isEmpty(data.getCompanyName())) {
            mCompanyNameTextView.visibility = View.VISIBLE
            mCompanyNameTextView.text = data.getCompanyName()
        }

        // Category Text View
        if (getCompanyUid() > 0) {
            mCategoryTextView.text = mActivity.resources.getString(R.string.text_job)
        } else {
            if (data.getParentCategoryUid() == ARGS_JOB_SELECT_TYPE_PARENT_JOB) {
                mCategoryTextView.text = mActivity.resources.getString(R.string.text_job)
            } else {
                mCategoryTextView.text = mActivity.resources.getString(R.string.text_job_search)
            }
        }

        // Category Spinner
        if (data.getCategoryUid() == ARGS_JOB_SELECT_TYPE_SHOT || data.getCategoryUid() == ARGS_JOB_SEARCH_SELECT_TYPE_SHOT) {
            mCategorySpinner.selectItemByIndex(0)
        } else if (data.getCategoryUid() == ARGS_JOB_SELECT_TYPE_LONG || data.getCategoryUid() == ARGS_JOB_SEARCH_SELECT_TYPE_LONG) {
            mCategorySpinner.selectItemByIndex(1)
        }

        // Title Edit Text
        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            mTitleEditText.setText(data.getBoardSubject())
        }

        // Start Date Text View
        if (!TextUtils.isEmpty(data.getBeginDate()) && !TextUtils.isEmpty(data.getEndDate())) {
            mStartDateTextView.text = getUtcToString(data.getBeginDate())
            mEndDateTextView.text = getUtcToString(data.getEndDate())
        }

        // Area Desc Edit Text View
        if (!TextUtils.isEmpty(data.getLocationDesc())) {
            mAreaDescEditText.setText(data.getLocationDesc())
        }
        if (!TextUtils.isEmpty(data.getLatitude())) {
            mLatitude = data.getLatitude()
        }
        if (!TextUtils.isEmpty(data.getLongitude())) {
            mLongitude = data.getLongitude()
        }

        // Image List
        if (data.getBoardImageListData().size > 0) {
            for (i in data.getBoardImageListData().indices) {
                val nativeImageUploadData = NativeImageUploadData()
                nativeImageUploadData.setViewType(0x01)
                nativeImageUploadData.setIsDeviceImage(false)
                nativeImageUploadData.setImageUrl(data.getBoardImageListData()[i].getItemImageName())
                nativeImageUploadData.setImageUid(data.getBoardImageListData()[i].getItemImageUid())

                mNativeImageUploadListData.add(nativeImageUploadData)
            }

            mNativeImageUploadAdapter.setData(mNativeImageUploadListData)
        }

        // File List
        if (data.getBoardFileListData().size > 0) {
            for (i in data.getBoardFileListData().indices) {
                val fileUploadData = FileUploadData()
                fileUploadData.setFileUid(data.getBoardFileListData()[i].getBoardFileUid())
                fileUploadData.setFileName(data.getBoardFileListData()[i].getBoardFileOrigin())

                mFileUriListData.add(fileUploadData)
            }

            mFileUploadAdapter.setData(mFileUriListData)
        }

        // Content Edit Text
        if (!TextUtils.isEmpty(data.getBoardContent())) {
            mContentEditText.setText(data.getBoardContent())
        }
    }

    private fun getUtcToString(utcDate: String): String {
        if (!TextUtils.isEmpty(utcDate)) {
            var time = utcDate
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            return outputFormatter.format(parsed.atZoneSameInstant(zone))
        }
        return ""
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog(textView: TextView) {
        val datePickerDialog = DatePickerDialog(mActivity, R.style.DialogTheme)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            val afterMonth = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
            val afterDay = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()

            textView.text = "$year-$afterMonth-$afterDay"
        }
        datePickerDialog.show()
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

    private fun setAreaSpinner() {

        setLoadingView(false)

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

        mIsFirstSetting = false

//        if (getIsModify()) {
//            getBoardDetail()
//        }
    }

    private fun fileUploadDialog() {

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

    }

    @SuppressLint("SimpleDateFormat")
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

            val timeStamp: String = SimpleDateFormat("yyyy_MM_dd").format(Date(System.currentTimeMillis()))

            if (isFile) {
                val fileUploadData = FileUploadData()
                fileUploadData.setFileName(getFileName(uri)!!)
                fileUploadData.setFileUri(uri)
                fileUploadData.setIsUploadImage(false)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        NagajaLog().d("wooks, Main Activity onActivityResult:$requestCode:$resultCode:$data")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
//                INTENT_FILE_UPLOAD_REQUEST_CODE -> {
//                    val files: ArrayList<MediaFile> = data!!.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
//                    NagajaLog().d("wooks, REQUEST_CODE_PICK_FILE size = ${files.size}")
//                    NagajaLog().d("wooks, REQUEST_CODE_PICK_FILE File Name = ${files[0].name}")
//                    NagajaLog().d("wooks, REQUEST_CODE_PICK_FILE File Path = ${files[0].path}")
//                    NagajaLog().d("wooks, REQUEST_CODE_PICK_FILE File Uri = ${files[0].uri}")
//
//                    NagajaLog().d("wooks, File Name = ${getFileName(files[0].uri)}")
//
//
//
//                    val fileUploadData = FileUploadData()
//                    fileUploadData.setFileName(getFileName(files[0].uri)!!)
//                    fileUploadData.setFileUri(files[0].uri)
//                    fileUploadData.setIsUploadImage(false)
//                    mFileUriListData.add(fileUploadData)
//
//                    mFileUploadAdapter.setData(mFileUriListData)
//                }

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
        if (context is OnJobRegisterFragmentListener) {
            mActivity = context as Activity

            if (context is OnJobRegisterFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnJobRegisterFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnJobRegisterFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnJobRegisterFragmentListener"
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
     * API. Get Store Category Data
     */
    fun getStoreCategoryData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<StoreCategoryData>> {
                override fun onSuccess(resultData: ArrayList<StoreCategoryData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    val categoryName = ArrayList<String>()
                    for (i in resultData.indices) {
                        categoryName.add(resultData[i].getCategoryName())
                    }

                    mCategorySpinner.setItems(categoryName)

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
            getSelectType().toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Location Nation
     */
    private fun getLocationNation() {

        setLoadingView(true)

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
            selectNation.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Board Register Save
     */
    private fun getJobRegisterSave() {

        if (getSelectType() == SELECT_TYPE_JOB) {
            if (mSelectCategory == 0) {
                mCategory = ARGS_JOB_SELECT_TYPE_SHOT
            } else if (mSelectCategory == 1) {
                mCategory = ARGS_JOB_SELECT_TYPE_LONG
            }
        } else {
            if (mSelectCategory == 0) {
                mCategory = ARGS_JOB_SEARCH_SELECT_TYPE_SHOT
            } else if (mSelectCategory == 1) {
                mCategory = ARGS_JOB_SEARCH_SELECT_TYPE_LONG
            }
        }

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()

        if (getModifyBoardUid() > 0) {

            var deleteImageUid = ""
            var deleteFileUid = ""

            if (mDeleteImageUidListData.size > 0) {
                for (i in mDeleteImageUidListData.indices) {
                    if (TextUtils.isEmpty(deleteImageUid)) {
                        deleteImageUid = mDeleteImageUidListData[i]
                    } else {
                        deleteImageUid = deleteImageUid + "," + mDeleteImageUidListData[i]
                    }
                }
            }

            if (mDeleteFileUidListData.size > 0) {
                for (i in mDeleteFileUidListData.indices) {
                    if (TextUtils.isEmpty(deleteFileUid)) {
                        deleteFileUid = mDeleteFileUidListData[i]
                    } else {
                        deleteFileUid = deleteFileUid + "," + mDeleteFileUidListData[i]
                    }
                }
            }

            nagajaManager?.getJobModify(
                mRequestQueue,
                object : NagajaManager.RequestListener<String> {
                    override fun onSuccess(resultData: String) {
                        setLoadingView(false)

                        showToast(mActivity, mActivity.resources.getString(R.string.text_success_board_register_write))

                        if (getCompanyUid() > 0) {
                            mListener.onCompanyRegisterSuccess()
                        } else {
                            mListener.onBack()
                        }
                    }

                    override fun onFail(errorMsg: String?) {
                        NagajaLog().e("wooks, errorMsg = $errorMsg")
                        showToast(mActivity, errorMsg!!)
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
                mTitleEditText.text.toString(),
                mContentEditText.text.toString(),
                mCategory,
                MAPP.USER_DATA.getMemberUid(),
                SharedPreferencesUtil().getSaveNationCode(mActivity),
                mMainAreaUid,
                mSubAreaUid,
                mAreaDescEditText.text.toString(),
                mSnsInformationEditText.text.toString(),
                mStartDateTextView.text.toString() + " 00:00:00",
                mEndDateTextView.text.toString() + " 00:00:00",
                mImageUploadListData,
                mFileUriListData,
                getCompanyUid(),
                mLatitude,
                mLongitude,
                deleteImageUid,
                deleteFileUid,
                mBoardData.getBoardUid()
            )

        } else {
            nagajaManager?.getJobRegisterSave(
                mRequestQueue,
                object : NagajaManager.RequestListener<String> {
                    override fun onSuccess(resultData: String) {
                        setLoadingView(false)

                        showToast(mActivity, mActivity.resources.getString(R.string.text_success_board_register_write))

                        if (getCompanyUid() > 0) {
                            mListener.onCompanyRegisterSuccess()
                        } else {
                            mListener.onBack()
                        }
                    }

                    override fun onFail(errorMsg: String?) {
                        NagajaLog().e("wooks, errorMsg = $errorMsg")
                        showToast(mActivity, errorMsg!!)
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
                mTitleEditText.text.toString(),
                mContentEditText.text.toString(),
                mCategory,
                MAPP.USER_DATA.getMemberUid(),
                SharedPreferencesUtil().getSaveNationCode(mActivity),
                mMainAreaUid,
                mSubAreaUid,
                mAreaDescEditText.text.toString(),
                mSnsInformationEditText.text.toString(),
                mStartDateTextView.text.toString() + " 00:00:00",
                mEndDateTextView.text.toString() + " 00:00:00",
                mImageUploadListData,
                mFileUriListData,
                getCompanyUid(),
                mLatitude,
                mLongitude
            )
        }
    }

    /**
     * API. Get Board Detail
     */
    fun getBoardDetail() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardMissingAndJobDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<BoardData> {
                override fun onSuccess(resultData: BoardData) {
                    mBoardData = resultData
                    displayBoardData(resultData)
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
            getModifyBoardUid(),
            false
        )
    }
}