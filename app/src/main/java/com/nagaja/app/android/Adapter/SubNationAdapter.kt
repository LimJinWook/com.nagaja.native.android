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
import com.nagaja.app.android.Data.LocationAreaSubData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_main_nation.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*


class SubNationAdapter(context: Context): RecyclerView.Adapter<SubNationAdapter.ViewHolder>() {
    private var mLocationAreaSubListData: ArrayList<LocationAreaSubData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnSubNationClickListener: OnSubNationClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnSubNationClickListener {
        fun onClick(data: LocationAreaSubData, position: Int)
    }

    fun setOnSubNationClickListener(listener: OnSubNationClickListener?) {
        mOnSubNationClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<LocationAreaSubData>) {
        if (null != mLocationAreaSubListData) {
            mLocationAreaSubListData.clear()
        }
        
        val itemList: ArrayList<LocationAreaSubData> = ArrayList<LocationAreaSubData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mLocationAreaSubListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mLocationAreaSubListData) {
            val data: LocationAreaSubData = mLocationAreaSubListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mLocationAreaSubListData) {
            mLocationAreaSubListData.size
        } else 0
    }

    fun getItem(position: Int): LocationAreaSubData? {
        if (null != mLocationAreaSubListData) {
            if (0 <= position && position < mLocationAreaSubListData.size) {
                return mLocationAreaSubListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setLocationAreaSubData(position: Int) {
        for (i in 0 until mLocationAreaSubListData.size) {
            mLocationAreaSubListData[i].setIsSelect(i == position)
        }

        notifyDataSetChanged()
    }

    fun clear() {
        mLocationAreaSubListData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_main_nation,
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
        val data: LocationAreaSubData = getItem(position) ?: return

        // Main Nation Text View
        if (!TextUtils.isEmpty(data.getCategoryName())) {
            viewHolder.mMainNationTextView.text = data.getCategoryName()

            if (data.getIsSelect()) {
//                viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.bg_color_2177e4))
                viewHolder.mMainNationTextView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_eaf1fb))

                if (data.getIsCategoryStatusEnable()) {
                    viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_222222))
                } else {
                    viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_bebebe))
                }

            } else {
//                viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_222222))
                viewHolder.mMainNationTextView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_f4f8fd))

                if (data.getIsCategoryStatusEnable()) {
                    viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_222222))
                } else {
                    viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_bebebe))
                }
            }
        }

        // Convert View
        viewHolder.mConvertView.setOnClickListener {
            mOnSubNationClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mMainNationTextView: TextView

        init {
            mConvertView = convertView
            mMainNationTextView = convertView.list_item_main_nation_text_view
        }
    }
}