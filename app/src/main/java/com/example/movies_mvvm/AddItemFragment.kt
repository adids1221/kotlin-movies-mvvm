package com.example.movies_mvvm

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movies_mvvm.databinding.AddItemLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class AddItemFragment : Fragment() {

    private var _binding: AddItemLayoutBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.resultImage.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(
            inflater, container, false
        )

        titleFocusListener()
        descriptionFocusListener()
        ratingFocusListener()

        var isDateSelected = false
        val calendar = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { p0, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.FRENCH)
            binding.addMovieReleaseDate.text = sdf.format(calendar.time)
            isDateSelected = true
            if (!isFutureDate(binding.addMovieReleaseDate.text.toString()))
                binding.addMovieRating.isEnabled = true
        }

        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = container?.let {
            DatePickerDialog(
                it.context,
                listener,
                cYear,
                cMonth,
                cDay
            )
        }

        binding.addMovieReleaseDate.setOnClickListener {
            datePickerDialog?.show()
        }


        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.addItemBtn.setOnClickListener {
            submitForm(isDateSelected)
        }
        return binding.root
    }

    private fun submitForm(isDateSelected: Boolean) {

        binding.addTitleContainer.helperText = validTitle()
        binding.addDescriptionContainer.helperText = validDescription()
        binding.addRatingContainer.helperText = validRating()

        val isValidTitle = binding.addTitleContainer.helperText == null
        val isValidDescription = binding.addDescriptionContainer.helperText == null
        val isValidRating = binding.addRatingContainer.helperText == null

        if (isDateSelected && isValidTitle && isValidDescription && isValidRating) {
            val title = binding.addMovieTitle.text.toString()
            val description = binding.addMovieDescription.text.toString()
            val releaseDate = binding.addMovieReleaseDate.text.toString()
            val ratingText = binding.addMovieRating.text.toString()
            val rating = if (ratingText.isNotBlank()) ratingText.toDouble() else 0.0

            val newMovie = Item(title, description, releaseDate, rating, imageUri.toString())

            MovieItemManager.add(newMovie)
            val bundle = bundleOf(
                "title" to title,
                "description" to description,
                "releaseDate" to releaseDate,
                "rating" to rating, "moviePoster" to imageUri.toString()
            )
            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment, bundle)
        } else
            invalidForm(isDateSelected)
    }

    private fun invalidForm(isDateSelected: Boolean) {
        var message = ""
        println("binding.addTitleContainer.helperText: ${binding.addTitleContainer.helperText}")
        println("isDateSelected: $isDateSelected")
        if (binding.addTitleContainer.helperText != null)
            message += "\n\nTitle: " + binding.addTitleContainer.helperText
        if (binding.addDescriptionContainer.helperText != null)
            message += "\n\nDescription: " + binding.addDescriptionContainer.helperText
        if (binding.addRatingContainer.helperText != null)
            message += "\n\nRating: " + binding.addRatingContainer.helperText
        if (!isDateSelected)
            message += "\n\nSelect Movie Release Date"

        AlertDialog.Builder(binding.root.context).setTitle("Invalid From").setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun titleFocusListener() {
        binding.addMovieTitle.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.addTitleContainer.helperText = validTitle()
            }
        }
    }

    private fun descriptionFocusListener() {
        binding.addMovieDescription.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.addDescriptionContainer.helperText = validDescription()
            }
        }
    }

    private fun ratingFocusListener() {
        binding.addMovieRating.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.addRatingContainer.helperText = validRating()
            }
        }
    }

    private fun validTitle(): String? {
        val title = binding.addMovieTitle.text.toString()
        return if (title.isEmpty()) "Invalid Movie Title" else null
    }

    private fun validDescription(): String? {
        val description = binding.addMovieDescription.text.toString()
        return if (description.isEmpty()) "Invalid Movie Description" else null
    }

    private fun validRating(): String? {
        val rating = binding.addMovieRating.text.toString().toDoubleOrNull()
        val releaseDate = binding.addMovieReleaseDate.text.toString()
        val noNeedRating = rating == null && isFutureDate(releaseDate)
        val ratingForFuture = rating != null && isFutureDate(releaseDate)
        val invalidRating = rating != null && (rating < 0 || rating > 10)
        println("rating: $rating, releaseDate: $releaseDate || noNeedRating? $noNeedRating || ratingForFuture: $ratingForFuture || invalidRating: $invalidRating")
        if (binding.addMovieRating.isEnabled) {
            if (noNeedRating)
                return null
            else if (ratingForFuture)
                return "Movie that isn't released yet can't have rating!"
            else if (invalidRating)
                return "Invalid Movie Rating"
        }
        return null
    }

    private fun isFutureDate(dateStr: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
        val date = dateFormat.parse(dateStr)
        val currentTimeMillis = System.currentTimeMillis()
        val currentDate = Date(currentTimeMillis)
        return date!!.after(currentDate)

    }


}