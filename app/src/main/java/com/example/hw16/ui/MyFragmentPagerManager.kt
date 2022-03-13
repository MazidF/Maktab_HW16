package com.example.hw16.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyFragmentPagerManager(manager: FragmentManager, private val fragments: List<Class<out Fragment>>)
    : FragmentPagerAdapter(manager) {

    init {
    }

    override fun getCount() = fragments.size

    override fun getItem(position: Int): Fragment {
        return fragments[position].newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragments[position].simpleName
    }
}
