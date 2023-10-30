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
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_CNY
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_JPY
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_KRW
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_PHP
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_USD
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_EN
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_FIL
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_JA
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_KO
import com.nagaja.app.android.Base.NagajaFragment.Companion.SELECT_LANGUAGE_ZH
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Data.SearchCurrencyData
import com.nagaja.app.android.Data.StoreDetailMenuData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil
import com.nagaja.app.android.Utils.Util

import kotlinx.android.synthetic.main.list_item_company_product_item.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*


class StoreMenuAdapter(context: Context): RecyclerView.Adapter<StoreMenuAdapter.ViewHolder>() {
    private var mStoreDetailMenuListData: ArrayList<StoreDetailMenuData> = ArrayList()
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
        fun onClick(data: StoreDetailMenuData)
        fun onImageClick(imageList: ArrayList<String>, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<StoreDetailMenuData>) {
        if (null != mStoreDetailMenuListData) {
            mStoreDetailMenuListData.clear()
        }
        
        val itemList: ArrayList<StoreDetailMenuData> = ArrayList<StoreDetailMenuData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }

                if (!item.getIsUseYn()) {
                    continue
                }

                itemList.add(item)
            }
        }
        mStoreDetailMenuListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mStoreDetailMenuListData) {
            val data: StoreDetailMenuData = mStoreDetailMenuListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mStoreDetailMenuListData) {
            mStoreDetailMenuListData.size
        } else 0
    }

    fun getItem(position: Int): StoreDetailMenuData? {
        if (null != mStoreDetailMenuListData) {
            if (0 <= position && position < mStoreDetailMenuListData.size) {
                return mStoreDetailMenuListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_store_menu,
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
        val data: StoreDetailMenuData = getItem(position) ?: return

        mCurrencyAdapter = CurrencyAdapter(mContext!!)

        if (data.getImageListData().size > 0) {
            viewHolder.mMenuImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getImageListData()[0].getItemImageName()))
            viewHolder.mMenuImageView.setOnSingleClickListener {
                val imageList = ArrayList<String>()
                imageList.add(DEFAULT_IMAGE_DOMAIN + data.getImageListData()[0].getItemImageName())
                mOnItemClickListener!!.onImageClick(imageList, 0)
            }
        }

        if (!TextUtils.isEmpty(data.getItemName())) {
            viewHolder.mNameTextView.text = data.getItemName()
            viewHolder.mNameTextView.isSelected = true
        }

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

            var mainPrice = ""
            var mainPriceCurrency = ""
            for (i in searchCurrencyListData.indices) {
                if (searchCurrencyListData[i].getCode() == CURRENCY_TYPE_USD && SharedPreferencesUtil().getSelectLanguage(mContext!!) == SELECT_LANGUAGE_EN) {
                    if (!TextUtils.isEmpty(searchCurrencyListData[i].getPrice().toString())) {
                        mainPrice = Util().getTwoDecimalPlaces(searchCurrencyListData[i].getPrice())
                    }
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_dollar)
                    searchCurrencyListData.removeAt(i)
                    break
                } else if (searchCurrencyListData[i].getCode() == CURRENCY_TYPE_KRW && SharedPreferencesUtil().getSelectLanguage(mContext!!) == SELECT_LANGUAGE_KO) {
                    if (!TextUtils.isEmpty(searchCurrencyListData[i].getPrice().toString())) {
                        mainPrice = Util().getTwoDecimalPlaces(searchCurrencyListData[i].getPrice())
                    }
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_won)
                    searchCurrencyListData.removeAt(i)
                    break
                } else if (searchCurrencyListData[i].getCode() == CURRENCY_TYPE_PHP && SharedPreferencesUtil().getSelectLanguage(mContext!!) == SELECT_LANGUAGE_FIL) {
                    if (!TextUtils.isEmpty(searchCurrencyListData[i].getPrice().toString())) {
                        mainPrice = Util().getTwoDecimalPlaces(searchCurrencyListData[i].getPrice())
                    }
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_peso)
                    searchCurrencyListData.removeAt(i)
                    break
                } else if (searchCurrencyListData[i].getCode() == CURRENCY_TYPE_CNY && SharedPreferencesUtil().getSelectLanguage(mContext!!) == SELECT_LANGUAGE_ZH) {
                    if (!TextUtils.isEmpty(searchCurrencyListData[i].getPrice().toString())) {
                        mainPrice = Util().getTwoDecimalPlaces(searchCurrencyListData[i].getPrice())
                    }
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_yuan)
                    searchCurrencyListData.removeAt(i)
                    break
                } else if (searchCurrencyListData[i].getCode() == CURRENCY_TYPE_JPY && SharedPreferencesUtil().getSelectLanguage(mContext!!) == SELECT_LANGUAGE_JA) {
                    if (!TextUtils.isEmpty(searchCurrencyListData[i].getPrice().toString())) {
                        mainPrice = Util().getTwoDecimalPlaces(searchCurrencyListData[i].getPrice())
                    }
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_yen)
                    searchCurrencyListData.removeAt(i)
                    break
                }
            }

            if (TextUtils.isEmpty(mainPrice) || TextUtils.isEmpty(mainPriceCurrency)) {
                mainPrice = Util().getTwoDecimalPlaces(searchCurrencyListData[0].getPrice())
                if (searchCurrencyListData[0].getCode() == CURRENCY_TYPE_USD) {
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_dollar)
                } else if (searchCurrencyListData[0].getCode() == CURRENCY_TYPE_KRW) {
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_won)
                } else if (searchCurrencyListData[0].getCode() == CURRENCY_TYPE_PHP) {
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_peso)
                } else if (searchCurrencyListData[0].getCode() == CURRENCY_TYPE_CNY) {
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_yuan)
                } else if (searchCurrencyListData[0].getCode() == CURRENCY_TYPE_JPY) {
                    mainPriceCurrency = mContext!!.resources.getString(R.string.text_currency_yen)
                }
            }

            viewHolder.mMainPriceTextView.text = mainPrice
            viewHolder.mMainPriceCurrencyTextView.text = mainPriceCurrency



            mCurrencyAdapter.setData(searchCurrencyListData)
        }

        if (!TextUtils.isEmpty(data.getItemContent())) {
            viewHolder.mMessageTextView.text = data.getItemContent()
        }

    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mMenuImageView: SimpleDraweeView
        var mNameTextView: TextView
        var mMainPriceTextView: TextView
        var mMainPriceCurrencyTextView: TextView
        var mMessageTextView: TextView
        var mCurrencyRecyclerView: RecyclerView

        init {
            mConvertView = convertView
            mMenuImageView = convertView.list_item_store_menu_image_view
            mNameTextView = convertView.list_item_store_menu_name_text_view
            mMainPriceTextView = convertView.list_item_store_menu_main_price_text_view
            mMainPriceCurrencyTextView = convertView.list_item_store_menu_main_price_currency_text_view
            mMessageTextView = convertView.list_item_store_menu_message_text_view
            mCurrencyRecyclerView = convertView.list_item_store_menu_currency_recycler_view
        }
    }
}