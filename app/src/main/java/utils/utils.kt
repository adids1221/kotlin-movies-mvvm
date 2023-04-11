package utils

import android.app.DatePickerDialog
import androidx.viewbinding.ViewBinding
import com.example.movies_mvvm.databinding.AddItemLayoutBinding
import com.example.movies_mvvm.databinding.EditItemLayoutBinding
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
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

fun getDateSetListener(
    binding: ViewBinding,
    calendar: Calendar
): DatePickerDialog.OnDateSetListener {
    return DatePickerDialog.OnDateSetListener { _, _, _, _ ->
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.FRENCH)
        val ratingContainerText = mapOf(
            true to "Rating is disabled, the movie isn't release yet!",
            false to "Rating isn't required, default value is 0"
        )
        when (binding) {
            is AddItemLayoutBinding -> {
                binding.addMovieReleaseDate.apply {
                    setText(sdf.format(calendar.time))
                    clearFocus()
                    val isFuture = isFutureDate(text.toString())
                    binding.addMovieRating.apply {
                        isEnabled = !isFuture
                        setText("0")
                    }
                    binding.addRatingContainer.helperText = ratingContainerText[isFuture]
                }
            }
            is EditItemLayoutBinding -> {
                binding.addMovieReleaseDate.apply {
                    setText(sdf.format(calendar.time))
                    clearFocus()
                    val isFuture = isFutureDate(text.toString())
                    binding.addMovieRating.apply {
                        isEnabled = !isFuture
                        if (isFuture || text.toString().isEmpty()) setText("0")
                    }
                    binding.addRatingContainer.helperText = ratingContainerText[isFuture]
                }
            }
        }
    }
}



fun EditText.addTitleFocusListener(titleContainer: TextInputLayout) {
    this.setOnFocusChangeListener { _, focused ->
        if (!focused) {
            val helperText = validTitle(this.text.toString())
            titleContainer.helperText = helperText
        }
    }
}

fun EditText.addReleaseDateFocusListener(
    datePickerDialog: DatePickerDialog?,
    releaseDateContainer: TextInputLayout
) {
    this.setOnFocusChangeListener { _, focused ->
        if (focused) {
            datePickerDialog?.show()
        } else {
            val helperText = validReleaseDate(this.text.toString())
            releaseDateContainer.helperText = helperText
        }
    }
}

fun EditText.addDescriptionFocusListener(descriptionContainer: TextInputLayout) {
    this.setOnFocusChangeListener { _, focused ->
        if (!focused) {
            val helperText = validDescription(this.text.toString())
            descriptionContainer.helperText = helperText
        }
    }
}

fun EditText.addRatingFocusListener(releaseDate: EditText, ratingContainer: TextInputLayout) {
    this.setOnFocusChangeListener { view, focused ->
        if (!focused) {
            val helperText = validRating(
                this.text.toString(),
                this.isEnabled,
                releaseDate.text.toString()
            )
            ratingContainer.helperText = helperText
        }
    }
}
