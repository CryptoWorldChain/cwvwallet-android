package net.sourceforge.http.model.spdt

import java.io.Serializable

data class HomeAdvertResp(
        val msg: String
) : Serializable {
    var banners: List<Banner>? = null
    var err_code: String? = null
}

data class Banner(
        val url: String
) : Serializable {
    var desc: String? = null
    var img: String? = null
    var seq: Int? = null
    var title: String? = null

}