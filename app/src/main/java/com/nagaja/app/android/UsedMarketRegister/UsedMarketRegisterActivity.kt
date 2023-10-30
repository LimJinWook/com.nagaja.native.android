package com.nagaja.app.android.UsedMarketRegister

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

class UsedMarketRegisterActivity : NagajaActivity(), UsedMarketRegisterFragment.OnUsedMarketRegisterFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_used_market_register)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@UsedMarketRegisterActivity)!!, false)

        val itemUid = intent.getIntExtra(INTENT_DATA_MY_USED_MARKET_MODIFY_DATA, 0)
        val companyUid = intent.getIntExtra(INTENT_DATA_COMPANY_UID_DATA, 0)

        init(itemUid, companyUid)
    }

    private fun init(itemUid: Int, companyUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = UsedMarketRegisterFragment::class.java.name
        val usedMarketRegisterFragment = UsedMarketRegisterFragment.newInstance(itemUid, companyUid)
        fragmentTransaction.add(R.id.activity_used_market_register_container, usedMarketRegisterFragment, tag).commit()
    }

    private fun showMapScreen() {
        val intent = Intent(this@UsedMarketRegisterActivity, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, false)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@UsedMarketRegisterActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@UsedMarketRegisterActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@UsedMarketRegisterActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFinish() {
        finish()
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
                mPhotoUri = FileProvider.getUriForFile(this@UsedMarketRegisterActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onSuccessRegister() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onSuccessModify() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onBackPressed() {
        val usedMarketRegisterFragment: UsedMarketRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_used_market_register_container) as UsedMarketRegisterFragment?
        if (!usedMarketRegisterFragment!!.spinnerCheck()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_ADDRESS_REQUEST_CODE -> {
                    val addressData = data?.getSerializableExtra(INTENT_DATA_ADDRESS_DATA) as AddressData
                    val usedMarketRegisterFragment: UsedMarketRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_used_market_register_container) as UsedMarketRegisterFragment?
                    usedMarketRegisterFragment!!.selectMap(addressData)
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val usedMarketRegisterFragment: UsedMarketRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_used_market_register_container) as UsedMarketRegisterFragment?
                    usedMarketRegisterFragment!!.selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val usedMarketRegisterFragment: UsedMarketRegisterFragment? = supportFragmentManager.findFragmentById(R.id.activity_used_market_register_container) as UsedMarketRegisterFragment?
                    usedMarketRegisterFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }
            }
        }
    }
}