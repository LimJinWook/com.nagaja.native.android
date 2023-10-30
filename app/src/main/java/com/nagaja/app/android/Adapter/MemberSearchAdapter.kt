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
import com.nagaja.app.android.Data.FindUserData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_search_member.view.*


class MemberSearchAdapter(context: Context): RecyclerView.Adapter<MemberSearchAdapter.ViewHolder>() {
    private var mFindUserListData: ArrayList<FindUserData> = ArrayList()
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
        fun onClick(data: FindUserData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<FindUserData>) {
        if (null != mFindUserListData) {
            mFindUserListData.clear()
        }
        
        val itemList: ArrayList<FindUserData> = ArrayList<FindUserData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mFindUserListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mFindUserListData) {
            val data: FindUserData = mFindUserListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mFindUserListData) {
            mFindUserListData.size
        } else 0
    }

    fun getItem(position: Int): FindUserData? {
        if (null != mFindUserListData) {
            if (0 <= position && position < mFindUserListData.size) {
                return mFindUserListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_search_member,
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

        val data: FindUserData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getName()) && data.getName() != "null") {
            viewHolder.mSearchMemberTextView.text = data.getName()
        }

        viewHolder.mSearchView.setBackgroundColor(if (data.getIsSelect()) ContextCompat.getColor(mContext!!, R.color.bg_color_e2e7ee) else ContextCompat.getColor(mContext!!, R.color.bg_color_ffffff))

        viewHolder.mSearchView.setOnClickListener {
            mOnItemClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mSearchView: View
        var mSearchMemberTextView: TextView

        init {
            mConvertView = convertView
            mSearchView = convertView.list_item_search_member_view
            mSearchMemberTextView = convertView.list_item_search_member_text_view
        }
    }
}