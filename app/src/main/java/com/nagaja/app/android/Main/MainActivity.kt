package com.nagaja.app.android.Main

import android.Manifest
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.decoder.ImageDecoderConfig.*
import com.nagaja.app.android.Adapter.ViewPagerAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_FAQ
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_FREE_BOARD
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_JOB_AND_JOB_SEARCH
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_MAIN
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_MAIN_BOARD
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_NEWS
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_NOTICE
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_PLAYGROUND
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_REPORT_DISAPPEARANCE
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_STORE_RECOMMEND
import com.nagaja.app.android.Base.NagajaFragment.Companion.COMPANY_TYPE_TALK_ROOM
import com.nagaja.app.android.Base.NagajaFragment.Companion.DIALOG_TYPE_NORMAL
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_CHAT
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_EVENT
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_NOTE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_NOTICE
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_REPORT_MISSING
import com.nagaja.app.android.Base.NagajaFragment.Companion.PUSH_TYPE_RESERVATION
import com.nagaja.app.android.Board.BoardActivity
import com.nagaja.app.android.BoardDetail.BoardDetailActivity
import com.nagaja.app.android.BoardWrite.BoardWriteActivity
import com.nagaja.app.android.ChatView.ChatViewActivity
import com.nagaja.app.android.CompanyReservation.CompanyReservationActivity
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.FAQ.FAQActivity
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Job.JobActivity
import com.nagaja.app.android.Login.LoginActivity
import com.nagaja.app.android.Missing.MissingActivity
import com.nagaja.app.android.News.NewsActivity
import com.nagaja.app.android.Note.NoteActivity
import com.nagaja.app.android.Notice.NoticeActivity
import com.nagaja.app.android.Notification.NotificationActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.RecommendStore.RecommendStoreActivity
import com.nagaja.app.android.Reservation.ReservationActivity
import com.nagaja.app.android.Search.SearchActivity
import com.nagaja.app.android.Splash.SplashFragment
import com.nagaja.app.android.StoreList.StoreListActivity
import com.nagaja.app.android.StoreReservation.StoreReservationActivity
import com.nagaja.app.android.TermsWebView.TermsWebViewActivity
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_footer.*
import kotlinx.android.synthetic.main.layout_header.*


class MainActivity : NagajaActivity() {

    companion object {
        const val SHOW_TYPE_HOME                = 0x00
        const val SHOW_TYPE_RESERVATION         = 0x01
        const val SHOW_TYPE_LOCATION            = 0x02
        const val SHOW_TYPE_CHAT                = 0x03
        const val SHOW_TYPE_MY_PAGER            = 0x04

        const val PERMISSION_REQUEST_CODE       = 0x01

        const val DYNAMIC_LINK_URL             = "https://nagajaapp.page.link/url?url="
        const val DYNAMIC_PREFIX               = "https://nagajaapp.page.link"
        const val SHARE_LINK_IMAGE_URL         = "https://s3.ap-northeast-2.amazonaws.com/s3.nagaja.com/nagaja/system/image/fileNameSetf6585ca5-b3b6-4b2f-ad38-eb170bd6995a.png"

        const val IOS_PACKAGE                  = "com.nagaja.app.ios.NAGAJA"
        const val IOS_APP_STORE_ID             = "6448978918"

        var mContext: Context? = null

        var mIsLoading = false
    }

    private lateinit var mHomeIconImageView: ImageView
    private lateinit var mReservationIconImageView: ImageView
    private lateinit var mReservationIconNewDotImageView: ImageView
    private lateinit var mMyLocationImageView: ImageView
    private lateinit var mChatImageView: ImageView
    private lateinit var mChatIconNewDotImageView: ImageView
    private lateinit var mMyPageImageView: ImageView
    private lateinit var mHeaderShareImageView: ImageView

    private lateinit var mHomeTextView: TextView
    private lateinit var mReservationTextView: TextView
    private lateinit var mMyLocationTextView: TextView
    private lateinit var mChatTextView: TextView
    private lateinit var mMyPageTextView: TextView
    private lateinit var mLocationTextView: TextView

    private lateinit var mViewPager: MainViewPager
    private lateinit var mViewPagerAdapter: ViewPagerAdapter

    private var mSelectViewType = 0
    private var mLinkCategoryUid = 0
    private var mLinkBoardUid = 0

    private var mIsInit = false
    private var mIsFinish = false
    var mIsFirst = true
    var mIsTabClick = false
    private var mIsStart = true

