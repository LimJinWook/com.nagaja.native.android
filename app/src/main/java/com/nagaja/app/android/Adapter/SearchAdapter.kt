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
import com.nagaja.app.android.Data.SearchData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_notification.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_notice.view.*
import kotlinx.android.synthetic.main.list_item_search.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*


class SearchAdapter(context: Context): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private var mSearchListData: ArrayList<SearchData> = ArrayList()
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

    companion object {
        var mIsNews = false
    }

    interface OnItemClickListener {
        fun onClick(data: SearchData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<SearchData>) {
        if (null != mSearchListData) {
            mSearchListData.clear()
        }
        
        val itemList: ArrayList<SearchData> = ArrayList<SearchData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mSearchListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mSearchListData) {
            val data: SearchData = mSearchListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mSearchListData) {
            mSearchListData.size
        } else 0
    }

    fun getItem(position: Int): SearchData? {
        if (null != mSearchListData) {
            if (0 <= position && position < mSearchListData.size) {
                return mSearchListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_search, parent, false)
        return ViewHolder(inflatedView)
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (null == viewHolder) {
            return
        }
        val data: SearchData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

        if (data.getRootCategoryUid() == 3) {

            /**
             * 가맹점
             * */

            if (!TextUtils.isEmpty(data.getImageName())) {
                viewHolder.mSearchImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getImageName()))
            } else {
                viewHolder.mSearchImageView.setImageURI(Uri.parse(""))
            }

            if (!TextUtils.isEmpty(data.getRootCategoryName())) {
                viewHolder.mCategoryTextView.text = data.getRootCategoryName()
            }

            if (!TextUtils.isEmpty(data.getContent())) {
                viewHolder.mContentTextView.text = data.getContent()
            }

            viewHolder.mReviewPointView.visibility = View.VISIBLE
            viewHolder.mTransactionStatusView.visibility = View.GONE

            if (!TextUtils.isEmpty(data.getReviewPointAverage().toString())) {
                viewHolder.mReviewPointTextView.text = data.getReviewPointAverage().toString()
            } else {
                viewHolder.mReviewPointTextView.text = "0.0"
            }

            if (data.getRecommendCount() > 0) {
                viewHolder.mGoodCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_good_count_2), data.getRecommendCount().toString())
            } else {
                viewHolder.mGoodCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_good_count_2), "0")
            }


            viewHolder.mBusinessTimeView.visibility = View.VISIBLE

            val beginTime = if (!TextUtils.isEmpty(data.getServiceBeginTime())) {
                data.getServiceBeginTime().substring(0, 5)
            } else {
                ""
            }

            val endTime = if (!TextUtils.isEmpty(data.getServiceEndTime())) {
                data.getServiceEndTime().substring(0, 5)
            } else {
                ""
            }

            if (!TextUtils.isEmpty(beginTime)) {
                viewHolder.mBusinessTimeTextView.text = beginTime
                if (!TextUtils.isEmpty(endTime)) {
                    viewHolder.mBusinessTimeTextView.text = viewHolder.mBusinessTimeTextView.text.toString() + " ~ " + endTime
                }
            }

            viewHolder.mCurrencyRecyclerView.visibility = View.GONE

        } else {

            /**
             * 중고마켓
             * */

            mCurrencyAdapter = CurrencyAdapter(mContext!!)

            if (!TextUtils.isEmpty(data.getImageName())) {
                viewHolder.mSearchImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getImageName()))
            } else {
                viewHolder.mSearchImageView.setImageURI(Uri.parse(""))
            }

            if (!TextUtils.isEmpty(data.getRootCategoryName())) {
                viewHolder.mCategoryTextView.text = data.getRootCategoryName()
            }

            if (!TextUtils.isEmpty(data.getContent())) {
                viewHolder.mContentTextView.text = data.getContent()
            }

            viewHolder.mReviewPointView.visibility = View.GONE
            viewHolder.mTransactionStatusView.visibility = View.VISIBLE

            viewHolder.mBusinessTimeView.visibility = View.GONE

            if (data.getItemStatus() == 7) {
                viewHolder.mTransactionStatusTextView.text = mContext!!.resources.getString(R.string.text_used_market_list_sale_on_sale)
            } else {
                viewHolder.mTransactionStatusTextView.text = mContext!!.resources.getString(R.string.text_preparing_for_sale)
            }


            viewHolder.mCurrencyRecyclerView.visibility = View.VISIBLE

            viewHolder.mCurrencyRecyclerView.setHasFixedSize(true)
            viewHolder.mCurrencyRecyclerView.layoutManager = LinearLayoutManager(mContext!!, LinearLayoutManager.HORIZONTAL, false)

            viewHolder.mCurrencyRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
            viewHolder.mCurrencyRecyclerView.adapter = mCurrencyAdapter

            if (data.getCurrencyListData().size > 0) {
                mCurrencyAdapter.setData(data.getCurrencyListData())
            }
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnItemClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {

        var mConvertView: View

        var mLineView: View
        var mSearchImageView: SimpleDraweeView
        var mCategoryTextView: TextView
        var mContentTextView: TextView
        var mReviewPointView: View
        var mReviewPointTextView: TextView
        var mGoodCountTextView: TextView
        var mBusinessTimeView: View
        var mBusinessTimeTextView: TextView
        var mTransactionStatusView: View
        var mTransactionStatusTextView: TextView
        var mCurrencyRecyclerView: RecyclerView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_search_line_view
            mSearchImageView = convertView.list_item_search_image_view
            mCategoryTextView = convertView.list_item_search_category_text_view
            mContentTextView = convertView.list_item_search_content_text_view
            mReviewPointView = convertView.list_item_search_review_point_view
            mReviewPointTextView = convertView.list_item_search_review_point_text_view
            mGoodCountTextView = convertView.list_item_search_good_count_text_view
            mBusinessTimeView = convertView.list_item_search_business_time_view
            mBusinessTimeTextView = convertView.list_item_search_business_time_text_view
            mTransactionStatusView = convertView.list_item_search_transaction_status_view
            mTransactionStatusTextView = convertView.list_item_search_transaction_status_text_view
            mCurrencyRecyclerView = convertView.list_item_search_used_market_currency_recycler_view
        }
    }
}