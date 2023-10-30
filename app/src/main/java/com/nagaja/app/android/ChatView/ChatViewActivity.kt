package com.nagaja.app.android.ChatView

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.ChangeLocation.ChangeLocationActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class ChatViewActivity : NagajaActivity(), ChatViewFragment.OnChatViewFragmentListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_view)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@ChatViewActivity)!!, false)

        val chatClass = intent.getStringExtra(INTENT_DATA_CHAT_CLASS_DATA)
        val chatData = intent.getStringExtra(INTENT_DATA_CHAT_DATA)
        val chatRoomNumber = intent.getIntExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, 0)

        if (!TextUtils.isEmpty(chatClass) && !TextUtils.isEmpty(chatData)) {
            init(chatClass!!, chatData!!, chatRoomNumber)
        }
    }

    private fun init(chatClass: String, chatData: String, roomNumber: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = ChatViewFragment::class.java.name
        val chatViewFragment: ChatViewFragment = ChatViewFragment.newInstance(chatClass, chatData, roomNumber)
        fragmentTransaction.add(R.id.activity_chat_view_container, chatViewFragment, tag).commit()
    }

    private fun showChangeLocationAndNotificationScreen(selectType: Int) {

        val intent: Intent

        when (selectType) {
            SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION -> {
                intent = Intent(this@ChatViewActivity, ChangeLocationActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_SEARCH -> {
                intent = Intent(this@ChatViewActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            SELECT_HEADER_MENU_TYPE_NOTIFICATION -> {
                intent = Intent(this@ChatViewActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onChangeLocationAndNotificationScreen(selectType: Int) {
        showChangeLocationAndNotificationScreen(selectType)
    }

    override fun onFinish() {
        finish()
    }

}