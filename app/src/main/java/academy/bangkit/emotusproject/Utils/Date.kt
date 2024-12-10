package academy.bangkit.emotusproject.Utils

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Date {

    fun formatDatesTime(input: String): Pair<String, String> {
        val zonedDateTimeUTC = ZonedDateTime.parse(input)
        val zonedDateTimeWIB = zonedDateTimeUTC.withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        val formattedDate = zonedDateTimeWIB.format(dateFormatter)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = zonedDateTimeWIB.format(timeFormatter)

        return Pair(formattedDate, formattedTime)
    }
}