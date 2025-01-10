package com.example.dikommobile.model

import android.os.Parcel
import android.os.Parcelable
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
    var subcategoryId: String = "",
    var isLiked: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(article)
        parcel.writeString(height)
        parcel.writeString(width)
        parcel.writeString(depth)
        parcel.writeString(photoBase64)
        parcel.writeString(price)
        parcel.writeInt(numberInCart)
        parcel.writeString(categoryId)
        parcel.writeString(subcategoryId)
        parcel.writeByte(if (isLiked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }
}
