package com.nagaja.app.android.StoreReservation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.*
import com.nagaja.app.android.Utils.NetworkProvider.Companion.DEFAULT_IMAGE_DOMAIN
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.Data.*
import com.nagaja.app.android.Dialog.CustomDialog
import com.nagaja.app.android.Dialog.CustomDialogReservationDateSelectBottom
import com.nagaja.app.android.Util.ErrorRequest
import com.nagaja.app.android.Utils.*
import com.nagaja.app.android.R
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_CODE_NOT_ENOUGH_POINT
import com.nagaja.app.android.Util.ErrorRequest.Companion.ERROR_EXPIRED_TOKEN

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.fragment_report.view.*
import kotlinx.android.synthetic.main.fragment_store_reservation.view.*
import kotlinx.android.synthetic.main.fragment_used_market_register.view.*
import kotlinx.android.synthetic.main.layout_title.view.*
import java.util.*


class StoreReservationFragment : NagajaFragment() {

    private lateinit var mActivity: Activity

    private lateinit var mContainer: View

    private lateinit var mReservationDateTextView: TextView
    private lateinit var mReservationTimeTextView: TextView
    private lateinit var mTotalPersonTextView: TextView
    private lateinit var mReservationTextView: TextView

    private lateinit var mReservationNameEditText: EditText
    private lateinit var mReservationPhoneNumberEditText: EditText
    private lateinit var mReservationMemoEditText: EditText

    private lateinit var mPersonSpinner: PowerSpinnerView

    private lateinit var mListener: OnStoreReservationFragmentListener

    private lateinit var mRequestQueue: RequestQueue

    private lateinit var mReservationScheduleTimeData: ReservationScheduleTimeData

    private var mSelectPerson = -1

    interface OnStoreReservationFragmentListener {
        fun onFinish()
        fun onFullScreenImage(imageList: ArrayList<String>, position: Int)
        fun onReservationScreen()
    }

