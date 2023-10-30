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
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_missing.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class MissingAdapter(context: Context): RecyclerView.Adapter<MissingAdapter.ViewHolder>() {
    private var mBoardListData: ArrayList<BoardData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnMissingItemClickListener: OnMissingItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnMissingItemClickListener {
        fun onClick(data: BoardData)
    }

    fun setOnMissingItemClickListener(listener: OnMissingItemClickListener?) {
        mOnMissingItemClickListener = listener
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
            R.layout.list_item_missing,
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
            viewHolder.mProfileImageView.visibility = View.VISIBLE
            viewHolder.mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getBoardImageListData()[0].getItemImageName()))
        } else {
//            viewHolder.mProfileImageView.setImageURI("")
            viewHolder.mProfileImageView.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            viewHolder.mTitleTextView.text = data.getBoardSubject()
        }

        if (!TextUtils.isEmpty(data.getBoardContent())) {
            viewHolder.mDescTextView.text = data.getBoardContent()
        }

        if (!TextUtils.isEmpty(data.getLocationMainAreaName()) && !TextUtils.isEmpty(data.getLocationSubAreaName())) {
            viewHolder.mLocationView.visibility = View.VISIBLE
            viewHolder.mLocationTextView.text = data.getLocationMainAreaName() + " " + data.getLocationSubAreaName() + " " + data.getLocationDesc()
        } else {
            viewHolder.mLocationView.visibility = View.GONE
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

        if (!TextUtils.isEmpty(data.getCompanyName())) {
            viewHolder.mRegisterUserTextView.text = data.getCompanyName()
        }

        if (data.getViewCount() > 0) {
            viewHolder.mViewCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_board_view_count), data.getViewCount().toString())
        } else {
            viewHolder.mViewCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_board_view_count), "0")
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnMissingItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mProfileImageView: SimpleDraweeView
        var mTitleTextView: TextView
        var mDescTextView: TextView
        var mLocationView: View
        var mLocationTextView: TextView
        var mDateTextView: TextView
        var mRegisterUserTextView: TextView
        var mViewCountTextView: TextView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_missing_line_view
            mProfileImageView = convertView.list_item_missing_profile_image_view
            mTitleTextView = convertView.list_item_missing_title_text_view
            mDescTextView = convertView.list_item_missing_description_text_view
            mLocationView = convertView.list_item_missing_location_view
            mLocationTextView = convertView.list_item_missing_location_text_view
            mDateTextView = convertView.list_item_missing_date_text_view
            mRegisterUserTextView = convertView.list_item_missing_register_used_text_view
            mViewCountTextView = convertView.list_item_missing_view_count_text_view
        }
    }
}