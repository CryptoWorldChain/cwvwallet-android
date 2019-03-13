package fanrong.cwvwalled.base

import fanrong.cwvwalled.utils.PreferenceHelper

object Constants {
    const val DAPP_ID = "CWV"
    var chain_Id = "1"
        get() {
            return "" + PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.CHAIN_ID, "1")
        }
        set(value) {
            field = value
            PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.CHAIN_ID, value)
        }
}