package com.nagaja.app.android.RecommendStore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Report.ReportActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecommendStoreActivity : NagajaActivity(), RecommendStoreFragment.OnRecommendStoreFragmentListener,
    RecommendStoreDetailFragment.OnRecommendStoreFragmentListener, RecommendStoreWriteFragment.OnRecommendStoreWriteFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    companion object {
        var mMainMenuItemData = MainMenuItemData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_store)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@RecommendStoreActivity)!!, false)

        mMainMenuItemData = intent.getSerializableExtra(INTENT_DATA_STORE_LIST_DATA) as MainMenuItemData

        init(mMainMenuItemData.getCategoryUid())
    }

    private fun init(categoryUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = RecommendStoreFragment::class.java.name
        val recommendStoreFragment = RecommendStoreFragment.newInstance(categoryUid)
        fragmentTransaction.add(R.id.activity_recommend_store_container, recommendStoreFragment, tag).commit()
    }

    private fun showRecommendStoreDetailScreen(categoryUid: Int, boardUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = RecommendStoreDetailFragment::class.java.name
        val recommendStoreDetailFragment = RecommendStoreDetailFragment.newInstance(categoryUid, boardUid)
        fragmentTransaction.add(R.id.activity_recommend_store_container, recommendStoreDetailFragment, tag).addToBackStack(tag).commit()
    }

    private fun showRecommendStoreWriteScreen(categoryUid: Int, boardUid: Int, isModify: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = RecommendStoreWriteFragment::class.java.name
        val recommendStoreWriteFragment = RecommendStoreWriteFragment.newInstance(categoryUid, boardUid, isModify)
        fragmentTransaction.replace(R.id.activity_recommend_store_container, recommendStoreWriteFragment, tag).addToBackStack(tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        val intent = Intent(this@RecommendStoreActivity, ReportActivity::class.java)
        intent.putExtra(INTENT_DATA_REPORT_TYPE_DATA, reportType)
        intent.putExtra(INTENT_DATA_REPORT_UID_DATA, reviewUid)
        intent.putExtra(INTENT_DATA_REPORT_STORE_NAME_DATA, storeName)
        startActivityForResult(intent, INTENT_REPORT_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@RecommendStoreActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@RecommendStoreActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@RecommendStoreActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFinish() {
        finish()
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onRecommendStoreDetailScreen(categoryUid: Int, boardUid: Int) {
        showRecommendStoreDetailScreen(categoryUid, boardUid)
    }

    override fun onRecommendStoreWriteScreen(categoryUid: Int, boardUid: Int, isModify: Boolean) {
        showRecommendStoreWriteScreen(categoryUid, boardUid, isModify)
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        showReportScreen(reportType, reviewUid, storeName)
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onGalleryImageSelect() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
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
                mPhotoUri = FileProvider.getUriForFile(this@RecommendStoreActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onSuccess(isDetailScreen: Boolean) {
        if (isDetailScreen) {
            val recommendStoreDetailFragment: RecommendStoreDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_recommend_store_container) as RecommendStoreDetailFragment?
            recommendStoreDetailFragment!!.getBoardDetail()
        } else {
            val recommendStoreFragment: RecommendStoreFragment? = supportFragmentManager.findFragmentById(R.id.activity_recommend_store_container) as RecommendStoreFragment?
            recommendStoreFragment!!.getStoreCategoryData(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_REPORT_REQUEST_CODE -> {
                    val recommendStoreDetailFragment: RecommendStoreDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_recommend_store_container) as RecommendStoreDetailFragment?
                    recommendStoreDetailFragment!!.getBoardDetail()
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val recommendStoreWriteFragment: RecommendStoreWriteFragment? = supportFragmentManager.findFragmentById(R.id.activity_recommend_store_container) as RecommendStoreWriteFragment?
                    recommendStoreWriteFragment!!.selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val recommendStoreWriteFragment: RecommendStoreWriteFragment? = supportFragmentManager.findFragmentById(R.id.activity_recommend_store_container) as RecommendStoreWriteFragment?
                    recommendStoreWriteFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }
            }
        }
    }

}