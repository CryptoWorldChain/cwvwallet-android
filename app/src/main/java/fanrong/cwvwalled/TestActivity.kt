package fanrong.cwvwalled

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.webkit.ValueCallback
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.ui.activity.CreateAccountPasswordActivity
import fanrong.cwvwalled.ui.activity.CreateAccountPreActivity
import fanrong.cwvwalled.ui.activity.CreateAccountStepOneActivity
import fanrong.cwvwalled.ui.activity.MainActivity
import fanrong.cwvwalled.utils.AppManager
import fanrong.cwvwalled.utils.CallJsCodeUtils
import fanrong.cwvwalled.utils.PreferenceHelper
import fanrong.cwvwalled.utils.SWLog
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_test.*
import org.json.JSONObject
import xianchao.com.basiclib.utils.CheckedUtils

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        btn_test.setOnClickListener {
            newToNextPage()
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    val cwvWallet = GreWalletModel("")
    val ethWallet = GreWalletModel("")


    private fun newToNextPage() {
        var mnemonic = "gloom priority evidence alone believe cereal scan melody essay apple alcohol grief"

        Observable.create<String>(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                //获取主私钥
                CallJsCodeUtils.mnemonicToHDPrivateKey(mnemonic, object : ValueCallback<String> {
                    override fun onReceiveValue(mainKey: String?) {
                        SWLog.e("mainKey " + mainKey)
                        emitter.onNext(CallJsCodeUtils.readStringJsValue(mainKey))
                    }
                })
            }
        }).subscribe(object : Consumer<String> {
            override fun accept(ethPrivateKey: String?) {
                // 获取 cwv 钱包 地址和私钥
                CallJsCodeUtils.cwv_GenFromPrikey(ethPrivateKey) {
                    var realContent = it
                    if (CheckedUtils.isJson(realContent)) {
                        val jsonObject = JSONObject(realContent)
                        cwvWallet.privateKey = jsonObject.getString("hexPrikey")
                        cwvWallet.address = "0x" + jsonObject.getString("hexAddress")
                    }

                    val shareData = PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.NICK_NAME,"AA")
                    cwvWallet.walletName = "CWV-" + shareData
                    cwvWallet.walletType = "CWV"
                    cwvWallet.mnemonic = mnemonic
                    ethWallet.walletName = "ETH-" + shareData
                    ethWallet.walletType = "ETH"
                    ethWallet.mnemonic = mnemonic
                    SWLog.e(cwvWallet)
                    SWLog.e(ethWallet)
                    GreWalletOperator.insert(cwvWallet)
                    GreWalletOperator.insert(ethWallet)
                }
            }
        })
    }


}
