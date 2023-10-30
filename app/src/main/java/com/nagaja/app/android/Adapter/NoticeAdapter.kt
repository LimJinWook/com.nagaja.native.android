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
import com.nagaja.app.android.Data.NoticeData
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_notice.view.*
import kotlinx.android.synthetic.main.list_item_store_menu.view.*
import kotlinx.android.synthetic.main.list_item_store_recommend_menu.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class NoticeAdapter(context: Context, isNews: Boolean): RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {
    private var mNoticeListData: ArrayList<NoticeData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnNewsItemClickListener: OnNewsItemClickListener? = null


    init {
        mContext = context
        mIsNews = isNews
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
    }

    companion object {
        var mIsNews = false
    }

    interface OnNewsItemClickListener {
        fun onClick(itemUid: Int)
        fun onClick(data: NoticeData)
    }

    fun setOnNewsItemClickListener(listener: OnNewsItemClickListener?) {
        mOnNewsItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NoticeData>) {
        if (null != mNoticeListData) {
            mNoticeListData.clear()
        }
        
        val itemList: ArrayList<NoticeData> = ArrayList<NoticeData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mNoticeListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNoticeListData) {
            val data: NoticeData = mNoticeListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNoticeListData) {
            mNoticeListData.size
        } else 0
    }

    fun getItem(position: Int): NoticeData? {
        if (null != mNoticeListData) {
            if (0 <= position && position < mNoticeListData.size) {
                return mNoticeListData[position]
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView: View
        if (mIsNews) {
            inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
        } else {
            inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)
        }

        return ViewHolder(inflatedView)
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (null == viewHolder) {
            return
        }
        val data: NoticeData = getItem(position) ?: return

        if (mIsNews) {

            viewHolder.mLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

            if (data.getNoticeImageListData().size <= 0) {
                viewHolder.mNewImageView.visibility = View.GONE
            } else {
                viewHolder.mNewImageView.visibility = View.VISIBLE
                viewHolder.mNewImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + data.getNoticeImageListData()[0].getItemImageName()))
            }

            if (!TextUtils.isEmpty(data.getNoticeSubject())) {
                viewHolder.mTitleTextView.text = data.getNoticeSubject()
            }

            if (!TextUtils.isEmpty(data.getNoticeContent())) {
                viewHolder.mContentView.text = data.getNoticeContent()
            }

            viewHolder.mViewCountTextView.text = if (data.getViewCount() > 0) data.getViewCount().toString() else "0"

            viewHolder.mCommentCountTextView.text = if (data.getCommentCount() > 0) data.getCommentCount().toString() else "0"

            viewHolder.mConvertView.setOnSingleClickListener {
                mOnNewsItemClickListener!!.onClick(data.getNoticeUid())
            }
        } else {

            viewHolder.mNoticeLineView.visibility = if (position == 0) View.VISIBLE else View.GONE

            if (!TextUtils.isEmpty(data.getNoticeSubject())) {
                viewHolder.mNoticeTitleTextView.text = "[" + data.getCategoryName() + "]" + " " + data.getNoticeSubject()
            }

            if (!TextUtils.isEmpty(data.getNoticeContent())) {
                viewHolder.mNoticeContentTextView.text = data.getNoticeContent()
            }

            if (!TextUtils.isEmpty(data.getCreateDate())) {
                var time = data.getCreateDate()
                val index: Int = time.indexOf("+")
                time = time.substring(0, index)

                val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
                val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                viewHolder.mNoticeDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
            }

            if (data.getViewCount() > 0) {
                viewHolder.mNoticeViewCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_board_view_count), data.getViewCount().toString())
            } else {
                viewHolder.mNoticeViewCountTextView.text = String.format(mContext!!.resources.getString(R.string.text_board_view_count), "0")
            }

            viewHolder.mConvertView.setOnSingleClickListener {
                mOnNewsItemClickListener!!.onClick(data)
            }
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {

        var mConvertView: View

        // News
        lateinit var mLineView: View
        lateinit var mNewImageView: SimpleDraweeView
        lateinit var mTitleTextView: TextView
        lateinit var mContentView: TextView
        lateinit var mViewCountTextView: TextView
        lateinit var mCommentCountTextView: TextView

        // Notice
        lateinit var mNoticeLineView: View
        lateinit var mNoticeTitleTextView: TextView
        lateinit var mNoticeContentTextView: TextView
        lateinit var mNoticeDateTextView: TextView
        lateinit var mNoticeViewCountTextView: TextView

        init {
            mConvertView = convertView

            if (mIsNews) {
                mLineView = convertView.list_item_news_line_view
                mNewImageView = convertView.list_item_news_image_view
                mTitleTextView = convertView.list_item_news_title_text_view
                mContentView = convertView.list_item_news_content_text_view
                mViewCountTextView = convertView.list_item_news_view_count_text_view
                mCommentCountTextView = convertView.list_item_news_comment_count_text_view
            } else {
                mNoticeLineView = convertView.list_item_notice_line_view
                mNoticeTitleTextView = convertView.list_item_notice_title_text_view
                mNoticeContentTextView = convertView.list_item_notice_content_text_view
                mNoticeDateTextView = convertView.list_item_notice_date_text_view
                mNoticeViewCountTextView = convertView.list_item_notice_view_count_text_view
            }

        }
    }
}