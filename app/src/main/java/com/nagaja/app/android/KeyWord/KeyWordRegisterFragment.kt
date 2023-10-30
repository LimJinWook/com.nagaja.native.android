package com.nagaja.app.android.KeyWord

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.RoomDB.KeyWord.Contacts
import com.nagaja.app.android.RoomDB.KeyWord.KeyWordDatabase
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.Utils.setOnSingleClickListener
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_key_word_register.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*


class KeyWordRegisterFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mRegisterEditText: EditText

    private lateinit var mTypeSpinner: PowerSpinnerView

    private var mSelectType = -1
    
    private lateinit var mListener: OnKeyWordRegisterFragmentListener

    interface OnKeyWordRegisterFragmentListener {
        fun onBack()
    }

    companion object {
        fun newInstance(): KeyWordRegisterFragment {
            val args = Bundle()
            val fragment = KeyWordRegisterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_key_word_register, container, false)

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
        titleTextView.text = mActivity.resources.getString(R.string.text_title_key_word_register)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Type Spinner
        mTypeSpinner = mContainer.fragment_key_word_register_type_spinner
        mTypeSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSelectType = newIndex
        })

        // Register Edit Text
        mRegisterEditText = mContainer.fragment_key_word_register_edit_text
        mRegisterEditText.filters = arrayOf(Util().blankSpaceFilter, InputFilter.LengthFilter(10))
        mRegisterEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Confirm Text View
        val confirmTextView = mContainer.fragment_key_word_register_confirm_text_view
        confirmTextView.setOnSingleClickListener {
            if (mSelectType < 0) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_error_select_key_word_type))
                return@setOnSingleClickListener
            }

            if (TextUtils.isEmpty(mRegisterEditText.text)) {
                showToast(mActivity, mActivity.resources.getString(R.string.text_hint_key_word))
                return@setOnSingleClickListener
            }

            updateKeyWordRoomDB()
        }
    }

    private fun updateKeyWordRoomDB() {
        val keyWordRoomDB = KeyWordDatabase.getInstance(mActivity)
        val contacts = Contacts(0, mSelectType, mRegisterEditText.text.toString())

        val keyWordListData = keyWordRoomDB!!.contactsDao().getAll()
        for (i in keyWordListData.indices) {
            if ((mSelectType == keyWordListData[i].category) && (mRegisterEditText.text.toString() == keyWordListData[i].keyWord)) {
                showToast(mActivity, "xxxxxxxxxxxxx")
            }
        }

        keyWordRoomDB!!.contactsDao().insertAll(contacts)

        mListener.onBack()
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
        if (context is OnKeyWordRegisterFragmentListener) {
            mActivity = context as Activity

            if (context is OnKeyWordRegisterFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnKeyWordRegisterFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnKeyWordRegisterFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnKeyWordRegisterFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

}