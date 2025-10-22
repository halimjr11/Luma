package com.halimjr11.luma.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.halimjr11.luma.databinding.ErrorViewBinding

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ErrorViewBinding.inflate(LayoutInflater.from(context), this, true)

    var onRetry: (() -> Unit)? = null
        private set

    init {
        binding.btnRetry.setOnClickListener {
            onRetry?.invoke()
        }
    }

    fun setMessageAndCallback(message: String, onClick: () -> Unit = {}) {
        binding.tvErrorMessage.text = message
        binding.lottieError.playAnimation()
        onRetry = onClick
    }

    fun setFavMessageAndCallback(message: String) {
        binding.tvErrorMessage.text = message
        binding.btnRetry.isVisible = false
        binding.lottieError.playAnimation()
    }

    fun setMessegeOnLoading(message: String, onClick: () -> Unit) {
        binding.lottieError.isVisible = false
        binding.tvErrorMessage.text = message
        onRetry = onClick
    }
}