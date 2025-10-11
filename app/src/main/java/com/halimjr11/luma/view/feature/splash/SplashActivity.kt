package com.halimjr11.luma.view.feature.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.halimjr11.luma.databinding.ActivitySplashBinding
import com.halimjr11.luma.view.feature.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        animateLogo()
    }

    private fun animateLogo() = with(binding) {
        val fadeIn = ObjectAnimator.ofFloat(splashLogo, "alpha", 0f, 1f)
        val scaleX = ObjectAnimator.ofFloat(splashLogo, "scaleX", 0.8f, 1f)
        val scaleY = ObjectAnimator.ofFloat(splashLogo, "scaleY", 0.8f, 1f)

        AnimatorSet().run {
            playTogether(fadeIn, scaleX, scaleY)
            duration = 1200
            interpolator = DecelerateInterpolator()
            start()

            addListener(onEnd = {
                // small glow pulse
                val pulseX = ObjectAnimator.ofFloat(splashLogo, "scaleX", 1f, 1.05f, 1f)
                val pulseY = ObjectAnimator.ofFloat(splashLogo, "scaleY", 1f, 1.05f, 1f)
                AnimatorSet().apply {
                    playTogether(pulseX, pulseY)
                    duration = 800
                    startDelay = 300
                    start()
                }

                lifecycleScope.launch {
                    delay(2500)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            })
        }
    }
}
