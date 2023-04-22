package com.example.movies_mvvm.ui.add_movie

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
import java.util.*

class AddItemFragment : Fragment() {

    private var _binding: AddItemLayoutBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
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
    ): View? {
        _binding = AddItemLayoutBinding.inflate(
            inflater, container, false
        )

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            isDateSelected = true
            calendar.set(year, month, dayOfMonth)
            getDateSetListener(binding, calendar).onDateSet(null, year, month, dayOfMonth)
        }

        datePickerDialog = container?.let {
            DatePickerDialog(
                it.context,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }!!

        val context = binding.root.context

        binding.addMovieTitle.addTitleFocusListener(context, binding.addTitleContainer)
        binding.addMovieReleaseDate.addReleaseDateFocusListener(
            context,
            datePickerDialog,
            binding.addReleaseDateContainer
        )
        binding.addMovieDescription.addDescriptionFocusListener(
            context,
            binding.addDescriptionContainer
        )
        binding.addMovieRating.addRatingFocusListener(
            context,
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
        val imageSource = if (imageUri == null) "Missing Movie Poster" else null
        if (validFormFields(
                binding,
                isDateSelected,
                MovieData(title, releaseDate, description, ratingText, imageSource)
            )
        ) {
            val newMovie =
                Item(title!!, description!!, releaseDate!!, rating, imageUri.toString())
            viewModel.addItem(newMovie)
            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)
        } else {
            invalidForm(isDateSelected, imageUri?.let { imageUri.toString() }, binding)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}