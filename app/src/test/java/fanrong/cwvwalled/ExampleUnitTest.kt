package fanrong.cwvwalled

import com.google.gson.Gson
import fanrong.cwvwalled.http.model.UpdateResp
import fanrong.cwvwalled.utils.SWLog
import org.cwv.client.sdk.util.WalletUtil
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import java.util.zip.DataFormatException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    var chartXText = arrayOfNulls<String>(6)

//    satisfy zoo jar victory couch clay topic slam banana lecture spoil goose
//    KeyPairs{pubkey='f005164389534df90d6937e134a656a2f478ea1ed3689a932012adc8a555e81de11583b2325306fb1bbfd0fb808774831385fae7d8c97afe372d4d0a7632ebbf', prikey='1557105bb772abf3c8e0ccd7a13f63f016f2bc3e24c42c64a9f7075acb925de4', address='0e821881557ec50c86d7ac4ba3550fe1327d3681', bcuid='B6kzt88wYGUAdYxXEq9LFhWF5Ut9'}
//    goodand[C@419c5f1a


    @Test
    fun addition_isCorrect() {

        val mnemonic = WalletUtil.getMnemonic()
        println(mnemonic)
        println(WalletUtil.getKeyPair(mnemonic).toString())

        val aaaa = Aaaa()
        aaaa.change(aaaa.good,aaaa.ch)

        println(aaaa.good + "and" + aaaa.ch.toString())

    }


}
