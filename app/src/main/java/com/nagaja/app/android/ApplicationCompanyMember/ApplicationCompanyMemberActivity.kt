package com.nagaja.app.android.ApplicationCompanyMember

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
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ApplicationCompanyMemberActivity : NagajaActivity(), ApplicationCompanyMemberFragment.OnApplicationCompanyMemberFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri
    var mIsFile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_company_member)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@ApplicationCompanyMemberActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ApplicationCompanyMemberFragment::class.java.name
        val applicationCompanyMemberFragment = ApplicationCompanyMemberFragment.newInstance()
        fragmentTransaction.add(R.id.activity_application_company_member_container, applicationCompanyMemberFragment, tag).commit()
    }

    private fun showMapScreen() {
        val intent = Intent(this@ApplicationCompanyMemberActivity, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, false)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@ApplicationCompanyMemberActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@ApplicationCompanyMemberActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@ApplicationCompanyMemberActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
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
                mPhotoUri = FileProvider.getUriForFile(this@ApplicationCompanyMemberActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onMapScreen() {
        showMapScreen()
    }

    override fun onSuccessRegister() {
        setResult(RESULT_OK)
        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {

                INTENT_ADDRESS_REQUEST_CODE -> {
                    val addressData = data?.getSerializableExtra(INTENT_DATA_ADDRESS_DATA) as AddressData
                    val applicationCompanyMemberFragment: ApplicationCompanyMemberFragment? = supportFragmentManager.findFragmentById(R.id.activity_application_company_member_container) as ApplicationCompanyMemberFragment?
                    applicationCompanyMemberFragment!!.selectMap(addressData)
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val applicationCompanyMemberFragment: ApplicationCompanyMemberFragment? = supportFragmentManager.findFragmentById(R.id.activity_application_company_member_container) as ApplicationCompanyMemberFragment?
                    applicationCompanyMemberFragment!!.selectImage(data!!.data!!, mIsFile)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val applicationCompanyMemberFragment: ApplicationCompanyMemberFragment? = supportFragmentManager.findFragmentById(R.id.activity_application_company_member_container) as ApplicationCompanyMemberFragment?
                    applicationCompanyMemberFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath, mIsFile)
                }

//                INTENT_SELECT_MAP_REQUEST_CODE -> {
//                    val latitude = data!!.getStringExtra(INTENT_DATA_SELECT_LATITUDE_DATA)
//                    val longitude = data.getStringExtra(INTENT_DATA_SELECT_LONGITUDE_DATA)
//
//                    val applicationCompanyMemberFragment: ApplicationCompanyMemberFragment? = supportFragmentManager.findFragmentById(R.id.activity_application_company_member_container) as ApplicationCompanyMemberFragment?
//                    applicationCompanyMemberFragment!!.getLocation(latitude!!, longitude!!)
//                }
            }
        }
    }
}