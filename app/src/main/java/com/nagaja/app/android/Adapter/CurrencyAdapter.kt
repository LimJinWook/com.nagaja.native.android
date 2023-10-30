package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.SearchCurrencyData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_currency.view.*


class CurrencyAdapter(context: Context): RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {
    private var mSearchCurrencyListData: ArrayList<SearchCurrencyData> = ArrayList()
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
        fun onClick(data: SearchCurrencyData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<SearchCurrencyData>) {
        if (null != mSearchCurrencyListData) {
            mSearchCurrencyListData.clear()
        }
        
        val itemList: ArrayList<SearchCurrencyData> = ArrayList<SearchCurrencyData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mSearchCurrencyListData = itemList

        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mSearchCurrencyListData) {
            val data: SearchCurrencyData = mSearchCurrencyListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mSearchCurrencyListData) {
            mSearchCurrencyListData.size
        } else 0
    }

    fun getItem(position: Int): SearchCurrencyData? {
        if (null != mSearchCurrencyListData) {
            if (0 <= position && position < mSearchCurrencyListData.size) {
                return mSearchCurrencyListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_currency,
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

        val data: SearchCurrencyData = getItem(position) ?: return

        var code = mContext!!.resources.getString(R.string.text_currency_dollar)

        when (data.getCode()) {
            "USD" -> {
                code = mContext!!.resources.getString(R.string.text_currency_dollar)
            }

            "KRW" -> {
                code = mContext!!.resources.getString(R.string.text_currency_won)
            }

            "PHP" -> {
                code = mContext!!.resources.getString(R.string.text_currency_peso)
            }

            "CNY" -> {
                code = mContext!!.resources.getString(R.string.text_currency_yuan)
            }

            "JPY" -> {
                code = mContext!!.resources.getString(R.string.text_currency_yen)
            }
        }

        viewHolder.mCodeTextView.text = code

//        if (!TextUtils.isEmpty(data.getCurrencyPriceString())) {
//            viewHolder.mPriceTextView.text = /*Util().unitConvert(data.getCurrencyPriceString())*/data.getCurrencyPriceString()
//        }

        if (data.getPrice() >= 0.0) {
            viewHolder.mPriceTextView.text = Util().getTwoDecimalPlaces(data.getPrice())
        }


    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mCodeTextView: TextView
        var mPriceTextView : TextView

        init {
            mConvertView = convertView
            mCodeTextView = convertView.list_item_currency_code_text_view
            mPriceTextView = convertView.list_item_currency_price_text_view
        }
    }
}