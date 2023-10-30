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
import com.nagaja.app.android.Data.NativeJobAndMissingData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_job.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*


class NativeJobAdapter(context: Context): RecyclerView.Adapter<NativeJobAdapter.ViewHolder>() {
    private var mNativeJobAndMissingListData: ArrayList<NativeJobAndMissingData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnJobItemClickListener: OnJobItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnJobItemClickListener {
        fun onClick(data: NativeJobAndMissingData)
    }

    fun setOnJobItemClickListener(listener: OnJobItemClickListener?) {
        mOnJobItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NativeJobAndMissingData>) {
        if (null != mNativeJobAndMissingListData) {
            mNativeJobAndMissingListData.clear()
        }
        
        val itemList: ArrayList<NativeJobAndMissingData> = ArrayList<NativeJobAndMissingData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mNativeJobAndMissingListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNativeJobAndMissingListData) {
            val data: NativeJobAndMissingData = mNativeJobAndMissingListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNativeJobAndMissingListData) {
            mNativeJobAndMissingListData.size
        } else 0
    }

    fun getItem(position: Int): NativeJobAndMissingData? {
        if (null != mNativeJobAndMissingListData) {
            if (0 <= position && position < mNativeJobAndMissingListData.size) {
                return mNativeJobAndMissingListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_job,
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
        val data: NativeJobAndMissingData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

        if (data.getBoardImageList().size > 0) {
            if (!TextUtils.isEmpty(data.getBoardImageList()[0].getBoardImageName())) {
                val url = "https://the330-nagaja.s3.ap-northeast-2.amazonaws.com"
                viewHolder.mJobImageView.setImageURI(Uri.parse(url + data.getBoardImageList()[0].getBoardImageName()))
            }
        } else {
            viewHolder.mJobImageView.setImageURI(Uri.parse(""))
        }

        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            viewHolder.mTitleTextView.text = data.getBoardSubject()
        }

        if (!TextUtils.isEmpty(data.getBoardContent())) {
            viewHolder.mContentView.text = data.getBoardContent()
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnJobItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mJobImageView: SimpleDraweeView
        var mTitleTextView: TextView
        var mContentView: TextView
        var mDateTextView: TextView
        var mRegisterAgencyTextView: TextView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_job_line_view
            mJobImageView = convertView.list_item_job_image_view
            mTitleTextView = convertView.list_item_job_title_text_view
            mContentView = convertView.list_item_job_content_text_view
            mDateTextView = convertView.list_item_job_date_text_view
            mRegisterAgencyTextView = convertView.list_item_job_register_agency_text_view
        }
    }
}