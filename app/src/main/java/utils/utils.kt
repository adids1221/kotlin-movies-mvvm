package utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.viewbinding.ViewBinding
import com.example.movies_mvvm.databinding.AddItemLayoutBinding
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

data class MovieData(
    val title: String?,
    val releaseDate: String?,
    val description: String?,
    val ratingText: String?,
    val imageSource: String?
)

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
                        if (isFuture || text.toString().isEmpty()) setText("0")
                    }
                    binding.addRatingContainer.helperText = ratingContainerText[isFuture]
                }
            }
        }
    }
}

fun validFormFields(binding: ViewBinding, isDateSelected: Boolean, movieData: MovieData): Boolean {
    var result = false
    when (binding) {
        is AddItemLayoutBinding -> {
            val (title, releaseDate, description, ratingText, imageSource) = movieData
            binding.addTitleContainer.helperText = title?.let { validTitle(it) }
            binding.addReleaseDateContainer.helperText = releaseDate?.let { validReleaseDate(it) }
            binding.addDescriptionContainer.helperText = description?.let { validDescription(it) }
            binding.addRatingContainer.helperText = validRating(
                ratingText!!,
                binding.addMovieRating.isEnabled,
                releaseDate!!
            )

            val isValidTitle = binding.addTitleContainer.helperText == null
            val isValidDate = binding.addReleaseDateContainer.helperText == null && isDateSelected
            val isValidDescription = binding.addDescriptionContainer.helperText == null
            val isValidRating = binding.addRatingContainer.helperText == null
            val isValidPoster = imageSource == null
            result =
                isValidTitle && isValidDate && isValidDescription && isValidRating && isValidPoster
        }
    }
    return result
}

fun invalidForm(isDateSelected: Boolean, imageUri: String?, binding: ViewBinding) {
    val errorMessage = mutableListOf<String>()
    when (binding) {
        is AddItemLayoutBinding -> {
            binding.addTitleContainer.helperText?.let {
                errorMessage.add("Title: $it")
            }

            if (!isDateSelected) {
                errorMessage.add("Release Date Is Required!")
            } else {
                binding.addReleaseDateContainer.helperText?.let {
                    errorMessage.add("Release Date: $it")
                }
            }

            binding.addDescriptionContainer.helperText?.let {
                errorMessage.add("Description: $it")
            }

            binding.addRatingContainer.helperText?.let {
                errorMessage.add("Rating: $it")
            }

            if (imageUri == null) {
                errorMessage.add("Poster: Missing Movie Poster")
            }

            val message = errorMessage.joinToString(separator = "\n")

            AlertDialog.Builder(binding.root.context)
                .setTitle("Invalid Form")
                .setMessage(message)
                .setPositiveButton("Okay") { _, _ -> }
                .show()
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

