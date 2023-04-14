package com.example.movies_mvvm.ui.single_movie

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movies_mvvm.R
import com.example.movies_mvvm.databinding.MovieLayoutBinding
import com.example.movies_mvvm.ui.ItemsViewModel
import utils.getRating


class MovieFragment : Fragment() {
    private var _binding: MovieLayoutBinding? = null

    val viewModel: ItemsViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieLayoutBinding.inflate(
            inflater, container, false
        )
        val translationImage: Animator =
            ObjectAnimator.ofFloat(binding.moviePoster, "translationY", -150f, 0f).setDuration(2000)
        val translationTitle: Animator =
            ObjectAnimator.ofFloat(binding.movieTitle, "translationX", 150f, 0f).setDuration(2000)
        val translationDesc: Animator =
            ObjectAnimator.ofFloat(binding.movieDescription, "translationY", 150f, 0f).setDuration(2000)
        val rotationRating: Animator =
            ObjectAnimator.ofFloat(binding.itemRatingBar, "rotationX", 0f, 360f).setDuration(2000)
        val rotationReleaseDate: Animator =
            ObjectAnimator.ofFloat(binding.movieReleaseDate, "rotationX", 0f, -360f).setDuration(2000)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationImage,translationTitle,translationDesc,rotationRating,rotationReleaseDate)
        animatorSet.start()
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.action_movieFragment_to_editItemFragment)
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_movieFragment_to_allItemsFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenItem.observe(viewLifecycleOwner) {
            binding.movieTitle.text = it.title
            binding.movieReleaseDate.text = it.releaseDate
            binding.movieDescription.text = it.description
            binding.itemRatingBar.rating = it.rating?.let { it1 -> getRating(it1) }!!
            Glide
                .with(requireContext())
                .load(it.poster)
                .centerCrop()
                .into(binding.moviePoster)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}