package net.sourceforge.http.model

import java.io.Serializable

data class ReqestUserInfoReq(var userid: String) : Serializable {

}

data class ReqestUserInfoResp(
        val errcode: String,
        val msg: String,
        val userinfo: Userinfo
) : Serializable

data class Userinfo(
        val userid: String
) : Serializable {
    val valigooglesecret: String? = null
    val valiidnumber: String? = null
    val googleverify: String? = null
    val idnumber: String? = null
    val istrpwd: String? = null
    val realname: String? = null
    val status: String? = null
    val userType: String? = null
}