package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_THUMB
import com.nagaja.app.android.Data.StoreDetailReviewData
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_store_review.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class StoreReviewAdapter(context: Context): RecyclerView.Adapter<StoreReviewAdapter.ViewHolder>() {
    private var mStoreDetailReviewListData: ArrayList<StoreDetailReviewData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    private lateinit var mStoreReviewUploadImageAdapter: StoreReviewUploadImageAdapter

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    interface OnItemClickListener {
        fun onImageClick(imageList: ArrayList<String>, position: Int)
        fun onClick(data: StoreDetailReviewData)
        fun onModify(data: StoreDetailReviewData, position: Int)
        fun onDelete(data: StoreDetailReviewData, position: Int)
        fun onReport(reportUid: Int)
        fun onRecommend(reviewUid: Int, isLike: Boolean, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<StoreDetailReviewData>) {
        if (null != mStoreDetailReviewListData) {
            mStoreDetailReviewListData.clear()
        }
        
        val itemList: ArrayList<StoreDetailReviewData> = ArrayList<StoreDetailReviewData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mStoreDetailReviewListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mStoreDetailReviewListData) {
            val data: StoreDetailReviewData = mStoreDetailReviewListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mStoreDetailReviewListData) {
            mStoreDetailReviewListData.size
        } else 0
    }

    fun getItem(position: Int): StoreDetailReviewData? {
        if (null != mStoreDetailReviewListData) {
            if (0 <= position && position < mStoreDetailReviewListData.size) {
                return mStoreDetailReviewListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mStoreDetailReviewListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun setRecommend(isRecommend: Boolean, position: Int) {
        mStoreDetailReviewListData[position].setIsRecommendYn(isRecommend)
        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_store_review,
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
        val data: StoreDetailReviewData = getItem(position) ?: return

        mStoreReviewUploadImageAdapter = StoreReviewUploadImageAdapter(mContext!!)

        if (!TextUtils.isEmpty(data.getProfileImageUrl())) {
            viewHolder.mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getProfileImageUrl()))
            viewHolder.mProfileImageView.setOnSingleClickListener {
                val imageList = ArrayList<String>()
                imageList.add(DEFAULT_IMAGE_DOMAIN + data.getProfileImageUrl())
                mOnItemClickListener!!.onImageClick(imageList, 0)
            }
        }

        if (!TextUtils.isEmpty(data.getMemberName())) {
            viewHolder.mNameTextView.text = data.getMemberName()
        }

        if (!TextUtils.isEmpty(data.getMemberEmail())) {
            viewHolder.mIDTextView.text = "(" + Util().getEmailMasking(data.getMemberEmail()) + ")"
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

        viewHolder.mRingStarView.rating = data.getReviewViewPoint().toFloat()
        viewHolder.mRingStarView.setIsIndicator(true)

        if (!TextUtils.isEmpty(data.getReviewSubject())) {
            viewHolder.mTitleTextView.text = data.getReviewSubject()
        }

        if (!TextUtils.isEmpty(data.getReviewContent())) {
            viewHolder.mCommentTextView.text = data.getReviewContent()
        }

        viewHolder.mUploadImageRecyclerView.setHasFixedSize(true)
        viewHolder.mUploadImageRecyclerView.layoutManager = LinearLayoutManager(
            mContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mStoreReviewUploadImageAdapter.setOnImageClickListener(object : StoreReviewUploadImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                val imageList = ArrayList<String>()
                for (i in data.getReviewImageListData().indices) {
                    imageList.add(DEFAULT_IMAGE_DOMAIN + data.getReviewImageListData()[i].getReviewImageName())
                }
                mOnItemClickListener!!.onImageClick(imageList, position)
            }
        })
        viewHolder.mUploadImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        viewHolder.mUploadImageRecyclerView.adapter = mStoreReviewUploadImageAdapter

        if (data.getReviewImageListData().size > 0) {
            val imageList = ArrayList<String>()
            for (i in data.getReviewImageListData().indices) {
                imageList.add(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getReviewImageListData()[i].getReviewImageName())
            }
            mStoreReviewUploadImageAdapter.setData(imageList)
        } else {
            val imageList = ArrayList<String>()
            imageList.clear()
            mStoreReviewUploadImageAdapter.setData(imageList)
        }

        if (MAPP.USER_DATA.getMemberUid() == data.getMemberUid()) {
            viewHolder.mNotMyWriteView.visibility = View.GONE
            viewHolder.mMyWriteView.visibility = View.VISIBLE

            viewHolder.mModifyTextView.setOnClickListener {
                mOnItemClickListener!!.onModify(data, position)
            }

            viewHolder.mDeleteTextView.setOnClickListener {
                mOnItemClickListener!!.onDelete(data, position)
            }
        } else {
            viewHolder.mNotMyWriteView.visibility = View.VISIBLE
            viewHolder.mMyWriteView.visibility = View.GONE

            // Recommend View
            viewHolder.mRecommendView.setOnClickListener {
                mOnItemClickListener!!.onRecommend(data.getReviewUid(), !data.getIsRecommendYn(), position)
            }

            if (data.getIsRecommendYn()) {
                viewHolder.mRecommendIconImageView.setImageResource(R.drawable.icon_recommend_enable)
                viewHolder.mRecommendTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.bg_color_0d4d97))
            } else {
                viewHolder.mRecommendIconImageView.setImageResource(R.drawable.icon_recommend_2)
                viewHolder.mRecommendTextView.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_color_767676))
            }

            // Report View
            viewHolder.mReportView.setOnClickListener {
                mOnItemClickListener!!.onReport(data.getReviewUid())
            }

            // Report Icon Image View
            if (data.getIsReportYn()) {
                viewHolder.mReportIconImageView.setImageResource(R.drawable.icon_report_enable)
            } else {
                viewHolder.mReportIconImageView.setImageResource(R.drawable.icon_note_report)
            }

        }

    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mProfileImageView: SimpleDraweeView
        var mNameTextView: TextView
        var mIDTextView: TextView
        var mRingStarView: RatingBar
        var mDateTextView: TextView
        var mTitleTextView: TextView
        var mCommentTextView: TextView
        var mUploadImageRecyclerView: RecyclerView
        var mNotMyWriteView: View
        var mMyWriteView: View
        var mRecommendView: View
        var mRecommendTextView: TextView
        var mRecommendIconImageView: ImageView
        var mReportView: View
        var mReportIconImageView: ImageView
        var mModifyTextView: TextView
        var mDeleteTextView: TextView


        init {
            mConvertView = convertView
            mProfileImageView = convertView.fragment_list_item_store_review_profile_image_view
            mNameTextView = convertView.fragment_list_item_store_review_name_text_view
            mIDTextView = convertView.fragment_list_item_store_review_id_text_view
            mRingStarView = convertView.fragment_list_item_store_review_ring_star_view
            mDateTextView = convertView.fragment_list_item_store_review_date_text_view
            mTitleTextView = convertView.fragment_list_item_store_review_title_text_view
            mCommentTextView = convertView.fragment_list_item_store_review_comment_text_view
            mUploadImageRecyclerView = convertView.fragment_list_item_store_review_recycler_view
            mNotMyWriteView = convertView.fragment_list_item_store_review_not_my_write_view
            mMyWriteView = convertView.fragment_list_item_store_review_my_write_view
            mRecommendView = convertView.fragment_list_item_store_review_recommend_view
            mRecommendTextView = convertView.fragment_list_item_store_review_recommend_text_view
            mRecommendIconImageView = convertView.fragment_list_item_store_review_recommend_icon_image_view
            mReportView = convertView.fragment_list_item_store_review_report_view
            mReportIconImageView = convertView.fragment_list_item_store_review_report_icon_image_view
            mModifyTextView = convertView.fragment_list_item_store_review_modify_text_view
            mDeleteTextView = convertView.fragment_list_item_store_review_delete_text_view
        }
    }
}