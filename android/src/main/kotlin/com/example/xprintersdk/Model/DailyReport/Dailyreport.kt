package com.example.xprintersdk.Model.DailyReport


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Dailyreport(
    @SerializedName("data")
    val `data`: Data?
) {
    @Keep
    data class Data(
        @SerializedName("total_card_order")
        val totalCardOrder: String?,
        @SerializedName("total_card_order_amount")
        val totalCardOrderAmount: Double?,
        @SerializedName("total_cash_in")
        val totalCashIn: String?,
        @SerializedName("total_cash_order")
        val totalCashOrder: String?,
        @SerializedName("total_cash_order_amount")
        val totalCashOrderAmount: Double?,
        @SerializedName("total_cash_out")
        val totalCashOut: String?,
        @SerializedName("total_local_order")
        val totalLocalOrder: String?,
        @SerializedName("total_online_order")
        val totalOnlineOrder: String?,
        @SerializedName("total_order")
        val totalOrder: String?,
        @SerializedName("total_refund")
        val totalRefund: String?,
        @SerializedName("date")
        val date: String?,
        @SerializedName("total_refund_card_Amount")
        val totalrefundcardAmount: String?,
        @SerializedName("total_refund_cash_Amount")
        val totalrefundcashAmount: String?,
        @SerializedName("total_change")
        var totalChange : String?,
        @SerializedName("totalReturn")
        val totalReturn: String?,
    )
}