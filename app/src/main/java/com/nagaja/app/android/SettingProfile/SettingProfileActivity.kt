package com.nagaja.app.android.SettingProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Address.AddressActivity
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_CHANGE_PHONE_NUMBER
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHONE_AUTH_TYPE_MEMBER_WITHDRAWAL
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.AddressData
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.PhoneAuth.PhoneAuthActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SettingProfileActivity : NagajaActivity(), SettingProfileFragment.OnSettingProfileFragmentListener,
    ModifyProfileFragment.OnModifyProfileFragmentListener, MemberWithdrawalFragment.OnMemberWithdrawalFragmentListener {

    var mCaptureImageFileName: String = ""
    var mCurrentPhotoPath: String = ""
    lateinit var mPhotoUri: Uri

    companion object {
        var mIsSuccessMemberWithdrawal = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_profile)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@SettingProfileActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = SettingProfileFragment::class.java.name
        val settingProfileFragment = SettingProfileFragment.newInstance()
        fragmentTransaction.add(R.id.activity_setting_profile_container, settingProfileFragment, tag).commit()
    }

    private fun showModifyScreen() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ModifyProfileFragment::class.java.name
        val modifyProfileFragment = ModifyProfileFragment.newInstance()
        fragmentTransaction.replace(R.id.activity_setting_profile_container, modifyProfileFragment, tag).addToBackStack(tag).commit()
    }

    private fun showPhoneAuthScreen() {
        val intent = Intent(this, PhoneAuthActivity::class.java)
        intent.putExtra(INTENT_DATA_PHONE_AUTH_TYPE_DATA, PHONE_AUTH_TYPE_MEMBER_WITHDRAWAL)
        startActivityForResult(intent, INTENT_PHONE_AUTH_CHANGE_PHONE_NUMBER_REQUEST_CODE)
    }

    private fun shoMemberWithdrawalScreen() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = MemberWithdrawalFragment::class.java.name
        val memberWithdrawalFragment = MemberWithdrawalFragment.newInstance()
        fragmentTransaction.replace(R.id.activity_setting_profile_container, memberWithdrawalFragment, tag).addToBackStack(tag).commit()
    }

    private fun showAddressScreen(isKorea: Boolean) {
        val intent = Intent(this@SettingProfileActivity, AddressActivity::class.java)
        intent.putExtra(INTENT_DATA_IS_KOREA_SELECT_DATA, isKorea)
        startActivityForResult(intent, INTENT_ADDRESS_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@SettingProfileActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@SettingProfileActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@SettingProfileActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onModifyScreen() {
        showModifyScreen()
    }

    override fun onPhoneAuthScreen() {
        showPhoneAuthScreen()
    }

    override fun onSuccessMemberWithdrawal() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onMemberWithdrawalScreen() {
        shoMemberWithdrawalScreen()
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
                mPhotoUri = FileProvider.getUriForFile(this@SettingProfileActivity, "$packageName.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
                startActivityForResult(takePictureIntent, INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onAddressScreen(isKorea: Boolean) {
        showAddressScreen(isKorea)
    }

    override fun onSuccessModifyProfile() {
        onBackPressed()

        val settingProfileFragment: SettingProfileFragment? = supportFragmentManager.findFragmentById(R.id.activity_setting_profile_container) as SettingProfileFragment?
        settingProfileFragment!!.updateProfileImage()
    }

    override fun onBackPressed() {
        if (mIsSuccessMemberWithdrawal) {
            onSuccessMemberWithdrawal()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_PHONE_AUTH_CHANGE_PHONE_NUMBER_REQUEST_CODE -> {
                    val phoneNumber = data?.getStringExtra(INTENT_DATA_PHONE_NUMBER_CHANGE_DATA)
                    val settingProfileFragment: SettingProfileFragment? = supportFragmentManager.findFragmentById(R.id.activity_setting_profile_container) as SettingProfileFragment?
                    settingProfileFragment!!.successPhoneNumberAuth(phoneNumber!!)
                }

                INTENT_IMAGE_UPLOAD_GALLERY_REQUEST_CODE -> {
                    val modifyProfileFragment: ModifyProfileFragment? = supportFragmentManager.findFragmentById(R.id.activity_setting_profile_container) as ModifyProfileFragment?
                    modifyProfileFragment!!.selectImage(data!!.data!!)
                }

                INTENT_IMAGE_UPLOAD_CAMERA_REQUEST_CODE -> {
                    val modifyProfileFragment: ModifyProfileFragment? = supportFragmentManager.findFragmentById(R.id.activity_setting_profile_container) as ModifyProfileFragment?
                    modifyProfileFragment!!.getCameraImageData(mPhotoUri, mCurrentPhotoPath)
                }

                INTENT_ADDRESS_REQUEST_CODE -> {
                    val addressData = data?.getSerializableExtra(INTENT_DATA_ADDRESS_DATA) as AddressData
                    val settingProfileFragment: SettingProfileFragment? = supportFragmentManager.findFragmentById(R.id.activity_setting_profile_container) as SettingProfileFragment?
                    settingProfileFragment!!.getAddress(addressData)
                }
            }
        }
    }
}