package com.nagaja.app.android.BoardDetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.BoardWrite.BoardWriteActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.FullScreenImage.FullScreenImageActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.Report.ReportActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class BoardDetailActivity : NagajaActivity(), BoardDetailFragment.OnBoardFragmentListener {

    companion object {
        var mSelectBoardType = 0
        var mSelectBoardPosition = 0
        var mIsModify = false
        var mIsLikeBookmarkChange = false
        var mIsLikeChange = false
        var mIsBookmarkChange = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@BoardDetailActivity)!!, false)

        val boardUid = intent.getIntExtra(INTENT_DATA_BOARD_UID_DATA, 0)
        mSelectBoardType = intent.getIntExtra(INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA, 0)
        mSelectBoardPosition = intent.getIntExtra(INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA, 0)

        init(boardUid)
    }

    private fun init(boardUid: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = BoardDetailFragment::class.java.name
        val boardDetailFragment = BoardDetailFragment.newInstance(boardUid)
        fragmentTransaction.replace(R.id.activity_board_detail_container, boardDetailFragment, tag).commit()
    }

//    private fun init(boardUid: Int) {
//        val fragmentManager: FragmentManager = supportFragmentManager
//
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        val tag: String = BoardDetailFragment::class.java.name
//        val nativeBoardDetailFragment = NativeBoardDetailFragment.newInstance(boardUid)
//        fragmentTransaction.replace(R.id.activity_board_detail_container, nativeBoardDetailFragment, tag).commit()
//    }

    private fun showFullScreenImage(imageList: ArrayList<String>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_LIST_DATA, imageList)
        intent.putExtra(INTENT_DATA_IMAGE_PATH_POSITION, position)
        startActivity(intent)
    }

    private fun showWriteBoardScreen(categoryUid: Int, boardUid: Int) {
        val intent = Intent(this, BoardWriteActivity::class.java)
        intent.putExtra(INTENT_DATA_BOARD_CATEGORY_DATA, categoryUid)
        intent.putExtra(INTENT_DATA_BOARD_UID_DATA, boardUid)
        intent.putExtra(INTENT_DATA_BOARD_MODIFY_DATA, true)
        startActivityForResult(intent, INTENT_BOARD_WRITE_REQUEST_CODE)
    }

    override fun onFullScreenImage(imageList: ArrayList<String>, position: Int) {
        showFullScreenImage(imageList, position)
    }

    override fun onSuccessDelete() {
        intent.putExtra(INTENT_DATA_BOARD_DELETE_SUCCESS, true)
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA, mSelectBoardType)
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA, mSelectBoardPosition)
        setResult(RESULT_OK, intent)

        finish()
    }

    private fun showReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        val intent = Intent(this@BoardDetailActivity, ReportActivity::class.java)
        intent.putExtra(INTENT_DATA_REPORT_TYPE_DATA, reportType)
        intent.putExtra(INTENT_DATA_REPORT_UID_DATA, reviewUid)
        intent.putExtra(INTENT_DATA_REPORT_STORE_NAME_DATA, storeName)
        startActivityForResult(intent, INTENT_REPORT_REQUEST_CODE)
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@BoardDetailActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@BoardDetailActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@BoardDetailActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onWriteBoardScreen(categoryUid: Int, boardUid: Int) {
        showWriteBoardScreen(categoryUid, boardUid)
    }

    override fun onReportScreen(reportType: Int, reviewUid: Int, storeName: String) {
        showReportScreen(reportType, reviewUid, storeName)
    }

    override fun onDefaultLoginScreen() {
        defaultLoginScreen()
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onFinish() {
        if (mIsModify) {
            intent.putExtra(INTENT_DATA_BOARD_MODIFY_DATA, true)
            setResult(RESULT_OK, intent)
        } else if (mIsLikeBookmarkChange) {
            intent.putExtra(INTENT_DATA_BOARD_LIKE_BOOKMARK_CHANGE_DATA, true)
            intent.putExtra(INTENT_DATA_BOARD_MODIFY_LIKE_DATA, mIsLikeChange)
            intent.putExtra(INTENT_DATA_BOARD_MODIFY_BOOKMARK_DATA, mIsBookmarkChange)
            setResult(RESULT_OK, intent)
        }
        finish()
    }

    override fun onBackPressed() {
        onFinish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_REPORT_REQUEST_CODE -> {
                    val boardDetailFragment: BoardDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_board_detail_container) as BoardDetailFragment?
                    boardDetailFragment!!.successReport()
                }

                INTENT_BOARD_WRITE_REQUEST_CODE -> {
                    val boardDetailFragment: BoardDetailFragment? = supportFragmentManager.findFragmentById(R.id.activity_board_detail_container) as BoardDetailFragment?
                    boardDetailFragment!!.getBoardDetail()

                    mIsModify = true
                }
            }
        }
    }
}