    private lateinit var mRequestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (MAPP.IS_START_APP) {
            selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@MainActivity)!!, true)
            MAPP.IS_START_APP = false
        }

        mContext = this@MainActivity

        Fresco.initialize(this@MainActivity)

        mViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        mRequestQueue = Volley.newRequestQueue(this@MainActivity)

        init()
    }

    private fun init() {

        // Header Location View
        val headerLocationView = layout_header_location_view
        headerLocationView.setOnClickListener {
            showView(SHOW_TYPE_LOCATION)
        }

        // Header Location Text View
        mLocationTextView = layout_header_location_text_view

        // Header Select Language Image View
        val selectLanguageImageView = layout_header_select_language_image_view
        selectLanguageImageView.setImageResource(getLanguageIcon())
        selectLanguageImageView.setOnClickListener {
            showSelectLanguageCustomDialog()
        }

        /*// Header Select Language Spinner
        mSelectLanguageSpinner = layout_header_select_language_spinner
        mSelectLanguageSpinner.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem("", iconRes = R.drawable.flag_korean_2),
//                    IconSpinnerItem(text = requireActivity().resources.getString(R.string.text_filipino), iconRes = R.drawable.flag_filipino_2),
                    IconSpinnerItem("", iconRes = R.drawable.flag_english_2),
                    IconSpinnerItem("", iconRes = R.drawable.flag_chinese_2),
                    IconSpinnerItem("", iconRes = R.drawable.flag_japanese_2)
                ))
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

            var selectLanguage = 0
            if (SharedPreferencesUtil().getSelectLanguage(this@MainActivity) == SELECT_LANGUAGE_KO) {
                selectLanguage = 0
            } else if (SharedPreferencesUtil().getSelectLanguage(this@MainActivity) == SELECT_LANGUAGE_EN) {
                selectLanguage = 1
            } else if (SharedPreferencesUtil().getSelectLanguage(this@MainActivity) == SELECT_LANGUAGE_ZH) {
                selectLanguage = 2
            } else if (SharedPreferencesUtil().getSelectLanguage(this@MainActivity) == SELECT_LANGUAGE_JA) {
                selectLanguage = 3
            }
            selectItemByIndex(selectLanguage)
            lifecycleOwner = this@MainActivity
        }
        mSelectLanguageSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<IconSpinnerItem?> { oldIndex, oldItem, newIndex, newItem ->

            when (newIndex) {
                0 -> {
                    SharedPreferencesUtil().setSelectLanguage(this@MainActivity, SELECT_LANGUAGE_KO)
                }

                1 -> {
                    SharedPreferencesUtil().setSelectLanguage(this@MainActivity, SELECT_LANGUAGE_EN)
                }

                2 -> {
                    SharedPreferencesUtil().setSelectLanguage(this@MainActivity, SELECT_LANGUAGE_ZH)
                }

                3 -> {
                    SharedPreferencesUtil().setSelectLanguage(this@MainActivity, SELECT_LANGUAGE_JA)
                }

                else -> {
                    SharedPreferencesUtil().setSelectLanguage(this@MainActivity, SELECT_LANGUAGE_EN)
                }
            }

            selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@MainActivity)!!)
        })*/

        // Header Search Image View
        val headerSearchImageView = layout_header_search_image_view
        headerSearchImageView.setOnSingleClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        // Header Share Image View
        mHeaderShareImageView = layout_header_clipboard_image_view
        mHeaderShareImageView.setOnSingleClickListener {

            var tabCategory = COMPANY_TYPE_MAIN
            when (mSelectViewType) {
                SHOW_TYPE_HOME,
                SHOW_TYPE_LOCATION,
                SHOW_TYPE_CHAT,
                SHOW_TYPE_MY_PAGER -> {
                    tabCategory = COMPANY_TYPE_MAIN
                }

                SHOW_TYPE_RESERVATION -> {
                    tabCategory = COMPANY_TYPE_MAIN_BOARD
                }
            }
            setShareUrl(tabCategory, COMPANY_TYPE_MAIN)
        }

        // Header Notification Image View
        val headerNotificationImageView = layout_header_notification_image_view
        headerNotificationImageView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                val intent = Intent(this@MainActivity, NotificationActivity::class.java)
                startActivity(intent)
            }
        }


        // Custom View Pager
        mViewPager = activity_main_custom_view_pager
        mViewPager.adapter = mViewPagerAdapter
        mViewPager.setPagingEnabled(false)
        mViewPager.offscreenPageLimit = 2
        //            mViewPager.setCurrentItem(2, false);
        mViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    showView(SHOW_TYPE_HOME)
                } else if (position == 1) {
                    showView(SHOW_TYPE_RESERVATION)
                } else if (position == 2) {
                    showView(SHOW_TYPE_LOCATION)
                } else if (position == 3) {
                    showView(SHOW_TYPE_CHAT)
                } else if (position == 4) {
                    showView(SHOW_TYPE_MY_PAGER)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        /**
         * Footer View
         * */
        // Home View
        val homeView = layout_footer_home_view
        homeView.setOnClickListener {
            showView(SHOW_TYPE_HOME)
        }

        // Home Icon Image View
        mHomeIconImageView = layout_footer_home_image_view

        // Home Text View
        mHomeTextView = layout_footer_home_text_view

        // Reservation View
        val reservationView = layout_footer_reservation_view
        reservationView.setOnClickListener {
            showView(SHOW_TYPE_RESERVATION)
        }

        // Reservation Icon Image View
        mReservationIconImageView = layout_footer_reservation_image_view

        // Reservation Icon New Dot Image View
        mReservationIconNewDotImageView = layout_footer_reservation_new_dot_image_view

        // Reservation Text View
        mReservationTextView = layout_footer_reservation_text_view

        // My Location View
        val myLocationView = layout_footer_my_location_view
        myLocationView.setOnClickListener {
            showView(SHOW_TYPE_LOCATION)
        }

        // My Location Icon Image View
        mMyLocationImageView = layout_footer_my_location_image_view

        // My Location Text View
        mMyLocationTextView = layout_footer_my_location_text_view

        // Chat View
        val chatView = layout_footer_chat_view
        chatView.setOnClickListener {
            showView(SHOW_TYPE_CHAT)
        }

        // Chat Image View
        mChatImageView = layout_footer_chat_image_view

        // Chat Icon New Dot Image View
        mChatIconNewDotImageView = layout_footer_chat_new_dot_image_view

        // Chat Text View
        mChatTextView = layout_footer_chat_text_view

        // My Page View
        val myPage = layout_footer_my_page_view
        myPage.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
//                val intent = Intent(this@MainActivity, LoginActivity::class.java)
//                intent.putExtra(INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_DATA, true)
//                startActivityForResult(intent, INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_REQUEST_CODE)
                showDefaultLoginScreen()
            } else {
                showView(SHOW_TYPE_MY_PAGER)
            }
        }

        // My Page Icon Image View
        mMyPageImageView = layout_footer_my_page_image_view

        // My Page Text View
        mMyPageTextView = layout_footer_my_page_text_view
        if (MAPP.IS_NON_MEMBER_SERVICE) {
            mMyPageTextView.text = this@MainActivity.resources.getString(R.string.text_login)
        }

//        setLocationDisplay()

//        if (!TextUtils.isEmpty(SharedPreferencesUtil().getLocationName(this@MainActivity))) {
//            displayCurrentLocation(SharedPreferencesUtil().getLocationName(this@MainActivity)!!)
//        } else {
//            getDefaultSettingArea()
//        }


//        getDefaultSettingArea()


        if (!MAPP.IS_NON_MEMBER_SERVICE) {
            getCompanyMemberData()
        } else {
            if (null != MAPP.PUSH_DATA) {
                if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_NOTICE || MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_EVENT
                    || MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_REPORT_MISSING) {

                    val categoryUid = MAPP.PUSH_DATA!!.getTopicCategoryUid()
                    val boardUid = MAPP.PUSH_DATA!!.getTopicBoardUid()

                    NagajaLog().e("wooks, categoryUid = $categoryUid")
                    NagajaLog().e("wooks, boardUid = $boardUid")

                    if (categoryUid > 0 && boardUid > 0) {
                        val mainMenuItemData = MainMenuItemData()
                        mainMenuItemData.setCategoryUid(categoryUid)
                        mainMenuItemData.setShareBoardUid(boardUid)

                        val intent: Intent
                        if (categoryUid == COMPANY_TYPE_NOTICE || categoryUid == 247 || categoryUid == 248) {
                            intent = Intent(this@MainActivity, NoticeActivity::class.java)
                            intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                            startActivity(intent)
                        } else if (categoryUid == COMPANY_TYPE_REPORT_DISAPPEARANCE) {
                            intent = Intent(this@MainActivity, MissingActivity::class.java)
                            intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                            startActivity(intent)
                        }
                    }
                }
            }

            NagajaLog().e("wooks, MAPP.PUSH_DATA = 1111")
            MAPP.PUSH_DATA = null
        }

        showView(SHOW_TYPE_HOME)

