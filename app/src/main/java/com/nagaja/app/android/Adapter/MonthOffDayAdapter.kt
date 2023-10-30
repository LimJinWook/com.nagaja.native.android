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
import com.nagaja.app.android.Data.MonthOffDayData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_month_off_day.view.*


class MonthOffDayAdapter(context: Context): RecyclerView.Adapter<MonthOffDayAdapter.ViewHolder>() {
    private var mMonthOffDayListData: ArrayList<MonthOffDayData> = ArrayList()
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
        fun onClick(date: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<MonthOffDayData>) {
        if (null != mMonthOffDayListData) {
            mMonthOffDayListData.clear()
        }
        
        val itemList: ArrayList<MonthOffDayData> = ArrayList<MonthOffDayData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mMonthOffDayListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mMonthOffDayListData) {
            val data: MonthOffDayData = mMonthOffDayListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mMonthOffDayListData) {
            mMonthOffDayListData.size
        } else 0
    }

    fun getItem(position: Int): MonthOffDayData? {
        if (null != mMonthOffDayListData) {
            if (0 <= position && position < mMonthOffDayListData.size) {
                return mMonthOffDayListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_month_off_day,
            parent,
            false
        )
        return ViewHolder(inflatedView)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (null == viewHolder) {
            return
        }
        val data: MonthOffDayData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getDate())) {
            viewHolder.mMonthOffDayTextView.text = data.getDate()
        }

        if (data.getIsSelect()) {
            viewHolder.mMonthOffDayView.setBackgroundResource(R.drawable.bg_select_circle)
            viewHolder.mMonthOffDayTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ffffff))
        } else {
            viewHolder.mMonthOffDayView.setBackgroundResource(R.drawable.bg_unselect_circle)
            viewHolder.mMonthOffDayTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_222222))
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnItemClickListener!!.onClick(data.getDate())
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mMonthOffDayView: View
        var mMonthOffDayTextView: TextView

        init {
            mConvertView = convertView
            mMonthOffDayView = convertView.list_item_month_off_day_view
            mMonthOffDayTextView = convertView.list_item_month_off_day_text_view
        }
    }
}