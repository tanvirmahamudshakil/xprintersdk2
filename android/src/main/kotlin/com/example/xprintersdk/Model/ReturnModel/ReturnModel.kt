package com.example.xprintersdk.Model.ReturnModel


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ReturnModel(
    @SerializedName("Date")
    val date: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("quantity")
    val quantity: Int?
)