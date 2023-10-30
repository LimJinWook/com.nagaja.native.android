package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.ReservationMemoData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_reservation_memo.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class ReservationMemoAdapter(context: Context): RecyclerView.Adapter<ReservationMemoAdapter.ViewHolder>() {
    private var mReservationMemoListData: ArrayList<ReservationMemoData> = ArrayList()
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
        fun onClick(data: ReservationMemoData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<ReservationMemoData>) {
        if (null != mReservationMemoListData) {
            mReservationMemoListData.clear()
        }
        
        val itemList: ArrayList<ReservationMemoData> = ArrayList<ReservationMemoData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mReservationMemoListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mReservationMemoListData) {
            val data: ReservationMemoData = mReservationMemoListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mReservationMemoListData) {
            mReservationMemoListData.size
        } else 0
    }

    fun getItem(position: Int): ReservationMemoData? {
        if (null != mReservationMemoListData) {
            if (0 <= position && position < mReservationMemoListData.size) {
                return mReservationMemoListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_reservation_memo,
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
        val data: ReservationMemoData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getCreateDate())) {
            var time = data.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            viewHolder.mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        if (!TextUtils.isEmpty(data.getReservationComment())) {
            viewHolder.mMemoTextView.text = data.getReservationComment()
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mDateTextView: TextView
        var mMemoTextView: TextView

        init {
            mConvertView = convertView
            mDateTextView = convertView.list_item_reservation_memo_date_text_view
            mMemoTextView = convertView.list_item_reservation_memo_memo_text_view
        }
    }
}