package net.sourceforge.http.model.spdt

import java.io.Serializable


data class QueryOtherServiceResp(
        val msg: String
) : Serializable {

    var apps: List<OtherService>? = null
    var err_code: String? = null
}

data class OtherService(
        val url: String
) : Serializable {

    val desc: String? = null
    val icon: String? = null
    val seq: Int? = null
    val title: String? = null
}
