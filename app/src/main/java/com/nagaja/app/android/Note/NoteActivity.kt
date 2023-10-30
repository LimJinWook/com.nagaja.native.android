package com.nagaja.app.android.Note

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Data.NoteData
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.CompanyDefaultData
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

class NoteActivity : NagajaActivity(), NoteFragment.OnNoteFragmentListener,
    NoteDetailFragment.OnNoteDetailFragmentListener, NoteReplyFragment.OnNoteReplyFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri
    var mIsFile = false

    var mIsCompanyNote = false
    var mCompanyDefaultData: CompanyDefaultData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@NoteActivity)!!, false)

        mIsCompanyNote = intent.getBooleanExtra(INTENT_DATA_IS_COMPANY_NOTE_DATA, false)
        val boardUid = intent.getIntExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, -1)

        if (mIsCompanyNote) {
            mCompanyDefaultData = intent.getSerializableExtra(INTENT_DATA_COMPANY_DEFAULT_DATA) as CompanyDefaultData
            companyNoteInit(mCompanyDefaultData!!, boardUid)
        } else {
            init(boardUid)
        }

    }

    private fun init(noteUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NoteFragment::class.java.name
        val noteFragment = NoteFragment.newInstance(false, noteUid)
        fragmentTransaction.add(R.id.activity_note_container, noteFragment, tag).commit()
    }

    private fun companyNoteInit(companyDefaultData: CompanyDefaultData, noteUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NoteFragment::class.java.name
        val noteFragment = NoteFragment.newInstance(companyDefaultData, true, noteUid)
        fragmentTransaction.add(R.id.activity_note_container, noteFragment, tag).commit()
    }

    private fun showNoteDetailScreen(data: NoteData, isCompanyNote: Boolean, companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NoteDetailFragment::class.java.name
        val noteDetailFragment = NoteDetailFragment.newInstance(data, isCompanyNote, companyUid)
        fragmentTransaction.add(R.id.activity_note_container, noteDetailFragment, tag).addToBackStack(tag).commit()
    }

    private fun showReplyScreen(data: NoteData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NoteReplyFragment::class.java.name
        val noteReplyFragment = NoteReplyFragment.newInstance(data)
        fragmentTransaction.replace(R.id.activity_note_container, noteReplyFragment, tag).addToBackStack(tag).commit()
    }

    private fun showEmptyReplyScreen() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NoteReplyFragment::class.java.name
        val noteReplyFragment = NoteReplyFragment.newInstance()
        fragmentTransaction.replace(R.id.activity_note_container, noteReplyFragment, tag).addToBackStack(tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showReportScreen(reportType: Int, noteUid: Int, storeName: String) {
        val intent = Intent(this@NoteActivity, ReportActivity::class.java)
        intent.putExtra(INTENT_DATA_REPORT_TYPE_DATA, reportType)
        intent.putExtra(INTENT_DATA_REPORT_UID_DATA, noteUid)
        intent.putExtra(INTENT_DATA_REPORT_STORE_NAME_DATA, storeName)
        if (null != mCompanyDefaultData) {
            intent.putExtra(INTENT_DATA_COMPANY_UID_DATA, if (mCompanyDefaultData!!.getCompanyUid() > 0) mCompanyDefaultData!!.getCompanyUid() else 0)
        }
        startActivityForResult(intent, INTENT_REPORT_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@NoteActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@NoteActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@NoteActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onNoteDetailScreen(data: NoteData, isCompanyNote: Boolean, companyUid: Int) {
        showNoteDetailScreen(data, isCompanyNote, companyUid)
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onSuccess() {
        onBack()

        val noteFragment: NoteFragment? = supportFragmentManager.findFragmentById(R.id.activity_note_container) as NoteFragment?
        noteFragment!!.getNoteList(true)
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onReplyScreen(data: NoteData) {
        showReplyScreen(data)
    }

    override fun onReplyScreen() {
        showEmptyReplyScreen()
    }

    override fun onReportScreen(reportType: Int, noteUid: Int, storeName: String) {
        showReportScreen(reportType, noteUid, storeName)
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
                mPhotoUri = FileProvider.getUriForFile(this@NoteActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_REPORT_REQUEST_CODE -> {
                    val noteDetailFragment: NoteDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_note_container) as NoteDetailFragment?
                    noteDetailFragment!!.successReport()
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val noteReplyFragment: NoteReplyFragment? = supportFragmentManager.findFragmentById(R.id.activity_note_container) as NoteReplyFragment?
                    noteReplyFragment!!.selectImage(data!!.data!!, mIsFile)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val noteReplyFragment: NoteReplyFragment? = supportFragmentManager.findFragmentById(R.id.activity_note_container) as NoteReplyFragment?
                    noteReplyFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath, mIsFile)
                }
            }
        }
    }
}