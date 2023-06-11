package com.example.movies_mvvm.ui.all_movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_mvvm.data.model.Movie
import com.example.movies_mvvm.databinding.ItemLayoutBinding
import utils.getRating


class MovieAdapter(private val items: List<Movie>, val callback: ItemListener) :
    RecyclerView.Adapter<MovieAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemClicked(index: Int)
        fun onItemLongClick(index: Int)

    }

    inner class ItemViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        fun bind(movie: Movie) {
            Glide
                .with(binding.root)
                .load(movie.posterPath)
                .centerCrop()
                .into(binding.moviePoster)
            binding.movieDescription.text = movie.overview
            binding.movieTitle.text = movie.title
            binding.movieReleaseDate.text = movie.releaseDate
            binding.itemRatingBar.rating = movie.voteAverage?.let { getRating(it) }!!
        }


        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }


        override fun onClick(v: View?) {
            callback.onItemClicked(adapterPosition)


        }

        override fun onLongClick(p0:View?):Boolean{
            callback.onItemLongClick(adapterPosition)
            return true
        }


    }

    fun itemAt(position: Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
}