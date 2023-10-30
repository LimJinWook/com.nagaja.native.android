package com.nagaja.app.android.FCM

import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.volley.toolbox.Volley
import com.google.android.material.internal.ManufacturerUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nagaja.app.android.Base.NagajaActivity.Companion.FCM_TYPE_SERVER_CHECK
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_IS_PUSH_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_IS_SERVER_CHECK
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_PUSH_RESERVATE_COMPANY_UID_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_PUSH_RESERVATE_TYPE_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_PUSH_TOPIC_BOARD_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_PUSH_TOPIC_COMPANY_UID_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_PUSH_TYPE_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_PUSH_WEB_LINK_DATA
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_SERVER_CHECK_END_TIME
import com.nagaja.app.android.Base.NagajaActivity.Companion.INTENT_DATA_SERVER_CHECK_START_TIME
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_CHAT
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_EVENT
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_GO_TO_PLACE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_NOTE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_NOTICE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_POINT
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_REPORT_MISSING
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_RESERVATION
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_SERVER_CHECK
import com.nagaja.app.android.Data.PushData
import com.nagaja.app.android.Main.MainActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Splash.SplashActivity
import com.nagaja.app.android.Utils.*
import java.io.IOException
import java.net.URL
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val NOTIFICATION_CHANNEL_ID = "10001"

    private val TAG = "FirebaseMsgService"
    private var id = 1

    companion object {
        const val INTENT_FILTER = "INTENT_FILTER"
        const val REQUEST_CODE = 0x999
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.size > 0) {
            NagajaLog().d("wooks, Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            NagajaLog().d("wooks, Message Notification Body: " + remoteMessage.notification!!.body)
        }

        val context: Context = this@MyFirebaseMessagingService
        if (!SharedPreferencesUtil().getIsNotification(context)) {
            NagajaLog().d("wooks, Notification Setting False")
            return
        }

        if (remoteMessage.data["type"] != "server_check") {
            if (!SharedPreferencesUtil().getIsNotification(context)) {
                return
            }
        }

        if (remoteMessage.notification != null) { //포그라운드
            NagajaLog().e("wooks, FCM Foreground")
            notificationSomethingsForeground(remoteMessage)
        } else if (remoteMessage.data.isNotEmpty()) { //백그라운드
            if (isAppIsInBackground()) {
                NagajaLog().e("wooks, FCM Background")
                if (remoteMessage.data["type"] != FCM_TYPE_SERVER_CHECK) {
                    notificationSomethings(remoteMessage)
                }
            } else {
                NagajaLog().e("wooks, FCM Foreground")
                notificationSomethingsForeground(remoteMessage)
            }
        }

    }

    private fun sendNotification(item: Map<String, String>) {
        val title = item["title"]
        val body = item["body"]
        val code = item["image"]
        Log.d(TAG, "title : $title")
        Log.d(TAG, "body : $body")
        Log.d(TAG, "code : $code")
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("code", code)
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, id++ /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notificationBuilder = NotificationCompat.Builder(this, "PREFS_CHANNEL_ID")
            .setSmallIcon(getIcon())
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(100, 200, 100, 200))
            .setPriority(Notification.PRIORITY_MAX)
        notificationBuilder = notificationBuilder.setSound(defaultSoundUri)
        try {
            if (!TextUtils.isEmpty(code)) {
                val url = URL(code)
                //이미지 처리
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bigPicture)
                        .setBigContentTitle(title)
                        .setSummaryText(body)
                )
                val urlImage = URL(code)
                //아이콘 처리
                val bigIcon = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream())
                notificationBuilder.setLargeIcon(bigIcon)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id++, notificationBuilder.build())
    }

    /**
     * App Running Check
     */
    private fun isAppIsInBackground(): Boolean {
        var isInBackground = true
        val am = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == this.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == this.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }

    private fun notificationSomethings(remoteMessage: RemoteMessage) {

        if (remoteMessage.data["type"].equals(PUSH_TYPE_SERVER_CHECK)) {
                val value = remoteMessage.data["value"].toBoolean()
                var serverCheckStartTime = remoteMessage.data["server_check_start_date"]
                var serverCheckEndTime = remoteMessage.data["server_check_end_date"]
                val intent = Intent(INTENT_FILTER)
                intent.putExtra(INTENT_DATA_IS_SERVER_CHECK, value)
                if (value) {
                    if (null == serverCheckStartTime) {
                        serverCheckStartTime = ""
                    }

                    if (null == serverCheckEndTime) {
                        serverCheckEndTime = ""
                    }

                    intent.putExtra(INTENT_DATA_SERVER_CHECK_START_TIME, serverCheckStartTime)
                    intent.putExtra(INTENT_DATA_SERVER_CHECK_END_TIME, serverCheckEndTime)
                }
                sendBroadcast(intent)
            return
        }

        var intent: Intent? = null
        try {

            when (remoteMessage.data["type"]) {
//                PUSH_TYPE_CHAT,
                PUSH_TYPE_RESERVATION,
//                PUSH_TYPE_GO_TO_PLACE,
//                PUSH_TYPE_POINT,
                -> {

                    intent = Intent(this, SplashActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_RESERVATE_TYPE_DATA, remoteMessage.data["reservate_type"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_RESERVATE_COMPANY_UID_DATA, remoteMessage.data["compuid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_RESERVATION) {
                        pushData.setReservateType(remoteMessage.data["reservate_type"]!!.toInt())
                        pushData.setReservateCompanyUid(remoteMessage.data["compuid"]!!.toInt())
                    }
                }

                PUSH_TYPE_REPORT_MISSING -> {
                    intent = Intent(this, SplashActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, remoteMessage.data["cagouid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_REPORT_MISSING) {
                        pushData.setTopicCategoryUid(remoteMessage.data["cagouid"]!!.toInt())
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                    }
                }

                PUSH_TYPE_NOTE -> {
                    intent = Intent(this, SplashActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, remoteMessage.data["cagouid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_COMPANY_UID_DATA, remoteMessage.data["compuid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_NOTE) {
                        pushData.setTopicCategoryUid(remoteMessage.data["cagouid"]!!.toInt())
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                        pushData.setCompanyUid(remoteMessage.data["compuid"]!!.toInt())
                    }
                }

                PUSH_TYPE_NOTICE,
                PUSH_TYPE_EVENT -> {
                    intent = Intent(this, SplashActivity::class.java)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, remoteMessage.data["cagouid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_WEB_LINK_DATA, remoteMessage.data["webLink"])

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_NOTICE || remoteMessage.data["type"] == PUSH_TYPE_EVENT) {
                        pushData.setTopicCategoryUid(remoteMessage.data["cagouid"]!!.toInt())
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                        pushData.setWebLinkUrl(remoteMessage.data["webLink"]!!)
                    }
                }

                PUSH_TYPE_CHAT -> {
                    intent = Intent(this, SplashActivity::class.java)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_CHAT) {
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                    }
                }

                PUSH_TYPE_SERVER_CHECK -> {
                    val value = remoteMessage.data["value"].toBoolean()
                    var serverCheckStartTime = remoteMessage.data["server_check_start_date"]
                    var serverCheckEndTime = remoteMessage.data["server_check_end_date"]
                    val intent = Intent(INTENT_FILTER)
                    intent.putExtra(INTENT_DATA_IS_SERVER_CHECK, value)
                    if (value) {
                        if (null == serverCheckStartTime) {
                            serverCheckStartTime = ""
                        }

                        if (null == serverCheckEndTime) {
                            serverCheckEndTime = ""
                        }

                        intent.putExtra(INTENT_DATA_SERVER_CHECK_START_TIME, serverCheckStartTime)
                        intent.putExtra(INTENT_DATA_SERVER_CHECK_END_TIME, serverCheckEndTime)
                    }
                    sendBroadcast(intent)
                    return
                }
            }

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        if (null == intent) {
            return
        }

        // TODO:
        /*intent.putExtra(INTENT_DATA_PUSH_WEB_LINK_DATA, remoteMessage.data["webLink"])

        NagajaLog().d("wooks, remoteMessage.data[webLink] 123123= ${intent.getStringExtra(INTENT_DATA_PUSH_WEB_LINK_DATA)}")

        NagajaLog().d("wooks, remoteMessage.data[webLink] = ${remoteMessage.data["webLink"]}")
        if (!TextUtils.isEmpty(remoteMessage.data["webLink"])) {
            MAPP.PUSH_URL = remoteMessage.data["webLink"]
        }*/

        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "one-channel"
        val channelName = "My Channel One1"
        val channelDescription = "My Channel One Description"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 100, 200)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(getIcon()) //
//            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
            .setContentTitle(remoteMessage.data["title"])
            .setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.data["body"]))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(100, 200, 100, 200))
            .setPriority(NotificationCompat.PRIORITY_MAX)
        notificationBuilder = notificationBuilder.setSound(defaultSoundUri)

//        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        try {
            if (!TextUtils.isEmpty(remoteMessage.data["image"])) {
                val url = URL(remoteMessage.data["image"])
                //이미지 처리
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bigPicture)
                        .setBigContentTitle(remoteMessage.data["title"])
                        .setSummaryText(remoteMessage.data["body"])
                )
                val urlImage = URL(remoteMessage.data["image"])
                //아이콘 처리
                val bigIcon = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream())
                notificationBuilder.setLargeIcon(bigIcon)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(333 /* ID of notification */, notificationBuilder.build())
    }

    private fun notificationSomethingsForeground(remoteMessage: RemoteMessage) {

        var intent: Intent? = null
        try {

            when (remoteMessage.data["type"]) {
//                PUSH_TYPE_CHAT,
                PUSH_TYPE_RESERVATION,
//                PUSH_TYPE_GO_TO_PLACE,
//                PUSH_TYPE_POINT,
                -> {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_RESERVATE_TYPE_DATA, remoteMessage.data["reservate_type"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_RESERVATE_COMPANY_UID_DATA, remoteMessage.data["compuid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_RESERVATION) {
                        pushData.setReservateType(remoteMessage.data["reservate_type"]!!.toInt())
                        pushData.setReservateCompanyUid(remoteMessage.data["compuid"]!!.toInt())
                    }
//                    if (!TextUtils.isEmpty(remoteMessage.data["webLink"])) {
//                        pushData.setWebLinkUrl(remoteMessage.data["webLink"]!!)
//                    }

                    MAPP.PUSH_DATA = pushData
                }

                PUSH_TYPE_REPORT_MISSING -> {
                    intent = Intent(this, MainActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, remoteMessage.data["cagouid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_REPORT_MISSING) {
                        pushData.setTopicCategoryUid(remoteMessage.data["cagouid"]!!.toInt())
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                    }
                }

                PUSH_TYPE_NOTE -> {
                    intent = Intent(this, MainActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, remoteMessage.data["cagouid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_COMPANY_UID_DATA, remoteMessage.data["compuid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_NOTE) {
                        pushData.setTopicCategoryUid(remoteMessage.data["cagouid"]!!.toInt())
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                        pushData.setCompanyUid(remoteMessage.data["compuid"]!!.toInt())
                    }
                }

                PUSH_TYPE_NOTICE,
                PUSH_TYPE_EVENT -> {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, remoteMessage.data["cagouid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())
                    intent.putExtra(INTENT_DATA_PUSH_WEB_LINK_DATA, remoteMessage.data["webLink"])

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_NOTICE || remoteMessage.data["type"] == PUSH_TYPE_EVENT) {
                        pushData.setTopicCategoryUid(remoteMessage.data["cagouid"]!!.toInt())
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                        pushData.setWebLinkUrl(remoteMessage.data["webLink"]!!)
                    }
//                    if (!TextUtils.isEmpty(remoteMessage.data["webLink"])) {
//                        pushData.setWebLinkUrl(remoteMessage.data["webLink"]!!)
//                    }

                    MAPP.PUSH_DATA = pushData
                }

                PUSH_TYPE_CHAT -> {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
                    intent.putExtra(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, remoteMessage.data["boarduid"]!!.toInt())

                    val pushData = PushData()
                    pushData.setType(remoteMessage.data["type"]!!)
                    if (remoteMessage.data["type"] == PUSH_TYPE_CHAT) {
                        pushData.setTopicBoardUid(remoteMessage.data["boarduid"]!!.toInt())
                    }

                    MAPP.PUSH_DATA = pushData
                }

                PUSH_TYPE_SERVER_CHECK -> {
                    if (!isAppIsInBackground()) {
                        if (Util().getClassName(this)!!.contains("MainActivity") || Util().getClassName(this)!!.contains("ServerCheckActivity")) {
                            val value = remoteMessage.data["value"].toBoolean()
                            var serverCheckStartTime = remoteMessage.data["server_check_start_date"]
                            var serverCheckEndTime = remoteMessage.data["server_check_end_date"]
                            val intent = Intent(INTENT_FILTER)
                            intent.putExtra(INTENT_DATA_IS_SERVER_CHECK, value)
                            if (value) {
                                if (null == serverCheckStartTime) {
                                    serverCheckStartTime = ""
                                }

                                if (null == serverCheckEndTime) {
                                    serverCheckEndTime = ""
                                }

                                intent.putExtra(INTENT_DATA_SERVER_CHECK_START_TIME, serverCheckStartTime)
                                intent.putExtra(INTENT_DATA_SERVER_CHECK_END_TIME, serverCheckEndTime)
                            }
                            sendBroadcast(intent)
                            return
                        }
                    }
                }

//                else -> {
//                    intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra(INTENT_DATA_IS_PUSH_DATA, true)
//                    intent.putExtra(INTENT_DATA_PUSH_TYPE_DATA, remoteMessage.data["type"])
//                    intent.putExtra(INTENT_DATA_PUSH_RESERVATE_TYPE_DATA, remoteMessage.data["reservate_type"]!!.toInt())
//                    intent.putExtra(INTENT_DATA_PUSH_RESERVATE_COMPANY_UID_DATA, remoteMessage.data["compuid"]!!.toInt())
//
//                    val pushData = PushData()
//                    pushData.setType(remoteMessage.data["type"]!!)
//                    if (remoteMessage.data["type"] == PUSH_TYPE_RESERVATION) {
//                        pushData.setReservateType(remoteMessage.data["reservate_type"]!!.toInt())
//                        pushData.setReservateCompanyUid(remoteMessage.data["compuid"]!!.toInt())
//                    }
//                    if (!TextUtils.isEmpty(remoteMessage.data["webLink"])) {
//                        pushData.setWebLinkUrl(remoteMessage.data["webLink"]!!)
//                    }
//
//                    MAPP.PUSH_DATA = pushData
//                }
            }

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        if (null == intent) {
            return
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "one-channel"
        val channelName = "My Channel One1"
        val channelDescription = "My Channel One Description"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 100, 200)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(getIcon()) //                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
            .setContentTitle(remoteMessage.data["title"])
            .setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.data["body"]))
//            .setContentText(remoteMessage.data["body"])
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(100, 200, 100, 200))
            .setPriority(NotificationCompat.PRIORITY_MAX)
        notificationBuilder = notificationBuilder.setSound(defaultSoundUri)
        try {
            if (!TextUtils.isEmpty(remoteMessage.data["image"])) {
                val url = URL(remoteMessage.data["image"])
                //이미지 처리
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bigPicture)
                        .setBigContentTitle(remoteMessage.data["title"])
                        .setSummaryText(remoteMessage.data["body"])
                )
                val urlImage = URL(remoteMessage.data["image"])
                //아이콘 처리
                val bigIcon = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream())
                notificationBuilder.setLargeIcon(bigIcon)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(333 /* ID of notification */, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun getIcon(): Int {
        return if (ManufacturerUtils.isSamsungDevice()) {
            R.mipmap.ic_launcher_round
        } else {
            R.mipmap.ic_notification
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)

        if (!TextUtils.isEmpty(s)) {
            MAPP.USER_FCM_TOKEN = s

            val context: Context = this@MyFirebaseMessagingService
            if (SharedPreferencesUtil().getIsNewInstall(context)) {
                return
            }

            var location = ""
            location = if (null == SharedPreferencesUtil().getSaveLocation(context) || TextUtils.isEmpty(SharedPreferencesUtil().getSaveLocation(context))
                || SharedPreferencesUtil().getSaveLocation(context)!!.contains("null")) {
                "0,0"
            } else {
                SharedPreferencesUtil().getSaveLocation(context)!!
            }

            if (TextUtils.isEmpty(location) || location == "0,0") {
                when (SharedPreferencesUtil().getSelectLanguage(this)) {
                    NagajaFragment.SELECT_LANGUAGE_EN -> {
                        location = NagajaFragment.CURRENT_LOCATION_EN
                    }

                    NagajaFragment.SELECT_LANGUAGE_KO -> {
                        location = NagajaFragment.CURRENT_LOCATION_KO
                    }

                    NagajaFragment.SELECT_LANGUAGE_FIL -> {
                        location = NagajaFragment.CURRENT_LOCATION_FIL
                    }

                    NagajaFragment.SELECT_LANGUAGE_ZH -> {
                        location = NagajaFragment.CURRENT_LOCATION_ZH
                    }

                    NagajaFragment.SELECT_LANGUAGE_JA -> {
                        location = NagajaFragment.CURRENT_LOCATION_JA
                    }
                }
            }

            val mRequestQueue =  Volley.newRequestQueue(context)

            NagajaLog().d("wooks, onNewToken getUserDeviceInformation")
            val nagajaManager = NagajaManager().getInstance()
            nagajaManager?.getUserDeviceInformation(
                mRequestQueue,
                object : NagajaManager.RequestListener<String?> {
                    override fun onSuccess(resultData: String?) {
                    }

                    override fun onFail(errorMsg: String?) {
                    }

                    override fun onFail(errorCode: Int?) {
                    }

                },
                Util().getAndroidID(context),
                "ANDROID",
                Build.MODEL,
                context.packageManager.getPackageInfo(context.packageName, 0).versionName,
                 location,
                MAPP.USER_FCM_TOKEN,
                if (!TextUtils.isEmpty(SharedPreferencesUtil().getSelectLanguage(context))) SharedPreferencesUtil().getSelectLanguage(context)!!.lowercase(Locale.getDefault()) else ""
            )
        }
    }

}