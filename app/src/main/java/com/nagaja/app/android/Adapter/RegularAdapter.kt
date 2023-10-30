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
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Data.RegularData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_company_regular_swipe.view.*
import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_regular.view.*
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_address_text_view
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_chat_view
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_date_text_view
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_image_view
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_memo_text_view
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_name_text_view
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_phone_call_view
import kotlinx.android.synthetic.main.list_item_regular.view.list_item_regular_phone_number_text_view
import kotlinx.android.synthetic.main.list_item_regular_swipe.view.*
import kotlinx.android.synthetic.main.list_item_regular_swipe.view.list_item_regular_swipe_layout
import java.io.IOException
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class RegularAdapter(context: Context, isCompanyRegular: Boolean): RecyclerView.Adapter<RegularAdapter.ViewHolder>() {
    private var mRegularListData: ArrayList<RegularData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnRegularClickListener: OnRegularClickListener? = null

    private val mViewBinderHelper = ViewBinderHelper()

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
        mViewBinderHelper.setOpenOnlyOne(true)
        mIsCompanyRegular = isCompanyRegular
    }

    companion object {
        var mIsCompanyRegular = false
    }

    interface OnRegularClickListener {
        fun onClick(data: RegularData, position: Int)
        fun onShowStore(companyUid: Int)
        fun onChat(data: RegularData)
        fun onPhoneCall(phoneNumber: String)
    }

    fun setOnRegularClickListener(listener: OnRegularClickListener?) {
        mOnRegularClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<RegularData>) {
        if (null != mRegularListData) {
            mRegularListData.clear()
        }
        
        val itemList: ArrayList<RegularData> = ArrayList<RegularData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mRegularListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mRegularListData) {
            val data: RegularData = mRegularListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mRegularListData) {
            mRegularListData.size
        } else 0
    }

    fun getItem(position: Int): RegularData? {
        if (null != mRegularListData) {
            if (0 <= position && position < mRegularListData.size) {
                return mRegularListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mRegularListData.removeAt(position)
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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView: View
        if (mIsCompanyRegular) {
            inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_company_regular_swipe, parent, false)
        } else {
            inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_regular_swipe, parent, false)
        }

        return ViewHolder(inflatedView)
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (null == viewHolder) {
            return
        }
        val data: RegularData = getItem(position) ?: return

        if (mIsCompanyRegular) {
            if (!TextUtils.isEmpty(data.getMemberImageName())) {
                viewHolder.mMemberImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getMemberImageName()))
            } else {
                viewHolder.mMemberImageView.setImageURI(Uri.parse(""))
            }

            if (!TextUtils.isEmpty(data.getMemberNickName())) {
                viewHolder.mMemberNickNameTextView.text = data.getMemberNickName()
            }

            if (!TextUtils.isEmpty(data.getMemberNationPhone()) && !TextUtils.isEmpty(data.getMemberPhoneNumber())) {
                viewHolder.mMemberPhoneNumberTextView.text = /*data.getMemberNationPhone() + " " + */data.getMemberPhoneNumber()
            }

