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
import com.nagaja.app.android.Data.CategoryData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_reservation_category.view.*


class CategoryAdapter(context: Context): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var mCategoryListData: ArrayList<CategoryData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnCategoryClickListener: OnCategoryClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnCategoryClickListener {
        fun onClick(data: CategoryData, position: Int)
    }

    fun setOnCategoryClickListener(listener: OnCategoryClickListener?) {
        mOnCategoryClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<CategoryData>) {
        if (null != mCategoryListData) {
            mCategoryListData.clear()
        }
        
        val itemList: ArrayList<CategoryData> = ArrayList<CategoryData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mCategoryListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mCategoryListData) {
            val data: CategoryData = mCategoryListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mCategoryListData) {
            mCategoryListData.size
        } else 0
    }

    fun getItem(position: Int): CategoryData? {
        if (null != mCategoryListData) {
            if (0 <= position && position < mCategoryListData.size) {
                return mCategoryListData[position]
            }
        }
        return null
    }

    fun setSelectCategory(position: Int) {
        for (i in 0 until mCategoryListData.size) {
            if (i == position) {
                mCategoryListData[i].setIsSelect(true)
            } else {
                mCategoryListData[i].setIsSelect(false)
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_reservation_category,
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
        val data: CategoryData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getTitle())) {
            viewHolder.mCategoryTextView.text = data.getTitle()
            if (data.getIsSelect()) {
                viewHolder.mCategoryTextView.setBackgroundResource(R.drawable.bg_category_boarder_select)
                viewHolder.mCategoryTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.bg_color_0d4d97))
            } else {
                viewHolder.mCategoryTextView.setBackgroundResource(R.drawable.bg_category_boarder)
                viewHolder.mCategoryTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_767676))
            }
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnCategoryClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mCategoryTextView: TextView

        init {
            mConvertView = convertView
            mCategoryTextView = convertView.list_item_reservation_category_text_view
        }
    }
}