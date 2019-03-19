package fanrong.cwvwalled.ui.fragment

import android.app.Dialog
import android.icu.text.StringSearch
import android.text.TextUtils
import android.view.View
import android.webkit.ValueCallback
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.ui.view.PasswordHintDialog
import fanrong.cwvwalled.utils.CallJsCodeUtils
import fanrong.cwvwalled.utils.PreferenceHelper
import fanrong.cwvwalled.utils.SWLog
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_import_key.*
import org.json.JSONObject
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.RandomUtils
import java.security.SecureRandomSpi

class ImportKeyFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_import_key
    }

    lateinit var walletType: String

    override fun initView() {
        walletType = arguments!!.getString(PageParamter.PAREMTER_WALLET_TYPE)
        tv_import.setOnClickListener(this)
        tv_pasword_dialog.setOnClickListener(this)
    }

    override fun loadData() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_import -> {
                import()
            }
            R.id.tv_pasword_dialog -> {
                val hintDialog = PasswordHintDialog(activity!!)
                hintDialog.btnlistener = object : FRDialogBtnListener {
                    override fun onCancel(dialog: Dialog) {
                        dialog.dismiss()
                    }

                    override fun onConfirm(dialog: Dialog) {
                        dialog.dismiss()
                    }

                }
                hintDialog.show()
            }
        }
    }


    private fun import() {
        var privateKey = et_private_key.getText().toString()
        privateKey = privateKey.trim()
        if (TextUtils.isEmpty(privateKey)) {
            showTopMsg("请输入助记词")
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
        if ("CWV".equals(walletType)) {
            importCWV(privateKey)
        } else {
            importETH(privateKey)
        }

    }

    private fun importETH(privateKey: String) {
        (activity as BaseActivity).showProgressDialog("")

        CallJsCodeUtils.privateToAddress(privateKey) {
            val jsValue = CallJsCodeUtils.readStringJsValue(it)
            hideProgressDialog()
            if (CheckedUtils.isEmpty(jsValue)) {
                showTopMsg("导入失败")
            } else {
                ethWallet.walletName = "ETH-" + RandomUtils.getRandomString(4)
                ethWallet.walletType = "ETH"
                ethWallet.address = "0x" + jsValue

                if (GreWalletOperator.queryAddress(ethWallet.address) == null) {
                    ethWallet.isImport = true
                    showTopMsg("导入成功")
                    SWLog.e(ethWallet)
                    finishActivityDelay(2000)
                } else {
                    showTopMsg("相应的钱包已存在")
                }
            }
        }

    }

    val cwvWallet = GreWalletModel("")
    val ethWallet = GreWalletModel("")

    fun importCWV(privateKey: String) {
        showProgressDialog("")
        CallJsCodeUtils.cwv_GenFromPrikey(privateKey) {
            hideProgressDialog()

            var realContent = CallJsCodeUtils.readStringJsValue(it)
            if (CheckedUtils.isJson(realContent)) {
                val jsonObject = JSONObject(realContent)
                cwvWallet.privateKey = jsonObject.getString("hexPrikey")
                cwvWallet.address = "0x" + jsonObject.getString("hexAddress")
            } else {
                showTopMsg("导入失败")
                return@cwv_GenFromPrikey
            }
            cwvWallet.walletName = "CWV-" + RandomUtils.getRandomString(4)
            cwvWallet.walletType = "CWV"
            cwvWallet.isImport = true

            SWLog.e(cwvWallet)
            if (GreWalletOperator.queryAddress(cwvWallet.address) == null) {
                GreWalletOperator.insert(cwvWallet)
                showTopMsg("导入成功")
                finishActivityDelay(2000)
            } else {
                showTopMsg("相应的钱包已存在")
            }
        }
    }

}