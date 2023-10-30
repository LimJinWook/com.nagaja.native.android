package com.nagaja.app.android.Missing

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
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MissingActivity : NagajaActivity(), MissingFragment.OnMissingFragmentListener,
    MissingDetailFragment.OnMissingDetailFragmentListener, MissingWriteFragment.OnMissingWriteFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    companion object {
        var mMainMenuItemData = MainMenuItemData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missing)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@MissingActivity)!!, false)

        mMainMenuItemData = intent.getSerializableExtra(INTENT_DATA_STORE_LIST_DATA) as MainMenuItemData
        val isMyMissing = intent.getBooleanExtra(INTENT_DATA_IS_MY_MISSING_DATA, false)

        init(mMainMenuItemData.getCategoryUid(), isMyMissing)
    }

    private fun init(categoryUid: Int, isMyMissing: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = MissingFragment::class.java.name
        val missingFragment = MissingFragment.newInstance(categoryUid, isMyMissing)
        fragmentTransaction.add(R.id.activity_missing_container, missingFragment, tag).commit()
    }

    private fun showMissingDetailScreen(boardUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = MissingDetailFragment::class.java.name
        val missingDetailFragment = MissingDetailFragment.newInstance(boardUid)
        fragmentTransaction.add(R.id.activity_missing_container, missingDetailFragment, tag).addToBackStack(tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this@MissingActivity, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showMissingWriteScreen(type: Int, boardUid: Int, isModify: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = MissingWriteFragment::class.java.name
        val missingWriteFragment = MissingWriteFragment.newInstance(type, boardUid, isModify)
        fragmentTransaction.replace(R.id.activity_missing_container, missingWriteFragment, tag).addToBackStack(tag).commit()
    }

    private fun showMapScreen() {
        val intent = Intent(this@MissingActivity, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, false)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@MissingActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@MissingActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@MissingActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onMissingDetailScreen(boardUid: Int) {
        showMissingDetailScreen(boardUid)
    }

    override fun onMissingWriteScreen(type: Int, isModify: Boolean) {
        showMissingWriteScreen(type, 0, isModify)
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onModifyScreen(boardUid: Int) {
        showMissingWriteScreen(0, boardUid, true)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onFinish() {
        finish()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onBack() {
        onBackPressed()
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
                mPhotoUri = FileProvider.getUriForFile(this@MissingActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onMapScreen() {
        showMapScreen()
    }

    override fun onSuccess(isDetailScreen: Boolean) {
        if (isDetailScreen) {
            val missingDetailFragment: MissingDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_missing_container) as MissingDetailFragment?
            missingDetailFragment!!.getBoardDetail()
        } else {
            val missingFragment: MissingFragment? = supportFragmentManager.findFragmentById(R.id.activity_missing_container) as MissingFragment?
//            missingFragment!!.getStoreCategoryData(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_ADDRESS_REQUEST_CODE -> {
                    val addressData = data?.getSerializableExtra(INTENT_DATA_ADDRESS_DATA) as AddressData
                    val missingWriteFragment: MissingWriteFragment? = supportFragmentManager.findFragmentById(R.id.activity_missing_container) as MissingWriteFragment?
                    missingWriteFragment!!.selectMap(addressData)
                }

//                INTENT_REPORT_REQUEST_CODE -> {
//                    val recommendStoreDetailFragment: RecommendStoreDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_recommend_store_container) as RecommendStoreDetailFragment?
//                    recommendStoreDetailFragment!!.getBoardDetail()
//                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val missingWriteFragment: MissingWriteFragment? = supportFragmentManager.findFragmentById(R.id.activity_missing_container) as MissingWriteFragment?
                    missingWriteFragment!!.selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val missingWriteFragment: MissingWriteFragment? = supportFragmentManager.findFragmentById(R.id.activity_missing_container) as MissingWriteFragment?
                    missingWriteFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }
            }
        }
    }
}