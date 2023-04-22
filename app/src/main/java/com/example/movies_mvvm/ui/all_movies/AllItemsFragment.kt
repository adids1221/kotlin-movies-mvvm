package com.example.movies_mvvm.ui.all_movies

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
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

    private val viewModel: ItemsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = AllItemsLayoutBinding.inflate(
            inflater, container, false
        )
        binding.infoButton.setOnClickListener {
            AlertDialog.Builder(binding.root.context)
                .setTitle(getString(R.string.info_alert_title))
                .setMessage(getString(R.string.info_alert_instructions))
                .setPositiveButton("Okay") { _, _ -> }
                .show()
        }
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.items?.observe(viewLifecycleOwner) {
            binding.recycler.adapter =
                ItemAdapter(it, object : ItemAdapter.ItemListener {
                    override fun onItemClicked(index: Int) {
                        viewModel.setItem(it[index])
                        findNavController().navigate(
                            R.id.action_allItemsFragment_to_movieFragment
                        )
                    }

                    override fun onItemLongClick(index: Int) {
                        viewModel.setItem(it[index])
                        findNavController().navigate(
                            R.id.action_allItemsFragment_to_editItemFragment
                        )
                    }


                })
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }

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
                val item =
                    (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)

                builder.apply {
                    setTitle(getString(R.string.remove_confirmation_title))
                    setMessage(getString(R.string.remove_confirmation_message))
                    setCancelable(false)
                    setPositiveButton(getString(R.string.remove_confirmation_pos)) { _, _ ->
                        viewModel.removeItem(item)
                        binding.recycler.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                    setNegativeButton(getString(R.string.confirmation_dialog_cancel)) { _, _ ->
                        binding.recycler.adapter!!.notifyItemChanged(viewHolder.adapterPosition)
                    }


                }.show()


            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_remove_all) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setTitle(getString(R.string.remove_all_confirmation_title))
                setMessage(getString(R.string.remove_all_confirmation_message))
                setCancelable(false)
                setPositiveButton(getString(R.string.remove_all_confirmation_pos)) { _, _ ->
                    viewModel.removeAll()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.remove_all_confirmation_toast),
                        Toast.LENGTH_LONG
                    ).show()
                }
                setNegativeButton(getString(R.string.confirmation_dialog_cancel)) { _, _ ->

                }


            }.show()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}