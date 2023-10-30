package com.nagaja.app.android.KeyWord

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.RoomDB.KeyWord.Contacts
import com.nagaja.app.android.RoomDB.KeyWord.KeyWordDatabase
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_key_word.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*
import kotlin.collections.ArrayList


class KeyWordFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mUsedMarketEmptyTextView: TextView
    private lateinit var mBoardEmptyTextView: TextView
    
    private lateinit var mScrollView: NestedScrollView

    private lateinit var mUsedMarketKeyWordRecyclerView: RecyclerView
    private lateinit var mBoardKeyWordRecyclerView: RecyclerView

    private lateinit var mKeyWordAdapter: KeyWordAdapter
    private lateinit var mBoardKeyWordAdapter: KeyWordAdapter

    private lateinit var mKeyWordRoomDB: KeyWordDatabase

    private lateinit var mKeyWordListData: List<Contacts>

    private lateinit var mListener: OnKeyWordFragmentListener

    interface OnKeyWordFragmentListener {
        fun onFinish()
        fun onKeyWordRegisterScreen()
    }

    companion object {
        fun newInstance(): KeyWordFragment {
            val args = Bundle()
            val fragment = KeyWordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mKeyWordAdapter = KeyWordAdapter(mActivity)
        mBoardKeyWordAdapter = KeyWordAdapter(mActivity)

        mKeyWordRoomDB = KeyWordDatabase.getInstance(mActivity)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_key_word, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        // Scroll View
        mScrollView = mContainer.fragment_key_word_scroll_view

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.GONE

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_setting_keyword)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.VISIBLE
        cancelImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Key Word Recycler View
        mUsedMarketKeyWordRecyclerView = mContainer.fragment_key_word_used_market_recycler_view
        mUsedMarketKeyWordRecyclerView.setHasFixedSize(true)

        val layoutManager = FlexboxLayoutManager(mActivity)
        layoutManager.flexDirection = FlexDirection.ROW
//        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        mUsedMarketKeyWordRecyclerView.layoutManager = layoutManager

        mKeyWordAdapter.setOnKeyWordDeleteClickListener(object : KeyWordAdapter.OnKeyWordDeleteClickListener {
            override fun onClick(data: Contacts) {
                showDeletePopup(data)
            }
        })
        mUsedMarketKeyWordRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mUsedMarketKeyWordRecyclerView.adapter = mKeyWordAdapter

        // Used Market Empty Text View
        mUsedMarketEmptyTextView = mContainer.fragment_key_word_used_market_empty_text_view




        // Board Key Word Recycler View
        mBoardKeyWordRecyclerView = mContainer.fragment_key_word_board_recycler_view
        mBoardKeyWordRecyclerView.setHasFixedSize(true)

        val boardKeywordLayoutManager = FlexboxLayoutManager(mActivity)
        boardKeywordLayoutManager.flexDirection = FlexDirection.ROW
//        boardKeywordLayoutManager.justifyContent = JustifyContent.CENTER
        boardKeywordLayoutManager.alignItems = AlignItems.CENTER
        mBoardKeyWordRecyclerView.layoutManager = boardKeywordLayoutManager

        mBoardKeyWordAdapter.setOnKeyWordDeleteClickListener(object : KeyWordAdapter.OnKeyWordDeleteClickListener {
            override fun onClick(data: Contacts) {
                showDeletePopup(data)
            }
        })
        mBoardKeyWordRecyclerView.recycledViewPool.setMaxRecycledViews(0, 25)
        mBoardKeyWordRecyclerView.adapter = mBoardKeyWordAdapter

        // Used Market Empty Text View
        mBoardEmptyTextView = mContainer.fragment_key_word_board_empty_text_view




        // Key Word Register Text View
        val keyWordRegisterTextView = mContainer.fragment_key_word_register_text_view
        keyWordRegisterTextView.setOnSingleClickListener {
            mListener.onKeyWordRegisterScreen()
        }


        setKeyWordRoomDB()

    }

    /**
     * Key-Word Room DB Setting
     * */
    private fun setKeyWordRoomDB() {
        val usedMarketListData = ArrayList<Contacts>()
        val boardListData = ArrayList<Contacts>()

        mKeyWordListData = emptyList()

        mKeyWordListData = mKeyWordRoomDB.contactsDao().getAll()
        if (mKeyWordListData.isNotEmpty()) {
            for (i in mKeyWordListData.indices) {
                if (mKeyWordListData[i].category == 0) {
                    usedMarketListData.add(mKeyWordListData[i])
                } else {
                    boardListData.add(mKeyWordListData[i])
                }
            }
        }

        if (usedMarketListData.isNotEmpty()) {
            mKeyWordAdapter.setData(usedMarketListData)
        } else {
            mUsedMarketKeyWordRecyclerView.visibility = View.GONE
            mUsedMarketEmptyTextView.visibility = View.VISIBLE
        }

        if (boardListData.isNotEmpty()) {
            mBoardKeyWordAdapter.setData(boardListData)
        } else {
            mBoardKeyWordRecyclerView.visibility = View.GONE
            mBoardEmptyTextView.visibility = View.VISIBLE
        }
    }

    /**
     * Show Key-Word Delete Popup
     * */
    private fun showDeletePopup(data: Contacts) {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NORMAL,

            mActivity.resources.getString(R.string.text_title_delete_key_word),
            "'" + data.keyWord + "'\n" + mActivity.resources.getString(R.string.text_message_delete_key_word),
            mActivity.resources.getString(R.string.text_cancel),
            mActivity.resources.getString(R.string.text_delete)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
                customDialog.dismiss()
            }

            override fun onConfirm() {
                deleteKeyWordData(data)
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    /**
     * Delete Key-Word Room DB & List Data
     * */
    private fun deleteKeyWordData(data: Contacts) {
        for (i in mKeyWordListData.indices) {
            if ((data.category == mKeyWordListData[i].category) && (data.keyWord == mKeyWordListData[i].keyWord)) {
                mKeyWordRoomDB.contactsDao().delete(mKeyWordListData[i])
                setKeyWordRoomDB()
                break
            }
        }
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
        if (context is OnKeyWordFragmentListener) {
            mActivity = context as Activity

            if (context is OnKeyWordFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnKeyWordFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnKeyWordFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnKeyWordFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}