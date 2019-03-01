package fanrong.cwvwalled.ui.activity

import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.NodeListReq
import fanrong.cwvwalled.http.model.NodeListResp
import fanrong.cwvwalled.utils.BgUtils
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils

class SplashActivity : BaseActivity() {

    override fun loadData() {

        requestETHNodeList()
        requestFBCNodeList()
    }

    override fun onClick(v: View) {
        if (checkExistWallet()) {
            startActivity(MainActivity::class.java)
        } else {
            startActivity(CreateAccountPreActivity::class.java)
        }
        finish()

    }

    override fun initView() {
        contentView.setBackgroundResource(BgUtils.getBg(BgUtils.YINDAO))
        iv_tonext.setOnClickListener(this)
        loadData()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }


    private fun checkExistWallet(): Boolean {
        if (CheckedUtils.nonEmpty(GreWalletOperator.queryAll())) {
            return true
        }
        return false
    }

    fun requestETHNodeList() {
        val req = NodeListReq("dev")
        RetrofitClient.getETHNetWorkApi()
                .requestNodeList(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<NodeListResp> {

                    override fun onFailure(call: Call<NodeListResp>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<NodeListResp>, response: Response<NodeListResp>) {
                        val body = response.body()!!
                        if ("1".equals(body.err_code)) {
                            if (CheckedUtils.nonEmpty(body.dev_net)) {
                                val nodeModel = GreNodeOperator.copyFromNodeModel(body.dev_net!![0])
                                nodeModel.node_name = "ETH"
                                GreNodeOperator.insert(nodeModel)
                            }
                        }
                    }

                })


    }

    fun requestFBCNodeList() {

        val req = NodeListReq("dev")
        RetrofitClient.getFBCNetWorkApi()
                .requestNodeList(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<NodeListResp> {

                    override fun onFailure(call: Call<NodeListResp>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<NodeListResp>, response: Response<NodeListResp>) {
                        val body = response.body()!!
                        if ("1".equals(body.err_code)) {
                            if (CheckedUtils.nonEmpty(body.dev_net)) {
                                val nodeModel = GreNodeOperator.copyFromNodeModel(body.dev_net!![0])
                                nodeModel.node_name = "CWV"
                                GreNodeOperator.insert(nodeModel)
                            }
                        }
                    }

                })
    }

}
