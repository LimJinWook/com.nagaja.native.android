package com.nagaja.app.android.Store

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.GpsTracker
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_store.view.*
import kotlinx.android.synthetic.main.fragment_used_market.view.*
import kotlinx.android.synthetic.main.fragment_used_market_detail.view.*
import kotlinx.android.synthetic.main.layout_store_menu_view.view.*
import kotlinx.android.synthetic.main.layout_store_review_view.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class StoreFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mMenuView: View
    private lateinit var mReView: View
    private lateinit var mMenuLayout: View
    private lateinit var mReViewLayout: View
    private lateinit var mMenuUnderLineView: View
    private lateinit var mReViewUnderLineView: View

    private lateinit var mScrollView: NestedScrollView

    private lateinit var mMenuTextView: TextView
    private lateinit var mReViewTextView: TextView
    private lateinit var mAddressTextView: TextView

    private lateinit var mMainImageRecyclerView: RecyclerView
    private lateinit var mRecommendRecyclerView: RecyclerView
    private lateinit var mStoreMenuRecyclerView: RecyclerView
    private lateinit var mStoreReviewRecyclerView: RecyclerView

    private lateinit var mStoreMainImageAdapter: StoreMainImageAdapter
    private lateinit var mStoreRecommendMenuAdapter: StoreRecommendMenuAdapter
    private lateinit var mStoreMenuAdapter: StoreMenuAdapter
    private lateinit var mStoreReviewAdapter: StoreReviewAdapter

    private val mMainImageList = ArrayList<String>()
    private val mReViewImageList = ArrayList<String>()

    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    private val mLatLng: LatLng? = null
    private var mMapMarker: Marker? = null

    private lateinit var mListener: OnStoreFragmentListener

    private var mClipboardManager: ClipboardManager? = null
    private var mClipData: ClipData? = null

    interface OnStoreFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onMapScreen(location: String)
    }

    companion object {

        const val PERMISSION_REQUEST_CODE       = 0x01

        const val TAB_TYPE_MENU         = 0x01
        const val TAB_TYPE_REVIEW       = 0x02

        fun newInstance(): StoreFragment {
            val args = Bundle()
            val fragment = StoreFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mClipboardManager = mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        mStoreMainImageAdapter = StoreMainImageAdapter(mActivity)
        mStoreRecommendMenuAdapter = StoreRecommendMenuAdapter(mActivity)
        mStoreMenuAdapter = StoreMenuAdapter(mActivity)
        mStoreReviewAdapter = StoreReviewAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_store, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        // Scroll View
        mScrollView = mContainer.fragment_store_scroll_view

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = "나가자피자-필리핀점"

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Address Text View
        mAddressTextView = mContainer.fragment_store_address_text_view

        // Google Map View
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragment_store_map_view) as SupportMapFragment
        mMapFragment.getMapAsync {
            val seoul = LatLng(37.494870, 126.960763)
            val location = CameraUpdateFactory.newLatLngZoom(seoul, 17f)
            it.moveCamera(location)
            it.animateCamera(location)
        }

        // Main Image Recycler View
        mMainImageRecyclerView = mContainer.fragment_store_main_image_recycler_view
        mMainImageRecyclerView.setHasFixedSize(true)
        mMainImageRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mStoreMainImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mMainImageList, position)
            }
        })
        mMainImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMainImageRecyclerView.adapter = mStoreMainImageAdapter

        // Fine Way View
        val findWayView = mContainer.fragment_store_find_way_view
        findWayView.setOnClickListener {
            val uri = "http://maps.google.com/maps?saddr=14.632281840274995,121.03010939623091&daddr=14.632054880067493,121.03750730467308"
//            val uri = "http://maps.google.com/maps?saddr=37.4832143,126.8974597&daddr=37.4834143,126.9044597"
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            if (Build.VERSION.SDK_INT < /*Build.VERSION_CODES.TIRAMISU*/ 33) {
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
            }
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
            startActivity(intent)

            NagajaLog().d("wooks, LOCATION = ${getUserLocation()}")

//            mListener.onMapScreen("14.701043228454056,121.06546351595411")
        }

        // Navigation View
        val navigationView = mContainer.fragment_store_navigation_view
        navigationView.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=14.701043228454056,121.06546351595411&mode=d")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        // Copy Address View
        val copyAddressView = mContainer.fragment_store_copy_address_view
        copyAddressView.setOnClickListener {
            if (!TextUtils.isEmpty(mAddressTextView.text)) {
                mClipData = ClipData.newPlainText("label", mAddressTextView.text.toString())
                mClipboardManager!!.setPrimaryClip(mClipData!!)

                showToast(mActivity, mActivity.resources.getString(R.string.text_copy_address))
            }
        }

        // Menu View
        mMenuView = mContainer.fragment_store_menu_view
        mMenuView.setOnClickListener {
            setTab(TAB_TYPE_MENU)
        }

        // Menu Text View
        mMenuTextView = mContainer.fragment_store_menu_text_view

        // Menu Under Line View
        mMenuUnderLineView = mContainer.fragment_store_menu_under_line_view

        // ReView View
        mReView = mContainer.fragment_store_review_view
        mReView.setOnClickListener {
            setTab(TAB_TYPE_REVIEW)
        }

        // ReView Text View
        mReViewTextView = mContainer.fragment_store_review_text_view

        // ReView Under Line View
        mReViewUnderLineView = mContainer.fragment_store_review_under_line_view

        // Menu Layout
        mMenuLayout = mContainer.layout_store_menu

        // ReView Layout
        mReViewLayout = mContainer.layout_store_review

        // Recommend Menu Recycler View
        mRecommendRecyclerView = mContainer.layout_store_menu_view_recommend_recycler_view
        mRecommendRecyclerView.setHasFixedSize(true)
        mRecommendRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mStoreRecommendMenuAdapter.setOnStoreRecommendMenuClickListener(object : StoreRecommendMenuAdapter.OnStoreRecommendMenuClickListener {
            override fun onClick(data: StoreRecommendMenuData) {
                // TODO("Not yet implemented")
            }

        })
        mRecommendRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mRecommendRecyclerView.adapter = mStoreRecommendMenuAdapter

        // Store Menu Recycler View
        mStoreMenuRecyclerView = mContainer.layout_store_menu_view_menu_recycler_view
        mStoreMenuRecyclerView.setHasFixedSize(true)
        mStoreMenuRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mStoreMenuAdapter.setOnItemClickListener(object : StoreMenuAdapter.OnItemClickListener {
            override fun onClick(data: StoreDetailMenuData) {
                TODO("Not yet implemented")
            }

            override fun onImageClick(imageList: ArrayList<String>, position: Int) {
                TODO("Not yet implemented")
            }

        })
        mStoreMenuRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mStoreMenuRecyclerView.adapter = mStoreMenuAdapter

        // Store Review Recycler View
        mStoreReviewRecyclerView = mContainer.layout_store_review_recycler_view
        mStoreReviewRecyclerView.setHasFixedSize(true)
        mStoreReviewRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mStoreReviewAdapter.setOnItemClickListener(object : StoreReviewAdapter.OnItemClickListener {
            override fun onImageClick(imageList: ArrayList<String>, position: Int) {
                mListener.onFullScreenImage(imageList, position)
            }

            override fun onClick(data: StoreDetailReviewData) {
//                TODO("Not yet implemented")
            }

            override fun onModify(data: StoreDetailReviewData, position: Int) {
//                TODO("Not yet implemented")
            }

            override fun onDelete(data: StoreDetailReviewData, position: Int) {
//                TODO("Not yet implemented")
            }

            override fun onReport(reportUid: Int) {
//                TODO("Not yet implemented")
            }

            override fun onRecommend(reviewUid: Int, isLike: Boolean, position: Int) {
//                TODO("Not yet implemented")
            }

        })
        mStoreReviewRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mStoreReviewRecyclerView.adapter = mStoreReviewAdapter


        setTab(TAB_TYPE_MENU)

        setMainImageData()
    }

    private fun setTab(type: Int) {
        when (type) {
            TAB_TYPE_MENU -> {
                mMenuTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mMenuUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mReViewTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mReViewUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mMenuLayout.visibility = View.VISIBLE
                mReViewLayout.visibility = View.GONE
            }

            TAB_TYPE_REVIEW -> {
                mMenuTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mMenuUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mReViewTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mReViewUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mMenuLayout.visibility = View.GONE
                mReViewLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun getUserLocation(): String {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )

            NagajaLog().e("wooks, Location Permission Denied")

            return ""
        } else {
            val gpsTracker = GpsTracker(mActivity)
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

            NagajaLog().d("wooks, getUserLocation Latitude = $latitude")
            NagajaLog().d("wooks, getUserLocation Longitude = $longitude")

            return "$latitude,$longitude"
        }
    }

    private fun setMainImageData() {

        mMainImageList.add("https://imagescdn.gettyimagesbank.com/500/201708/a10969334.jpg")
        mMainImageList.add("https://cdn.dominos.co.kr/admin/upload/goods/20210902_1L4hLyvI.jpg?RS=350x350&SP=1")
        mMainImageList.add("https://cdn.dominos.co.kr/admin/upload/goods/20200508_1fYuDcMq.jpg?RS=350x350&SP=1")
        mMainImageList.add("http://img3.tmon.kr/cdn3/deals/2019/10/10/2524792090/original_2524792090_front_3e933_1570697801production.jpg")
        mMainImageList.add("http://openimage.interpark.com/goods_image_big/3/5/5/1/8498583551_l.jpg")

        mStoreMainImageAdapter.setData(mMainImageList)

        setMap()
        setRecommendMenuData()
    }

    private fun setMap() {
        
        val testLocation = "14.701043228454056,121.06546351595411"
            
        mMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
            mMap = googleMap

            mMap!!.uiSettings.isScrollGesturesEnabled = false
            mMap!!.uiSettings.isZoomGesturesEnabled = false
            mMap!!.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false

            mMap!!.setOnMapClickListener {
                mListener.onMapScreen("14.701043228454056,121.06546351595411")
            }

            if (null != mMapMarker) {
                mMapMarker!!.remove()
                mMapMarker = null
            }

            if (TextUtils.isEmpty(testLocation)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_empty_location))
                return@OnMapReadyCallback
            }

            val lat = testLocation.substring(0, testLocation.indexOf(","))
            val lon = testLocation.substring(testLocation.indexOf(",") + 1, testLocation.length)

            val marker = LatLng(lat.toDouble(), lon.toDouble())
            val location = CameraUpdateFactory.newLatLngZoom(marker, 17f)

            mMapMarker = mMap!!.addMarker(
                MarkerOptions()
                    .position(marker)
                    .title("나가자피자-필리핀점")
                    .icon(bitmapDescriptorFromVector(mActivity, R.drawable.icon_map_marker_4))
            )
            mMapMarker!!.showInfoWindow()
            mMap!!.moveCamera(location)
            mMap!!.animateCamera(location)

            mAddressTextView.text = getAddress(lat.toDouble(), lon.toDouble())
        })
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(mActivity, Locale.getDefault())
        (mActivity).runOnUiThread(
            Runnable {
                try {
                    val addressList = geocoder.getFromLocation(lan, lon, 1)
                    if (addressList!!.size != 0) {
                        address = addressList[0].getAddressLine(0)
//                        mNationCode = addressList[0].countryCode
//                        mPostalCode = addressList[0].postalCode

                        if (address.substring(0, 4) == "대한민국") {
                            address = address.substring(5)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
        return address
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun setRecommendMenuData() {
        val storeRecommendMenuListData = ArrayList<StoreRecommendMenuData>()

        for (i in 0..9) {
            val storeRecommendMenuData = StoreRecommendMenuData()
            when (i) {
                0 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://cdn.consumerpost.co.kr/news/photo/202109/302333_202487_1421.jpg")
                    storeRecommendMenuData.setName("인기메뉴")
                    storeRecommendMenuData.setPrice("17,000")
                }

                1 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://image.newdaily.co.kr/site/data/img/2017/01/24/2017012410033_0.jpg")
                    storeRecommendMenuData.setName("맛있는피자")
                    storeRecommendMenuData.setPrice("20,000")
                }

                2 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://akamai.pizzahut.co.kr/2020biz/public/bizPress/ac6d5498-d2b3-4d79-845a-58bb8b762bb4.jpg")
                    storeRecommendMenuData.setName("하프피자")
                    storeRecommendMenuData.setPrice("19,900")
                }

                3 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://cdn.gukjenews.com/news/photo/201805/921479_698149_5655.jpg")
                    storeRecommendMenuData.setName("더맛있는피자")
                    storeRecommendMenuData.setPrice("19,900")
                }

                4 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://image.newdaily.co.kr/site/data/img/2017/01/24/2017012410033_0.jpg")
                    storeRecommendMenuData.setName("런치세트")
                    storeRecommendMenuData.setPrice("30,000")
                }

                5 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://akamai.pizzahut.co.kr/2020biz/public/bizPress/5e2c066e-0353-48ef-8b51-c1a78c068c72.jpg")
                    storeRecommendMenuData.setName("첫구매피자")
                    storeRecommendMenuData.setPrice("9,900")
                }

                6 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://img.etnews.com/photonews/2012/1362321_20201204102305_451_0001.jpg")
                    storeRecommendMenuData.setName("1+1피자")
                    storeRecommendMenuData.setPrice("45,900")
                }

                7 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://akamai.pizzahut.co.kr/2020biz/public/bizPress/0cc7d4ab-37e8-4d57-bcfe-cbffb697f7e2.jpg")
                    storeRecommendMenuData.setName("메가크런치")
                    storeRecommendMenuData.setPrice("10,900")
                }

                8 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://t1.daumcdn.net/cfile/tistory/21B466335981220E23")
                    storeRecommendMenuData.setName("크런치치즈스테이크피자")
                    storeRecommendMenuData.setPrice("39,900")
                }

                9 -> {
                    storeRecommendMenuData.setMenuImageUrl("https://t1.daumcdn.net/cfile/tistory/21B466335981220E23")
                    storeRecommendMenuData.setName("크런치치즈스테이크피자")
                    storeRecommendMenuData.setPrice("39,900")
                }
            }

            storeRecommendMenuListData.add(storeRecommendMenuData)
        }

        mStoreRecommendMenuAdapter.setData(storeRecommendMenuListData)

        setMenuData()
    }

    private fun setMenuData() {
        val storeMenuListData = ArrayList<StoreMenuData>()

        for (i in 0..9) {
            val storeMenuData = StoreMenuData()
            when (i) {
                0 -> {
                    storeMenuData.setMenuImageUrl("https://cdn.consumerpost.co.kr/news/photo/202109/302333_202487_1421.jpg")
                    storeMenuData.setName("나가자 피자")
                    storeMenuData.setMessage("유기농으로 한국에서 공수 해와 신선한 냉장 상태를 제공 합니다. 아주 맛있습니다.")
                    storeMenuData.setPriceDollar("20")
                    storeMenuData.setPriceWon("24792")
                    storeMenuData.setPricePeso("1097")
                }

                1 -> {
                    storeMenuData.setMenuImageUrl("https://image.newdaily.co.kr/site/data/img/2017/01/24/2017012410033_0.jpg")
                    storeMenuData.setName("수퍼 파파스")
                    storeMenuData.setMessage("메뉴 리소스는 MenuInflater로 확장되는 애플리케이션 메뉴(옵션 메뉴, 컨텍스트 메뉴 또는 하위 메뉴)를 정의합니다.")
                    storeMenuData.setPriceDollar("23")
                    storeMenuData.setPriceWon("28510")
                    storeMenuData.setPricePeso("1262")
                }

                2 -> {
                    storeMenuData.setMenuImageUrl("https://akamai.pizzahut.co.kr/2020biz/public/bizPress/ac6d5498-d2b3-4d79-845a-58bb8b762bb4.jpg")
                    storeMenuData.setName("수퍼 파파스")
                    storeMenuData.setMessage("메뉴 항목. <menu> 요소를 포함할 수 있습니다(하위 메뉴가 있는 경우). <menu> 또는 <group> 요소의 하위 요소여야 합니다.")
                    storeMenuData.setPriceDollar("30")
                    storeMenuData.setPriceWon("37197")
                    storeMenuData.setPricePeso("1646")
                }

                3 -> {
                    storeMenuData.setMenuImageUrl("https://cdn.gukjenews.com/news/photo/201805/921479_698149_5655.jpg")
                    storeMenuData.setName("더맛있는피자")
                    storeMenuData.setMessage("메뉴 항목. <menu> 요소를 포함할 수 있습니다(하위 메뉴가 있는 경우). <menu> 또는 <group> 요소의 하위 요소여야 합니다.")
                    storeMenuData.setPriceDollar("15")
                    storeMenuData.setPriceWon("18601")
                    storeMenuData.setPricePeso("823")
                }

                4 -> {
                    storeMenuData.setMenuImageUrl("https://image.newdaily.co.kr/site/data/img/2017/01/24/2017012410033_0.jpg")
                    storeMenuData.setName("런치세트")
                    storeMenuData.setMessage("리소스 ID. 고유한 리소스 ID입니다. 이 항목의 새 리소스 ID를 만들려면 \"@+id/name\" 양식을 사용합니다. 더하기 기호는 새 ID로 생성되어야 한다는 의미입니다.")
                    storeMenuData.setPriceDollar("36")
                    storeMenuData.setPriceWon("44618")
                    storeMenuData.setPricePeso("1975")
                }

                5 -> {
                    storeMenuData.setMenuImageUrl("https://akamai.pizzahut.co.kr/2020biz/public/bizPress/5e2c066e-0353-48ef-8b51-c1a78c068c72.jpg")
                    storeMenuData.setName("첫구매피자")
                    storeMenuData.setMessage("문자열 리소스. 문자열 리소스 또는 원시 문자열로 표시된 메뉴 제목입니다.")
                    storeMenuData.setPriceDollar("70")
                    storeMenuData.setPriceWon("86758")
                    storeMenuData.setPricePeso("3840")
                }

                6 -> {
                    storeMenuData.setMenuImageUrl("https://img.etnews.com/photonews/2012/1362321_20201204102305_451_0001.jpg")
                    storeMenuData.setName("1+1피자")
                    storeMenuData.setMessage("문자열 리소스. 문자열 리소스 또는 원시 문자열로 요약된 제목입니다. 이 제목은 일반 제목이 너무 긴 경우에 사용됩니다.")
                    storeMenuData.setPriceDollar("45")
                    storeMenuData.setPriceWon("55773")
                    storeMenuData.setPricePeso("2468")
                }

                7 -> {
                    storeMenuData.setMenuImageUrl("https://img.etnews.com/photonews/2012/1362321_20201204102305_451_0001.jpg")
                    storeMenuData.setName("1+1피자")
                    storeMenuData.setMessage("드로어블 리소스. 메뉴 항목 아이콘으로 사용될 이미지입니다.")
                    storeMenuData.setPriceDollar("10")
                    storeMenuData.setPriceWon("12394")
                    storeMenuData.setPricePeso("548")
                }

                8 -> {
                    storeMenuData.setMenuImageUrl("https://t1.daumcdn.net/cfile/tistory/21B466335981220E23")
                    storeMenuData.setName("크런치치즈스테이크피자")
                    storeMenuData.setMessage("키워드. 앱 바에서 이 항목이 작업 항목으로 표시되는 시기 및 방법입니다. 메뉴 항목은 활동이 앱 바를 포함하는 경우에만 작업 항목으로 표시될 수 있습니다. 유효한 값은 다음과 같습니다.")
                    storeMenuData.setPriceDollar("120")
                    storeMenuData.setPriceWon("148716")
                    storeMenuData.setPricePeso("6583")
                }

                9 -> {
                    storeMenuData.setMenuImageUrl("https://akamai.pizzahut.co.kr/2020biz/public/bizPress/5e2c066e-0353-48ef-8b51-c1a78c068c72.jpg")
                    storeMenuData.setName("나가자 마케팅 피자")
                    storeMenuData.setMessage("작업 항목과 함께 제목 텍스트(android:title에서 정의함)도 포함합니다. 값을 파이프(|)로 구분하여 플래그 세트로 다른 값 중의 하나와 함께 이 값을 포함할 수 있습니다.")
                    storeMenuData.setPriceDollar("99")
                    storeMenuData.setPriceWon("122661")
                    storeMenuData.setPricePeso("5432")
                }
            }

            storeMenuListData.add(storeMenuData)
        }

//        mStoreMenuAdapter.setData(storeMenuListData)

        setReviewData()
    }

    private fun setReviewData() {
        val storeReviewListData = ArrayList<StoreReviewData>()

        for (i in 0 until 5)  {
            val storeReviewData = StoreReviewData()
            when (i) {
                0 -> {
                    storeReviewData.setProfileImageUrl("https://dimg.donga.com/wps/NEWS/IMAGE/2021/09/07/109121848.2.jpg")
                    storeReviewData.setName("유재석")
                    storeReviewData.setID("AAABBB***")
                    storeReviewData.setTimestamp("1673250268000")
                    storeReviewData.setStarRating(5)
                    storeReviewData.setComment("유기농으로 한국에서 공수 해와 신선한 냉장 상태를 제공 합니다. 아주 맛있습니다.")
                    storeReviewData.setIsMyWrite(false)

                    val uploadImageListData = ArrayList<String>()
                    uploadImageListData.add("https://health.chosun.com/site/data/img_dir/2022/07/11/2022071101596_0.jpg")
                    uploadImageListData.add("https://d2v80xjmx68n4w.cloudfront.net/gigs/fPoZ31584321311.jpg")
                    uploadImageListData.add("https://imagescdn.gettyimagesbank.com/500/202109/a12387827.jpg")
                    uploadImageListData.add("http://d20aeo683mqd6t.cloudfront.net/ko/articles/title_images/000/039/143/medium/IMG_5649%E3%81%AE%E3%82%B3%E3%83%92%E3%82%9A%E3%83%BC.jpg?2019")
                    uploadImageListData.add("http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2019/09/27/20190927000594_0.jpg")
                    uploadImageListData.add("https://health.chosun.com/site/data/img_dir/2022/07/11/2022071101596_0.jpg")
                    uploadImageListData.add("https://d2v80xjmx68n4w.cloudfront.net/gigs/fPoZ31584321311.jpg")
                    uploadImageListData.add("https://imagescdn.gettyimagesbank.com/500/202109/a12387827.jpg")
                    uploadImageListData.add("http://d20aeo683mqd6t.cloudfront.net/ko/articles/title_images/000/039/143/medium/IMG_5649%E3%81%AE%E3%82%B3%E3%83%92%E3%82%9A%E3%83%BC.jpg?2019")
                    uploadImageListData.add("http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2019/09/27/20190927000594_0.jpg")

                    storeReviewData.setUploadImageArrayList(uploadImageListData)

                }

                1 -> {
                    storeReviewData.setProfileImageUrl("https://img.insight.co.kr/static/2021/06/19/700/img_20210619102129_v06qrfrj.webp")
                    storeReviewData.setName("손나은")
                    storeReviewData.setID("asdkla***")
                    storeReviewData.setTimestamp("1673250268000")
                    storeReviewData.setStarRating(3)
                    storeReviewData.setComment("메뉴 리소스는 MenuInflater로 확장되는 애플리케이션 메뉴(옵션 메뉴, 컨텍스트 메뉴 또는 하위 메뉴)를 정의합니다.")
                    storeReviewData.setIsMyWrite(false)

                    val uploadImageListData = ArrayList<String>()
                    uploadImageListData.add("https://t1.daumcdn.net/cfile/tistory/230F274E5384F3A408")
                    uploadImageListData.add("https://www.incheonin.com/news/photo/old/upfiles/editor/201710/QDEGAIX3i.jpg")

                    storeReviewData.setUploadImageArrayList(uploadImageListData)
                }

                2 -> {
                    storeReviewData.setProfileImageUrl("https://cdn.imweb.me/thumbnail/20230106/247b41759825a.jpg")
                    storeReviewData.setName("아저씨")
                    storeReviewData.setID("123***")
                    storeReviewData.setTimestamp("1673250268000")
                    storeReviewData.setStarRating(0)
                    storeReviewData.setComment("메뉴 항목. <menu> 요소를 포함할 수 있습니다(하위 메뉴가 있는 경우). <menu> 또는 <group> 요소의 하위 요소여야 합니다.")
                    storeReviewData.setIsMyWrite(false)

                    val uploadImageListData = ArrayList<String>()
                    uploadImageListData.add("https://tgzzmmgvheix1905536.cdn.ntruss.com/2021/01/62b7a76c394847078682de0c235c399d")
                    uploadImageListData.add("https://m.segye.com/content/image/2021/01/07/20210107516500.jpg")
                    uploadImageListData.add("https://img.daily.co.kr/@files/www.daily.co.kr/content/food/2020/20200730/514faadc4b73cd4812f080e029f2f1af.jpg")

                    storeReviewData.setUploadImageArrayList(uploadImageListData)
                }

                3 -> {
                    storeReviewData.setProfileImageUrl("https://cdn.stardailynews.co.kr/news/photo/202112/312683_359047_3544.jpg")
                    storeReviewData.setName("아이돌")
                    storeReviewData.setID("ppppqqqq***")
                    storeReviewData.setTimestamp("1673250268000")
                    storeReviewData.setStarRating(0)
                    storeReviewData.setComment("문자열 리소스. 문자열 리소스 또는 원시 문자열로 요약된 제목입니다. 이 제목은 일반 제목이 너무 긴 경우에 사용됩니다.")
                    storeReviewData.setIsMyWrite(true)

                    val uploadImageListData = ArrayList<String>()
                    uploadImageListData.add("https://t1.daumcdn.net/cfile/tistory/9978584E5A5C18C427")
                    storeReviewData.setUploadImageArrayList(uploadImageListData)
                }

                4 -> {
                    storeReviewData.setProfileImageUrl("https://image.newdaily.co.kr/site/data/img/2017/01/24/2017012410033_0.jpg")
                    storeReviewData.setName("The330")
                    storeReviewData.setID("the330***")
                    storeReviewData.setTimestamp("1673250268000")
                    storeReviewData.setStarRating(5)
                    storeReviewData.setComment("리소스 ID. 고유한 리소스 ID입니다. 이 항목의 새 리소스 ID를 만들려면 \"@+id/name\" 양식을 사용합니다. 더하기 기호는 새 ID로 생성되어야 한다는 의미입니다.")
                    storeReviewData.setIsMyWrite(false)

                    val uploadImageListData = ArrayList<String>()
//                    uploadImageListData.add("https://t1.daumcdn.net/cfile/tistory/9978584E5A5C18C427")
                    storeReviewData.setUploadImageArrayList(uploadImageListData)
                }

            }

            storeReviewListData.add(storeReviewData)
        }

//        mStoreReviewAdapter.setData(storeReviewListData)

    }

    fun dispatchTouchEvent() {
        mScrollView.requestDisallowInterceptTouchEvent(true)
    }

    /**
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnStoreFragmentListener) {
            mActivity = context as Activity

            if (context is OnStoreFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnStoreFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnStoreFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnStoreFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}