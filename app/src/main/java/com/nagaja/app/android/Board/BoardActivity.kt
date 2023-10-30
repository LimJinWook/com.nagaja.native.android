package com.nagaja.app.android.Board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_FREE_BOARD
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_PLAYGROUND
import com.nagaja.app.android.BoardDetail.BoardDetailActivity
import com.nagaja.app.android.BoardWrite.BoardWriteActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class BoardActivity : NagajaActivity(), BoardFragment.OnBoardFragmentListener {

    companion object {
        var mMainMenuItemData = MainMenuItemData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@BoardActivity)!!, false)

        mMainMenuItemData = intent.getSerializableExtra(INTENT_DATA_STORE_LIST_DATA) as MainMenuItemData

        var categoryUid = mMainMenuItemData.getCategoryUid()
        var isPlayground = false

        if (categoryUid == COMPANY_TYPE_PLAYGROUND) {
            categoryUid = COMPANY_TYPE_FREE_BOARD
            isPlayground = true
        }

        init(categoryUid, isPlayground)
    }

    private fun init(categoryUid: Int, isPlayground: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = BoardFragment::class.java.name
        val boardFragment = BoardFragment.newInstance(categoryUid, isPlayground)
        fragmentTransaction.add(R.id.activity_board_container, boardFragment, tag).commit()
    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showBoardDetailScreen(boardUid: Int, selectBoardType: Int, position: Int) {
        val intent = Intent(this, BoardDetailActivity::class.java)
        intent.putExtra(INTENT_DATA_BOARD_UID_DATA, boardUid)
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA, selectBoardType)
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA, position)
        startActivityForResult(intent, INTENT_BOARD_REQUEST_CODE)
    }

    private fun showWriteBoardScreen(categoryUid: Int) {
        val intent = Intent(this, BoardWriteActivity::class.java)
        intent.putExtra(INTENT_DATA_BOARD_CATEGORY_DATA, categoryUid)
        startActivityForResult(intent, INTENT_BOARD_WRITE_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@BoardActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@BoardActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@BoardActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onBoardDetailScreen(boardUid: Int, selectBoardType: Int, position: Int) {
        showBoardDetailScreen(boardUid, selectBoardType, position)
    }

    override fun onWriteBoardScreen(categoryUid: Int) {
        showWriteBoardScreen(categoryUid)
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
                INTENT_BOARD_REQUEST_CODE -> {
                    val boardFragment: BoardFragment? = supportFragmentManager.findFragmentById(R.id.activity_board_container) as BoardFragment?

                    if (data!!.getBooleanExtra(INTENT_DATA_BOARD_MODIFY_DATA, false)) {
                        boardFragment!!.setRefresh()
                    } else if (data.getBooleanExtra(INTENT_DATA_BOARD_LIKE_BOOKMARK_CHANGE_DATA, false)) {
                        boardFragment!!.changeClickData(
                            data.getBooleanExtra(INTENT_DATA_BOARD_MODIFY_LIKE_DATA, false),
                            data.getBooleanExtra(INTENT_DATA_BOARD_MODIFY_BOOKMARK_DATA, false)
                        )
                    } else if (data.getBooleanExtra(INTENT_DATA_BOARD_DELETE_SUCCESS, false)) {
                        boardFragment!!.successDelete(
                            data.getIntExtra(INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA, 0),
                            data.getIntExtra(INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA, 0)
                        )
                    }
                }

                INTENT_BOARD_WRITE_REQUEST_CODE -> {
                    val boardFragment: BoardFragment? = supportFragmentManager.findFragmentById(R.id.activity_board_container) as BoardFragment?
                    boardFragment!!.setRefresh()
                }
            }
        }
    }
}