//        if (SharedPreferencesUtil().getSaveNationCode(this@MainActivity) > 0) {
//            getLocationArea()
//        }

        mIsInit = true

    }

    fun showDefaultLoginScreen() {
        defaultLoginScreen()
    }

    fun showView(menuType: Int) {

        when (menuType) {
            SHOW_TYPE_HOME -> {
                mHomeIconImageView.setImageResource(R.drawable.icon_home_enable)
                mHomeTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_0d4d97))

                mReservationIconImageView.setImageResource(R.drawable.icon_reservation_disable)
                mReservationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyLocationImageView.setImageResource(R.drawable.icon_my_location_disable)
                mMyLocationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mChatImageView.setImageResource(R.drawable.icon_chat_disable)
                mChatTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyPageImageView.setImageResource(R.drawable.icon_my_page_disable)
                mMyPageTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mHeaderShareImageView.visibility = View.VISIBLE
            }

            SHOW_TYPE_RESERVATION -> {
                mHomeIconImageView.setImageResource(R.drawable.icon_home_disable)
                mHomeTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mReservationIconImageView.setImageResource(R.drawable.icon_reservation_enable)
                mReservationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_0d4d97))

                mMyLocationImageView.setImageResource(R.drawable.icon_my_location_disable)
                mMyLocationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mChatImageView.setImageResource(R.drawable.icon_chat_disable)
                mChatTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyPageImageView.setImageResource(R.drawable.icon_my_page_disable)
                mMyPageTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

//                if (!mIsTabClick) {
//                    val mainBoardFragment: MainBoardFragment = mViewPager.adapter?.instantiateItem(mViewPager, 1) as MainBoardFragment
//                    mainBoardFragment.test()
//
//                    mIsTabClick = true
//                }

                if (!mIsLoading) {
                    val mainBoardFragment: MainBoardFragment = mViewPager.adapter?.instantiateItem(mViewPager, 1) as MainBoardFragment
                    mainBoardFragment.test()
                }

                mHeaderShareImageView.visibility = View.VISIBLE


//                val reservationFragment: ReservationFragment = mViewPager.adapter?.instantiateItem(mViewPager, 1 ) as ReservationFragment
//                reservationFragment.getReservationList(true, "", "")
//                reservationFragment.getStoreCategoryData(true)
            }

            SHOW_TYPE_LOCATION -> {
                mHomeIconImageView.setImageResource(R.drawable.icon_home_disable)
                mHomeTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mReservationIconImageView.setImageResource(R.drawable.icon_reservation_disable)
                mReservationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyLocationImageView.setImageResource(R.drawable.icon_my_location_enable)
                mMyLocationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_0d4d97))

                mChatImageView.setImageResource(R.drawable.icon_chat_disable)
                mChatTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyPageImageView.setImageResource(R.drawable.icon_my_page_disable)
                mMyPageTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mHeaderShareImageView.visibility = View.GONE

            }

            SHOW_TYPE_CHAT -> {

                if (MAPP.IS_NON_MEMBER_SERVICE) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.putExtra(INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_DATA, true)
                    startActivity(intent)

                    return
                }

                mHomeIconImageView.setImageResource(R.drawable.icon_home_disable)
                mHomeTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mReservationIconImageView.setImageResource(R.drawable.icon_reservation_disable)
                mReservationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyLocationImageView.setImageResource(R.drawable.icon_my_location_disable)
                mMyLocationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mChatImageView.setImageResource(R.drawable.icon_chat_enable)
                mChatTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_0d4d97))

                mMyPageImageView.setImageResource(R.drawable.icon_my_page_disable)
                mMyPageTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mHeaderShareImageView.visibility = View.GONE

            }

            SHOW_TYPE_MY_PAGER -> {
                mHomeIconImageView.setImageResource(R.drawable.icon_home_disable)
                mHomeTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mReservationIconImageView.setImageResource(R.drawable.icon_reservation_disable)
                mReservationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyLocationImageView.setImageResource(R.drawable.icon_my_location_disable)
                mMyLocationTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mChatImageView.setImageResource(R.drawable.icon_chat_disable)
                mChatTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_c4c4c4))

                mMyPageImageView.setImageResource(R.drawable.icon_my_page_enable)
                mMyPageTextView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_0d4d97))

                mHeaderShareImageView.visibility = View.GONE

            }
        }

        mViewPager.setCurrentItem(menuType, false)
        mSelectViewType = menuType

        checkPermission()
    }

    fun displayCurrentLocation(locationName: String) {
        NagajaLog().e("wooks, Display Setting Name = $locationName")
        MAPP.SELECT_NATION_NAME = locationName
        mLocationTextView.text = locationName
    }

    fun showBoardWriteScreen(categoryUid: Int) {
        val intent = Intent(this@MainActivity, BoardWriteActivity::class.java)
        intent.putExtra(INTENT_DATA_BOARD_CATEGORY_DATA, categoryUid)
        startActivityForResult(intent, INTENT_BOARD_WRITE_REQUEST_CODE)
    }

    fun showBoardDetailScreen(boardUid: Int, selectBoardType: Int, position: Int) {
        val intent = Intent(this@MainActivity, BoardDetailActivity::class.java)
        intent.putExtra(INTENT_DATA_BOARD_UID_DATA, boardUid)
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA, selectBoardType)
        intent.putExtra(INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA, position)
        startActivityForResult(intent, INTENT_BOARD_REQUEST_CODE)
    }

    private fun checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    PERMISSION_REQUEST_CODE
                )
            } else {
            }
        } else{
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        /*Manifest.permission.WRITE_EXTERNAL_STORAGE,*/ Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA
                    ),
                    PERMISSION_REQUEST_CODE
                )
            } else {
            }
        }
    }

    /**
    * Permission Result
    */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED) {

                } else {
//                    mListener.onFinish()
                }
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                } else {
//                    mListener.onFinish()
                }
            }
        }

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED
                && grantResults[4] == PackageManager.PERMISSION_GRANTED) {

//                mListener.onLoginScreen()
                if (Build.VERSION.SDK_INT >= 32) {
                    createNotificationChannel()
                }
            }/* else {
                mListener.onFinish()
            }*/
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel One1"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = "one-channel"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = "channel description"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showLoginGuideCustomDialog() {
        val customDialog = CustomDialog(
            this@MainActivity,
            DIALOG_TYPE_NORMAL,
            this@MainActivity.resources.getString(R.string.text_noti),
            this@MainActivity.resources.getString(R.string.text_message_login_guide),
            this@MainActivity.resources.getString(R.string.text_cancel),
            this@MainActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra(INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_DATA, true)
                startActivity(intent)
            }
        })
        customDialog.show()
    }

    fun getLocation(): String {
        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                SplashFragment.PERMISSION_REQUEST_CODE
            )

            return ""
        } else {
            val gpsTracker = GpsTracker(this@MainActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

//            SharedPreferencesUtil().setSaveLocation(this@MainActivity, "$latitude,$longitude")

            NagajaLog().e("wooks, latitude = $latitude / longitude = $longitude")
            return "$latitude,$longitude"
        }
    }

    fun showStoreListScreen(data: MainMenuItemData) {
        val intent: Intent

        if (data.getCategoryUid() == COMPANY_TYPE_NEWS) {
            intent = Intent(this@MainActivity, NewsActivity::class.java)
        } else if (data.getCategoryUid() == COMPANY_TYPE_FAQ) {
            intent = Intent(this@MainActivity, FAQActivity::class.java)
        } else if (data.getCategoryUid() == COMPANY_TYPE_STORE_RECOMMEND) {
            intent = Intent(this@MainActivity, RecommendStoreActivity::class.java)
        } else if (data.getCategoryUid() == COMPANY_TYPE_NOTICE) {
            intent = Intent(this@MainActivity, NoticeActivity::class.java)
        } else if (data.getCategoryUid() == COMPANY_TYPE_REPORT_DISAPPEARANCE) {
            intent = Intent(this@MainActivity, MissingActivity::class.java)
        } else if (data.getCategoryUid() == COMPANY_TYPE_JOB_AND_JOB_SEARCH) {
            intent = Intent(this@MainActivity, JobActivity::class.java)
        } else if (data.getCategoryUid() == COMPANY_TYPE_FREE_BOARD || data.getCategoryUid() == COMPANY_TYPE_TALK_ROOM) {
            intent = Intent(this@MainActivity, BoardActivity::class.java)
        } else if (data.getCategoryUid() == COMPANY_TYPE_PLAYGROUND) {
            data.setCategoryUid(COMPANY_TYPE_TALK_ROOM)
            intent = Intent(this@MainActivity, BoardActivity::class.java)

//            intent = Intent(this@MainActivity, PlaygroundActivity::class.java)
        } else {
            intent = Intent(this@MainActivity, StoreListActivity::class.java)
        }

        intent.putExtra(INTENT_DATA_STORE_LIST_DATA, data)
        startActivityForResult(intent, INTENT_SHOW_LOCATION_SCREEN_REQUEST_CODE)
    }

    fun successLogin() {
        if (!MAPP.IS_NON_MEMBER_SERVICE) {
            mMyPageTextView.text = this@MainActivity.resources.getText(R.string.text_my_page)
        }
    }

    fun showTermsAndPolicyScreen(selectType: Int, url: String) {
        val intent = Intent(this, TermsWebViewActivity::class.java)
        intent.putExtra(INTENT_DATA_SELECT_TERMS_TYPE_DATA, selectType)
        intent.putExtra(INTENT_DATA_SELECT_TERMS_URL_DATA, url)
        startActivity(intent)
    }

    override fun onBackPressed() {

        val frag: Fragment = mViewPagerAdapter.instantiateItem(mViewPager, SHOW_TYPE_LOCATION) as Fragment
        if (frag is LocationFragment) {
            if (frag.spinnerCheck() && mSelectViewType == SHOW_TYPE_LOCATION) {
                return
            }
        }

        if (mSelectViewType == SHOW_TYPE_RESERVATION || mSelectViewType == SHOW_TYPE_LOCATION || mSelectViewType == SHOW_TYPE_CHAT || mSelectViewType == SHOW_TYPE_MY_PAGER ) {
            showView(SHOW_TYPE_HOME)
            return
        }

        if (mIsFinish) {
            val customDialog = CustomDialog(
                this@MainActivity,
                DIALOG_TYPE_NORMAL,
                this@MainActivity.resources.getString(R.string.text_app_finish),
                this@MainActivity.resources.getString(R.string.text_message_finish_app),
                this@MainActivity.resources.getString(R.string.text_cancel),
                this@MainActivity.resources.getString(R.string.text_confirm)
            )

            customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
                override fun onCancel() {
                    customDialog.dismiss()
                }

                override fun onConfirm() {
                    customDialog.dismiss()
                    finish()
                }
            })
            customDialog.show()

            return
        }
        this.mIsFinish = true
        Handler(Looper.getMainLooper()).postDelayed({
            mIsFinish = false
        }, 2000)
    }

