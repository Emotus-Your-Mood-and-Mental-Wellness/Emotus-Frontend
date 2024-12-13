package com.emotus.app.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Date {

    fun formatDatesTime(input: String): List<String> {
        val formatter = DateTimeFormatter.ISO_DATE_TIME

        val dateTime = ZonedDateTime.parse(input, formatter)

        val localDateTime = dateTime.withZoneSameInstant(java.time.ZoneId.systemDefault())

        // Format hari
        val dayFormat = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
        // Format tanggal
        val dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        // Format waktu
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

        return listOf(
            dayFormat.format(localDateTime),  // Hari
            dateFormat.format(localDateTime), // Tanggal
            timeFormat.format(localDateTime)  // Jam
        )
    }
}