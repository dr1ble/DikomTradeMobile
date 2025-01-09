package com.example.dikommobile.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val _registrationStatus = MutableLiveData<RegistrationStatus>()
    val registrationStatus: LiveData<RegistrationStatus> get() = _registrationStatus

    // Состояние регистрации
    sealed class RegistrationStatus {
        object Loading : RegistrationStatus()
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
                            "uid" to it.uid,
                            "email" to email,
                            "username" to name
                        )
                        FirebaseFirestore.getInstance().collection("Users")
                            .document(it.uid)
                            .set(userInfo)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    _registrationStatus.value = RegistrationStatus.Success("Регистрация успешна")
                                    SharedPrefsHelper.setUserAuthorized(true) // Ваш метод для сохранения статуса авторизации
                                } else {
                                    _registrationStatus.value = RegistrationStatus.Error("Ошибка сохранения данных: ${databaseTask.exception?.message}")
                                }
                            }
                            .addOnFailureListener { exception ->
                                _registrationStatus.value = RegistrationStatus.Error("Ошибка при записи в Firestore: ${exception.message}")
                            }
                    }
                } else {
                    _registrationStatus.value = RegistrationStatus.Error("Ошибка регистрации: ${task.exception?.localizedMessage}")
                }
            }
    }
}
