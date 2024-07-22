package com.copart.rtlaisdk.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateUtils {
    companion object {
        private val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
        private val friendlyDateTimeWithZoneFormatter =
            DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z")

        fun isoToFriendlyDateTimeWithZone(isoDate: String): String {
            val instant = Instant.parse(isoDate)
            val dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
            return dateTime.format(friendlyDateTimeWithZoneFormatter)
        }

        fun isoToRelativeDate(isoDate: String): String {
            val date = LocalDateTime.parse(isoDate, isoFormatter)
            val now = LocalDateTime.now()
            val days = ChronoUnit.DAYS.between(date.toLocalDate(), now.toLocalDate())

            return when {
                days == 0L -> "Today"
                days == 1L -> "Yesterday"
                days in 2..6 -> "$days days ago"
                days in 7..13 -> "1 week ago"
                days in 14..29 -> "${days / 7} weeks ago"
                days in 30..364 -> "${days / 30} months ago"
                else -> "More than a year ago"
            }
        }
    }
}