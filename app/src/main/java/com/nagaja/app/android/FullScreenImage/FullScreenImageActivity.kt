package com.nagaja.app.android.FullScreenImage

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class FullScreenImageActivity : NagajaActivity(), FullScreenImageFragment.OnFullScreenImageFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@FullScreenImageActivity)!!, false)

        val imagePathList = intent.getSerializableExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA) as ArrayList<String>
        val position = intent.getIntExtra(INTENT_DATA_IMAGE_PATH_POSITION, -1)

        init(imagePathList, position)
    }

    private fun init(imagePathList: ArrayList<String>, position: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = FullScreenImageFragment::class.java.name
        val fullScreenImageFragment: FullScreenImageFragment = FullScreenImageFragment.newInstance(imagePathList, position)
        fragmentTransaction.add(R.id.activity_full_screen_image_container, fullScreenImageFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }
}