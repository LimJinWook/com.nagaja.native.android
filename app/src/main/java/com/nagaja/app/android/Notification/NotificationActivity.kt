package com.nagaja.app.android.Notification

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.ChatView.ChatViewActivity
import com.nagaja.app.android.Note.NoteActivity
import com.nagaja.app.android.PointHistory.PointHistoryActivity
import com.nagaja.app.android.Reservation.ReservationActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class NotificationActivity : NagajaActivity(), NotificationFragment.OnNoticeFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@NotificationActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = NotificationFragment::class.java.name
        val notificationFragment = NotificationFragment.newInstance()
        fragmentTransaction.add(R.id.activity_notification_container, notificationFragment, tag).commit()
    }

    private fun showNoteScreen() {
        val intent = Intent(this@NotificationActivity, NoteActivity::class.java)
        startActivity(intent)
    }

    private fun showReservationScreen() {
        val intent = Intent(this@NotificationActivity, ReservationActivity::class.java)
        startActivity(intent)
    }

    private fun showChatScreen(boardUid: Int) {
        val intent = Intent(this@NotificationActivity, ChatViewActivity::class.java)
        intent.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, boardUid)
        intent.putExtra(INTENT_DATA_CHAT_CLASS_DATA, "null")
        intent.putExtra(INTENT_DATA_CHAT_DATA, "null")
        startActivity(intent)
    }

    private fun showChangeLocationAndNotificationScreen(isChangeLocation: Boolean) {
        val intent: Intent = if (isChangeLocation) {
            Intent(this@NotificationActivity, ChangeLocationActivity::class.java)
        } else {
            Intent(this@NotificationActivity, NotificationActivity::class.java)
        }
        startActivity(intent)
    }

    private fun showPointHistoryScreen(companyUid: Int) {
        val intent = Intent(this@NotificationActivity, PointHistoryActivity::class.java)
        intent.putExtra(INTENT_DATA_COMPANY_UID_DATA, companyUid)
        startActivity(intent)
    }

    override fun onFinish() {
        finish()
    }

    override fun onNoteScreen() {
        showNoteScreen()
    }

    override fun onReservationScreen() {
        showReservationScreen()
    }

    override fun onChatScreen(boardUid: Int) {
        showChatScreen(boardUid)
    }

    override fun onChangeLocationAndNotificationScreen(isChangeLocation: Boolean) {
        showChangeLocationAndNotificationScreen(isChangeLocation)
    }

    override fun onPointHistoryScreen(companyUid: Int) {
        showPointHistoryScreen(companyUid)
    }

}