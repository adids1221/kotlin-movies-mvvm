package com.example.movies_mvvm.ui.add_movie

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.movies_mvvm.R
import com.example.movies_mvvm.data.model.Item
import com.example.movies_mvvm.databinding.AddItemLayoutBinding
import com.example.movies_mvvm.ui.ItemsViewModel
import utils.*
import java.text.SimpleDateFormat
import java.util.*

class AddItemFragment : Fragment() {

    private var _binding: AddItemLayoutBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private val viewModel: ItemsViewModel by activityViewModels()

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

        var isDateSelected = false
        val calendar = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.FRENCH)
            binding.addMovieReleaseDate.apply {
                setText(sdf.format(calendar.time))
                isDateSelected = true
                val isFuture = isFutureDate(text.toString())
                binding.addMovieRating.apply {
                    isEnabled = !isFuture
                    setText(if (isFuture) "" else "0")
                }
                binding.addRatingContainer.helperText = when {
                    isFuture -> "Rating is disabled, the movie isn't release yet!"
                    else -> "Rating isn't required, default value is 0"
                }
            }
        }

        val datePickerDialog = container?.let {
            DatePickerDialog(
                it.context,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        binding.addMovieTitle.addTitleFocusListener(binding.addTitleContainer)
        binding.addMovieReleaseDate.addReleaseDateFocusListener(
            datePickerDialog,
            binding.addReleaseDateContainer
        )
        binding.addMovieDescription.addDescriptionFocusListener(binding.addDescriptionContainer)
        binding.addMovieRating.addRatingFocusListener(
            binding.addMovieReleaseDate,
            binding.addRatingContainer
        )

        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.addItemBtn.setOnClickListener {
            submitForm(isDateSelected)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)
        }
        return binding.root
    }


    private fun submitForm(isDateSelected: Boolean) {
        val title = binding.addMovieTitle.text?.toString()
        val releaseDate = binding.addMovieReleaseDate.text?.toString()
        val description = binding.addMovieDescription.text?.toString()
        val ratingText = binding.addMovieRating.text?.toString()
        val rating = when {
            ratingText?.isNotBlank() == true -> ratingText.toDouble()
            else -> 0.0
        }
        val poster = if (imageUri == null) "Missing Movie Poster" else null

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
        val isValidPoster = poster == null

        if (isValidTitle && isValidDate && isValidDescription && isValidRating && isValidPoster) {
            val newMovie = Item(title!!, description!!, releaseDate, rating, imageUri.toString())
            viewModel.addItem(newMovie)
            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)
        } else {
            invalidForm(isDateSelected)
        }
    }

    private fun invalidForm(isDateSelected: Boolean) {
        val errorMessage = mutableListOf<String>()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}