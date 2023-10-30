package com.nagaja.app.android.Utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import com.google.firebase.messaging.FirebaseMessagingService
import com.nagaja.app.android.Adapter.BoardHorizontalAdapter.Companion.CATEGORY_TYPE_ALL
import com.nagaja.app.android.Adapter.BoardHorizontalAdapter.Companion.CATEGORY_TYPE_CATEGORY_1
import com.nagaja.app.android.Adapter.BoardHorizontalAdapter.Companion.CATEGORY_TYPE_CATEGORY_2
import com.nagaja.app.android.Adapter.BoardHorizontalAdapter.Companion.CATEGORY_TYPE_CATEGORY_3
import com.nagaja.app.android.Adapter.BoardHorizontalAdapter.Companion.CATEGORY_TYPE_FAMOUS_RESTAURANT
import com.nagaja.app.android.Adapter.BoardHorizontalAdapter.Companion.CATEGORY_TYPE_PLAYGROUND
import com.nagaja.app.android.Adapter.BoardHorizontalAdapter.Companion.CATEGORY_TYPE_TALK
import com.nagaja.app.android.Base.NagajaFragment.Companion.AMERICA_LA
import com.nagaja.app.android.Base.NagajaFragment.Companion.AMERICA_UID_CODE
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_FACILITIES
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_HOSPITAL
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_LEISURE_ATTRACTIONS
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_LODGING
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_MASSAGE_AESTHETICS
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_RENTAL_CAR_CHILDMINDER_GUIDE
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_RESTAURANT
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_TRAVEL_AGENCY
import com.nagaja.app.android.Base.NagajaFragment.Companion.KOREA_SEOUL
import com.nagaja.app.android.Base.NagajaFragment.Companion.KOREA_UID_CODE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHILIPPINES_MANILA
import com.nagaja.app.android.Base.NagajaFragment.Companion.PHILIPPINES_UID_CODE
import com.nagaja.app.android.R
import java.io.IOException
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class Util {

    /**
     * 이메일 포맷 체크
     *
     * @param email
     * @return
     */
    fun checkEmail(email: String?): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    /**
     * 공백 체크
     */
    var blankSpaceFilter = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (Character.isWhitespace(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }

    /**
     * 영문만 체크
     * */
    var inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
        val ps = Pattern.compile("^[a-z0-9]*$")
        if (!ps.matcher(source).matches()) {
            ""
        } else null
    }

    /**
     * 위도 경도 소수점 6자리
     * */
    fun getSixDecimalPlaces(number: Double): String {
        return String.format("%.6f", number)
    }

    /**
     * 소수점 2자리
     * */
    fun getTwoDecimalPlaces(number: Double): String {
        val decimal = DecimalFormat("#,###.##")
        return decimal.format(number).toString()
    }

    /**
     * 오늘 날짜
     * */
    @SuppressLint("SimpleDateFormat")
    fun getTodayDate(): String? {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.format(Date())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get Class Name
     */
    fun getClassName(context: Context): String? {
        val activityManager = context.getSystemService(FirebaseMessagingService.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfos = activityManager.getRunningTasks(1)
        NagajaLog().d("wooks, Get Class Name = " + runningTaskInfos[0].topActivity!!.className)
        return runningTaskInfos[0].topActivity!!.className
    }

    /**
     * Company Type
     * */
    fun getCompanyType(context: Context, type: Int): String {
        var typeName = ""
        when (type) {
            COMPANY_TYPE_HOSPITAL -> {
                typeName = context.resources.getString(R.string.text_company_type_hospital)
            }

            COMPANY_TYPE_FACILITIES -> {
                typeName = context.resources.getString(R.string.text_company_type_facilities)
            }

            COMPANY_TYPE_RENTAL_CAR_CHILDMINDER_GUIDE -> {
                typeName = context.resources.getString(R.string.text_company_type_rental_car_childminder_guide)
            }

            COMPANY_TYPE_TRAVEL_AGENCY -> {
                typeName = context.resources.getString(R.string.text_company_type_travel_agency)
            }

            COMPANY_TYPE_RESTAURANT -> {
                typeName = context.resources.getString(R.string.text_company_type_restaurant)
            }

            COMPANY_TYPE_LODGING -> {
                typeName = context.resources.getString(R.string.text_company_type_lodging)
            }

            COMPANY_TYPE_LEISURE_ATTRACTIONS -> {
                typeName = context.resources.getString(R.string.text_company_type_leisure_attractions)
            }

            COMPANY_TYPE_MASSAGE_AESTHETICS -> {
                typeName = context.resources.getString(R.string.text_company_type_massage_aesthetics)
            }

            else -> {
                typeName = context.resources.getString(R.string.text_etc)
            }
        }

        return typeName
    }

    @SuppressLint("HardwareIds")
    fun getAndroidID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * Password Regex Check
     * */
//    fun getPasswordRegexCheck(password: String?): Boolean {
//        val passwordPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$"
//    }


    /**
     * Time Zone ID Code
     * */
    fun getTimeZoneID(context: Context): String {

        var timeZoneID = ""
        when (SharedPreferencesUtil().getSaveNationCode(context)) {
            AMERICA_UID_CODE -> {
                timeZoneID = AMERICA_LA
            }

            KOREA_UID_CODE -> {
                timeZoneID = KOREA_SEOUL
            }

            PHILIPPINES_UID_CODE -> {
                timeZoneID = PHILIPPINES_MANILA
            }

            else -> {
                timeZoneID = PHILIPPINES_MANILA
            }
        }

        return timeZoneID
    }

    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getFilePath(context: Context, uri: Uri): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
                context.applicationContext,
                uri
            )
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(
                    split[1]
                )
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA
            )
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun setCategoryText(context: Context, categoryType: Int): String {

        var categoryValue = ""
        when (categoryType) {
            CATEGORY_TYPE_ALL -> {
                categoryValue = context.resources.getString(R.string.text_all)
            }

            CATEGORY_TYPE_PLAYGROUND -> {
                categoryValue = context.resources.getString(R.string.text_playground)
            }

            CATEGORY_TYPE_FAMOUS_RESTAURANT -> {
                categoryValue = context.resources.getString(R.string.text_recommend_good_restaurant)
            }

            CATEGORY_TYPE_TALK -> {
                categoryValue = context.resources.getString(R.string.text_talk_room)
            }

            CATEGORY_TYPE_CATEGORY_1 -> {
                categoryValue = "카테고리1"
            }

            CATEGORY_TYPE_CATEGORY_2 -> {
                categoryValue = "카테고리2"
            }

            CATEGORY_TYPE_CATEGORY_3 -> {
                categoryValue = "카테고리3"
            }
        }

        return categoryValue
    }

    /**
     * App Running Check
     */
    /*fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(FirebaseMessagingService.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }*/

    /**
     * Timestamp To String (Second)
     */
    @SuppressLint("SimpleDateFormat")
    fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    /**
     * Timestamp To String (Minute)
     */
    @SuppressLint("SimpleDateFormat")
    fun getDateTimeMinute(s: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")

            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateTimeDay(s: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy.MM.dd")

            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    /**
     * Get Class Name
     */
    /*fun getClassName(context: Context): String? {
        val activityManager = context.getSystemService(FirebaseMessagingService.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfos = activityManager.getRunningTasks(1)
        NagajaLog().d("wooks, Get Class Name = " + runningTaskInfos[0].topActivity!!.className)
        return runningTaskInfos[0].topActivity!!.className
    }*/

    /**
     * Photo Exif Rotation
     */
    fun getOrientationOfImage(filepath: String?): Int {
        var exif: ExifInterface? = null
        exif = try {
            ExifInterface(filepath!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        val orientation = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    @SuppressLint("Range")
    fun getPathFromUri(context: Context, uri: Uri?): String? {
        val cursor = context.contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndex("_data"))
        cursor.close()
        return path
    }

    @SuppressLint("Range")
    fun getPathToUri(context: Context, filePath: String): Uri? {
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data = '$filePath'", null, null)
        cursor!!.moveToNext()
        val id = cursor.getInt(cursor.getColumnIndex("_id"))
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
    }

    @Throws(java.lang.Exception::class)
    fun getRotatedBitmap(bitmap: Bitmap?, degrees: Int): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0) return bitmap
        val m = Matrix()
        m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    /**
     * Text Empty Check
     */
    fun getTextEmptyCheck(text: String): Boolean {
        if (TextUtils.isEmpty(text) || text == "null") {
            return true
        } else {
            return false
        }
    }

    fun convertToComma(value: Int): String {
        val myFormatter = DecimalFormat("###,###")
        return myFormatter.format(value)
    }

    /**
     * Url From File Name
     */
    fun getFileNameFromURL(url: String?): String? {
        if (url == null) {
            return ""
        }
        try {
            val resource = URL(url)
            val host: String = resource.getHost()
            if (host.length > 0 && url.endsWith(host)) {
                // handle ...example.com
                return ""
            }
        } catch (e: MalformedURLException) {
            return ""
        }
        val startIndex = url.lastIndexOf('/') + 1
        val length = url.length

        // find end index for ?
        var lastQMPos = url.lastIndexOf('?')
        if (lastQMPos == -1) {
            lastQMPos = length
        }

        // find end index for #
        var lastHashPos = url.lastIndexOf('#')
        if (lastHashPos == -1) {
            lastHashPos = length
        }

        // calculate the end index
        val endIndex = Math.min(lastQMPos, lastHashPos)
        return url.substring(startIndex, endIndex)
    }

    fun unitConvert(getNum: String): String? {
        val value = getNum.toLong()
        val symbols = DecimalFormatSymbols(Locale.US)
        val mdf = DecimalFormat("###,###,###", symbols)
        mdf.format(value)
        return mdf.format(value)
    }

    @Throws(Exception::class)
    fun passwordEncoding(param: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(param.toByteArray())
        val result = StringBuilder(bytes.size * 2)
        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }
        return result.toString().toLowerCase()
    }

    /**
     * Password Regular Expression
     * */
    fun passwordRegularExpressionCheck(password: String): Boolean {
        val regularPattern  = "^.*(?=^.{6,20}\$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#\$%^&+=]).*\$"
        val pattern = Pattern.compile(regularPattern)

        return pattern.matcher(password).find()
    }

    /**
     * Email Masking
     * */
    fun getEmailMasking(email: String): String {
        val result = StringBuffer()
        val regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pattern = Pattern.compile(regexEmail, Pattern.CASE_INSENSITIVE)
        if (!pattern.matcher(email).matches()) {
            return email
        }
        val nickNameSplit = email.split("@")
        if (nickNameSplit.size == 2) {
            if (nickNameSplit[0].length > 3) {
                result.append(nickNameSplit[0].substring(0, 3))
            } else {
                result.append(nickNameSplit[0])
            }
            result.append("***")
                .append("@")
                .append(nickNameSplit[1])
        }

        return result.toString()
    }

    /**
     * String To Time
     * */
    fun getStringToTime(date: String): String {
        var time = date
        time = time.replace("T", " ")

        val index: Int = time.indexOf(".")
        time = time.substring(0, index)

        return time.substring(0, time.length - 3)
    }

    /**
     * UTC To Date String
     * */
//    fun getUtcToDate(context: Context, date: String): String {
//        var time = date
//        val index: Int = time.indexOf("+")
//        time = time.substring(0, index)
//
//        val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
//        val zone = ZoneId.of(Util().getTimeZoneID(context))
//        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//
//        return outputFormatter.format(parsed.atZoneSameInstant(zone))
//    }

    /**
     * Expandable View
     */
    fun expand(v: View) {
        if (v.visibility == View.VISIBLE) return
        val durations: Long
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        durations = ((targetHeight / v.context.resources.displayMetrics.density)).toLong()

        v.alpha = 0.0F
        v.visibility = View.VISIBLE
        v.animate().alpha(1.0F).setDuration(durations).setListener(null)

        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = durations
        v.startAnimation(a)
    }

    /**
     * Collapse View
     */
    fun collapse(v: View) {
        if (v.visibility == View.GONE) return
        val durations: Long
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        durations = (initialHeight / v.context.resources.displayMetrics.density).toLong()

        v.alpha = 1.0F
        v.animate().alpha(0.0F).setDuration(durations).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                    v.alpha = 1.0F
                }
            })

        a.duration = durations
        v.startAnimation(a)
    }

    fun getCalculateNoOfColumns(context: Context, columnWidthDp: Float): Int {
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}