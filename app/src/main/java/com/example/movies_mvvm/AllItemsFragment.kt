package com.example.movies_mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movies_mvvm.databinding.AllItemsLayoutBinding

class AllItemsFragment : Fragment() {

    private var _binding: AllItemsLayoutBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllItemsLayoutBinding.inflate(
            inflater, container, false
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoviesList.adapter = ItemAdapter(MovieItemManager.items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}