package com.nagaja.app.android.BoardWrite

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
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.Data.NativeImageUploadData
import com.nagaja.app.android.Data.StoreCategoryData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_board_write.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BoardWriteFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View

    private lateinit var mTitleEditText: EditText
    private lateinit var mContentEditText: EditText

    private lateinit var mCategorySpinner: PowerSpinnerView

    private lateinit var mImageUploadRecyclerView: RecyclerView

    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter

    private lateinit var mListener: OnBoardWriteFragmentListener

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mCategoryListData = ArrayList<StoreCategoryData>()
    private val mCategoryNameListData = ArrayList<String>()
    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()
    private var mDeleteImageUidListData = ArrayList<String>()

    private var mCategorySelectPosition = -1
    private var mCategoryUid = 0

    private var mCurrentPhotoPath = ""

    interface OnBoardWriteFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onDelete(selectType: Int, position: Int)
        fun onGalleryImageSelect()
        fun onCameraImage()
        fun onSuccess()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        const val ARGS_BOARD_CATEGORY_DATA                      = "args_board_category_data"
        const val ARGS_BOARD_UID_DATA                           = "args_board_uid_data"
        const val ARGS_BOARD_MODIFY_DATA                        = "args_board_modify_data"

        fun newInstance(categoryUid: Int, boardUid: Int, isModify: Boolean): BoardWriteFragment {
            val args = Bundle()
            val fragment = BoardWriteFragment()
            args.putInt(ARGS_BOARD_CATEGORY_DATA, categoryUid)
            args.putInt(ARGS_BOARD_UID_DATA, boardUid)
            args.putBoolean(ARGS_BOARD_MODIFY_DATA, isModify)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getBoardCategory(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_BOARD_CATEGORY_DATA) as Int
    }

    private fun getBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_BOARD_UID_DATA) as Int
    }

    private fun getIsModify(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_BOARD_MODIFY_DATA) as Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_board_write, container, false)

        init()

        return mContainer
    }

    @SuppressLint("ClickableViewAccessibility")
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
        titleTextView.text = mActivity.resources.getString(R.string.text_board_category_talking_room)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Category Spinner
        mCategorySpinner = mContainer.fragment_board_write_category_spinner
        mCategorySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mCategorySelectPosition = newIndex

            for (i in mCategoryListData.indices) {
                if (i == newIndex) {
                    mCategoryUid = mCategoryListData[i].getCategoryUid()
                    break
                }
            }
        })

        // Title Edit Text
        mTitleEditText = mContainer.fragment_board_write_title_edit_text
        mTitleEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Content Edit Text
        mContentEditText = mContainer.fragment_board_write_content_edit_text
        mContentEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mContentEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.fragment_board_write_content_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        // Image Upload Recycler View
        mImageUploadRecyclerView = mContainer.fragment_board_write_image_upload_recycler_view
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

        // Cancel Text View
        val cancelTextView = mContainer.fragment_board_write_cancel_text_view
        cancelTextView.setOnClickListener {
            mListener.onFinish()
        }

        // Register Text View
        val registerTextView = mContainer.fragment_board_write_register_text_view
        registerTextView.text = if (getIsModify()) mActivity.resources.getString(R.string.text_modify) else mActivity.resources.getString(R.string.text_register)
        registerTextView.setOnClickListener {
            checkRegister()
        }


        // Loading View
        mLoadingView = mContainer.fragment_board_write_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_board_write_loading_lottie_view



        setDefaultSelect()
        getBoardCategoryData()
    }

    private fun setDefaultSelect() {
        val imageUploadData = NativeImageUploadData()
        imageUploadData.setViewType(0x00)

        mNativeImageUploadListData.add(imageUploadData)

        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)

    }

    private fun checkRegister() {
        if (mCategorySelectPosition == -1) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_category))
            return
        } else if (TextUtils.isEmpty(mTitleEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_board_title))
            return
        } else if (TextUtils.isEmpty(mContentEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_recommend_content))
            return
        }

        if (getIsModify()) {
            getBoardRegisterModify()
        } else {
            getBoardRegisterSave()
        }
    }

    private fun displayBoardModifyData(data: BoardData) {

        for (i in mCategoryListData.indices) {
            if (mCategoryListData[i].getCategoryUid() == data.getCategoryUid()) {
                mCategorySpinner.selectItemByIndex(i)
                break
            }
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
        if (context is OnBoardWriteFragmentListener) {
            mActivity = context as Activity

            if (context is OnBoardWriteFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnBoardWriteFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnBoardWriteFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnBoardWriteFragmentListener"
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
    private fun getBoardCategoryData() {
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

                    if (getIsModify()) {
                        getBoardDetail()
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
            COMPANY_TYPE_TALK_ROOM.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Board Detail
     */
    private fun getBoardDetail() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<BoardData> {
                override fun onSuccess(resultData: BoardData) {

                    setLoadingView(false)

                    displayBoardModifyData(resultData)
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
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Board Register Save
     */
    private fun getBoardRegisterSave() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardRegisterSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_board_register_write))

                    mListener.onSuccess()
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
            mCategoryUid,
            MAPP.USER_DATA.getMemberUid(),
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            SharedPreferencesUtil().getSaveMainAreaCode(mActivity),
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            mImageUploadListData
        )
    }

    /**
     * API. Get Board Register Modify
     */
    private fun getBoardRegisterModify() {

        var deleteImageUid = ""
        for (i in mDeleteImageUidListData.indices) {
            deleteImageUid = if (TextUtils.isEmpty(deleteImageUid)) {
                mDeleteImageUidListData[i]
            } else {
                deleteImageUid + "," + mDeleteImageUidListData[i]
            }
        }

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardRegisterModify(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_board_register_write))

                    mListener.onSuccess()
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
            mCategoryUid,
            MAPP.USER_DATA.getMemberUid(),
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            SharedPreferencesUtil().getSaveMainAreaCode(mActivity),
            SharedPreferencesUtil().getSaveSubAreaCode(mActivity),
            deleteImageUid,
            mImageUploadListData
        )
    }
}