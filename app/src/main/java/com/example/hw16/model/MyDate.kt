package com.example.hw16.model

import java.util.*

data class MyDate(val year: Int, val month: Int, val day: Int, val hour: Int, val minute: Int) {
    companion object {
        fun build(date: Date) {

        }
    }
}