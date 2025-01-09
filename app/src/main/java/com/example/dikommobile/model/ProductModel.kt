package com.example.dikommobile.model

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

data class ProductModel @OptIn(ExperimentalEncodingApi::class) constructor(
    var name: String = "",
    var description: String = "",
    var article: String = "",
    var height: String = "",
    var width: String = "",
    var depth: String = "",
    var photoBase64: String = "",
    var price: String = "",
    var numberInCart: Int = 0,
    var categoryId: String = "",
    var subcategoryId: String = ""
)
