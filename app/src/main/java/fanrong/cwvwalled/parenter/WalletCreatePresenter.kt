package fanrong.cwvwalled.parenter

import com.nos.sdk.util.WalletUtil
import fanrong.cwvwalled.litepal.GreWalletModel

class WalletCreatePresenter {

    fun getWords(result: (words: String) -> Unit) {
        result(WalletUtil.getMnemonic())
    }

    fun createWallet(words: String,
                     result: (cwvWallet: GreWalletModel?, ethWallet: GreWalletModel?) -> Unit) {

        //助记词生成公私钥地址对
        val kp = WalletUtil.getKeyPair(words)
        val cwvWalletModel = GreWalletModel("")
        cwvWalletModel.address = kp.address
        cwvWalletModel.privateKey = kp.prikey
        cwvWalletModel.pubKey = kp.pubkey
        result(cwvWalletModel,null)
    }

}