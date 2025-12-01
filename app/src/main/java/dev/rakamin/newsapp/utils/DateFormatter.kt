package dev.rakamin.newsapp.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateFormatter {

    private const val INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val OUTPUT_FORMAT = "dd MMM yyyy, HH:mm"

    fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "-"

        return try {
            val inputFormatter = SimpleDateFormat(INPUT_FORMAT, Locale.getDefault())
            inputFormatter.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormatter = SimpleDateFormat(OUTPUT_FORMAT, Locale("id", "ID"))
            outputFormatter.timeZone = TimeZone.getDefault()

            val date = inputFormatter.parse(dateString)
            date?.let { outputFormatter.format(it) } ?: "-"
        } catch (e: Exception) {
            "-"
        }
    }
}
