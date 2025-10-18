package com.halimjr11.luma.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.halimjr11.luma.databinding.ItemFeaturedStoryBinding
import com.halimjr11.luma.domain.model.StoryDomain

class FeaturedAdapter :
    ListAdapter<StoryDomain, FeaturedAdapter.FeaturedViewHolder>(StoryDiffCallback) {
    private var onCardClick: ((StoryDomain) -> Unit)? = null

    fun setOnClickCallback(action: ((StoryDomain) -> Unit)?) {
        onCardClick = action
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeaturedViewHolder = FeaturedViewHolder(
        binding = ItemFeaturedStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeaturedViewHolder(
        private val binding: ItemFeaturedStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryDomain) = binding.run {
            ivCarouselBanner.load(data.photoUrl)
            tvCarouselTitle.text = data.description
            binding.root.setOnClickListener {
                onCardClick?.invoke(data)
            }
        }
    }
}