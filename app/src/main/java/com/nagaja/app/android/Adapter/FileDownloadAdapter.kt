package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.FileData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_file_download.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*


class FileDownloadAdapter(context: Context): RecyclerView.Adapter<FileDownloadAdapter.ViewHolder>() {
    private var mFileListData: ArrayList<FileData> = ArrayList()
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
        fun onClick(data: FileData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<FileData>) {
        if (null != mFileListData) {
            mFileListData.clear()
        }
        
        val itemList: ArrayList<FileData> = ArrayList<FileData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mFileListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mFileListData) {
            val data: FileData = mFileListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mFileListData) {
            mFileListData.size
        } else 0
    }

    fun getItem(position: Int): FileData? {
        if (null != mFileListData) {
            if (0 <= position && position < mFileListData.size) {
                return mFileListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_file_download,
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
        val data: FileData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getBoardFileName())) {
            viewHolder.mFileNameTextView.text = data.getBoardFileName()
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mFileNameTextView: TextView

        init {
            mConvertView = convertView
            mFileNameTextView = convertView.list_item_file_download_name_text_view
        }
    }
}