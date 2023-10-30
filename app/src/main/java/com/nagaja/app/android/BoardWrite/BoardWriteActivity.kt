package com.nagaja.app.android.BoardWrite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
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
import kotlin.collections.ArrayList

class BoardWriteActivity : NagajaActivity(), BoardWriteFragment.OnBoardWriteFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@BoardWriteActivity)!!, false)

        val categoryUid = intent.getIntExtra(INTENT_DATA_BOARD_CATEGORY_DATA, 0)
        val boardUid = intent.getIntExtra(INTENT_DATA_BOARD_UID_DATA, 0)
        val isModify = intent.getBooleanExtra(INTENT_DATA_BOARD_MODIFY_DATA, false)

        init(categoryUid, boardUid, isModify)
    }

    private fun init(categoryUid: Int, boardUid: Int, isModify: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = BoardWriteFragment::class.java.name
        val boardWriteFragment = BoardWriteFragment.newInstance(categoryUid, boardUid, isModify)
        fragmentTransaction.replace(R.id.activity_board_write_container, boardWriteFragment, tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@BoardWriteActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@BoardWriteActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@BoardWriteActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFinish() {
        finish()
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onDelete(selectType: Int, position: Int) {
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA, selectType)
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA, position)
        setResult(RESULT_OK, intent)

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
                mPhotoUri = FileProvider.getUriForFile(this@BoardWriteActivity, "$packageName.fileprovider", photoFile)
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

//                INTENT_REPORT_REQUEST_CODE -> {
//                    val recommendStoreDetailFragment: RecommendStoreDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_recommend_store_container) as RecommendStoreDetailFragment?
//                    recommendStoreDetailFragment!!.getBoardDetail()
//                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val boardWriteFragment: BoardWriteFragment? = supportFragmentManager.findFragmentById(R.id.activity_board_write_container) as BoardWriteFragment?
                    boardWriteFragment!!.selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val boardWriteFragment: BoardWriteFragment? = supportFragmentManager.findFragmentById(R.id.activity_board_write_container) as BoardWriteFragment?
                    boardWriteFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }
            }
        }
    }

}
