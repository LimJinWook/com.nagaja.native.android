package com.nagaja.app.android.Dialog

import android.annotation.SuppressLint
import android.app.Dialog
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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nagaja.app.android.Data.ImageUploadData
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_CNY
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_JPY
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_KRW
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_PHP
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_USD
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_UID_USD
import com.nagaja.app.android.Base.NagajaFragment.Companion.DIALOG_TYPE_NORMAL
import com.nagaja.app.android.CompanyInformation.CompanyDefaultFragment.Companion.CURRENCY_UID_PESO
import com.nagaja.app.android.CompanyInformation.CompanyDefaultFragment.Companion.CURRENCY_UID_WON
import com.nagaja.app.android.CompanyInformation.CompanyDefaultFragment.Companion.CURRENCY_UID_YEN
import com.nagaja.app.android.CompanyInformation.CompanyDefaultFragment.Companion.CURRENCY_UID_YUAN
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.custom_dialog_product_bottom.view.*
import kotlinx.android.synthetic.main.fragment_used_market_register.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class CustomDialogProductBottom(context: Context, data: CompanyProductData?, companyUid: Int, categoryUid: Int, isModify: Boolean) : BottomSheetDialogFragment() {

    private lateinit var mContext: Context

    private lateinit var mContainer: View
    private lateinit var mPriceDollarView: View
    private lateinit var mPriceWonView: View
    private lateinit var mPricePesoView: View
    private lateinit var mPriceYuanView: View
    private lateinit var mPriceYenView: View
    private lateinit var mLoadingView: View

    private lateinit var mCategorySpinner: PowerSpinnerView

    private lateinit var mProductNameEditText: EditText
    private lateinit var mPriceDollarEditText: EditText
    private lateinit var mPriceWonEditText: EditText
    private lateinit var mPricePesoEditText: EditText
    private lateinit var mPriceYuanEditText: EditText
    private lateinit var mPriceYenEditText: EditText
    private lateinit var mContentEditText: EditText

    private lateinit var mTitleTextView: TextView
    private lateinit var mImageNameTextView: TextView

    private lateinit var mProductNameDeleteImageView: ImageView
    private lateinit var mCancelImageView: ImageView
    private lateinit var mSelectImageView: ImageView

    private lateinit var mLoadingLottieView: LottieAnimationView

    private var mListener: onCustomDialogProductBottomListener? = null

    private lateinit var mRequestQueue: RequestQueue

    private var mCompanyProductData = CompanyProductData()
    private var mCurrencyListData = ArrayList<CurrencyData>()
    private var mImageUploadListData = ArrayList<ImageUploadData>()
    private var mProductCategoryListData = ArrayList<ProductCategoryData>()

    private var mCompanyUid = 0
    private var mCategoryUid = 0
    private var mItemCategoryUid = 0

    private var mIsModify = false
    private var mIsChangeImage = false

    private var mCaptureImageFileName: String = ""
    private var mCurrentPhotoPath: String = ""
    private lateinit var mPhotoUri: Uri

    fun setonCustomDialogProductBottomListener(listener: onCustomDialogProductBottomListener?) {
        mListener = listener
    }

    init {
        this.mContext = context
        if (data != null) {
            this.mCompanyProductData = data
        }
        this.mIsModify = isModify
        this.mCompanyUid = companyUid
        this.mCategoryUid = categoryUid
    }

    companion object {

    }

    interface onCustomDialogProductBottomListener {
        fun onSuccessRegister()
    }

    fun newInstance(context: Context, data: CompanyProductData, companyUid: Int, categoryUid: Int, isModify: Boolean): CustomDialogProductBottom {
        val fragment = CustomDialogProductBottom(context, data, companyUid, categoryUid, isModify)
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }
        return bottomSheetDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        mRequestQueue = Volley.newRequestQueue(mContext)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainer = inflater.inflate(R.layout.custom_dialog_product_bottom, container, false)

        init()

        return mContainer
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {

        NagajaLog().e("wooks, mCompanyProductData.getItemName() = ${mCompanyProductData.getItemName()}")

        // Title Text View
        mTitleTextView = mContainer.custom_dialog_product_bottom_title_text_view

        // Cancel Image View
        mCancelImageView = mContainer.custom_dialog_product_bottom_cancel_image_view
        mCancelImageView.setOnClickListener {
            dismiss()
        }

        // Category Spinner View
        mCategorySpinner = mContainer.custom_dialog_product_bottom_category_spinner
        mCategorySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mCategoryUid = mProductCategoryListData[newIndex].getBaseCategoryUid()
            mItemCategoryUid = mProductCategoryListData[newIndex].getCategoryUid()
        })

        // Price Dollar View
        mPriceDollarView = mContainer.custom_dialog_product_bottom_price_dollar_view

        // Price Dollar Edit Text
        mPriceDollarEditText = mContainer.custom_dialog_product_bottom_price_dollar_edit_text
        mPriceDollarEditText.filters = arrayOf(Util().blankSpaceFilter)
        mPriceDollarEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
