package com.example.movies_mvvm.ui.all_movies

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_mvvm.R
import com.example.movies_mvvm.databinding.AllItemsLayoutBinding
import com.example.movies_mvvm.ui.ItemsViewModel

class AllItemsFragment : Fragment() {

    private var _binding: AllItemsLayoutBinding? = null

    private val binding get() = _binding!!

    private val viewModel : ItemsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllItemsLayoutBinding.inflate(
            inflater, container, false
        )
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.items?.observe(viewLifecycleOwner){
            binding.recycler.adapter =
                ItemAdapter(it, object : ItemAdapter.ItemListener {
                    override fun onItemClicked(index: Int) {
                        val bundle = bundleOf(
                            "title" to it[index].title,
                            "description" to it[index].description,
                            "releaseDate" to it[index].releaseDate,
                            "rating" to it[index].rating,
                            "poster" to it[index].poster
                        )

                        findNavController().navigate(
                            R.id.action_allItemsFragment_to_movieFragment,
                            bundle
                        )
                    }

                })
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }


       /* ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

                builder.apply {
                    setTitle("Remove Confirmation")
                    setMessage("Are you sure you want to remove this movie?")
                    setCancelable(false)
                    setPositiveButton("Remove") { _, _ ->
                        MovieItemManager.remove(viewHolder.adapterPosition)
                        binding.recycler.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                    setNegativeButton("Cancel") { _, _ ->
                        binding.recycler.adapter!!.notifyItemChanged(viewHolder.adapterPosition)
                    }


                }.create()
                builder.show()


            }
        }).attachToRecyclerView(binding.recycler)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}