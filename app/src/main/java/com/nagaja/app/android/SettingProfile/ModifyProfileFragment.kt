package com.nagaja.app.android.SettingProfile

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
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Data.ModifyProfileData
import com.nagaja.app.android.Utils.NetworkProvider
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogBottom
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_modify_profile.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store_write.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ModifyProfileFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View

    private lateinit var mProfileImageView: SimpleDraweeView

    private lateinit var mNickNameEditText: EditText

    private lateinit var mDeleteImageView: ImageView

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mListener: OnModifyProfileFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private var mImageUpdateData = ImageUploadData()

    private var mCurrentPhotoPath = ""

    interface OnModifyProfileFragmentListener {
        fun onBack()
        fun onGalleryImageSelect()
        fun onCameraImage()
        fun onSuccessModifyProfile()
    }

    companion object {
        fun newInstance(): ModifyProfileFragment {
            val args = Bundle()
            val fragment = ModifyProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_modify_profile, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_modify_profile)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Profile Image View
        mProfileImageView = mContainer.fragment_modify_profile_image_view
        mProfileImageView.setImageURI(Uri.parse(NetworkProvider.DEFAULT_IMAGE_DOMAIN + MAPP.USER_DATA.getProfileName()))

        // Profile Change Image View
        val profileChangeImageView = mContainer.fragment_modify_profile_change_image_view
        profileChangeImageView.setOnClickListener {
            showCustomImageUploadDialog()
        }

        // Nick Name Edit Text
        mNickNameEditText = mContainer.fragment_modify_profile_nick_name_edit_text
        mNickNameEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(20))
        mNickNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mNickNameEditText.setText( if (!TextUtils.isEmpty(MAPP.USER_DATA.getNickName())) MAPP.USER_DATA.getNickName() else "" )

        // Delete Image View
        mDeleteImageView = mContainer.fragment_modify_profile_delete_image_view



        // Loading View
        mLoadingView = mContainer.fragment_modify_profile_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_modify_profile_loading_lottie_view

        // Confirm Text View
        val confirmTextView = mContainer.fragment_modify_profile_confirm_text_view
        confirmTextView.setOnClickListener {
            checkProfileData()
        }

    }

    private fun checkProfileData() {
        if (TextUtils.isEmpty(mNickNameEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_nick_name))
            return
        }

        getModifyProfileUpdate()
    }

    private fun showCustomImageUploadDialog() {
        val customDialogBottom = CustomDialogBottom(mActivity, isImage = true, isProfileModify = true)
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
                showCustomDeleteImagePopup()
                customDialogBottom.dismiss()
            }

            override fun onCancel() {
                customDialogBottom.dismiss()
            }
        })
        customDialogBottom.show(parentFragmentManager, "Custom Dialog")
    }

    private fun showCustomDeleteImagePopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_delete_profile_image),
            mActivity.resources.getString(R.string.text_message_delete_profile_image),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()

                getDeleteProfileImage()
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




        try {
            var bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(mActivity.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(mActivity.contentResolver, uri)
            }

            mImageUpdateData.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        NagajaLog().d("wooks, Select File Name = ${Uri.decode(result)}")
        var fileName = Uri.decode(result)
        if (fileName.contains(".heic")) {
            fileName = fileName.replace(".heic", ".png")
        }
        mImageUpdateData.setFileName(fileName)

        val path = Util().getPathFromUri(mActivity, uri)
        if (!TextUtils.isEmpty(path)) {
            mProfileImageView.setImageURI(uri)
        }

        val file = File(Util().getPathFromUri(mActivity, uri)!!)
        val fileSize: Long = (file.length() / 1024)
        NagajaLog().d("wooks, Select File Size = $fileSize")

        if (fileSize > 30000) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_30mb_less))
            return
        }

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

            mImageUpdateData.setImageBitmap(bitmap)
            mImageUpdateData.setFileName(getFileName(uri)!!)

            mProfileImageView.setImageURI(uri)
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
        if (context is OnModifyProfileFragmentListener) {
            mActivity = context as Activity

            if (context is OnModifyProfileFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnModifyProfileFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnModifyProfileFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnModifyProfileFragmentListener"
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
     * API. Get Modify Profile Update
     */
    private fun getModifyProfileUpdate() {

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getModifyProfileUpdate(
            mRequestQueue,
            object : NagajaManager.RequestListener<ModifyProfileData> {
                override fun onSuccess(resultData: ModifyProfileData) {

                    setLoadingView(false)

                    MAPP.USER_DATA.setNickName(resultData.getMemberNickName())
                    MAPP.USER_DATA.setProfileName(resultData.getProfileImageName())
                    MAPP.USER_DATA.setProfileUrl(resultData.getProfileImageOrigin())

                    mListener.onSuccessModifyProfile()

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
            mNickNameEditText.text.toString(),
            mImageUpdateData
        )
    }

    /**
     * API. Get Delete Profile Image
     */
    private fun getDeleteProfileImage() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getDeleteProfileImage(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    MAPP.USER_DATA.setProfileName("")
                    mProfileImageView.setImageURI("")
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
            MAPP.USER_DATA.getSecureKey()
        )
    }
}