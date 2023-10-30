package com.nagaja.app.android.Report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : NagajaActivity(), ReportFragment.OnReportFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@ReportActivity)!!, false)

        val reportType = intent.getIntExtra(INTENT_DATA_REPORT_TYPE_DATA, 0)
        val reportUid = intent.getIntExtra(INTENT_DATA_REPORT_UID_DATA, 0)
        val reportName = intent.getStringExtra(INTENT_DATA_REPORT_STORE_NAME_DATA)
        val companyUid = intent.getIntExtra(INTENT_DATA_COMPANY_UID_DATA, 0)

        init(reportType, reportUid, reportName!!, companyUid)
    }

    private fun init(reportType: Int, reportUid: Int, reportName: String, companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ReportFragment::class.java.name
        val reportFragment = ReportFragment.newInstance(reportType, reportUid, reportName, companyUid)
        fragmentTransaction.add(R.id.activity_report_container, reportFragment, tag).commit()
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@ReportActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@ReportActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@ReportActivity, NotificationActivity::class.java)
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
                mPhotoUri = FileProvider.getUriForFile(this@ReportActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onSuccess() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val reportFragment: ReportFragment? = supportFragmentManager.findFragmentById(R.id.activity_report_container) as ReportFragment?
                    reportFragment!!.selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val reportFragment: ReportFragment? = supportFragmentManager.findFragmentById(R.id.activity_report_container) as ReportFragment?
                    reportFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }

            }
        }
    }
}