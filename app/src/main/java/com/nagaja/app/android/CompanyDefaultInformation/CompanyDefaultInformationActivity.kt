package com.nagaja.app.android.CompanyDefaultInformation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nagaja.app.android.Adapter.CompanyInformationViewPagerAdapter
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.Data.CompanyDefaultData
import com.nagaja.app.android.Utils.MainViewPager
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil

import kotlinx.android.synthetic.main.activity_company_default_information.*


class CompanyDefaultInformationActivity : NagajaActivity() {

    private lateinit var mDefaultInformationView: View
    private lateinit var mDefaultInformationLineView: View
    private lateinit var mSalesInformationView: View
    private lateinit var mSalesInformationLineView: View
    private lateinit var mProductInformationView: View
    private lateinit var mProductInformationLineView: View

    private lateinit var mDefaultInformationTextView: TextView
    private lateinit var mSalesInformationTextView: TextView
    private lateinit var mProductInformationTextView: TextView
    private lateinit var mBottomButtonTextView: TextView

    private var mCompanyDefaultData = CompanyDefaultData()

    private var mSelectManagementType = 0

    private lateinit var mViewPager: MainViewPager
    private lateinit var mViewPagerAdapter: CompanyInformationViewPagerAdapter

    private lateinit var mRequestQueue: RequestQueue

    companion object {
        const val SHOW_TYPE_DEFAULT_INFORMATION                = 0x00
        const val SHOW_TYPE_SALES_INFORMATION                  = 0x01
        const val SHOW_TYPE_PRODUCT_INFORMATION                = 0x02
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_default_information)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@CompanyDefaultInformationActivity)!!, false)

        mCompanyDefaultData = intent.getSerializableExtra(INTENT_DATA_COMPANY_DEFAULT_DATA) as CompanyDefaultData
        mSelectManagementType = intent.getIntExtra(INTENT_DATA_SELECT_COMPANY_MANAGEMENT_TYPE_DATA, 0)

        mRequestQueue = Volley.newRequestQueue(this@CompanyDefaultInformationActivity)

        mViewPagerAdapter = CompanyInformationViewPagerAdapter(supportFragmentManager)

        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {

        // Default Information View
        mDefaultInformationView = activity_company_default_information_view
        mDefaultInformationView.setOnClickListener {
            showView(SHOW_TYPE_DEFAULT_INFORMATION)
        }

        // Default Information Text View
        mDefaultInformationTextView = activity_company_default_information_text_view

        // Default Information Line View
        mDefaultInformationLineView = activity_company_default_information_line_view




        // Sales Information View
        mSalesInformationView = activity_company_default_information_sales_information_view
        mSalesInformationView.setOnClickListener {
            showView(SHOW_TYPE_SALES_INFORMATION)
        }

        // Sales Information Text View
        mSalesInformationTextView = activity_company_default_information_sales_information_text_view

        // Sales Information Line View
        mSalesInformationLineView = activity_company_default_information_sales_information_line_view




        // Product Information View
        mProductInformationView = activity_company_default_information_product_information_view
        mProductInformationView.setOnClickListener {
            showView(SHOW_TYPE_PRODUCT_INFORMATION)
        }

        // Product Information Text View
        mProductInformationTextView = activity_company_default_information_product_information_text_view

        // Product Information Line View
        mProductInformationLineView = activity_company_default_information_product_information_line_view




        // Bottom Text View
        mBottomButtonTextView = activity_company_default_information_bottom_button_text_view


        // Custom View Pager
        mViewPager = activity_company_default_information_custom_view_pager
        mViewPager.adapter = mViewPagerAdapter
        mViewPager.setPagingEnabled(false)
        mViewPager.offscreenPageLimit = 3
        mViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    showView(SHOW_TYPE_DEFAULT_INFORMATION)
                } else if (position == 1) {
                    showView(SHOW_TYPE_SALES_INFORMATION)
                } else if (position == 2) {
                    showView(SHOW_TYPE_PRODUCT_INFORMATION)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        showView(SHOW_TYPE_DEFAULT_INFORMATION)
    }

    fun showView(menuType: Int) {

        when (menuType) {
            SHOW_TYPE_DEFAULT_INFORMATION -> {
                mDefaultInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_0d4d97))
                mDefaultInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_0d4d97))

                mSalesInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.text_color_bbbbbb))
                mSalesInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_e2e7ee))

                mProductInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.text_color_bbbbbb))
                mProductInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_e2e7ee))

                mBottomButtonTextView.visibility = View.VISIBLE
                mBottomButtonTextView.text = this@CompanyDefaultInformationActivity.resources.getString(R.string.text_company_default_change_information)

            }

            SHOW_TYPE_SALES_INFORMATION -> {
                mDefaultInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.text_color_bbbbbb))
                mDefaultInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_e2e7ee))

                mSalesInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_0d4d97))
                mSalesInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_0d4d97))

                mProductInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.text_color_bbbbbb))
                mProductInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_e2e7ee))

                mBottomButtonTextView.visibility = View.GONE

            }

            SHOW_TYPE_PRODUCT_INFORMATION -> {
                mDefaultInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.text_color_bbbbbb))
                mDefaultInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_e2e7ee))

                mSalesInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.text_color_bbbbbb))
                mSalesInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_e2e7ee))

                mProductInformationTextView.setTextColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_0d4d97))
                mProductInformationLineView.setBackgroundColor(ContextCompat.getColor(this@CompanyDefaultInformationActivity, R.color.bg_color_0d4d97))

                mBottomButtonTextView.visibility = View.VISIBLE
                mBottomButtonTextView.text = this@CompanyDefaultInformationActivity.resources.getString(R.string.text_register_product)

            }
        }

        mViewPager.setCurrentItem(menuType, false)
    }

}
