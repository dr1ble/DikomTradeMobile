package com.example.dikommobile.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private val _profileName: MutableLiveData<String> = MutableLiveData("")
    val profileName: LiveData<String> = _profileName
    private val _email: MutableLiveData<String> = MutableLiveData("")
    val email: LiveData<String> = _email

    init {
        _email.value = Firebase.auth.currentUser?.email

        Firebase.auth.uid?.let { uid ->
            Firebase.firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Получаем имя пользователя из документа
                        val username = document.getString("username")
                        _profileName.value = username ?: "Имя не установлено"
                    } else {
                        _profileName.value = "Пользователь не найден"
                    }
                }
                .addOnFailureListener { exception ->
                    _profileName.value = "Ошибка при загрузке данных"
                    Log.e("Firebase", "Ошибка при получении данных", exception)
                }
        }
    }
}
