package com.example.vegecare.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vegecare.data.user.database.User
import com.example.vegecare.data.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerStatus = MutableLiveData<String>()
    val registerStatus: LiveData<String> get() = _registerStatus

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = registerUser(name, email, password)
            _registerStatus.postValue(result)
        }
    }

    private suspend fun registerUser(name: String, email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            if (repository.checkEmail(email)) {
                "Email already exists!"
            } else {
                repository.registerUser(User(name = name, email = email, password = password))
                "Registration successful!"
            }
        }
    }
}
