package com.halimjr11.luma.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.halimjr11.luma.R
import com.halimjr11.luma.databinding.ItemStoryBinding
import com.halimjr11.luma.domain.model.StoryDomain

class StoryAdapter :
    PagingDataAdapter<StoryDomain, StoryAdapter.StoryViewHolder>(StoryDiffCallback) {
    private var onCardClick: ((StoryDomain) -> Unit)? = null

    fun setOnClickCallback(action: ((StoryDomain) -> Unit)?) {
        onCardClick = action
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryDomain?) {
            binding.tvItemName.text = story?.name
            binding.tvItemDescription.text = story?.description
            binding.ivItemPhoto.load(story?.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_placeholder)
            }
            binding.root.setOnClickListener {
                story?.let { item -> onCardClick?.invoke(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
