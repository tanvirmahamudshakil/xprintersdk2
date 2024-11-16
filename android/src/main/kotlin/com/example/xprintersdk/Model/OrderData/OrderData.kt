package com.example.xprintersdk.Model.OrderData


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class OrderData(
    @SerializedName("barcode")
    val barcode: String?,
    @SerializedName("branch")
    val branch: Branch?,
    @SerializedName("branch_id")
    val branchId: Int?,
    @SerializedName("changeAmount")
    var changeAmount : Double?,
    @SerializedName("cash_entry")
    val cashEntry: List<CashEntry?>?,
    @SerializedName("comment")
    val comment: Any?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("delivery_charge")
    val deliveryCharge: Double?,
    @SerializedName("discounted_amount")
    val discountedAmount: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("local_id")
    val localId: Int?,
    @SerializedName("net_amount")
    val netAmount: Double?,
    @SerializedName("order_channel")
    val orderChannel: String?,
    @SerializedName("order_date")
    val orderDate: String?,
    @SerializedName("order_files")
    val orderFiles: List<Any?>?,
    @SerializedName("order_products")
    val orderProducts: List<OrderProduct?>?,
    @SerializedName("order_type")
    val orderType: String?,
    @SerializedName("payable_amount")
    val payableAmount: Double?,
    @SerializedName("payment")
    val payment: Payment?,
    @SerializedName("payment_id")
    val paymentId: Any?,
    @SerializedName("payment_type")
    val paymentType: String?,
    @SerializedName("prescriber")
    val prescriber: Any?,
    @SerializedName("prescriber_id")
    val prescriberId: Any?,
    @SerializedName("requested_delivery_timestamp")
    val requestedDeliveryTimestamp: String?,
    @SerializedName("requester")
    val requester: Requester?,
    @SerializedName("requester_guest")
    val requesterGuest: RequesterGuest?,
    @SerializedName("requester_id")
    val requesterId: Int?,
    @SerializedName("requester_type")
    val requesterType: String?,
    @SerializedName("requester_uuid")
    val requesterUuid: String?,
    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddress?,
    @SerializedName("shipping_address_id")
    val shippingAddressId: Any?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("table_id")
    var table_id: Int?,
    @SerializedName("property")
    val property: PropertyXXX?,
    @SerializedName("vat")
    var vat_amount: Double,
    @SerializedName("order_id")
    var order_id: Int
) {
    @Keep
    data class PropertyXXX(
        @SerializedName("comment") val comment: Any,
        @SerializedName("requested_delivery_timestamp_type") val requestedDeliveryTimestampType: String
    )
    @Keep
    data class Branch(
        @SerializedName("created_at")
        val createdAt: Any?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("property")
        val `property`: Property?,
        @SerializedName("updated_at")
        val updatedAt: Any?,
        @SerializedName("value")
        val value: String?
    ) {
        @Keep
        data class Property(
            @SerializedName("address")
            val address: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("phone")
            val phone: String?,
            @SerializedName("postcode")
            val postcode: String?
        )
    }

    @Keep
    data class CashEntry(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("timestamp")
        val timestamp: Any?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("order_id")
        val orderId: Int?,
        @SerializedName("amount")
        val amount: Double?,
        @SerializedName("comment")
        val comment: String?,
        @SerializedName("created_at")
        val createdAt: Any?,
        @SerializedName("updated_at")
        val updatedAt: Any?,

//        @SerializedName("components")
//        val components: List<Component?>?,
//        @SerializedName("discountable_amount")
//        val discountableAmount: Double?,
//        @SerializedName("id")
//        val id: Int?,
//        @SerializedName("net_amount")
//        val netAmount: Double?,
//        @SerializedName("order_id")
//        val orderId: Int?,
//        @SerializedName("parent_id")
//        val parentId: String?,
//        @SerializedName("parent_product_id")
//        val parentProductId: String?,
//        @SerializedName("product")
//        val product: Product?,
//        @SerializedName("product_id")
//        val productId: Int?,
//        @SerializedName("unit")
//        val unit: Int?
    ) {
//        @Keep
//        data class Component(
//            @SerializedName("comment")
//            val comment: Any?,
//            @SerializedName("components")
//            val components: List<Any?>?,
//            @SerializedName("discountable_amount")
//            val discountableAmount: Double?,
//            @SerializedName("id")
//            val id: Int?,
//            @SerializedName("net_amount")
//            val netAmount: Double?,
//            @SerializedName("order_id")
//            val orderId: Int?,
//            @SerializedName("parent_id")
//            val parentId: Int?,
//            @SerializedName("parent_product_id")
//            val parentProductId: Int?,
//            @SerializedName("product")
//            val product: Product?,
//            @SerializedName("product_id")
//            val productId: Int?,
//            @SerializedName("unit")
//            val unit: Int?
//        ) {
//            @Keep
//            data class Product(
//                @SerializedName("barcode")
//                val barcode: Any?,
//                @SerializedName("creator_id")
//                val creatorId: Int?,
//                @SerializedName("creator_uuid")
//                val creatorUuid: String?,
//                @SerializedName("description")
//                val description: Any?,
//                @SerializedName("discountable")
//                val discountable: Double?,
//                @SerializedName("id")
//                val id: Int?,
//                @SerializedName("property")
//                val `property`: Property?,
//                @SerializedName("short_name")
//                val shortName: String?,
//                @SerializedName("sort_order")
//                val sortOrder: Int?,
//                @SerializedName("status")
//                val status: Int?,
//                @SerializedName("tags")
//                val tags: Any?,
//                @SerializedName("type")
//                val type: String?,
//                @SerializedName("uuid")
//                val uuid: String?
//            ) {
//                @Keep
//                data class Property(
//                    @SerializedName("is_coupon")
//                    val isCoupon: String?,
//                    @SerializedName("platform")
//                    val platform: String?
//                )
//            }
//        }
//
//        @Keep
//        data class Product(
//            @SerializedName("barcode")
//            val barcode: String?,
//            @SerializedName("creator_id")
//            val creatorId: Int?,
//            @SerializedName("creator_uuid")
//            val creatorUuid: String?,
//            @SerializedName("description")
//            val description: String?,
//            @SerializedName("discountable")
//            val discountable: Double?,
//            @SerializedName("id")
//            val id: Int?,
//            @SerializedName("property")
//            val `property`: Property?,
//            @SerializedName("short_name")
//            val shortName: String?,
//            @SerializedName("sort_order")
//            val sortOrder: Int?,
//            @SerializedName("status")
//            val status: Int?,
//            @SerializedName("tags")
//            val tags: String?,
//            @SerializedName("type")
//            val type: String?,
//            @SerializedName("uuid")
//            val uuid: String?
//        ) {
//            @Keep
//            data class Property(
//                @SerializedName("address")
//                val address: String?,
//                @SerializedName("email")
//                val email: String?,
//                @SerializedName("phone")
//                val phone: String?,
//                @SerializedName("postcode")
//                val postcode: String?
//            )
//        }
    }

    @Keep
    data class OrderProduct(
        @SerializedName("comment")
        val comment: String?,
        @SerializedName("components")
        val components: List<Component?>?,
        @SerializedName("discountable_amount")
        val discountableAmount: Double?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("net_amount")
        val netAmount: Double?,
        @SerializedName("order_id")
        val orderId: Int?,
        @SerializedName("parent_id")
        val parentId: Any?,
        @SerializedName("parent_product_id")
        val parentProductId: Any?,
        @SerializedName("product")
        val product: Product?,
        @SerializedName("product_id")
        val productId: Int?,
        @SerializedName("unit")
        val unit: Int?,
        @SerializedName("offer")
        val offer : ProductDatumOffer?
    ) {
        @Keep
        data class ProductDatumOffer(
            @SerializedName("id")
            val id : Int?,
            @SerializedName("offer_id")
            val offerId : Int?,
            @SerializedName("product_id")
            val productId : Int?,
            @SerializedName("offer")
            val offer : OfferOffer?
        ){
            @Keep
            data class OfferOffer(
                @SerializedName("id")
                val id : Int?,
                @SerializedName("title")
                val title : String?,
                @SerializedName("type")
                val type : String?,
                @SerializedName("buy")
                val buy: Int?,
                @SerializedName("for")
                val offerFor : Double?,
                @SerializedName("status")
                val status : Int?,
            )
        }
        @Keep
        data class Component(
            @SerializedName("comment")
            val comment: Any?,
            @SerializedName("components")
            val components: List<Component?>?,
            @SerializedName("discountable_amount")
            val discountableAmount: Double?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("net_amount")
            val netAmount: Double?,
            @SerializedName("order_id")
            val orderId: Int?,
            @SerializedName("parent_id")
            val parentId: Int?,
            @SerializedName("parent_product_id")
            val parentProductId: Int?,
            @SerializedName("product")
            val product: Product?,
            @SerializedName("product_id")
            val productId: Int?,
            @SerializedName("unit")
            val unit: Int?
        ) {
            @Keep
            data class Component(
                @SerializedName("comment")
                val comment: Any?,
                @SerializedName("components")
                val components: List<Any?>?,
                @SerializedName("discountable_amount")
                val discountableAmount: Double?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("net_amount")
                val netAmount: Double?,
                @SerializedName("order_id")
                val orderId: Int?,
                @SerializedName("parent_id")
                val parentId: Int?,
                @SerializedName("parent_product_id")
                val parentProductId: Int?,
                @SerializedName("product")
                val product: Product?,
                @SerializedName("product_id")
                val productId: Int?,
                @SerializedName("unit")
                val unit: Int?
            ) {
                @Keep
                data class Product(
                    @SerializedName("barcode")
                    val barcode: Any?,
                    @SerializedName("creator_id")
                    val creatorId: Int?,
                    @SerializedName("creator_uuid")
                    val creatorUuid: String?,
                    @SerializedName("description")
                    val description: Any?,
                    @SerializedName("discountable")
                    val discountable: Double?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("property")
                    val `property`: Property?,
                    @SerializedName("short_name")
                    val shortName: String?,
                    @SerializedName("sort_order")
                    val sortOrder: Int?,
                    @SerializedName("status")
                    val status: Int?,
                    @SerializedName("tags")
                    val tags: Any?,
                    @SerializedName("type")
                    val type: String?,
                    @SerializedName("uuid")
                    val uuid: String?
                ) {
                    @Keep
                    data class Property(
                        @SerializedName("is_coupon")
                        val isCoupon: String?,
                        @SerializedName("platform")
                        val platform: String?
                    )
                }
            }

            @Keep
            data class Product(
                @SerializedName("barcode")
                val barcode: Any?,
                @SerializedName("creator_id")
                val creatorId: Int?,
                @SerializedName("creator_uuid")
                val creatorUuid: String?,
                @SerializedName("description")
                val description: Any?,
                @SerializedName("discountable")
                val discountable: Double?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("property")
                val `property`: Property?,
                @SerializedName("short_name")
                val shortName: String?,
                @SerializedName("sort_order")
                val sortOrder: Int?,
                @SerializedName("status")
                val status: Int?,
                @SerializedName("tags")
                val tags: String?,
                @SerializedName("type")
                val type: String?,
                @SerializedName("uuid")
                val uuid: String?
            ) {
                @Keep
                data class Property(
                    @SerializedName("is_coupon")
                    val isCoupon: String?,
                    @SerializedName("platform")
                    val platform: String?,
                    @SerializedName("item_type")
                    var itemtype : String?
                )
            }
        }

        @Keep
        data class Product(
            @SerializedName("barcode")
            val barcode: Any?,
            @SerializedName("creator_id")
            val creatorId: Int?,
            @SerializedName("creator_uuid")
            val creatorUuid: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("discountable")
            val discountable: Double?,
            @SerializedName("files")
            val files: List<File?>?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("property")
            val `property`: Property?,
            @SerializedName("short_name")
            val shortName: String?,
            @SerializedName("sort_order")
            val sortOrder: Int?,
            @SerializedName("status")
            val status: Int?,
            @SerializedName("tags")
            val tags: String?,
            @SerializedName("type")
            val type: String?,
            @SerializedName("uuid")
            val uuid: String?
        ) {
            @Keep
            data class File(
                @SerializedName("file_name")
                val fileName: String?,
                @SerializedName("file_path")
                val filePath: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("product_id")
                val productId: Int?,
                @SerializedName("type")
                val type: String?
            )

            @Keep
            data class Property(
                @SerializedName("category")
                val category: String?,
                @SerializedName("discount_type")
                val discountType: String?,
                @SerializedName("discount_value")
                val discountValue: String?,
                @SerializedName("epos_category")
                val eposCategory: String?,
                @SerializedName("featured")
                val featured: String?,
                @SerializedName("is_coupon")
                val isCoupon: String?,
                @SerializedName("platform")
                val platform: String?,
                @SerializedName("short_description")
                val shortDescription: String?,
                @SerializedName("print_order")
                val printorder: String?,
                @SerializedName("unit_amount")
                val unit_amount: String?,
                @SerializedName("tare_weight")
                val tare_weight: String?,
                @SerializedName("unit_product_type")
                val unit_product_type: String?,
                @SerializedName("unit_of_sale")
                val unit_of_sale: String?,
                @SerializedName("expire_date")
                val expire_date : String?
            )
        }
    }

    @Keep
    data class Payment(
        @SerializedName("amount")
        val amount: Double?,
        @SerializedName("card_type")
        val cardType: String?,
        @SerializedName("comment")
        val comment: String?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("payment_date")
        val paymentDate: String?,
        @SerializedName("reference")
        val reference: String?,
        @SerializedName("requester_id")
        val requesterId: Int?,
        @SerializedName("requester_type")
        val requesterType: String?,
        @SerializedName("source")
        val source: String?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("updated_at")
        val updatedAt: String?
    )

    @Keep
    data class Requester(
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("email_verified_at")
        val emailVerifiedAt: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("property")
        val `property`: Property?,
        @SerializedName("provider")
        val provider: String?,
        @SerializedName("provider_id")
        val providerId: String?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("username")
        val username: String?,
        @SerializedName("uuid")
        val uuid: String?
    ) {
        @Keep
        data class Property(
            @SerializedName("first_name")
            val firstName: String?,
            @SerializedName("last_name")
            val lastName: String?,
            @SerializedName("phone")
            val phone: String?
        )
    }

    @Keep
    data class RequesterGuest(
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("last_name")
        val lastName: String?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("uuid")
        val uuid: String?
    )

    @Keep
    data class ShippingAddress(
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("creator_id")
        val creatorId: Int?,
        @SerializedName("creator_type")
        val creatorType: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("property")
        val `property`: Property?,
        @SerializedName("status")
        val status: Int?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("updated_at")
        val updatedAt: String?
    ) {
        @Keep
        data class Property(
            @SerializedName("address")
            val address: String?,
            @SerializedName("house")
            val house: String?,
            @SerializedName("postcode")
            val postcode: String?,
            @SerializedName("state")
            val state: String?,
            @SerializedName("town")
            val town: String?
        )
    }
}