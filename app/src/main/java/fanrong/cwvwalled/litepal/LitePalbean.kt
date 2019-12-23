package fanrong.cwvwalled.litepal

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable


data class AddressModel(val name: String, val address: String, var remark: String = "") : LitePalSupport(), Serializable {
    val id: Long = 0
    override fun toString(): String {
        return "AddressModel(name='$name', address='$address', remark='$remark', id=$id)"
    }
}


data class GreNodeModel(var node_url: String) : LitePalSupport() {
    var isUsing: Boolean? = null
        get() {
            if (field == null) {
                return false
            }
            return field
        }

    var node_des: String? = null
    var node_name: String? = null
    var node_net: String? = null
    var is_def: Boolean? = null

        get() {
            if (field == null) {
                return false
            }
            return field
        }
    var id: Long = 0
    var isFromService: Boolean? = null
        get() {
            return if (field == null) false else field
        }


    override fun toString(): String {
        return "GreNodeModel(node_url='$node_url', isUsing=$isUsing, node_des='$node_des', node_name='$node_name', node_net='$node_net', is_def=$is_def, id=$id)"
    }
}


data class GreWalletModel(@Column(unique = true) var address: String) : LitePalSupport(), Serializable {
    var id: Long = 0
    var pubKey: String? = null
    var privateKey: String? = null
    var walletName: String? = null
    var balance: String? = null

    var rmb: String? = null
    var mnemonic: String? = null
    var walletType: String? = null

    public var isShowRmb: Boolean? = null
        get() {
            return if (field == null) true else field
        }

    var isImport: Boolean? = null
        get() {
            if (field == null) {
                return false
            }
            return field
        }

    override fun toString(): String {
        return "GreWalletModel(address='$address', pubKey='$pubKey', privateKey='$privateKey', walletName='$walletName', balance='$balance', id=$id)"
    }
}


data class LiteCoinBeanModel(var coin_name: String) : LitePalSupport(), Serializable {

    var id: Long = 0
    var contract_addr: String? = null
    var channel_name: String? = null
    var coin_symbol: String? = null
    var coin_decimals: String? = null
    var coin_total_supply: String? = null
    var coin_icon: String? = null
    var single_max_amt: String? = null
    var single_min_amt: String? = null
    var count: String? = null
    var countCNY: String? = null
    var sourceAddr: String? = null
    var walletName: String? = null

    override fun toString(): String {
        return "LiteCoinBeanModel(coin_name='$coin_name', id=$id, contract_addr=$contract_addr, channel_name=$channel_name, coin_symbol=$coin_symbol, coin_decimals=$coin_decimals, coin_total_supply=$coin_total_supply, coin_icon=$coin_icon, single_max_amt=$single_max_amt, single_min_amt=$single_min_amt, count=$count, countCNY=$countCNY)"
    }



}

class TokenInfo :  LitePalSupport(), Serializable  {
    var tokenName: String? = null
    var tokenAddress: String? = null
    //非接口返回
    var tokenType : String ="CWV"
    var sourceAddr: String? = null
    var isOpen: Boolean? = null

        get() {
            if (field == null) {
                field = false
            }
            return field
        }
}



