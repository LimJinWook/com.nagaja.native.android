package com.nagaja.app.android.Job

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.CategoryAdapter
import com.nagaja.app.android.Adapter.NativeJobAdapter
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.NagajaManager
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_job.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.fragment_regular.view.*
import kotlinx.android.synthetic.main.fragment_setting_profile.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class NativeJobFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View
    private lateinit var mJobUnderLineView: View
    private lateinit var mJobSearchUnderLineView: View
    private lateinit var mSearchConfirmView: View

    private lateinit var mSearchEditText: EditText

    private lateinit var mJobTextView: TextView
    private lateinit var mJobSearchTextView: TextView
    private lateinit var mJobAndRecruitingTextView: TextView
    private lateinit var mTotalCountTextView: TextView

    private lateinit var mCategoryRecyclerView: RecyclerView
    private lateinit var mJobRecyclerView: RecyclerView

    private lateinit var mCategoryAdapter: CategoryAdapter
    private lateinit var mNativeJobAdapter: NativeJobAdapter

    private var mJobListData: ArrayList<NativeJobAndMissingData> = ArrayList()

    private var mType = 0

    private var mIsPause = false

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mListener: OnNativeJobFragmentListener

    interface OnNativeJobFragmentListener {
        fun onFinish()
        fun onNativeJobDetailScreen(data: NativeJobAndMissingData)
    }

    companion object {
        const val SELECT_TYPE_JOB                        = 0x00
        const val SELECT_TYPE_JOB_SEARCH                 = 0x01

        fun newInstance(): NativeJobFragment {
            val args = Bundle()
            val fragment = NativeJobFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCategoryAdapter = CategoryAdapter(mActivity)
        mNativeJobAdapter = NativeJobAdapter(mActivity)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_job, container, false)

        init()

        return mContainer
    }

    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_job_and_job_search)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Jon Text View
        mJobTextView = mContainer.fragment_job_text_view
        mJobTextView.setOnSingleClickListener {
            setTabView(SELECT_TYPE_JOB)
            mType = SELECT_TYPE_JOB
        }

        // Job Under-Line View
        mJobUnderLineView = mContainer.fragment_job_under_line_view

        // Job Search Text View
        mJobSearchTextView = mContainer.fragment_job_search_text_view
        mJobSearchTextView.setOnSingleClickListener {
            setTabView(SELECT_TYPE_JOB_SEARCH)
            mType = SELECT_TYPE_JOB_SEARCH
        }

        // Job Search Under-Line View
        mJobSearchUnderLineView = mContainer.fragment_job_search_under_line_view

        // Category Recycler View
        mCategoryRecyclerView = mContainer.fragment_job_category_recycler_view
        mCategoryRecyclerView.setHasFixedSize(true)
        mCategoryRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        mCategoryAdapter.setOnCategoryClickListener(object : CategoryAdapter.OnCategoryClickListener {
            override fun onClick(data: CategoryData, position: Int) {
                mCategoryAdapter.setSelectCategory(position)
            }
        })
        mCategoryRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mCategoryRecyclerView.adapter = mCategoryAdapter

        // Search Edit Text
        mSearchEditText = mContainer.fragment_job_search_edit_text
        mSearchEditText.filters = arrayOf(Util().blankSpaceFilter)
        mSearchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mSearchEditText.text.isEmpty()) {
                    displayJobListData(mJobListData)
                } else {
                    val searchListData = ArrayList<NativeJobAndMissingData>()
                    if (mJobListData.size > 0) {
                        for (i in 0 until mJobListData.size) {
                            if (mJobListData[i].getBoardSubject().contains(mSearchEditText.text.toString())) {
                                searchListData.add(mJobListData[i])
                            }
                        }
                    }

                    mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), searchListData.size.toString())
                    mNativeJobAdapter.setData(searchListData)
                }
            }
        })

        // Search Confirm View
        mSearchConfirmView = mContainer.fragment_job_search_confirm_view
