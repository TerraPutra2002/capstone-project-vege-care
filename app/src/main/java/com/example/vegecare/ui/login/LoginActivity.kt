package com.example.vegecare.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.vegecare.MainActivity
import com.example.vegecare.data.user.UserViewModelFactory
import com.example.vegecare.data.user.database.AppDatabase
import com.example.vegecare.data.user.repository.UserRepository
import com.example.vegecare.databinding.ActivityLoginBinding
import com.example.vegecare.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        UserViewModelFactory(UserRepository(AppDatabase.getDatabase(this).userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loginStatus.observe(this, { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()

            if (result == "Login successful!") {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.login(email, password)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
