package com.example.movies_mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_mvvm.databinding.AllItemsLayoutBinding
import utils.demoData

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
        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("title")?.let{
            Toast.makeText(requireActivity(),it,Toast.LENGTH_LONG).show()
        }

        binding.recycler.adapter = ItemAdapter(demoData,object :ItemAdapter.ItemListener{
            override fun onItemClicked(index: Int) {
                val bundle = bundleOf("title" to demoData[index].title,
                "description" to demoData[index].description,
                "releaseDate" to demoData[index].releaseDate,
                "rating" to demoData[index].rating,
                "poster" to demoData[index].poster)

                findNavController().navigate(R.id.action_allItemsFragment_to_movieFragment,bundle)
            }

        })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}