package com.example.movies_mvvm.ui.edit_movie

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
import utils.*
import java.util.*

class EditItemFragment : Fragment() {

    private var _binding: EditItemLayoutBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var initialImageUri: String? = null
    private var movieId: Int? = null
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

        binding.updateItemBtn.setOnClickListener {
            submitForm(isDateSelected)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_editItemFragment_to_allItemsFragment)
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

            movieId = it.id

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
        val title = binding.addMovieTitle.text?.toString()
        val releaseDate = binding.addMovieReleaseDate.text?.toString()
        val description = binding.addMovieDescription.text?.toString()
        val ratingText = binding.addMovieRating.text?.toString()
        val rating = when {
            ratingText?.isNotBlank() == true -> ratingText.toDouble()
            else -> 0.0
        }
        val imageSource = if (imageUri == null) initialImageUri else imageUri.toString()
        if (validFormFields(
                binding,
                isDateSelected,
                MovieData(title, releaseDate, description, ratingText, null)
            )
        ) {
            val mMovie = Item(title!!, description!!, releaseDate!!, rating, imageSource)
            mMovie.id = movieId!!
            viewModel.updateItem(mMovie)
            findNavController().navigate(R.id.action_editItemFragment_to_allItemsFragment)
        } else {
            invalidForm(isDateSelected, imageSource, binding)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}