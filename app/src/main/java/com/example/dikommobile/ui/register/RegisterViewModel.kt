package com.example.dikommobile.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {

    private val _registrationStatus = MutableLiveData<RegistrationStatus>()
    val registrationStatus: LiveData<RegistrationStatus> get() = _registrationStatus

    // Состояние регистрации
    sealed class RegistrationStatus {
        data object Loading : RegistrationStatus()
        data class Success(val message: String) : RegistrationStatus()
        data class Error(val message: String) : RegistrationStatus()
    }

    fun registerUser(name: String, email: String, password: String) {
        _registrationStatus.value = RegistrationStatus.Loading

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let {
                        val userInfo = hashMapOf(
                            "email" to email,
                            "username" to name,
                            "password" to password
                        )
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it.uid)
                            .setValue(userInfo)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    _registrationStatus.value = RegistrationStatus.Success("Регистрация успешна")
                                } else {
                                    _registrationStatus.value = RegistrationStatus.Error("Ошибка сохранения данных: ${databaseTask.exception?.message}")
                                }
                            }
                            .addOnFailureListener { exception ->
                                _registrationStatus.value = RegistrationStatus.Error("Ошибка при записи в базу данных: ${exception.message}")
                            }
                    }
                } else {
                    _registrationStatus.value = RegistrationStatus.Error("Ошибка регистрации: ${task.exception?.localizedMessage}")
                }
            }
    }
}
