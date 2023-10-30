package com.nagaja.app.android.Note

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Data.NoteData
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Adapter.NoteBoxAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.CompanyDefaultData
import com.nagaja.app.android.Data.NoteDetailData
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.NetworkUtil.NetworkManager
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_faq.view.*
import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.fragment_recommend_store_write.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*

class NoteFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mCompanyInformationView: View
    private lateinit var mAllView: View
    private lateinit var mSendBoxView: View
    private lateinit var mReceiveBoxView: View
    private lateinit var mAllUnderLineView: View
    private lateinit var mSendBoxUnderLineView: View
    private lateinit var mReceiveBoxUnderLineView: View
    private lateinit var mLoadingView: View
    private lateinit var mDeleteNoteView: View
    private lateinit var mEmptyListView: View

    private lateinit var mCompanyImageView: SimpleDraweeView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mCompanyNameTextView: TextView
    private lateinit var mCompanyTypeTextView: TextView
    private lateinit var mCompanyManagerTextView: TextView
    private lateinit var mCompanyAddressTextView: TextView

    private lateinit var mAllTextView: TextView
    private lateinit var mSendBoxTextView: TextView
    private lateinit var mReceiveBoxTextView: TextView
    private lateinit var mStartDateTextView: TextView
    private lateinit var mEndDateTextView: TextView
    private lateinit var mDeleteTextView: TextView
    private lateinit var mDeleteCountTextView: TextView

    private lateinit var mNoteRecyclerView: RecyclerView

    private lateinit var mNoteBoxAdapter: NoteBoxAdapter

    private var mNoteListData: ArrayList<NoteData> = ArrayList()
    private var mReceiveNoteListData: ArrayList<NoteData> = ArrayList()
    private var mSendNoteListData: ArrayList<NoteData> = ArrayList()

    private lateinit var mListener: OnNoteFragmentListener

    private lateinit var mLoadingLottieView: LottieAnimationView

    private lateinit var mRequestQueue: RequestQueue

    private var mSelectStartDate = ""
    private var mSelectEndDate = ""

    private var mIsFirst = true
    private var mIsEndOfScroll = false
    private var mIsPause = false

    private var mPageCount = 1
    private var mCategoryUid = 0
    private var mItemTotalCount = 0
    private var mSelectTab = 0
    private var mCompanyUid = 0

    private var mDeleteNoteUidList = ArrayList<Int>()
    private var mDeleteNoteUidString = ""

    interface OnNoteFragmentListener {
        fun onBack()
        fun onNoteDetailScreen(noteBoxData: NoteData, isCompanyNote: Boolean, companyUid: Int)
        fun onReplyScreen()
        fun onDefaultLoginScreen()
        fun onChangeLocationAndNotificationScreen(selectType : Int)
    }

    companion object {

        const val SELECT_TYPE_ALL                        = 0x01
        const val SELECT_TYPE_SEND_BOX                   = 0x02
        const val SELECT_TYPE_RECEIVE_BOX                = 0x03

        const val SELECT_CALENDAR_PICKER_TYPE_START      = 0x01
        const val SELECT_CALENDAR_PICKER_TYPE_END        = 0x02

        const val ARGS_COMPANY_DEFAULT_DATA              = "args_company_default_data"
        const val ARGS_IS_COMPANY_NOTE_DATA              = "args_is_company_note_data"
        const val ARGS_NOTE_BOARD_UID_DATA               = "args_note_board_uid_data"

        fun newInstance(isCompanyNote: Boolean, boardUid: Int): NoteFragment {
            val args = Bundle()
            val fragment = NoteFragment()
            args.putBoolean(ARGS_IS_COMPANY_NOTE_DATA, isCompanyNote)
            args.putInt(ARGS_NOTE_BOARD_UID_DATA, boardUid)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(companyDefaultData: CompanyDefaultData, isCompanyNote: Boolean, boardUid: Int): NoteFragment {
            val args = Bundle()
            val fragment = NoteFragment()
            args.putSerializable(ARGS_COMPANY_DEFAULT_DATA, companyDefaultData)
            args.putBoolean(ARGS_IS_COMPANY_NOTE_DATA, isCompanyNote)
            args.putInt(ARGS_NOTE_BOARD_UID_DATA, boardUid)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getCompanyDefaultData(): CompanyDefaultData? {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_COMPANY_DEFAULT_DATA) as CompanyDefaultData?
    }

    private fun getIsCompanyNote(): Boolean {
        val arguments = arguments
        return arguments?.getBoolean(ARGS_IS_COMPANY_NOTE_DATA) as Boolean
    }

    private fun getNoteBoardUid(): Int {
        val arguments = arguments
        return arguments?.getInt(ARGS_NOTE_BOARD_UID_DATA) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mNoteBoxAdapter = NoteBoxAdapter(mActivity, getIsCompanyNote(), if (null == getCompanyDefaultData()) 0 else getCompanyDefaultData()!!.getCompanyUid())

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_note, container, false)

        init()

        return mContainer
    }

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
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = if (!getIsCompanyNote()) mActivity.resources.getString(R.string.text_note_box) else mActivity.resources.getString(R.string.text_company_note)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onBack(   )
        }

        // Swipe Refresh Layout
        mSwipeRefreshLayout = mContainer.fragment_note_swipe_view
        mSwipeRefreshLayout.setOnRefreshListener {
            if (NetworkManager.checkNetworkState(requireActivity())) {
                mIsEndOfScroll = true
                mPageCount = 1

                mIsFirst = true

                mItemTotalCount = 0

                mSelectStartDate = ""
                mSelectEndDate = ""
                mStartDateTextView.text = mActivity.resources.getString(R.string.text_start_date)
                mEndDateTextView.text = mActivity.resources.getString(R.string.text_end_date)

                mDeleteNoteUidList.clear()

                getNoteList(true)
            } else {
                mSwipeRefreshLayout.isRefreshing = false
                showToast(mActivity, mActivity.resources.getString(R.string.text_message_network_disconnect))
            }
        }

        // Company Information View
        mCompanyInformationView = mContainer.fragment_note_company_information_view
        mCompanyInformationView.visibility = if (getIsCompanyNote()) View.VISIBLE else View.GONE

        // Company Image View
        mCompanyImageView = mContainer.fragment_note_company_image_view

        // Company Name Text View
        mCompanyNameTextView = mContainer.fragment_note_company_name_text_view

        // Company Type Txt View
        mCompanyTypeTextView = mContainer.fragment_note_company_type_text_view

        // Company Manager Text View
        mCompanyManagerTextView = mContainer.fragment_note_company_manager_text_view

        // Company Address Text View
        mCompanyAddressTextView = mContainer.fragment_note_company_address_text_view


        // All View
        mAllView = mContainer.fragment_note_all_view
        mAllView.setOnClickListener {
            setTabView(SELECT_TYPE_ALL, false)
        }

        // All Text View
        mAllTextView = mContainer.fragment_note_all_view_text_view

        // All Under Line View
        mAllUnderLineView = mContainer.fragment_note_all_view_under_line_view

        // Send Box View
        mSendBoxView = mContainer.fragment_note_send_box_view
        mSendBoxView.setOnClickListener {
            setTabView(SELECT_TYPE_SEND_BOX, false)
        }

        // Send Box Text View
        mSendBoxTextView = mContainer.fragment_note_send_box_text_view

        // Send Box Under Line View
        mSendBoxUnderLineView = mContainer.fragment_note_send_box_under_line_view

        // Receive Box View
        mReceiveBoxView = mContainer.fragment_note_receive_box_view
        mReceiveBoxView.setOnClickListener {
            setTabView(SELECT_TYPE_RECEIVE_BOX, false)
        }

        // Receive Box Text View
        mReceiveBoxTextView = mContainer.fragment_note_receive_box_text_view

        // Receive Box Under Line View
        mReceiveBoxUnderLineView = mContainer.fragment_note_receive_box_under_line_view

        // Start Date View
        val startDateView = mContainer.fragment_note_start_date_view
        startDateView.setOnClickListener {
            showDatePickerDialog(SELECT_CALENDAR_PICKER_TYPE_START)
        }

        // Start Date Text View
        mStartDateTextView = mContainer.fragment_note_start_date_text_view

        // End Date View
        val endDateView = mContainer.fragment_note_end_date_view
        endDateView.setOnClickListener {
            showDatePickerDialog(SELECT_CALENDAR_PICKER_TYPE_END)
        }

        // End Date Text View
        mEndDateTextView = mContainer.fragment_note_end_date_text_view

        // Note Recycler View
        mNoteRecyclerView = mContainer.fragment_note_recycler_view
        mNoteRecyclerView.setHasFixedSize(true)
        mNoteRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        mNoteRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!mNoteRecyclerView.canScrollVertically(1) && !mNoteRecyclerView.canScrollVertically(-1)) {
                    return
                }

                if (!mNoteRecyclerView.canScrollVertically(1)) {
                    if (!mIsEndOfScroll) {
                        if (mNoteListData.size >= mItemTotalCount) {
                            return
                        }

                        mIsEndOfScroll = true
                        getNoteList(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mNoteBoxAdapter.setOnNoteBoxClickListener(object : NoteBoxAdapter.OnNoteBoxClickListener {

            override fun onClick(data: NoteData) {
                mListener.onNoteDetailScreen(data, getIsCompanyNote(), mCompanyUid)
            }

            override fun onCheck(data: NoteData, position: Int, isChecked: Boolean) {
                mNoteListData[position].setIsCheck(isChecked)
                mNoteBoxAdapter.setItemChecked(position, isChecked)

                if (isChecked) {
                    mDeleteNoteUidList.add(data.getNoteUid())
                } else {
                    for (i in mDeleteNoteUidList.indices) {
                        if (mDeleteNoteUidList[i] == data.getNoteUid()) {
                            mDeleteNoteUidList.removeAt(i)
                            break
                        }
                    }
                }

//                showDeleteCount()
            }
        })
        mNoteRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mNoteRecyclerView.adapter = mNoteBoxAdapter

        // Delete Note View
        mDeleteNoteView = mContainer.fragment_note_delete_view
        mDeleteNoteView.setOnClickListener {
            if (mDeleteNoteUidList.isEmpty()) {
                return@setOnClickListener
            }

            showDeletePopup()
        }

        // Delete Text View
        mDeleteTextView = mContainer.fragment_note_delete_text_view


        // Delete Count Text View
        mDeleteCountTextView = mContainer.fragment_note_delete_count_text_view

        // Reply Text View
        val replyTextView = mContainer.fragment_note_reply_text_view
        replyTextView.setOnClickListener {
            mListener.onReplyScreen()
        }


        // Loading View
        mLoadingView = mContainer.fragment_note_loading_view

        // Loading Lottie View
        mLoadingLottieView = mContainer.fragment_note_loading_lottie_view

        // Empty List View
        mEmptyListView = mContainer.fragment_note_list_empty_view




        if (mIsPause) {
            mIsPause = false
        } else {
            if (getIsCompanyNote()) {
                setCompanyData()
            }


            setTabView(SELECT_TYPE_ALL, true)

            getNoteList(true)

            if (getNoteBoardUid() > 0) {
                getNoteDetailData()
            }
        }
    }

    private fun setTabView(selectType: Int, isFirst: Boolean) {

        when (selectType) {
            SELECT_TYPE_ALL -> {
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mAllTextView.setTypeface(mAllTextView.typeface, Typeface.BOLD)
                mAllUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mSendBoxTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mSendBoxTextView.setTypeface(mSendBoxTextView.typeface, Typeface.NORMAL)
                mSendBoxUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mReceiveBoxTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mReceiveBoxTextView.setTypeface(mReceiveBoxTextView.typeface, Typeface.NORMAL)
                mReceiveBoxUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                if (!isFirst) {
                    mNoteBoxAdapter.setData(mNoteListData)
                    mNoteRecyclerView.scrollToPosition(0)
                }
            }

            SELECT_TYPE_SEND_BOX -> {
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mAllTextView.setTypeface(mAllTextView.typeface, Typeface.NORMAL)
                mAllUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mSendBoxTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mSendBoxTextView.setTypeface(mSendBoxTextView.typeface, Typeface.BOLD)
                mSendBoxUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mReceiveBoxTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mReceiveBoxTextView.setTypeface(mReceiveBoxTextView.typeface, Typeface.NORMAL)
                mReceiveBoxUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                if (!isFirst) {
                    mNoteBoxAdapter.setData(mSendNoteListData)
                    mNoteRecyclerView.scrollToPosition(0)
                }
            }

            SELECT_TYPE_RECEIVE_BOX -> {
                mAllTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mAllTextView.setTypeface(mAllTextView.typeface, Typeface.NORMAL)
                mAllUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mSendBoxTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mSendBoxTextView.setTypeface(mSendBoxTextView.typeface, Typeface.NORMAL)
                mSendBoxUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mReceiveBoxTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mReceiveBoxTextView.setTypeface(mReceiveBoxTextView.typeface, Typeface.BOLD)
                mReceiveBoxUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                if (!isFirst) {
                    mNoteBoxAdapter.setData(mReceiveNoteListData)
                    mNoteRecyclerView.scrollToPosition(0)
                }
            }
        }

        mSelectTab = selectType
    }

    @SuppressLint("SetTextI18n")
    private fun setCompanyData() {
        if (!TextUtils.isEmpty(getCompanyDefaultData()!!.getCompanyMainImageUrl())) {
            mCompanyImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + getCompanyDefaultData()!!.getCompanyMainImageUrl()))
        }

        if (!TextUtils.isEmpty(getCompanyDefaultData()!!.getCompanyName())) {
            mCompanyNameTextView.text = getCompanyDefaultData()!!.getCompanyName()
        }

        if (getCompanyDefaultData()!!.getCategoryUid() > 0) {
            mCompanyTypeTextView.text = Util().getCompanyType(mActivity, getCompanyDefaultData()!!.getCategoryUid())
        }

        if (!TextUtils.isEmpty(getCompanyDefaultData()!!.getManageName())) {
            mCompanyManagerTextView.text = getCompanyDefaultData()!!.getManageName()
        }

        if (!TextUtils.isEmpty(getCompanyDefaultData()!!.getCompanyAddress())) {
            mCompanyAddressTextView.text = getCompanyDefaultData()!!.getCompanyAddress() + " " + getCompanyDefaultData()!!.getCompanyAddressDetail()
        }

        mCompanyUid = getCompanyDefaultData()!!.getCompanyUid()
    }

    @SuppressLint("SetTextI18n")
    private fun showDeleteCount() {
        if (mDeleteNoteUidList.isEmpty()) {
            mDeleteCountTextView.visibility = View.GONE
        } else {
            mDeleteCountTextView.visibility = View.VISIBLE
            mDeleteCountTextView.text = " (" + mDeleteNoteUidList.size.toString() + ")"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog(type: Int) {

        val calendar: Calendar = Calendar.getInstance()
        DatePickerDialog(
            mActivity,
            R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val currentDate = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)

                    val afterMonth = if (monthOfYear + 1 < 10) "0" + (monthOfYear + 1) else (monthOfYear + 1).toString()
                    val afterDay = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                    when (type) {
                        SELECT_CALENDAR_PICKER_TYPE_START -> {
                            mStartDateTextView.text = "$year-$afterMonth-$afterDay"
                            mSelectStartDate = "$year-$afterMonth-$afterDay"

                            getNoteList(true)
                        }

                        SELECT_CALENDAR_PICKER_TYPE_END -> {
                            mEndDateTextView.text = "$year-$afterMonth-$afterDay"
                            mSelectEndDate = "$year-$afterMonth-$afterDay"

                            getNoteList(true)
                        }
                    }
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    private fun showDeletePopup() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,
            mActivity.resources.getString(R.string.text_delete_note),
            mActivity.resources.getString(R.string.text_message_delete_note),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                customDialog.dismiss()

                getNoteDelete()
            }
        })
        customDialog.show()
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

    private fun setLoadingView(isLoading: Boolean) {
        if (isLoading) {
            mLoadingView.visibility = View.VISIBLE
            mLoadingLottieView.speed = 2f
            mLoadingLottieView.playAnimation()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                mLoadingView.visibility = View.GONE
                mLoadingLottieView.pauseAnimation()
            }, 1000)
        }
    }

    override fun onPause() {
        super.onPause()

        mIsPause = true

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
        if (context is OnNoteFragmentListener) {
            mActivity = context as Activity

            if (context is OnNoteFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNoteFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNoteFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNoteFragmentListener"
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
     * API. Get Store Category Data
     */
    fun getNoteList(isRefresh: Boolean) {

        if (getIsCompanyNote() && TextUtils.isEmpty(mCompanyNameTextView.text)) {
            setCompanyData()
        }

        setLoadingView(true)

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteList(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<NoteData>> {
                override fun onSuccess(resultData: ArrayList<NoteData>) {

                    setLoadingView(false)
                    mSwipeRefreshLayout.isRefreshing = false

                    if (resultData.isEmpty()) {
                        mItemTotalCount = 0
                        mNoteBoxAdapter.setData(resultData)

                        mNoteRecyclerView.visibility = View.GONE
                        mEmptyListView.visibility = View.VISIBLE

                        return
                    } else {
                        mNoteRecyclerView.visibility = View.VISIBLE
                        mEmptyListView.visibility = View.GONE
                    }

                    if (isRefresh) {
                        mNoteListData.clear()
                        mNoteListData = resultData

                        mIsFirst = false
                    } else {
                        if (mNoteListData.size == 0) {
                            mNoteListData = resultData
                        } else {
                            mNoteListData.addAll(resultData)
                        }
                    }

                    mItemTotalCount = mNoteListData[0].getTotalCount()

                    mIsEndOfScroll = false
//                    mNoteBoxAdapter.setData(mNoteListData)


                    mReceiveNoteListData.clear()
                    mSendNoteListData.clear()
                    mDeleteNoteUidList.clear()

                    mDeleteNoteUidString = ""


                    for (i in mNoteListData.indices) {
                        if (getIsCompanyNote()) {
                            if (mNoteListData[i].getReceiveCompanyUid() == getCompanyDefaultData()!!.getCompanyUid()) {
                                mReceiveNoteListData.add(mNoteListData[i])
                            } else if (mNoteListData[i].getCompanyUid() == getCompanyDefaultData()!!.getCompanyUid()) {
                                mSendNoteListData.add(mNoteListData[i])
                            }
                        } else {
                            if (mNoteListData[i].getReceiveMemberUid() == MAPP.USER_DATA.getMemberUid()) {
                                mReceiveNoteListData.add(mNoteListData[i])
                            } else if (mNoteListData[i].getSendMemberUid() == MAPP.USER_DATA.getMemberUid()) {
                                mSendNoteListData.add(mNoteListData[i])
                            }
                        }
                    }

                    if (mSelectTab == 0) {
                        mSelectTab = SELECT_TYPE_ALL
                    }

                    setTabView(mSelectTab, false)

                    mPageCount++

                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mSelectStartDate,
            mSelectEndDate,
            SharedPreferencesUtil().getSaveNationCode(mActivity),
            getIsCompanyNote(),
            mCompanyUid
        )
    }

    /**
     * API. Get Note Delete
     */
    private fun getNoteDelete() {

        for (i in mDeleteNoteUidList.indices) {
            if (i == 0) {
                mDeleteNoteUidString = mDeleteNoteUidList[i].toString()
            } else {
                mDeleteNoteUidString = mDeleteNoteUidString + "," + mDeleteNoteUidList[i].toString()
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteDelete(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {

                    getNoteList(true)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                    setLoadingView(false)
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                    setLoadingView(false)
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mDeleteNoteUidString,
            if (null == getCompanyDefaultData()) 0 else getCompanyDefaultData()!!.getCompanyUid(),
            getIsCompanyNote()
        )
    }

    /**
     * API. Get Note Detail Data
     */
    private fun getNoteDetailData() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getNoteDetailData(
            mRequestQueue,
            object : NagajaManager.RequestListener<NoteDetailData> {
                override fun onSuccess(resultData: NoteDetailData) {

                    val noteData = NoteData()
                    noteData.setNoteUid(resultData.getNoteUid())
                    noteData.setIsSender(false)

                    mListener.onNoteDetailScreen(noteData, getIsCompanyNote(), mCompanyUid)
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
            getNoteBoardUid(),
            getIsCompanyNote(),
            mCompanyUid
        )
    }
}