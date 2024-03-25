package com.fiknaufalh.githubfinder.helpers

import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    fun formatDate(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val date = inputFormat.parse(dateStr)
        val formattedDate = outputFormat.format(date!!)
        return formatDayOfMonth(formattedDate)
    }

    private fun formatDayOfMonth(dateStr: String): String {
        val dayOfMonth = dateStr.substring(0, 2)
        val dayInt = Integer.parseInt(dayOfMonth)
        return "$dayInt${getDayOfMonthSuffix(dayInt)}${dateStr.substring(2)}"
    }

    private fun getDayOfMonthSuffix(n: Int): String {
        return when (n) {
            in 11..13 -> "th"
            else -> when (n % 10) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
        }
    }
}