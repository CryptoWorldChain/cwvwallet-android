package fanrong.cwvwalled.common

import android.text.TextUtils
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.PreferenceHelper
import net.sourceforge.http.model.CheckRealNameReq
import net.sourceforge.http.model.CheckRealNameResp
import org.litepal.LitePal
import retrofit2.Call
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils

class UserInfoObject {

    companion object {

        var password: String = ""
            get() {
                return PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.PASSWORD, "")
            }

        var userid: String = ""
            set(value) {
                field = value
                saveUserId(value)
                // 设置userId 更新实名认证状态
                checkRealNameState()
            }


        var nickName: String = ""
            get() {
                return PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.NICK_NAME, "")
            }
            set(value) {
                field = value
                saveNickName(value)
            }
        var userHeader: String = ""
            set(value) {
                field = value
                saveHeaderPath(value)
            }

        /**
         * 1是待审核，0是未审核 3是审核通过
         */
        var authNameState = "-1"
            set(value) {
                field = value
                // 发送个消息 ，实名认证状态改变
//            EventBus.getDefault().post(RealNameEvent())
            }

        fun init() {
            userid = PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.USER_ID, "")
            password = PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.PASSWORD, "")
            nickName = PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.NICK_NAME, "")
            userHeader = PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.HEADER_PATH, "")
            if (!TextUtils.isEmpty(userid)) {
                checkRealNameState()
            }
        }

        fun checkRealNameState() {
            val req = CheckRealNameReq(userId = userid)
            RetrofitClient.getOTCNetWorkApi()
                    .checkRealName(ConvertToBody.ConvertToBody(req))
                    .enqueue(object : retrofit2.Callback<CheckRealNameResp> {
                        override fun onFailure(call: Call<CheckRealNameResp>, t: Throwable) {
                        }

                        override fun onResponse(call: Call<CheckRealNameResp>, response: Response<CheckRealNameResp>) {
                            authNameState = response.body()?.errcode!!
                        }

                    })
        }


        fun isPhoneLogin(): Boolean {
            return !CheckedUtils.isEmpty(userid)
        }


        fun isRealName(): Boolean {
            return "3".equals(authNameState)
        }


        fun userLogout() {
            PreferenceHelper.getInstance().clear()
            LitePal.deleteAll(GreWalletModel::class.java)
            LitePal.deleteAll(LiteCoinBeanModel::class.java)
        }

        private fun saveNickName(value: String) {
            PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.NICK_NAME, value)
        }

        private fun saveHeaderPath(value: String) {
            PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.HEADER_PATH, value)
        }

        private fun saveUserId(value: String) {
            PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.USER_ID, value)
        }

    }

}