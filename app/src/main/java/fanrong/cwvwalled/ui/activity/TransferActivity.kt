package fanrong.cwvwalled.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.view.View
import com.yzq.zxinglibrary.android.CaptureActivity
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.common.UserInfoObject
import fanrong.cwvwalled.http.model.TransferReq
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.litepal.AddressModel
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.parenter.TransferEthPresenter
import fanrong.cwvwalled.parenter.TransferPresenter
import fanrong.cwvwalled.ui.view.InputPasswordDialog
import fanrong.cwvwalled.ui.view.TransferConfirmDialog
import fanrong.cwvwalled.utils.MoneyUtils
import kotlinx.android.synthetic.main.activity_transfer.*
import net.sourceforge.listener.TextWatcherAfter
import xianchao.com.basiclib.utils.BundleUtils
import xianchao.com.basiclib.utils.CheckedUtils

class TransferActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_transfer
    }


    lateinit var coinBeanModel: LiteCoinBeanModel
    lateinit var transferPresenter: TransferPresenter
    override fun initView() {
        coinBeanModel = intent.getSerializableExtra(PageParamter.PAREMTER_LITE_COINBEAN) as LiteCoinBeanModel

        setTitleText("转账")
        setLeftImgOnclickListener {
            finish()
        }
        setRightImgOnclickListener(R.drawable.scan_icon) {
            startActivity(CaptureActivity::class.java)
        }

        bt_next.setOnClickListener(this)
        iv_choose.setOnClickListener(this)

        if ("ETH".equals(coinBeanModel.channel_name)) {
            transferPresenter = TransferEthPresenter(coinBeanModel)
        } else {
            transferPresenter = TransferEthPresenter(coinBeanModel)
        }

        et_address.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable?) {
                updataButton()
            }
        })

        tv_money.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable?) {
                updataButton()
            }
        })
    }

    fun updataButton() {
        val address = et_address.text.toString()
        val money = tv_money.text.toString()
        bt_next.isEnabled = CheckedUtils.nonEmpty(address) && CheckedUtils.nonEmpty(money)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_next -> {

                val confirmDialog = TransferConfirmDialog(this)
                confirmDialog.fromAddress = coinBeanModel.sourceAddr
                confirmDialog.toAddress = et_address.text.toString()
                confirmDialog.count = tv_money.text.toString()
                confirmDialog.remark = tv_remark.text.toString()

                confirmDialog.listener = object : FRDialogBtnListener {
                    override fun onCancel(dialog: Dialog) {
                    }

                    override fun onConfirm(dialog: Dialog) {
                        dialog.dismiss()
                        showPassword()
                    }

                }
                confirmDialog.show()


            }
            R.id.iv_choose -> {
                val with = BundleUtils.createWith(PageParamter.PAREMTER_IS_PICK, true)
                startActivityForResult(AddressListActivity::class.java, with)
            }
        }
    }

    private fun showPassword() {

        val passwordDialog = InputPasswordDialog(this)
        passwordDialog.btnListener = object : FRDialogBtnListener {
            override fun onCancel(dialog: Dialog) {
            }

            override fun onConfirm(dialog: Dialog) {
                if (UserInfoObject.password.equals(passwordDialog.inputPassword)) {

                    val req = TransferReq()

                    transferPresenter.transferMoney(req, object : TransferPresenter.TransferResultCallBack {

                        override fun success() {
                        }

                        override fun failed(msg: String) {
                        }
                    })

                } else {
                    dialog.dismiss()
                    showTopMsg("密码不正确")
                }

            }
        }
        passwordDialog.show()

    }

    var nonce = "0"

    override fun loadData() {

        transferPresenter.getNonce(object : ValueCallBack<String?> {
            override fun valueBack(t: String?) {
                CheckedUtils.nonEmpty(t) { nonce = t!! }

                if (CheckedUtils.nonEmpty(t)) {

                }
            }
        })

        transferPresenter.getBalance(object : ValueCallBack<String?> {
            override fun valueBack(t: String?) {
                val handleDecimal = MoneyUtils.commonHandleDecimal(t)
                tv_useable_money.text = "余额 ${handleDecimal} ${coinBeanModel.coin_symbol}"
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val model = data!!.getSerializableExtra(PageParamter.RESULT_ADDRESS) as AddressModel
            et_address.setText(model.address)
        }

    }


}
