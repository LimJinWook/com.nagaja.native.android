package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.CompanyManagerData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_company_sales_information.view.*


class CompanySalesManagerAdapter(context: Context): RecyclerView.Adapter<CompanySalesManagerAdapter.ViewHolder>() {
    private var mCompanyManagerListData: ArrayList<CompanyManagerData> = ArrayList()
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
        fun onClick(data: CompanyManagerData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<CompanyManagerData>) {
        if (null != mCompanyManagerListData) {
            mCompanyManagerListData.clear()
        }
        
        val itemList: ArrayList<CompanyManagerData> = ArrayList<CompanyManagerData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mCompanyManagerListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mCompanyManagerListData) {
            val data: CompanyManagerData = mCompanyManagerListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mCompanyManagerListData) {
            mCompanyManagerListData.size
        } else 0
    }

    fun getItem(position: Int): CompanyManagerData? {
        if (null != mCompanyManagerListData) {
            if (0 <= position && position < mCompanyManagerListData.size) {
                return mCompanyManagerListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_company_sales_information,
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

        val data: CompanyManagerData = getItem(position) ?: return

        if (!TextUtils.isEmpty(data.getMemberName())) {
            viewHolder.mNameTextView.text = data.getMemberName()
        }

        var date = ""
        if (!TextUtils.isEmpty(data.getUpdateDate()) && data.getUpdateDate() != "null") {
            val index: Int = data.getUpdateDate().indexOf("T")
            date = data.getUpdateDate().substring(0, index).replace("-", ".")
        } else if (!TextUtils.isEmpty(data.getCreateDate()) && data.getUpdateDate() == "null") {
            val index: Int = data.getCreateDate().indexOf("T")
            date = data.getCreateDate().substring(0, index).replace("-", ".")
        }
        viewHolder.mDateTextView.text = date
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mNameTextView: TextView
        var mDateTextView: TextView

        init {
            mConvertView = convertView
            mNameTextView = convertView.list_item_company_sales_information_name_text_view
            mDateTextView = convertView.list_item_company_sales_information_date_text_view
        }
    }
}