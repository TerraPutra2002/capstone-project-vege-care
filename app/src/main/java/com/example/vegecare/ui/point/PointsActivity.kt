package com.example.vegecare.ui.point

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vegecare.databinding.ActivityPointsBinding

class PointsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPointsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPointsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.specialOffer.setOnClickListener {
            Toast.makeText(
                this,
                "Anda membeli produk: Bebas penggunaan fitur deteksi selama 2 bulan",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.lp1000.setOnClickListener {
            Toast.makeText(this, "Anda membeli produk: 1000 LP", Toast.LENGTH_SHORT).show()
        }

        binding.lp800.setOnClickListener {
            Toast.makeText(this, "Anda membeli produk: 800 LP", Toast.LENGTH_SHORT).show()
        }

        binding.lp600.setOnClickListener {
            Toast.makeText(this, "Anda membeli produk: 600 LP", Toast.LENGTH_SHORT).show()
        }

        binding.lp400.setOnClickListener {
            Toast.makeText(this, "Anda membeli produk: 400 LP", Toast.LENGTH_SHORT).show()
        }

        binding.lp200.setOnClickListener {
            Toast.makeText(this, "Anda membeli produk: 200 LP", Toast.LENGTH_SHORT).show()
        }

        binding.lp50.setOnClickListener {
            Toast.makeText(this, "Anda membeli produk: 50 LP", Toast.LENGTH_SHORT).show()
        }
    }
}