//            viewHolder.mMemberDeleteTextView.setOnClickListener {
//                mOnRegularClickListener!!.onClick(data, position)
//            }

            viewHolder.mMemberChatView.setOnClickListener {
                mOnRegularClickListener!!.onChat(data)
            }

            viewHolder.mMemberPhoneCallView.setOnClickListener {
                mOnRegularClickListener!!.onPhoneCall( "+" + data.getMemberNationPhone() + data.getMemberPhoneNumber())
            }

            if (!TextUtils.isEmpty(data.getCreateDate())) {
                var time = data.getCreateDate()
                val index: Int = time.indexOf("+")
                time = time.substring(0, index)

                val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
                val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                viewHolder.mMemberDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
            }

        } else {

            mViewBinderHelper.bind(viewHolder.mSwipeLayout, data.getRegularUid().toString())

            if (!TextUtils.isEmpty(data.getCompanyImageName())) {
                viewHolder.mImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getCompanyImageName()))
            } else {
                viewHolder.mImageView.setImageURI(Uri.parse(""))
            }

            if (!TextUtils.isEmpty(data.getCompanyName())) {
                viewHolder.mNameTextView.text = data.getCompanyName()
            } else {
                viewHolder.mNameTextView.text = data.getCompanyNameEnglish()
            }

            if (!TextUtils.isEmpty(data.getCompanyAddress())) {
                viewHolder.mAddressTextView.text = data.getCompanyAddress() + " " + data.getCompanyAddressDetail()
            }

            if (!TextUtils.isEmpty(data.getCompanyNationPhone()) && !TextUtils.isEmpty(data.getCompanyPhoneNumber())) {
                viewHolder.mPhoneNumberTextView.text = "+" + data.getCompanyNationPhone() + data.getCompanyPhoneNumber()
            }

            if (!TextUtils.isEmpty(data.getRegularMemo())) {
                viewHolder.mMemoTextView.text = data.getRegularMemo()
            }

            viewHolder.mDeleteTextView.setOnClickListener {
                mOnRegularClickListener!!.onClick(data, position)
            }

            viewHolder.mRegularView.setOnClickListener {
                mOnRegularClickListener!!.onShowStore(data.getCompanyUid())
            }

            viewHolder.mChatView.setOnClickListener {
                mOnRegularClickListener!!.onChat(data)
            }

            viewHolder.mPhoneCallView.setOnClickListener {
                mOnRegularClickListener!!.onPhoneCall(data.getCompanyNationPhone() + data.getCompanyPhoneNumber())
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
        }







//        viewHolder.aaa.setSwipeListener(object : SwipeRevealLayout.SwipeListener {
//            override fun onClosed(view: SwipeRevealLayout?) {
//                NagajaLog().d("wooks, onClosed POSITION = $position")
//            }
//
//            override fun onOpened(view: SwipeRevealLayout?) {
//                NagajaLog().d("wooks, onOpened POSITION = $position")
//            }
//
//            override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
//            }
//
//        })
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        lateinit var mConvertView: View
        lateinit var mSwipeLayout: SwipeRevealLayout
        lateinit var mRegularView: View
        lateinit var mImageView: SimpleDraweeView

        lateinit var mNameTextView: TextView
//        var mShareImageView: ImageView
        lateinit var mAddressTextView: TextView
        lateinit var mPhoneNumberTextView: TextView
        lateinit var mChatView: View
        lateinit var mPhoneCallView: View
        lateinit var mMemoTextView: TextView
        lateinit var mDateTextView: TextView
        lateinit var mDeleteTextView: TextView



        lateinit var mMemberImageView: SimpleDraweeView
        lateinit var mMemberNickNameTextView: TextView
        lateinit var mMemberPhoneNumberTextView: TextView
        lateinit var mMemberChatView: View
        lateinit var mMemberPhoneCallView: View
        lateinit var mMemberDateTextView: TextView

        init {
            mConvertView = convertView

            if (mIsCompanyRegular) {
                mMemberImageView = convertView.list_item_company_regular_image_view
                mMemberNickNameTextView = convertView.list_item_company_regular_name_text_view
                mMemberPhoneNumberTextView = convertView.list_item_company_regular_phone_number_text_view
                mMemberChatView = convertView.list_item_company_regular_chat_view
                mMemberPhoneCallView = convertView.list_item_company_regular_phone_call_view
                mMemberDateTextView = convertView.list_item_company_regular_date_text_view
            } else {
                mSwipeLayout = convertView.list_item_regular_swipe_layout
                mRegularView = convertView.list_item_regular_view
                mImageView = convertView.list_item_regular_image_view
                mNameTextView = convertView.list_item_regular_name_text_view
//            mShareImageView = convertView.list_item_regular_share_image_view
                mAddressTextView = convertView.list_item_regular_address_text_view
                mPhoneNumberTextView = convertView.list_item_regular_phone_number_text_view
                mChatView = convertView.list_item_regular_chat_view
                mPhoneCallView = convertView.list_item_regular_phone_call_view
                mMemoTextView = convertView.list_item_regular_memo_text_view
                mDateTextView = convertView.list_item_regular_date_text_view
                mDeleteTextView = convertView.list_item_regular_delete_text_view
            }

        }
    }
}