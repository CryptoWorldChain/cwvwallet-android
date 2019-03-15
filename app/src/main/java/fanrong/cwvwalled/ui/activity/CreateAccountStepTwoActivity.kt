package fanrong.cwvwalled.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Dimension
import android.view.View
import android.webkit.ValueCallback
import android.widget.TextView
import com.donkingliang.labels.LabelsView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.utils.AppManager
import fanrong.cwvwalled.utils.CallJsCodeUtils
import fanrong.cwvwalled.utils.SWLog
import fanrong.cwvwalled.utils.ViewUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_create_account_step_two.*
import org.json.JSONObject
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.RandomUtils
import java.util.*

class CreateAccountStepTwoActivity : BaseActivity() {

    lateinit var mnemonic: String
    lateinit var rightList: List<String>


    override fun getLayoutId(): Int {
        return R.layout.activity_create_account_step_two
    }
    override fun initView() {
        btn_confirm.setOnClickListener(this)

        mnemonic = intent.getStringExtra(PageParamter.MNEMONIC_STR)
        rightList = mnemonic.split(" ")
        val mnemonics = mnemonic.split(" ")
        Collections.shuffle(mnemonics)

        label_unselect.setLabels(mnemonics)
        label_unselect.setOnLabelClickListener(object : LabelsView.OnLabelClickListener {
            override fun onLabelClick(label: TextView, data: Any?, position: Int) {
                // 选中的不可以点击
                if (label.parent == label_select) {
                    return
                }

                if (!rightList[12 - label_unselect.childCount].equals(label.text)) {
                    rl_error_wraper.visibility = View.VISIBLE
                    label_select.removeView(label_select.findViewWithTag<TextView>("error"))
                    label_select.addView(ViewUtils.copyTextView(label))
                } else {
                    rl_error_wraper.visibility = View.INVISIBLE
                    // 先清除掉错误的文字
                    val withTag = label_select.findViewWithTag<TextView>("error")
                    if (withTag != null) {
                        label_select.removeView(withTag)
                    }
                    label.setBackgroundColor(Color.parseColor("#00000000"))
                    label.setTextSize(Dimension.SP, 15f)
                    label_unselect.removeView(label)
                    label_select.addView(label)
                }
            }

        })

    }

    override fun loadData() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_confirm -> {
                toNextPage()
            }
            else -> {

            }
        }
    }


    val cwvWallet = GreWalletModel("")
    val ethWallet = GreWalletModel("")

    private fun toNextPage() {
//
//        if (label_select.childCount != 12) {
//            showTopMsg("请完成助记词选择")
//            return
//        }
//
//        rightList.forEachIndexed { index, s ->
//            var textView = label_select.getChildAt(index) as TextView
//            if (!s.equals(textView.text.toString())) {
//                showTopMsg("助记词选择不正确")
//                return@toNextPage
//            }
//        }

        showProgressDialog("")

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
        }).flatMap(object : Function<String, Observable<String>> {
            override fun apply(mainKey: String): Observable<String> {

                var onSubscribe = object : ObservableOnSubscribe<String> {
                    override fun subscribe(emitter: ObservableEmitter<String>) {
                        //获取 eth 地址
                        CallJsCodeUtils.getAddress(mainKey, object : ValueCallback<String> {
                            override fun onReceiveValue(ethAddress: String) {
                                var realEthAddress = CallJsCodeUtils.readStringJsValue(ethAddress)
                                ethWallet.address = realEthAddress
                                // mainkey 往下传继续创建钱包逻辑
                                emitter.onNext(mainKey)
                            }
                        })
                    }
                }

                return Observable.create(onSubscribe)
            }

        }).flatMap(object : Function<String, Observable<String>> {
            override fun apply(mainKey: String): Observable<String> {

                var onSubscribe = ObservableOnSubscribe<String> {
                    // 获取 eth 私钥
                    CallJsCodeUtils.getPrivateKey(mainKey, object : ValueCallback<String> {
                        override fun onReceiveValue(ethPrivateKey: String?) {
                            var realEthPrivateKey = CallJsCodeUtils.readStringJsValue(ethPrivateKey)
                            ethWallet.privateKey = realEthPrivateKey
                            it.onNext(realEthPrivateKey)
                        }
                    })
                }
                return Observable.create(onSubscribe)
            }
        }).subscribe(object : Consumer<String> {
            override fun accept(ethPrivateKey: String?) {
                hideProgressDialog()
                // 获取 cwv 钱包 地址和私钥
                CallJsCodeUtils.cwv_GenFromPrikey(ethPrivateKey) {
                    var realContent = CallJsCodeUtils.readStringJsValue(it)
                    if (CheckedUtils.isJson(realContent)) {
                        val jsonObject = JSONObject(realContent)
                        cwvWallet.privateKey = jsonObject.getString("hexPrikey")
                        cwvWallet.address = "0x" + jsonObject.getString("hexAddress")
                    }
                    cwvWallet.walletName = "CWV-" + RandomUtils.getRandomString(4)
                    cwvWallet.walletType = "CWV"
                    cwvWallet.mnemonic = mnemonic
                    ethWallet.walletName = "ETH-" + RandomUtils.getRandomString(4)
                    ethWallet.walletType = "ETH"
                    ethWallet.mnemonic = mnemonic
                    SWLog.e(cwvWallet)
                    SWLog.e(ethWallet)
                    GreWalletOperator.insert(cwvWallet)
                    GreWalletOperator.insert(ethWallet)
                    startActivity(MainActivity::class.java)
                    AppManager.finishActivity(CreateAccountPreActivity::class.java)
                    AppManager.finishActivity(CreateAccountStepOneActivity::class.java)
                    finish()
                }
            }
        })
    }

}
