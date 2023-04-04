package com.example.movies_mvvm

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter =
            ItemAdapter(MovieItemManager.items, object : ItemAdapter.ItemListener {
                override fun onItemClicked(index: Int) {
                    val bundle = bundleOf(
                        "title" to MovieItemManager.items[index].title,
                        "description" to MovieItemManager.items[index].description,
                        "releaseDate" to MovieItemManager.items[index].releaseDate,
                        "rating" to MovieItemManager.items[index].rating,
                        "poster" to MovieItemManager.items[index].poster
                    )

                    findNavController().navigate(
                        R.id.action_allItemsFragment_to_movieFragment,
                        bundle
                    )
                }

            })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
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
                    setPositiveButton("Remove") { p0, p1 ->
                        MovieItemManager.remove(viewHolder.adapterPosition)
                        binding.recycler.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                    setNegativeButton("Cancel") { po, p1 ->
                        binding.recycler.adapter!!.notifyItemChanged(viewHolder.adapterPosition)
                    }


                }.create()
                builder.show()


            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}