package com.example.xprintersdk.Model.WaiterReport


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

class WaiterReport : ArrayList<WaiterReport.WaiterReportItem>(){
    @Keep
    data class WaiterReportItem(
        @SerializedName("firstName")
        val firstName: String?,
        @SerializedName("lastName")
        val lastName: String?,
        @SerializedName("loginTime")
        val loginTime: String?,
        @SerializedName("logoutTime")
        val logoutTime: String?,
        @SerializedName("orderList")
        val orderList: List<Order?>?,
        @SerializedName("password")
        val password: String?,
        @SerializedName("phone")
        val phone: String?
    ) {
        @Keep
        data class Order(
            @SerializedName("orderid")
            val orderid: Int?
        )
    }
}