package com.halimjr11.luma.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.halimjr11.luma.domain.model.StoryDomain

object StoryDiffCallback : DiffUtil.ItemCallback<StoryDomain>() {
    override fun areItemsTheSame(oldItem: StoryDomain, newItem: StoryDomain): Boolean =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: StoryDomain, newItem: StoryDomain): Boolean =
        oldItem == newItem
}