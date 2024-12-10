package com.example.vegecare.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.vegecare.data.user.UserViewModelFactory
import com.example.vegecare.data.user.database.AppDatabase
import com.example.vegecare.data.user.repository.UserRepository
import com.example.vegecare.databinding.ActivityRegisterBinding
import com.example.vegecare.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        UserViewModelFactory(UserRepository(AppDatabase.getDatabase(this).userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.registerStatus.observe(this, { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()

            if (result == "Registration successful!") {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                binding.nameEditTextLayout.error = "Nama tidak boleh kosong"
                isValid = false
            } else {
                binding.nameEditTextLayout.error = null
            }

            if (email.isEmpty()) {
                binding.emailEditTextLayout.error = "Email tidak boleh kosong"
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditTextLayout.error = "Format email tidak valid"
                isValid = false
            } else {
                binding.emailEditTextLayout.error = null
            }

            if (password.isEmpty()) {
                binding.passwordEditTextLayout.error = "Password tidak boleh kosong"
                isValid = false
            } else if (password.length < 8) {
                binding.passwordEditTextLayout.error = "Password minimal 8 karakter"
                isValid = false
            } else {
                binding.passwordEditTextLayout.error = null
            }

            if (isValid) {
                viewModel.register(name, email, password)
            }
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
