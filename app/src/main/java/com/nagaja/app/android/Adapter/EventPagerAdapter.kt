package com.nagaja.app.android.Adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Data.BannerEventData
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_event_view_pager.view.*

class EventPagerAdapter(
    private val mContext: Context,
    private var mBannerEventListData: ArrayList<BannerEventData> = ArrayList(),
    private val mImageList: ArrayList<Int>?) : PagerAdapter() {

    private var mOnEventPagerClickListener: OnEventPagerClickListener? = null

    interface OnEventPagerClickListener {
        fun onClick(linkUrl: String?)
        fun onEventShow(categoryUid: Int, targetUid: Int)
    }

    fun setOnEventPagerClickListener(listener: OnEventPagerClickListener?) {
        mOnEventPagerClickListener = listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.list_item_event_view_pager, null)

        val imageView = view.list_item_event_view_pager_test_image_view
        imageView.clipToOutline = true
        if (null != mImageList) {
            imageView.setImageResource(mImageList[position])
        }

        val simpleDraweeView = view.list_item_event_view_pager_image_view
        if (null != simpleDraweeView) {
            imageView!!.clipToOutline = true
            simpleDraweeView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + mBannerEventListData[position].getBannerImageName()))
        }

        simpleDraweeView!!.visibility = View.VISIBLE
        imageView!!.visibility = View.GONE
        view.setOnClickListener {
//            if (simpleDraweeView.visibility == View.VISIBLE) {
//                if (null != mOnEventPagerClickListener) {
//                    if (!TextUtils.isEmpty(mBannerEventListData[position].getBannerTargetUri())) {
//                        mOnEventPagerClickListener!!.onClick(mBannerEventListData[position].getBannerTargetUri())
//                    }
//                }
//            }

            if (!TextUtils.isEmpty(mBannerEventListData[position].getBannerTargetUri())) {
                mOnEventPagerClickListener!!.onClick(mBannerEventListData[position].getBannerTargetUri())
            } else {
                mOnEventPagerClickListener!!.onEventShow(mBannerEventListData[position].getCategoryUid(), mBannerEventListData[position].getTargetUid())
            }
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return if (null != mImageList && mImageList.size > 0) {
            mImageList.size
        } else {
            mBannerEventListData!!.size
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o as View
    }
}