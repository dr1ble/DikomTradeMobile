package com.example.dikommobile.ui.login

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val _toast: MutableLiveData<String?> = MutableLiveData(null)
    val toast: LiveData<String?> = _toast
    private val _authState = MutableLiveData(false)
    val authState: LiveData<Boolean> = _authState

    fun login(email: String, password: String){

        if (email.isEmpty() || password.isEmpty()){
            _toast.value = "Invalid email or password"
        }
        else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email,
                password
            )
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        _toast.value = "Успешный вход"
                        _authState.value = true
                    } else {
                        _toast.value = "Ошибка авторизации: ${task.exception?.message}"
                    }
                }
        }
    }
}