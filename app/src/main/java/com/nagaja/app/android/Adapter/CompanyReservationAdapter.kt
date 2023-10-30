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

import kotlinx.android.synthetic.main.list_item_company_reservation.view.*


class CompanyReservationAdapter(context: Context): RecyclerView.Adapter<CompanyReservationAdapter.ViewHolder>() {
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
        fun onClick(data: ReservationData)
        fun onChat(data: ReservationData)
        fun onPhoneCall(phoneNumber: String)
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
            R.layout.list_item_company_reservation,
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

        // Status
        when (data.getReservationStatus()) {
            TYPE_RESERVATION_APPLICATION,
            TYPE_RESERVATION_CONFIRMATION -> {

                var status = ""
                if (data.getReservationStatus() == TYPE_RESERVATION_APPLICATION) {
                    status = mContext!!.resources.getString(R.string.text_application_reservation)
                    viewHolder.mCheckImageView.visibility = View.GONE
                } else {
                    status = mContext!!.resources.getString(R.string.text_reservation_comfirmation)
                    viewHolder.mCheckImageView.visibility = View.VISIBLE
                    viewHolder.mCheckImageView.setImageResource(R.drawable.icon_reservation_check)
                }

                viewHolder.mBackGroundView.setBackgroundResource(R.drawable.bg_boarder_ffffff)
                viewHolder.mStatusTextView.text = status
                viewHolder.mStatusTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_0d4d97))
                viewHolder.mBottomView.visibility = View.VISIBLE
                viewHolder.mConvertView.isEnabled = true

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

                if (!TextUtils.isEmpty(data.getReservationName())) {
                    viewHolder.mReservationNameTextView.text = data.getReservationName()
                }

                if (!TextUtils.isEmpty(data.getReservationBeginTime()) && !TextUtils.isEmpty(data.getReservationEndTime())) {
                    viewHolder.mReservationDateTextView.text = data.getReservationBeginTime() + " ~ " + data.getReservationEndTime()
                }
            }
        }

        viewHolder.mConvertView.setOnClickListener {
            mOnReservationClickListener!!.onClick(data)
        }

        viewHolder.mChatView.setOnClickListener {
            mOnReservationClickListener!!.onChat(data)
        }

        viewHolder.mPhoneCallView.setOnClickListener {
            mOnReservationClickListener!!.onPhoneCall("+" + data.getReservationNationPhone() + data.getReservationPhoneNumber())
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mEmptyView: View
        var mContainerView: View
        var mBackGroundView: View
        var mCheckImageView: ImageView
        var mStatusTextView: TextView
        var mBottomView: View
        var mReservationNameTextView: TextView
        var mReservationDateTextView: TextView
        var mChatView: View
        var mPhoneCallView: View

        init {
            mConvertView = convertView
            mEmptyView = convertView.list_item_company_reservation_empty_view
            mContainerView = convertView.list_item_company_reservation_container_view
            mBackGroundView = convertView.list_item_company_reservation_background_view
            mCheckImageView = convertView.list_item_company_reservation_check_image_view
            mStatusTextView = convertView.list_item_company_reservation_status_text_view
            mBottomView = convertView.list_item_company_reservation_bottom_view
            mReservationNameTextView = convertView.list_item_company_reservation_name_text_view
            mReservationDateTextView = convertView.list_item_company_reservation_date_text_view
            mChatView = convertView.list_item_company_reservation_chat_view
            mPhoneCallView = convertView.list_item_company_reservation_phone_call_view
        }
    }
}