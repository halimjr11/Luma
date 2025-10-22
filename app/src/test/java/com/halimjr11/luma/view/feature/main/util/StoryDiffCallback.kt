package com.halimjr11.luma.view.feature.main.util

import androidx.recyclerview.widget.DiffUtil
import com.halimjr11.luma.domain.model.StoryDomain

class StoryDiffCallback : DiffUtil.ItemCallback<StoryDomain>() {
    override fun areItemsTheSame(oldItem: StoryDomain, newItem: StoryDomain): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: StoryDomain, newItem: StoryDomain): Boolean =
        oldItem == newItem
}