//                if (!TextUtils.isEmpty(mPriceDollarEditText.text)) {
//                    val priceDollar = Util().unitConvert(mPriceDollarEditText.text.toString())
//                    mPriceDollarEditText.setText(priceDollar)
//                }
            }
        })

        // Price Won View
        mPriceWonView = mContainer.custom_dialog_product_bottom_price_won_view

        // Price Won Edit Text
        mPriceWonEditText = mContainer.custom_dialog_product_bottom_price_won_edit_text
        mPriceWonEditText.filters = arrayOf(Util().blankSpaceFilter)
        mPriceWonEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
//                if (!TextUtils.isEmpty(mPriceWonEditText.text)) {
//                    mPriceWonEditText.setText(Util().unitConvert(mPriceWonEditText.text.toString()))
//                }
            }
        })

        // Price Peso View
        mPricePesoView = mContainer.custom_dialog_product_bottom_price_peso_view

        // Price Peso Edit Text
        mPricePesoEditText = mContainer.custom_dialog_product_bottom_price_peso_edit_text
        mPricePesoEditText.filters = arrayOf(Util().blankSpaceFilter)
        mPricePesoEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
//                if (!TextUtils.isEmpty(mPricePesoEditText.text)) {
//                    mPricePesoEditText.setText(Util().unitConvert(mPricePesoEditText.text.toString()))
//                }
            }
        })

        // Price Yuan View
        mPriceYuanView = mContainer.custom_dialog_product_bottom_price_yuan_view

        // Price Yuan Edit Text
        mPriceYuanEditText = mContainer.custom_dialog_product_bottom_price_yuan_edit_text
        mPriceYuanEditText.filters = arrayOf(Util().blankSpaceFilter)
        mPriceYuanEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
//                if (!TextUtils.isEmpty(mPricePesoEditText.text)) {
//                    mPricePesoEditText.setText(Util().unitConvert(mPricePesoEditText.text.toString()))
//                }
            }
        })

        // Price Yen View
        mPriceYenView = mContainer.custom_dialog_product_bottom_price_yen_view

        // Price Yuan Edit Text
        mPriceYenEditText = mContainer.custom_dialog_product_bottom_price_yen_edit_text
        mPriceYenEditText.filters = arrayOf(Util().blankSpaceFilter)
        mPriceYenEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
//                if (!TextUtils.isEmpty(mPricePesoEditText.text)) {
//                    mPricePesoEditText.setText(Util().unitConvert(mPricePesoEditText.text.toString()))
//                }
            }
        })


        // Product Name Edit Text
        mProductNameEditText = mContainer.custom_dialog_product_bottom_product_name_edit_text
        mProductNameEditText.filters = arrayOf(InputFilter.LengthFilter(30))
        mProductNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                mProductNameDeleteImageView.visibility = if (!TextUtils.isEmpty(mProductNameEditText.text)) View.VISIBLE else View.GONE
            }
        })

        // Product Name Delete Image View
        mProductNameDeleteImageView = mContainer.custom_dialog_product_bottom_product_name_delete_image_view
        mProductNameDeleteImageView.setOnClickListener {
            mProductNameEditText.setText("")
        }







        // Content Edit Text
        mContentEditText = mContainer.custom_dialog_product_bottom_content_edit_text
