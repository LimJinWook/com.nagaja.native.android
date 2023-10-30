package com.nagaja.app.android.FAQ

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.*
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.StoreMainImageAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_faq_detail.view.*
import kotlinx.android.synthetic.main.fragment_full_screen_image.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_news_detail.view.*
import kotlinx.android.synthetic.main.fragment_note_detail.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FAQDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View

    private lateinit var mImageRecyclerView: RecyclerView

    private lateinit var mMainImageAdapter: StoreMainImageAdapter

    private val mMainImageList = ArrayList<String>()
    private val mOriginImageList = ArrayList<String>()

    private lateinit var mListener: OnFAQDetailFragmentListener

    interface OnFAQDetailFragmentListener {
        fun onBack()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {
        const val ARGS_FAQ_DATA        = "args_faq_data"

        fun newInstance(data: NoticeData): FAQDetailFragment {
            val args = Bundle()
            val fragment = FAQDetailFragment()
            args.putSerializable(ARGS_FAQ_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getFAQData(): NoticeData {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_FAQ_DATA) as NoticeData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainImageAdapter = StoreMainImageAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContainer = inflater.inflate(R.layout.fragment_faq_detail, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        // Header Location View
        val headerLocationView = mContainer.layout_header_location_view
        headerLocationView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_CHANGE_LOCATION)
        }

        // Header Location Text View
        val locationTextView = mContainer.layout_header_location_text_view
        if (!TextUtils.isEmpty(MAPP.SELECT_NATION_NAME)) {
            locationTextView.text = MAPP.SELECT_NATION_NAME
        }

        // Header Select Language Image View
        val selectLanguageImageView = mContainer.layout_header_select_language_image_view
        selectLanguageImageView.setImageResource(getLanguageIcon(mActivity))
        selectLanguageImageView.setOnClickListener {
            showSelectLanguageCustomDialog(mActivity)
        }

        // Header Search Image View
        val headerSearchImageView = mContainer.layout_header_search_image_view
        headerSearchImageView.setOnSingleClickListener {
            mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_SEARCH)
        }

        // Header Share Image View
        val headerShareImageView = mContainer.layout_header_clipboard_image_view
        headerShareImageView.setOnSingleClickListener {
            setShareUrl(mActivity, if (getFAQData().getParentCategoryUid() == COMPANY_TYPE_NOTICE) COMPANY_TYPE_NOTICE else COMPANY_TYPE_FAQ, getFAQData().getNoticeUid())
        }

        // Header Notification Image View
        val headerNotificationImageView = mContainer.layout_header_notification_image_view
        headerNotificationImageView.setOnSingleClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                mListener.onChangeLocationAndNotificationScreen(NagajaActivity.SELECT_HEADER_MENU_TYPE_NOTIFICATION)
            }
        }

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        var title = if (getFAQData().getCategoryUid() == 247) {
            mActivity.resources.getString(R.string.text_notice)
        } else if (getFAQData().getCategoryUid() == 248) {
            mActivity.resources.getString(R.string.text_event)
        } else {
            mActivity.resources.getString(R.string.text_faq)
        }
        titleTextView.text = title

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Category Text View
        val categoryTextView = mContainer.fragment_faq_detail_category_text_view
        if (!TextUtils.isEmpty(getFAQData().getCategoryName())) {
            categoryTextView.text = getFAQData().getCategoryName()
        }

        // FAQ Title Text View
        val faqTitleTextView = mContainer.fragment_faq_detail_title_text_view
        if (!TextUtils.isEmpty(getFAQData().getNoticeSubject())) {
            faqTitleTextView.text = getFAQData().getNoticeSubject()
        }

        // Date Text View
        val dateTextView = mContainer.fragment_faq_detail_date_text_view
        if (!TextUtils.isEmpty(getFAQData().getCreateDate())) {
            var time = getFAQData().getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            dateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        // View Count Text View
        val viewCountTextView = mContainer.fragment_faq_detail_view_count_text_view
        viewCountTextView.text = if (getFAQData().getViewCount() > 0) getFAQData().getViewCount().toString() else "0"

//        // Download View
//        val downloadView = mContainer.fragment_faq_detail_download_view
//        downloadView.visibility = if (!TextUtils.isEmpty(getFAQData()!!.getDownloadUrl())) View.VISIBLE else View.GONE
//
//        // Download Text View
//        val downloadTextView = mContainer.fragment_faq_detail_download_text_view
//        if (!TextUtils.isEmpty(getFAQData()!!.getDownloadUrl()) && !TextUtils.isEmpty(getFAQData()!!.getDownloadFileName())) {
//            downloadTextView.text = getFAQData()!!.getDownloadFileName() + "." + getFAQData()!!.getExtension()
//            downloadTextView.setOnSingleClickListener {
//                if (mIsDownloading) {
//                    return@setOnSingleClickListener
//                }
//
//                mLoadingView.visibility = View.VISIBLE
//                downloadFile()
//            }
//        }

        // Image Recycler View
        mImageRecyclerView = mContainer.fragment_faq_detail_image_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        mMainImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mOriginImageList, position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mMainImageAdapter

        // Content Text View
        val contentTextView = mContainer.fragment_faq_detail_content_text_view
        if (!TextUtils.isEmpty(getFAQData().getNoticeContent())) {
            contentTextView.text = getFAQData().getNoticeContent()
        }

        // Loading View
        mLoadingView = mContainer.fragment_faq_detail_loading_view

        // Download Popup View
//        mDownloadPopupView = mContainer.fragment_faq_detail_download_popup_view
//        mDownloadPercentTextView = mContainer.fragment_faq_detail_download_percent_text_view
//        mDownloadPercent2TextView = mContainer.fragment_faq_detail_download_percent_2_text_view
//        mDownloadProgressBar = mContainer.fragment_faq_detail_download_progress_bar
//        mDownloadProgressBar.isIndeterminate = true

        setImageListData()
    }

    private fun setImageListData() {
        for (i in getFAQData().getNoticeImageListData().indices) {
            mMainImageList.add(DEFAULT_IMAGE_DOMAIN /*+ IMAGE_VIEW*/ + getFAQData().getNoticeImageListData()[i].getItemImageName())
            mOriginImageList.add(DEFAULT_IMAGE_DOMAIN + getFAQData().getNoticeImageListData()[i].getItemImageName())
        }
        mMainImageAdapter.setData(mMainImageList)
    }

    private fun showLoginGuideCustomDialog() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_noti),
            mActivity.resources.getString(R.string.text_message_login_guide),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()
                mListener.onDefaultLoginScreen()
            }
        })
        customDialog.show()
    }

    override fun onPause() {
        super.onPause()
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
        if (context is OnFAQDetailFragmentListener) {
            mActivity = context as Activity

            if (context is OnFAQDetailFragmentListener) {
                mListener = context;
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnFAQDetailFragmentListener"
                );
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnFAQDetailFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFAQDetailFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}