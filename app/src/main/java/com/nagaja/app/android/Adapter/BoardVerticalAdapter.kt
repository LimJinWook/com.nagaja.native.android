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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Utils.NetworkProvider.Companion.IMAGE_THUMB
import com.nagaja.app.android.Data.BoardData
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener

import kotlinx.android.synthetic.main.list_item_board_vertical.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class BoardVerticalAdapter(context: Context): RecyclerView.Adapter<BoardVerticalAdapter.ViewHolder>() {
    private var mBoardListData: ArrayList<BoardData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnBoardItemClickListener: OnBoardItemClickListener? = null

    private lateinit var mStoreReviewUploadImageAdapter: StoreReviewUploadImageAdapter

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    companion object {
        const val SELECT_TYPE_GOOD                            = 0x01
        const val SELECT_TYPE_COMMENT                         = 0x02
        const val SELECT_TYPE_BOOKMARK                        = 0x03
    }

    interface OnBoardItemClickListener {
        fun onImageClick(imageList: ArrayList<String>, position: Int)
        fun onClick(data: BoardData, position: Int)
        fun onSelectItem(data: BoardData, position: Int, selectType: Int)
    }

    fun setOnBoardItemClickListener(listener: OnBoardItemClickListener?) {
        mOnBoardItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<BoardData>) {
        if (null != mBoardListData) {
            mBoardListData.clear()
        }
        
        val itemList: ArrayList<BoardData> = ArrayList<BoardData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mBoardListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mBoardListData) {
            val data: BoardData = mBoardListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mBoardListData) {
            mBoardListData.size
        } else 0
    }

    fun getItem(position: Int): BoardData? {
        if (null != mBoardListData) {
            if (0 <= position && position < mBoardListData.size) {
                return mBoardListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
        mBoardListData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun setItemSelect(position: Int, selectType: Int) {
        if (selectType == SELECT_TYPE_GOOD) {
            mBoardListData[position].setIsRecommend(mBoardListData[position].getIsRecommend())
            NagajaLog().e("wooks, data.getRecommendCount().toString() 111 = ${mBoardListData[position].getRecommendCount()}")
            if (mBoardListData[position].getIsRecommend()) {
                mBoardListData[position].setRecommendCount(mBoardListData[position].getRecommendCount() + 1)
            } else {
                if (mBoardListData[position].getRecommendCount() > 0) {
                    mBoardListData[position].setRecommendCount(mBoardListData[position].getRecommendCount() - 1)
                }
            }
            NagajaLog().e("wooks, data.getRecommendCount().toString() 222 = ${mBoardListData[position].getRecommendCount()}")
        } else {
            mBoardListData[position].setIsBookMark(mBoardListData[position].getIsBookMark())
            if (mBoardListData[position].getIsBookMark()) {
                mBoardListData[position].setBookMarkCount(mBoardListData[position].getBookMarkCount() + 1)
            } else {
                if (mBoardListData[position].getBookMarkCount() > 0) {
                    mBoardListData[position].setBookMarkCount(mBoardListData[position].getBookMarkCount() - 1)
                }
            }
        }

//        notifyDataSetChanged()
        notifyItemChanged(position)
    }

    fun selectLikeBookmark(isClick: Boolean, position: Int, isLike: Boolean) {
        if (isLike) {
            mBoardListData[position].setIsRecommend(isClick)
        } else {
            mBoardListData[position].setIsBookMark(isClick)
        }

        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_board_vertical,
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
        val data: BoardData = getItem(position) ?: return

        mStoreReviewUploadImageAdapter = StoreReviewUploadImageAdapter(mContext!!)

//        viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

        if (!TextUtils.isEmpty(data.getMemberImageName())) {
            viewHolder.mProfileImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getMemberImageName()))
        }

        if (!TextUtils.isEmpty(data.getMemberName())) {
            viewHolder.mNameTextView.text = data.getMemberName()
        }

        if (!TextUtils.isEmpty(data.getCategoryName())) {
            viewHolder.mCategoryTextView.text = data.getCategoryName()
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

        if (!TextUtils.isEmpty(data.getBoardSubject())) {
            viewHolder.mTitleTextView.text = data.getBoardSubject()
        }

        if (!TextUtils.isEmpty(data.getBoardContent())) {
            viewHolder.mContentTextView.text = data.getBoardContent()
        }

        if (data.getBoardImageListData().size > 0) {
            viewHolder.mBoardRecyclerView.visibility = View.VISIBLE
            viewHolder.mBoardRecyclerView.setHasFixedSize(true)
            viewHolder.mBoardRecyclerView.layoutManager = LinearLayoutManager(
                mContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            val imageList = ArrayList<String>()
            for (i in data.getBoardImageListData().indices) {
                imageList.add(DEFAULT_IMAGE_DOMAIN + IMAGE_THUMB + data.getBoardImageListData()[i].getItemImageName())
            }

            mStoreReviewUploadImageAdapter.setOnImageClickListener(object : StoreReviewUploadImageAdapter.OnImageClickListener {
                override fun onClick(position: Int) {
                    val imageList = ArrayList<String>()
                    for (i in data.getBoardImageListData().indices) {
                        imageList.add(DEFAULT_IMAGE_DOMAIN + data.getBoardImageListData()[i].getItemImageName())
                    }
                    mOnBoardItemClickListener!!.onImageClick(imageList, position)
                }
            })
            viewHolder.mBoardRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
            viewHolder.mBoardRecyclerView.adapter = mStoreReviewUploadImageAdapter

            mStoreReviewUploadImageAdapter.setData(imageList)
        } else {
            viewHolder.mBoardRecyclerView.visibility = View.GONE
        }

        if (data.getRecommendCount() > 0) {
            viewHolder.mGoodCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_good_count), data.getRecommendCount().toString())
        } else {
            viewHolder.mGoodCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_good_count), "0")
        }

        if (data.getCommentCount() > 0) {
            viewHolder.mCommentCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_comment_count), data.getCommentCount().toString())
        } else {
            viewHolder.mCommentCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_comment_count), "0")
        }

        if (data.getBookMarkCount() > 0) {
            viewHolder.mBookmarkCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_bookmark_count), data.getBookMarkCount().toString())
        } else {
            viewHolder.mBookmarkCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_bookmark_count), "0")
        }

        viewHolder.mGoodImageView.setImageResource(if (data.getIsRecommend()) R.drawable.icon_store_regula else R.drawable.icon_store_regula_2)

        viewHolder.mBookmarkImageView.setImageResource(if (data.getIsBookMark()) R.drawable.icon_bookmark_star_disable else R.drawable.icon_bookmark_star)

        viewHolder.mGoodView.setOnClickListener {
            mOnBoardItemClickListener!!.onSelectItem(data, position, SELECT_TYPE_GOOD)
        }

        viewHolder.mBookmarkView.setOnClickListener {
            mOnBoardItemClickListener!!.onSelectItem(data, position, SELECT_TYPE_BOOKMARK)
        }


        viewHolder.mConvertView.setOnSingleClickListener {
            mOnBoardItemClickListener!!.onClick(data, position)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
//        var mLineView: View
        var mProfileImageView: SimpleDraweeView
        var mNameTextView: TextView
        var mCategoryTextView: TextView
        var mDateTextView: TextView
        var mTitleTextView: TextView
        var mContentTextView: TextView
        var mBoardRecyclerView: RecyclerView
        var mGoodCountTextView: TextView
        var mCommentCountTextView: TextView
        var mBookmarkCountTextView: TextView
        var mGoodView: View
        var mGoodImageView: ImageView
        var mGoodTextView: TextView
        var mCommentView: View
        var mBookmarkView: View
        var mBookmarkImageView: ImageView

        init {
            mConvertView = convertView
//            mLineView = convertView.list_item_board_vertical_profile_image_view
            mProfileImageView = convertView.list_item_board_vertical_profile_image_view
            mNameTextView = convertView.list_item_board_vertical_name_text_view
            mCategoryTextView = convertView.list_item_board_vertical_category_text_view
            mDateTextView = convertView.list_item_board_vertical_date_text_view
            mTitleTextView = convertView.list_item_board_vertical_title_text_view
            mContentTextView = convertView.list_item_board_vertical_content_text_view
            mBoardRecyclerView = convertView.list_item_board_vertical_recycler_view
            mGoodCountTextView = convertView.list_item_board_vertical_good_count_text_view
            mCommentCountTextView = convertView.list_item_board_vertical_comment_count_text_view
            mBookmarkCountTextView = convertView.list_item_board_vertical_bookmark_count_text_view

            mGoodView = convertView.list_item_board_vertical_good_view
            mGoodImageView = convertView.list_item_board_vertical_good_image_view

            mGoodTextView = convertView.list_item_board_vertical_good_text_view
            mCommentView = convertView.list_item_board_vertical_comment_view

            mBookmarkView = convertView.list_item_board_vertical_bookmark_view
            mBookmarkImageView = mConvertView.list_item_board_vertical_bookmark_image_view
        }
    }
}