package com.nagaja.app.android.ApplicationCompanyMember

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.ImageDecoder
import android.location.Geocoder
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
import com.nagaja.app.android.Adapter.FileUploadAdapter
import com.nagaja.app.android.Adapter.NativeImageUploadAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_FILE_UPLOAD_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.R
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_application_company_member.view.*
import kotlinx.android.synthetic.main.fragment_job_register.view.*
import kotlinx.android.synthetic.main.fragment_missing_write.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ApplicationCompanyMemberFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View

    private lateinit var mCompanyNameEditText: EditText
    private lateinit var mCompanyAddressDetailEditText: EditText
    private lateinit var mManagerNameEditText: EditText
    private lateinit var mPhoneNumberEditText: EditText
    private lateinit var mEmailEditText: EditText
    private lateinit var mFacebookEditText: EditText
    private lateinit var mKakaoEditText: EditText
    private lateinit var mLineEditText: EditText

    private lateinit var mCompanyAddressTextView: TextView
    private lateinit var mLocationTextView: TextView

    private lateinit var mCompanyTypeSpinner: PowerSpinnerView

    private lateinit var mImageUploadRecyclerView: RecyclerView
    private lateinit var mFileUploadRecyclerView: RecyclerView

    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter
    private lateinit var mFileUploadAdapter: FileUploadAdapter

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnApplicationCompanyMemberFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mCompanyTypeDataList: ArrayList<CompanyTypeData>

    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()

    private var mFileUriListData = ArrayList<FileUploadData>()


    private var mCompanyTypeSelectPosition = 0
    private var mSelectCategoryUid = 0

    private var mCurrentPhotoPath = ""
    private var mCaptureImageFileName = ""
    private var mLatitude = ""
    private var mLongitude = ""
    private var mNationCode = ""

    interface OnApplicationCompanyMemberFragmentListener {
        fun onFinish()
        fun onGalleryImageSelect(isFile: Boolean)
        fun onCameraImage(isFile: Boolean)
        fun onMapScreen()
        fun onSuccessRegister()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        fun newInstance(): ApplicationCompanyMemberFragment {
            val args = Bundle()
            val fragment = ApplicationCompanyMemberFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)
        mFileUploadAdapter = FileUploadAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_application_company_member, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_enterprise_application)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Company Type Spinner
        mCompanyTypeSpinner = mContainer.fragment_application_company_member_type_spinner
        mCompanyTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mCompanyTypeSelectPosition = newIndex

            mSelectCategoryUid = mCompanyTypeDataList[newIndex].getCategoryUid()
        })

        // Image Upload Recycler View
        mImageUploadRecyclerView = mContainer.fragment_application_company_member_image_upload_recycler_view
        mImageUploadRecyclerView.setHasFixedSize(true)
        mImageUploadRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)

        mNativeImageUploadAdapter.setOnItemClickListener(object : NativeImageUploadAdapter.OnItemClickListener {
            override fun onClick(data: NativeImageUploadData, position: Int) {
                mNativeImageUploadListData.removeAt(position)
                mNativeImageUploadAdapter.setData(mNativeImageUploadListData)

                if (mImageUploadListData.isNotEmpty()) {
                    if (mNativeImageUploadListData.size <= position) {
                        mImageUploadListData.removeAt((position - mNativeImageUploadListData.size))
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

        // Company Name Edit Text
        mCompanyNameEditText = mContainer.fragment_application_company_member_company_name_edit_text
        mCompanyNameEditText.filters = arrayOf(InputFilter.LengthFilter(30))
        mCompanyNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Company Address Text View
        mCompanyAddressTextView = mContainer.fragment_application_company_member_address_text_view
        mCompanyAddressTextView.setOnClickListener {
            mListener.onMapScreen()
        }

        // Company Address Detail Edit Text
        mCompanyAddressDetailEditText = mContainer.fragment_application_company_member_address_detail_edit_text
        mCompanyAddressDetailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Location Text View
        mLocationTextView = mContainer.fragment_application_company_member_location_text_view

        // Manager Name Edit Text
        mManagerNameEditText = mContainer.fragment_application_company_member_manager_name_edit_text
        mManagerNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Phone Number Edit Text
        mPhoneNumberEditText = mContainer.fragment_application_company_member_phone_number_edit_text
        mPhoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Email Edit Text
        mEmailEditText = mContainer.fragment_application_company_member_email_edit_text
        mEmailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Facebook Edit Text
        mFacebookEditText = mContainer.fragment_application_company_member_facebook_edit_text
        mFacebookEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Kakao Edit Text
        mKakaoEditText = mContainer.fragment_application_company_member_kakao_edit_text
        mKakaoEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Line Edit Text
        mLineEditText = mContainer.fragment_application_company_member_line_edit_text
        mLineEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        // Map Image View
        val mapImageView = mContainer.fragment_application_company_member_map_image_view
        mapImageView.setOnSingleClickListener {
            mListener.onMapScreen()
        }




        // File Upload Image View
        val fileUploadImageView = mContainer.fragment_application_company_member_file_upload_image_view
        fileUploadImageView.setOnClickListener {
            if (mFileUriListData.size == 3) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_upload_file_count))
            } else {
                fileUploadDialog()
            }
        }

        // File Upload Recycler View
        mFileUploadRecyclerView = mContainer.fragment_application_company_member_file_upload_recycler_view
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


        // Register Text View
        val registerTextView = mContainer.fragment_application_company_member_register_text_view
        registerTextView.setOnSingleClickListener {
            checkCompanyApplicationRegister()
        }


        // Loading View
        mLoadingView = mContainer.fragment_application_company_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_application_company_loading_lottie_view


        getCompanyTypeList()
        setDefaultSelect()
    }

    private fun setDefaultSelect() {
        val imageUploadData = NativeImageUploadData()
        imageUploadData.setViewType(0x00)

        mNativeImageUploadListData.add(imageUploadData)

        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)
    }

    private fun checkCompanyApplicationRegister() {
        if (mSelectCategoryUid == 0) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_select_compnay_type))
            return
        } else if (mImageUploadListData.isEmpty()) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_register_one_company_image))
            return
        } else if (TextUtils.isEmpty(mCompanyNameEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_company_name))
            return
        } else if (TextUtils.isEmpty(mLocationTextView.text.toString()) || TextUtils.isEmpty(mLatitude) || TextUtils.isEmpty(mLongitude)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_company_location))
            return
        } else if (TextUtils.isEmpty(mManagerNameEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_manager_name))
            return
        } else if (TextUtils.isEmpty(mPhoneNumberEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_phone_number_2))
            return
        } else if (TextUtils.isEmpty(mEmailEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_email_information))
            return
        } else if (!Util().checkEmail(mEmailEditText.text.toString())) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_valid_email))
            return
        } else if (TextUtils.isEmpty(mEmailEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_email_information))
            return
        } else if (mFileUriListData.isEmpty()) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_register_attachment))
            return
        }

        showCustomRegisterConfirmPopup()
    }

    private fun showCustomRegisterConfirmPopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_enterprise_application),
            mActivity.resources.getString(R.string.text_message_register_company_application),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                getApplicationCompany()
            }
        })
        customDialog.show()
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

    private fun showRegisterResultPopup(isSuccess: Boolean) {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            if (isSuccess) mActivity.resources.getString(R.string.text_title_success_register) else mActivity.resources.getString(R.string.text_noti),
            if (isSuccess) mActivity.resources.getString(R.string.text_message_success_company_register) else mActivity.resources.getString(R.string.text_error_unknown_error),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()

                if (isSuccess) {
                    mListener.onFinish()
                }
            }
        })
        customDialog.show()
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

    private fun showSuccessRegisterCustomDialog() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_application_company_member),
            mCompanyNameEditText.text.toString() + "\n\n" + mActivity.resources.getString(R.string.text_message_success_register),
            "",
            mActivity.resources.getString(R.string.text_confirm),
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()

                mListener.onSuccessRegister()
            }
        })
        customDialog.show()
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

    private fun getAddress(lan: Double, lon: Double) {
        var address = ""
        val geocoder = Geocoder(mActivity, Locale.getDefault())
        (mActivity).runOnUiThread(
            Runnable {
                try {
                    val addressList = geocoder.getFromLocation(lan, lon, 1)
                    if (addressList!!.size != 0) {
                        address = addressList[0].getAddressLine(0)
                        mNationCode = addressList[0].countryCode
                        mNationCode = "63"
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
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

//    fun getLocation(latitude: String, longitude: String) {
//        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
//            mLocationEditText.setText("$latitude, $longitude")
//        }
//    }

    @SuppressLint("SetTextI18n")
    fun selectMap(address: AddressData) {
        if (!TextUtils.isEmpty(address.getAddress())) {
            mCompanyAddressTextView.text = address.getAddress()
        }

        if (address.getLatitude() > 0.0) {
            mLatitude = address.getLatitude().toString()
        }

        if (address.getLongitude() > 0.0) {
            mLongitude = address.getLongitude().toString()
        }

        if (!TextUtils.isEmpty(mLatitude) && !TextUtils.isEmpty(mLongitude)) {
            mLocationTextView.text = "$mLatitude, $mLongitude"
            getAddress(mLatitude.toDouble(), mLongitude.toDouble())
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

    override fun onPause() {
        super.onPause()

        if (mCompanyTypeSpinner.isShowing) {
            mCompanyTypeSpinner.dismiss()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        NagajaLog().d("wooks, Main Activity onActivityResult:$requestCode:$resultCode:$data")

        if (resultCode == Activity.RESULT_OK) {
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
//                    mFileUriListData.add(fileUploadData)
//
//                    mFileUploadAdapter.setData(mFileUriListData)
//
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
        if (context is OnApplicationCompanyMemberFragmentListener) {
            mActivity = context as Activity

            if (context is OnApplicationCompanyMemberFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnApplicationCompanyMemberFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnApplicationCompanyMemberFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnApplicationCompanyMemberFragmentListener"
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
     * API. Get Location Name
     */
    private fun getCompanyTypeList() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getApplicationCompanyMemberType(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CompanyTypeData>> {
                override fun onSuccess(resultData: ArrayList<CompanyTypeData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    mCompanyTypeDataList = resultData

                    val typeList = ArrayList<String>()
                    for (i in resultData.indices) {
                        typeList.add(resultData[i].getCategoryName())
                    }

                    mCompanyTypeSpinner.setItems(typeList)
                }

                override fun onFail(errorMsg: String?) {
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
     * API. Get Application Company
     */
    fun getApplicationCompany() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getApplicationCompany(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showSuccessRegisterCustomDialog()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                    setLoadingView(false)
                }
            },
            mActivity,
            MAPP.USER_DATA.getSecureKey(),
            mSelectCategoryUid,
            mCompanyNameEditText.text.toString(),
            mCompanyAddressTextView.text.toString(),
            mCompanyAddressDetailEditText.text.toString(),
            mLatitude,
            mLongitude,
            mManagerNameEditText.text.toString(),
            mNationCode,
            mPhoneNumberEditText.text.toString(),
            mEmailEditText.text.toString(),
            mFacebookEditText.text.toString(),
            mKakaoEditText.text.toString(),
            mLineEditText.text.toString(),
            mNationCode,
            MAPP.USER_DATA.getMemberUid(),
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            SharedPreferencesUtil().getSaveMainAreaCode(mActivity),
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            mImageUploadListData,
            mFileUriListData
        )
    }
}