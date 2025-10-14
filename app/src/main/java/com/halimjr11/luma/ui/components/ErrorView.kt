package com.halimjr11.luma.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.halimjr11.eventora.databinding.ErrorViewBinding
import com.halimjr11.eventora.ui.helper.gone

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

    fun setMessageAndCallback(message: String, onClick: () -> Unit) {
        binding.tvErrorMessage.text = message
        binding.lottieError.playAnimation()
        onRetry = onClick
    }

    fun setMessageNoButton(message: String) {
        binding.tvErrorMessage.text = message
        binding.lottieError.playAnimation()
        binding.btnRetry.gone()
    }
}