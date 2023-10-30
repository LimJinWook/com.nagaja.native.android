package com.nagaja.app.android.Dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_KRW
import com.nagaja.app.android.Base.NagajaFragment.Companion.CURRENCY_TYPE_PHP
import com.nagaja.app.android.Data.ExchangeRateData
import com.nagaja.app.android.Utils.NumberTextWatcher
import com.nagaja.app.android.Utils.Util
import com.nagaja.app.android.R

import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import kotlinx.android.synthetic.main.custom_event_dialog.*
import kotlinx.android.synthetic.main.custom_exchange_rate_dialog.*
import java.util.*

class CustomExchangeRateDialog(context: Context, exchangeRateListData: ArrayList<ExchangeRateData>) : Dialog(context) {

    private var mContext: Context = context

    private lateinit var mAmountEditText: EditText

    private lateinit var mChangeTextView: TextView

    private lateinit var mSwapImageView: ImageView

    private lateinit var mAmountCurrencySpinner: PowerSpinnerView
    private lateinit var mChangeCurrencySpinner: PowerSpinnerView

    private var mExchangeRateListData = ArrayList<ExchangeRateData>()
    private var mAmountCurrencyData = ExchangeRateData()
    private var mChangeCurrencyData = ExchangeRateData()

    private var mCurrentAmountPosition = 0
    private var mCurrentChangePosition = 0

    init {
        this.mExchangeRateListData = exchangeRateListData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = window!!.attributes
        val wm = mContext.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lp.width = (wm.defaultDisplay.width * 0.9).toInt()
        window!!.attributes = lp
        window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        setContentView(R.layout.custom_exchange_rate_dialog)

        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {

        // Cancel Image View
        val cancelImageView = custom_exchange_rate_dialog_cancel_image_view
        cancelImageView.setOnClickListener {
            dismiss()
        }

        // Amount Edit Text
        mAmountEditText = custom_exchange_rate_dialog_amount_edit_text
        mAmountEditText.filters = arrayOf(Util().blankSpaceFilter)
        mAmountEditText.addTextChangedListener(NumberTextWatcher(mAmountEditText))
        mAmountEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mAmountCurrencySpinner.isShowing) {
                    mAmountCurrencySpinner.dismiss()
                }

                if (mChangeCurrencySpinner.isShowing) {
                    mChangeCurrencySpinner.dismiss()
                }

                if (TextUtils.isEmpty(mAmountEditText.text)) {
                    mChangeTextView.text = "0"
                } else {
                    val amount = mAmountEditText.text.toString().replace(",", "")
                    mChangeTextView.text = Util().getTwoDecimalPlaces((mChangeCurrencyData.getCurrencyNow() / mAmountCurrencyData.getCurrencyNow() * amount.toDouble()))
                }
            }
        })

        // Amount Currency Spinner
        mAmountCurrencySpinner = custom_exchange_rate_dialog_amount_currency_spinner
        mAmountCurrencySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mAmountCurrencyData = mExchangeRateListData[newIndex]

            mCurrentAmountPosition = newIndex

            mAmountEditText.setText("")
            mChangeTextView.text = "0"
        })

        // Change Currency Spinner
        mChangeCurrencySpinner = custom_exchange_rate_dialog_change_currency_spinner
        mChangeCurrencySpinner.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            mChangeCurrencyData = mExchangeRateListData[newIndex]

            mCurrentChangePosition = newIndex

            mAmountEditText.setText("")
            mChangeTextView.text = "0"
        })

        // Change Text View
        mChangeTextView = custom_exchange_rate_dialog_change_text_view

        // Swap Image View
        mSwapImageView = custom_exchange_rate_dialog_swap_image_view
        mSwapImageView.setOnClickListener {
            val amountPosition = mCurrentChangePosition
            val changePosition = mCurrentAmountPosition

            mAmountCurrencySpinner.selectItemByIndex(amountPosition)
            mChangeCurrencySpinner.selectItemByIndex(changePosition)
        }

        setCurrency()
    }

    private fun setCurrency() {
        val currencyList = ArrayList<String>()
        for (i in mExchangeRateListData.indices) {
            if (!TextUtils.isEmpty(mExchangeRateListData[i].getCurrencyCode())) {
                currencyList.add(mExchangeRateListData[i].getCurrencyCode())
            }
        }

        if (currencyList.size > 0) {
            mAmountCurrencySpinner.setItems(currencyList)
            for (i in currencyList.indices) {
                if (currencyList[i] == CURRENCY_TYPE_KRW) {
                    mAmountCurrencySpinner.selectItemByIndex(i)
                    mAmountCurrencyData = mExchangeRateListData[i]
                    break
                }
            }

            mChangeCurrencySpinner.setItems(currencyList)
            for (i in currencyList.indices) {
                if (currencyList[i] == CURRENCY_TYPE_PHP) {
                    mChangeCurrencySpinner.selectItemByIndex(i)
                    mChangeCurrencyData = mExchangeRateListData[i]
                    break
                }
            }
        }
    }

}