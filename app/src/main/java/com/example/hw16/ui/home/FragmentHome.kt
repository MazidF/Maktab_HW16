package com.example.hw16.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hw16.databinding.FragmentHomeBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.TaskState.*
import com.example.hw16.ui.App
import com.example.hw16.ui.ViewModelMain
import com.example.hw16.utils.logger
import com.google.android.material.tabs.TabLayoutMediator

class FragmentHome : Fragment() {
    private val modelMain: ViewModelMain by activityViewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    private val model: ViewModelHome by activityViewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger("onCreate FragmentHome")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger("onViewCreate FragmentHome")
        init()
    }

    private fun init() {
        modelMain.user.observe(viewLifecycleOwner) {
            if (it == null) return@observe
        }
        with(binding) {
            initViewPager()
        }
    }

    private fun FragmentHomeBinding.initViewPager() {
        val labels = listOf("All", DONE.name, DOING.name, TODO.name)
        homeViewPager.adapter = MyFragmentPagerManager(
            this@FragmentHome,
            List(4) { FragmentHomeSub::class.java },
            labels.map { bundleOf("state" to it) },
        )
        TabLayoutMediator(homeTab, homeViewPager) { tab, position ->
            tab.text = labels[position]
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        logger("onDestroy FragmentHome")
    }
}