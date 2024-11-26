package com.example.vegecare.ui.splashscreen

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vegecare.databinding.ActivitySplashScreenBinding
import com.example.vegecare.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateSplashImage()

        navigateToLoginAfterDelay()
    }

    private fun animateSplashImage() {
        ObjectAnimator.ofFloat(binding.splashImage, "alpha", 0f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun navigateToLoginAfterDelay() {
        val splashDuration = 3000L
        binding.root.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, splashDuration)
    }
}
