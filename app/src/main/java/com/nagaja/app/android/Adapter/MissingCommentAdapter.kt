package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.CommentData
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_news_comment.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class MissingCommentAdapter(context: Context): RecyclerView.Adapter<MissingCommentAdapter.ViewHolder>() {
    private var mCommentListData: ArrayList<CommentData> = ArrayList()
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
        fun onDelete(commentUid: Int, position: Int)
        fun onReport(commentUid: Int, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<CommentData>) {
        if (null != mCommentListData) {
            mCommentListData.clear()
        }
        
        val itemList: ArrayList<CommentData> = ArrayList<CommentData>()
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
            val data: CommentData = mCommentListData[position]
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

    fun getItem(position: Int): CommentData? {
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
        val data: CommentData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

        viewHolder.mCommentView.setBackgroundColor(if (data.getMemberUid() == MAPP.USER_DATA.getMemberUid()) ContextCompat.getColor(mContext!!, R.color.bg_color_f4f8fd)
        else ContextCompat.getColor(mContext!!, R.color.bg_color_ffffff))

        if (!TextUtils.isEmpty(data.getMemberName())) {
            if (MAPP.USER_DATA.getMemberGrant() == 1) {
                if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) {
                    viewHolder.mNameTextView.text = data.getMemberName()
                } else {
                    viewHolder.mNameTextView.text = "*****"
                }
            } else {
                viewHolder.mNameTextView.text = data.getMemberName()
            }
        }

        viewHolder.mDeleteImageView.visibility = if (data.getMemberUid() == MAPP.USER_DATA.getMemberUid()) View.VISIBLE else View.GONE
        viewHolder.mDeleteImageView.setOnSingleClickListener {
            mOnItemClickListener!!.onDelete(data.getCommentUid(), position)
        }

        if (!TextUtils.isEmpty(data.getComment())) {
            viewHolder.mCommentTextView.text = data.getComment()
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

        viewHolder.mReportTextView.visibility = View.GONE
//        viewHolder.mReportTextView.visibility = if (data.getMemberUid() == MAPP.USER_DATA.getMemberUid()) View.GONE else View.VISIBLE
//        viewHolder.mReportTextView.setOnSingleClickListener {
//            mOnItemClickListener!!.onReport(data.getCommentUid(), position)
//        }
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