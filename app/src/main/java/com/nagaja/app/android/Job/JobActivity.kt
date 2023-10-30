package com.nagaja.app.android.Job

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
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.Data.NativeJobAndMissingData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Store.StoreActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class JobActivity : NagajaActivity(), JobFragment.OnJobFragmentListener, JobDetailFragment.OnJobDetailFragmentListener,
    NativeJobFragment.OnNativeJobFragmentListener, NativeJobDetailFragment.OnNativeJobDetailFragmentListener,
    JobRegisterFragment.OnJobRegisterFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri
    var mIsFile = false

    companion object {
        var mMainMenuItemData = MainMenuItemData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@JobActivity)!!, false)

        mMainMenuItemData = intent.getSerializableExtra(INTENT_DATA_STORE_LIST_DATA) as MainMenuItemData
        val isMyJob = intent.getBooleanExtra(INTENT_DATA_IS_MY_JOB_DATA, false)

        init(mMainMenuItemData.getCategoryUid(), isMyJob)
//        nativeInit()
    }

    private fun init(categoryUid: Int, isMyJob: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = JobFragment::class.java.name
        val jobFragment = JobFragment.newInstance(categoryUid, isMyJob)
        fragmentTransaction.add(R.id.activity_job_container, jobFragment, tag).commit()
    }

    private fun nativeInit() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NativeJobFragment::class.java.name
        val nativeJobFragment = NativeJobFragment.newInstance()
        fragmentTransaction.add(R.id.activity_job_container, nativeJobFragment, tag).commit()
    }

    private fun showJobDetailScreen(boardUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = JobDetailFragment::class.java.name
        val jobDetailFragment = JobDetailFragment.newInstance(boardUid, 0)
        fragmentTransaction.replace(R.id.activity_job_container, jobDetailFragment, tag).addToBackStack(tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showNativeJobDetailScreen(data: NativeJobAndMissingData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NativeJobDetailFragment::class.java.name
        val nativeJobDetailFragment = NativeJobDetailFragment.newInstance(data)
        fragmentTransaction.replace(R.id.activity_job_container, nativeJobDetailFragment, tag).addToBackStack(tag).commit()
    }

    private fun showJobRegisterScreen(jobType: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = JobRegisterFragment::class.java.name
        val jobRegisterFragment = JobRegisterFragment.newInstance(jobType, 0, 0)
        fragmentTransaction.replace(R.id.activity_job_container, jobRegisterFragment, tag).addToBackStack(tag).commit()
    }

    private fun showJobModifyScreen(jobType: Int, boardUid: Int, companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = JobRegisterFragment::class.java.name
        val jobRegisterFragment = JobRegisterFragment.newInstance(jobType, companyUid, boardUid)
        fragmentTransaction.replace(R.id.activity_job_container, jobRegisterFragment, tag).addToBackStack(tag).commit()
    }

    private fun showMapScreen() {
        val intent = Intent(this@JobActivity, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, false)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showChatViewScreen(chatClass: String, chatData: String) {
        val intent = Intent(this@JobActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, chatClass)
        intent.putExtra(INTENT_DATA_CHAT_DATA, chatData)
        startActivity(intent)
    }

    private fun showStoreScreen() {
        val intent = Intent(this@JobActivity, StoreActivity::class.java)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@JobActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@JobActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@JobActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onJobDetailScreen(boardUid: Int) {
        showJobDetailScreen(boardUid)
    }

    override fun onJobRegister(jobType: Int) {
        showJobRegisterScreen(jobType)
    }

    override fun onFinish() {
        finish()
    }

    override fun onNativeJobDetailScreen(data: NativeJobAndMissingData) {
        showNativeJobDetailScreen(data)
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

    override fun onChatViewScreen(chatClass: String, chatData: String) {
        showChatViewScreen(chatClass, chatData)
    }

    override fun onGalleryImageSelect(isFile: Boolean) {
        mIsFile = isFile
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE)
    }

    override fun onCompanyRegisterSuccess() {
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
                mPhotoUri = FileProvider.getUriForFile(this@JobActivity, "$packageName.fileprovider", photoFile)
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
                    val jobRegisterFragment: JobRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_job_container) as JobRegisterFragment?
                    jobRegisterFragment!!.selectMap(addressData)
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val jobRegisterFragment: JobRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_job_container) as JobRegisterFragment?
                    jobRegisterFragment!!.selectImage(data!!.data!!, mIsFile)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val jobRegisterFragment: JobRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_job_container) as JobRegisterFragment?
                    jobRegisterFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath, mIsFile)
                }
            }
        }
    }
}