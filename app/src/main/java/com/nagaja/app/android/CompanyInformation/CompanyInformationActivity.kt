package com.nagaja.app.android.CompanyInformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Address.AddressActivity
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_JOB_AND_JOB_SEARCH
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.CompanyJob.CompanyJobActivity
import com.nagaja.app.android.CompanyReservation.CompanyReservationActivity
import com.nagaja.app.android.CompanyUsedMarket.CompanyUsedMarketActivity
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Data.CompanyDefaultData
import com.nagaja.app.android.Data.CompanyMemberData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Note.NoteActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.PointHistory.PointHistoryActivity
import com.nagaja.app.android.Regular.RegularActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CompanyInformationActivity : NagajaActivity(), CompanyInformationFragment.OnCompanyInformationFragmentListener,
    CompanyDefaultFragment.OnCompanyDefaultFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri
    var mIsFile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_information)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@CompanyInformationActivity)!!, false)

        val companyMemberData = intent.getSerializableExtra(INTENT_DATA_MY_COMPANY_DATA) as CompanyMemberData

        init(companyMemberData)
    }

    private fun init(companyMemberData: CompanyMemberData) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = CompanyInformationFragment::class.java.name
        val companyInformationFragment = CompanyInformationFragment.newInstance(companyMemberData)
        fragmentTransaction.add(R.id.activity_company_information_container, companyInformationFragment, tag).commit()
    }

    private fun showCompanyDefaultScreen(data: CompanyDefaultData, selectType: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = CompanyDefaultFragment::class.java.name
        val companyDefaultFragment = CompanyDefaultFragment.newInstance(data, selectType)
        fragmentTransaction.replace(R.id.activity_company_information_container, companyDefaultFragment, tag).addToBackStack(tag).commit()

//        val intent = Intent(this@CompanyInformationActivity, CompanyDefaultInformationActivity::class.java)
//        intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, data)
//        intent.putExtra(INTENT_DATA_SELECT_COMPANY_MANAGEMENT_TYPE_DATA, selectType)
//        startActivity(intent)
    }

    private fun showMapScreen() {
        val intent = Intent(this@CompanyInformationActivity, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, false)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showCompanyNoteScreen(data: CompanyDefaultData) {
        val intent = Intent(this@CompanyInformationActivity, NoteActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_COMPANY_NOTE_DATA, true)
        intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, data)
        startActivity(intent)
    }

    private fun showCompanyRegularScreen(data: CompanyDefaultData) {
        val intent = Intent(this@CompanyInformationActivity, RegularActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_COMPANY_NOTE_DATA, true)
        intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, data)
        startActivity(intent)
    }

    private fun showCompanyReservationScreen(data: CompanyDefaultData) {
        val intent = Intent(this@CompanyInformationActivity, CompanyReservationActivity::class.java)
        intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, data)
        startActivity(intent)
    }

    private fun showCompanyUsedMarketScreen(data: CompanyDefaultData) {
        val intent = Intent(this@CompanyInformationActivity, CompanyUsedMarketActivity::class.java)
        intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, data)
        startActivity(intent)
    }

    private fun showCompanyJobScreen(data: CompanyDefaultData) {
        val intent = Intent(this@CompanyInformationActivity, CompanyJobActivity::class.java)
        intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, data)
        intent.putExtra(INTENT_DATA_COMPANY_JOB_UID_DATA, COMPANY_TYPE_JOB_AND_JOB_SEARCH)
        startActivity(intent)
    }

    private fun showPointHistoryScreen(companyUid: Int) {
        val intent = Intent(this@CompanyInformationActivity, PointHistoryActivity::class.java)
        intent.putExtra(INTENT_DATA_COMPANY_UID_DATA, companyUid)
        startActivity(intent)
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
                intent = Intent(this@CompanyInformationActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@CompanyInformationActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@CompanyInformationActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onGalleryImageSelect(isFile: Boolean) {
        mIsFile = isFile

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE)
    }

    override fun onBack() {
        onBackPressed()
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
                mPhotoUri = FileProvider.getUriForFile(this@CompanyInformationActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onMapScreen() {
        showMapScreen()
    }

    override fun onCompanyDefaultScreen(data: CompanyDefaultData, selectType: Int) {
        showCompanyDefaultScreen(data, selectType)
    }

    override fun onCompanyNoteScreen(data: CompanyDefaultData) {
        showCompanyNoteScreen(data)
    }

    override fun onCompanyRegularScreen(data: CompanyDefaultData) {
        showCompanyRegularScreen(data)
    }

    override fun onCompanyReservationScreen(data: CompanyDefaultData) {
        showCompanyReservationScreen(data)
    }

    override fun onCompanyUsedMarketScreen(data: CompanyDefaultData) {
        showCompanyUsedMarketScreen(data)
    }

    override fun onCompanyJobScreen(data: CompanyDefaultData) {
        showCompanyJobScreen(data)
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

    override fun onPointHistoryScreen(companyUid: Int) {
        showPointHistoryScreen(companyUid)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_ADDRESS_REQUEST_CODE -> {
                    val addressData = data?.getSerializableExtra(INTENT_DATA_ADDRESS_DATA) as AddressData
                    val companyDefaultFragment: CompanyDefaultFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_information_container) as CompanyDefaultFragment?
                    companyDefaultFragment!!.selectMap(addressData)
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val companyDefaultFragment: CompanyDefaultFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_information_container) as CompanyDefaultFragment?
                    companyDefaultFragment!!.selectImage(data!!.data!!, mIsFile)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val companyDefaultFragment: CompanyDefaultFragment? = supportFragmentManager.findFragmentById(R.id.activity_company_information_container) as CompanyDefaultFragment?
                    companyDefaultFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath, mIsFile)
                }
            }
        }
    }
}