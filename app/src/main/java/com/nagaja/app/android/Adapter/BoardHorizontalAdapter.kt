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
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_THUMB
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_board_horizontal.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class BoardHorizontalAdapter(context: Context): RecyclerView.Adapter<BoardHorizontalAdapter.ViewHolder>() {
    private var mBoardListData: ArrayList<BoardData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnBoardItemClickListener: OnBoardItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    companion object {
        const val CATEGORY_TYPE_ALL                   = 0x00
        const val CATEGORY_TYPE_PLAYGROUND            = 0x01
        const val CATEGORY_TYPE_FAMOUS_RESTAURANT     = 0x02
        const val CATEGORY_TYPE_TALK                  = 0x03
        const val CATEGORY_TYPE_CATEGORY_1            = 0x04
        const val CATEGORY_TYPE_CATEGORY_2            = 0x05
        const val CATEGORY_TYPE_CATEGORY_3            = 0x06
    }

    interface OnBoardItemClickListener {
        fun onClick(data: BoardData, position: Int)
    }

    fun setOnBoardItemClickListener(listener: OnBoardItemClickListener?) {
        mOnBoardItemClickListener = listener
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

    fun deleteItem(position: Int) {
        mBoardListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_board_horizontal,
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

        if (!TextUtils.isEmpty(data.getMemberImageName())) {
            viewHolder.mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getMemberImageName()))
        }

        if (!TextUtils.isEmpty(data.getMemberName())) {
            viewHolder.mNameTextView.text = data.getMemberName()
        }

        if (!TextUtils.isEmpty(data.getCategoryName())) {
            viewHolder.mCategoryTextView.text = data.getCategoryName()
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

        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            viewHolder.mTitleTextView.text = data.getBoardSubject()
        }

        if (!TextUtils.isEmpty(data.getBoardContent())) {
            viewHolder.mContentTextView.text = data.getBoardContent()
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnBoardItemClickListener!!.onClick(data , position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mProfileImageView: SimpleDraweeView
        var mNameTextView: TextView
        var mCategoryTextView: TextView
        var mDateTextView: TextView
        var mTitleTextView: TextView
        var mContentTextView: TextView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_board_horizontal_margin_view
            mProfileImageView = convertView.list_item_board_horizontal_profile_image_view
            mNameTextView = convertView.list_item_board_horizontal_name_text_view
            mCategoryTextView = convertView.list_item_board_horizontal_category_text_view
            mDateTextView = convertView.list_item_board_horizontal_date_text_view
            mTitleTextView = convertView.list_item_board_horizontal_title_text_view
            mContentTextView = convertView.list_item_board_horizontal_content_text_view
        }
    }
}