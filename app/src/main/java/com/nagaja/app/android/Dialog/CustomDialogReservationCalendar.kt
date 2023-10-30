package com.nagaja.app.android.Dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager
import com.applikeysolutions.cosmocalendar.view.CalendarView
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_dialog_reservation_calendar.*
import kotlinx.android.synthetic.main.custom_event_dialog.*
import java.text.SimpleDateFormat
import java.util.*

class CustomDialogReservationCalendar(context: Context) : Dialog(context) {

    private var mContext: Context = context

    private lateinit var mCalendarView: CalendarView

    private lateinit var mListener: OnCustomEventDialogListener

    private var mStartDate = ""
    private var mEndDate = ""


    init {
        this.mContext = context
    }

    fun setOnCustomEventDialogListener(listener: OnCustomEventDialogListener) {
        mListener = listener
    }

    interface OnCustomEventDialogListener {
        fun onCancel()
        fun onConfirm(startDate: String, endDate: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.custom_dialog_reservation_calendar)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {

        // Cancel Image View
        val cancelImageView = custom_dialog_reservation_calendar_cancel_image_view
        cancelImageView.setOnClickListener {
            mListener.onCancel()
        }

        mCalendarView = custom_dialog_reservation_calendar_view
        mCalendarView.selectionManager = RangeSelectionManager(OnDaySelectedListener {
            if (mCalendarView.selectedDates.size <= 0) {
                return@OnDaySelectedListener
            }

            if (mCalendarView.selectedDays.size >= 1) {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

                mStartDate = simpleDateFormat.format(mCalendarView.selectedDays[0].calendar.time)
                mEndDate = simpleDateFormat.format(mCalendarView.selectedDays[mCalendarView.selectedDays.size - 1].calendar.time)

                if (TextUtils.isEmpty(mEndDate)) {
                    mEndDate = mStartDate
                }
            }
        })

        // Search Text View
        val searchTextView = custom_dialog_reservation_calendar_search_text_view
        searchTextView.setOnClickListener {
            if (TextUtils.isEmpty(mStartDate) || TextUtils.isEmpty(mEndDate)) {
                Toast.makeText(mContext, mContext.resources.getString(R.string.text_error_select_search_date), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mListener.onConfirm(mStartDate, mEndDate)
        }
    }

}