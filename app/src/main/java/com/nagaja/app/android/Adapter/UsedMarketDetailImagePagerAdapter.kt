package com.nagaja.app.android.Adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_used_market_detail_image.view.*

class UsedMarketDetailImagePagerAdapter(
    private val mContext: Context,
    private var mImageListData: ArrayList<String> = ArrayList(),
    private val mImageList: ArrayList<Int>?) : PagerAdapter() {

    private var mOnImagePagerClickListener: OnImagePagerClickListener? = null

    interface OnImagePagerClickListener {
        fun onClick(position: Int)
    }

    fun setOnImagePagerClickListener(listener: OnImagePagerClickListener?) {
        mOnImagePagerClickListener = listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.list_item_used_market_detail_image, null)

        val simpleDraweeView = view.list_item_used_market_detail_view_pager_image_view
        if (null != simpleDraweeView) {
            if (null != mImageListData) {
                if (!TextUtils.isEmpty(mImageListData[position])) {
                    simpleDraweeView.setImageURI(Uri.parse(mImageListData[position]))
                } else {
                    simpleDraweeView.setImageURI(Uri.parse("res:///" + R.drawable.bg_default))
                }
            }
        }

        view.setOnClickListener {
            mOnImagePagerClickListener!!.onClick(position)
        }

        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return if (null != mImageList && mImageList.size > 0) {
            mImageList.size
        } else {
            mImageListData!!.size
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o as View
    }
}