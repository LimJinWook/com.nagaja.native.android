package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.BreakTimeData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_break_time.view.*
import java.io.IOException
import java.util.*


class BreakTimeAdapter(context: Context): RecyclerView.Adapter<BreakTimeAdapter.ViewHolder>() {
    private var mBreakTimeListData: ArrayList<BreakTimeData> = ArrayList()
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
        fun onClick(data: BreakTimeData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<BreakTimeData>) {
        if (null != mBreakTimeListData) {
            mBreakTimeListData.clear()
        }
        
        val itemList: ArrayList<BreakTimeData> = ArrayList<BreakTimeData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mBreakTimeListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mBreakTimeListData) {
            val data: BreakTimeData = mBreakTimeListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mBreakTimeListData) {
            mBreakTimeListData.size
        } else 0
    }

    fun getItem(position: Int): BreakTimeData? {
        if (null != mBreakTimeListData) {
            if (0 <= position && position < mBreakTimeListData.size) {
                return mBreakTimeListData[position]
            }
        }
        return null
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(mContext!!, Locale.getDefault())

        (mContext as Activity).runOnUiThread {
            try {
                val addressList = geocoder.getFromLocation(lan, lon, 1)
                if (addressList!!.size != 0) {
                    address = addressList[0].getAddressLine(0)
                    if (address.substring(0, 4) == "대한민국") {
                        address = address.substring(5)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return address
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_break_time,
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
        val data: BreakTimeData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getStartTime())) {
            viewHolder.mStartTimeTextView.text = data.getStartTime()
        }

        if (!TextUtils.isEmpty(data.getEndTime())) {
            viewHolder.mEndTimeTextView.text = data.getEndTime()
        }

        viewHolder.mDeleteImageView.setOnClickListener {
            mOnItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mStartTimeTextView: TextView
        var mEndTimeTextView: TextView
        var mDeleteImageView: ImageView

        init {
            mConvertView = convertView
            mStartTimeTextView = convertView.list_item_break_time_start_text_view
            mEndTimeTextView = convertView.list_item_break_time_end_text_view
            mDeleteImageView = convertView.list_item_break_time_delete_image_view
        }
    }
}