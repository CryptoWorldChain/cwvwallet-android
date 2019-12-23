package net.sourceforge.http.model.spdt

import org.cwv.client.sdk.HiChain
import xianchao.com.basiclib.utils.checkIsEmpty
import java.io.Serializable

data class TransactionRecordReq(var dapp_id: String) : Serializable {
    var coinType: String? = null
    var limit: String? = null
    var skip: String? = null
    var page_num: String? = null
    var address: String? = null

    var node_url: String? = null
    var account_addr: String? = null
    var contract_addr: String? = null
    var tx_type: String? = null
    var from_addr: String? = null
    var to_addr: String? = null

}

data class TransactionRecordResp(var msg: String) : Serializable {
    var err_code: String? = null
    var total_rows: String? = null
    //    var total_pages: String? = null
//    var page_num: String? = null
//    var tx_array: MutableList<TransRecordItem>? = null
    var walletTrans: MutableList<TransRecordItem>? = null
}

data class TransRecordItem(
        val tx_type: String
) : Serializable {
    var collectionAddress: MutableList<String>? = null
    var sendAddress: MutableList<String>? = null
    val transHash: String? = null
    val blockHeight: String? = null
    val transTime: String? = null
    val amount: Long? = null
    val status: String? = null
    var exData: String? = null
        get() {
            return HiChain.hexStringToUTF8(field)
        }

    val contract_addr: String? = null
    val des: String? = null
    val err_msg: String? = null
    val from_addr: String? = null
    val signed_message: String? = null
    val to_addr: String? = null
    val tx_id: String? = null
    val value: String? = null
    val tx_status: String? = null
    var gas_used: String? = null
    var cumulative_gas_used: String? = null

    fun isTransOut(address: String?): Boolean {
        if (address.checkIsEmpty()) {
            return false
        }
        var sendAdd = if (sendAddress.checkIsEmpty()) "" else sendAddress!![0]
        return address?.equals(sendAdd) ?: false
    }

    fun getTransOutAddr(): String {
        return if (sendAddress.checkIsEmpty()) "" else sendAddress!![0]
    }

    fun getTransInAddr(): String {
        return if (collectionAddress.checkIsEmpty()) "" else collectionAddress!![0]
    }

}

