package net.sourceforge.http.model

import java.io.Serializable

data class OrderAppealReq(var orderNo: String) : Serializable {

    var contactWay: String? = null
    var remark: String? = null

}

data class OrderAppealResp(var msg: String) : Serializable {
    var errcode: String? = null
}