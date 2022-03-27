package com.example.hw16.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hw16.databinding.FragmentCalendarBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.ui.App


class FragmentCalendar : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    val model: ViewModelCalendar by viewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with(binding) {
            calendarView.apply {
                setOnScrollChangeListener { _, _, _, _, _ ->
                    this.date
                }
            }
            calList.apply {

            }
        }
    }
}
