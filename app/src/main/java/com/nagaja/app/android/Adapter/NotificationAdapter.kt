package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.NotificationData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_faq.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_notification.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class NotificationAdapter(context: Context): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private var mNotificationListData: ArrayList<NotificationData> = ArrayList()
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
        const val NOTICE_TYPE_RESERVATION                  = 1
        const val NOTICE_TYPE_CHAT                         = 2
        const val NOTICE_TYPE_NOTICE                       = 3
        const val NOTICE_TYPE_EVENT                        = 4
        const val NOTICE_TYPE_SERVER_CHECK                 = 5
        const val NOTICE_TYPE_NOTE                         = 6
    }

    interface OnItemClickListener {
        fun onClick(data: NotificationData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NotificationData>) {
        if (null != mNotificationListData) {
            mNotificationListData.clear()
        }
        
        val itemList: ArrayList<NotificationData> = ArrayList<NotificationData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mNotificationListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNotificationListData) {
            val data: NotificationData = mNotificationListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNotificationListData) {
            mNotificationListData.size
        } else 0
    }

    fun getItem(position: Int): NotificationData? {
        if (null != mNotificationListData) {
            if (0 <= position && position < mNotificationListData.size) {
                return mNotificationListData[position]
            }
        }
        return null
    }

    fun setItemConfirm(position: Int) {
        if (mNotificationListData.isEmpty()) {
            return
        }

        mNotificationListData[position].setPushStatus(1)

        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_notification,
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
        val data: NotificationData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

        viewHolder.mUnidentifiedTextView.visibility = if (data.getPushStatus() == 1) View.GONE else View.VISIBLE

        if (!TextUtils.isEmpty(data.getPushSubject())) {
            viewHolder.mSubjectTextView.text = data.getPushSubject()
        }

        if (!TextUtils.isEmpty(data.getCreateDate())) {
            var time = data.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            viewHolder.mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        if (!TextUtils.isEmpty(data.getPushMemo())) {
            viewHolder.mMessageTextView.text = data.getPushMemo()
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnItemClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mUnidentifiedTextView: TextView
        var mSubjectTextView: TextView
        var mDateTextView: TextView
        var mMessageTextView: TextView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_notification_line_view
            mUnidentifiedTextView = convertView.list_item_notification_unidentified_text_view
            mSubjectTextView = convertView.list_item_notification_subject_text_view
            mDateTextView = convertView.list_item_notification_date_text_view
            mMessageTextView = convertView.list_item_notification_message_text_view
        }
    }
}