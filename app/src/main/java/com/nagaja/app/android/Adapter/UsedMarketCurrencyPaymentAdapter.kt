package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_CNY
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_JPY
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_KRW
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_PHP
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_USD
import com.nagaja.app.android.Data.UsedMarketCurrencyData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_main_nation.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_used_market.view.*
import kotlinx.android.synthetic.main.list_item_used_market_currency_payment.view.*
import java.io.IOException
import java.util.*


class UsedMarketCurrencyPaymentAdapter(context: Context): RecyclerView.Adapter<UsedMarketCurrencyPaymentAdapter.ViewHolder>() {
    private var mUsedMarketCurrencyListData: ArrayList<UsedMarketCurrencyData> = ArrayList()
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
        fun onClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<UsedMarketCurrencyData>) {
        if (null != mUsedMarketCurrencyListData) {
            mUsedMarketCurrencyListData.clear()
        }
        
        val itemList: ArrayList<UsedMarketCurrencyData> = ArrayList<UsedMarketCurrencyData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mUsedMarketCurrencyListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mUsedMarketCurrencyListData) {
            val data: UsedMarketCurrencyData = mUsedMarketCurrencyListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mUsedMarketCurrencyListData) {
            mUsedMarketCurrencyListData.size
        } else 0
    }

    fun getItem(position: Int): UsedMarketCurrencyData? {
        if (null != mUsedMarketCurrencyListData) {
            if (0 <= position && position < mUsedMarketCurrencyListData.size) {
                return mUsedMarketCurrencyListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun getAddress(lan: Double, lon: Double): String {
        var address = ""
        val geocoder = Geocoder(mContext!!, Locale.getDefault())

        (mContext as Activity).runOnUiThread {
            try {
                val addressList = geocoder.getFromLocation(lan, lon, 1)
                if (addressList!!.size != 0) {
                    address = addressList[0].getAddressLine(0)
                    if (address.substring(0, 4) == "대한민국") {
                        address = address.substring(5)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return address
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_used_market_currency_payment,
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
        val data: UsedMarketCurrencyData = getItem(position) ?: return

        var currencyCode = "$"
        if (!TextUtils.isEmpty(data.getCurrencyCode())) {
            when (data.getCurrencyCode()) {
                CURRENCY_TYPE_USD -> {
                    currencyCode = "$"
                }

                CURRENCY_TYPE_KRW -> {
                    currencyCode = "￦"
                }

                CURRENCY_TYPE_PHP -> {
                    currencyCode = "₱"
                }

                CURRENCY_TYPE_CNY -> {
                    currencyCode = "元"
                }

                CURRENCY_TYPE_JPY -> {
                    currencyCode = "¥"
                }
            }
        }

        viewHolder.mCurrencyTextView.text = currencyCode

        viewHolder.mPaymentTextView.text = Util().getTwoDecimalPlaces(data.getCurrencyPrice())

        viewHolder.mConvertView.setOnClickListener {
            mOnItemClickListener!!.onClick()
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mCurrencyTextView: TextView
        var mPaymentTextView: TextView

        init {
            mConvertView = convertView
            mCurrencyTextView = convertView.list_item_used_market_currency_text_view
            mPaymentTextView = convertView.list_item_used_market_payment_text_view
        }
    }
}