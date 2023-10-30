package com.nagaja.app.android.CompanyJob

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Address.AddressActivity
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.ChatView.ChatViewActivity
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Data.CompanyDefaultData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Job.JobDetailFragment
import com.nagaja.app.android.Job.JobRegisterFragment
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CompanyJobActivity : NagajaActivity(), CompanyJobFragment.OnCompanyJobFragmentListener,
    JobDetailFragment.OnJobDetailFragmentListener,
    JobRegisterFragment.OnJobRegisterFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri
    var mIsFile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_job)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@CompanyJobActivity)!!, false)

        val companyDefaultData = intent.getSerializableExtra(INTENT_DATA_COMPANY_DEFAULT_DATA) as CompanyDefaultData
        val categoryUid = intent.getIntExtra(INTENT_DATA_COMPANY_JOB_UID_DATA, 0)

        init(companyDefaultData, categoryUid)
    }

    private fun init(data: CompanyDefaultData, categoryUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = CompanyJobFragment::class.java.name
        val companyJobFragment: CompanyJobFragment = CompanyJobFragment.newInstance(data, categoryUid)
        fragmentTransaction.add(R.id.activity_company_job_container, companyJobFragment, tag).commit()
    }

    private fun showJobDetailScreen(boardUid: Int, companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = JobDetailFragment::class.java.name
        val jobDetailFragment = JobDetailFragment.newInstance(boardUid, companyUid)
        fragmentTransaction.replace(R.id.activity_company_job_container, jobDetailFragment, tag).addToBackStack(tag).commit()
    }

    private fun showJobRegisterScreen(jobType: Int, companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = JobRegisterFragment::class.java.name
        val jobRegisterFragment = JobRegisterFragment.newInstance(jobType, companyUid, 0)
        fragmentTransaction.replace(R.id.activity_company_job_container, jobRegisterFragment, tag).addToBackStack(tag).commit()
    }

    private fun showJobModifyScreen(jobType: Int, boardUid: Int, companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = JobRegisterFragment::class.java.name
        val jobRegisterFragment = JobRegisterFragment.newInstance(jobType, companyUid, boardUid)
        fragmentTransaction.replace(R.id.activity_company_job_container, jobRegisterFragment, tag).addToBackStack(tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showMapScreen() {
        val intent = Intent(this@CompanyJobActivity, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, false)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showChatViewScreen(chatClass: String, chatData: String) {
        val intent = Intent(this@CompanyJobActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, chatClass)
        intent.putExtra(INTENT_DATA_CHAT_DATA, chatData)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@CompanyJobActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@CompanyJobActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@CompanyJobActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFinish() {
//        onBackPressed()
        finish()
    }

    override fun onJobDetailScreen(boardUid: Int, companyUid: Int) {
        showJobDetailScreen(boardUid, companyUid)
    }

    override fun onJobRegister(jobType: Int, companyUid: Int) {
        showJobRegisterScreen(jobType, companyUid)
    }

    override fun onChatViewScreen(chatClass: String, chatData: String) {
        showChatViewScreen(chatClass, chatData)
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onJobModifyScreen(jobType: Int, boardUid: Int, companyUid: Int) {
        showJobModifyScreen(jobType, boardUid, companyUid)
    }

    override fun onMapScreen() {
        showMapScreen()
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onGalleryImageSelect(isFile: Boolean) {
        mIsFile = isFile
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE)
    }

    override fun onCompanyRegisterSuccess() {
        onBackPressed()
        val companyJobFragment: CompanyJobFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_job_container) as CompanyJobFragment?
        companyJobFragment!!.getCompanyJobList(true)
    }

    override fun onCameraImage(isFile: Boolean) {
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

            mIsFile = isFile

            if (photoFile != null) {
                mPhotoUri = FileProvider.getUriForFile(this@CompanyJobActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_ADDRESS_REQUEST_CODE -> {
                    val addressData = data?.getSerializableExtra(INTENT_DATA_ADDRESS_DATA) as AddressData
                    val jobRegisterFragment: JobRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_job_container) as JobRegisterFragment?
                    jobRegisterFragment!!.selectMap(addressData)
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val jobRegisterFragment: JobRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_job_container) as JobRegisterFragment?
                    jobRegisterFragment!!.selectImage(data!!.data!!, mIsFile)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val jobRegisterFragment: JobRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_job_container) as JobRegisterFragment?
                    jobRegisterFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath, mIsFile)
                }
            }
        }
    }

//    override fun onBackPressed() {
//        try {
//            val companyReservationFragment: CompanyReservationFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_reservation_container) as CompanyReservationFragment?
//            if (companyReservationFragment!!.isCheckInformationView()) {
//                companyReservationFragment.hideInformationView()
//            } else {
//                finish()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            finish()
//        }
//    }
}