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
import com.nagaja.app.android.Data.FocusItemData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*


class FocusAdapter(context: Context): RecyclerView.Adapter<FocusAdapter.ViewHolder>() {
    private var mFocusItemListData: ArrayList<FocusItemData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnFocusClickListener: OnFocusClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnFocusClickListener {
        fun onClick(data: FocusItemData)
    }

    fun setOnFocusClickListener(listener: OnFocusClickListener?) {
        mOnFocusClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<FocusItemData>) {
        if (null != mFocusItemListData) {
            mFocusItemListData.clear()
        }
        
        val itemList: ArrayList<FocusItemData> = ArrayList<FocusItemData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mFocusItemListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mFocusItemListData) {
            val data: FocusItemData = mFocusItemListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mFocusItemListData) {
            mFocusItemListData.size
        } else 0
    }

    fun getItem(position: Int): FocusItemData? {
        if (null != mFocusItemListData) {
            if (0 <= position && position < mFocusItemListData.size) {
                return mFocusItemListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_focus,
            parent,
            false
        )
        return ViewHolder(inflatedView)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (null == viewHolder) {
            return
        }
        val data: FocusItemData = getItem(position) ?: return


        val path = "res:/" + data.getImage()
        viewHolder.mImageView.setImageURI(Uri.parse(path))

        if (!TextUtils.isEmpty(data.getTitle())) {
            viewHolder.mTitleTextView.text = data.getTitle()
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnFocusClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mImageView: SimpleDraweeView
        var mTitleTextView: TextView

        init {
            mConvertView = convertView
            mImageView = convertView.list_item_focus_image_view
            mTitleTextView = convertView.list_item_focus_title_text_view
        }
    }
}