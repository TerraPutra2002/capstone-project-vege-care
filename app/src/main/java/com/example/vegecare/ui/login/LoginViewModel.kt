package com.example.vegecare.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vegecare.data.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository, private val context: Context) : ViewModel() {

    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> get() = _loginStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = loginUser(email, password)
            _loginStatus.postValue(result)
        }
    }

    private suspend fun loginUser(email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            val user = repository.loginUser(email, password)
            if (user != null) {
                saveLoginState(user.id, user.name)
                "Login successful!"
            } else {
                "Invalid email or password!"
            }
        }
    }

    private fun saveLoginState(userId: Int, username: String) {
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putBoolean("isLoggedIn", true)
            putInt("userId", userId)
            putString("username", username)
            apply()
        }
    }

    fun getLoginState(): Boolean {
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun getUsername(): String? {
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }
}
