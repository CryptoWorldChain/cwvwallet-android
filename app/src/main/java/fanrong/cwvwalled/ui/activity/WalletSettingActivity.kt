package fanrong.cwvwalled.ui.activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.common.UserInfoObject
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.ui.view.ChangeWalletNameDialog
import fanrong.cwvwalled.ui.view.InputPasswordDialog
import kotlinx.android.synthetic.main.activity_wallet_setting.*
import xianchao.com.basiclib.utils.CheckedUtils

class WalletSettingActivity : BaseActivity() {

    lateinit var walletModel: GreWalletModel

    override fun getLayoutId(): Int {
        return R.layout.activity_wallet_setting
    }

    override fun initView() {
        walletModel = intent.getSerializableExtra(PageParamter.PAREMTER_WALLET) as GreWalletModel

        ll_wallet.setOnClickListener(this)
        ll_word.setOnClickListener(this)
        ll_key.setOnClickListener(this)

        setTitleText(walletModel.walletName!!)
        setLeftImgOnclickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        tv_wallet_name.text = walletModel.walletName
        tv_cwv_address.text = walletModel.address

        ll_word.visibility = if (CheckedUtils.isEmpty(walletModel.mnemonic)) View.GONE else View.VISIBLE
    }

    override fun onClick(v: View) {
        if (v.id == R.id.ll_wallet) {
            showChangeNameDialog()
            return
        }

        var dialog = InputPasswordDialog(this)
        dialog.btnListener = object : FRDialogBtnListener {
            override fun onCancel(dialog: Dialog) {
                dialog.dismiss()
            }

            override fun onConfirm(dialog: Dialog) {
                dialog.dismiss()
                val passwordDialog = dialog as InputPasswordDialog

                if (!UserInfoObject.Companion.password.equals(passwordDialog.inputPassword)) {
                    showTopMsg("密码不正确")
                    return
                }

                if (v.id == R.id.ll_key) {
                    val bundle = Bundle()
                    bundle.putSerializable(PageParamter.PAREMTER_WALLET, walletModel)
                    startActivity(ExportPrivateKeyActivity::class.java, bundle)
                } else if (v.id == R.id.ll_word) {
                    val bundle = Bundle()
                    bundle.putSerializable(PageParamter.PAREMTER_WALLET, walletModel)
                    startActivity(ExportWordActivity::class.java, bundle)
                }


            }

        }
        dialog.show()
    }

    private fun showChangeNameDialog() {
        var dialog = ChangeWalletNameDialog(this)
        dialog.btnListener = object : FRDialogBtnListener {
            override fun onCancel(dialog: Dialog) {
                dialog.dismiss()
            }

            override fun onConfirm(dialog: Dialog) {
                dialog.dismiss()
                val nameDialog = dialog as ChangeWalletNameDialog
                walletModel.walletName = nameDialog.walletName
                GreWalletOperator.updateWalletName(walletModel)
                tv_wallet_name.text = walletModel.walletName
            }
        }
        dialog.show()

    }

    override fun loadData() {
    }
}
