package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Data.CompanyProductData
import com.nagaja.app.android.Data.SearchCurrencyData
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.NagajaLog

import kotlinx.android.synthetic.main.list_item_company_product_item.view.*
import kotlinx.android.synthetic.main.list_item_search.view.*


class CompanyProductItemAdapter(context: Context): RecyclerView.Adapter<CompanyProductItemAdapter.ViewHolder>() {
    private var mCompanyProductListData: ArrayList<CompanyProductData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    private lateinit var mCurrencyAdapter: CurrencyAdapter

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnItemClickListener {
        fun onModify(data: CompanyProductData)
        fun onDelete(data: CompanyProductData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<CompanyProductData>) {
        if (null != mCompanyProductListData) {
            mCompanyProductListData.clear()
        }
        
        val itemList: ArrayList<CompanyProductData> = ArrayList<CompanyProductData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mCompanyProductListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mCompanyProductListData) {
            val data: CompanyProductData = mCompanyProductListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mCompanyProductListData) {
            mCompanyProductListData.size
        } else 0
    }

    fun getItem(position: Int): CompanyProductData? {
        if (null != mCompanyProductListData) {
            if (0 <= position && position < mCompanyProductListData.size) {
                return mCompanyProductListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_company_product_item,
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
        val data: CompanyProductData = getItem(position) ?: return

        mCurrencyAdapter = CurrencyAdapter(mContext!!)

        viewHolder.mLineView.visibility = if (position == 0) View.GONE else View.VISIBLE

        if (data.getImageListData().size > 0) {
            if (!TextUtils.isEmpty(data.getImageListData()[0].getItemImageName())) {
                viewHolder.mProductImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getImageListData()[0].getItemImageName()))
            }
        }

        if (!TextUtils.isEmpty(data.getItemName())) {
            viewHolder.mNameTextView.text = data.getItemName()
        }

        if (!TextUtils.isEmpty(data.getItemContent())) {
            viewHolder.mContentTextView.text = data.getItemContent()
        }

//        if (!TextUtils.isEmpty(data.getItemPrice())) {
//            viewHolder.mPriceTextView.text = data.getItemCurrencyCode() + " " + Util().getTwoDecimalPlaces(data.getItemPrice().toDouble())
//        } else {
//            if (data.getCurrencyListData().size > 0) {
//                var currencyCode = ""
//                if (data.getCurrencyListData()[0].getCurrencyCode() == "USD") {
//                    currencyCode = mContext!!.resources.getString(R.string.text_currency_dollar)
//                } else if (data.getCurrencyListData()[0].getCurrencyCode() == "KRW") {
//                    currencyCode = mContext!!.resources.getString(R.string.text_currency_won)
//                } else if (data.getCurrencyListData()[0].getCurrencyCode() == "PHP") {
//                    currencyCode = mContext!!.resources.getString(R.string.text_currency_peso)
//                } else if (data.getCurrencyListData()[0].getCurrencyCode() == "CNY") {
//                    currencyCode = mContext!!.resources.getString(R.string.text_currency_yuan)
//                } else if (data.getCurrencyListData()[0].getCurrencyCode() == "JPY") {
//                    currencyCode = mContext!!.resources.getString(R.string.text_currency_yen)
//                }
//
//                viewHolder.mPriceTextView.text = currencyCode + " " + Util().getTwoDecimalPlaces(data.getCurrencyListData()[0].getCurrencyPrice())
//            }
//        }

        if (data.getCurrencyListData().size > 0) {

            viewHolder.mCurrencyRecyclerView.setHasFixedSize(true)
            viewHolder.mCurrencyRecyclerView.layoutManager = LinearLayoutManager(mContext!!, LinearLayoutManager.HORIZONTAL, false)

            viewHolder.mCurrencyRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
            viewHolder.mCurrencyRecyclerView.adapter = mCurrencyAdapter

            val searchCurrencyListData = ArrayList<SearchCurrencyData>()
            for (i in data.getCurrencyListData().indices) {
                if (data.getCurrencyListData()[i].getCurrencyPrice() > 0) {
                    val searchCurrencyData = SearchCurrencyData()
                    searchCurrencyData.setCode(data.getCurrencyListData()[i].getCurrencyCode())
                    searchCurrencyData.setPrice(data.getCurrencyListData()[i].getCurrencyPrice())

                    searchCurrencyListData.add(searchCurrencyData)
                }
            }

            mCurrencyAdapter.setData(searchCurrencyListData)
        }

        viewHolder.mModifyTextView.setOnClickListener {
            mOnItemClickListener!!.onModify(data)
        }

        viewHolder.mDeleteTextView.setOnClickListener {
            mOnItemClickListener!!.onDelete(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mProductImageView: SimpleDraweeView
        var mNameTextView: TextView
        var mContentTextView: TextView
        var mModifyTextView: TextView
        var mDeleteTextView: TextView
        var mCurrencyRecyclerView: RecyclerView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_company_product_item_line_view
            mProductImageView = convertView.list_item_company_product_item_image_view
            mNameTextView = convertView.list_item_company_product_item_name_text_view
            mContentTextView = convertView.list_item_company_product_item_content_text_view
            mModifyTextView = convertView.list_item_company_product_item_modify_text_view
            mDeleteTextView = convertView.list_item_company_product_item_delete_text_view
            mCurrencyRecyclerView = convertView.list_item_company_product_item_currency_recycler_view
        }
    }
}