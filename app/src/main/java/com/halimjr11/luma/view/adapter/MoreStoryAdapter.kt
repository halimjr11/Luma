package com.halimjr11.luma.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.halimjr11.luma.databinding.ItemStoryBinding
import com.halimjr11.luma.domain.model.StoryDomain

class MoreStoryAdapter :
    ListAdapter<StoryDomain, MoreStoryAdapter.MoreStoryViewHolder>(StoryDiffCallback) {
    private var onCardClick: ((StoryDomain) -> Unit)? = null

    fun setOnClickCallback(action: ((StoryDomain) -> Unit)?) {
        onCardClick = action
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoreStoryViewHolder = MoreStoryViewHolder(
        binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: MoreStoryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class MoreStoryViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryDomain) = binding.run {
            ivItemPhoto.load(data.photoUrl)
            tvItemName.text = data.name
            tvItemDescription.text = data.description
            binding.root.setOnClickListener {
                onCardClick?.invoke(data)
            }
        }
    }
}