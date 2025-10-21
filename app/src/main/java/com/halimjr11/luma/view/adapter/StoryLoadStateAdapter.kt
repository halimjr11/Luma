package com.halimjr11.luma.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.halimjr11.luma.databinding.ItemLoadingStateBinding

class StoryLoadStateAdapter() :
    LoadStateAdapter<StoryLoadStateAdapter.LoadStateViewHolder>() {
    private var onClick: (() -> Unit)? = null
    fun setOnRetryCallback(retry: () -> Unit) {
        onClick = retry
    }

    inner class LoadStateViewHolder(private val binding: ItemLoadingStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.evListStory.isVisible = loadState is LoadState.Error
            binding.evListStory.setMessageAndCallback("Failed to load data") {
                onClick?.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val binding = ItemLoadingStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }
}
