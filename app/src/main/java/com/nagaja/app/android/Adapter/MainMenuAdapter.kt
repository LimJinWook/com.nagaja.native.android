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
import com.nagaja.app.android.Data.MainMenuItemData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*


class MainMenuAdapter(context: Context, isFocus: Boolean): RecyclerView.Adapter<MainMenuAdapter.ViewHolder>() {
    private var mMainMenuItemListData: ArrayList<MainMenuItemData> = ArrayList()
    private var mContext: Context? = null
    private var mIsFocus: Boolean = false
    private var mInflater: LayoutInflater? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }

        mIsFocus = isFocus
    }

    interface OnItemClickListener {
        fun onClick(data: MainMenuItemData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<MainMenuItemData>) {
        if (null != mMainMenuItemListData) {
            mMainMenuItemListData.clear()
        }
        
        val itemList: ArrayList<MainMenuItemData> = ArrayList<MainMenuItemData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }

                if (!item.getIsUseYn()) {
                    continue
                }

                itemList.add(item)
            }
        }
        mMainMenuItemListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mMainMenuItemListData) {
            val data: MainMenuItemData = mMainMenuItemListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mMainMenuItemListData) {
            mMainMenuItemListData.size
        } else 0
    }

    fun getItem(position: Int): MainMenuItemData? {
        if (null != mMainMenuItemListData) {
            if (0 <= position && position < mMainMenuItemListData.size) {
                return mMainMenuItemListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            if (mIsFocus) R.layout.list_item_focus else R.layout.list_item_focus_2,
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

        val data: MainMenuItemData = getItem(position) ?: return

        if (mIsFocus) {
            viewHolder.mEmptyView.visibility = if (position == 0) View.VISIBLE else View.GONE
        } else {
            viewHolder.mEmptyView.visibility = if (position == 0 || position == 1) View.VISIBLE else View.GONE
        }

        if (!TextUtils.isEmpty(data.getMenuImageName())) {
            viewHolder.mImageView.setImageURI(Uri.parse(data.getMenuImageName()))
        }

        if (!TextUtils.isEmpty(data.getMenuName())) {
            viewHolder.mTitleTextView.text = data.getMenuName()
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mEmptyView: View
        var mImageView: SimpleDraweeView
        var mTitleTextView: TextView

        init {
            mConvertView = convertView
            mEmptyView = convertView.list_item_focus_empty_view
            mImageView = convertView.list_item_focus_image_view
            mTitleTextView = convertView.list_item_focus_title_text_view
        }
    }
}