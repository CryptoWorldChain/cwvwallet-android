package fanrong.cwvwalled

import com.google.gson.Gson
import fanrong.cwvwalled.common.ThreadExecutor
import fanrong.cwvwalled.http.model.UpdateResp
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.utils.SWLog
import org.cwv.client.sdk.Config
import org.cwv.client.sdk.HiChain
import org.cwv.client.sdk.model.TransferInfo
import org.cwv.client.sdk.util.WalletUtil
import org.junit.Test

import org.junit.Assert.*
import xianchao.com.basiclib.BasicLibComponant
import java.lang.RuntimeException
import java.util.*
import java.util.zip.DataFormatException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * dish ocean cushion wedding grant another exchange common visa heavy vast clarify
 * f084bdda314a69e08af5a3a7ccee3c4ee339e602
 * 7b7eea34454afd9607339fdc236c6dfe8707ae40f140f7c7ff5c58836636541f
 *
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    var chartXText = arrayOfNulls<String>(6)

//    satisfy zoo jar victory couch clay topic slam banana lecture spoil goose
//    KeyPairs{pubkey='f005164389534df90d6937e134a656a2f478ea1ed3689a932012adc8a555e81de11583b2325306fb1bbfd0fb808774831385fae7d8c97afe372d4d0a7632ebbf', prikey='1557105bb772abf3c8e0ccd7a13f63f016f2bc3e24c42c64a9f7075acb925de4',
//    address='0e821881557ec50c86d7ac4ba3550fe1327d3681', bcuid='B6kzt88wYGUAdYxXEq9LFhWF5Ut9'}
//    goodand[C@419c5f1a

//    jelly shy silent chat piano cloud follow slender issue icon reveal region
//    KeyPairs{pubkey='0a5cd430fafc986fc3d5b7c355bc9df3543d3de75e4bb65955db40bfa15753bbb611b0b6ec8861a1828a615246729b13d21ac4b4f23fd31dc734c38bee38fa38', prikey='198a6986b97de1d43523a498b74916c03d6b7ffbfc815aa3bf62b5dfdf082e9a',
//    address='9bba8c8eb30742f9aeef87746f8127f3217f09db', bcuid='LsNyhhau5MqH2NqLtMnsZLr5U2l2'}


    @Test
    fun addition_isCorrect() {

//        val mnemonic = WalletUtil.getMnemonic()
//        println(mnemonic)
//        println(WalletUtil.getKeyPair(mnemonic).toString())

        Config.changeHost("http://114.115.166.19:8000")
        val userAccountInfo = HiChain.getUserAccountInfo("f084bdda314a69e08af5a3a7ccee3c4ee339e602")
        println(userAccountInfo)

        val aaaa = Aaaa()
        aaaa.change(aaaa.good, aaaa.ch)

        println(aaaa.good + "and" + aaaa.ch.toString())

    }

    @Test
    fun testBalance() {

        Config.changeHost("http://114.115.204.97:38000")
        val userAccountInfo = HiChain.getUserAccountInfo("f084bdda314a69e08af5a3a7ccee3c4ee339e602")
        println(userAccountInfo)
    }


    @Test
    fun testTransMain() {
        Config.changeHost("http://114.115.204.97:38000")
        HiChain.init("f084bdda314a69e08af5a3a7ccee3c4ee339e602")
        var ti = TransferInfo()
        ti.setToAddr("0e821881557ec50c86d7ac4ba3550fe1327d3681")
        ti.setAmount("10000000000000")
//        ti.setToken("CWV")

        var outs = arrayListOf<TransferInfo>()
        outs.add(ti)

        var result = HiChain.transferTo("f084bdda314a69e08af5a3a7ccee3c4ee339e602",
                "7b7eea34454afd9607339fdc236c6dfe8707ae40f140f7c7ff5c58836636541f", "", outs)
        println(result)
    }


}
