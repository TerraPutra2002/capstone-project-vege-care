package com.example.vegecare.ui.splashscreen

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vegecare.MainActivity
import com.example.vegecare.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ObjectAnimator.ofFloat(binding.splashImage, "alpha", 0f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        val splashDuration = 3000L

        binding.root.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashDuration)
    }
}
