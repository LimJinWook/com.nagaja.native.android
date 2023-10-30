package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Base.NagajaFragment.Companion.USED_MARKET_STATUS_CANCEL
import com.nagaja.app.android.Base.NagajaFragment.Companion.USED_MARKET_STATUS_COMPLETE
import com.nagaja.app.android.Base.NagajaFragment.Companion.USED_MARKET_STATUS_PROGRESS
import com.nagaja.app.android.Base.NagajaFragment.Companion.USED_MARKET_STATUS_REGISTER
import com.nagaja.app.android.Base.NagajaFragment.Companion.USED_MARKET_STATUS_RETURN
import com.nagaja.app.android.Data.UsedMarketCurrencyData
import com.nagaja.app.android.Data.UsedMarketData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_THUMB

import kotlinx.android.synthetic.main.fragment_used_market_list.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_main_nation.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_used_market.view.*
import java.io.IOException
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


class UsedMarketAdapter(context: Context): RecyclerView.Adapter<UsedMarketAdapter.ViewHolder>() {
    private var mUsedMarketListData: ArrayList<UsedMarketData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private lateinit var mUsedMarketCurrencyPaymentAdapter: UsedMarketCurrencyPaymentAdapter

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnItemClickListener {
        fun onClick(itemUid: Int, companyUid: Int, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<UsedMarketData>) {
        if (null != mUsedMarketListData) {
            mUsedMarketListData.clear()
        }
        
        val itemList: ArrayList<UsedMarketData> = ArrayList<UsedMarketData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mUsedMarketListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mUsedMarketListData) {
            val data: UsedMarketData = mUsedMarketListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mUsedMarketListData) {
            mUsedMarketListData.size
        } else 0
    }

    fun getItem(position: Int): UsedMarketData? {
        if (null != mUsedMarketListData) {
            if (0 <= position && position < mUsedMarketListData.size) {
                return mUsedMarketListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun deleteItem(position: Int) {
        mUsedMarketListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
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

    fun replaceItem(item: UsedMarketData, position: Int) {
        mUsedMarketListData.add(position, item)
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_used_market,
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
        val data: UsedMarketData = getItem(position) ?: return

        mUsedMarketCurrencyPaymentAdapter = UsedMarketCurrencyPaymentAdapter(mContext!!)

//        if (data.getImageListData().size > 0) {
//            viewHolder.mContentImageView.visibility = View.VISIBLE
//            if (!TextUtils.isEmpty(data.getImageListData()[0].getItemImageName())) {
//                viewHolder.mContentImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getImageListData()[0].getItemImageName()))
//            }
//        } else {
//            viewHolder.mContentImageView.visibility = View.GONE
//        }

        if (data.getImageListData().size > 0) {
            if (!TextUtils.isEmpty(data.getImageListData()[0].getItemImageName())) {
                viewHolder.mContentImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getImageListData()[0].getItemImageName()))
            } else {
                viewHolder.mContentImageView.setImageURI(Uri.parse("res:///" + R.drawable.bg_default))
            }
        } else {
            viewHolder.mContentImageView.setImageURI(Uri.parse("res:///" + R.drawable.bg_default))
        }

        var status = ""
        when               (data.getItemStatus()) {
            USED_MARKET_STATUS_REGISTER -> {
                status = mContext!!.resources.getString(R.string.text_register)
                viewHolder.mContainerView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_ffffff))
            }

            USED_MARKET_STATUS_CANCEL -> {
                status = mContext!!.resources.getString(R.string.text_register_cancel)
                viewHolder.mContainerView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_ffffff))
            }

            USED_MARKET_STATUS_PROGRESS -> {
                status = mContext!!.resources.getString(R.string.text_preparing_for_sale)
                viewHolder.mContainerView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_f5f5f5))
            }

            USED_MARKET_STATUS_COMPLETE -> {
                status = mContext!!.resources.getString(R.string.text_used_market_list_sale_on_sale)
                viewHolder.mContainerView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_ffffff))
            }

            USED_MARKET_STATUS_RETURN -> {
                status = mContext!!.resources.getString(R.string.text_used_market_list_sale_complete_sale)
                viewHolder.mContainerView.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.bg_color_f5f5f5))
            }
        }
        viewHolder.mSalePurchaseTextView.text = status

        if (!TextUtils.isEmpty(data.getItemSubject())) {
            viewHolder.mTitleTextView.text = data.getItemSubject()
        }

        var mainArea = ""
        var subArea = ""
        if (!TextUtils.isEmpty(data.getSiDoName())) {
            mainArea = data.getSiDoName()
        }
        if (!TextUtils.isEmpty(data.getGuGunName())) {
            subArea = data.getGuGunName()
        }
        viewHolder.mAddressTextView.text = "$mainArea $subArea"

        if (data.getCurrencyListData().size > 0) {
            viewHolder.mCurrencyRecyclerView.setHasFixedSize(true)
            viewHolder.mCurrencyRecyclerView.layoutManager = LinearLayoutManager(mContext!!, LinearLayoutManager.VERTICAL, false)

            viewHolder.mCurrencyRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
            viewHolder.mCurrencyRecyclerView.adapter = mUsedMarketCurrencyPaymentAdapter

            mUsedMarketCurrencyPaymentAdapter.setOnItemClickListener(object : UsedMarketCurrencyPaymentAdapter.OnItemClickListener {
                override fun onClick() {
                    mOnItemClickListener!!.onClick(data.getItemUid(), data.getCompanyUid(), position)
                }
            })

            val currencyListData = ArrayList<UsedMarketCurrencyData>()
//            val usedMarketCurrencyData = UsedMarketCurrencyData()
//            usedMarketCurrencyData.setCurrencyCode(data.getItemCurrencyCode())
//            usedMarketCurrencyData.setCurrencyPrice(data.getItemPrice())
//            currencyListData.add(usedMarketCurrencyData)
            for (i in data.getCurrencyListData().indices) {
                if (data.getCurrencyListData()[i].getCurrencyPrice() != 0.0) {
                    currencyListData.add(data.getCurrencyListData()[i])
                }
            }
            mUsedMarketCurrencyPaymentAdapter.setData(currencyListData)
        }


        if (!TextUtils.isEmpty(data.getCreateDate())) {
            var time = data.getCreateDate()
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            viewHolder.mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnItemClickListener!!.onClick(data.getItemUid(), data.getCompanyUid(), position)
        }

    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mContainerView: View
        var mContentImageView: SimpleDraweeView
        var mSalePurchaseTextView: TextView
        var mTitleTextView: TextView
        var mAddressTextView: TextView
        var mDateTextView: TextView
        var mCurrencyRecyclerView: RecyclerView

        init {
            mConvertView = convertView
            mContainerView = convertView.list_item_used_market_view
            mContentImageView = convertView.list_item_used_market_image_view
            mSalePurchaseTextView = convertView.list_item_used_market_sale_purchase_text_view
            mTitleTextView = convertView.list_item_used_market_title_text_view
            mAddressTextView = convertView.list_item_used_market_address_text_view
            mDateTextView = convertView.list_item_used_market_date_text_view
            mCurrencyRecyclerView = convertView.list_item_used_market_payment_recycler_view
        }
    }
}