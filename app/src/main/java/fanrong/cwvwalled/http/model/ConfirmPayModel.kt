package net.sourceforge.http.model

import java.io.Serializable

data class ConfirmPayReq(var orderNo: String, var paymentWay: String) : Serializable {
    var userId: String? = null
}

data class ConfirmPayResp(
        val errcode: String
) : Serializable {
    val msg: String? = null
    val orderAmount: String? = null
    val orderNo: String? = null
    val orderNumber: String? = null
    val orderPrice: String? = null
    val pamentWay: String? = null
    val paymentNumber: String? = null
    val paymentTime: String? = null
    val prompt: String? = null
}













