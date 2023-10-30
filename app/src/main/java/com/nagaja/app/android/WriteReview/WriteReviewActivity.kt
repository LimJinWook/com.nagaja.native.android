package com.nagaja.app.android.WriteReview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.StoreDetailReviewData
import com.nagaja.app.android.Data.WriteReviewData
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WriteReviewActivity : NagajaActivity(), WriteReviewFragment.OnWriteReviewFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@WriteReviewActivity)!!, false)

        val isModify = intent.getBooleanExtra(INTENT_DATA_WRITE_REVIEW_IS_MODIFY_DATA, false)
        if (isModify) {
            val reviewDataModify = intent.getSerializableExtra(INTENT_DATA_WRITE_REVIEW_DATA) as StoreDetailReviewData
            init(reviewDataModify, isModify)
        } else {
            val writeReviewData = intent.getSerializableExtra(INTENT_DATA_WRITE_REVIEW_DATA) as WriteReviewData
            init(writeReviewData, isModify)
        }
    }

    private fun init(data: WriteReviewData, isModify: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = WriteReviewFragment::class.java.name
        val writeReviewFragment = WriteReviewFragment.newInstance(data, isModify)
        fragmentTransaction.add(R.id.activity_write_review_container, writeReviewFragment, tag).commit()
    }

    private fun init(data: StoreDetailReviewData, isModify: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = WriteReviewFragment::class.java.name
        val writeReviewFragment = WriteReviewFragment.newInstance(data, isModify)
        fragmentTransaction.add(R.id.activity_write_review_container, writeReviewFragment, tag).commit()
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@WriteReviewActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@WriteReviewActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@WriteReviewActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onFinish() {
        finish()
    }

    override fun onGalleryImageSelect() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE)
    }

    override fun onCameraImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null

            try {
                val tempDir: File = cacheDir

                val timeStamp: String = SimpleDateFormat("yyyyMMdd").format(Date())
                mCaptureImageFileName = "Capture_" + timeStamp + "_"
                val tempImage: File = File.createTempFile(mCaptureImageFileName, ".png", tempDir)

                mCurrentPhotoPath = tempImage.absolutePath
                photoFile = tempImage
            } catch (e: IOException) {
                NagajaLog().e("wooks, Screen Shot Image Create Error! : $e")
            }

            if (photoFile != null) {
                mPhotoUri = FileProvider.getUriForFile(this@WriteReviewActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onSuccessUpload() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val writeReviewFragment: WriteReviewFragment? = supportFragmentManager.findFragmentById(R.id.activity_write_review_container) as WriteReviewFragment?
                    writeReviewFragment!!.selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val writeReviewFragment: WriteReviewFragment? = supportFragmentManager.findFragmentById(R.id.activity_write_review_container) as WriteReviewFragment?
                    writeReviewFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }

            }
        }
    }

}