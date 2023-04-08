package com.example.movies_mvvm.ui.edit_movie

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
import com.bumptech.glide.Glide
import com.example.movies_mvvm.R
import com.example.movies_mvvm.data.model.Item
import com.example.movies_mvvm.databinding.EditItemLayoutBinding
import com.example.movies_mvvm.ui.ItemsViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditItemFragment : Fragment() {

    private var _binding: EditItemLayoutBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private var initialImageUri: String? = null
    private var isDateSelected = false

    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var datePickerDialog: DatePickerDialog

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
    ): View {
        _binding = EditItemLayoutBinding.inflate(
            inflater, container, false
        )
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.FRENCH)
            binding.addMovieReleaseDate.setText(sdf.format(calendar.time))
            isDateSelected = true
            if (!isFutureDate(binding.addMovieReleaseDate.text.toString())) {
                binding.addMovieRating.isEnabled = true
                binding.addRatingContainer.helperText = "Rating isn't required, default value is 0"
            } else {
                binding.addMovieRating.setText("")
                binding.addMovieRating.isEnabled = false
                binding.addRatingContainer.helperText =
                    "Rating is disabled, the movie isn't release yet!"
            }
        }

        val defaultDate = Calendar.getInstance()

        datePickerDialog = container?.let {
            DatePickerDialog(
                it.context,
                listener,
                defaultDate.get(Calendar.YEAR),
                defaultDate.get(Calendar.MONTH),
                defaultDate.get(Calendar.DAY_OF_MONTH)
            )
        }!!

        titleFocusListener()
        descriptionFocusListener()
        ratingFocusListener()
        releaseDateFocusListener(datePickerDialog)

        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.updateItemBtn.setOnClickListener {
            submitForm(isDateSelected)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenItem.observe(viewLifecycleOwner) {
            val ratingText = String.format("%.1f", it.rating)
            binding.addMovieTitle.setText(it.title)
            binding.addMovieReleaseDate.setText(it.releaseDate)
            binding.addMovieDescription.setText(it.description)
            binding.addMovieRating.setText(if (ratingText == (0.0).toString()) null else ratingText)
            Glide
                .with(requireContext())
                .load(it.poster)
                .centerCrop()
                .into(binding.resultImage)

            val parts = it.releaseDate.split("/")
            datePickerDialog.updateDate(
                parts[2].toInt(),
                parts[1].toInt() - 1,
                parts[0].toInt()
            )

            initialImageUri = it.poster
            isDateSelected = true
            binding.addMovieRating.isEnabled = true
        }

    }


    private fun submitForm(isDateSelected: Boolean) {

        binding.addTitleContainer.helperText = validTitle()
        binding.addReleaseDateContainer.helperText = validReleaseDate()
        binding.addDescriptionContainer.helperText = validDescription()
        binding.addRatingContainer.helperText = validRating()

        val isValidTitle = binding.addTitleContainer.helperText == null
        val isValidDate = binding.addReleaseDateContainer.helperText == null && isDateSelected
        val isValidDescription = binding.addDescriptionContainer.helperText == null
        val isValidRating = binding.addRatingContainer.helperText == null
        val isValidPoster = validImage() == null


        if (isValidTitle && isValidDate && isValidDescription && isValidRating && isValidPoster) {
            val title = binding.addMovieTitle.text.toString()
            val description = binding.addMovieDescription.text.toString()
            val releaseDate = binding.addMovieReleaseDate.text.toString()
            val ratingText = binding.addMovieRating.text.toString()
            val rating = if (ratingText.isNotBlank()) ratingText.toDouble() else 0.0
            val poster = if (imageUri == null) initialImageUri else imageUri.toString()

            val updatedMovie = Item(title, description, releaseDate, rating, poster)

            viewModel.updateItem(updatedMovie)

            findNavController().navigate(R.id.action_editItemFragment_to_allItemsFragment)
        } else
            invalidForm(isDateSelected)
    }

    private fun invalidForm(isDateSelected: Boolean) {
        var message = ""
        if (binding.addTitleContainer.helperText != null)
            message += "\n\nTitle: " + binding.addTitleContainer.helperText
        if (!isDateSelected) {
            message += "\n\nRelease Date Is Required!"
        } else if (binding.addReleaseDateContainer.helperText != null) {
            message += "\n\nRelease Date: " + binding.addReleaseDateContainer.helperText
        }
        if (binding.addDescriptionContainer.helperText != null)
            message += "\n\nDescription: " + binding.addDescriptionContainer.helperText
        if (binding.addRatingContainer.helperText != null)
            message += "\n\nRating: " + binding.addRatingContainer.helperText
        if (imageUri == null)
            message += "\n\nPoster: Missing Movie Poster"

        AlertDialog.Builder(binding.root.context).setTitle("Invalid From").setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }.show()
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

    private fun releaseDateFocusListener(datePickerDialog: DatePickerDialog?) {
        binding.addMovieReleaseDate.setOnFocusChangeListener { _, focused ->
            if (focused) {
                datePickerDialog?.show()
            } else {
                binding.addReleaseDateContainer.helperText = validReleaseDate()
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

    private fun validReleaseDate(): String? {
        val releaseDate = binding.addMovieReleaseDate.text.toString()
        return if (releaseDate.isEmpty()) "Invalid Movie Release Date" else null
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
        if (binding.addMovieRating.isEnabled) {
            if (noNeedRating)
                return null
            else if (ratingForFuture)
                return "Movie that isn't released yet can't have rating!"
            else if (invalidRating)
                return "Invalid Movie Rating, rating is between 0 to 10"
        }
        return null
    }

    private fun validImage(): Any? {
        return if (binding.resultImage.drawable != null && (imageUri != null || initialImageUri != null)) null else "Missing Movie Poster"
    }

    private fun isFutureDate(dateStr: String): Boolean {
        if (dateStr.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
            val date = dateFormat.parse(dateStr)
            val currentTimeMillis = System.currentTimeMillis()
            val currentDate = Date(currentTimeMillis)
            return date!!.after(currentDate)
        }
        return false
    }


}