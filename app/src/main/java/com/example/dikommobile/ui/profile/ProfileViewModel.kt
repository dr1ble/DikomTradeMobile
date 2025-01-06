package com.example.dikommobile.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private val _profileName: MutableLiveData<String> = MutableLiveData("")
    val profileName: LiveData<String> = _profileName
    private val _email: MutableLiveData<String> = MutableLiveData("")
    val email: LiveData<String> = _email

    init {
        _email.value = Firebase.auth.currentUser?.email

        Firebase.auth.uid?.let { uid ->
            Firebase.database.getReference("Users").child(uid).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Получаем результат в виде DataSnapshot
                    val snapshot = task.result

                    // Проверяем, существует ли данные в snapshot
                    if (snapshot != null && snapshot.exists()) {
                        // Извлекаем имя пользователя
                        val username = snapshot.child("username").getValue(String::class.java)

                        // Устанавливаем имя в TextView
                        _profileName.value = username ?: "Имя не установлено"
                    } else {
                        // Если данных нет
                        _profileName.value = "Пользователь не найден"
                    }
                } else {
                    // Если запрос не удался
                    _profileName.value = "Ошибка при загрузке данных"
                    Log.e("Firebase", "Ошибка при получении данных", task.exception)
                }
            }
        }
    }
}