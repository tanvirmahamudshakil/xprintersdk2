package com.example.xprintersdk.LabelPrinter.utils

import android.content.Context
import android.widget.Toast


object UIUtils {
    fun toast(strRes: Int, context: Context) {
        Toast.makeText(context, strRes, Toast.LENGTH_SHORT).show()
    }

    fun toast(str: String,context: Context) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }
}