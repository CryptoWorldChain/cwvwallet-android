package net.sourceforge.http.model

import java.io.Serializable

data class TransactionReq(
        var ugOtcAdvertId: String
        , var number: String
        , var type: String
        , var userId: String
) : Serializable {
    var paymentway: String? = null
}

data class TransantionResp(
        var errcode: String
        , var orderId: String
        , var orderNo: String
        , var orderAmount: String
        , var orderPrice: String
        , var orderNumber: String
        , var createdTime: String
        , var prompt: String
        , var paymentNumber: String
        , var msg: String
) : Serializable


// 获取手机验证码
data class SendMsgReq(
        var countrycode: String
        , var phone: String
        , var type: String
        , var reqresource: String
        , var language: String
) : Serializable

data class SendMsgResp(
        var errcode: String
) : Serializable


// 绑定手机号
data class BindPhoneReq(
        var countrycode: String
        , var phone: String
        , var code: String
        , var type: String
) : Serializable

data class BindPhoneResp(
        var errcode: String
        , var userid: String
) : Serializable {
    var msg: String? = null
}


// 检查实名认证状态
data class CheckRealNameReq(
        var userId: String
) : Serializable

data class CheckRealNameResp(
        var errcode: String
        , var msg: String
) : Serializable


data class AuthRealNameReq(
        var realname: String
        , var certificateno: String
        , var type: String
        , var userId: String

) : Serializable

data class AuthRealNameResp(
        var errcode: String
) : Serializable


//
data class OderListReq(
        var type: String
        , var pageno: String
        , var pagesize: String
        , var userId: String
) : Serializable

data class OderListResp(
        var msg: String
) : Serializable {
    var errcode: String? = null
    var page: Page? = null
    var userOrder: MutableList<UserOrder>? = null
}

data class Page(var sum: String) : Serializable {
    var pageno: String? = null
    var pagesize: String? = null
}

data class UserOrder(var otcOrderId: String) : Serializable {
    var orderNo: String? = null
    var advertId: String? = null
    var buyUserId: String? = null
    var sellUserId: String? = null
    var number: String? = null
    var price: String? = null
    var brokerage: String? = null
    var status: String? = null
    var createdTime: String? = null
    var isEvaluation: String? = null
    var convertRmb: String? = null
    var orderAmount: String? = null
    var paymentNumber: String? = null
    // 1去支付，2去转账
    var way: String? = null
    var endTime: String? = null
    var paymentway: String? = null
}


data class PayModeListReq(
        var userId: String
) : Serializable

data class PayModeResp(
        var msg: String
) : Serializable {
    var errcode: String? = null
    var paymentWay: List<PaymentWay>? = null
}

data class PaymentWay(var paymentWay: String) : Serializable {
    var paymentWayId: String? = null
    var name: String? = null
    var accountOpenBank: String? = null
    var status: String? = null
    var QRCode: String? = null
}


data class AddPayModeReq(
        var userId: String

) : Serializable {
    var paymentWay: String? = null
    var name: String? = null
    var QRCode: String? = null
    var accountOpenBank: String? = null

}

data class AddPayModeResp(var msg: String) : Serializable {
    var errcode: String? = null


}

data class FindItemModel(var titles: String, var dess: String, var img: Int, var url: String) : Serializable {
    override fun toString(): String {
        return "FindItemModel(titles='$titles', dess='$dess', img='$img', url='$url')"
    }
}















