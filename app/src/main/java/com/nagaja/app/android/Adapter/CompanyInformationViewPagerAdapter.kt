package com.nagaja.app.android.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nagaja.app.android.CompanyDefaultInformation.CompanyDefaultInformationFragment
import com.nagaja.app.android.CompanyDefaultInformation.CompanyProductInformationFragment
import com.nagaja.app.android.CompanyDefaultInformation.CompanySalesInformationFragment
import com.nagaja.app.android.Main.*

class CompanyInformationViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val list: ArrayList<*>
    override fun getItem(position: Int): Fragment {
        return list[position] as Fragment
    }

    override fun getCount(): Int {
        return list.size
    }

    init {
        list = ArrayList<Any?>()
        list.add(CompanyDefaultInformationFragment())
        list.add(CompanySalesInformationFragment())
        list.add(CompanyProductInformationFragment())
    }
}
