package fanrong.cwvwalled.ui.activity

import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreNodeOperator
import kotlinx.android.synthetic.main.activity_node_config.*

class NodeConfigActivity : BaseActivity() {



    override fun getLayoutId(): Int {
        return R.layout.activity_node_config
    }

    override fun initView() {
        setTitleText("节点设置")
        setLeftImgOnclickListener { finish() }
        tv_eth_config.setOnClickListener(this)
        tv_cwv_config.setOnClickListener(this)

        val using = GreNodeOperator.queryETHnode()
        tv_eth_config.text = using.node_url
        val cwvNodeUsing = GreNodeOperator.queryCWVnode()
        tv_cwv_config.text = cwvNodeUsing.node_url



        tv_eth_config.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(PageParamter.PAREMTER_NODE_TYPE, "ETH")
            startActivity(NodeListActivity::class.java, bundle)
        }
        tv_cwv_config.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(PageParamter.PAREMTER_NODE_TYPE, "CWV")
            startActivity(NodeListActivity::class.java, bundle)
        }
    }

    override fun onClick(v: View) {
    }

    override fun loadData() {
    }

    override fun onRestart() {
        super.onRestart()
        val using = GreNodeOperator.queryETHnode()
        tv_eth_config.text = using.node_url
        val cwvNodeUsing = GreNodeOperator.queryCWVnode()
        tv_cwv_config.text = cwvNodeUsing.node_url
    }
}
