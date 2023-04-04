package com.example.movies_mvvm.ui.single_movie

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.movies_mvvm.databinding.MovieLayoutBinding
import utils.getRating

class MovieFragment : Fragment() {
    private var _binding: MovieLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieLayoutBinding.inflate(
            inflater, container, false
        )
        val rotationImage: Animator =
            ObjectAnimator.ofFloat(binding.moviePoster, "rotationY", 0f, 360f).setDuration(2000)
        rotationImage.start()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*arguments?.getString("title")?.let {
            binding.movieTitle.text = it
        }
        arguments?.getString("releaseDate")?.let {
            binding.movieReleaseDate.text = it
        }
        arguments?.getString("description")?.let {
            binding.movieDescription.text = it
        }
        arguments?.getDouble("rating")?.let {
            println("rating it -> $it")
            binding.itemRatingBar.rating = getRating(it)
        }
        arguments?.getString("poster")?.let {
            Glide
                .with(binding.root)
                .load(it)
                .centerCrop()
                .into(binding.moviePoster)
        }*/

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}