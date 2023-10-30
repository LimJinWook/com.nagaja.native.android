package com.nagaja.app.android.FullScreenImage

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.viewpager.widget.ViewPager
import com.github.chrisbanes.photoview.PhotoView
import com.nagaja.app.android.Adapter.PreViewPagerAdapter
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.NagajaLog

import kotlinx.android.synthetic.main.fragment_full_screen_image.view.*


class FullScreenImageFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mProgressBar: ProgressBar

    private lateinit var mPhotoView: PhotoView

//    private lateinit var mViewPager: CustomViewPager
    private lateinit var mViewPager: ViewPager

    private lateinit var mPreViewPagerAdapter: PreViewPagerAdapter

    private lateinit var mListener: OnFullScreenImageFragmentListener

    interface OnFullScreenImageFragmentListener {
        fun onFinish()
    }

    companion object {
        private const val ARG_IMAGE_PATH_LIST       = "arg_image_path_list"
        private const val ARG_IMAGE_POSITION        = "arg_image_position"

        fun newInstance(imagePathList: ArrayList<String>, position: Int): FullScreenImageFragment {
            val args = Bundle()
            val fragment = FullScreenImageFragment()
            args.putSerializable(ARG_IMAGE_PATH_LIST, imagePathList)
            args.putInt(ARG_IMAGE_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getImagePathList(): ArrayList<String> {
        val arguments = arguments
        return arguments?.getSerializable(ARG_IMAGE_PATH_LIST) as ArrayList<String>
    }

    private fun getImagePosition(): Int {
        val arguments = arguments
        return arguments?.getInt(ARG_IMAGE_POSITION)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {

        mContainer = inflater.inflate(R.layout.fragment_full_screen_image, container, false)

        init()

        return mContainer
    }

    private fun init() {

        mViewPager = mContainer.fragment_full_screen_view_pager

        val mViewPagerIndicator = mContainer.fragment_full_screen_dot_indicator

        NagajaLog().e("wooks, !!! = ${getImagePathList()[0]}")
        mPreViewPagerAdapter = PreViewPagerAdapter(mActivity, getImagePathList())
        mViewPager.adapter = mPreViewPagerAdapter
        mViewPagerIndicator.setViewPager(mViewPager)
//        if (getImagePathList().size > 1) {
//            mViewPagerIndicator.setViewPager(mViewPager)
//        } else if (getImagePathList().size <= 1) {
//            mViewPagerIndicator.visibility = View.GONE
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            mViewPager.currentItem = getImagePosition()
        }, 10)
    }

    /**
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFullScreenImageFragmentListener) {
            mActivity = context as Activity

            if (context is OnFullScreenImageFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnFullScreenImageFragmentListener"
                )
            }
        }
    }

    /*
         * Called when the fragment attaches to the context
         */
    protected fun onAttachToContext(context: Context) {
        if (context is OnFullScreenImageFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFullScreenImageFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}