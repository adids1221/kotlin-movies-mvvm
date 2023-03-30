package com.example.movies_mvvm

import android.app.appsearch.BatchResultCallback
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.BatchingListUpdateCallback

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_mvvm.databinding.ItemLayoutBinding


class ItemAdapter(private val items: List<Item>,val callback:ItemListener) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener{
        fun onItemClicked(index:Int)
    }

   inner class ItemViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: Item) {
            Glide
                .with(binding.root)
                .load(item.poster)
                .centerCrop()
                .into(binding.moviePoster)
            binding.movieDescription.text = item.description
            binding.movieTitle.text = item.title
            binding.movieReleaseDate.text = item.releaseDate
            binding.itemRatingBar.rating = item.rating?.let { getRating(it) }!!



        }




        fun getRating(rating: Double): Float {
            val maxRating = 5.0
            val stepSize = 0.5
            val numOfStars = 5

            val numStars = (rating / (maxRating / numOfStars)).toFloat()

            return (numStars * stepSize).toFloat()
        }

       init{
           binding.root.setOnClickListener(this)
       }



       override fun onClick(v: View?) {
           callback.onItemClicked(adapterPosition)



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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
}