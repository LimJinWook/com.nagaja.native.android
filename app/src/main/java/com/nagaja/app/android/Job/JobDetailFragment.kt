package com.nagaja.app.android.Job

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.StoreMainImageAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Job.JobFragment.Companion.SELECT_TYPE_JOB
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_job.view.*
import kotlinx.android.synthetic.main.fragment_job_detail.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class JobDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mDownloadContainerView: View
    private lateinit var mDownloadPopupView : View
    private lateinit var mLoadingView: View

    private lateinit var mJobTypeTextView: TextView
    private lateinit var mJobTitleTextView: TextView
    private lateinit var mDateTextView: TextView
    private lateinit var mViewCountTextView: TextView
    private lateinit var mDownloadFileNameTextView: TextView
    private lateinit var mContentTextView: TextView
    private lateinit var mChatTextView: TextView
    private lateinit var mModifyTextView: TextView
    private lateinit var mBackTextView: TextView

    private lateinit var mImageRecyclerView: RecyclerView

    private lateinit var mMainImageAdapter: StoreMainImageAdapter

    private lateinit var mListener: OnJobDetailFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mBoardData: BoardData

    private val mMainImageList = ArrayList<String>()

    interface OnJobDetailFragmentListener {
        fun onBack()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onJobModifyScreen(jobType: Int, boardUid: Int, companyUid: Int)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
        fun onChatViewScreen(chatClass: String, chatData: String)
    }

    companion object {
        const val ARGS_BOARD_UID_DATA              = "args_board_uid_data"
        const val ARGS_COMPANY_UID_DATA            = "args_company_uid_data"

        fun newInstance(boardUid: Int, companyUid: Int): JobDetailFragment {
            val args = Bundle()
            val fragment = JobDetailFragment()
            args.putInt(ARGS_BOARD_UID_DATA, boardUid)
            args.putInt(ARGS_COMPANY_UID_DATA, companyUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_BOARD_UID_DATA) as Int
    }

    private fun getCompanyUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_COMPANY_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainImageAdapter = StoreMainImageAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_job_detail, container, false)

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
            setShareUrl(mActivity, COMPANY_TYPE_JOB_AND_JOB_SEARCH, mBoardData.getBoardUid())
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
        titleTextView.text = mActivity.resources.getString(R.string.text_job_and_job_search)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Job And Job Search Text View
        mJobTypeTextView = mContainer.fragment_job_detail_job_and_job_search_text_view

        // Job Title Text View
        mJobTitleTextView = mContainer.fragment_job_detail_title_text_view


        // Date Text View
        mDateTextView = mContainer.fragment_job_detail_date_text_view

        // View Count Text View
        mViewCountTextView = mContainer.fragment_job_detail_view_count_text_view

        // Download Container View
        mDownloadContainerView = mContainer.fragment_job_detail_download_container_view

        // Download View
        val downloadView = mContainer.fragment_job_detail_download_view
        downloadView.setOnSingleClickListener {

            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                if (!TextUtils.isEmpty(mBoardData.getBoardFileListData()[0].getBoardFileName())) {
                    try {
                        val uri = Uri.parse(DEFAULT_IMAGE_DOMAIN + mBoardData.getBoardFileListData()[0].getBoardFileName())
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        NagajaLog().e("wooks, You don't have any browser to open web page")
                    }
                }
            }
        }

        // Download File Name Text View
        mDownloadFileNameTextView = mContainer.fragment_job_detail_download_file_name_text_view

        // Image Recycler View
        mImageRecyclerView = mContainer.fragment_job_detail_image_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        mMainImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mMainImageList, position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mMainImageAdapter

        // Content Text View
        mContentTextView = mContainer.fragment_job_detail_content_text_view

        // Chat Text View
        mChatTextView = mContainer.fragment_job_detail_chat_text_view
        mChatTextView.setOnClickListener {
            if (MAPP.IS_NON_MEMBER_SERVICE) {
                showLoginGuideCustomDialog()
            } else {
                var isMemberChat = true
                var targetUid = 0

                if (mBoardData.getMemberUid() > 0 && mBoardData.getCompanyUid() == 0) {
                    isMemberChat = true
                    targetUid = mBoardData.getMemberUid()
                } else if (mBoardData.getCompanyUid() > 0) {
                    isMemberChat = false
                    targetUid = mBoardData.getCompanyUid()
                }

                NagajaLog().e("wooks, isMemberChat = $isMemberChat")
                NagajaLog().e("wooks, targetUid = $targetUid")

                val chatData = "[{${MAPP.USER_DATA.getMemberUid()}, 0}, {${if (isMemberChat) "$targetUid, 0" else "${mBoardData.getMemberUid()}, $targetUid"}}]"
                if (!TextUtils.isEmpty(chatData)) {
                    mListener.onChatViewScreen(CHAT_CLASS_JOB, chatData)
                }
            }
        }

        // Modify Text View
        mModifyTextView = mContainer.fragment_job_detail_modify_text_view
        mModifyTextView.setOnClickListener {
            mListener.onJobModifyScreen(mBoardData.getParentCategoryUid(), mBoardData.getBoardUid(), getCompanyUid())
        }

        // Back Text View
        mBackTextView = mContainer.fragment_job_detail_back_text_view
        mBackTextView.setOnSingleClickListener {
            mListener.onBack()
        }

        getBoardDetail()
    }

    private fun displayBoardData(data: BoardData) {

        // Job And Job Search Text View
        mJobTypeTextView.text = if (data.getParentCategoryUid() == SELECT_TYPE_JOB) mActivity.resources.getString(R.string.text_job) else mActivity.resources.getString(R.string.text_job_search)

        // Job Title Text View
        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            mJobTitleTextView.text = data.getBoardSubject()
        }

        // Date Text View
        if (!TextUtils.isEmpty(data.getCreateDate())) {
            var time = data.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        // View Count Text VIew
        if (data.getViewCount() > 0) {
            mViewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_board_view_count), data.getViewCount().toString())
        } else {
            mViewCountTextView.text = String.format(mActivity.resources.getString(R.string.text_board_view_count), "0")
        }

        // Download Container View
        if (data.getBoardFileListData().size > 0) {
            mDownloadContainerView.visibility = View.VISIBLE
            if (!TextUtils.isEmpty(data.getBoardFileListData()[0].getBoardFileOrigin())) {
                mDownloadFileNameTextView.text = data.getBoardFileListData()[0].getBoardFileOrigin()
            }
        } else {
            mDownloadContainerView.visibility = View.GONE
        }

        // Image Recycler View
        mMainImageList.clear()
        for (i in mBoardData.getBoardImageListData().indices) {
            mMainImageList.add(DEFAULT_IMAGE_DOMAIN + mBoardData.getBoardImageListData()[i].getItemImageName())
        }
        mMainImageAdapter.setData(mMainImageList)

        // Content Text View
        if (!TextUtils.isEmpty(data.getBoardContent())) {
            mContentTextView.text = data.getBoardContent()
        }

        // Chat Text View
        if (getCompanyUid() > 0) {
            if (getCompanyUid() == data.getCompanyUid()) {
                mChatTextView.visibility = View.GONE
                mModifyTextView.visibility = View.VISIBLE
            } else {
                mChatTextView.visibility = View.VISIBLE
                mModifyTextView.visibility = View.GONE
            }
        } else {
            if (data.getCompanyUid() > 0) {
                if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) {
                    mChatTextView.visibility = View.GONE
                    mModifyTextView.visibility = View.GONE
                } else {
                    mChatTextView.visibility = View.VISIBLE
                    mModifyTextView.visibility = View.GONE
                }
            } else {
                if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) {
                    mChatTextView.visibility = View.GONE
                    mModifyTextView.visibility = View.VISIBLE
                } else {
                    mChatTextView.visibility = View.VISIBLE
                    mModifyTextView.visibility = View.GONE
                }
            }
        }
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
        if (context is OnJobDetailFragmentListener) {
            mActivity = context as Activity

            if (context is OnJobDetailFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnJobDetailFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnJobDetailFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnJobDetailFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // ==============================================================================================================================
    // API
    // ==============================================================================================================================

    /**
     * API. Get Board Detail
     */
    fun getBoardDetail() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getBoardMissingAndJobDetail(
            mRequestQueue,
            object : NagajaManager.RequestListener<BoardData> {
                override fun onSuccess(resultData: BoardData) {
                    mBoardData = resultData
                    displayBoardData(resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            getBoardUid(),
            false
        )
    }

}