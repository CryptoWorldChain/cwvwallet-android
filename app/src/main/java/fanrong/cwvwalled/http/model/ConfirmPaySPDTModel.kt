package net.sourceforge.http.model

import java.io.Serializable

data class ConfirmPaySPDTReq(var userId: String) : Serializable {
    var orderNo: String? = null
}

data class ConfirmPaySPDTResp(var msg: String) : Serializable {

}