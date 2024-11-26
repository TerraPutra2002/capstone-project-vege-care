package com.example.vegecare.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vegecare.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

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
                "Login successful!"
            } else {
                "Invalid email or password!"
            }
        }
    }
}
