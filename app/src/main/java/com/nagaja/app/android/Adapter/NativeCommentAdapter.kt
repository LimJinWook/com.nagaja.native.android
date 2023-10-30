package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.NativeCommentData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_news_comment.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*


class NativeCommentAdapter(context: Context): RecyclerView.Adapter<NativeCommentAdapter.ViewHolder>() {
    private var mCommentListData: ArrayList<NativeCommentData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnNewsItemClickListener: OnNewsItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnNewsItemClickListener {
        fun onClick(data: NativeCommentData, isDelete: Boolean, position: Int)
    }

    fun setOnNewsItemClickListener(listener: OnNewsItemClickListener?) {
        mOnNewsItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NativeCommentData>) {
        if (null != mCommentListData) {
            mCommentListData.clear()
        }
        
        val itemList: ArrayList<NativeCommentData> = ArrayList<NativeCommentData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mCommentListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mCommentListData) {
            val data: NativeCommentData = mCommentListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mCommentListData) {
            mCommentListData.size
        } else 0
    }

    fun getItem(position: Int): NativeCommentData? {
        if (null != mCommentListData) {
            if (0 <= position && position < mCommentListData.size) {
                return mCommentListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mCommentListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_news_comment,
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
        val data: NativeCommentData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

//        viewHolder.mCommentView.setBackgroundColor(if (data.getIsMyComment()) ContextCompat.getColor(mContext!!, R.color.bg_color_f4f8fd) else ContextCompat.getColor(mContext!!, R.color.bg_color_ffffff))
        
        if (!TextUtils.isEmpty(data.getMemberName())) {
            viewHolder.mNameTextView.text = data.getMemberName()
        }

//        viewHolder.mDeleteImageView.visibility = if (data.getIsMyComment()) View.VISIBLE else View.GONE
        viewHolder.mDeleteImageView.setOnSingleClickListener {
            mOnNewsItemClickListener!!.onClick(data, true, position)
        }

        if (!TextUtils.isEmpty(data.getComment())) {
            viewHolder.mCommentTextView.text = data.getComment()
        }

        if (!TextUtils.isEmpty(data.getCreateDate())) {
            viewHolder.mDateTextView.text = data.getCreateDate()
        }

//        if (data.getIsPrivate()) {
//            viewHolder.mPublicView.visibility = View.GONE
//            viewHolder.mPrivateView.visibility = View.VISIBLE
//        } else {
//            viewHolder.mPublicView.visibility = View.VISIBLE
//            viewHolder.mPrivateView.visibility = View.GONE
//        }

//        viewHolder.mReportTextView.visibility = if (data.getIsMyComment()) View.GONE else View.VISIBLE
        viewHolder.mReportTextView.setOnSingleClickListener {
            mOnNewsItemClickListener!!.onClick(data, false, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mCommentView: View
        var mLineView: View
        var mNameTextView: TextView
        var mDeleteImageView: ImageView
        var mCommentTextView: TextView
        var mDateTextView: TextView
        var mReportTextView: TextView
        var mPublicView: View
        var mPrivateView: View

        init {
            mConvertView = convertView
            mCommentView = convertView.list_item_news_comment_view
            mLineView = convertView.list_item_news_comment_line_view
            mNameTextView = convertView.list_item_news_comment_name_text_view
            mDeleteImageView = convertView.list_item_news_comment_delete_image_view
            mCommentTextView = convertView.list_item_news_comment_text_view
            mDateTextView = convertView.list_item_news_comment_date_text_view
            mReportTextView = convertView.list_item_news_comment_report_text_view
            mPublicView = convertView.list_item_news_public_view
            mPrivateView = convertView.list_item_news_private_view
        }
    }
}