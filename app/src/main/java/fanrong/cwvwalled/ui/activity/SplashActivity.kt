package fanrong.cwvwalled.ui.activity

import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.UserInfoObject
import fanrong.cwvwalled.eventbus.CWVNoteChangeEvent
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.NodeListReq
import fanrong.cwvwalled.http.model.NodeListResp
import fanrong.cwvwalled.litepal.GreNodeModel
import fanrong.cwvwalled.utils.BgUtils
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.activity_splash.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils
import java.util.prefs.NodeChangeEvent

class SplashActivity : BaseActivity() {

    override fun loadData() {

//        requestETHNodeList()
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
        UserInfoObject.init()
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

        if (CheckedUtils.nonEmpty(GreNodeOperator.queryAllETHnode())) {
            SWLog.e("已加载，不需要在获取  eth node")
            return
        }

        val req = NodeListReq("main")
        RetrofitClient.getETHNetWorkApi()
                .requestNodeList(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<NodeListResp> {

                    override fun onFailure(call: Call<NodeListResp>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<NodeListResp>, response: Response<NodeListResp>) {
                        val body = response.body()!!
                        if ("1".equals(body.err_code)) {
                            if (CheckedUtils.nonEmpty(body.nodeInfo)) {
                                val main_net = body.nodeInfo

                                main_net!!.forEach {
                                    val nodeModel = GreNodeOperator.copyFromNodeModel(it)
                                    nodeModel.node_name = "ETH"
                                    nodeModel.isUsing = nodeModel.is_def
                                    nodeModel.isFromService = true
                                    GreNodeOperator.insert(nodeModel)
                                }
                            }
                        }
                    }

                })


    }

    fun requestFBCNodeList() {

        if (CheckedUtils.nonEmpty(GreNodeOperator.queryAllCWVnode())) {
            SWLog.e("已加载，不需要在获取  cwv node")
            val event = CWVNoteChangeEvent()
            event.gnodeModel = GreNodeOperator.queryCWVnode()
            EventBus.getDefault().post(event)
            return
        }


        val req = NodeListReq("main")
        RetrofitClient.getFBCNetWorkApi()
                .requestNodeList(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<NodeListResp> {

                    override fun onFailure(call: Call<NodeListResp>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<NodeListResp>, response: Response<NodeListResp>) {
                        val body = response.body()!!
                        if ("1".equals(body.err_code)) {
                            if (CheckedUtils.nonEmpty(body.nodeInfo)) {
                                val main_net = body.nodeInfo

                                main_net!!.forEach {
                                    val nodeModel = GreNodeOperator.copyFromNodeModel(it)
                                    nodeModel.node_name = "CWV"
                                    nodeModel.isUsing = main_net.indexOf(it) == 0
                                    nodeModel.is_def = main_net.indexOf(it) == 0
                                    nodeModel.isFromService = true
                                    GreNodeOperator.insert(nodeModel)
                                }
                            }
                        }
                    }

                })
    }

}
