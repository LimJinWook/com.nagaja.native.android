package com.nagaja.app.android.Note

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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Data.NoteData
import com.nagaja.app.android.Data.NoteDetailData
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_VIEW
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.fragment_note_detail.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class NoteDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mLoadingView: View
    private lateinit var mStatusView: View
    private lateinit var mDownloadView: View
    private lateinit var mReportView: View
    private lateinit var mRegularView: View
    private lateinit var mKeepView: View

    private lateinit var mSenderTextView: TextView
    private lateinit var mSenderNameTextView: TextView
    private lateinit var mSendDateTextView: TextView
    private lateinit var mMessageTextView: TextView
    private lateinit var mReplyTextView: TextView

    private lateinit var mReportIconImageView: ImageView
    private lateinit var mRegularIconImageView: ImageView
    private lateinit var mKeepIconImageView: ImageView

    private lateinit var mProfileImageView: SimpleDraweeView

    private lateinit var mDownloadRecyclerView: RecyclerView
    private lateinit var mImageRecyclerView: RecyclerView

    private lateinit var mMainImageAdapter: StoreReviewUploadImageAdapter
    private lateinit var mFileDownloadAdapter: FileDownloadAdapter

    private lateinit var mListener: OnNoteDetailFragmentListener

    private var mIsDownloading = false

    private lateinit var mRequestQueue: RequestQueue

    private val mMainImageList = ArrayList<String>()
    private val mOriginImageList = ArrayList<String>()

    private var mNoteDetailData = NoteDetailData()

    interface OnNoteDetailFragmentListener {
        fun onBack()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onReplyScreen(data: NoteData)
        fun onReportScreen(reportType: Int, noteUid: Int, storeName: String)
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val ARGS_NOTE_DATA                         = "args_note_data"
        const val ARGS_IS_COMPANY_NOTE_DATA              = "args_is_company_note_data"
        const val ARGS_COMPANY_UID                       = "args_company_uid"

        fun newInstance(data: NoteData, isCompanyNote: Boolean, companyUid: Int): NoteDetailFragment {
            val args = Bundle()
            val fragment = NoteDetailFragment()
            args.putSerializable(ARGS_NOTE_DATA, data)
            args.putBoolean(ARGS_IS_COMPANY_NOTE_DATA, isCompanyNote)
            args.putInt(ARGS_COMPANY_UID, companyUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getNoteData(): NoteData {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_NOTE_DATA) as NoteData
    }

    private fun getIsCompanyNote(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_COMPANY_NOTE_DATA) as Boolean
    }

    private fun getCompanyUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_COMPANY_UID) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainImageAdapter = StoreReviewUploadImageAdapter(mActivity)
        mFileDownloadAdapter = FileDownloadAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_note_detail, container, false)

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
        headerShareImageView.visibility = View.GONE
