package com.example.movies_mvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_mvvm.databinding.ItemLayoutBinding
import com.squareup.picasso.Picasso

class ItemAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            Picasso.get().load(item.poster).into(binding.moviePoster)
            binding.movieDescription.text = item.description
            binding.movieTitle.text = item.title
            binding.movieReleaseDate.text = item.releaseDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }


    override fun getItemCount(): Int = items.size
}