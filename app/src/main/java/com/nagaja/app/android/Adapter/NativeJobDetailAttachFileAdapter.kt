package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.NativeBoardFileData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_attach_file.view.*

class NativeJobDetailAttachFileAdapter(context: Context): RecyclerView.Adapter<NativeJobDetailAttachFileAdapter.ViewHolder>() {
    private var mNativeBoardFileListData: ArrayList<NativeBoardFileData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnAttachFileItemClickListener: OnAttachFileItemClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnAttachFileItemClickListener {
        fun onClick(data: NativeBoardFileData)
    }

    fun setOnAttachFileItemClickListener(listener: OnAttachFileItemClickListener?) {
        mOnAttachFileItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NativeBoardFileData>) {
        if (null != mNativeBoardFileListData) {
            mNativeBoardFileListData.clear()
        }
        
        val itemList: ArrayList<NativeBoardFileData> = ArrayList<NativeBoardFileData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mNativeBoardFileListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNativeBoardFileListData) {
            val data: NativeBoardFileData = mNativeBoardFileListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNativeBoardFileListData) {
            mNativeBoardFileListData.size
        } else 0
    }

    fun getItem(position: Int): NativeBoardFileData? {
        if (null != mNativeBoardFileListData) {
            if (0 <= position && position < mNativeBoardFileListData.size) {
                return mNativeBoardFileListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_attach_file,
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
        val data: NativeBoardFileData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getBoardFileName())) {
            viewHolder.mAttachFileNameTextView.text = data.getBoardFileOrigin()
        }

        viewHolder.mAttachFileView.setOnClickListener {
             mOnAttachFileItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mAttachFileView: View
        var mAttachFileNameTextView: TextView

        init {
            mConvertView = convertView
            mAttachFileView = convertView.list_item_attach_file_view
            mAttachFileNameTextView = convertView.list_item_attach_file_name_text_view
        }
    }
}