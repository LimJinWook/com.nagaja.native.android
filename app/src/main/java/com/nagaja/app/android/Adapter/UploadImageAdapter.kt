package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_store_review.view.*
import kotlinx.android.synthetic.main.list_item_store_review_upload_image.view.*


class UploadImageAdapter(context: Context): RecyclerView.Adapter<UploadImageAdapter.ViewHolder>() {
    private var mImageListData: ArrayList<String> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnImageClickListener: OnImageClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnImageClickListener {
        fun onClick(position: Int)
    }

    fun setOnImageClickListener(listener: OnImageClickListener?) {
        mOnImageClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<String>) {
        if (null != mImageListData) {
            mImageListData.clear()
        }
        
        val itemList: ArrayList<String> = ArrayList<String>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mImageListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mImageListData) {
            val data: String = mImageListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mImageListData) {
            mImageListData.size
        } else 0
    }

    fun getItem(position: Int): String? {
        if (null != mImageListData) {
            if (0 <= position && position < mImageListData.size) {
                return mImageListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_store_review_upload_image,
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
        val data: String = getItem(position) ?: return

        viewHolder.mMarjinView.visibility = if (position == 0) View.VISIBLE else View.GONE

        if (!TextUtils.isEmpty(data)) {
            viewHolder.mUploadImageView.setImageURI(Uri.parse(data))
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnImageClickListener!!.onClick(position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mMarjinView: View
        var mUploadImageView: SimpleDraweeView

        init {
            mConvertView = convertView
            mMarjinView = convertView.list_item_store_review_upload_margin_view
            mUploadImageView = convertView.list_item_store_review_upload_image_view
        }
    }
}