package com.nagaja.app.android.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nagaja.app.android.Main.*

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val list: ArrayList<*>
    override fun getItem(position: Int): Fragment {
        return list[position] as Fragment
    }

    override fun getCount(): Int {
        return list.size
    }

    init {
        list = ArrayList<Any?>()
        list.add(HomeFragment())
//        list.add(ReservationFragment())
        list.add(MainBoardFragment())
        list.add(LocationFragment())
        list.add(ChatFragment())
        list.add(MyPageFragment())
    }
}
