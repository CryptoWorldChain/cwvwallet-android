package net.sourceforge.presenter

import fanrong.cwvwalled.litepal.GreNodeModel

interface NodeListPresenter {
    fun loadData(): MutableList<GreNodeModel>

    fun changeUsingNode(gnodeModel: GreNodeModel)

}