package fanrong.cwvwalled.ui.fragment

import android.app.Dialog
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.webkit.ValueCallback
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.eventbus.HomeCardNumChangeEvent
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.parenter.WalletCreatePresenter
import fanrong.cwvwalled.ui.activity.CreateAccountPasswordActivity
import fanrong.cwvwalled.ui.activity.CreateAccountPreActivity
import fanrong.cwvwalled.ui.activity.CreateAccountStepOneActivity
import fanrong.cwvwalled.ui.activity.MainActivity
import fanrong.cwvwalled.ui.view.PasswordHintDialog
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
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.RandomUtils
import java.util.*

class ImportWordFragment : BaseFragment() {

    lateinit var walletType: String
    lateinit var presenter: WalletCreatePresenter

    override fun getLayoutId(): Int {
        return R.layout.fragment_import_word
    }

    override fun initView() {
        presenter = WalletCreatePresenter()

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


    fun backWallet(mnemonic: String) {
        (activity as BaseActivity).showProgressDialog("")

        presenter.importCWVWallet(RandomUtils.getRandomString(4), mnemonic) { cwvWallet, msg ->
            hideProgressDialog()
            showTopMsg(msg ?: "")
            if (cwvWallet != null) {
                EventBus.getDefault().post(HomeCardNumChangeEvent())
                finishActivityDelay(2000)
            }
        }
    }

}