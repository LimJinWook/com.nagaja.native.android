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
import com.nagaja.app.android.Data.StoreCategoryData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_store_category.view.*


class StoreCategoryAdapter(context: Context): RecyclerView.Adapter<StoreCategoryAdapter.ViewHolder>() {
    private var mStoreCategoryListData: ArrayList<StoreCategoryData> = ArrayList()
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
        fun onClick(data: StoreCategoryData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<StoreCategoryData>) {
        if (null != mStoreCategoryListData) {
            mStoreCategoryListData.clear()
        }
        
        val itemList: ArrayList<StoreCategoryData> = ArrayList<StoreCategoryData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mStoreCategoryListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mStoreCategoryListData) {
            val data: StoreCategoryData = mStoreCategoryListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mStoreCategoryListData) {
            mStoreCategoryListData.size
        } else 0
    }

    fun getItem(position: Int): StoreCategoryData? {
        if (null != mStoreCategoryListData) {
            if (0 <= position && position < mStoreCategoryListData.size) {
                return mStoreCategoryListData[position]
            }
        }
        return null
    }

    fun setSelectCategory(position: Int) {
        for (i in 0 until mStoreCategoryListData.size) {
            if (i == position) {
                mStoreCategoryListData[i].setIsSelect(true)
            } else {
                mStoreCategoryListData[i].setIsSelect(false)
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_store_category,
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
        val data: StoreCategoryData = getItem(position) ?: return

        viewHolder.mEmptyView.visibility = if (position == 0) View.GONE else View.VISIBLE

        if (!TextUtils.isEmpty(data.getCategoryName())) {
            viewHolder.mCategoryTextView.text = data.getCategoryName()
            if (data.getIsSelect()) {
                viewHolder.mCategoryTextView.setBackgroundResource(R.drawable.bg_category_boarder_select)
                viewHolder.mCategoryTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.bg_color_0d4d97))
            } else {
                viewHolder.mCategoryTextView.setBackgroundResource(R.drawable.bg_category_boarder)
                viewHolder.mCategoryTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_767676))
            }
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnItemClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mCategoryTextView: TextView
        var mEmptyView: View

        init {
            mConvertView = convertView
            mCategoryTextView = convertView.list_item_store_category_text_view
            mEmptyView = convertView.list_item_store_category_empty_view
        }
    }
}