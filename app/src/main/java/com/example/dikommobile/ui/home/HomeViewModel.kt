package com.example.dikommobile.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dikommobile.model.ProductModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {
    private val _products = MutableLiveData<MutableList<ProductModel>>()

    val products: LiveData<MutableList<ProductModel>> = _products

    fun loadItems() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lists = mutableListOf<ProductModel>()
                for (document in querySnapshot.documents) {
                    val product = document.toObject(ProductModel::class.java)
                    if (product != null) {
                        lists.add(product)
                    }
                }
                _products.value = lists
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Ошибка при загрузке данных: ${exception.message}", exception)
                // Обработайте ошибку, например, покажите сообщение пользователю
            }
    }
}