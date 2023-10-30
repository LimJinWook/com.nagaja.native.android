package com.nagaja.app.android.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.list_item_pre_view_image.view.*


class PreViewPagerAdapter(context: Context, imagePathList: ArrayList<String>) : PagerAdapter() {
    private var mImagesPath: ArrayList<String> = ArrayList()
    private val mContext: Context

    override fun getCount(): Int {
        return mImagesPath.size
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.list_item_pre_view_image, container, false)
        val imageView: PhotoView = view.list_item_pre_view_image_view
//        imageView.setImageResource(mImagesPath[position])

//        val options: RequestOptions = RequestOptions()
//            .placeholder(R.drawable.bg_default)
//            .error(R.drawable.bg_default)
//            .fitCenter()
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .priority(Priority.HIGH)

        imageView.setOnMatrixChangeListener {
            imageView.setAllowParentInterceptOnEdge(imageView.scale.toInt() == 1)
        }

        Glide.with(mContext)
            .load(mImagesPath[position])
//            .apply(options)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
//                    mProgressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
//                    mProgressBar.visibility = View.GONE
                    return false
                }
            })
            .into(imageView)

        container.addView(view)
        return view
    }

    companion object {
        private const val TAG = "ShopNewsPagerAdapter"
    }

    init {
        this.mContext = context
        this.mImagesPath = imagePathList
    }
}
