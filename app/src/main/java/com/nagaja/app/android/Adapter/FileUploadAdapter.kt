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
import com.nagaja.app.android.Data.FileUploadData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_faq.view.*
import kotlinx.android.synthetic.main.list_item_file_upload.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*


class FileUploadAdapter(context: Context): RecyclerView.Adapter<FileUploadAdapter.ViewHolder>() {
    private var mFilUploadListData: ArrayList<FileUploadData> = ArrayList()
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
        fun onDelete(data: FileUploadData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<FileUploadData>) {
        if (null != mFilUploadListData) {
            mFilUploadListData.clear()
        }
        
        val itemList: ArrayList<FileUploadData> = ArrayList<FileUploadData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mFilUploadListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mFilUploadListData) {
            val data: FileUploadData = mFilUploadListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mFilUploadListData) {
            mFilUploadListData.size
        } else 0
    }

    fun getItem(position: Int): FileUploadData? {
        if (null != mFilUploadListData) {
            if (0 <= position && position < mFilUploadListData.size) {
                return mFilUploadListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mFilUploadListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_file_upload,
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
        val data: FileUploadData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getFileName())) {
            viewHolder.mFileNameTextView.text = data.getFileName()
        }

        viewHolder.mCancelImageView.setOnClickListener {
            mOnItemClickListener!!.onDelete(data, position)
        }

    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mFileNameTextView: TextView
        var mCancelImageView: ImageView

        init {
            mConvertView = convertView
            mFileNameTextView = convertView.list_item_file_upload_file_name_text_view
            mCancelImageView = convertView.list_item_file_upload_cancel_image_viewe
        }
    }
}