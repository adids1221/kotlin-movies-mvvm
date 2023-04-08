package utils

import java.text.SimpleDateFormat
import java.util.*

fun getRating(rating: Double): Float {
    val maxRating = 5.0
    val stepSize = 0.5
    val numOfStars = 5

    val numStars = (rating / (maxRating / numOfStars)).toFloat()

    return (numStars * stepSize).toFloat()
}

fun validTitle(title: String): String? {
    return if (title.isEmpty()) "Invalid Movie Title" else null
}

fun validReleaseDate(releaseDate: String): String? {
    return if (releaseDate.isEmpty()) "Invalid Movie Release Date" else null
}

fun validDescription(description: String): String? {
    return if (description.isEmpty()) "Invalid Movie Description" else null
}

fun isFutureDate(dateStr: String): Boolean {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
    val currentDate = Date()
    return try {
        val date = dateFormat.parse(dateStr)
        date?.after(currentDate) ?: false
    } catch (e: Exception) {
        false
    }
}

fun validRating(movieRating: String, isEnabled: Boolean, releaseDate: String): String? {
    val rating = movieRating.toDoubleOrNull()
    if (isEnabled) {
        if (rating == null && isFutureDate(releaseDate)) {
            return null
        } else if (rating != null && isFutureDate(releaseDate)) {
            return "Movie that isn't released yet can't have rating!"
        } else if (rating != null && (rating < 0 || rating > 10)) {
            return "Invalid Movie Rating, rating is between 0 to 10"
        }
    }
    return null
}
