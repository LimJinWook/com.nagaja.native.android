package com.nagaja.app.android.Note

import android.annotation.SuppressLint
import android.app.Activity
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
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Data.NoteData
import com.nagaja.app.android.Adapter.FileUploadAdapter
import com.nagaja.app.android.Adapter.NativeImageUploadAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_FILE_UPLOAD_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.FileUploadData
import com.nagaja.app.android.Data.MemberInformationData
import com.nagaja.app.android.Data.NativeImageUploadData
import com.nagaja.app.android.Data.NoteMemberSearchData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_job_register.view.*
import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.fragment_note_detail.view.*
import kotlinx.android.synthetic.main.fragment_note_reply.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NoteReplyFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mNoteActivity: NoteActivity

    private lateinit var mContainer: View
    private lateinit var mNoteReplyView: View
    private lateinit var mNoteReplyEmptyView: View
    private lateinit var mLoadingView: View
    private lateinit var mCompanySenderView: View

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mRecipientInputEditText: EditText
    private lateinit var mContentEditText: EditText

    private lateinit var mReplyReceiveNameTextView: TextView
    private lateinit var mReplyReceiveMemberTextView: TextView
    private lateinit var mReplyReceivePhoneNumberTextView: TextView
    private lateinit var mReplyReceiveContentTextView: TextView
    private lateinit var mCompanySenderTextView: TextView

    private lateinit var mRecipientSpinner: PowerSpinnerView
    private lateinit var mMemberSearchSpinner: PowerSpinnerView

    private lateinit var mImageUploadRecyclerView: RecyclerView
    private lateinit var mFileUploadRecyclerView: RecyclerView

    private lateinit var mNativeImageUploadAdapter: NativeImageUploadAdapter
    private lateinit var mFileUploadAdapter: FileUploadAdapter

    private lateinit var mListener: OnNoteReplyFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mLoadingLottieView: LottieAnimationView

    private var mNativeImageUploadListData = ArrayList<NativeImageUploadData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()
    private var mFileUriListData = ArrayList<FileUploadData>()
    private var mNoteMemberSearchListData = ArrayList<NoteMemberSearchData>()

    private var mNoteMemberSearchData = NoteMemberSearchData()

    private var mSelectRecipientValue = 0

    private var mCurrentPhotoPath = ""

    interface OnNoteReplyFragmentListener {
        fun onBack()
        fun onSuccess()
        fun onCameraImage(isFile: Boolean)
        fun onGalleryImageSelect(isFile: Boolean)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        const val ARGS_NOTE_DATA                   = "args_note_data"
        const val ARGS_IS_REPLY_DATA               = "args_is_reply_data"

        fun newInstance(data: NoteData): NoteReplyFragment {
            val args = Bundle()
            val fragment = NoteReplyFragment()
            args.putSerializable(ARGS_NOTE_DATA, data)
            args.putBoolean(ARGS_IS_REPLY_DATA, true)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): NoteReplyFragment {
            val args = Bundle()
            val fragment = NoteReplyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getNoteData(): NoteData {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_NOTE_DATA) as NoteData
    }

    private fun getIsReply(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_REPLY_DATA) as Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity: Activity = requireActivity()
        if (activity is NoteActivity) {
            mNoteActivity = activity
        }

        mNativeImageUploadAdapter = NativeImageUploadAdapter(mActivity, false)
        mFileUploadAdapter = FileUploadAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_note_reply, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_send_reply)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Scroll View
        mScrollView = mContainer.fragment_note_reply_scroll_view

        // Note Reply View
        mNoteReplyView = mContainer.fragment_note_reply_view
        mNoteReplyView.visibility = if (getIsReply()) View.VISIBLE else View.GONE

        // Reply Receive Name Text View
        mReplyReceiveNameTextView = mContainer.fragment_note_reply_receive_name_text_view
        if (getIsReply() && !TextUtils.isEmpty(getNoteData().getSendMemberName())) {
            mReplyReceiveNameTextView.text = getNoteData().getSendMemberName()
        } else if (getIsReply() && !TextUtils.isEmpty(getNoteData().getCompanyName())) {
            mReplyReceiveNameTextView.text = getNoteData().getCompanyName()
        }

        // Reply Receive Member Text View
        mReplyReceiveMemberTextView = mContainer.fragment_note_reply_receive_member_text_view

        // Reply Receive Phone Number Text View
        mReplyReceivePhoneNumberTextView = mContainer.fragment_note_reply_receive_phone_number_text_view

        // Reply Receive Content Text View
        mReplyReceiveContentTextView = mContainer.fragment_note_reply_receive_content_text_view
        if (getIsReply() && !TextUtils.isEmpty(getNoteData().getNoteMessage())) {
            mReplyReceiveContentTextView.text = getNoteData().getNoteMessage()
            enableScroll(mReplyReceiveContentTextView)
        }

        // Company Sender View
        mCompanySenderView = mContainer.fragment_note_company_sender_view
        if (mNoteActivity.mIsCompanyNote) {
            mCompanySenderView.visibility = View.VISIBLE
        } else {
            mCompanySenderView.visibility = View.GONE
        }

        // Company Sender Text View
        mCompanySenderTextView = mContainer.fragment_note_reply_company_note_sender_text_view
        if (mNoteActivity.mIsCompanyNote) {
            if (!TextUtils.isEmpty(mNoteActivity.mCompanyDefaultData!!.getCompanyNameEnglish())) {
                mCompanySenderTextView.text = mNoteActivity.mCompanyDefaultData!!.getCompanyNameEnglish()
            }
        }

        // Note Reply Empty View
        mNoteReplyEmptyView = mContainer.fragment_note_reply_empty_view
        mNoteReplyEmptyView.visibility = if (getIsReply()) View.GONE else View.VISIBLE

        // Recipient Spinner
        mRecipientSpinner = mContainer.fragment_note_reply_recipient_spinner
        mRecipientSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSelectRecipientValue = newIndex + 1

            when (newIndex) {
                0 -> {
                    mRecipientInputEditText.hint = mActivity.resources.getString(R.string.text_hint_input_id)
                    mRecipientInputEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }

                1 -> {
                    mRecipientInputEditText.hint = mActivity.resources.getString(R.string.text_hint_input_phone_number)
                    mRecipientInputEditText.inputType = InputType.TYPE_CLASS_PHONE
                }

                2 -> {
                    mRecipientInputEditText.hint = mActivity.resources.getString(R.string.text_input_company_name)
                    mRecipientInputEditText.inputType = InputType.TYPE_CLASS_TEXT
                }

                3 -> {
                    mRecipientInputEditText.hint = mActivity.resources.getString(R.string.text_input_company_phone_number)
                    mRecipientInputEditText.inputType = InputType.TYPE_CLASS_PHONE
                }
            }

            mRecipientInputEditText.setText("")
            mMemberSearchSpinner.visibility = View.GONE
            mMemberSearchSpinner.dismiss()
            mMemberSearchSpinner.setItems(ArrayList<String>())
        })

        // Recipient Input Edit Text
        mRecipientInputEditText = mContainer.fragment_note_reply_recipient_input_edit_text
        mRecipientInputEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mRecipientInputEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                mMemberSearchSpinner.dismiss()
            }
        }

        // Member Search Text View
        val memberSearchTextView = mContainer.fragment_note_reply_member_search_text_view
        memberSearchTextView.setOnClickListener {

            hideKeyboard(mActivity)

            if (mSelectRecipientValue == 0) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_search_type))
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mRecipientInputEditText.text.toString())) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_search))
                return@setOnClickListener
            } else if (mRecipientInputEditText.length() < 3) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_search_input_3_words))
                return@setOnClickListener
            }

            getNoteMemberSearch()
        }

        // Member Search Spinner
        mMemberSearchSpinner = mContainer.fragment_note_reply_member_search_spinner
        mMemberSearchSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mNoteMemberSearchData = mNoteMemberSearchListData[newIndex]
            mRecipientInputEditText.isFocusable = false
        })

        // Content Edit Text
        mContentEditText = mContainer.fragment_note_reply_write_content_edit_text
        mContentEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // File Upload Image View
        val fileUploadImageView = mContainer.fragment_note_reply_file_upload_image_view
        fileUploadImageView.setOnClickListener {
            if (mFileUriListData.size == 3) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_upload_file_count))
            } else {
                fileUpload()
            }
        }

        mFileUploadRecyclerView = mContainer.fragment_note_reply_file_upload_recycler_view
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

        // Image Upload Recycler View
        mImageUploadRecyclerView = mContainer.fragment_note_reply_image_upload_recycler_view
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

        // Send Text View
        val sendTextView = mContainer.fragment_note_reply_send_text_view
        sendTextView.setOnClickListener {
            checkNote()
        }

        // Loading View
        mLoadingView = mContainer.fragment_note_reply_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_note_reply_loading_lottie_view




        if (getIsReply()) {
            if (getNoteData().getCompanyUid() > 0 && getNoteData().getSendMemberUid() == 0) {
                getCompanyNoteInformation()
            } else if (getNoteData().getCompanyUid() == 0 && getNoteData().getSendMemberUid() > 0) {
                getMemberInformation()
            }
        } else {

        }

        setDefaultSelect()
    }

    private fun setDefaultSelect() {
        val imageUploadData = NativeImageUploadData()
        imageUploadData.setViewType(0x00)

        mNativeImageUploadListData.add(imageUploadData)

        mNativeImageUploadAdapter.setData(mNativeImageUploadListData)

    }

    private fun checkNote() {
        if (TextUtils.isEmpty(mContentEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_contents))
            return
        }

        getNoteSendSave()
    }

    private fun displaySenderData(data: MemberInformationData) {

        if (!TextUtils.isEmpty(data.getName())) {
            mReplyReceiveMemberTextView.text = data.getName()
        }

        if (!TextUtils.isEmpty(data.getPhoneNumber())) {
            mReplyReceivePhoneNumberTextView.text = data.getPhoneNumber()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun enableScroll(view: View) {
        if (view is TextView) {
            view.movementMethod = ScrollingMovementMethod()
        }
        view.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
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

    override fun onPause() {
        super.onPause()

        if (mMemberSearchSpinner.isShowing) {
            mMemberSearchSpinner.dismiss()
        }

        if (mRecipientSpinner.isShowing) {
            mRecipientSpinner.dismiss()
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
        if (context is OnNoteReplyFragmentListener) {
            mActivity = context as Activity

            if (context is OnNoteReplyFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNoteReplyFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNoteReplyFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNoteReplyFragmentListener"
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
     * API. Get Member Information
     */
    private fun getMemberInformation() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getMemberInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<MemberInformationData> {
                override fun onSuccess(resultData: MemberInformationData) {

                    displaySenderData(resultData)
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
            getNoteData().getSendMemberUid()
        )
    }

    /**
     * API. Get Company Note Information
     */
    private fun getCompanyNoteInformation() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyNoteInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<MemberInformationData> {
                override fun onSuccess(resultData: MemberInformationData) {

                    displaySenderData(resultData)
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
            getNoteData().getCompanyUid()
        )
    }

    /**
     * API. Get Note Save
     */
    private fun getNoteSendSave() {

        var receiveCompanyUid = 0
        var receiveMemberUid = 0

        if (getIsReply()) {
            if (getNoteData().getCompanyUid() > 0 && getNoteData().getSendMemberUid() == 0) {
                receiveCompanyUid = getNoteData().getCompanyUid()
            } else if (getNoteData().getCompanyUid() == 0 && getNoteData().getSendMemberUid() > 0) {
                receiveMemberUid = getNoteData().getSendMemberUid()
            }
        } else {
            if (mSelectRecipientValue == 1 || mSelectRecipientValue == 2) {
                receiveMemberUid = mNoteMemberSearchData.getUid()
            } else if (mSelectRecipientValue == 3 || mSelectRecipientValue == 4) {
                receiveCompanyUid = mNoteMemberSearchData.getUid()
            }
        }

        var companySendUid = 0
        if (mNoteActivity.mIsCompanyNote) {
            companySendUid = mNoteActivity.mCompanyDefaultData!!.getCompanyUid()
        }

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteSendSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    showToast(mActivity, mActivity.resources.getString(R.string.text_success_send_note))
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
            mActivity,
            MAPP.USER_DATA.getSecureKey(),
            mContentEditText.text.toString(),
            receiveCompanyUid,
            receiveMemberUid,
            companySendUid,
            mNoteActivity.mIsCompanyNote,
            mImageUploadListData,
            mFileUriListData
        )
    }

    /**
     * API. Get Note Member Search
     */
    private fun getNoteMemberSearch() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteMemberSearch(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<NoteMemberSearchData>> {
                override fun onSuccess(resultData: ArrayList<NoteMemberSearchData>) {
                    if (resultData.isEmpty()) {
                        mNoteMemberSearchListData.clear()
                        showToast(mActivity, mActivity.resources.getString(R.string.text_no_search_data))
                        return
                    }

                    mNoteMemberSearchListData = resultData

                    val memberSearchList = ArrayList<String>()
                    for (i in mNoteMemberSearchListData.indices) {
                        memberSearchList.add(mNoteMemberSearchListData[i].getName())
                    }

                    mMemberSearchSpinner.setItems(memberSearchList)
                    mMemberSearchSpinner.visibility = if (memberSearchList.size > 0) View.VISIBLE else View.GONE
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
            mSelectRecipientValue,
            mRecipientInputEditText.text.toString()
        )
    }
}