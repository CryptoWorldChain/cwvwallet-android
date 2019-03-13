package net.sourceforge.presenter

import fanrong.cwvwalled.eventbus.CWVNoteChangeEvent
import fanrong.cwvwalled.litepal.GreNodeModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import org.greenrobot.eventbus.EventBus

class NodeListCWVPresenter : NodeListPresenter {
    override fun loadData(): MutableList<GreNodeModel> {
        return GreNodeOperator.queryAllCWVnode()
    }

    override fun changeUsingNode(gnodeModel: GreNodeModel) {
        var event = CWVNoteChangeEvent()
        event.gnodeModel = gnodeModel
        EventBus.getDefault().post(event)
    }
}