package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.NoticeData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_faq.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*


class FAQAdapter(context: Context): RecyclerView.Adapter<FAQAdapter.ViewHolder>() {
    private var mNoticeListData: ArrayList<NoticeData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnFAQItemClickListener: OnFAQItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnFAQItemClickListener {
        fun onClick(data: NoticeData)
    }

    fun setOnFAQItemClickListener(listener: OnFAQItemClickListener?) {
        mOnFAQItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NoticeData>) {
        if (null != mNoticeListData) {
            mNoticeListData.clear()
        }
        
        val itemList: ArrayList<NoticeData> = ArrayList<NoticeData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mNoticeListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNoticeListData) {
            val data: NoticeData = mNoticeListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNoticeListData) {
            mNoticeListData.size
        } else 0
    }

    fun getItem(position: Int): NoticeData? {
        if (null != mNoticeListData) {
            if (0 <= position && position < mNoticeListData.size) {
                return mNoticeListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_faq,
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
        val data: NoticeData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

        if (!TextUtils.isEmpty(data.getNoticeSubject())) {
            viewHolder.mTitleTextView.text = "Q. " + data.getNoticeSubject()
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnFAQItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mTitleTextView: TextView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_faq_line_view
            mTitleTextView = convertView.list_item_faq_title_text_view
        }
    }
}