//        mSearchConfirmView.setOnSingleClickListener {
//            if (TextUtils.isEmpty(mSearchEditText.text)) {
//                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_search))
//                return@setOnSingleClickListener
//            }
//
//            val searchListData = ArrayList<NativeJobAndMissingData>()
//            if (mJobListData.size > 0) {
//                for (i in 0 until mJobListData.size) {
//                    if (mJobListData[i].getBoardSubject().contains(mSearchEditText.text.toString())) {
//                        searchListData.add(mJobListData[i])
//                    }
//                }
//            }
//
//            mNativeJobAdapter.setData(searchListData)
//        }

        // Total Count Text View
        mTotalCountTextView = mContainer.fragment_job_total_count_text_view

        // Job Recycler View
        mJobRecyclerView = mContainer.fragment_job_recycler_view
        mJobRecyclerView.setHasFixedSize(true)
        mJobRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        mNativeJobAdapter.setOnJobItemClickListener(object : NativeJobAdapter.OnJobItemClickListener {
            override fun onClick(data: NativeJobAndMissingData) {
                mListener.onNativeJobDetailScreen(data)
            }
        })
        mJobRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mJobRecyclerView.adapter = mNativeJobAdapter

        // Job And Recruiting Text View
        mJobAndRecruitingTextView = mContainer.fragment_job_recruiting_text_view

        if (mIsPause) {
            mIsPause = false
            setTabView(mType)
        } else {
            setCategoryListData()
            setTabView(SELECT_TYPE_JOB)
        }
    }

    private fun setTabView(selectType: Int) {
        var isJob = false
        when (selectType) {
            SELECT_TYPE_JOB -> {
                mJobTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mJobTextView.setTypeface(mJobTextView.typeface, Typeface.BOLD)
                mJobUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mJobSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mJobSearchTextView.setTypeface(mJobSearchTextView.typeface, Typeface.NORMAL)
                mJobSearchUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mJobAndRecruitingTextView.text = mActivity.resources.getString(R.string.text_recruiting)

                isJob = true
            }

            SELECT_TYPE_JOB_SEARCH -> {
                mJobTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_bbbbbb))
                mJobTextView.setTypeface(mJobTextView.typeface, Typeface.NORMAL)
                mJobUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_e2e7ee))

                mJobSearchTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))
                mJobSearchTextView.setTypeface(mJobSearchTextView.typeface, Typeface.BOLD)
                mJobSearchUnderLineView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_color_0d4d97))

                mJobAndRecruitingTextView.text = mActivity.resources.getString(R.string.text_search_job)

                isJob = false
            }
        }

        getJobData(isJob)