    companion object {

        private const val ARGS_STORE_DETAIL_DATA         = "args_store_detail_data"

        fun newInstance(data: StoreDetailData): StoreReservationFragment {
            val args = Bundle()
            val fragment = StoreReservationFragment()
            args.putSerializable(ARGS_STORE_DETAIL_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getStoreDetailData(): StoreDetailData {
        val arguments = arguments
        return arguments?.getSerializable(ARGS_STORE_DETAIL_DATA) as StoreDetailData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRequestQueue = Volley.newRequestQueue(mActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_store_reservation, container, false)

        init()

        return mContainer
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun init() {

        // Back Image View
        val backImageView = mContainer.layout_title_back_image_view
        backImageView.visibility = View.VISIBLE
        backImageView.setOnClickListener {
            mListener.onFinish()
        }

        // Title Text View
        val titleTextView = mContainer.layout_title_text_view
        titleTextView.text = mActivity.resources.getString(R.string.text_reservation)

        // Cancel Image View
        val cancelImageView = mContainer.layout_title_cancel_image_view
        cancelImageView.visibility = View.GONE

        // Image View
        val storeImageView = mContainer.fragment_store_reservation_image_view
        if (!TextUtils.isEmpty(getStoreDetailData().getMainImageUrl())) {
            storeImageView.setImageURI(Uri.parse(DEFAULT_IMAGE_DOMAIN + getStoreDetailData().getMainImageUrl()))
            storeImageView.setOnClickListener {
                val imageList = ArrayList<String>()
                imageList.add(DEFAULT_IMAGE_DOMAIN + getStoreDetailData().getMainImageUrl())

                if (imageList.size > 0) {
                    mListener.onFullScreenImage(imageList, 0)
                }
            }
        }

        // Store Name Text View
        val storeNameTextView = mContainer.fragment_store_reservation_store_name_text_view
        if (!TextUtils.isEmpty(getStoreDetailData().getCompanyName())) {
            storeNameTextView.text = getStoreDetailData().getCompanyName()
        }

        // Regular View
        val regularView = mContainer.fragment_store_reservation_regular_view
        regularView.visibility = if (getStoreDetailData().getIsRegularUseYn()) View.VISIBLE else View.GONE

        // Reservation Date Text View
        mReservationDateTextView = mContainer.fragment_store_reservation_date_text_view
        mReservationDateTextView.setOnClickListener {
            val customDialogReservationDateSelectBottom = CustomDialogReservationDateSelectBottom(mActivity, getStoreDetailData().getCompanyUid())
            customDialogReservationDateSelectBottom.setonSelectListener(object : CustomDialogReservationDateSelectBottom.onSelectListener {
                override fun onSelectDateAndTime(data: ReservationScheduleTimeData) {

                    mReservationScheduleTimeData = data

                    displayReservationData()
                }
            })
            customDialogReservationDateSelectBottom.show(parentFragmentManager, "Custom Dialog")
        }

        // Reservation Time Text View
        mReservationTimeTextView = mContainer.fragment_store_reservation_time_text_view

        // Person Spinner
        mPersonSpinner = mContainer.fragment_store_reservation_total_person_spinner
        mPersonSpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mSelectPerson = newIndex
        })

        // Total Person Text View
        mTotalPersonTextView = mContainer.fragment_store_reservation_total_person_text_view

        // Reservation Name Text View
        mReservationNameEditText = mContainer.fragment_store_reservation_name_edit_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getName())) {
            mReservationNameEditText.setText(MAPP.USER_DATA.getName())
        }
        mReservationNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Reservation Phone Number Edit Text
        mReservationPhoneNumberEditText = mContainer.fragment_store_reservation_phone_number_edit_view
        if (!TextUtils.isEmpty(MAPP.USER_DATA.getPhoneNumber())) {
            mReservationPhoneNumberEditText.setText(MAPP.USER_DATA.getPhoneNumber())
        }
        mReservationPhoneNumberEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        // Reservation Memo Edit Text
        mReservationMemoEditText = mContainer.fragment_store_reservation_memo_edit_view
        mReservationMemoEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        mReservationMemoEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id == R.id.fragment_store_reservation_memo_edit_view) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })

        // Reservation Text View
        mReservationTextView = mContainer.fragment_store_reservation_text_view
        mReservationTextView.setOnClickListener {
            checkReservationData()
        }
    }

    private fun displayReservationData() {

        if (!TextUtils.isEmpty(mReservationScheduleTimeData.getCompanyDays())) {
            mReservationDateTextView.text = mReservationScheduleTimeData.getCompanyDays()
        }

        if (!TextUtils.isEmpty(mReservationScheduleTimeData.getBeginTime())) {
            mReservationTimeTextView.text = mReservationScheduleTimeData.getBeginTime()
        }

        if (mReservationScheduleTimeData.getReservationTimeLimitCount() > mReservationScheduleTimeData.getReservationTimeCount()) {
            val selectPersonCountList = ArrayList<String>()
            for (i in 0 until (mReservationScheduleTimeData.getReservationTimeLimitCount() - mReservationScheduleTimeData.getReservationTimeCount())) {
                selectPersonCountList.add((i + 1).toString() + " " + mActivity.resources.getString(R.string.text_people))
            }

            mPersonSpinner.setItems(selectPersonCountList)

            mTotalPersonTextView.text = (mReservationScheduleTimeData.getReservationTimeLimitCount() - mReservationScheduleTimeData.getReservationTimeCount()).toString()
        }
    }

    private fun checkReservationData() {

        if (TextUtils.isEmpty(mReservationDateTextView.text) || TextUtils.isEmpty(mReservationTimeTextView.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_select_reservation_date))
            return
        } else if (mSelectPerson < 0) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_inputreservation_personnel))
            return
        } else if (TextUtils.isEmpty(mReservationNameEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_input_reservation_name))
            return
        } else if (TextUtils.isEmpty(mReservationPhoneNumberEditText.text)) {
            showToast(mActivity, mActivity.resources.getString(R.string.text_hint_input_phone_number_2))
            return
        }

        getReservationSave()
    }

    private fun showReservationSuccessDialog(data: ReservationSuccessData) {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_reservation_complete),
            mActivity.resources.getString(R.string.text_message_reservation_success)
                    + "\n\n" + mActivity.resources.getString(R.string.text_store_name) + " : " + getStoreDetailData().getCompanyName()
                    + "\n" + mActivity.resources.getString(R.string.text_reservation_date) + " : " + mReservationScheduleTimeData.getCompanyDays() + " " + mReservationScheduleTimeData.getBeginTime()
                    + "\n" + mActivity.resources.getString(R.string.text_reservation_name) + " : " + mReservationNameEditText.text.toString(),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()
                mListener.onReservationScreen()
            }
        })
        customDialog.show()
    }

    private fun showStorePointEnoughCustomDialog() {
        val customDialog = CustomDialog(
            mActivity,
            DIALOG_TYPE_NO_CANCEL,
            mActivity.resources.getString(R.string.text_title_insufficient_franchise_points),
            mActivity.resources.getString(R.string.text_message_insufficient_franchise_points),
            "",
            mActivity.resources.getString(R.string.text_confirm)
        )

        customDialog.setOnCustomDialogListener(object : CustomDialog.OnCustomDialogListener {
            override fun onCancel() {
            }

            override fun onConfirm() {
                customDialog.dismiss()
            }
        })
        customDialog.show()
    }

    override fun onPause() {
        super.onPause()

        if (mPersonSpinner.isShowing) {
            mPersonSpinner.dismiss()
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
        if (context is OnStoreReservationFragmentListener) {
            mActivity = context as Activity

            if (context is OnStoreReservationFragmentListener) {
                mListener = context
            } else {
                throw RuntimeException(
                    context.toString()
                            + " must implement OnStoreReservationFragmentListener"
                )
            }
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected fun onAttachToContext(context: Context) {
        if (context is OnStoreReservationFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnStoreReservationFragmentListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    // =================================================================================
    // API
    // =================================================================================

     /**
     * API. Get Reservation Schedule Time
     */
    private fun getReservationSave() {

        val nagajaManager = NagajaManager().getInstance()
        nagajaManager?.getReservationSave(
            mRequestQueue,
            object : NagajaManager.RequestListener<ReservationSuccessData> {
                override fun onSuccess(resultData: ReservationSuccessData) {

                    showReservationSuccessDialog(resultData)
                }

                override fun onFail(errorMsg: String?) {
                    NagajaLog().e("wooks, errorMsg = $errorMsg")
                }

                override fun onFail(errorCode: Int?) {
                    if (errorCode == ERROR_EXPIRED_TOKEN) {
                        disConnectUserToken(requireActivity())
                    } else if (errorCode == ERROR_CODE_NOT_ENOUGH_POINT) {
                        showStorePointEnoughCustomDialog()
                    }
                }
            },
            MAPP.USER_DATA.getSecureKey(),
            1,
            mSelectPerson + 1,
            mReservationNameEditText.text.toString(),
            MAPP.SELECT_LANGUAGE_CODE,
            MAPP.USER_DATA.getPhoneNumber(),
            mReservationMemoEditText.text.toString(),
            "",
            getStoreDetailData().getCompanyUid(),
            mReservationScheduleTimeData.getScheduleTimeUid()
        )
    }
}