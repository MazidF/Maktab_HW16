package com.example.hw16.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentPagerManager(
    fragment: Fragment,
    private val fragments: List<Class<out Fragment>>,
    private val args: List<Bundle>,
) : FragmentStateAdapter(fragment) {
    private val manager = fragment.childFragmentManager

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position].newInstance().apply {
            arguments = args[position]
        }
    }
}
