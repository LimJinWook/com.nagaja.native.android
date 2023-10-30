package com.nagaja.app.android.Dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.events.calendar.views.EventsCalendar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nagaja.app.android.Adapter.ReservationScheduleTimeAdapter
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_dialog_product_bottom.view.*
import kotlinx.android.synthetic.main.custom_dialog_reservation_date.view.*
import kotlinx.android.synthetic.main.fragment_missing.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class CustomDialogReservationDateSelectBottom(context: Context, companyUid: Int) : BottomSheetDialogFragment(), EventsCalendar.Callback {

    private lateinit var mContainer: View

    private lateinit var mContext: Context

    private lateinit var mRegisterTextView: TextView

    private lateinit var mCalendarView: EventsCalendar

    private lateinit var mScheduleRecyclerView: RecyclerView

    private lateinit var mScheduleTimeAdapter: ReservationScheduleTimeAdapter

    private var mListener: onSelectListener? = null

    private lateinit var mRequestQueue: RequestQueue

    private var mIsModify = false
    private var mIsSelect = false

    private var mSelectDay = ""

    private var mCompanyUid = 0
    private var mScheduleDayUid = 0

    private lateinit var mScheduleDayListData: ArrayList<ReservationScheduleDayData>

    private lateinit var mScheduleTimeData: ReservationScheduleTimeData

    fun setonSelectListener(listener: onSelectListener?) {
        mListener = listener
    }

    init {
        this.mContext = context
        this.mCompanyUid = companyUid
    }

    companion object {

    }

    interface onSelectListener {
        fun onSelectDateAndTime(data: ReservationScheduleTimeData)
    }

    fun newInstance(context: Context, companyUid: Int): CustomDialogReservationDateSelectBottom {
        val fragment = CustomDialogReservationDateSelectBottom(context, companyUid)
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }
        return bottomSheetDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        mScheduleTimeAdapter = ReservationScheduleTimeAdapter(mContext)

        mRequestQueue = Volley.newRequestQueue(mContext)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainer = inflater.inflate(R.layout.custom_dialog_reservation_date, container, false)

        init()

        return mContainer
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {

        // Cancel Image View
        val cancelImageView = mContainer.custom_dialog_reservation_date_cancel_image_view
        cancelImageView.setOnClickListener {
            dismiss()
        }

        // Calendar View
        mCalendarView = mContainer.custom_dialog_reservation_date_calendar_view

        // Recycler View
        mScheduleRecyclerView = mContainer.custom_dialog_reservation_date_recycler_view
        mScheduleRecyclerView.setHasFixedSize(true)
        mScheduleRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        mScheduleTimeAdapter.setOnItemClickListener(object : ReservationScheduleTimeAdapter.OnItemClickListener {
            override fun onClick(data: ReservationScheduleTimeData, position: Int) {
                mScheduleTimeAdapter.setSelectItem(position)
                mScheduleTimeData = data

                mIsSelect = true
            }
        })
        mScheduleRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mScheduleRecyclerView.adapter = mScheduleTimeAdapter

        // Register Text View
        mRegisterTextView = mContainer.custom_dialog_reservation_date_register_text_view
        mRegisterTextView.setOnClickListener {
            if (mIsSelect) {
                mListener!!.onSelectDateAndTime(mScheduleTimeData)
                dismiss()
            } else {
                Toast.makeText(mContext, mContext.resources.getString(R.string.text_hint_select_reservation_time), Toast.LENGTH_SHORT).show()
            }
        }


        setCalendar()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCalendar() {
        val today = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 2)
        mCalendarView.setSelectionMode(mCalendarView.SINGLE_SELECTION)
            .setToday(today)
            .setMonthRange(today, end)
            .setWeekStartDay(Calendar.SUNDAY, false)
            .setIsBoldTextOnSelectionEnabled(true)
            .setCallback(this)
            .setCurrentSelectedDate(today)
            .build()

        mSelectDay = SimpleDateFormat("yyyy-MM-dd").format(today.time)

        getReservationScheduleDay()
    }

    override fun onDayLongPressed(selectedDate: Calendar?) {
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDaySelected(selectedDate: Calendar?) {
        mSelectDay = SimpleDateFormat("yyyy-MM-dd").format(selectedDate!!.time)
        NagajaLog().e("wooks, mSelectDay = $mSelectDay")

        for (i in mScheduleDayListData.indices) {
            if (mSelectDay == mScheduleDayListData[i].getCompanyDays()) {
                mScheduleDayUid = mScheduleDayListData[i].getScheduleDaysUid()

                getReservationScheduleTime()
                break
            }
        }
    }

    override fun onMonthChanged(monthStartDate: Calendar?) {
    }

    private fun disConnectUserToken(context: Context) {
        val customDialog = CustomDialog(
            context,
            NagajaFragment.DIALOG_TYPE_NO_CANCEL,
            context.resources.getString(R.string.text_expired_tokens),
            context.resources.getString(R.string.text_message_expired_tokens),
            "",
            context.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()

                activity!!.finishAffinity()
                exitProcess(0)
            }
        })
        customDialog.show()
    }

    // =================================================================================
    // API
    // =================================================================================

    /**
     * API. Get Reservation Schedule Day
     */
    private fun getReservationScheduleDay() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReservationScheduleDay(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<ReservationScheduleDayData>> {
                override fun onSuccess(resultData: ArrayList<ReservationScheduleDayData>) {
                    mScheduleDayListData = resultData

                    if (!TextUtils.isEmpty(mSelectDay)) {
                        for (i in mScheduleDayListData.indices) {
                            if (mSelectDay == mScheduleDayListData[i].getCompanyDays()) {
                                mScheduleDayUid = mScheduleDayListData[i].getScheduleDaysUid()

                                getReservationScheduleTime()
                                break
                            }
                        }
                    }
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
            mCompanyUid.toString()
        )
    }

    /**
     * API. Get Reservation Schedule Time
     */
    private fun getReservationScheduleTime() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReservationScheduleTime(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<ReservationScheduleTimeData>> {
                override fun onSuccess(resultData: ArrayList<ReservationScheduleTimeData>) {
                    if (resultData.isEmpty()) {
                        return
                    }

                    mScheduleTimeAdapter.setData(resultData)
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
            mScheduleDayUid.toString()
        )
    }

}