//        setJobListData(selectType)
    }

    private fun setCategoryListData() {
        val categoryListData = ArrayList<CategoryData>()

        for (i in 0 .. 6) {
            val categoryData = CategoryData()
            when (i) {
                0 -> {
                    categoryData.setTitle("단기알바")
                    categoryData.setTypeValue(0)
                    categoryData.setIsSelect(true)
                }

                1 -> {
                    categoryData.setTitle("장기알바")
                    categoryData.setTypeValue(1)
                    categoryData.setIsSelect(false)
                }

                2 -> {
                    categoryData.setTitle("카테고리1")
                    categoryData.setTypeValue(2)
                    categoryData.setIsSelect(false)
                }

                3 -> {
                    categoryData.setTitle("카테고리2")
                    categoryData.setTypeValue(3)
                    categoryData.setIsSelect(false)
                }

                4 -> {
                    categoryData.setTitle("카테고리3")
                    categoryData.setTypeValue(4)
                    categoryData.setIsSelect(false)
                }

                5 -> {
                    categoryData.setTitle("카테고리4")
                    categoryData.setTypeValue(5)
                    categoryData.setIsSelect(false)
                }

                6 -> {
                    categoryData.setTitle("카테고리5")
                    categoryData.setTypeValue(6)
                    categoryData.setIsSelect(false)
                }
            }

            categoryListData.add(categoryData)
        }

        mCategoryAdapter.setData(categoryListData)

    }

    private fun displayJobListData(listData: ArrayList<NativeJobAndMissingData>) {
        if (listData.size <= 0) {
            mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), "0")
            return
        }

        mJobListData = listData

        mTotalCountTextView.text = String.format(mActivity.resources.getString(R.string.text_total_count), listData.size.toString())

        mNativeJobAdapter.setData(listData)
    }

    override fun onPause() {
        super.onPause()

        mIsPause = true
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
        if (context is OnNativeJobFragmentListener) {
            mActivity = context as Activity

            if (context is OnNativeJobFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNativeJobFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNativeJobFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNativeJobFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun getJobData(isJob: Boolean) {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getJobAndMissingListData(
            mRequestQueue,
            object : NagajaManager.RequestListener<ArrayList<NativeJobAndMissingData>> {
                override fun onSuccess(resultData: ArrayList<NativeJobAndMissingData>) {
                    if (null == resultData) {
                        return
                    }

                    mJobListData.clear()

                    if (isJob) {
                        resultData[0].setBoardContent("PagerAdapter is more general than the adapters used for AdapterViews. Instead of providing a View recycling mechanism directly ViewPager uses callbacks to indicate the steps taken during an update. A PagerAdapter may implement a form of View recycling if desired or use a more sophisticated method of managing page Views such as Fragment transactions where each page is represented by its own Fragment.\n" +
                                "\n" +
                                "ViewPager associates each page with a key Object instead of working with Views directly. This key is used to track and uniquely identify a given page independent of its position in the adapter. A call to the PagerAdapter method startUpdate indicates that the contents of the ViewPager are about to change. One or more calls to instantiateItem and/or destroyItem will follow, and the end of an update will be signaled by a call to finishUpdate. By the time finishUpdate returns the views associated with the key objects returned by instantiateItem should be added to the parent ViewGroup passed to these methods and the views associated with the keys passed to destroyItem should be removed. The method isViewFromObject identifies whether a page View is associated with a given key object.\n" +
                                "\n" +
                                "A very simple PagerAdapter may choose to use the page Views themselves as key objects, returning them from instantiateItem after creation and adding them to the parent ViewGroup. A matching destroyItem implementation would remove the View from the parent ViewGroup and isViewFromObject could be implemented as return view == object;.\n" +
                                "\n" +
                                "PagerAdapter supports data set changes. Data set changes must occur on the main thread and must end with a call to notifyDataSetChanged similar to AdapterView adapters derived from android.widget.BaseAdapter. A data set change may involve pages being added, removed, or changing position. The ViewPager will keep the current page active provided the adapter implements the method getItemPosition.\n" +
                                "\n")

                        resultData[2].setBoardContent("PagerAdapter is more general than the adapters used for AdapterViews. Instead of providing a View recycling mechanism directly ViewPager uses callbacks to indicate the steps taken during an update. A PagerAdapter may implement a form of View recycling if desired or use a more sophisticated method of managing page Views such as Fragment transactions where each page is represented by its own Fragment.\n" +
                                "\n" +
                                "ViewPager associates each page with a key Object instead of working with Views directly. This key is used to track and uniquely identify a given page independent of its position in the adapter. A call to the PagerAdapter method startUpdate indicates that the contents of the ViewPager are about to change. One or more calls to instantiateItem and/or destroyItem will follow, and the end of an update will be signaled by a call to finishUpdate. By the time finishUpdate returns the views associated with the key objects returned by instantiateItem should be added to the parent ViewGroup passed to these methods and the views associated with the keys passed to destroyItem should be removed. The method isViewFromObject identifies whether a page View is associated with a given key object.\n" +
                                "\n" +
                                "A very simple PagerAdapter may choose to use the page Views themselves as key objects, returning them from instantiateItem after creation and adding them to the parent ViewGroup. A matching destroyItem implementation would remove the View from the parent ViewGroup and isViewFromObject could be implemented as return view == object;.\n" +
                                "\n" +
                                "PagerAdapter supports data set changes. Data set changes must occur on the main thread and must end with a call to notifyDataSetChanged similar to AdapterView adapters derived from android.widget.BaseAdapter. A data set change may involve pages being added, removed, or changing position. The ViewPager will keep the current page active provided the adapter implements the method getItemPosition.\n" +
                                "\n")
                    }

                    displayJobListData(resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ErrorRequest.ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(mActivity)
                    }
                }

            },
            if (isJob) 0 else 1,
        )
    }
}