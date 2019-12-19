package fanrong.cwvwalled.ui.activity

import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.webkit.ValueCallback
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.parenter.WalletCreatePresenter
import fanrong.cwvwalled.utils.AppManager
import fanrong.cwvwalled.utils.CallJsCodeUtils
import fanrong.cwvwalled.utils.PreferenceHelper
import fanrong.cwvwalled.utils.SWLog
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_create_account_retrieve.*
import net.sourceforge.UI.view.BackErrorDialog
import org.json.JSONObject
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.RandomUtils
import java.util.*

class RetrieveAccountActivity : BaseActivity() {
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

    lateinit var presenter: WalletCreatePresenter

    override fun initView() {
        presenter = WalletCreatePresenter()

        btn_confirm.setOnClickListener(this)
        et_userpassword.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                }
            }
        })

        et_userpassword.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        et_userpassword_confirm.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

        cb_password.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                et_userpassword.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                et_userpassword_confirm.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            } else {
                et_userpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                et_userpassword_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_create_account_retrieve
    }


    private fun toNextPage() {


        var mnemonic = et_mnemonic.getText().toString()

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
        if (strings.size != 12) {
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
        showProgressDialog("")

        presenter.createWallet(mnemonic) { cwvWallet, ethWallet ->
            hideProgressDialog()

            val shareData = PreferenceHelper.getInstance().getStringShareData(PreferenceHelper.PreferenceKey.NICK_NAME, "AA")

            if (cwvWallet != null) {
                cwvWallet.walletName = "CWV-" + shareData
                cwvWallet.walletType = "CWV"
                cwvWallet.mnemonic = mnemonic
                SWLog.e(cwvWallet)
                GreWalletOperator.insert(cwvWallet)
            } else {
                showTopMsg("创建钱包失败")
                return@createWallet
            }

            if (ethWallet != null) {
                ethWallet.walletName = "ETH-" + shareData
                ethWallet.walletType = "ETH"
                ethWallet.mnemonic = mnemonic
                SWLog.e(ethWallet)
                GreWalletOperator.insert(ethWallet)
            }

            startActivity(MainActivity::class.java)
            finish()
        }

    }

}
