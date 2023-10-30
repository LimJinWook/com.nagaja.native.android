package com.nagaja.app.android.Dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nagaja.app.android.Adapter.BreakTimeAdapter
import com.nagaja.app.android.Adapter.MemberSearchAdapter
import com.nagaja.app.android.Adapter.MonthOffDayAdapter
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.custom_dialog_sales_bottom.view.*
import kotlinx.android.synthetic.main.layout_custom_dialog_sales_bottom_break_time.view.*
import kotlinx.android.synthetic.main.layout_custom_dialog_sales_bottom_business_time.view.*
import kotlinx.android.synthetic.main.layout_custom_dialog_sales_bottom_day_off.view.*
import kotlinx.android.synthetic.main.layout_custom_dialog_sales_bottom_person_setting.view.*
import java.util.*


class CustomDialogSalesBottom(context: Context, dialogType: Int) : BottomSheetDialogFragment() {

    private lateinit var mContainer: View
    private lateinit var mPersonSetting: View
    private lateinit var mDayOffView: View
    private lateinit var mBusinessTimeView: View
    private lateinit var mBreakTimeView: View

    private lateinit var mRegisterTextView: TextView

    private lateinit var mTitleTextView: TextView
    private lateinit var mCancelImageView: ImageView

    private lateinit var mDayOffSpinner: PowerSpinnerView

    private lateinit var mWeekOffDayView: View
    private var mWeekOffDaySelectView = arrayOfNulls<View>(7)
    private var mWeekOffDaySelectTextView = arrayOfNulls<TextView>(7)


    private lateinit var mDoubleWeekOffDayView: View
    private var mDoubleWeekOffDaySelectView = arrayOfNulls<View>(14)
    private var mDoubleWeekOffDaySelectTextView = arrayOfNulls<TextView>(14)


    private lateinit var mMonthOffDayView: View
    private lateinit var mMonthOffDayRecyclerView: RecyclerView
    private lateinit var mMonthOffDayAdapter: MonthOffDayAdapter
    private var mMonthOffDayArrayList = ArrayList<MonthOffDayData>()
    private var mFindUserData = FindUserData()


    private lateinit var mPersonSettingMemberSpinner: PowerSpinnerView
    private lateinit var mMemberSearchTextView: TextView
    private lateinit var mMemberSearchEditText: EditText
    private lateinit var mMemberSearchRecyclerView: RecyclerView
    private lateinit var mMemberSearchAdapter: MemberSearchAdapter
    private var mFindUserListData = ArrayList<FindUserData>()



    private lateinit var mBreakTimeStartSpinner: PowerSpinnerView
    private lateinit var mBreakTimeEndSpinnerView: PowerSpinnerView
    private lateinit var mBreakTimeRecyclerView: RecyclerView
    private lateinit var mBreakTimeAdapter: BreakTimeAdapter
    private var mBreakTimeListData = ArrayList<BreakTimeData>()



    // Business Time
    private lateinit var mBusinessStartTimeSpinner: PowerSpinnerView
    private lateinit var mBusinessEndTimeSpinner: PowerSpinnerView



    private var mContext: Context

    private var mDialogType = 0
    private var mDayOffType = 0
    private var mPersonalSettingMemberType = 2
    private var mSelectManagerUid = 0

    private var mSelectOffDayPosition = ""
    private var mStartBusinessTime = ""
    private var mEndBusinessTime = ""
    private var mStartBreakTime = ""
    private var mEndBreakTime = ""

    private var mIsSelectFindUser = false
    private var mIsRegisterMember = false

    private var mDoubleWeekOffDayTypeArrayList = ArrayList<String>()

    private var mListener: OnCustomDialogBottomListener? = null

    private lateinit var mRequestQueue: RequestQueue

    fun setOnCustomDialogBottomListener(listener: OnCustomDialogBottomListener?) {
        mListener = listener
    }

    init {
        this.mContext = context
        this.mDialogType = dialogType
    }

