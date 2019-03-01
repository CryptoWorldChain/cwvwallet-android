package net.sourceforge.http.model

import fanrong.cwvwalled.base.Constants
import java.io.Serializable

data class QueryOrderReq(var orderId: String) : Serializable {
    var dapp_id: String = Constants.DAPP_ID
}

data class QueryOrderResp(
        val errcode: String
) : Serializable {
    val accountAddress: String? = null
    val advertId: String? = null
    val brokerage: String? = null
    val buyUserId: String? = null
    val createdTime: String? = null
    val isEvaluation: String? = null
    val msg: String? = null
    val number: String? = null
    val orderNo: String? = null
    val otcOrderId: String? = null
    val paymentNumber: String? = null
    val paymentWay: List<OrderPaymentWay>? = null
    val price: String? = null
    val prompt: String? = null
    val sellUserId: String? = null
    val status: String? = null

}

data class OrderPaymentWay(
        val name: String
) : Serializable {
    val account: String? = null
    val QRCode: String? = null
    val paymentWay: String? = null
    val paymentWayId: String? = null
    val status: String? = null
}













