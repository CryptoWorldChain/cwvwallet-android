package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.PreferenceHelper
import fanrong.cwvwalled.utils.SWLog
import org.brewchain.core.crypto.model.KeyPairs
import org.cwv.client.sdk.util.WalletUtil
import xianchao.com.basiclib.utils.checkIsEmpty

class WalletCreatePresenter {
    //生成助记词
    fun getWords(result: (words: String) -> Unit) {
        result(WalletUtil.getMnemonic())
    }

     //通过私钥导入钱包
    fun importCWVWalletFromPriKey(walletName: String, priKey: String, result: (cwvWallet: GreWalletModel?, msg: String?) -> Unit) {
        var kp: KeyPairs? = null
        try {
            kp = WalletUtil.getKeyPairFromPk(priKey)

            if (kp == null) {
                result(null, "导入失败,请检查私钥是否正确")
                return
            }

            if (GreWalletOperator.queryAddress(kp.address) != null) {
                result(null, "钱包地址已存在")
                return
            }


            val cwvWalletModel = GreWalletModel("")
            cwvWalletModel.address = kp.address
            cwvWalletModel.privateKey = kp.prikey
            cwvWalletModel.pubKey = kp.pubkey
            cwvWalletModel.isImport = true

            if (cwvWalletModel != null) {
                cwvWalletModel.walletName = "CWV-" + walletName
                cwvWalletModel.walletType = "CWV"
                cwvWalletModel.mnemonic = ""
                SWLog.e(cwvWalletModel)
                GreWalletOperator.insert(cwvWalletModel)

                val beanModel = LiteCoinBeanModel("CWV")
                beanModel.channel_name = ""
                beanModel.coin_decimals = ""
                beanModel.coin_icon = ""
                beanModel.coin_symbol = "CWV"
                beanModel.contract_addr = ""
                beanModel.sourceAddr = cwvWalletModel.address
                beanModel.walletName = cwvWalletModel.walletName
                beanModel.save()
            }

//        if (ethWallet!= null){
//            ethWallet.walletName = "ETH-" + shareData
//            ethWallet.walletType = "ETH"
//            ethWallet.mnemonic = words
//            SWLog.e(ethWallet)
//            GreWalletOperator.insert(ethWallet)
//        }
            result(cwvWalletModel, "导入成功")
        } catch (e: Exception) {
            e.printStackTrace()
            result(null, "导入失败，请检查私钥")
        }

    }
    //通过创建钱包
    fun createWallet(words: String,
                     result: (cwvWallet: GreWalletModel?, ethWallet: GreWalletModel?) -> Unit) {

        //助记词生成公私钥地址对
        var kp: KeyPairs? = null
        try {
            kp = WalletUtil.getKeyPair(words)
        } catch (ex: Exception) {
            ex.printStackTrace()
            result(null, null)
            return
        }
        if (kp == null) {
            result(null, null)
            return
        }

        val cwvWalletModel = GreWalletModel("")
        cwvWalletModel.address = kp.address
        cwvWalletModel.privateKey = kp.prikey
        cwvWalletModel.pubKey = kp.pubkey

        var shareData = PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.NICK_NAME, "AA")
        if (shareData.checkIsEmpty()) {
            shareData = "钱包"
        }
        if (cwvWalletModel != null) {
            cwvWalletModel.walletName = "CWV-" + shareData
            cwvWalletModel.walletType = "CWV"
            cwvWalletModel.mnemonic = words
            SWLog.e(cwvWalletModel)
            GreWalletOperator.insert(cwvWalletModel)

            val beanModel = LiteCoinBeanModel("CWV")
            beanModel.channel_name = "adsfa"
            beanModel.coin_decimals = "asdfa"
            beanModel.coin_icon = "dsfaf"
            beanModel.coin_symbol = "CWV"
            beanModel.contract_addr = "fdalskfjals"
            beanModel.sourceAddr = cwvWalletModel.address
            beanModel.walletName = cwvWalletModel.walletName
            beanModel.save()
        }

//        if (ethWallet!= null){
//            ethWallet.walletName = "ETH-" + shareData
//            ethWallet.walletType = "ETH"
//            ethWallet.mnemonic = words
//            SWLog.e(ethWallet)
//            GreWalletOperator.insert(ethWallet)
//        }
        result(cwvWalletModel, null)
    }
    //通过助记词导入钱包
    fun importCWVWallet(walletName: String, words: String,
                        result: (cwvWallet: GreWalletModel?, msg: String?) -> Unit) {

        //助记词生成公私钥地址对
        var kp: KeyPairs? = null
        try {
            kp = WalletUtil.getKeyPair(words)
        } catch (ex: Exception) {
            ex.printStackTrace()
            result(null, null)
            return
        }

        if (kp == null) {
            result(null, "导入失败，请检查助记词")
            return
        }

        if (GreWalletOperator.queryAddress(kp.address) != null) {
            result(null, "钱包地址已存在")
            return
        }

        val cwvWalletModel = GreWalletModel("")
        cwvWalletModel.address = kp.address
        cwvWalletModel.privateKey = kp.prikey
        cwvWalletModel.pubKey = kp.pubkey
        cwvWalletModel.isImport = true

        if (cwvWalletModel != null) {
            cwvWalletModel.walletName = "CWV-" + walletName
            cwvWalletModel.walletType = "CWV"
            cwvWalletModel.mnemonic = words
            SWLog.e(cwvWalletModel)
            GreWalletOperator.insert(cwvWalletModel)

            val beanModel = LiteCoinBeanModel("CWV")
            beanModel.channel_name = "adsfa"
            beanModel.coin_decimals = "asdfa"
            beanModel.coin_icon = "dsfaf"
            beanModel.coin_symbol = "CWV"
            beanModel.contract_addr = "fdalskfjals"
            beanModel.sourceAddr = cwvWalletModel.address
            beanModel.walletName = cwvWalletModel.walletName
            beanModel.save()
        }

//        if (ethWallet!= null){
//            ethWallet.walletName = "ETH-" + shareData
//            ethWallet.walletType = "ETH"
//            ethWallet.mnemonic = words
//            SWLog.e(ethWallet)
//            GreWalletOperator.insert(ethWallet)
//        }
        result(cwvWalletModel, "导入成功")
    }

}