    companion object {
        const val SALES_DIALOG_TYPE_PERSON_SETTING            = 0x01
        const val SALES_DIALOG_TYPE_DAY_OFF                   = 0x02
        const val SALES_DIALOG_TYPE_BUSINESS_TIME             = 0x03
        const val SALES_DIALOG_TYPE_BREAK_TIME                = 0x04

        const val DAY_OFF_TYPE_DAY_WEEK                       = 0x00
        const val DAY_OFF_TYPE_DOUBLE_WEEK_DAY                = 0x01
        const val DAY_OFF_TYPE_MONTH                          = 0x02

    }

    interface OnCustomDialogBottomListener {
        fun onRegister()
        fun onDisConnectUserToken()
        fun onSuccessManager()
        fun onSuccessBusinessTime()
        fun onSuccessBreakTime()
    }

    fun newInstance(context: Context, dialogType: Int): CustomDialogSalesBottom {
        val fragment = CustomDialogSalesBottom(context, dialogType)
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        mRequestQueue = Volley.newRequestQueue(mContext)

        mMonthOffDayAdapter = MonthOffDayAdapter(mContext)

        mMemberSearchAdapter = MemberSearchAdapter(mContext)

        mBreakTimeAdapter = BreakTimeAdapter(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainer = inflater.inflate(R.layout.custom_dialog_sales_bottom, container, false)
        init()
        return mContainer
    }

    private fun init() {

        // Title Text View
        mTitleTextView = mContainer.custom_dialog_sales_bottom_title_text_view

        // Cancel Image View
        mCancelImageView = mContainer.custom_dialog_sales_bottom_cancel_image_view
        mCancelImageView.setOnClickListener {
            dismiss()
        }

        // Person Setting View
        mPersonSetting = mContainer.custom_dialog_sales_bottom_person_setting_view

        // Day Off View
        mDayOffView = mContainer.custom_dialog_sales_bottom_day_off_view

        // Business Time View
        mBusinessTimeView = mContainer.custom_dialog_sales_bottom_business_hours_view

        // Break Time View
        mBreakTimeView = mContainer.custom_dialog_sales_bottom_business_break_time_view

        // Register Text View
        mRegisterTextView = mContainer.custom_dialog_sales_bottom_business_register_text_view
        mRegisterTextView.setOnClickListener {

            if (mDialogType == SALES_DIALOG_TYPE_PERSON_SETTING) {
                if (mIsSelectFindUser) {
                    if (mIsRegisterMember) {
//                        getCompanyMemberRemove(mSelectManagerUid)
                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_member_registered_member), Toast.LENGTH_SHORT).show()
                    } else {
                        getCompanyMemberRegister(mPersonalSettingMemberType, mFindUserData.getUid(), MAPP.COMPANY_SALES_DATA.getCompanyUid())
                    }

                }
            } else if (mDialogType == SALES_DIALOG_TYPE_BUSINESS_TIME) {
                if (!TextUtils.isEmpty(mStartBusinessTime) && !TextUtils.isEmpty(mEndBusinessTime)) {
                    getCompanyBusinessTime(mStartBusinessTime, mEndBusinessTime)
                }
            } else if (mDialogType == SALES_DIALOG_TYPE_BREAK_TIME) {
                if (!TextUtils.isEmpty(mStartBreakTime) && !TextUtils.isEmpty(mEndBreakTime)) {
                    if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime()) && !TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime())
                        && !TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime()) && !TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime())) {

                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_full_break_time), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime()) || TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime())
                        && TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime()) && TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime())) {

                        getCompanyBreakTime(mStartBreakTime, mEndBreakTime, MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime(), MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime(), false)
                    } else if ((!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime()) && !TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime()))
                        && (TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime()) || TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime()))) {

                        getCompanyBreakTime(MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime(), MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime(), mStartBreakTime, mEndBreakTime, false)
                    }
                }
            } else {
                mListener!!.onRegister()
                dismiss()
            }
        }

        setDialogType()
    }

    private fun setDialogType() {
        when (mDialogType) {
            SALES_DIALOG_TYPE_PERSON_SETTING -> {
                mTitleTextView.text = mContext.resources.getString(R.string.text_person_setting)

                mPersonSetting.visibility = View.VISIBLE
                mDayOffView.visibility = View.GONE
                mBusinessTimeView.visibility = View.GONE
                mBreakTimeView.visibility = View.GONE

                mRegisterTextView.visibility = View.INVISIBLE

                personalSetting()
            }

            SALES_DIALOG_TYPE_DAY_OFF -> {
                mTitleTextView.text = mContext.resources.getString(R.string.text_regular_holiday_setting)

                mPersonSetting.visibility = View.GONE
                mDayOffView.visibility = View.VISIBLE
                mBusinessTimeView.visibility = View.GONE
                mBreakTimeView.visibility = View.GONE

                dayOffSetting()
            }

            SALES_DIALOG_TYPE_BUSINESS_TIME -> {
                mTitleTextView.text = mContext.resources.getString(R.string.text_business_hours_setting)

                mPersonSetting.visibility = View.GONE
                mDayOffView.visibility = View.GONE
                mBusinessTimeView.visibility = View.VISIBLE
                mBreakTimeView.visibility = View.GONE

                businessTimeSetting()
            }

            SALES_DIALOG_TYPE_BREAK_TIME -> {
                mTitleTextView.text = mContext.resources.getString(R.string.text_break_time_setting)

                mPersonSetting.visibility = View.GONE
                mDayOffView.visibility = View.GONE
                mBusinessTimeView.visibility = View.GONE
                mBreakTimeView.visibility = View.VISIBLE

                breakTimeSetting()
            }
        }
    }

    private fun personalSetting() {

        // Person Setting Member Spinner
        mPersonSettingMemberSpinner = mContainer.layout_custom_dialog_sales_bottom_person_setting_member_spinner
        mPersonSettingMemberSpinner.selectItemByIndex(0)
        mPersonSettingMemberSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mPersonalSettingMemberType = newIndex + 2
        })

        // Search Edit Text
        mMemberSearchEditText = mContainer.layout_custom_dialog_sales_bottom_person_setting_member_search_edit_text
        mMemberSearchEditText.filters = arrayOf(Util().blankSpaceFilter)
        mMemberSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Member Search Recycler View
        mMemberSearchRecyclerView = mContainer.layout_custom_dialog_sales_bottom_person_setting_member_search_recycler_view
        mMemberSearchRecyclerView.visibility = View.GONE
        mMemberSearchRecyclerView.setHasFixedSize(true)
        mMemberSearchRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mMemberSearchAdapter.setOnItemClickListener(object : MemberSearchAdapter.OnItemClickListener {
            override fun onClick(data: FindUserData, position: Int) {
                mIsRegisterMember = false

                mFindUserData = data

                for (i in mFindUserListData.indices) {
                    if (i == position) {
                        mFindUserListData[i].setIsSelect(!mFindUserListData[i].getIsSelect())
                    } else {
                        mFindUserListData[i].setIsSelect(false)
                    }

                    if (mFindUserListData[i].getIsSelect()) {
                        mIsSelectFindUser = true
                    }
                }

                for (i in MAPP.COMPANY_MANAGER_LIST_DATA.indices) {
                    if (MAPP.COMPANY_MANAGER_LIST_DATA[i].getMemberUid() == mFindUserData.getUid()) {
                        mIsRegisterMember = true
                        mSelectManagerUid = MAPP.COMPANY_MANAGER_LIST_DATA[i].getCompanyManagerUid()
                    }
                }

//                if (mIsRegisterMember) {
//                    mRegisterTextView.setBackgroundResource(R.drawable.bg_button_radius_red)
//                    mRegisterTextView.text = mContext.resources.getString(R.string.text_delete)
//                } else {
//                    mRegisterTextView.setBackgroundResource(R.drawable.bg_button_radius_blue)
//                    mRegisterTextView.text = mContext.resources.getString(R.string.text_register)
//                }

                mRegisterTextView.visibility = if (mIsSelectFindUser) View.VISIBLE else View.INVISIBLE
                mMemberSearchAdapter.setData(mFindUserListData)

            }

        })
        mMemberSearchRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMemberSearchRecyclerView.adapter = mMemberSearchAdapter

        // Member Search Text View
        mMemberSearchTextView = mContainer.layout_custom_dialog_sales_bottom_person_setting_member_search_text_view
        mMemberSearchTextView.setOnClickListener {
            if (mMemberSearchEditText.text.isEmpty()) {
                return@setOnClickListener
            }

            getCompanyAndMemberSearch()
        }
    }

    @SuppressLint("ResourceType")
    private fun dayOffSetting() {

        if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceMonthOffDays()) && MAPP.COMPANY_SALES_DATA.getServiceMonthOffDays() != "null") {
            mDayOffType = DAY_OFF_TYPE_MONTH
        } else if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceWeekOffDays()) && MAPP.COMPANY_SALES_DATA.getServiceWeekOffDays() != "null") {
            mDayOffType = DAY_OFF_TYPE_DAY_WEEK
        } else if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceWeekDoubleOffDays()) && MAPP.COMPANY_SALES_DATA.getServiceWeekDoubleOffDays() != "null") {
            mDayOffType = DAY_OFF_TYPE_DOUBLE_WEEK_DAY
        }

        for (i in 0..14) {
            mDoubleWeekOffDayTypeArrayList.add((i + 1).toString())
        }

        // Day Off Spinner
        mDayOffSpinner = mContainer.layout_custom_dialog_sales_bottom_day_off_spinner
        mDayOffSpinner.selectItemByIndex(mDayOffType)
        mDayOffSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            displayOffDaySelect(newIndex)
        })









        setWeekOffDay()

        setDoubleWeekOffDay()

        setMonthOffDay()










        displayOffDaySelect(mDayOffType)


    }

    private fun businessTimeSetting() {

        // Business Start Time Spinner
        mBusinessStartTimeSpinner = mContainer.layout_custom_dialog_sales_bottom_business_time_start_spinner
        mBusinessStartTimeSpinner.selectItemByIndex(0)
        mBusinessStartTimeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mStartBusinessTime = newItem!!
        })

        // Business End Time Spinner
        mBusinessEndTimeSpinner = mContainer.layout_custom_dialog_sales_bottom_business_time_end_spinner
        mBusinessEndTimeSpinner.selectItemByIndex(1)
        mBusinessEndTimeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mEndBusinessTime = newItem!!
        })

        if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceBeginTime()) && MAPP.COMPANY_SALES_DATA.getServiceBeginTime() != "null") {
            val startTimeArrayList = mContext.resources.getStringArray(R.array.array_sales_time)
            for (i in startTimeArrayList.indices) {
                if (MAPP.COMPANY_SALES_DATA.getServiceBeginTime() == startTimeArrayList[i]) {
                    mBusinessStartTimeSpinner.selectItemByIndex(i)
                    break
                }
            }
        }

        if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceEndTime()) && MAPP.COMPANY_SALES_DATA.getServiceEndTime() != "null") {
            val endTimeArrayList = mContext.resources.getStringArray(R.array.array_sales_time)
            for (i in endTimeArrayList.indices) {
                if (MAPP.COMPANY_SALES_DATA.getServiceEndTime() == endTimeArrayList[i]) {
                    mBusinessEndTimeSpinner.selectItemByIndex(i)
                    break
                }
            }
        }
    }

    private fun breakTimeSetting() {

        // Break Time Start Spinner
        mBreakTimeStartSpinner = mContainer.layout_custom_dialog_sales_bottom_break_time_start_spinner
        mBreakTimeStartSpinner.selectItemByIndex(0)
        mBreakTimeStartSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mStartBreakTime = newItem!!
        })

        // Break Time End Spinner
        mBreakTimeEndSpinnerView = mContainer.layout_custom_dialog_sales_bottom_break_time_end_spinner
        mBreakTimeEndSpinnerView.selectItemByIndex(1)
        mBreakTimeEndSpinnerView.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mEndBreakTime = newItem!!
        })

        // Break Time Recycler View
        mBreakTimeRecyclerView = mContainer.layout_custom_dialog_sales_bottom_break_time_recycler_view
        mBreakTimeRecyclerView.setHasFixedSize(true)
        mBreakTimeRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mBreakTimeAdapter.setOnItemClickListener(object : BreakTimeAdapter.OnItemClickListener {
            override fun onClick(data: BreakTimeData) {
                val startTime = data.getStartTime() + ":00"
                val endTime = data.getEndTime() + ":00"

                if (startTime == MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime() && endTime == MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime()) {
                    getCompanyBreakTime("", "", MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime(), MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime(), true)
                } else if (startTime == MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime() && endTime == MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime()) {
                    getCompanyBreakTime(MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime(), MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime(), "", "", true)
                }
            }
        })
        mBreakTimeRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mBreakTimeRecyclerView.adapter = mBreakTimeAdapter


        if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime()) &&!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime())) {
            val breakTimeData = BreakTimeData()
            var startTime = MAPP.COMPANY_SALES_DATA.getFirstBreakBeginTime()
            var endTime = MAPP.COMPANY_SALES_DATA.getFirstBreakEndTime()

            if (startTime.length > 5) {
                startTime = startTime.substring(0, 5)
            }

            if (endTime.length > 5) {
                endTime = endTime.substring(0, 5)
            }

            breakTimeData.setStartTime(startTime)
            breakTimeData.setEndTime(endTime)

            mBreakTimeListData.add(breakTimeData)

        }

        if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime()) &&!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime())) {
            val breakTimeData = BreakTimeData()

            var startTime = MAPP.COMPANY_SALES_DATA.getSecondBreakBeginTime()
            var endTime = MAPP.COMPANY_SALES_DATA.getSecondBreakEndTime()

            if (startTime.length > 5) {
                startTime = startTime.substring(0, 5)
            }

            if (endTime.length > 5) {
                endTime = endTime.substring(0, 5)
            }

            breakTimeData.setStartTime(startTime)
            breakTimeData.setEndTime(endTime)

            mBreakTimeListData.add(breakTimeData)
        }

        mBreakTimeAdapter.setData(mBreakTimeListData)
    }

    private fun setWeekOffDay() {

        // Week Off Day View
        mWeekOffDayView = mContainer.layout_custom_dialog_sales_bottom_week_off_day_view

        for (i in 0..7) {
            when (i) {
                0 -> {
                    mWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_sunday_view
                    mWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DAY_WEEK, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_sunday_text_view
                }
                1 -> {
                    mWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_monday_view
                    mWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DAY_WEEK, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_monday_text_view
                }
                2 -> {
                    mWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_tuesday_view
                    mWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DAY_WEEK, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_tuesday_text_view
                }
                3 -> {
                    mWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_wednesday_view
                    mWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DAY_WEEK, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_wednesday_text_view
                }
                4 -> {
                    mWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_thursday_view
                    mWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DAY_WEEK, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_thursday_text_view
                }
                5 -> {
                    mWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_friday_view
                    mWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DAY_WEEK, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_friday_text_view
                }
                6 -> {
                    mWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_saturday_view
                    mWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DAY_WEEK, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_day_off_saturday_text_view
                }
            }
        }

        if (mDayOffType == DAY_OFF_TYPE_DAY_WEEK) {
            if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceWeekOffDays())) {
                val position = MAPP.COMPANY_SALES_DATA.getServiceWeekOffDays().toInt() - 1

                for (i in (mWeekOffDaySelectView.indices)) {
                    if (i == position) {
                        mWeekOffDaySelectView[i]!!.setBackgroundResource(R.drawable.bg_select_circle)
                        mWeekOffDaySelectTextView[i]!!.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_ffffff))
                    } else {
                        mWeekOffDaySelectView[i]!!.setBackgroundResource(R.drawable.bg_unselect_circle)
                        mWeekOffDaySelectTextView[i]!!.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_222222))
                    }
                }
            }
        }
    }




    private fun setDoubleWeekOffDay() {

        // Double Week Off Day View
        mDoubleWeekOffDayView = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_view

        for (i in 0..14) {
            when (i) {
                0 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_sunday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_sunday_text_view
                }
                1 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_monday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_monday_text_view
                }
                2 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_tuesday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_tuesday_text_view
                }
                3 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_wednesday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_wednesday_text_view
                }
                4 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_thursday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_thursday_text_view
                }
                5 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_friday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_friday_text_view
                }
                6 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_saturday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_first_saturday_text_view
                }
                7 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_sunday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_sunday_text_view
                }
                8 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_monday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_monday_text_view
                }
                9 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_tuesday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_tuesday_text_view
                }
                10 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_wednesday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_wednesday_text_view
                }
                11 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_thursday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_thursday_text_view
                }
                12 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_friday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_friday_text_view
                }
                13 -> {
                    mDoubleWeekOffDaySelectView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_saturday_view
                    mDoubleWeekOffDaySelectView[i]!!.setOnClickListener {
                        getCompanyDayOffChange(DAY_OFF_TYPE_DOUBLE_WEEK_DAY, mDoubleWeekOffDayTypeArrayList[i])
                    }

                    mDoubleWeekOffDaySelectTextView[i] = mContainer.layout_custom_dialog_sales_bottom_double_week_off_day_second_saturday_text_view
                }
            }
        }

        if (mDayOffType == DAY_OFF_TYPE_DOUBLE_WEEK_DAY) {
            if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceWeekDoubleOffDays())) {
                val position = MAPP.COMPANY_SALES_DATA.getServiceWeekDoubleOffDays().toInt() - 1

                for (i in mDoubleWeekOffDaySelectView.indices) {
                    if (i == position) {
                        mDoubleWeekOffDaySelectView[i]!!.setBackgroundResource(R.drawable.bg_select_circle)
                        mDoubleWeekOffDaySelectTextView[i]!!.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_ffffff))
                    } else {
                        mDoubleWeekOffDaySelectView[i]!!.setBackgroundResource(R.drawable.bg_unselect_circle)
                        mDoubleWeekOffDaySelectTextView[i]!!.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_222222))
                    }
                }
            }
        }
    }





    private fun setMonthOffDay() {

        // Month Off Day View
        mMonthOffDayView = mContainer.layout_custom_dialog_sales_bottom_month_off_day_view

        // Month Off Day Recycler View
        mMonthOffDayRecyclerView = mContainer.layout_custom_dialog_sales_bottom_month_off_day_recycler_view
        mMonthOffDayRecyclerView.setHasFixedSize(true)
        mMonthOffDayRecyclerView.layoutManager = GridLayoutManager(mContext,7)

        mMonthOffDayAdapter.setOnItemClickListener(object : MonthOffDayAdapter.OnItemClickListener {
            override fun onClick(date: String) {
                getCompanyDayOffChange(DAY_OFF_TYPE_MONTH, date)
            }
        })
        mMonthOffDayRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mMonthOffDayRecyclerView.adapter = mMonthOffDayAdapter

        if (mMonthOffDayArrayList.isEmpty()) {
            for (i in 0..30) {
                val monthOffDayData = MonthOffDayData()

                monthOffDayData.setDate((i + 1).toString())
                monthOffDayData.setIsSelect(false)

                if (!TextUtils.isEmpty(MAPP.COMPANY_SALES_DATA.getServiceMonthOffDays()) && MAPP.COMPANY_SALES_DATA.getServiceMonthOffDays() != "null") {
                    if (i == (MAPP.COMPANY_SALES_DATA.getServiceMonthOffDays().toInt() - 1)) {
                        monthOffDayData.setIsSelect(true)
                    }
                }

                mMonthOffDayArrayList.add(monthOffDayData)
            }

            mMonthOffDayAdapter.setData(mMonthOffDayArrayList)
        }
    }




    private fun displayOffDaySelect(position: Int) {

        when (position) {
            DAY_OFF_TYPE_DAY_WEEK -> {
                mWeekOffDayView.visibility = View.VISIBLE
                mDoubleWeekOffDayView.visibility = View.GONE
                mMonthOffDayView.visibility = View.GONE

                setDoubleWeekOffDay()
            }

            DAY_OFF_TYPE_DOUBLE_WEEK_DAY -> {
                mWeekOffDayView.visibility = View.GONE
                mDoubleWeekOffDayView.visibility = View.VISIBLE
                mMonthOffDayView.visibility = View.GONE

                setDoubleWeekOffDay()
            }

            DAY_OFF_TYPE_MONTH -> {
                mWeekOffDayView.visibility = View.GONE
                mDoubleWeekOffDayView.visibility = View.GONE
                mMonthOffDayView.visibility = View.VISIBLE

                setMonthOffDay()
            }
        }
    }


    // ==========================================================================================
    // API
    // ==========================================================================================

    /**
     * API. Get Company Manager Data
     */
    private fun getCompanySalesDefaultInformation() {
        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanySalesDefaultInformation(
            mRequestQueue,
            object : NagajaManager.RequestListener<CompanySalesData> {
                override fun onSuccess(resultData: CompanySalesData) {
                    MAPP.COMPANY_SALES_DATA = resultData
                    dayOffSetting()
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        mListener!!.onDisConnectUserToken()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            MAPP.COMPANY_SALES_DATA.getCompanyUid()
        )
    }

    /**
     * API. Get Company Information
     */
    private fun getCompanyAndMemberSearch() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getFindMember(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<FindUserData>> {
                override fun onSuccess(resultData: ArrayList<FindUserData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    mFindUserListData.clear()
                    mFindUserListData = resultData

                    mMemberSearchRecyclerView.visibility = if (mFindUserListData.isNotEmpty()) View.VISIBLE else View.GONE

                    mMemberSearchAdapter.setData(mFindUserListData)
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        mListener!!.onDisConnectUserToken()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            mMemberSearchEditText.text.toString()
        )
    }

    /**
     * API. Get Company Information
     */
//    private fun getCompanyDayOffChange(offDayType: Int, offList: ArrayList<String>) {
    private fun getCompanyDayOffChange(offDayType: Int, offDay: String) {
        if (TextUtils.isEmpty(offDay)) {
            return
        }

        var weekOffDayListString = ""
        var doubleWeekOffDayListString = ""
        var monthOffDayListString = ""

        when (offDayType) {
            DAY_OFF_TYPE_DAY_WEEK -> {
                weekOffDayListString = offDay
            }

            DAY_OFF_TYPE_DOUBLE_WEEK_DAY -> {
                doubleWeekOffDayListString = offDay
            }

            DAY_OFF_TYPE_MONTH -> {
                monthOffDayListString = offDay
            }
        }

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyDayOffChange(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    if (offDayType == DAY_OFF_TYPE_MONTH) {
                        for (i in mMonthOffDayArrayList.indices) {
                            if ((monthOffDayListString.toInt() - 1) == i ) {
                                mMonthOffDayArrayList[i].setIsSelect(true)
                            } else {
                                mMonthOffDayArrayList[i].setIsSelect(false)
                            }
                        }
                        mMonthOffDayAdapter.setData(mMonthOffDayArrayList)
                    }

                    getCompanySalesDefaultInformation()
                }

                override fun onFail(errorMsg: String?) {
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        mListener!!.onDisConnectUserToken()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            weekOffDayListString,
            doubleWeekOffDayListString,
            monthOffDayListString,
            MAPP.COMPANY_SALES_DATA.getCompanyServiceUid(),
            MAPP.COMPANY_SALES_DATA.getCompanyUid()
        )
    }

    /**
     * API. Get Company Member Register
     */
    private fun getCompanyMemberRegister(memberType: Int, memberUid: Int, companyUid: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyMemberRegister(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    Toast.makeText(mContext, String.format(mContext.resources.getString(R.string.text_message_register_member), mFindUserData.getName()), Toast.LENGTH_SHORT).show()

                    mListener!!.onSuccessManager()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        mListener!!.onDisConnectUserToken()
                    }

                    if (errorCode == ErrorRequest.ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_WRONG_GRANT) {
                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_error_wrong_grant), Toast.LENGTH_SHORT).show()
                    } else if (errorCode == ErrorRequest.ERROR_CODE_COMPANY_MEMBER_REGISTER_ERROR_MANAGER_LIMIT) {
                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_error_MANAGER_LIMIT), Toast.LENGTH_SHORT).show()
                    } else if (errorCode == ErrorRequest.ERROR_CODE_EXISTING_MANAGER) {
                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_error_existing_manager), Toast.LENGTH_SHORT).show()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            memberType,
            memberUid,
            companyUid
        )
    }

    /**
     * API. Get Company Member Register
     */
    private fun getCompanyMemberRemove(companyManagerUid: Int) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyMemberRemove(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    Toast.makeText(mContext, String.format(mContext.resources.getString(R.string.text_message_remove_member), mFindUserData.getName()), Toast.LENGTH_SHORT).show()

                    mListener!!.onSuccessManager()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        mListener!!.onDisConnectUserToken()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            companyManagerUid,
            MAPP.COMPANY_SALES_DATA.getCompanyUid()
        )
    }

    /**
     * API. Get Company Business Time
     */
    private fun getCompanyBusinessTime(startTime: String, endTIme: String) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyBusinessTime(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_register_business_time), Toast.LENGTH_SHORT).show()

                    MAPP.COMPANY_SALES_DATA.setServiceBeginTime(startTime)
                    MAPP.COMPANY_SALES_DATA.setServiceEndTime(endTIme)

                    mListener!!.onSuccessBusinessTime()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        mListener!!.onDisConnectUserToken()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            startTime,
            endTIme,
            MAPP.COMPANY_SALES_DATA.getCompanyServiceUid(),
            MAPP.COMPANY_SALES_DATA.getCompanyUid()
        )
    }

    /**
     * API. Get Company Break Time
     */
    private fun getCompanyBreakTime(firstStartTime: String, firstEndTime: String, secondStartTime: String, secondEndTime: String, isDelete: Boolean) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getCompanyBreakTime(
            mRequestQueue,
            object : NagajaManager.RequestListener<String> {
                override fun onSuccess(resultData: String) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    if (isDelete) {
                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_delete_break_time), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mContext, mContext.resources.getString(R.string.text_message_register_break_time), Toast.LENGTH_SHORT).show()
                    }

                    MAPP.COMPANY_SALES_DATA.setFirstBreakBeginTime(firstStartTime)
                    MAPP.COMPANY_SALES_DATA.setFirstBreakEndTime(firstEndTime)
                    MAPP.COMPANY_SALES_DATA.setSecondBreakBeginTime(secondStartTime)
                    MAPP.COMPANY_SALES_DATA.setSecondBreakEndTime(secondEndTime)

                    mListener!!.onSuccessBreakTime()
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().d("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        mListener!!.onDisConnectUserToken()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            firstStartTime,
            firstEndTime,
            secondStartTime,
            secondEndTime,
            MAPP.COMPANY_SALES_DATA.getCompanyServiceUid(),
            MAPP.COMPANY_SALES_DATA.getCompanyUid()
        )
    }
}