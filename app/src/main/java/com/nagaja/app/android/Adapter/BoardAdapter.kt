package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_notice_board.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class BoardAdapter(context: Context, isNotice: Boolean): RecyclerView.Adapter<BoardAdapter.ViewHolder>() {
    private var mBoardListData: ArrayList<BoardData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnNewsItemClickListener: OnNewsItemClickListener? = null
    private var mIsNotice = false

    init {
        mContext = context
        mIsNotice = isNotice
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnNewsItemClickListener {
        fun onClick(boardUid: Int)
    }

    fun setOnNewsItemClickListener(listener: OnNewsItemClickListener?) {
        mOnNewsItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<BoardData>) {
        if (null != mBoardListData) {
            mBoardListData.clear()
        }
        
        val itemList: ArrayList<BoardData> = ArrayList<BoardData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mBoardListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mBoardListData) {
            val data: BoardData = mBoardListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mBoardListData) {
            mBoardListData.size
        } else 0
    }

    fun getItem(position: Int): BoardData? {
        if (null != mBoardListData) {
            if (0 <= position && position < mBoardListData.size) {
                return mBoardListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_notice_board,
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
        val data: BoardData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

        if (data.getBoardImageListData().size > 0) {
            viewHolder.mNewImageView.visibility = View.VISIBLE
            viewHolder.mNewImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getBoardImageListData()[0].getItemImageName()))
        } else {
            viewHolder.mNewImageView.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            viewHolder.mTitleTextView.text = data.getBoardSubject()
        }

        if (!TextUtils.isEmpty(data.getBoardContent())) {
            viewHolder.mContentView.text = data.getBoardContent()
        }

        viewHolder.mViewCountTextView.text = if (data.getViewCount() > 0) data.getViewCount().toString() else "0"

        if (mIsNotice) {
            viewHolder.mCommentView.visibility = View.VISIBLE
            viewHolder.mDateTextView.visibility = View.GONE

            viewHolder.mCommentCountTextView.text = if (data.getCommentCount() > 0) data.getCommentCount().toString() else "0"
        } else {
            viewHolder.mCommentView.visibility = View.GONE
            viewHolder.mDateTextView.visibility = View.VISIBLE

            // Date Text
            if (!TextUtils.isEmpty(data.getCreateDate())) {
                var time = data.getCreateDate()
                val index: Int = time.indexOf("+")
                time = time.substring(0, index)

                val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
                val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                viewHolder.mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
            }
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnNewsItemClickListener!!.onClick(data.getBoardUid())
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mNewImageView: SimpleDraweeView
        var mTitleTextView: TextView
        var mContentView: TextView
        var mViewCountTextView: TextView
        var mCommentView: View
        var mCommentCountTextView: TextView
        var mDateTextView: TextView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_notice_board_line_view
            mNewImageView = convertView.list_item_notice_board_image_view
            mTitleTextView = convertView.list_item_notice_board_title_text_view
            mContentView = convertView.list_item_notice_board_content_text_view
            mViewCountTextView = convertView.list_item_notice_board_view_count_text_view
            mCommentView = convertView.list_item_notice_board_comment_view
            mCommentCountTextView = convertView.list_item_notice_board_comment_count_text_view
            mDateTextView = convertView.list_item_notice_board_comment_date_text_view
        }
    }
}