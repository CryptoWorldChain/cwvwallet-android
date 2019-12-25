package fanrong.cwvwalled.eventbus

import fanrong.cwvwalled.litepal.GreWalletModel

class HomeShowWalletEvent {
    var walletModel:GreWalletModel

    constructor(walletModel: GreWalletModel) {
        this.walletModel = walletModel
    }
}