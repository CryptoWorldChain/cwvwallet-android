package net.sourceforge.presenter

import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.eventbus.ETHNoteChangeEvent
import fanrong.cwvwalled.litepal.GreNodeModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import org.greenrobot.eventbus.EventBus

class NodeListETHPresenter : NodeListPresenter {
    override fun loadData(): MutableList<GreNodeModel> {
        return GreNodeOperator.queryAllETHnode()
    }

    override fun changeUsingNode(gnodeModel: GreNodeModel) {
        if (gnodeModel.isFromService!!) {
            Constants.chain_Id = "1"

        } else {
            Constants.chain_Id = "1337"
        }
        var event = ETHNoteChangeEvent()
        event.gnodeModel = gnodeModel
        EventBus.getDefault().post(event)
    }
}