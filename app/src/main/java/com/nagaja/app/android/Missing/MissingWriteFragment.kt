package com.nagaja.app.android.Missing

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
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.Missing.MissingFragment.Companion.SELECT_TYPE_MISSING_REPORT
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_missing_write.view.*
import kotlinx.android.synthetic.main.fragment_used_market_register.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MissingWriteFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View

    private lateinit var mAreaDescEditText: EditText
    private lateinit var mTitleEditText: EditText
    private lateinit var mContentEditText: EditText

    private lateinit var mCategoryTextView: TextView

    private lateinit var mMainAreaSpinner: PowerSpinnerView
    private lateinit var mSubAreaSpinner: PowerSpinnerView

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mImageUploadRecyclerView: RecyclerView

    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter

    private lateinit var mListener: OnMissingWriteFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()

    private var mNationListData = ArrayList<LocationNationData>()
    private var mLocationAreaListData = ArrayList<LocationAreaMainData>()
    private var mMainAreaNameListData = ArrayList<String>()
    private var mSubAreaNameListData = ArrayList<String>()
    private var mDeleteImageUidListData = ArrayList<String>()

    private var mCurrentPhotoPath = ""
    private var mCaptureImageFileName = ""
    private var mLatitude = ""
    private var mLongitude = ""

    private var mCategoryType = 0
    private var mMainAreaSelectPosition = -1
    private var mSubAreaSelectPosition = -1
    private var mMainAreaUid = 0
    private var mSubAreaUid = 0

    private var mIsFirstSetting = true

    interface OnMissingWriteFragmentListener {
        fun onBack()
        fun onSuccess(isDetailScreen: Boolean)
        fun onGalleryImageSelect()
        fun onCameraImage()
        fun onMapScreen()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_WRITE_TYPE                       = "args_write_type"
        const val ARGS_BOARD_UID_DATA                   = "args_board_uid_data"
        const val ARGS_IS_MODIFY_DATA                   = "args_is_modify_data"

        fun newInstance(writeType: Int, boardUid: Int, isModify: Boolean): MissingWriteFragment {
            val args = Bundle()
            val fragment = MissingWriteFragment()
            args.putInt(ARGS_WRITE_TYPE, writeType)
            args.putInt(ARGS_BOARD_UID_DATA, boardUid)
            args.putBoolean(ARGS_IS_MODIFY_DATA, isModify)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getWriteType(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_WRITE_TYPE) as Int
    }

    private fun getBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_BOARD_UID_DATA) as Int
    }

    private fun getIsModify(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_MODIFY_DATA) as Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_missing_write, container, false)

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
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_report_and_missing_person)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onBack()
        }

        // Category Text View
        mCategoryTextView = mContainer.fragment_missing_write_category_text_view
        mCategoryTextView.text = if (getWriteType() == SELECT_TYPE_MISSING_REPORT) mActivity.resources.getString(R.string.text_report) else mActivity.resources.getString(R.string.text_missing_person)

        // Main Area Spinner
        mMainAreaSpinner = mContainer.fragment_missing_write_main_area_spinner
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
        mSubAreaSpinner = mContainer.fragment_missing_write_sub_area_spinner
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
        mAreaDescEditText = mContainer.fragment_missing_write_area_desc_edit_text
        mAreaDescEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Address Image View
        val addressImageView = mContainer.fragment_missing_write_address_image_view
        addressImageView.setOnClickListener {
            mListener.onMapScreen()
        }

        // Title Edit Text
        mTitleEditText = mContainer.fragment_missing_write_title_edit_text
        mTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Content Edit Text
        mContentEditText = mContainer.fragment_missing_write_content_edit_text
        mContentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        mContentEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.fragment_missing_write_content_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        // Image Upload Recycler View
        mImageUploadRecyclerView = mContainer.fragment_missing_write_image_upload_recycler_view
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

                if (getIsModify()) {
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
        val registerTextView = mContainer.fragment_missing_write_register_text_view
        registerTextView.setOnClickListener {
            checkRegister()
        }

        // Loading View
        mLoadingView = mContainer.fragment_missing_write_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_missing_write_loading_lottie_view



        mCategoryType = getWriteType()


        setDefaultSelect()
        getLocationNation()


    }

    private fun setDefaultSelect() {
        val imageUploadData = NativeImageUploadData()
        imageUploadData.setViewType(0x00)

        mNativeImageUploadListData.add(imageUploadData)

        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)

    }

    private fun displayBoardModifyData(data: BoardData) {

        mCategoryType = data.getCategoryUid()
        mCategoryTextView.text = if (mCategoryType == SELECT_TYPE_MISSING_REPORT) mActivity.resources.getString(R.string.text_report) else mActivity.resources.getString(R.string.text_missing_person)

        if (!TextUtils.isEmpty(data.getLocationDesc())) {
            mAreaDescEditText.setText(data.getLocationDesc())
        }

        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            mTitleEditText.setText(data.getBoardSubject())
        }

        if (!TextUtils.isEmpty(data.getBoardContent())) {
            mContentEditText.setText(data.getBoardContent())
        }

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

    private fun checkRegister() {
        if (mMainAreaSelectPosition < 0 || mSubAreaSelectPosition < 0) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_select_area))
            return
        } else if (TextUtils.isEmpty(mTitleEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_board_title))
            return
        } else if (TextUtils.isEmpty(mContentEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_recommend_content))
            return
        }

        if (getIsModify()) {
            getBoardMissingRegisterModify()
        } else {
            getBoardMissingRegisterSave()
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

    fun getCameraImageUpload() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(mActivity.packageManager) != null) {

            var photoFile: File? = null
            try {
                val tempDir: File = mActivity.cacheDir

                val timeStamp: String = SimpleDateFormat("yyyyMMdd").format(Date())
                mCaptureImageFileName = "Capture_" + timeStamp + "_"
                val tempImage: File = File.createTempFile(mCaptureImageFileName, ".png", tempDir)

                mCurrentPhotoPath = tempImage.absolutePath
                photoFile = tempImage
            } catch (e: IOException) {
                NagajaLog().e("wooks, Screen Shot Image Create Error! : $e")
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(mActivity, "${mActivity.packageName}.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
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

//    fun setSelectLocationData(selectMapData: SelectMapData) {
//        mMainAddressTextView.text = selectMapData.getMainAddress()
//        mSubAddressTextView.text = selectMapData.getSubAddress()
//        mLocationTextView.text = selectMapData.getLocation()
//    }

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

        if (getIsModify()) {
            getBoardDetail()
        }
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
        if (context is OnMissingWriteFragmentListener) {
            mActivity = context as Activity

            if (context is OnMissingWriteFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnMissingWriteFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnMissingWriteFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnMissingWriteFragmentListener"
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
     * API. Get Board Detail
     */
    fun getBoardDetail() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardMissingAndJobDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<BoardData> {
                override fun onSuccess(resultData: BoardData) {
                    displayBoardModifyData(resultData)
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
            getBoardUid(),
            true
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
    private fun getBoardMissingRegisterSave() {

        setLoadingView(true)

        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(System.currentTimeMillis()))

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardMissingRegisterSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_board_register_write))

                    mListener.onBack()
                    mListener.onSuccess(true)
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
            mContentEditText.text.toString(),
            mCategoryType,
            MAPP.USER_DATA.getMemberUid(),
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            mMainAreaUid,
            mSubAreaUid,
            mAreaDescEditText.text.toString(),
            currentTime,
            mImageUploadListData
        )
    }

    /**
     * API. Get Board Register Missing Modify
     */
    private fun getBoardMissingRegisterModify() {

        var deleteImageUid = ""
        for (i in mDeleteImageUidListData.indices) {
            deleteImageUid = if (TextUtils.isEmpty(deleteImageUid)) {
                mDeleteImageUidListData[i]
            } else {
                deleteImageUid + "," + mDeleteImageUidListData[i]
            }
        }

        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(System.currentTimeMillis()))

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardMissingRegisterModify(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_board_register_write))

                    mListener.onBack()
                    mListener.onSuccess(false)
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
            getBoardUid(),
            mTitleEditText.text.toString(),
            mContentEditText.text.toString(),
            mCategoryType,
            MAPP.USER_DATA.getMemberUid(),
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            mMainAreaUid,
            mSubAreaUid,
            deleteImageUid,
            mAreaDescEditText.text.toString(),
            currentTime,
            mImageUploadListData
        )
    }
}