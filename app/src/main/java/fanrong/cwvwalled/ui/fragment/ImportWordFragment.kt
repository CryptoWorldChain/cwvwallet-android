package fanrong.cwvwalled.ui.fragment

import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.webkit.ValueCallback
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.ui.activity.CreateAccountPreActivity
import fanrong.cwvwalled.ui.activity.MainActivity
import fanrong.cwvwalled.utils.AppManager
import fanrong.cwvwalled.utils.CallJsCodeUtils
import fanrong.cwvwalled.utils.PreferenceHelper
import fanrong.cwvwalled.utils.SWLog
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_import_word.*
import net.sourceforge.UI.view.BackErrorDialog
import org.json.JSONObject
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.RandomUtils
import java.util.*

class ImportWordFragment : BaseFragment() {

    lateinit var walletType: String

    override fun getLayoutId(): Int {
        return R.layout.fragment_import_word
    }

    override fun initView() {
        walletType = arguments!!.getString(PageParamter.PAREMTER_WALLET_TYPE)
        tv_import.setOnClickListener(this)
    }

    override fun loadData() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_import -> {
                import()
            }
            else -> {
            }
        }
    }

    private fun import() {
        var mnemonic = et_word.getText().toString()

        if (TextUtils.isEmpty(mnemonic)) {
            showTopMsg("请输入助记词")
            return
        }
        // 防止用户多的输入空格，先删除所有空格，在 builder string
        val split = mnemonic.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val strings = ArrayList(Arrays.asList<String>(*split))

        val iterator = strings.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (CheckedUtils.isEmpty(next.replace(" ".toRegex(), ""))) {
                iterator.remove()
            }
        }
        val stringBuilder = StringBuilder()
        for (string in strings) {
            stringBuilder.append("$string ")
        }
        mnemonic = stringBuilder.toString().trim { it <= ' ' }
        if (split.size != 12) {
            showTopMsg("请正确填写助记词")
            return
        }


        val userPassword = et_userpassword.text.toString()
        if (CheckedUtils.isEmpty(userPassword)) {
            showTopMsg("请输入密码")
            return
        }
        var userPasswordConfirm = et_userpassword_confirm.text.toString()
        if (CheckedUtils.isEmpty(userPasswordConfirm)) {
            showTopMsg("请输入确认密码")
            return
        }

        if (userPassword.length < 8) {
            showTopMsg("密码必须不小于8位")
            return
        }
        if (!userPassword.equals(userPasswordConfirm)) {
            showTopMsg("两次输入密码不一致")
            return
        }

//        PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.USERNAME, username)
        PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.PASSWORD, userPassword)
//        startActivity(CreateAccountStepOneActivity::class.java)
        backWallet(mnemonic)

    }

    val cwvWallet = GreWalletModel("")
    val ethWallet = GreWalletModel("")

    fun backWallet(mnemonic: String) {
        (activity as BaseActivity).showProgressDialog("")
        Observable.create<String>(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                //获取主私钥
                CallJsCodeUtils.mnemonicToHDPrivateKey(mnemonic, object : ValueCallback<String> {
                    override fun onReceiveValue(mainKey: String?) {
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
                createWallet(ethPrivateKey, mnemonic)
            }
        }, Consumer<Throwable> {
            showTopMsg("导入失败")
        })
    }

    private fun createWallet(ethPrivateKey: String?, mnemonic: String) {
        (activity as BaseActivity).hideProgressDialog()
        // 获取 cwv 钱包 地址和私钥
        CallJsCodeUtils.cwv_GenFromPrikey(ethPrivateKey) {
            var realContent = CallJsCodeUtils.readStringJsValue(it)
            if (CheckedUtils.isJson(realContent)) {
                val jsonObject = JSONObject(realContent)
                cwvWallet.privateKey = jsonObject.getString("hexPrikey")
                cwvWallet.address = "0x" + jsonObject.getString("hexAddress")
            }
            cwvWallet.walletName = "CWV" + RandomUtils.getRandomString(4)
            cwvWallet.walletType = "CWV"
            cwvWallet.mnemonic = mnemonic
            cwvWallet.isImport = true

            ethWallet.walletName = "ETH" + RandomUtils.getRandomString(4)
            ethWallet.walletType = "ETH"
            ethWallet.mnemonic = mnemonic
            ethWallet.isImport = true

            if ("CWV".equals(walletType)) {
                SWLog.e(cwvWallet)
                if (GreWalletOperator.queryAddress(cwvWallet.address) == null) {
                    GreWalletOperator.insert(cwvWallet)
                    showTopMsg("导入成功")
                    finishActivityDelay(2000)
                } else {
                    showTopMsg("导入失败")
                }
            } else {
                SWLog.e(ethWallet)
                if (GreWalletOperator.queryAddress(cwvWallet.address) == null) {
                    GreWalletOperator.insert(ethWallet)
                    showTopMsg("导入成功")
                    finishActivityDelay(2000)
                } else {
                    showTopMsg("导入失败")
                }
            }
        }
    }


}