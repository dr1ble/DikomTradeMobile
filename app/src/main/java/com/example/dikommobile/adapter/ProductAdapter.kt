package com.example.dikommobile.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.dikommobile.R
import com.example.dikommobile.databinding.ViewholderProductBinding
import com.example.dikommobile.model.ProductModel
import com.example.dikommobile.ui.detailedproduct.DetailedProductFragment
import kotlin.io.encoding.ExperimentalEncodingApi

class ProductAdapter(val products: MutableList<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var context: Context? = null

    class ViewHolder(val binding: ViewholderProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }


    @OptIn(ExperimentalEncodingApi::class)
    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.binding.tvProductTitle.text = products[position].name
        holder.binding.tvProductPrice.text = products[position].price.toString()
        holder.binding.tvProductArticle.text = products[position].article

        val requestOptions = RequestOptions().transform()

        // Декодирование Base64 строки
        val base64Image = products[position].photoBase64
        val bitmap = decodeBase64ToBitmap(base64Image.toString())

        if (bitmap != null) {
            Glide.with(holder.itemView.context)
                .load(bitmap) // Передаем Bitmap
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.ivProductPhoto)

        } else {
            // Если изображение не удалось загрузить, используем плейсхолдер
            holder.binding.ivProductPhoto.setImageResource(R.drawable.product_11_0205)
        }

        holder.itemView.setOnClickListener {
            val fragment = DetailedProductFragment()

            val bundle = Bundle().apply {
                putParcelable("object", products[position])  // Передаем данные в фрагмент
            }

            fragment.arguments = bundle

            // Навигация с использованием NavController
            val navController = (context as AppCompatActivity).findNavController(R.id.mainContainerFragment)
            navController.navigate(R.id.action_homeFragment_to_detailedProductFragment2, bundle)
        }

    }

}
