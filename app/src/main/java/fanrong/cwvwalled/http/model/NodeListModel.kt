package fanrong.cwvwalled.http.model

import fanrong.cwvwalled.base.Constants
import java.io.Serializable

data class NodeListReq(var node_net: String) : Serializable {
    var dapp_id: String = Constants.DAPP_ID

}

data class NodeListResp(var err_code: String) : Serializable {

    var msg: String? = null
    var dev_net: List<NodeModel>? = null
    var test_net: List<NodeModel>? = null
    var main_net: List<NodeModel>? = null
}

data class NodeModel(
        val node_url: String
) : Serializable {
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

}