//        mContentEditText.filters = arrayOf(Util().blankSpaceFilter)
        mContentEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mContentEditText.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP ->
                    v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        // Image Name Text View
        mImageNameTextView = mContainer.custom_dialog_product_image_name_text_view

        // Select Image View
        mSelectImageView = mContainer.custom_dialog_product_select_image_view
        mSelectImageView.setOnClickListener {
            showCustomImageUploadDialog()
        }


        val registerTextView = mContainer.custom_dialog_product_bottom_confirm_text_view
        registerTextView.setOnClickListener {
            checkProductRegister()
        }


        // Loading View
        mLoadingView = mContainer.custom_dialog_product_bottom_register_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.custom_dialog_product_bottom_register_loading_lottie_view


        getCompanyProductCategoryData()

    }

    private fun displayCurrencyListData(currencyListData: ArrayList<CurrencyData>) {

        for (i in currencyListData.indices) {
            if (currencyListData[i].getCurrencyUid() == CURRENCY_UID_USD) {
                mPriceDollarView.visibility = View.VISIBLE
            } else if (currencyListData[i].getCurrencyUid() == CURRENCY_UID_WON) {
                mPriceWonView.visibility = View.VISIBLE
            } else if (currencyListData[i].getCurrencyUid() == CURRENCY_UID_PESO) {
                mPricePesoView.visibility = View.VISIBLE
            } else if (currencyListData[i].getCurrencyUid() == CURRENCY_UID_YUAN) {
                mPriceYuanView.visibility = View.VISIBLE
            } else if (currencyListData[i].getCurrencyUid() == CURRENCY_UID_YEN) {
                mPriceYenView.visibility = View.VISIBLE
            }
        }

        if (mIsModify) {
            setProductItem()
        }
    }

    private fun setProductItem() {
        if (!TextUtils.isEmpty(mCompanyProductData.getItemName())) {
            mProductNameEditText.setText(mCompanyProductData.getItemName())
        }



        for (i in mCompanyProductData.getCurrencyListData().indices) {
            var price = if (mCompanyProductData.getCurrencyListData()[i].getCurrencyPrice() > 0) mCompanyProductData.getCurrencyListData()[i].getCurrencyPrice().toString() else ""
            if (mCompanyProductData.getCurrencyListData()[i].getCurrencyCode() == CURRENCY_TYPE_USD) {
                mPriceDollarEditText.setText(price)
            } else if (mCompanyProductData.getCurrencyListData()[i].getCurrencyCode() == CURRENCY_TYPE_KRW) {
                mPriceWonEditText.setText(price)
            } else if (mCompanyProductData.getCurrencyListData()[i].getCurrencyCode() == CURRENCY_TYPE_PHP) {
                mPricePesoEditText.setText(price)
            } else if (mCompanyProductData.getCurrencyListData()[i].getCurrencyCode() == CURRENCY_TYPE_CNY) {
                mPriceYuanEditText.setText(price)
            } else if (mCompanyProductData.getCurrencyListData()[i].getCurrencyCode() == CURRENCY_TYPE_JPY) {
                mPriceYenEditText.setText(price)
            }
        }

        if (!TextUtils.isEmpty(mCompanyProductData.getItemContent())) {
            mContentEditText.setText(mCompanyProductData.getItemContent())
        }

        if (mCompanyProductData.getImageListData().size > 0) {
            if (!TextUtils.isEmpty(mCompanyProductData.getImageListData()[0].getItemImageOrigin())) {
                mImageNameTextView.text = mCompanyProductData.getImageListData()[0].getItemImageOrigin()
            }
        }
    }

    private fun checkProductRegister() {
        if (mItemCategoryUid <= 0) {
            Toast.makeText(mContext, mContext.resources.getString(R.string.text_error_select_category_type), Toast.LENGTH_SHORT).show()
            return
        } else if (TextUtils.isEmpty(mProductNameEditText.text)) {
            Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_input_product_name), Toast.LENGTH_SHORT).show()
            return
        } else if (TextUtils.isEmpty(mContentEditText.text)) {
            Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_input_product_content), Toast.LENGTH_SHORT).show()
            return
        } else {
            if (TextUtils.isEmpty(mPriceDollarEditText.text) && TextUtils.isEmpty(mPriceWonEditText.text) && TextUtils.isEmpty(mPricePesoEditText.text) &&
                TextUtils.isEmpty(mPriceYuanEditText.text) && TextUtils.isEmpty(mPriceYenEditText.text)) {

                Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_input_product_currency), Toast.LENGTH_SHORT).show()
                return
            }
        }

        showRegisterConfirmPopup()
    }

    private fun disConnectUserToken(context: Context) {
        val customDialog = CustomDialog(
            context,
            NagajaFragment.DIALOG_TYPE_NO_CANCEL,
            context.resources.getString(R.string.text_expired_tokens),
            context.resources.getString(R.string.text_message_expired_tokens),
            "",
            context.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()

                activity!!.finishAffinity()
                exitProcess(0)
            }
        })
        customDialog.show()
    }

    private fun showRegisterConfirmPopup() {
        val customDialog = CustomDialog(
            mContext,
            DIALOG_TYPE_NORMAL,
            mContext.resources.getString(R.string.text_register_product),
            mProductNameEditText.text.toString() + "\n" + mContext.resources.getString(R.string.text_message_register_product),
            mContext.resources.getString(R.string.text_cancel),
            mContext.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()

                getCompanyProductRegister()
            }
        })
        customDialog.show()
    }

    private fun showCustomImageUploadDialog() {
        val customDialogBottom = CustomDialogBottom(mContext, isImage = true, isProfileModify = false)
        customDialogBottom.setOnCustomDialogBottomListener(object : CustomDialogBottom.OnCustomDialogBottomListener {
            override fun onCamera() {
                showCameraImage()
                customDialogBottom.dismiss()
            }

            override fun onGallery() {
                showGalleryImageSelect()
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

    private fun showGalleryImageSelect() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE)
    }

    private fun showCameraImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(mContext.packageManager) != null) {

            var photoFile: File? = null

            try {
                val tempDir: File = mContext.cacheDir

                val timeStamp: String = SimpleDateFormat("yyyyMMdd").format(Date())
                mCaptureImageFileName = "Capture_" + timeStamp + "_"
                val tempImage: File = File.createTempFile(mCaptureImageFileName, ".png", tempDir)

                mCurrentPhotoPath = tempImage.absolutePath
                photoFile = tempImage
            } catch (e: IOException) {
                NagajaLog().e("wooks, Screen Shot Image Create Error! : $e")
            }

            if (photoFile != null) {
//                mPhotoUri = FileProvider.getUriForFile(mContext, "${mContext.packageManager}.fileprovider", photoFile)
                mPhotoUri = FileProvider.getUriForFile(mContext, "com.nagaja.app.android.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }
            }
        }
    }

    @SuppressLint("Range")
    fun selectImage(uri: Uri) {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = mContext.contentResolver.query(uri, null, null, null, null)
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


        val file = File(Util().getPathFromUri(mContext, uri)!!)
        val fileSize: Long = (file.length() / 1024)
        NagajaLog().d("wooks, Select File Size = $fileSize")

        if (fileSize > 30000) {
            Toast.makeText(mContext, mContext.resources.getString(R.string.text_error_select_30mb_less), Toast.LENGTH_SHORT).show()
            return
        }

        val imageUploadData = ImageUploadData()
        try {
            var bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(mContext.contentResolver, uri)
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

        mImageNameTextView.text = Uri.decode(result)

        mImageUploadListData.clear()
        mImageUploadListData.add(imageUploadData)

        mIsChangeImage = true

    }

    @SuppressLint("SimpleDateFormat")
    fun getCameraImageData(uri: Uri, currentPhotoPath: String) {
        mCurrentPhotoPath = currentPhotoPath
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.contentResolver, Uri.fromFile(File(mCurrentPhotoPath))))
        } else {
            MediaStore.Images.Media.getBitmap(mContext.contentResolver, Uri.fromFile(File(mCurrentPhotoPath)))
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

            mImageNameTextView.text = getFileName(uri)!!

            mImageUploadListData.clear()
            mImageUploadListData.add(imageUploadData)

            mIsChangeImage = true
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = mContext.contentResolver.query(uri, null, null, null, null)
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

    override fun onPause() {
        super.onPause()

        if (mCategorySpinner.isShowing) {
            mCategorySpinner.dismiss()
        }
    }

    // =================================================================================
    // API
    // =================================================================================

    /**
     * API. Get Store Category Data
     */
    private fun getCompanyProductCategoryData() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyProductCategoryData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<ProductCategoryData>> {
                override fun onSuccess(resultData: ArrayList<ProductCategoryData>) {

                    if (resultData.isEmpty()) {
                        return
                    }

                    mProductCategoryListData = resultData
                    val categoryListData = ArrayList<String>()
                    for (i in resultData.indices) {
                        categoryListData.add(resultData[i].getCategoryName())
                    }

                    mCategorySpinner.setItems(categoryListData)

                    if (mIsModify) {
                        for (i in resultData.indices) {
                            if (mCompanyProductData.getCategoryUid()== resultData[i].getBaseCategoryUid()) {
                                mCategorySpinner.selectItemByIndex(i)
                                break
                            }
                        }
                    }

                    getCompanyProductCurrencyList()
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
            MAPP.USER_DATA.getSecureKey(),
            mCompanyUid,
            mCategoryUid.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Company Product Currency List
     */
    private fun getCompanyProductCurrencyList() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyProductCurrencyList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CurrencyData>> {
                override fun onSuccess(resultData: ArrayList<CurrencyData>) {
                    if (resultData.isEmpty()) {
                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_select_product_currency), Toast.LENGTH_SHORT).show()
                        return
                    }

                    mCurrencyListData = resultData
                    displayCurrencyListData(resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mContext)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mCompanyUid
        )
    }

    /**
     * API. Get Company Product Register
     */
    private fun getCompanyProductRegister() {

        setLoadingView(true)

        var deleteImageUid = ""
        if (mIsModify && mIsChangeImage) {
            if (mCompanyProductData.getImageListData().size > 0) {
                deleteImageUid = mCompanyProductData.getImageListData()[0].getItemImageUid().toString()
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyProductRegister(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    setLoadingView(false)

                    mListener!!.onSuccessRegister()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mContext)
                    }
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mProductNameEditText.text.toString(),
            mProductNameEditText.text.toString(),
            mContentEditText.text.toString(),
            mCategoryUid,
            mItemCategoryUid,
            mCompanyUid,
            MAPP.USER_DATA.getMemberUid(),
            if (!TextUtils.isEmpty(mPriceWonEditText.text)) "KRW" else "",
            if (!TextUtils.isEmpty(mPriceWonEditText.text)) mPriceWonEditText.text.toString().replace(",", "") else "",
            if (!TextUtils.isEmpty(mPriceDollarEditText.text)) "USD" else "",
            if (!TextUtils.isEmpty(mPriceDollarEditText.text)) mPriceDollarEditText.text.toString().replace(",", "") else "",
            if (!TextUtils.isEmpty(mPricePesoEditText.text)) "PHP" else "",
            if (!TextUtils.isEmpty(mPricePesoEditText.text)) mPricePesoEditText.text.toString().replace(",", "") else "",
            if (!TextUtils.isEmpty(mPriceYuanEditText.text)) "CNY" else "",
            if (!TextUtils.isEmpty(mPriceYuanEditText.text)) mPriceYuanEditText.text.toString().replace(",", "") else "",
            if (!TextUtils.isEmpty(mPriceYenEditText.text)) "JPY" else "",
            if (!TextUtils.isEmpty(mPriceYenEditText.text)) mPriceYenEditText.text.toString().replace(",", "") else "",
            mIsModify,
            deleteImageUid,
            mCompanyProductData.getItemUid(),
            mImageUploadListData
        )
    }
}