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
import com.nagaja.app.android.Data.PointHistoryData
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import kotlinx.android.synthetic.main.list_item_faq.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_point_history.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class PointHistoryAdapter(context: Context): RecyclerView.Adapter<PointHistoryAdapter.ViewHolder>() {
    private var mPointHistoryListData: ArrayList<PointHistoryData> = ArrayList()
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

     companion object {
         const val POINT_TYPE_RECOMMEND                 = 11
         const val POINT_TYPE_COMPANY_CHAT              = 22
         const val POINT_TYPE_COMPANY_RESERVATION       = 23
         const val POINT_TYPE_USED_MARKET_PULL_UP       = 24
     }

    interface OnItemClickListener {
        fun onClick(data: PointHistoryData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<PointHistoryData>) {
        if (null != mPointHistoryListData) {
            mPointHistoryListData.clear()
        }
        
        val itemList: ArrayList<PointHistoryData> = ArrayList<PointHistoryData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mPointHistoryListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mPointHistoryListData) {
            val data: PointHistoryData = mPointHistoryListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mPointHistoryListData) {
            mPointHistoryListData.size
        } else 0
    }

    fun getItem(position: Int): PointHistoryData? {
        if (null != mPointHistoryListData) {
            if (0 <= position && position < mPointHistoryListData.size) {
                return mPointHistoryListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_point_history,
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
        val data: PointHistoryData = getItem(position) ?: return

        // Date Text View
        if (!TextUtils.isEmpty(data.getCreateDate())) {
            var time = data.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\nHH:mm")

            viewHolder.mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        var textColor = ContextCompat.getColor(mContext!!, R.color.text_color_2273eb)
        val point = data.getPointAmount() + data.getPointBonus()
        var content = ""
        when (data.getPointType()) {
            POINT_TYPE_RECOMMEND -> {
                textColor = ContextCompat.getColor(mContext!!, R.color.text_color_ff4755)
                content = mContext!!.resources.getString(R.string.text_message_referral_point_membership_registration)
            }

            POINT_TYPE_COMPANY_CHAT -> {
                textColor = ContextCompat.getColor(mContext!!, R.color.text_color_2273eb)
                content = mContext!!.resources.getString(R.string.text_message_corporate_inquiry_deduction_points)
            }

            POINT_TYPE_COMPANY_RESERVATION -> {
                textColor = ContextCompat.getColor(mContext!!, R.color.text_color_2273eb)
                content = mContext!!.resources.getString(R.string.text_message_corporate_reservation_deduction_points)
            }

            POINT_TYPE_USED_MARKET_PULL_UP -> {
                textColor = ContextCompat.getColor(mContext!!, R.color.text_color_2273eb)
                content = mContext!!.resources.getString(R.string.text_message_deduction_points_for_bumping_up_secondhand_market)
            }
        }
        viewHolder.mPointTextView.text = if (data.getPointType() == POINT_TYPE_RECOMMEND) "+" + point.toString() + " " + mContext!!.resources.getString(R.string.text_gp) else "-" + point.toString() + " " + mContext!!.resources.getString(R.string.text_gp)
        viewHolder.mPointTextView.setTextColor(textColor)

        viewHolder.mContentTextView.text = content
        viewHolder.mContentTextView.setTextColor(textColor)

        viewHolder.mDateTextView.setTextColor(textColor)



        viewHolder.mConvertView.setOnSingleClickListener {
            mOnItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mDateTextView: TextView
        var mContentTextView: TextView
        var mPointTextView: TextView

        init {
            mConvertView = convertView
            mDateTextView = convertView.list_item_point_history_date_text_view
            mContentTextView = convertView.list_item_point_history_content_text_view
            mPointTextView = convertView.list_item_point_history_point_text_view
        }
    }
}