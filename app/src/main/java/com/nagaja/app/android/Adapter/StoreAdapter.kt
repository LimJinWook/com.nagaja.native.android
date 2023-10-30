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
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_THUMB
import com.nagaja.app.android.Data.StoreData
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_store.view.*
import kotlinx.android.synthetic.main.list_item_store_category.view.*


class StoreAdapter(context: Context): RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    private var mStoreListData: ArrayList<StoreData> = ArrayList()
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
        fun onClick(data: StoreData, position: Int)
        fun onRecommend(position: Int, companyUid: Int, isLike: Boolean)
        fun onRegular(position: Int, companyUid: Int, isRegular: Boolean)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<StoreData>) {
        if (null != mStoreListData) {
            mStoreListData.clear()
        }
        
        val itemList: ArrayList<StoreData> = ArrayList<StoreData>()
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
        mStoreListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mStoreListData) {
            val data: StoreData = mStoreListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mStoreListData) {
            mStoreListData.size
        } else 0
    }

    fun getItem(position: Int): StoreData? {
        if (null != mStoreListData) {
            if (0 <= position && position < mStoreListData.size) {
                return mStoreListData[position]
            }
        }
        return null
    }

    fun setSelectData(position: Int, isRecommend: Boolean) {

        if (isRecommend) {
            mStoreListData[position].setIsRecommendUseYn(!mStoreListData[position].getIsRecommendUseYn())
        } else {
            mStoreListData[position].setIsRegularUseYn(!mStoreListData[position].getIsRegularUseYn())
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_store,
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
        val data: StoreData = getItem(position) ?: return

        viewHolder.mLineView.visibility = if (position == 0) View.GONE else View.VISIBLE

        if (!TextUtils.isEmpty(data.getCompanyMainImage())) {
            viewHolder.mStoreImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getCompanyMainImage()))
        }

        viewHolder.mCertificationIconImageView.visibility = if (data.getIsCompanyCertificationYn()) View.VISIBLE else View.GONE

        if (!TextUtils.isEmpty(data.getCompanyName())) {
            viewHolder.mTitleTextView.text = data.getCompanyName()
        } else {
            if (!TextUtils.isEmpty(data.getCompanyNameEnglish())) {
                viewHolder.mTitleTextView.text = data.getCompanyNameEnglish()
            }
        }

        if (data.getCompanyReviewPointAverage() > 0) {
            viewHolder.mReviewPointTextView.text = String.format("%.1f", data.getCompanyReviewPointAverage())
        }

        viewHolder.mReviewCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_store_list_review_count), data.getCompanyRecommendCount())

        if (!TextUtils.isEmpty(data.getServiceBeginTime()) && !TextUtils.isEmpty(data.getServiceEndTime())) {
            var beginTime = data.getServiceBeginTime()
            var endTime = data.getServiceEndTime()

            if (beginTime.length > 5) {
                beginTime = beginTime.substring(0, 5)
            }

            if (endTime.length > 5) {
                endTime = endTime.substring(0, 5)
            }

            viewHolder.mBusinessHoursTextView.text = "$beginTime ~ $endTime"
        }

        viewHolder.mDeliveryTextView.visibility = if (data.getIsDeliveryUseYn()) View.VISIBLE else View.GONE
        viewHolder.mReservationTextView.visibility = if (data.getIsReservationUseYn()) View.VISIBLE else View.GONE
        viewHolder.mPickUpTextView.visibility = if (data.getIsPickUpUseYn()) View.VISIBLE else View.GONE
        viewHolder.mParkingTextView.visibility = if (data.getIsParkingUseYn()) View.VISIBLE else View.GONE
        viewHolder.mPetTextView.visibility = if (data.getIsPetUseYn()) View.VISIBLE else View.GONE



        viewHolder.mRecommendIconImageView.setImageResource(if (data.getIsRecommendUseYn()) R.drawable.icon_recommend_enable else R.drawable.icon_recommend_2)
        viewHolder.mRecommendView.setOnClickListener {
            NagajaLog().e("wooks, data.getCompanyUid() = ${data.getCompanyUid()}")
            mOnItemClickListener!!.onRecommend(position, data.getCompanyUid(), !data.getIsRecommendUseYn())
        }

        viewHolder.mRegularIconImageView.setImageResource(if (data.getIsRegularUseYn()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)
        viewHolder.mRegularView.setOnClickListener {
            mOnItemClickListener!!.onRegular(position, data.getCompanyUid(), !data.getIsRegularUseYn())
        }


        viewHolder.mConvertView.setOnSingleClickListener {
            mOnItemClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mLineView: View
        var mCertificationIconImageView: ImageView
        var mStoreImageView: SimpleDraweeView
        var mTitleTextView: TextView
        var mReviewPointTextView: TextView
        var mReviewCountTextView: TextView
        var mBusinessHoursTextView: TextView
        var mDeliveryTextView: TextView
        var mReservationTextView: TextView
        var mPickUpTextView: TextView
        var mParkingTextView: TextView
        var mPetTextView: TextView
        var mChattingView: View
        var mReservationView: View
        var mRecommendView: View
        var mRecommendIconImageView: ImageView
        var mRegularView: View
        var mRegularIconImageView: ImageView

        init {
            mConvertView = convertView
            mLineView = convertView.list_item_store_line_view
            mCertificationIconImageView = convertView.list_item_store_certification_icon_image_view
            mStoreImageView = convertView.list_item_store_image_view
            mTitleTextView = convertView.list_item_store_title_text_view
            mReviewPointTextView = convertView.list_item_store_review_point_text_view
            mReviewCountTextView = convertView.list_item_store_review_count_text_view
            mBusinessHoursTextView = convertView.list_item_store_business_hours_text_view
            mDeliveryTextView = convertView.list_item_store_delivery_text_view
            mReservationTextView = convertView.list_item_store_reservation_text_view
            mPickUpTextView = convertView.list_item_store_pick_up_text_view
            mParkingTextView = convertView.list_item_store_parking_text_view
            mPetTextView = convertView.list_item_store_pet_text_view
            mChattingView = convertView.list_item_store_chatting_view
            mReservationView = convertView.list_item_store_reservation_view
            mRecommendView = convertView.list_item_store_recommend_view
            mRecommendIconImageView = convertView.list_item_store_recommend_icon_image_view
            mRegularView = convertView.list_item_store_regular_view
            mRegularIconImageView = convertView.list_item_store_regular_icon_image_view
        }
    }
}