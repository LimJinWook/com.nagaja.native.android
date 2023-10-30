package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.ReservationScheduleTimeData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.custom_dialog_reservation_date.view.*
import kotlinx.android.synthetic.main.list_item_reservation_schedule_time.view.*


class ReservationScheduleTimeAdapter(context: Context): RecyclerView.Adapter<ReservationScheduleTimeAdapter.ViewHolder>() {
    private var mReservationScheduleTimeListData: ArrayList<ReservationScheduleTimeData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnItemClickListener {
        fun onClick(data: ReservationScheduleTimeData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<ReservationScheduleTimeData>) {
        if (null != mReservationScheduleTimeListData) {
            mReservationScheduleTimeListData.clear()
        }
        
        val itemList: ArrayList<ReservationScheduleTimeData> = ArrayList<ReservationScheduleTimeData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mReservationScheduleTimeListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mReservationScheduleTimeListData) {
            val data: ReservationScheduleTimeData = mReservationScheduleTimeListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mReservationScheduleTimeListData) {
            mReservationScheduleTimeListData.size
        } else 0
    }

    fun getItem(position: Int): ReservationScheduleTimeData? {
        if (null != mReservationScheduleTimeListData) {
            if (0 <= position && position < mReservationScheduleTimeListData.size) {
                return mReservationScheduleTimeListData[position]
            }
        }
        return null
    }

    fun setSelectItem(position: Int) {
        for (i in 0 until mReservationScheduleTimeListData.size) {
            if (i == position) {
                mReservationScheduleTimeListData[i].setIsSelect(true)
            } else {
                mReservationScheduleTimeListData[i].setIsSelect(false)
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_reservation_schedule_time,
            parent,
            false
        )
        return ViewHolder(inflatedView)
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (null == viewHolder) {
            return
        }
        val data: ReservationScheduleTimeData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getCompanyDays())) {
            viewHolder.mDateAndTimeTextView.text = data.getCompanyDays() + " " + data.getBeginTime()
        }

        if (data.getReservationTimeLimitCount() == 0) {
            viewHolder.mPersonTextView.text = "0"

            viewHolder.mConvertView.isEnabled = false
        } else if (data.getReservationTimeLimitCount() > data.getReservationTimeCount()) {
            viewHolder.mPersonTextView.text = (data.getReservationTimeLimitCount() - data.getReservationTimeCount()).toString()
            viewHolder.mConvertView.isEnabled = true
        } else {
            viewHolder.mScheduleDateTimeView.setBackgroundResource(R.drawable.bg_boarder_black_e1e1e1)
            viewHolder.mDateAndTimeTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
            viewHolder.mPersonTextView.text = "0"
            viewHolder.mPersonTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
            viewHolder.mDateAndTimeTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
            viewHolder.mPerson2TextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))

            viewHolder.mConvertView.isEnabled = false
        }


        if (data.getIsSelect()) {
            viewHolder.mScheduleDateTimeView.setBackgroundResource(R.drawable.bg_boarder_blue_blue)

            viewHolder.mDateAndTimeTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
            viewHolder.mPersonTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
            viewHolder.mPerson2TextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
        } else {
            viewHolder.mScheduleDateTimeView.setBackgroundResource(R.drawable.bg_boarder_black)

            if (data.getReservationTimeLimitCount() == 0) {
                viewHolder.mScheduleDateTimeView.setBackgroundResource(R.drawable.bg_boarder_black_e1e1e1)
                viewHolder.mDateAndTimeTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
                viewHolder.mPersonTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
                viewHolder.mPerson2TextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
            } else if (data.getReservationTimeLimitCount() > data.getReservationTimeCount()) {
                viewHolder.mDateAndTimeTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_000000))
                viewHolder.mPersonTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_000000))
                viewHolder.mPerson2TextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_000000))
            } else {
                viewHolder.mScheduleDateTimeView.setBackgroundResource(R.drawable.bg_boarder_black_e1e1e1)
                viewHolder.mDateAndTimeTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
                viewHolder.mPersonTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
                viewHolder.mPerson2TextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
            }
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnItemClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mScheduleDateTimeView: View
        var mDateAndTimeTextView: TextView
        var mPersonTextView: TextView
        var mPerson2TextView: TextView

        init {
            mConvertView = convertView
            mScheduleDateTimeView = convertView.list_item_reservation_schedule_time_view
            mDateAndTimeTextView = convertView.list_item_reservation_schedule_time_date_text_view
            mPersonTextView = convertView.list_item_reservation_schedule_time_person_text_view
            mPerson2TextView = convertView.list_item_reservation_schedule_time_person_2_text_view
        }
    }
}