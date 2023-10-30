package com.nagaja.app.android.Report

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.ImageDecoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Adapter.NativeImageUploadAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.NativeImageUploadData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_report.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View

    private lateinit var mReportContentEditText: EditText

    private lateinit var mReportTypeSpinner: PowerSpinnerView

    private lateinit var mImageUploadRecyclerView: RecyclerView

    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter

    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnReportFragmentListener

    private var mSelectReportType = 0

    private var mCurrentPhotoPath = ""

    private lateinit var mRequestQueue: RequestQueue

    interface OnReportFragmentListener {
        fun onFinish()
        fun onGalleryImageSelect()
        fun onCameraImage()
        fun onSuccess()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_REPORT_TYPE                  = "args_report_type"
        const val ARGS_REPORT_UID                   = "args_report_uid"
        const val ARGS_REPORT_STORE_NAME            = "args_report_store_name"
        const val ARGS_COMPANY_UIU                  = "args_company_uiu"

        fun newInstance(reportType: Int, reportUid: Int, reportStoreName: String, companyUid: Int): ReportFragment {
            val args = Bundle()
            val fragment = ReportFragment()
            args.putInt(ARGS_REPORT_TYPE, reportType)
            args.putInt(ARGS_REPORT_UID, reportUid)
            args.putString(ARGS_REPORT_STORE_NAME, reportStoreName)
            args.putInt(ARGS_COMPANY_UIU, companyUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getReportType(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_REPORT_TYPE) as Int
    }

    private fun getReportUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_REPORT_UID) as Int
    }

    private fun getReportStoreName(): String {
        val arguments = arguments
        return arguments?.getString(ARGS_REPORT_STORE_NAME) as String
    }

    private fun getCompanyUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_COMPANY_UIU) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_report, container, false)

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
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_report)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Store Name Text View
        val storeNameTextView = mContainer.fragment_report_store_name_text_view
        if (!TextUtils.isEmpty(getReportStoreName())) {
            storeNameTextView.visibility = View.VISIBLE
            storeNameTextView.text = getReportStoreName()
        } else {
            storeNameTextView.visibility = View.GONE
        }

        // Report Type Spinner
        mReportTypeSpinner = mContainer.fragment_report_type_spinner
        mReportTypeSpinner.selectItemByIndex(0)
        mReportTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSelectReportType = newIndex
        })

        // Report Content Edit Text
        mReportContentEditText = mContainer.fragment_report_content_edit_text
        mReportContentEditText.filters = arrayOf(InputFilter.LengthFilter(200))
        mReportContentEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mReportContentEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.fragment_report_content_edit_text) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        // Image Upload Recycler View
        mImageUploadRecyclerView = mContainer.fragment_report_image_upload_recycler_view
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
                    mImageUploadListData.removeAt(position - 1)
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

        // Loading View
        mLoadingView = mContainer.fragment_report_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_report_loading_lottie_view

        // Report Upload Text View
        val reportUploadTextView = mContainer.fragment_report_upload_text_view
        reportUploadTextView.setOnClickListener {
            if (mSelectReportType == 0) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_report_type))
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(mReportContentEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_contents))
                return@setOnClickListener
            }

            getReportUpload(mReportContentEditText.text.toString())
        }

        setDefaultSelect()
    }

    private fun setDefaultSelect() {
        val imageUploadData = NativeImageUploadData()
        imageUploadData.setViewType(0x00)

        mNativeImageUploadListData.add(imageUploadData)

        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)
    }

    private fun setLoadingView(isLoading: Boolean) {
        if (isLoading) {
            mLoadingView.visibility = View.VISIBLE
            mLoadingLottieView.speed = 2f
            mLoadingLottieView.playAnimation()
        } else {
            mLoadingView.visibility = View.GONE
            mLoadingLottieView.pauseAnimation()
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
        if (context is OnReportFragmentListener) {
            mActivity = context as Activity

            if (context is OnReportFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnReportFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnReportFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnReportFragmentListener"
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
     * API. Get Store Detail
     */
    private fun getReportUpload(content: String) {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReportUpload(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_report))

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
            getReportType().toString(),
            mSelectReportType.toString(),
            content,
            getReportUid().toString(),
            getCompanyUid(),
            mImageUploadListData
        )
    }
}