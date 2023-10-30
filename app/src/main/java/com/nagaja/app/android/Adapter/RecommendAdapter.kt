package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Data.RecommendPlaceData
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_THUMB
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_VIEW

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*


class RecommendAdapter(context: Context): RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {
    private var mRecommendPlaceListData: ArrayList<RecommendPlaceData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnRecommendClickListener: OnRecommendClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnRecommendClickListener {
        fun onClick(companyUid: Int)
    }

    fun setOnRecommendClickListener(listener: OnRecommendClickListener?) {
        mOnRecommendClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<RecommendPlaceData>) {
        if (null != mRecommendPlaceListData) {
            mRecommendPlaceListData.clear()
        }
        
        val itemList: ArrayList<RecommendPlaceData> = ArrayList<RecommendPlaceData>()
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
        mRecommendPlaceListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mRecommendPlaceListData) {
            val data: RecommendPlaceData = mRecommendPlaceListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mRecommendPlaceListData) {
            mRecommendPlaceListData.size
        } else 0
    }

    fun getItem(position: Int): RecommendPlaceData? {
        if (null != mRecommendPlaceListData) {
            if (0 <= position && position < mRecommendPlaceListData.size) {
                return mRecommendPlaceListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_recommend,
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
        val data: RecommendPlaceData = getItem(position) ?: return

        // Empty View
        viewHolder.mEmptyView.visibility = if (position == 0) View.VISIBLE else View.GONE

        // Image View
        if (data.getRecommendPlaceImageList().size > 0) {
            if (!TextUtils.isEmpty(data.getRecommendPlaceImageList()[0].getCompanyImageName())) {
                viewHolder.mImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + IMAGE_VIEW + data.getRecommendPlaceImageList()[0].getCompanyImageName()))
            }
        }

        // Title Text View
        if (!TextUtils.isEmpty(data.getCompanyName())) {
            viewHolder.mTitleTextView.text = data.getCompanyName()
        } else {
            viewHolder.mTitleTextView.text = data.getCompanyNameEnglish()
        }

        // Sub Title Text View
        if (!TextUtils.isEmpty(data.getCompanyAddress())) {
            viewHolder.mSubTitleTextView.text = data.getCompanyAddress()
        }

        // Pick Up Text View
        viewHolder.mPickUpTextView.visibility = if (data.getIsPickUpAvailable()) View.VISIBLE else View.GONE

        // Delivery Text View
        viewHolder.mDeliveryTextView.visibility = if (data.getIsDeliveryAvailable()) View.VISIBLE else View.GONE

        // Reservation Text View
        viewHolder.mReservationTextView.visibility = if (data.getIsReservationAvailable()) View.VISIBLE else View.GONE

        // Parking Text View
        viewHolder.mParkingTextView.visibility = if (data.getIsParkingAvailable()) View.VISIBLE else View.GONE

        // Pet Text View
        viewHolder.mPetTextView.visibility = if (data.getIsPetAvailable()) View.VISIBLE else View.GONE

        // Event Message Text View
        if (!TextUtils.isEmpty(data.getMessage())) {
            viewHolder.mEventView.visibility = View.VISIBLE
            viewHolder.mEventMessageTextView.text = data.getMessage()
        } else {
            viewHolder.mEventView.visibility = View.GONE
        }

        viewHolder.mConvertView.setOnSingleClickListener {
            mOnRecommendClickListener!!.onClick(data.getCompanyUid())
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mEmptyView: View
        var mImageView: SimpleDraweeView
        var mTitleTextView: TextView
        var mSubTitleTextView: TextView
        var mPickUpTextView: TextView
        var mDeliveryTextView: TextView
        var mReservationTextView: TextView
        var mParkingTextView: TextView
        var mPetTextView: TextView
        var mEventView: View
        var mEventMessageTextView: TextView

        init {
            mConvertView = convertView
            mEmptyView = convertView.list_item_recommend_empty_view
            mImageView = convertView.list_item_recommend_image_view
            mTitleTextView = convertView.list_item_recommend_title_text_view
            mSubTitleTextView = convertView.list_item_recommend_sub_title_text_view
            mPickUpTextView = convertView.list_item_recommend_pick_up_text_view
            mDeliveryTextView = convertView.list_item_recommend_delivery_text_view
            mReservationTextView = convertView.list_item_recommend_reservation_text_view
            mParkingTextView = convertView.list_item_recommend_parking_text_view
            mPetTextView = convertView.list_item_recommend_pet_text_view
            mEventView = convertView.list_item_recommend_event_view
            mEventMessageTextView = convertView.list_item_recommend_event_text_view
        }
    }
}