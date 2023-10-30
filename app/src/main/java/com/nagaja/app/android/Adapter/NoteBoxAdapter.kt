package com.nagaja.app.android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Data.NoteData
import com.nagaja.app.android.Utils.MAPP
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_focus.view.*
import kotlinx.android.synthetic.main.list_item_note_box.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_reservation.view.*
import kotlinx.android.synthetic.main.list_item_reservation_category.view.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class NoteBoxAdapter(context: Context, isCompany: Boolean, myCompanyUid: Int): RecyclerView.Adapter<NoteBoxAdapter.ViewHolder>() {
    private var mNoteListData: ArrayList<NoteData> = ArrayList()
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mOnNoteBoxClickListener: OnNoteBoxClickListener? = null
    private var mIsCompany: Boolean = false
    private var mMyCompanyUid: Int = 0

    init {
        mContext = context
        if (null != mContext) {
            mInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } else {
            mInflater = null
        }
        mIsCompany = isCompany
        mMyCompanyUid = myCompanyUid
    }

    interface OnNoteBoxClickListener {
        fun onClick(data: NoteData)
        fun onCheck(data: NoteData, position: Int, isChecked: Boolean)
    }

    fun setOnNoteBoxClickListener(listener: OnNoteBoxClickListener?) {
        mOnNoteBoxClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: ArrayList<NoteData>) {
        if (null != mNoteListData) {
            mNoteListData.clear()
        }
        
        val itemList: ArrayList<NoteData> = ArrayList<NoteData>()
        if (null != dataList) {
            for (item in dataList) {
                if (null == item) {
                    continue
                }
                itemList.add(item)
            }
        }
        mNoteListData = itemList
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        if (null != mNoteListData) {
            val data: NoteData = mNoteListData[position]
            if (null != data) {
                return 0
            }
        }
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (null != mNoteListData) {
            mNoteListData.size
        } else 0
    }

    fun getItem(position: Int): NoteData? {
        if (null != mNoteListData) {
            if (0 <= position && position < mNoteListData.size) {
                return mNoteListData[position]
            }
        }
        return null
    }

    fun deleteItem(position: Int) {
//        mNoteListData.remove(mNoteListData)
//        mNoteListData.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, itemCount)

    }

    fun deleteItem() {
        val listData = ArrayList<NoteData>()
        for (i in 0 until mNoteListData.size) {
            if (!mNoteListData[i].getIsCheck()) {
                listData.add(mNoteListData[i])
            }
        }

        mNoteListData.clear()
        mNoteListData = listData

        notifyDataSetChanged()
    }

    fun setItemChecked(position: Int, isChecked: Boolean) {
        mNoteListData[position].setIsCheck(isChecked)
        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_note_box,
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
        val data: NoteData = getItem(position) ?: return

        var isReceive = false

        if (position == 0) {
            viewHolder.mTopLineView.visibility = View.GONE
        } else {
            viewHolder.mTopLineView.visibility = View.VISIBLE
        }

        // Check Image View
        viewHolder.mCheckImageView.setImageResource(if (data.getIsCheck()) R.drawable.icon_select else R.drawable.icon_un_select)
        viewHolder.mCheckImageView.setOnClickListener {

            mOnNoteBoxClickListener!!.onCheck(data, position, !data.getIsCheck())
        }

        // Message Text View
        if (!TextUtils.isEmpty(data.getNoteMessage())) {
            viewHolder.mMessageTextView.text = data.getNoteMessage()
        }

        // Send Receive Image View
//        if (data.getReceiveMemberUid() == MAPP.USER_DATA.getMemberUid()) {
//            viewHolder.mSendReceiveImageView.setImageResource(R.drawable.icon_receive)
//            isReceive = true
//        } else if (data.getSendMemberUid() == MAPP.USER_DATA.getMemberUid()) {
//            viewHolder.mSendReceiveImageView.setImageResource(R.drawable.icon_send)
//            isReceive = false
//        }

        if (mIsCompany) {
            if (data.getReceiveCompanyUid() == mMyCompanyUid) {
                viewHolder.mSendReceiveImageView.setImageResource(R.drawable.icon_receive)
                isReceive = true
            } else if (data.getCompanyUid() == mMyCompanyUid) {
                viewHolder.mSendReceiveImageView.setImageResource(R.drawable.icon_send)
                isReceive = false
            }
        } else {
            if (data.getReceiveMemberUid() == MAPP.USER_DATA.getMemberUid()) {
                viewHolder.mSendReceiveImageView.setImageResource(R.drawable.icon_receive)
                isReceive = true
            } else if (data.getSendMemberUid() == MAPP.USER_DATA.getMemberUid()) {
                viewHolder.mSendReceiveImageView.setImageResource(R.drawable.icon_send)
                isReceive = false
            }
        }

        data.setIsSender(!isReceive)

        // Timestamp Text View
        val createData = if (isReceive) data.getReceiveCreateDate() else data.getSendCreateDate()
        if (!TextUtils.isEmpty(createData)) {
            var time = createData
            val index: Int = time.indexOf("+")
            time = time.substring(0, index)

            val parsed: OffsetDateTime = LocalDateTime.parse(time).atOffset(ZoneOffset.UTC)
            val zone = ZoneId.of(Util().getTimeZoneID(mContext!!))
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            viewHolder.mDateTextView.text = outputFormatter.format(parsed.atZoneSameInstant(zone))
        }

        // Name Text View
        var senderName = ""
        if (isReceive) {
            if (TextUtils.isEmpty(data.getSendMemberName()) && !TextUtils.isEmpty(data.getCompanyName())) {
                senderName = data.getCompanyName()
            } else if (!TextUtils.isEmpty(data.getSendMemberName()) && TextUtils.isEmpty(data.getCompanyName())) {
                senderName = data.getSendMemberName()
            }
        } else {
            if (TextUtils.isEmpty(data.getReceiveMemberName()) && !TextUtils.isEmpty(data.getReceiveCompanyName())) {
                senderName = data.getReceiveCompanyName()
            } else if (!TextUtils.isEmpty(data.getReceiveMemberName()) && TextUtils.isEmpty(data.getReceiveCompanyName())) {
                senderName = data.getReceiveMemberName()
            }
        }
        if (!TextUtils.isEmpty(senderName)) {
            viewHolder.mNameTextView.text = senderName
        }

        // Convert View
        viewHolder.mContainerView.setOnClickListener {
            mOnNoteBoxClickListener!!.onClick(data)
        }
    }

    class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var mConvertView: View
        var mContainerView: View
        var mTopLineView: View
        var mCheckImageView: ImageView
        var mMessageTextView: TextView
        var mSendReceiveImageView: ImageView
        var mDateTextView: TextView
        var mNameTextView: TextView

        init {
            mConvertView = convertView
            mContainerView = convertView.list_item_note_container_view
            mTopLineView = convertView.list_item_note_box_top_line_view
            mCheckImageView = convertView.list_item_note_box_check_image_view
            mMessageTextView = convertView.list_item_note_box_message_text_view
            mSendReceiveImageView = convertView.list_item_note_box_send_receive_image_view
            mDateTextView = convertView.list_item_note_box_timestamp_text_view
            mNameTextView = convertView.list_item_note_box_name_text_view
        }
    }
}