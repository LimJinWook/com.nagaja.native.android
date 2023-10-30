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
import com.nagaja.app.android.Data.LocationAreaMainData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_main_nation.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*


class MainNationAdapter(context: Context): RecyclerView.Adapter<MainNationAdapter.ViewHolder>() {
    private var mLocationAreaMainListData: ArrayList<LocationAreaMainData> = ArrayList()
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
        fun onClick(data: LocationAreaMainData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<LocationAreaMainData>) {
        if (null != mLocationAreaMainListData) {
            mLocationAreaMainListData.clear()
        }
        
        val itemList: ArrayList<LocationAreaMainData> = ArrayList<LocationAreaMainData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mLocationAreaMainListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mLocationAreaMainListData) {
            val data: LocationAreaMainData = mLocationAreaMainListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mLocationAreaMainListData) {
            mLocationAreaMainListData.size
        } else 0
    }

    fun getItem(position: Int): LocationAreaMainData? {
        if (null != mLocationAreaMainListData) {
            if (0 <= position && position < mLocationAreaMainListData.size) {
                return mLocationAreaMainListData[position]
            }
        }
        return null
    }

    fun setLocationAreaMainData(position: Int) {
        for (i in 0 until mLocationAreaMainListData.size) {
            mLocationAreaMainListData[i].setIsSelect(i == position)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
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
        val data: LocationAreaMainData = getItem(position) ?: return

        // Main Nation Text View
        if (!TextUtils.isEmpty(data.getCategoryName())) {
            viewHolder.mMainNationTextView.text = data.getCategoryName()

            if (data.getIsSelect()) {
                viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.bg_color_2177e4))
                viewHolder.mMainNationTextView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_f4f8fd))
            } else {
                viewHolder.mMainNationTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_222222))
                viewHolder.mMainNationTextView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_ffffff))
            }
        }

        // Convert View
        viewHolder.mConvertView.setOnClickListener {
            mOnItemClickListener!!.onClick(data, position)
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