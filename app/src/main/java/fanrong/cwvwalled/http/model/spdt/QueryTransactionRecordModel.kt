package net.sourceforge.http.model.spdt

import java.io.Serializable

data class TransactionRecordReq(var dapp_id: String) : Serializable {
    var node_url: String? = null
    var account_addr: String? = null
    var contract_addr: String? = null
    var from_addr: String? = null
    var to_addr: String? = null
    var limit: String? = null
    var page_num: String? = null
    var tx_type: String? = null

}

data class TransactionRecordResp(var msg: String) : Serializable {
    var err_code: String? = null
    var total_rows: String? = null
    var total_pages: String? = null
    var page_num: String? = null
    var tx_array: MutableList<TransRecordItem>? = null
}

data class TransRecordItem(
        val tx_type: String
) : Serializable {
    val contract_addr: String? = null
    val created_time: Long? = null
    val des: String? = null
    val err_msg: String? = null
    val from_addr: String? = null
    val signed_message: String? = null
    val to_addr: String? = null
    val tx_id: String? = null
    val value: String? = null
    val tx_status: String? = null
    val block_number: String? = null
    var gas_used: String? = null
    var cumulative_gas_used: String? = null
    var ex_data: String? = null
}

