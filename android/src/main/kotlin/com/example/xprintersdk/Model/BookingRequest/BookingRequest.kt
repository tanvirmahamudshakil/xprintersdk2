package com.example.xprintersdk.Model.BookingRequest


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BookingRequest(
    @SerializedName("arrival_time")
    val arrivalTime: String?,
    @SerializedName("branch_id")
    val branchId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("number_of_guest")
    val numberOfGuest: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("updated_at")
    val updatedAt: String?
)