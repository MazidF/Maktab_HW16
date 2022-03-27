package com.example.hw16.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class MyFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
//        if (loadFragmentClass(classLoader, className) == FragmentCalendar::class.java) {
//            return FragmentCalendar(Calendar.getInstance())
//        }
        return super.instantiate(classLoader, className)
    }
}
