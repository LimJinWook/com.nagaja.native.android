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
import com.nagaja.app.android.Data.StoreRecommendMenuData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*


class StoreRecommendMenuAdapter(context: Context): RecyclerView.Adapter<StoreRecommendMenuAdapter.ViewHolder>() {
    private var mStoreRecommendMenuListData: ArrayList<StoreRecommendMenuData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnStoreRecommendMenuClickListener: OnStoreRecommendMenuClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnStoreRecommendMenuClickListener {
        fun onClick(data: StoreRecommendMenuData)
    }

    fun setOnStoreRecommendMenuClickListener(listener: OnStoreRecommendMenuClickListener?) {
        mOnStoreRecommendMenuClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<StoreRecommendMenuData>) {
        if (null != mStoreRecommendMenuListData) {
            mStoreRecommendMenuListData.clear()
        }
        
        val itemList: ArrayList<StoreRecommendMenuData> = ArrayList<StoreRecommendMenuData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mStoreRecommendMenuListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mStoreRecommendMenuListData) {
            val data: StoreRecommendMenuData = mStoreRecommendMenuListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mStoreRecommendMenuListData) {
            mStoreRecommendMenuListData.size
        } else 0
    }

    fun getItem(position: Int): StoreRecommendMenuData? {
        if (null != mStoreRecommendMenuListData) {
            if (0 <= position && position < mStoreRecommendMenuListData.size) {
                return mStoreRecommendMenuListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_store_recommend_menu,
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
        val data: StoreRecommendMenuData = getItem(position) ?: return

        viewHolder.mMarjinView.visibility = if (position == 0) View.VISIBLE else View.GONE

        if (!TextUtils.isEmpty(data.getMenuImageUrl())) {
            viewHolder.mMenuImageView.setImageURI(Uri.parse(data.getMenuImageUrl()))
        }

        if (!TextUtils.isEmpty(data.getName())) {
            viewHolder.mNameTextView.text = data.getName()
            viewHolder.mNameTextView.isSelected = true
        }

        if (!TextUtils.isEmpty(data.getPrice())) {
            viewHolder.mPriceTextView.text = data.getPrice() + " ￦"
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mMarjinView: View
        var mMenuImageView: SimpleDraweeView
        var mNameTextView: TextView
        var mPriceTextView: TextView

        init {
            mConvertView = convertView
            mMarjinView = convertView.list_item_store_main_recommend_margin_view
            mMenuImageView = convertView.list_item_store_recommend_menu_image_view
            mNameTextView = convertView.list_item_store_recommend_menu_name_text_view
            mPriceTextView = convertView.list_item_store_recommend_menu_price_text_view
        }
    }
}