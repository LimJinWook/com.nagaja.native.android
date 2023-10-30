package com.nagaja.app.android.Job

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nagaja.app.android.Adapter.NativeJobDetailAttachFileAdapter
import com.nagaja.app.android.Adapter.StoreMainImageAdapter
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.NativeBoardFileData
import com.nagaja.app.android.Data.NativeBoardImageData
import com.nagaja.app.android.Data.NativeJobAndMissingData
import com.nagaja.app.android.Utils.NagajaLog
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_native_job_detail.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class NativeJobDetailFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mImageRecyclerView: RecyclerView
    private lateinit var mAttachFileRecyclerView: RecyclerView

    private lateinit var mAttachFileAdapter: NativeJobDetailAttachFileAdapter
    private lateinit var mImageAdapter: StoreMainImageAdapter

    private lateinit var mListener: OnNativeJobDetailFragmentListener

     private var mIsDownloading = false

    interface OnNativeJobDetailFragmentListener {
        fun onBack()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
    }

    companion object {
        const val ARGS_JOB_DATA        = "args_job_data"

        fun newInstance(data: NativeJobAndMissingData): NativeJobDetailFragment {
            val args = Bundle()
            val fragment = NativeJobDetailFragment()
            args.putSerializable(ARGS_JOB_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getJobData(): NativeJobAndMissingData? {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_JOB_DATA) as NativeJobAndMissingData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mImageAdapter = StoreMainImageAdapter(mActivity)
        mAttachFileAdapter = NativeJobDetailAttachFileAdapter(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_native_job_detail, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onBack()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = if (getJobData()!!.getParentCategoryUid() == 40) mActivity.resources.getString(R.string.text_job) else mActivity.resources.getString(R.string.text_job_search)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Job And Job Search Text View
        val jobAndJobSearchTextView = mContainer.fragment_job_detail_job_and_job_search_text_view
        jobAndJobSearchTextView.text = if (getJobData()!!.getParentCategoryUid() == 40) mActivity.resources.getString(R.string.text_job) else mActivity.resources.getString(R.string.text_job_search)

        // Job Title Text View
        val jobTitleTextView = mContainer.fragment_job_detail_title_text_view
        if (!TextUtils.isEmpty(getJobData()!!.getBoardSubject())) {
            jobTitleTextView.text = getJobData()!!.getBoardSubject()
        }

        // Date Text View
        val dateTextView = mContainer.fragment_job_detail_date_text_view
        if (!TextUtils.isEmpty(getJobData()!!.getBeginDate()) && getJobData()!!.getBeginDate() != "null") {
            dateTextView.text = /*Util().getDateTimeDay(getJobData()!!.getBeginDate())*/getJobData()!!.getBeginDate()
        }

        // View Count Text View
        val viewCountTextView = mContainer.fragment_job_detail_view_count_text_view
        viewCountTextView.text = if (getJobData()!!.getViewCount() > 0) getJobData()!!.getViewCount().toString() else "0"

        // Attach File Recycler View
        mAttachFileRecyclerView = mContainer.fragment_job_detail_attach_file_recycler_view
        mAttachFileRecyclerView.setHasFixedSize(true)
        mAttachFileRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mAttachFileAdapter.setOnAttachFileItemClickListener(object : NativeJobDetailAttachFileAdapter.OnAttachFileItemClickListener {
            override fun onClick(data: NativeBoardFileData) {
                try {
                    val uri = Uri.parse("https://the330-nagaja.s3.ap-northeast-2.amazonaws.com/" + "board/file/" + data.getBoardFileName())
                    NagajaLog().d("wooks, mAttachFileAdapter File = $uri")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    NagajaLog().e("wooks, You don't have any browser to open web page")
                }
            }
        })
        mAttachFileRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mAttachFileRecyclerView.adapter = mAttachFileAdapter
        if (getJobData()!!.getBoardFileList().size > 0) {
            mAttachFileAdapter.setData(getJobData()!!.getBoardFileList())
        }

        // Download Container View
        val downloadContainerView = mContainer.fragment_job_detail_download_container_view
        downloadContainerView.visibility = if (getJobData()!!.getBoardFileList().size > 0) View.VISIBLE else View.GONE

//        // Download View
//        val downloadView = mContainer.fragment_job_detail_download_view
//        downloadView.setOnSingleClickListener {
//            if (mIsDownloading) {
//                return@setOnSingleClickListener
//            }
//
//            mLoadingView.visibility = View.VISIBLE
//            downloadFile()
//        }
//
//        // Download File Name Text View
//        val downloadFileNameTextView = mContainer.fragment_job_detail_download_file_name_text_view
//        if (!TextUtils.isEmpty(getJobData()!!.getDownloadUrl()) && !TextUtils.isEmpty(getJobData()!!.getDownloadFileName())) {
//            downloadFileNameTextView.text = getJobData()!!.getDownloadFileName()
//        }
//
//        // Download Extension Text View
//        val downloadExtensionTextView = mContainer.fragment_job_detail_download_extension_text_view
//        if (!TextUtils.isEmpty(getJobData()!!.getDownloadUrl()) && !TextUtils.isEmpty(getJobData()!!.getDownloadFileName()) && !TextUtils.isEmpty(getJobData()!!.getExtension())) {
//            downloadExtensionTextView.text = "." + getJobData()!!.getExtension()
//        }
//
        // Image Recycler View
        mImageRecyclerView = mContainer.fragment_job_detail_image_recycler_view
        mImageRecyclerView.setHasFixedSize(true)
        mImageRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mImageAdapter.setOnImageClickListener(object : StoreMainImageAdapter.OnImageClickListener {
            override fun onClick(position: Int) {
                mListener.onFullScreenImage(getImageList(getJobData()!!.getBoardImageList()), position)
            }
        })
        mImageRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mImageRecyclerView.adapter = mImageAdapter
        if (getJobData()!!.getBoardImageList().size > 0) {
            mImageRecyclerView.visibility = View.VISIBLE
            mImageAdapter.setData(getImageList(getJobData()!!.getBoardImageList()))
        } else {
            mImageRecyclerView.visibility = View.GONE
        }

        // Content Text View
        val contentTextView = mContainer.fragment_job_detail_content_text_view
        if (!TextUtils.isEmpty(getJobData()!!.getBoardContent())) {
            contentTextView.text = getJobData()!!.getBoardContent()
        }

        // Back Text View
        val backTextView = mContainer.fragment_job_detail_back_text_view
        backTextView.setOnSingleClickListener {
            mListener.onBack()
        }

        // Loading View
//        mLoadingView = mContainer.fragment_job_detail_loading_view
//
//        // Download Popup View
//        mDownloadPopupView = mContainer.fragment_job_detail_download_popup_view
//        mDownloadPercentTextView = mContainer.fragment_job_detail_download_percent_text_view
//        mDownloadPercent2TextView = mContainer.fragment_job_detail_download_percent_2_text_view
//        mDownloadProgressBar = mContainer.fragment_job_detail_download_progress_bar
//        mDownloadProgressBar.isIndeterminate = true
//
//

    }

//    private fun downloadFile() {
//
//        val savePath = Environment.getExternalStorageDirectory().absolutePath + "/" + "NAGAJA_Download"
//        val dir = File(savePath)
//        if (!dir.exists()) {
//            dir.mkdir()
//        }
//
//        PRDownloader.download(getJobData()!!.getDownloadUrl(), savePath, getJobData()!!.getDownloadFileName() + "." + getJobData()!!.getExtension())
//            .build()
//            .setOnStartOrResumeListener {
//                NagajaLog().d("wooks, Download Start ========================== ")
//                if (mLoadingView.visibility == View.VISIBLE) {
//                    mLoadingView.visibility = View.GONE
//                }
//
//                mIsDownloading = true
//            }
//            .setOnPauseListener {}
//            .setOnCancelListener {
//                mIsDownloading = false
//            }
//            .setOnProgressListener {
//                val progressPercent: Long = it.currentBytes * 100 / it.totalBytes
//                NagajaLog().d("wooks, progressPercent = " + progressPercent.toInt())
//
//                mDownloadPopupView.visibility = View.VISIBLE
//
//                mDownloadProgressBar.isIndeterminate = false
//                mDownloadProgressBar.progress = progressPercent.toInt()
//
//                mDownloadPercentTextView.text = String.format(mActivity.resources.getString(R.string.text_download_percent), progressPercent)
//            }
//            .start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                    NagajaLog().d("wooks, onDownloadComplete")
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        mDownloadPopupView.visibility = View.GONE
//                        showToast(mActivity, mActivity.resources.getString(R.string.text_download_complete))
//                    }, 1000)
//
//                    mIsDownloading = false
//                }
//
//                override fun onError(error: com.downloader.Error?) {
//                    NagajaLog().d("wooks, downloader onError= " + error.toString())
//
//                    mIsDownloading = false
//                }
//
//                fun onError(error: Error?) {}
//            })
//    }

    private fun getImageList(imageList: ArrayList<NativeBoardImageData>): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until imageList.size) {
            val url = "https://the330-nagaja.s3.ap-northeast-2.amazonaws.com"
            list.add(url + imageList[i].getBoardImageName())
        }

        return list
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
        if (context is OnNativeJobDetailFragmentListener) {
            mActivity = context as Activity

            if (context is OnNativeJobDetailFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnNativeJobDetailFragmentListener"
                )
            }
        }
    }

    /**
      * Called when the fragment attaches to the context
      */
    protected fun onAttachToContext(context: Context) {
        if (context is OnNativeJobDetailFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnNativeJobDetailFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}