//        headerShareImageView.setOnSingleClickListener {
//        }

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
        backImageView.setOnSingleClickListener {
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_note)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Sender Text View
        mSenderTextView = mContainer.fragment_note_detail_sender_text_view

        // Profile Image View
        mProfileImageView = mContainer.fragment_note_detail_profile_image_view

        // Sender Name Text VIew
        mSenderNameTextView = mContainer.fragment_note_detail_sender_name_text_view

        // Send Date Text View
        mSendDateTextView = mContainer.fragment_note_detail_send_date_text_view

        // Message Text View
        mMessageTextView = mContainer.fragment_note_detail_message_text_view

        // Status View
        mStatusView = mContainer.fragment_note_detail_company_view

        // Report View
        mReportView = mContainer.fragment_note_detail_company_report_view
        mReportView.setOnClickListener {
            if (mNoteDetailData.getNoteUid() > 0) {
                var currentName = ""
                if (!TextUtils.isEmpty(mNoteDetailData.getNoteReceiverData().getReceiveCompanyName()) && TextUtils.isEmpty(mNoteDetailData.getNoteReceiverData().getReceiveMemberName())) {
                    currentName = mNoteDetailData.getNoteReceiverData().getReceiveCompanyName()
                } else if (TextUtils.isEmpty(mNoteDetailData.getNoteReceiverData().getReceiveCompanyName()) && !TextUtils.isEmpty(mNoteDetailData.getNoteReceiverData().getReceiveMemberName())) {
                    currentName = mNoteDetailData.getNoteReceiverData().getReceiveMemberName()
                }
                mListener.onReportScreen(REPORT_TYPE_NOTE, mNoteDetailData.getNoteUid(), currentName)
            }
        }

        // Report Icon Image View
        mReportIconImageView = mContainer.fragment_note_detail_company_report_icon_image_view

        // Regular View
        mRegularView = mContainer.fragment_note_detail_company_regular_view
        mRegularView.setOnClickListener {
            getStoreRegularSave(!mNoteDetailData.getNoteReceiverData().getIsReceiveRegular())
        }

        // Regular Icon Image View
        mRegularIconImageView = mContainer.fragment_note_detail_company_regular_icon_image_view


        // Keep View
        mKeepView = mContainer.fragment_note_detail_company_keep_view
        mKeepView.setOnClickListener {
            getNoteKeep(!mNoteDetailData.getNoteReceiverData().getIsReceiveKeep())
        }

        // Keep Icon Image View
        mKeepIconImageView = mContainer.fragment_note_detail_company_keep_icon_image_view

        // Download View
        mDownloadView = mContainer.fragment_note_detail_download_view

        // Download Recycler View
        mDownloadRecyclerView = mContainer.fragment_note_detail_download_recycler_view
        mDownloadRecyclerView.setHasFixedSize(true)
        mDownloadRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        mFileDownloadAdapter.setOnItemClickListener(object : FileDownloadAdapter.OnItemClickListener {
            override fun onClick(data: FileData) {
                if (!TextUtils.isEmpty(data.getBoardFileName())) {
                    try {
                        val uri = Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getBoardFileName())
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        NagajaLog().e("wooks, You don't have any browser to open web page")
                    }
                }
            }
        })
        mDownloadRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mDownloadRecyclerView.adapter = mFileDownloadAdapter

        // Recycler View
        mImageRecyclerView = mContainer.fragment_note_detail_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        mMainImageAdapter.setOnImageClickListener(object : StoreReviewUploadImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(mOriginImageList, position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mMainImageAdapter

        // Close Text View
        val closeTextView = mContainer.fragment_note_detail_close_text_view
        closeTextView.setOnSingleClickListener {
            mListener.onBack()
        }

        // Reply Text View
        mReplyTextView = mContainer.fragment_note_detail_reply_text_view
        mReplyTextView.setOnSingleClickListener {
            mListener.onReplyScreen(getNoteData())
        }






        getNoteDetailData()

    }

    @SuppressLint("SetTextI18n")
    private fun displayNoteDetail() {

        // Sender Name Text View
        var name = ""
        var time = ""
        var image = ""

        if (!getNoteData().getIsSender()) {

            mSenderTextView.text = mActivity.resources.getString(R.string.text_sender)

            mStatusView.visibility = View.VISIBLE

            if (mNoteDetailData.getNoteSenderData().getCompanyUid() > 0 && mNoteDetailData.getNoteSenderData().getMemberUid() == 0) {
                if (!TextUtils.isEmpty(mNoteDetailData.getNoteSenderData().getCompanyName())) {
                    name = mNoteDetailData.getNoteSenderData().getCompanyName()
                    mRegularView.visibility = View.VISIBLE
                }

                mReportIconImageView.setImageResource( if (mNoteDetailData.getNoteReceiverData().getIsReceiveReport()) R.drawable.icon_report_enable else R.drawable.icon_note_report )
                mRegularIconImageView.setImageResource( if (mNoteDetailData.getNoteReceiverData().getIsReceiveRegular()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2 )
                mKeepIconImageView.setImageResource( if (mNoteDetailData.getNoteReceiverData().getIsReceiveKeep()) R.drawable.icon_note_bookmark_enable else R.drawable.icon_note_bookmark )

            } else if (mNoteDetailData.getNoteSenderData().getCompanyUid() == 0 && mNoteDetailData.getNoteSenderData().getMemberUid() > 0) {
                if (!TextUtils.isEmpty(mNoteDetailData.getNoteSenderData().getMemberNickName())) {
                    name = mNoteDetailData.getNoteSenderData().getMemberNickName()
                    mRegularView.visibility = View.GONE
                }

                mReportIconImageView.setImageResource( if (mNoteDetailData.getNoteReceiverData().getIsReceiveReport()) R.drawable.icon_report_enable else R.drawable.icon_note_report )
                mKeepIconImageView.setImageResource( if (mNoteDetailData.getNoteReceiverData().getIsReceiveKeep()) R.drawable.icon_note_bookmark_enable else R.drawable.icon_note_bookmark )

            }

            mSenderNameTextView.text = name + " [" + Util().getEmailMasking(mNoteDetailData.getNoteSenderData().getEmail()) + "]"
            time = mNoteDetailData.getNoteSenderData().getCreateDate()
            image = mNoteDetailData.getNoteSenderData().getImageName()

        } else {

            mSenderTextView.text = mActivity.resources.getString(R.string.text_receiver_2)

            mStatusView.visibility = View.GONE

            if (mNoteDetailData.getNoteReceiverData().getReceiveCompanyUid() > 0 && mNoteDetailData.getNoteReceiverData().getReceiveMemberUid() == 0) {
                if (!TextUtils.isEmpty(mNoteDetailData.getNoteReceiverData().getReceiveCompanyName())) {
                    name = mNoteDetailData.getNoteReceiverData().getReceiveCompanyName()
                }

            } else if (mNoteDetailData.getNoteReceiverData().getReceiveCompanyUid() == 0 && mNoteDetailData.getNoteReceiverData().getReceiveMemberUid() > 0) {
                if (!TextUtils.isEmpty(mNoteDetailData.getNoteReceiverData().getReceiveMemberName())) {
                    name = mNoteDetailData.getNoteReceiverData().getReceiveMemberName()
                }
            }

            mSenderNameTextView.text = name + " [" + Util().getEmailMasking(mNoteDetailData.getNoteReceiverData().getEmail()) + "]"
            time = mNoteDetailData.getNoteReceiverData().getCreateDate()
            image = mNoteDetailData.getNoteReceiverData().getImageName()

            mReplyTextView.visibility = View.GONE
        }


        // Profile Image View
        mProfileImageView.setOnClickListener {
            if (!TextUtils.isEmpty(image)) {
                val imageList = ArrayList<String>()
                imageList.add(mNoteDetailData.getNoteReceiverData().getImageName())
                mListener.onFullScreenImage(imageList, 0)
            }
        }
        if (!TextUtils.isEmpty(image)) {
            mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + image))
        }

        // Send Date Text View
        if (!TextUtils.isEmpty(time)) {
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mActivity))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            mSendDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        // Message Text View
        if (!TextUtils.isEmpty(mNoteDetailData.getNoteMessage())) {
            mMessageTextView.text = mNoteDetailData.getNoteMessage()
        }

        // Download View
        if (mNoteDetailData.getNoteFileList().size > 0) {
            mDownloadView.visibility = View.VISIBLE
            mFileDownloadAdapter.setData(mNoteDetailData.getNoteFileList())
        } else {
            mDownloadView.visibility = View.GONE
        }

        // Image Recycler View
        if (mNoteDetailData.getNoteImageList().size > 0) {
            mImageRecyclerView.visibility = View.VISIBLE
            mMainImageList.clear()
            mOriginImageList.clear()
            for (i in mNoteDetailData.getNoteImageList().indices) {
                mMainImageList.add(DEFAULT_IMAGE_DOMAIN + IMAGE_VIEW + mNoteDetailData.getNoteImageList()[i].getItemImageName())
                mOriginImageList.add(DEFAULT_IMAGE_DOMAIN + mNoteDetailData.getNoteImageList()[i].getItemImageName())
            }
            mMainImageAdapter.setData(mMainImageList)
        } else {
            mImageRecyclerView.visibility = View.GONE
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

    fun successReport() {
        mReportIconImageView.setImageResource(R.drawable.icon_report_enable)
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
        if (context is OnNoteDetailFragmentListener) {
            mActivity = context as Activity

            if (context is OnNoteDetailFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNoteDetailFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNoteDetailFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNoteDetailFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // =================================================================================
    // API
    // =================================================================================

    /**
     * API. Get Note Detail Data
     */
    private fun getNoteDetailData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteDetailData(
            mRequestQueue,
            object : NagajaManager.RequestListener<NoteDetailData> {
                override fun onSuccess(resultData: NoteDetailData) {

                    mNoteDetailData = resultData

//                    getNoteDetailSenderData(resultData)
                    getNoteReceiver()
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
            getNoteData().getNoteUid(),
            getIsCompanyNote(),
            getCompanyUid()
        )
    }

    /**
     * API. Get Note Receiver Data
     */
    private fun getNoteReceiver() {
        var uid = 0
        if (!getIsCompanyNote()) {
            if (mNoteDetailData.getMemberUid() > 0 && mNoteDetailData.getCompanyUid() == 0) {
                uid = mNoteDetailData.getMemberUid()
            } else if (mNoteDetailData.getMemberUid() == 0 && mNoteDetailData.getCompanyUid() > 0) {
                uid = mNoteDetailData.getCompanyUid()
            }
        } else {
            uid = getCompanyUid()
        }


        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteReceiver(
            mRequestQueue,
            object : NagajaManager.RequestListener<NoteReceiverData> {
                override fun onSuccess(resultData: NoteReceiverData) {

                    mNoteDetailData.setNoteReceiverData(resultData)
                    getNoteSender(uid)
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
            mNoteDetailData.getNoteUid(),
            getIsCompanyNote(),
            uid
        )
    }

    /**
     * API. Get Note Receiver Data
     */
    private fun getNoteSender(uid: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteSender(
            mRequestQueue,
            object : NagajaManager.RequestListener<NoteSenderData> {
                override fun onSuccess(resultData: NoteSenderData) {

                    mNoteDetailData.setNoteSenderData(resultData)
                    displayNoteDetail()
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
            mNoteDetailData.getNoteUid(),
            getIsCompanyNote(),
            uid
        )
    }

//    /**
//     * API. Get Note Detail Data
//     */
//    private fun getNoteDetailSenderData(data: NoteDetailData) {
//
//        val nagajaManager = NagajaManager().getInstance()
//        nagajaManager?.getNoteDetailSenderData(
//            mRequestQueue,
//            object : NagajaManager.RequestListener<NoteDetailSenderData> {
//                override fun onSuccess(resultData: NoteDetailSenderData) {
//
//                    mNoteDetailData.setNoteDetailSenderData(resultData)
//
////                    if (data.getNoteDetailSenderData().getCompanyUid() > 0 || getCompanyUid() > 0) {
////                        getNoteCompanyStatusData()
////                    } else {
////                        displayNoteDetail()
////                    }
//
//                    getNoteCompanyStatusData()
//                }
//
//                override fun onFail(errorMsg: String?) {
//                    NagajaLog().e("wooks, errorMsg = $errorMsg")
//                }
//
//                override fun onFail(errorCode: Int?) {
//                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
//                        disConnectUserToken(mActivity)
//                    }
//                }
//            },
//            MAPP.USER_DATA.getSecureKey(),
//            getNoteData().getNoteUid(),
//            getIsCompanyNote(),
//            getCompanyUid()
//        )
//    }
//
//    /**
//     * API. Get Note Company Status Data
//     */
//    private fun getNoteCompanyStatusData() {
//
//        val nagajaManager = NagajaManager().getInstance()
//        nagajaManager?.getNoteCompanyStatusData(
//            mRequestQueue,
//            object : NagajaManager.RequestListener<NoteDetailCompanyStatusData> {
//                override fun onSuccess(resultData: NoteDetailCompanyStatusData) {
//
//                    mNoteDetailData.setNoteDetailCompanyStatusData(resultData)
//
//                    displayNoteDetail()
//                }
//
//                override fun onFail(errorMsg: String?) {
//                    NagajaLog().e("wooks, errorMsg = $errorMsg")
//                }
//
//                override fun onFail(errorCode: Int?) {
//                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
//                        disConnectUserToken(mActivity)
//                    }
//                }
//            },
//            MAPP.USER_DATA.getSecureKey(),
//            getNoteData().getNoteUid(),
//            if (getIsCompanyNote()) getCompanyUid() else mNoteDetailData.getCompanyUid(),
//            getIsCompanyNote()
//        )
//    }

    /**
     * API. Get Store Regular Save
     */
    private fun getStoreRegularSave(isRegular: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getStoreRegularSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mRegularIconImageView.setImageResource( if (isRegular) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2 )
                    mNoteDetailData.getNoteReceiverData().setIsReceiveRegular(isRegular)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mNoteDetailData.getCompanyUid(),
            if (isRegular) 1 else 0
        )
    }

    /**
     * API. Get Store Regular Save
     */
    private fun getNoteKeep(isKeep: Boolean) {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteKeep(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    mKeepIconImageView.setImageResource( if (isKeep) R.drawable.icon_note_bookmark_enable else R.drawable.icon_note_bookmark )
                    mNoteDetailData.getNoteReceiverData().setIsReceiveKeep(isKeep)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mNoteDetailData.getNoteUid(),
            if (isKeep) 1 else 0,
            getCompanyUid(),
            getIsCompanyNote()
        )
    }
}