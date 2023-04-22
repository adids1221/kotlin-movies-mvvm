package utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import androidx.viewbinding.ViewBinding
import com.example.movies_mvvm.databinding.AddItemLayoutBinding
import com.example.movies_mvvm.databinding.EditItemLayoutBinding
import android.widget.EditText
import com.example.movies_mvvm.R
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

fun validTitle(context: Context, title: String): String? {
    return if (title.isEmpty()) context.getString(R.string.title_helper_text) else null
}

fun validReleaseDate(context: Context, releaseDate: String): String? {
    return if (releaseDate.isEmpty()) context.getString(R.string.release_date_helper_text) else null
}

fun validDescription(context: Context, description: String): String? {
    return if (description.isEmpty()) context.getString(R.string.description_helper_text) else null
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

fun validRating(
    context: Context,
    movieRating: String,
    isEnabled: Boolean,
    releaseDate: String
): String? {
    val rating = movieRating.toDoubleOrNull()
    if (isEnabled) {
        if (rating == null && isFutureDate(releaseDate)) {
            return null
        } else if (rating != null && isFutureDate(releaseDate)) {
            return context.getString(R.string.rating_disabled_helper_text)
        } else if (rating != null && (rating < 0 || rating > 10)) {
            return context.getString(R.string.rating_helper_text)
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

fun validFormFields(binding: ViewBinding, isDateSelected: Boolean, movieData: MovieData): Boolean {
    var result = false
    when (binding) {
        is EditItemLayoutBinding -> {
            val (title, releaseDate, description, ratingText) = movieData
            val context = binding.root.context
            binding.addTitleContainer.helperText = title?.let { validTitle(context, it) }
            binding.addReleaseDateContainer.helperText =
                releaseDate?.let { validReleaseDate(context, it) }
            binding.addDescriptionContainer.helperText =
                description?.let { validDescription(context, it) }
            binding.addRatingContainer.helperText = validRating(
                context,
                ratingText!!,
                binding.addMovieRating.isEnabled,
                releaseDate!!
            )
            val isValidTitle = binding.addTitleContainer.helperText == null
            val isValidDate = binding.addReleaseDateContainer.helperText == null && isDateSelected
            val isValidDescription = binding.addDescriptionContainer.helperText == null
            val isValidRating = binding.addRatingContainer.helperText == null
            result = isValidTitle && isValidDate && isValidDescription && isValidRating
        }
        is AddItemLayoutBinding -> {
            val (title, releaseDate, description, ratingText, imageSource) = movieData
            val context = binding.root.context
            binding.addTitleContainer.helperText = title?.let { validTitle(context, it) }
            binding.addReleaseDateContainer.helperText =
                releaseDate?.let { validReleaseDate(context, it) }
            binding.addDescriptionContainer.helperText =
                description?.let { validDescription(context, it) }
            binding.addRatingContainer.helperText = validRating(
                context,
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
        is EditItemLayoutBinding -> {
            val context = binding.root.context
            binding.addTitleContainer.helperText?.let {
                errorMessage.add("${context.getString(R.string.invalid_form_title)}: $it")
            }

            if (!isDateSelected) {
                errorMessage.add(context.getString(R.string.invalid_form_release_date_required))
            } else {
                binding.addReleaseDateContainer.helperText?.let {
                    errorMessage.add("${context.getString(R.string.invalid_form_release_date)}: $it")
                }
            }

            binding.addDescriptionContainer.helperText?.let {
                errorMessage.add("${context.getString(R.string.invalid_form_description)}: $it")
            }

            binding.addRatingContainer.helperText?.let {
                errorMessage.add("${context.getString(R.string.invalid_form_rating)}: $it")
            }

            if (imageUri == null)
                errorMessage.add(context.getString(R.string.invalid_form_poster))

            val message = errorMessage.joinToString(separator = "\n")

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.invalid_form_alert_title))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.invalid_form_alert_positive_btn)) { _, _ -> }
                .show()
        }
        is AddItemLayoutBinding -> {
            val context = binding.root.context
            binding.addTitleContainer.helperText?.let {
                errorMessage.add("${context.getString(R.string.invalid_form_title)}: $it")
            }

            if (!isDateSelected) {
                errorMessage.add(context.getString(R.string.invalid_form_release_date_required))
            } else {
                binding.addReleaseDateContainer.helperText?.let {
                    errorMessage.add("${context.getString(R.string.invalid_form_release_date)}: $it")
                }
            }

            binding.addDescriptionContainer.helperText?.let {
                errorMessage.add("${context.getString(R.string.invalid_form_description)}: $it")
            }

            binding.addRatingContainer.helperText?.let {
                errorMessage.add("${context.getString(R.string.invalid_form_rating)}: $it")
            }

            if (imageUri == null) {
                errorMessage.add(context.getString(R.string.invalid_form_poster))
            }

            val message = errorMessage.joinToString(separator = "\n")

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.invalid_form_alert_title))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.invalid_form_alert_positive_btn)) { _, _ -> }
                .show()
        }
    }
}


fun EditText.addTitleFocusListener(context: Context, titleContainer: TextInputLayout) {
    this.setOnFocusChangeListener { _, focused ->
        if (!focused) {
            val helperText = validTitle(context, this.text.toString())
            titleContainer.helperText = helperText
        }
    }
}

fun EditText.addReleaseDateFocusListener(
    context: Context,
    datePickerDialog: DatePickerDialog?,
    releaseDateContainer: TextInputLayout
) {
    this.setOnFocusChangeListener { _, focused ->
        if (focused) {
            datePickerDialog?.show()
        } else {
            val helperText = validReleaseDate(context, this.text.toString())
            releaseDateContainer.helperText = helperText
        }
    }
}

fun EditText.addDescriptionFocusListener(context: Context, descriptionContainer: TextInputLayout) {
    this.setOnFocusChangeListener { _, focused ->
        if (!focused) {
            val helperText = validDescription(context, this.text.toString())
            descriptionContainer.helperText = helperText
        }
    }
}

fun EditText.addRatingFocusListener(
    context: Context,
    releaseDate: EditText,
    ratingContainer: TextInputLayout
) {
    this.setOnFocusChangeListener { view, focused ->
        if (!focused) {
            val helperText = validRating(
                context,
                this.text.toString(),
                this.isEnabled,
                releaseDate.text.toString()
            )
            ratingContainer.helperText = helperText
        }
    }
}

