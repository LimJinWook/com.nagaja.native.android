package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_note_box.view.*
import kotlinx.android.synthetic.main.list_item_note_detail_image.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_reservation.view.*
import kotlinx.android.synthetic.main.list_item_reservation_category.view.*


class NoteDetailImageAdapter(context: Context): RecyclerView.Adapter<NoteDetailImageAdapter.ViewHolder>() {
    private var mNoteDetailImageListData: ArrayList<String> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnNoteDetailImageClickListener: OnNoteDetailImageClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnNoteDetailImageClickListener {
        fun onClick(position: Int)
    }

    fun setOnNoteDetailImageClickListener(listener: OnNoteDetailImageClickListener?) {
        mOnNoteDetailImageClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<String>) {
        if (null != mNoteDetailImageListData) {
            mNoteDetailImageListData.clear()
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
        mNoteDetailImageListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNoteDetailImageListData) {
            val data: String = mNoteDetailImageListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNoteDetailImageListData) {
            mNoteDetailImageListData.size
        } else 0
    }

    fun getItem(position: Int): String? {
        if (null != mNoteDetailImageListData) {
            if (0 <= position && position < mNoteDetailImageListData.size) {
                return mNoteDetailImageListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_note_detail_image,
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

        if (position == 0) {
            viewHolder.mEmptyView.visibility = View.VISIBLE
        } else {
            viewHolder.mEmptyView.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(data)) {
            viewHolder.mImageView.setImageURI(Uri.parse(data))
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnNoteDetailImageClickListener!!.onClick(position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mEmptyView: View
        var mImageView: ImageView

        init {
            mConvertView = convertView
            mEmptyView = convertView.list_item_note_detail_empty_view
            mImageView = convertView.list_item_note_detail_image_view
        }
    }
}