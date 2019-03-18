package fanrong.cwvwalled.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.yanzhenjie.recyclerview.swipe.*
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.litepal.GreNodeModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.ui.adapter.NodeListAdapter
import fanrong.cwvwalled.ui.view.AddNodeDialog
import kotlinx.android.synthetic.main.activity_node_list.*
import net.sourceforge.listener.FRItemClickListener
import net.sourceforge.presenter.NodeListETHPresenter
import net.sourceforge.presenter.NodeListPresenter
import net.sourceforge.presenter.NodeListCWVPresenter

class NodeListActivity : BaseActivity() {

    var nodeType: String? = null
    var nodes = mutableListOf<GreNodeModel>()

    lateinit var presenter: NodeListPresenter

    private lateinit var nodeListAdapter: NodeListAdapter


    override fun getLayoutId(): Int {
        return R.layout.activity_node_list
    }

    override fun onClick(v: View) {
    }

    override fun loadData() {
        nodes.clear()
        nodes.addAll(presenter.loadData())
        nodeListAdapter?.nodes = nodes
        nodeListAdapter?.notifyDataSetChanged()
    }

    override fun initView() {

        nodeType = intent?.extras?.getString(PageParamter.PAREMTER_NODE_TYPE)

        setLeftImgOnclickListener { finish() }
        if ("ETH".equals(nodeType)) {
            setTitleText("ETH")
            presenter = NodeListETHPresenter()
        } else {
            setTitleText("CWV")
            presenter = NodeListCWVPresenter()
        }

        ll_bottom_btn.setOnClickListener {
            showNodeDialog(null)
        }


        rv_recyclerview.setSwipeMenuCreator(menuCreate)
        rv_recyclerview.setSwipeMenuItemClickListener(menuClick)

        val layoutManager = LinearLayoutManager(this)
        rv_recyclerview.layoutManager = layoutManager
        rv_recyclerview.overScrollMode = View.OVER_SCROLL_NEVER
        nodeListAdapter = NodeListAdapter()
        rv_recyclerview.adapter = nodeListAdapter

        nodeListAdapter?.itemClickListener = object : FRItemClickListener {
            override fun itemClick(position: Int) {
                nodeListAdapter.selectedIndex = position
                nodeListAdapter.notifyDataSetChanged()
                GreNodeOperator.restoreGnodeModels(nodeListAdapter.nodes)

                presenter.changeUsingNode(nodeListAdapter.nodes!![position])
            }
        }
        loadData()

    }

    private fun showNodeDialog(model: GreNodeModel?) {
        var nodeDialog = AddNodeDialog(NodeListActivity@ this)
        if (model != null) {
            nodeDialog.node_url = model.node_url
        }
        nodeDialog.btnlistener = object : FRDialogBtnListener {
            override fun onCancel(dialog: Dialog) {
                dialog.dismiss()
            }

            override fun onConfirm(dialog: Dialog) {
                dialog.dismiss()
                if (model != null) {
                    model.node_url = (dialog as AddNodeDialog).node_url
                    model.node_name = nodeType
                    model.is_def = false
                    GreNodeOperator.insertOrReplace(model)
                } else {
                    var newModel = GreNodeModel((dialog as AddNodeDialog).node_url)
                    newModel.node_name = nodeType
                    newModel.is_def = false
                    GreNodeOperator.insert(newModel)
                }

                loadData()
            }
        }

        if (model != null) {
            nodeDialog.node_url = (model.node_url)
        }
        nodeDialog.show()

    }


    var menuCreate = object : SwipeMenuCreator {
        override fun onCreateMenu(leftMenu: SwipeMenu, rightMenu: SwipeMenu, position: Int) {

            if (nodeListAdapter.nodes?.get(position)!!.is_def!!) {
                return
            }

            val compileItem = SwipeMenuItem(this@NodeListActivity)
            compileItem.text = "编辑"
            compileItem.setTextColor(Color.parseColor("#ffffff"))
            compileItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            compileItem.setWidth(getResources().getDimension(R.dimen.pay_mode_item_menu_widht).toInt())
            compileItem.setBackgroundColor(Color.parseColor("#A1A1A1"))
            rightMenu.addMenuItem(compileItem)

            val deleteItem = SwipeMenuItem(this@NodeListActivity)
            deleteItem.text = "删除"
            deleteItem.setTextColor(Color.parseColor("#ffffff"))
            deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            deleteItem.setWidth(getResources().getDimension(R.dimen.pay_mode_item_menu_widht).toInt())
            deleteItem.setBackgroundColor(Color.parseColor("#D7390D"))
            rightMenu.addMenuItem(deleteItem)
        }

    }

    var menuClick = object : SwipeMenuItemClickListener {
        override fun onItemClick(menuBridge: SwipeMenuBridge, position: Int) {


//                menuBridge.position
            if (nodeListAdapter.nodes?.get(position)!!.is_def!!) {
                showTopMsg("仅支持操作自定义节点")
                menuBridge.closeMenu()
                return
            } else if (nodeListAdapter.nodes?.get(position)!!.isUsing!!) {
                showTopMsg("使用中的节点不支持操作")
                menuBridge.closeMenu()
                return
            } else if (nodeListAdapter.nodes?.get(position)!!.isFromService!!) {
                showTopMsg("仅支持操作自定义节点")
                menuBridge.closeMenu()
                return
            }


            if (menuBridge.position == 0) {
                // 编辑
                showNodeDialog(nodeListAdapter.nodes!!.get(position))
            } else {
                // 删除
                var gnodeModel = nodeListAdapter.nodes?.get(position)
                nodeListAdapter.nodes!!.remove(gnodeModel)
                nodeListAdapter.notifyDataSetChanged()
                GreNodeOperator.delete(gnodeModel)
            }
            menuBridge.closeMenu()
        }
    }

}
