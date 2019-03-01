package net.sourceforge.http.model

import fanrong.cwvwalled.base.Constants
import java.io.Serializable

/**
 * 请求币种信息接口，请求参数，接口返回
 */
data class QueryCoinTypeReq(var node_url: String) : Serializable {
    var coin_name: String? = null
    var contract_addr: String? = null
    var dapp_id = Constants.DAPP_ID
}

data class QueryCoinTypeResp(var msg: String) : Serializable {
    var err_code: String? = null
    var token: List<CoinBean>? = null
}

data class CoinBean(var coin_name: String) : Serializable {
    var contract_addr: String? = null
    var channel_name: String? = null
    var coin_symbol: String? = null
    var coin_decimals: String? = null
    var coin_total_supply: String? = null
    var coin_icon: String? = null
    var single_max_amt: String? = null
    var single_min_amt: String? = null
    var sourceAddr: String? = null
    // 非接口返回，，用于adapter 判断 switch
    var isOpen: Boolean? = null
        get() {
            if (field == null) {
                field = false
            }
            return field
        }
}