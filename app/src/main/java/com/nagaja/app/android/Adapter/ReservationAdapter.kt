package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Base.NagajaFragment.Companion.TYPE_RESERVATION_APPLICATION
import com.nagaja.app.android.Base.NagajaFragment.Companion.TYPE_RESERVATION_CANCEL
import com.nagaja.app.android.Base.NagajaFragment.Companion.TYPE_RESERVATION_COMPLETE
import com.nagaja.app.android.Base.NagajaFragment.Companion.TYPE_RESERVATION_CONFIRMATION
import com.nagaja.app.android.Base.NagajaFragment.Companion.TYPE_RESERVATION_USE_COMPLETE
import com.nagaja.app.android.Data.ReservationData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_reservation.view.*
import kotlinx.android.synthetic.main.list_item_reservation_category.view.*


class ReservationAdapter(context: Context): RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {
    private var mReservationListData: ArrayList<ReservationData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnReservationClickListener: OnReservationClickListener? = null

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnReservationClickListener {
        fun onClick(data: ReservationData, isDelete: Boolean)
    }

    fun setOnReservationClickListener(listener: OnReservationClickListener?) {
        mOnReservationClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<ReservationData>) {
        if (null != mReservationListData) {
            mReservationListData.clear()
        }
        
        val itemList: ArrayList<ReservationData> = ArrayList<ReservationData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mReservationListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mReservationListData) {
            val data: ReservationData = mReservationListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mReservationListData) {
            mReservationListData.size
        } else 0
    }

    fun getItem(position: Int): ReservationData? {
        if (null != mReservationListData) {
            if (0 <= position && position < mReservationListData.size) {
                return mReservationListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mReservationListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_reservation,
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
        val data: ReservationData = getItem(position) ?: return

        // Empty View
        viewHolder.mEmptyView.visibility = if (position == 0) View.VISIBLE else View.GONE

        // Company Name Text View
        if (!TextUtils.isEmpty(data.getCompanyNameEnglish())) {
            viewHolder.mCompanyNameTextView.text = data.getCompanyNameEnglish()
        } else {
            viewHolder.mCompanyNameTextView.text = data.getCompanyName()
        }

        // Status
        when (data.getReservationStatus()) {
            TYPE_RESERVATION_APPLICATION,
            TYPE_RESERVATION_CONFIRMATION -> {

                var status = ""
                if (data.getReservationStatus() == TYPE_RESERVATION_APPLICATION) {
                    status = mContext!!.resources.getString(R.string.text_application_reservation)
                    viewHolder.mCheckImageView.visibility = View.GONE
                    viewHolder.mCancelImageView.visibility = View.VISIBLE
                } else {
                    status = mContext!!.resources.getString(R.string.text_reservation_comfirmation)
                    viewHolder.mCheckImageView.visibility = View.VISIBLE
                    viewHolder.mCheckImageView.setImageResource(R.drawable.icon_reservation_check)
                    viewHolder.mCancelImageView.visibility = View.GONE
                }

                viewHolder.mBackGroundView.setBackgroundResource(R.drawable.bg_boarder_ffffff)
                viewHolder.mStatusTextView.text = status
                viewHolder.mStatusTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_0d4d97))
                viewHolder.mBottomView.visibility = View.VISIBLE
                viewHolder.mConvertView.isEnabled = true
                viewHolder.mReservationView.visibility = View.VISIBLE

                if (!TextUtils.isEmpty(data.getReservationName())) {
                    viewHolder.mReservationNameTextView.text = data.getReservationName()
                }

                if (!TextUtils.isEmpty(data.getReservationBeginTime()) && !TextUtils.isEmpty(data.getReservationEndTime())) {
                    viewHolder.mReservationDateTextView.text = data.getReservationBeginTime() + " ~ " + data.getReservationEndTime()
                }
            }

            TYPE_RESERVATION_COMPLETE -> {
                val status = mContext!!.resources.getString(R.string.text_reservation_complete_2)
                viewHolder.mCheckImageView.visibility = View.GONE

                viewHolder.mBackGroundView.setBackgroundResource(R.drawable.bg_boarder_ffffff)
                viewHolder.mStatusTextView.text = status
                viewHolder.mStatusTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_0d4d97))
                viewHolder.mBottomView.visibility = View.VISIBLE
                viewHolder.mConvertView.isEnabled = true
                viewHolder.mCancelImageView.visibility = View.GONE
                viewHolder.mReservationView.visibility = View.VISIBLE

                if (!TextUtils.isEmpty(data.getReservationName())) {
                    viewHolder.mReservationNameTextView.text = data.getReservationName()
                }

                if (!TextUtils.isEmpty(data.getReservationBeginTime()) && !TextUtils.isEmpty(data.getReservationEndTime())) {
                    viewHolder.mReservationDateTextView.text = data.getReservationBeginTime() + " ~ " + data.getReservationEndTime()
                }
            }

            TYPE_RESERVATION_USE_COMPLETE -> {
                val status = mContext!!.resources.getString(R.string.text_reservation_used)
                viewHolder.mCheckImageView.visibility = View.VISIBLE
                viewHolder.mCheckImageView.setImageResource(R.drawable.icon_reservation_check)

                viewHolder.mBackGroundView.setBackgroundResource(R.drawable.bg_boarder_e1e1e1)
                viewHolder.mStatusTextView.text = status
                viewHolder.mStatusTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_0d4d97))
                viewHolder.mBottomView.visibility = View.VISIBLE
                viewHolder.mConvertView.isEnabled = true
                viewHolder.mCancelImageView.visibility = View.GONE
                viewHolder.mReservationView.visibility = View.GONE

                if (!TextUtils.isEmpty(data.getReservationName())) {
                    viewHolder.mReservationNameTextView.text = data.getReservationName()
                }

                if (!TextUtils.isEmpty(data.getReservationBeginTime()) && !TextUtils.isEmpty(data.getReservationEndTime())) {
                    viewHolder.mReservationDateTextView.text = data.getReservationBeginTime() + " ~ " + data.getReservationEndTime()
                }
            }

            TYPE_RESERVATION_CANCEL -> {

                val status = mContext!!.resources.getString(R.string.text_reservation_cancellation)

                viewHolder.mBackGroundView.setBackgroundResource(R.drawable.bg_boarder_e1e1e1)
                viewHolder.mCheckImageView.visibility = View.VISIBLE
                viewHolder.mCheckImageView.setImageResource(R.drawable.icon_reservation_cancel)
                viewHolder.mStatusTextView.text = status
                viewHolder.mStatusTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_ff4755))
                viewHolder.mBottomView.visibility = View.VISIBLE
                viewHolder.mConvertView.isEnabled = true
                viewHolder.mCancelImageView.visibility = View.GONE
                viewHolder.mReservationView.visibility = View.VISIBLE

                if (!TextUtils.isEmpty(data.getReservationName())) {
                    viewHolder.mReservationNameTextView.text = data.getReservationName()
                }

                if (!TextUtils.isEmpty(data.getReservationBeginTime()) && !TextUtils.isEmpty(data.getReservationEndTime())) {
                    viewHolder.mReservationDateTextView.text = data.getReservationBeginTime() + " ~ " + data.getReservationEndTime()
                }
            }
        }

        viewHolder.mCancelImageView.setOnClickListener {
            mOnReservationClickListener!!.onClick(data, true)
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnReservationClickListener!!.onClick(data, false)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mEmptyView: View
        var mContainerView: View
        var mReservationView: View
        var mBackGroundView: View
        var mCompanyNameTextView: TextView
        var mCheckImageView: ImageView
        var mStatusTextView: TextView
        var mBottomView: View
        var mReservationNameTextView: TextView
        var mReservationDateTextView: TextView
        var mCancelImageView: ImageView

        init {
            mConvertView = convertView
            mEmptyView = convertView.list_item_reservation_empty_view
            mContainerView = convertView.list_item_reservation_container_view
            mReservationView = convertView.list_item_reservation_view
            mBackGroundView = convertView.list_item_reservation_background_view
            mCompanyNameTextView = convertView.list_item_reservation_company_name_text_view
            mCheckImageView = convertView.list_item_reservation_check_image_view
            mStatusTextView = convertView.list_item_reservation_status_text_view
            mBottomView = convertView.list_item_reservation_bottom_view
            mReservationNameTextView = convertView.list_item_reservation_name_text_view
            mReservationDateTextView = convertView.list_item_reservation_date_text_view
            mCancelImageView = convertView.list_item_reservation_cancel_image_view
        }
    }
}