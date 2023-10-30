package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.CompanyMemberData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_company_sub_item.view.*


class CompanyMemberAdapter(context: Context): RecyclerView.Adapter<CompanyMemberAdapter.ViewHolder>() {
    private var mCompanyMemberListData: ArrayList<CompanyMemberData> = ArrayList()
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
        fun onClick(data: CompanyMemberData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<CompanyMemberData>) {
        if (null != mCompanyMemberListData) {
            mCompanyMemberListData.clear()
        }
        
        val itemList: ArrayList<CompanyMemberData> = ArrayList<CompanyMemberData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mCompanyMemberListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mCompanyMemberListData) {
            val data: CompanyMemberData = mCompanyMemberListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mCompanyMemberListData) {
            mCompanyMemberListData.size
        } else 0
    }

    fun getItem(position: Int): CompanyMemberData? {
        if (null != mCompanyMemberListData) {
            if (0 <= position && position < mCompanyMemberListData.size) {
                return mCompanyMemberListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_company_sub_item,
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
        val data: CompanyMemberData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == itemCount-1) View.GONE else View.VISIBLE

        if (!TextUtils.isEmpty(data.getCompanyName())) {
            val type = if (data.getCompanyManagerGrant() == 1) mContext!!.resources.getString(R.string.text_master) else mContext!!.resources.getString(R.string.text_manager_2)
            viewHolder.mNameTextView.text = type + " - " + data.getCompanyName()
        }

        if (data.getCompanyStatus() == 1) {
            viewHolder.mCountView.visibility = View.VISIBLE
            viewHolder.mWaitingReview.visibility = View.GONE

            viewHolder.mChatCountTextView.text = data.getChatCount().toString()

            viewHolder.mNoteCountTextView.text = data.getNoteCount().toString()

            viewHolder.mReservationCountTextView.text = data.getReservationCount().toString()

            viewHolder.mPushCountTextView.text = data.getRegularCount().toString()

        } else {
            viewHolder.mCountView.visibility = View.GONE
            viewHolder.mWaitingReview.visibility = View.VISIBLE
        }

        viewHolder.mNameView.setOnClickListener {
            mOnItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mNameView: View
        var mNameTextView: TextView
        var mCountView: View
        var mChatCountTextView: TextView
        var mNoteCountTextView: TextView
        var mReservationCountTextView: TextView
        var mPushCountTextView: TextView
        var mWaitingReview: TextView
        var mLineView: View

        init {
            mConvertView = convertView
            mNameView = convertView.list_item_company_sub_name_view
            mNameTextView = convertView.list_item_company_sub_name_text_view
            mCountView = convertView.list_item_company_sub_count_view
            mChatCountTextView = convertView.list_item_company_sub_chat_count_text_view
            mNoteCountTextView = convertView.list_item_company_sub_note_count_text_view
            mReservationCountTextView = convertView.list_item_company_sub_reservation_count_text_view
            mPushCountTextView = convertView.list_item_company_sub_push_count_text_view
            mWaitingReview = convertView.list_item_company_sub_waiting_review_text_view
            mLineView = convertView.list_item_company_sub_line_view
        }
    }
}