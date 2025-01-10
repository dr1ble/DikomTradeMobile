package com.example.dikommobile.ui.detailedproduct

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dikommobile.databinding.FragmentDetailedproductBinding
import com.example.dikommobile.model.ProductModel

class DetailedProductFragment : Fragment() {

    private var _binding: FragmentDetailedproductBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: ProductModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получение данных из аргументов фрагмента
        arguments?.let {
            product = it.getParcelable("object")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val detailedProductViewModel =
            ViewModelProvider(this).get(DetailedProductViewModel::class.java)

        _binding = FragmentDetailedproductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обновление интерфейса данными из объекта product
        // Устанавливаем изображение
        product.photoBase64?.let {
            setImageFromBase64(it, binding.ivProductPhoto)
        }

        binding.tvDetailedProductTitle.text = product.name
        binding.tvDetailedProductPrice.text = product.price.toString()
        binding.tvDetailedProductDescription.text = product.description
        // Добавьте другие привязки для элементов UI, если требуется

        binding.ivBackArrow.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Функция для декодирования Base64 и установки изображения в ImageView
    private fun setImageFromBase64(base64String: String, imageView: android.widget.ImageView) {
        try {
            // Декодируем строку Base64 в байты
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

            // Преобразуем байты в Bitmap
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Устанавливаем Bitmap в ImageView
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            // Обработать ошибку в случае неверного формата Base64
        }
    }
}
