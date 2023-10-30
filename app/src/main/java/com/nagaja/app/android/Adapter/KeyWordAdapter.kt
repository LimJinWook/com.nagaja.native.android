package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.RoomDB.KeyWord.Contacts
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_key_word.view.*
import java.util.*


class KeyWordAdapter(context: Context): RecyclerView.Adapter<KeyWordAdapter.ViewHolder>() {
    private var mContactsListData: ArrayList<Contacts> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnKeyWordDeleteClickListener: OnKeyWordDeleteClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnKeyWordDeleteClickListener {
        fun onClick(data: Contacts)
    }

    fun setOnKeyWordDeleteClickListener(listener: OnKeyWordDeleteClickListener?) {
        mOnKeyWordDeleteClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<Contacts>) {
        if (null != mContactsListData) {
            mContactsListData.clear()
        }
        
        val itemList: ArrayList<Contacts> = ArrayList<Contacts>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mContactsListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mContactsListData) {
            val data: Contacts = mContactsListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mContactsListData) {
            mContactsListData.size
        } else 0
    }

    fun getItem(position: Int): Contacts? {
        if (null != mContactsListData) {
            if (0 <= position && position < mContactsListData.size) {
                return mContactsListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mContactsListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_key_word,
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
        val data: Contacts = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.keyWord)) {
            viewHolder.mKeyWordTextView.text = data.keyWord
        }

        viewHolder.mDeleteImageView.setOnSingleClickListener {
            mOnKeyWordDeleteClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mKeyWordTextView: TextView
        var mDeleteImageView: ImageView

        init {
            mConvertView = convertView
            mKeyWordTextView = convertView.list_item_key_word_text_view
            mDeleteImageView = convertView.list_item_key_word_delete_image_view
        }
    }
}