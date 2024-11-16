package com.example.xprintersdk.Model.LocalOrderData


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class LocalOrderData(
    @SerializedName("cashEntry")
    val cashEntry: Any?,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("customer")
    val customer: Customer?,
    @SerializedName("deliveryCharge")
    val deliveryCharge: Double?,
    @SerializedName("discountedAmount")
    val discountedAmount: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("items")
    val items: List<Item?>?,
    @SerializedName("localId")
    val localId: Int?,
    @SerializedName("netAmount")
    val netAmount: Double?,
    @SerializedName("orderChannel")
    val orderChannel: String?,
    @SerializedName("orderDate")
    val orderDate: String?,
    @SerializedName("orderType")
    val orderType: String?,
    @SerializedName("payableAmount")
    val payableAmount: Double?,
    @SerializedName("paymentId")
    val paymentId: Int?,
    @SerializedName("paymentType")
    val paymentType: String?,
    @SerializedName("prescriberId")
    val prescriberId: Any?,
    @SerializedName("requestedDeliveryTimestamp")
    val requestedDeliveryTimestamp: String?,
    @SerializedName("requesterId")
    val requesterId: Int?,
    @SerializedName("requesterType")
    val requesterType: String?,
    @SerializedName("requesterUuid")
    val requesterUuid: String?,
    @SerializedName("shippingAddressId")
    val shippingAddressId: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
) {
    @Keep
    data class Customer(
        @SerializedName("address")
        val address: Address?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("firstName")
        val firstName: String?,
        @SerializedName("lastName")
        val lastName: String?,
        @SerializedName("phone")
        val phone: String?
    ) {
        @Keep
        data class Address(
            @SerializedName("building")
            val building: String?,
            @SerializedName("city")
            val city: String?,
            @SerializedName("postcode")
            val postcode: String?,
            @SerializedName("street")
            val street: String?,
            @SerializedName("type")
            val type: String?
        )
    }

    @Keep
    data class Item(
        @SerializedName("comment")
        val comment: String?,
        @SerializedName("components")
        val components: List<Component?>?,
        @SerializedName("currency")
        val currency: String?,
        @SerializedName("discountPrice")
        val discountPrice: Double?,
        @SerializedName("extra")
        val extra: List<Extra?>?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("isDiscountApplied")
        val isDiscountApplied: Any?,
        @SerializedName("price")
        val price: Double?,
        @SerializedName("shortName")
        val shortName: String?,
        @SerializedName("subcategorykey")
        val subcategorykey: String?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("unit")
        val unit: Int?
    ) {
        @Keep
        data class Component(
            @SerializedName("comment")
            val comment: String?,
            @SerializedName("components")
            val components: Components?,
            @SerializedName("currency")
            val currency: String?,
            @SerializedName("groupName")
            val groupName: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("price")
            val price: Double?,
            @SerializedName("shortName")
            val shortName: String?,
            @SerializedName("type")
            val type: String?,
            @SerializedName("unit")
            val unit: Int?
        ) {
            @Keep
            data class Components(
                @SerializedName("comment")
                val comment: String?,
                @SerializedName("components")
                val components: Any?,
                @SerializedName("currency")
                val currency: String?,
                @SerializedName("groupName")
                val groupName: Any?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("price")
                val price: Int?,
                @SerializedName("shortName")
                val shortName: String?,
                @SerializedName("type")
                val type: String?,
                @SerializedName("unit")
                val unit: Int?
            )
        }

        @Keep
        data class Extra(
            @SerializedName("comment")
            val comment: String?,
            @SerializedName("components")
            val components: Any?,
            @SerializedName("currency")
            val currency: String?,
            @SerializedName("groupName")
            val groupName: Any?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("price")
            val price: Double?,
            @SerializedName("shortName")
            val shortName: String?,
            @SerializedName("type")
            val type: String?,
            @SerializedName("unit")
            val unit: Int?
        )
    }
}