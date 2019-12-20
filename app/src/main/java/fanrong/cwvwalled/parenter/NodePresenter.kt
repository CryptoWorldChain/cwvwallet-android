package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.eventbus.CWVNoteChangeEvent
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.NodeListReq
import fanrong.cwvwalled.http.model.NodeListResp
import fanrong.cwvwalled.litepal.GreNodeModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.utils.SWLog
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils
import java.util.prefs.NodeChangeEvent

class NodePresenter {

    fun initCWVnode() {

//        test 节点 http://114.115.166.19:8000/fbs

        LitePal.deleteAll(GreNodeModel::class.java)
        if (CheckedUtils.nonEmpty(GreNodeOperator.queryAllCWVnode())) {
            SWLog.e("已加载，不需要在获取  cwv node")

            val cwVnode = GreNodeOperator.queryCWVnode()
            val event = CWVNoteChangeEvent()
            event.gnodeModel = cwVnode
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

                                    // 发送事件修改 node 配置
                                    val event = CWVNoteChangeEvent()
                                    event.gnodeModel = nodeModel
                                    EventBus.getDefault().post(event)
                                }
                            }
                        }
                    }

                })

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

}