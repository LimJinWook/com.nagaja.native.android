package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Data.NativeImageUploadData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_image_upload.view.*
import kotlinx.android.synthetic.main.list_item_image_upload_select.view.*


class NativeImageUploadAdapter(context: Context, isCompany: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mNativeImageUploadListData: ArrayList<NativeImageUploadData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mIsCompany = false

    init {
        mContext = context
        mIsCompany = isCompany
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    companion object {
        const val VIEW_TYPE_SELECT          = 0x00
        const val VIEW_TYPE_IMAGE           = 0x01
    }

    interface OnItemClickListener {
        fun onClick(data: NativeImageUploadData, position: Int)
        fun onSelectImage()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NativeImageUploadData>) {
        if (null != mNativeImageUploadListData) {
            mNativeImageUploadListData.clear()
        }
        
        val itemList: ArrayList<NativeImageUploadData> = ArrayList<NativeImageUploadData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mNativeImageUploadListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNativeImageUploadListData) {
            val data: NativeImageUploadData = mNativeImageUploadListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNativeImageUploadListData) {
            mNativeImageUploadListData.size
        } else 0
    }

    override fun getItemViewType(position: Int): Int {
        return mNativeImageUploadListData[position].getViewType()
    }

    fun getItem(position: Int): NativeImageUploadData? {
        if (null != mNativeImageUploadListData) {
            if (0 <= position && position < mNativeImageUploadListData.size) {
                return mNativeImageUploadListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mNativeImageUploadListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View

        if (viewType == VIEW_TYPE_SELECT) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_image_upload_select, parent, false)
            return SelectViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_image_upload, parent, false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (null == viewHolder) {
            return
        }
        val data: NativeImageUploadData = getItem(position) ?: return

        if (viewHolder is ViewHolder) {
            try {
                if (data.getIsDeviceImage()) {
                    val path = Util().getPathFromUri(mContext!!, data.getImageUri())
                    if (!TextUtils.isEmpty(path)) {
                        viewHolder.mImageView.setImageURI(data.getImageUri())
                    }
                } else {
                    if (!TextUtils.isEmpty(data.getImageUrl())) {
                        viewHolder.mImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getImageUrl()))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewHolder.mImageView.setImageURI(data.getImageUri())
            }

            viewHolder.mDeleteImageView.setOnClickListener {
                mOnItemClickListener!!.onClick(data, position)
            }
        } else if (viewHolder is SelectViewHolder) {
            viewHolder.mCountTextView.text = if (mIsCompany) String.format(mContext!!.resources.getString(R.string.text_image_upload_count_10), itemCount - 1)
            else String.format(mContext!!.resources.getString(R.string.text_image_upload_count_5), itemCount - 1)

            viewHolder.mSelectView.setOnClickListener {
                mOnItemClickListener!!.onSelectImage()
            }
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mImageView: SimpleDraweeView
        var mDeleteImageView: ImageView

        init {
            mConvertView = convertView
            mImageView = convertView.list_item_image_upload_image_view
            mDeleteImageView = convertView.list_item_image_upload_delete_image_view
        }
    }

    class SelectViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mSelectView: View
        var mCountTextView: TextView

        init {
            mSelectView = convertView
            mCountTextView = convertView.list_item_image_upload_count_text_view
        }
    }
}