//    private fun selectLanguage(selectLanguage: String) {
//
//        var locale: Locale? = null
//        var language = ""
//
//        if (selectLanguage.equals(SELECT_LANGUAGE_EN, ignoreCase = true)) {
//            language = SELECT_LANGUAGE_EN
//            locale = Locale.ENGLISH
//            SharedPreferencesUtil().setNationPhoneCode(this@MainActivity, "1")
//            MAPP.SELECT_LANGUAGE_CODE = "1"
//        } else if (selectLanguage.equals(SELECT_LANGUAGE_KO, ignoreCase = true)) {
//            language = SELECT_LANGUAGE_KO
//            locale = Locale.KOREA
//            SharedPreferencesUtil().setNationPhoneCode(this@MainActivity, "82")
//            MAPP.SELECT_LANGUAGE_CODE = "82"
//        } else if (selectLanguage.equals(SELECT_LANGUAGE_FIL, ignoreCase = true)) {
//            language = SELECT_LANGUAGE_FIL
//            locale = Locale("fil", "PH")
//            SharedPreferencesUtil().setNationPhoneCode(this@MainActivity, "63")
//            MAPP.SELECT_LANGUAGE_CODE = "63"
//        } else if (selectLanguage.equals(SELECT_LANGUAGE_ZH, ignoreCase = true)) {
//            language = SELECT_LANGUAGE_ZH
//            locale = Locale.CHINA
//            SharedPreferencesUtil().setNationPhoneCode(this@MainActivity, "86")
//            MAPP.SELECT_LANGUAGE_CODE = "86"
//        } else if (selectLanguage.equals(SELECT_LANGUAGE_JA, ignoreCase = true)) {
//            language = SELECT_LANGUAGE_JA
//            locale = Locale.JAPAN
//            SharedPreferencesUtil().setNationPhoneCode(this@MainActivity, "81")
//            MAPP.SELECT_LANGUAGE_CODE = "81"
//        } else {
//            language = SELECT_LANGUAGE_EN
//            locale = Locale.ENGLISH
//            SharedPreferencesUtil().setNationPhoneCode(this@MainActivity, "1")
//            MAPP.SELECT_LANGUAGE_CODE = "1"
//        }
//
//
//        val cfg = Configuration()
//        cfg.locale = Locale(language)
//        cfg.setLocale(locale)
//
//        val intent = Intent(this@MainActivity, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(intent)
//
//        this@MainActivity.resources.updateConfiguration(cfg, null)
//        finish()
//        this@MainActivity.overridePendingTransition(0, 0)
//    }

    private fun getDynamicLinkCheck() {
        if (!TextUtils.isEmpty(MAPP.DYNAMIC_LINK_URL)) {
            val shareCategoryUid = MAPP.DYNAMIC_LINK_URL!!.substring(0, MAPP.DYNAMIC_LINK_URL!!.indexOf("/")).toInt()
            val shareBoardUid = MAPP.DYNAMIC_LINK_URL!!.substring(MAPP.DYNAMIC_LINK_URL!!.indexOf("/") + 1,  MAPP.DYNAMIC_LINK_URL!!.length).toInt()

            NagajaLog().e("wooks, shareCategoryUid = $shareCategoryUid")
            NagajaLog().e("wooks, shareBoardUid = $shareBoardUid")

            if (shareCategoryUid > 0 && shareBoardUid > 0) {
                val mainMenuItemData = MainMenuItemData()
                mainMenuItemData.setCategoryUid(shareCategoryUid)
                mainMenuItemData.setShareBoardUid(shareBoardUid)

                val intent: Intent
                if (shareCategoryUid == COMPANY_TYPE_NEWS) {
                    intent = Intent(this@MainActivity, NewsActivity::class.java)
                }/* else if (shareCategoryUid == COMPANY_TYPE_USED_MARKET) {
                    intent = Intent(this@MainActivity, Used::class.java)
                }*/ else if (shareCategoryUid == COMPANY_TYPE_FAQ) {
                    intent = Intent(this@MainActivity, FAQActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_STORE_RECOMMEND) {
                    intent = Intent(this@MainActivity, RecommendStoreActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_NOTICE) {
                    intent = Intent(this@MainActivity, NoticeActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_REPORT_DISAPPEARANCE) {
                    intent = Intent(this@MainActivity, MissingActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_JOB_AND_JOB_SEARCH) {
                    intent = Intent(this@MainActivity, JobActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_FREE_BOARD || shareCategoryUid == COMPANY_TYPE_TALK_ROOM) {
                    intent = Intent(this@MainActivity, BoardActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_PLAYGROUND) {
                    intent = Intent(this@MainActivity, BoardActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_MAIN) {
                    intent = Intent(this@MainActivity, MainActivity::class.java)
                } else if (shareCategoryUid == COMPANY_TYPE_MAIN_BOARD) {
                    showView(SHOW_TYPE_RESERVATION)
                    return
                } else {
                    intent = Intent(this@MainActivity, StoreListActivity::class.java)
                }

                if (shareCategoryUid != COMPANY_TYPE_MAIN) {
                    intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                    startActivityForResult(intent, INTENT_SHOW_LOCATION_SCREEN_REQUEST_CODE)
                }
            }
        }

        MAPP.DYNAMIC_LINK_URL = ""
    }

    /*private fun setShareUrl() {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
        .setLink(Uri.parse(DYNAMIC_LINK_URL + "26" + "/" + "930"))
            .setLink(Uri.parse("$DYNAMIC_LINK_URL$COMPANY_TYPE_MAIN/999"))
            .setDomainUriPrefix(DYNAMIC_PREFIX)
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID)
                    .setFallbackUrl(Uri.parse("https://www.nagaja.com"))
                    .setMinimumVersion(1)
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder(IOS_PACKAGE)
                    .setAppStoreId(IOS_APP_STORE_ID)
                    .setMinimumVersion("1.1.1")
                    .setFallbackUrl(Uri.parse("https://www.nagaja.com"))
                    .build()
            )
            .setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle(this@MainActivity.resources.getString(R.string.app_name))
                    .setDescription(this@MainActivity.resources.getString(R.string.text_share_desc))
                    .setImageUrl(Uri.parse(SHARE_LINK_IMAGE_URL))
                    .build())
            .buildShortDynamicLink()
            .addOnSuccessListener {
                val shortLink = it.shortLink!!
                NagajaLog().d("wooks, ShareUrl shortLink = $shortLink")
                createShareSheet(shortLink)

            }
            .addOnFailureListener {
                it.toString()
                NagajaLog().d("wooks, e : ${it.toString()}")
            }
    }

    private fun createShareSheet(dynamicLink: Uri){
        NagajaLog().d("wooks, dynamicLink = $dynamicLink")
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, dynamicLink.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }*/

    private fun getNowUseActivity(): String? {
        var returnActivityName: String? = ""
        try {
            val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            var className = ""
            className =
                manager.appTasks[0].taskInfo.topActivity!!.className
            returnActivityName = className
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return returnActivityName
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        try {
            NagajaLog().e("wooks, intent!!.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) = ${intent!!.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA)}")

            if (MAPP.IS_NON_MEMBER_SERVICE) {
                if (intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_RESERVATION
                    || intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_CHAT
                    || intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_NOTE ) {

                    return
                }
            }

            if (!TextUtils.isEmpty(intent!!.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA))) {
                if (intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_RESERVATION) {
                    if (intent.extras!!.getInt(INTENT_DATA_PUSH_RESERVATE_TYPE_DATA) == 0) {
                        // 예약자
                        NagajaLog().e("wooks, getNowUseActivity() = ${getNowUseActivity()}")
                        if (!TextUtils.isEmpty(getNowUseActivity())) {
                            if (getNowUseActivity()!!.contains("ReservationActivity")) {
                                return
                            } else {
                                val intent1 = Intent(this@MainActivity, ReservationActivity::class.java)
                                startActivity(intent1)
                            }
                        }
                    } else if (intent.extras!!.getInt(INTENT_DATA_PUSH_RESERVATE_TYPE_DATA) == 1) {
                        if (!TextUtils.isEmpty(getNowUseActivity())) {
                            if (getNowUseActivity()!!.contains("CompanyReservationActivity")) {
                                return
                            } else {
                                getCompanyDefaultData(intent.extras!!.getInt(INTENT_DATA_PUSH_RESERVATE_COMPANY_UID_DATA), false, -1)
                            }
                        }
                    }
                } else if (intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_NOTICE
                    || intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_EVENT
                    || intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_REPORT_MISSING ) {

                    val categoryUid = intent.extras!!.getInt(INTENT_DATA_PUSH_TOPIC_CATEGORY_DATA, 0)
                    val boardUid = intent.extras!!.getInt(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, 0)

                    NagajaLog().e("wooks, categoryUid = $categoryUid")
                    NagajaLog().e("wooks, boardUid = $boardUid")

                    if (categoryUid > 0 && boardUid > 0) {
                        val mainMenuItemData = MainMenuItemData()
                        mainMenuItemData.setCategoryUid(categoryUid)
                        mainMenuItemData.setShareBoardUid(boardUid)

                        val intent: Intent
                        if (categoryUid == COMPANY_TYPE_NOTICE || categoryUid == 247 || categoryUid == 248) {
                            intent = Intent(this@MainActivity, NoticeActivity::class.java)
                            intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                            startActivity(intent)
                        } else if (categoryUid == COMPANY_TYPE_REPORT_DISAPPEARANCE) {
                            intent = Intent(this@MainActivity, MissingActivity::class.java)
                            intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                            startActivity(intent)
                        }
                    }
                } else if (intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_NOTE) {
                    val companyUid = intent.extras!!.getInt(INTENT_DATA_PUSH_TOPIC_COMPANY_UID_DATA, 0)
                    val boardUid = intent.extras!!.getInt(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, 0)

                    if (companyUid > 0) {
                        getCompanyDefaultData(companyUid, true, boardUid)
                    } else {
                        val intentNote = Intent(this@MainActivity, NoteActivity::class.java)
                        intentNote.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, boardUid)
                        startActivity(intentNote)
                    }
                } else if (intent.extras!!.getString(INTENT_DATA_PUSH_TYPE_DATA) == PUSH_TYPE_CHAT) {
                    val boardUid = intent.extras!!.getInt(INTENT_DATA_PUSH_TOPIC_BOARD_DATA, 0)

                    if (boardUid > 0) {
                        val intentNote = Intent(this@MainActivity, ChatViewActivity::class.java)
                        intentNote.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, boardUid)
                        intentNote.putExtra(INTENT_DATA_CHAT_CLASS_DATA, "null")
                        intentNote.putExtra(INTENT_DATA_CHAT_DATA, "null")
                        startActivity(intentNote)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

            if (null != MAPP.PUSH_DATA) {
                try {
                    if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_RESERVATION) {
                        if (MAPP.PUSH_DATA!!.getReservateType() == 0) {
                            // 예약자
                            val intent = Intent(this@MainActivity, ReservationActivity::class.java)
                            startActivity(intent)
//                                    showToast(this@MainActivity, "000000")
                        } else if (MAPP.PUSH_DATA!!.getReservateType() == 1) {
                            getCompanyDefaultData(MAPP.PUSH_DATA!!.getReservateCompanyUid(), false, -1)
                        }
                    } else if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_NOTICE || MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_EVENT
                        || MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_REPORT_MISSING) {

                        val categoryUid = MAPP.PUSH_DATA!!.getTopicCategoryUid()
                        val boardUid = MAPP.PUSH_DATA!!.getTopicBoardUid()

                        NagajaLog().e("wooks, categoryUid = $categoryUid")
                        NagajaLog().e("wooks, boardUid = $boardUid")

                        if (categoryUid > 0 && boardUid > 0) {
                            val mainMenuItemData = MainMenuItemData()
                            mainMenuItemData.setCategoryUid(categoryUid)
                            mainMenuItemData.setShareBoardUid(boardUid)

                            val intent: Intent
                            if (categoryUid == COMPANY_TYPE_NOTICE || categoryUid == 247 || categoryUid == 248) {
                                intent = Intent(this@MainActivity, NoticeActivity::class.java)
                                intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                                startActivity(intent)
                            } else if (categoryUid == COMPANY_TYPE_REPORT_DISAPPEARANCE) {
                                intent = Intent(this@MainActivity, MissingActivity::class.java)
                                intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                                startActivity(intent)
                            }
                        }
                    } else if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_NOTE) {
                        val companyUid = MAPP.PUSH_DATA!!.getCompanyUid()
                        val boardUid = MAPP.PUSH_DATA!!.getTopicBoardUid()

                        if (companyUid > 0) {
                            getCompanyDefaultData(companyUid, true, boardUid)
                        } else {
                            val intentNote = Intent(this@MainActivity, NoteActivity::class.java)
                            intentNote.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, boardUid)
                            startActivity(intentNote)
                        }
                    } else if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_CHAT) {
                        val boardUid = MAPP.PUSH_DATA!!.getTopicBoardUid()

                        if (boardUid > 0) {
                            val intentNote = Intent(this@MainActivity, ChatViewActivity::class.java)
                            intentNote.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, boardUid)
                            intentNote.putExtra(INTENT_DATA_CHAT_CLASS_DATA, "null")
                            intentNote.putExtra(INTENT_DATA_CHAT_DATA, "null")
                            startActivity(intentNote)
                        }
                    }

                    NagajaLog().e("wooks, MAPP.PUSH_DATA = 3333")
                    MAPP.PUSH_DATA = null
                } catch (e: Exception) {
                    e.printStackTrace()
                    NagajaLog().e("wooks, MAPP.PUSH_DATA = 4444")
                    MAPP.PUSH_DATA = null
                }
            } else {
                NagajaLog().e("wooks, MAPP.PUSH_DATA = null")
            }
        }

        NagajaLog().e("wooks, MAPP.PUSH_DATA = 2222")
        MAPP.PUSH_DATA = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                INTENT_SHOW_LOCATION_SCREEN_REQUEST_CODE -> {
                    showView(SHOW_TYPE_LOCATION)
                }

                INTENT_BOARD_REQUEST_CODE -> {

                    val mainBoardFragment: MainBoardFragment = mViewPager.adapter?.instantiateItem(mViewPager, mViewPager.currentItem) as MainBoardFragment

                    if (data!!.getBooleanExtra(INTENT_DATA_BOARD_MODIFY_DATA, false)) {
                        mainBoardFragment.setRefresh()
                    } else if (data.getBooleanExtra(INTENT_DATA_BOARD_LIKE_BOOKMARK_CHANGE_DATA, false)) {
                        mainBoardFragment.changeClickData(
                            data.getBooleanExtra(INTENT_DATA_BOARD_MODIFY_LIKE_DATA, false),
                            data.getBooleanExtra(INTENT_DATA_BOARD_MODIFY_BOOKMARK_DATA, false)
                        )
                    } else if (data.getBooleanExtra(INTENT_DATA_BOARD_DELETE_SUCCESS, false)) {
                        mainBoardFragment.successDelete(
                            data.getIntExtra(INTENT_DATA_DETAIL_BOARD_SELECT_TYPE_DATA, 0),
                            data.getIntExtra(INTENT_DATA_DETAIL_BOARD_SELECT_POSITION_DATA, 0)
                        )
                    }
                }

                INTENT_BOARD_WRITE_REQUEST_CODE -> {
                    val mainBoardFragment: MainBoardFragment = mViewPager.adapter?.instantiateItem(mViewPager, mViewPager.currentItem) as MainBoardFragment
                    mainBoardFragment.setRefresh()
                }

//                INTENT_DATA_FROM_NON_MEMBER_TO_MEMBER_REQUEST_CODE -> {
//                    successLogin()
//                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        NagajaLog().e("wooks, MainActivity onResume()")

        if (mIsInit) {
            successLogin()
        }

//        if (!TextUtils.isEmpty(SharedPreferencesUtil().getLocationName(this@MainActivity))) {
//            displayCurrentLocation(SharedPreferencesUtil().getLocationName(this@MainActivity)!!)
//        } else {
//            getDefaultSettingArea()
//        }
        getDefaultSettingArea()

        if (!mIsInit) {
            if (!MAPP.IS_NON_MEMBER_SERVICE) {
                if (null != MAPP.PUSH_DATA) {
                    getCompanyMemberData()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    // ==========================================================================================
    // API
    // ==========================================================================================

    /**
     * API. Get Company Member Data
     */
    private fun getCompanyMemberData() {
        val nationPhoneCode = MAPP.SELECT_LANGUAGE_CODE
        val secureKey = MAPP.USER_DATA.getSecureKey()

        if (TextUtils.isEmpty(nationPhoneCode) || TextUtils.isEmpty(secureKey) ) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyMember(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<CompanyMemberData>> {
                override fun onSuccess(resultData: ArrayList<CompanyMemberData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    MAPP.USER_DATA.setCompanyMemberListData(resultData)

                    if (null != MAPP.PUSH_DATA) {
                        try {
                            if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_RESERVATION) {
                                if (MAPP.PUSH_DATA!!.getReservateType() == 0) {
                                    // 예약자
                                    val intent = Intent(this@MainActivity, ReservationActivity::class.java)
                                    startActivity(intent)
//                                    showToast(this@MainActivity, "000000")
                                } else if (MAPP.PUSH_DATA!!.getReservateType() == 1) {
                                    getCompanyDefaultData(MAPP.PUSH_DATA!!.getReservateCompanyUid(), false, -1)
                                }
                            } else if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_NOTICE || MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_EVENT
                                || MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_REPORT_MISSING) {

                                val categoryUid = MAPP.PUSH_DATA!!.getTopicCategoryUid()
                                val boardUid = MAPP.PUSH_DATA!!.getTopicBoardUid()

                                NagajaLog().e("wooks, categoryUid = $categoryUid")
                                NagajaLog().e("wooks, boardUid = $boardUid")

                                if (categoryUid > 0 && boardUid > 0) {
                                    val mainMenuItemData = MainMenuItemData()
                                    mainMenuItemData.setCategoryUid(categoryUid)
                                    mainMenuItemData.setShareBoardUid(boardUid)

                                    val intent: Intent
                                    if (categoryUid == COMPANY_TYPE_NOTICE || categoryUid == 247 || categoryUid == 248) {
                                        intent = Intent(this@MainActivity, NoticeActivity::class.java)
                                        intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                                        startActivity(intent)
                                    } else if (categoryUid == COMPANY_TYPE_REPORT_DISAPPEARANCE) {
                                        intent = Intent(this@MainActivity, MissingActivity::class.java)
                                        intent.putExtra(INTENT_DATA_STORE_LIST_DATA, mainMenuItemData)
                                        startActivity(intent)
                                    }
                                }
                            } else if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_NOTE) {

                                val companyUid = MAPP.PUSH_DATA!!.getCompanyUid()
                                val boardUid = MAPP.PUSH_DATA!!.getTopicBoardUid()

                                if (companyUid > 0) {
                                    getCompanyDefaultData(companyUid, true, boardUid)
                                } else {
                                    intent = Intent(this@MainActivity, NoteActivity::class.java)
                                    intent.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, boardUid)
                                    startActivity(intent)
                                }
                            } else if (MAPP.PUSH_DATA!!.getType() == PUSH_TYPE_CHAT) {
                                val boardUid = MAPP.PUSH_DATA!!.getTopicBoardUid()

                                if (boardUid > 0) {
                                    val intentNote = Intent(this@MainActivity, ChatViewActivity::class.java)
                                    intentNote.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, boardUid)
                                    intentNote.putExtra(INTENT_DATA_CHAT_CLASS_DATA, "null")
                                    intentNote.putExtra(INTENT_DATA_CHAT_DATA, "null")
                                    startActivity(intentNote)
                                }
                            }

                            NagajaLog().e("wooks, MAPP.PUSH_DATA = 3333")
                            MAPP.PUSH_DATA = null
                        } catch (e: Exception) {
                            e.printStackTrace()
                            NagajaLog().e("wooks, MAPP.PUSH_DATA = 4444")
                            MAPP.PUSH_DATA = null
                        }
                    } else {
                        NagajaLog().e("wooks, MAPP.PUSH_DATA = null")
                    }
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(this@MainActivity)
                    }
                }
            },
            nationPhoneCode,
            secureKey
        )
    }

    /**
     * API. Get Location Area
     */
    private fun getLocationArea() {
        var nationPosition = 1

        if (SharedPreferencesUtil().getSaveNationCode(this@MainActivity) == 1) {
            nationPosition = 1
        } else if (SharedPreferencesUtil().getSaveNationCode(this@MainActivity) == 82) {
            nationPosition = 2
        }  else if (SharedPreferencesUtil().getSaveNationCode(this@MainActivity) == 63) {
            nationPosition = 3
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getLocationArea(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<LocationAreaMainData>> {
                override fun onSuccess(resultData: ArrayList<LocationAreaMainData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    MAPP.LOCATION_AREA_LIST_DATA = resultData

                    var mainArea = ""
                    for (i in MAPP.LOCATION_AREA_LIST_DATA.indices) {
                        if (SharedPreferencesUtil().getSaveMainAreaCode(this@MainActivity) == MAPP.LOCATION_AREA_LIST_DATA[i].getCategoryUid()) {
                            mainArea = MAPP.LOCATION_AREA_LIST_DATA[i].getCategoryName()

                            for (k in MAPP.LOCATION_AREA_LIST_DATA[i].getLocationAreaSubListData().indices) {
                                if (SharedPreferencesUtil().getSaveSubAreaCode(this@MainActivity) == MAPP.LOCATION_AREA_LIST_DATA[i].getLocationAreaSubListData()[k].getCategoryUid()) {
                                    mainArea = mainArea + " - " + MAPP.LOCATION_AREA_LIST_DATA[i].getLocationAreaSubListData()[k].getCategoryName()
                                    break
                                }
                            }
                        }
                    }

                    displayCurrentLocation(mainArea)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(this@MainActivity)
                    }
                }
            },
            nationPosition.toString(),
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    private fun getDefaultSettingArea() {

        var latitude = ""
        var longitude = ""

        NagajaLog().e("wooks, SharedPreferencesUtil().getSaveLocation(this@MainActivity) = ${SharedPreferencesUtil().getSaveLocation(this@MainActivity)}")

//        if (!TextUtils.isEmpty(SharedPreferencesUtil().getSaveLocation(this@MainActivity)) && !TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
//            latitude = SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.substring(0, SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.indexOf(","))
//            longitude = SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.substring(SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.indexOf(",")
//                    + 1, SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.length)
//        } else {

//            if (!TextUtils.isEmpty(getLocation())) {
//                MAPP.USER_LOCATION = getLocation()
//                SharedPreferencesUtil().setSaveLocation(this@MainActivity, getLocation())
//
//                latitude = SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.substring(0, SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.indexOf(","))
//                longitude = SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.substring(SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.indexOf(",")
//                        + 1, SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.length)
//            } else {
//                when (SharedPreferencesUtil().getSelectLanguage(this@MainActivity)) {
//                    SELECT_LANGUAGE_EN -> {
//                        latitude = CURRENT_LOCATION_EN.substring(0, CURRENT_LOCATION_EN.indexOf(","))
//                        longitude = CURRENT_LOCATION_EN.substring(CURRENT_LOCATION_EN.indexOf(",") + 1, CURRENT_LOCATION_EN.length)
//
//                        MAPP.USER_LOCATION = CURRENT_LOCATION_EN
//                    }
//
//                    SELECT_LANGUAGE_KO -> {
//                        latitude = CURRENT_LOCATION_KO.substring(0, CURRENT_LOCATION_KO.indexOf(","))
//                        longitude = CURRENT_LOCATION_KO.substring(CURRENT_LOCATION_KO.indexOf(",") + 1, CURRENT_LOCATION_KO.length)
//
//                        MAPP.USER_LOCATION = CURRENT_LOCATION_KO
//                    }
//
//                    SELECT_LANGUAGE_FIL -> {
//                        latitude = CURRENT_LOCATION_FIL.substring(0, CURRENT_LOCATION_FIL.indexOf(","))
//                        longitude = CURRENT_LOCATION_FIL.substring(CURRENT_LOCATION_FIL.indexOf(",") + 1, CURRENT_LOCATION_FIL.length)
//
//                        MAPP.USER_LOCATION = CURRENT_LOCATION_FIL
//                    }
//
//                    SELECT_LANGUAGE_ZH -> {
//                        latitude = CURRENT_LOCATION_ZH.substring(0, CURRENT_LOCATION_ZH.indexOf(","))
//                        longitude = CURRENT_LOCATION_ZH.substring(CURRENT_LOCATION_ZH.indexOf(",") + 1, CURRENT_LOCATION_ZH.length)
//
//                        MAPP.USER_LOCATION = CURRENT_LOCATION_ZH
//                    }
//
//                    SELECT_LANGUAGE_JA -> {
//                        latitude = CURRENT_LOCATION_JA.substring(0, CURRENT_LOCATION_JA.indexOf(","))
//                        longitude = CURRENT_LOCATION_JA.substring(CURRENT_LOCATION_JA.indexOf(",") + 1, CURRENT_LOCATION_JA.length)
//
//                        MAPP.USER_LOCATION = CURRENT_LOCATION_JA
//                    }
//
//                }
//                SharedPreferencesUtil().setSaveLocation(this@MainActivity, MAPP.USER_LOCATION)
//            }
//        }

        if (TextUtils.isEmpty(SharedPreferencesUtil().getSaveLocation(this@MainActivity))) {
            latitude = CURRENT_LOCATION_FIL.substring(0, CURRENT_LOCATION_FIL.indexOf(","))
            longitude = CURRENT_LOCATION_FIL.substring(CURRENT_LOCATION_FIL.indexOf(",") + 1, CURRENT_LOCATION_FIL.length)

            MAPP.USER_LOCATION = CURRENT_LOCATION_FIL

//            SharedPreferencesUtil().setSaveLocation(this@MainActivity, MAPP.USER_LOCATION)
        } else {
            latitude = SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.substring(0, SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.indexOf(","))
            longitude = SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.substring(SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.indexOf(",")
                    + 1, SharedPreferencesUtil().getSaveLocation(this@MainActivity)!!.length)

            MAPP.USER_LOCATION = "$latitude, $longitude"

//            SharedPreferencesUtil().setSaveLocation(this@MainActivity, MAPP.USER_LOCATION)
        }

//        if ((TextUtils.isEmpty(latitude) || latitude == "null") || (TextUtils.isEmpty(longitude)) || longitude == "null") {
//            latitude = CURRENT_LOCATION_FIL.substring(0, CURRENT_LOCATION_FIL.indexOf(","))
//            longitude = CURRENT_LOCATION_FIL.substring(CURRENT_LOCATION_FIL.indexOf(",") + 1, CURRENT_LOCATION_FIL.length)
//
//            MAPP.USER_LOCATION = CURRENT_LOCATION_FIL
//
//            SharedPreferencesUtil().setSaveLocation(this@MainActivity, MAPP.USER_LOCATION)
//        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getDefaultSettingArea(
            mRequestQueue,
            object : NagajaManager.RequestListener<DefaultSettingAreaData> {
                override fun onSuccess(resultData: DefaultSettingAreaData) {
                    if (null == resultData) {
                        return
                    }

                    SharedPreferencesUtil().setSaveLocation(this@MainActivity, resultData.getVirtualLatitude().toString() + "," + resultData.getVirtualLongitude().toString())

                    var nationUid = 0
                    if (resultData.getLocationUid() == 1) {
                        nationUid = 1
                    } else if (resultData.getLocationUid() == 2) {
                        nationUid = 82
                    } else if (resultData.getLocationUid() == 3) {
                        nationUid = 63
                    } else {
                        nationUid = 63
                    }
                    SharedPreferencesUtil().setSaveNationCode(this@MainActivity, nationUid)

                    SharedPreferencesUtil().setSaveMainAreaCode(this@MainActivity, resultData.getCategoryUid())
                    SharedPreferencesUtil().setSaveSubAreaCode(this@MainActivity, resultData.getCategoryAreaUid())
//                    SharedPreferencesUtil().setLocationName(this@MainActivity, resultData.getCategoryName() + " - " + resultData.getCategoryAreaName())
                    SharedPreferencesUtil().setLocationName(this@MainActivity, /*resultData.getCategoryName() + " - " + */resultData.getCategoryAreaName())

                    displayCurrentLocation(SharedPreferencesUtil().getLocationName(this@MainActivity)!!)

                    if (!TextUtils.isEmpty(MAPP.DYNAMIC_LINK_URL)) {
                        getDynamicLinkCheck()
                    }
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(this@MainActivity)
                    }
                }
            },
            latitude,
            longitude,
            MAPP.SELECT_LANGUAGE_CODE
        )
    }

    /**
     * API. Get Company Member Data
     */
    private fun getCompanyDefaultData(companyUid: Int, isNote: Boolean, noteUid: Int) {
        if (TextUtils.isEmpty(MAPP.USER_DATA.getSecureKey())) {
            return
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyDefaultData(
            mRequestQueue,
            object : NagajaManager.RequestListener<CompanyDefaultData> {
                override fun onSuccess(resultData: CompanyDefaultData) {

                    if (isNote) {
                        if (noteUid > -1) {
                            val intent = Intent(this@MainActivity, NoteActivity::class.java)
                            intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, resultData)
                            intent.putExtra(INTENT_DATA_IS_COMPANY_NOTE_DATA, true)
                            intent.putExtra(INTENT_DATA_NOTE_BOARD_UID_DATA, noteUid)
                            startActivity(intent)
                        }
                    } else {
                        val intent = Intent(this@MainActivity, CompanyReservationActivity::class.java)
                        intent.putExtra(INTENT_DATA_COMPANY_DEFAULT_DATA, resultData)
                        startActivity(intent)
                    }
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(this@MainActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            companyUid
        )
    }
}