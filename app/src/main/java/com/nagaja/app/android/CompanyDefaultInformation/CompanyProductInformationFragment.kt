package com.nagaja.app.android.CompanyDefaultInformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nagaja.app.android.Adapter.*
import com.nagaja.app.android.Base.NagajaFragment
import com.nagaja.app.android.R

import kotlinx.android.synthetic.main.fragment_board.view.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.layout_title.view.*

class CompanyProductInformationFragment : NagajaFragment() {

    private lateinit var mContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        mContainer = inflater.inflate(R.layout.fragment_company_product_information, container, false)

        init()

        return mContainer
    }

    private fun init() {

    }


}