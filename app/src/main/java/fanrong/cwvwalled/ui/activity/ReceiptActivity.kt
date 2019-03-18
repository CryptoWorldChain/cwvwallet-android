package fanrong.cwvwalled.ui.activity

import android.app.Dialog
import android.view.View

import com.yzq.zxinglibrary.encode.CodeCreator

import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.ui.view.InputAmountDialog
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.FanRongTextUtils
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.layout_receipt.*
import xianchao.com.basiclib.XcSingletons

/**
 * Created by terry.c on 06/03/2018.
 */

class ReceiptActivity : BaseActivity() {

    lateinit var coinBeanModel: LiteCoinBeanModel

    override fun getLayoutId(): Int {
        return R.layout.layout_receipt
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_address -> {
                showTopMsg("已复制");
                AppUtils.clipboardString(this, tv_address.text.toString())
            }
            R.id.bt_input_amount -> {
                onCLickInputAmount()
            }

        }
    }

    override fun loadData() {
    }


    override fun initView() {
        coinBeanModel = intent.getSerializableExtra(PageParamter.PAREMTER_LITE_COINBEAN) as LiteCoinBeanModel

        setLeftImgOnclickListener {
            finish()
        }

        setTitleText(coinBeanModel.coin_symbol!!)

        tv_address.setOnClickListener(this)
        bt_input_amount.setOnClickListener(this)
        tv_address.text = coinBeanModel.sourceAddr
        tv_wallet_name.text = coinBeanModel.walletName

        var moneyQRCodeModel = MoneyQRCodeModel()
        moneyQRCodeModel.address = coinBeanModel.sourceAddr;
        moneyQRCodeModel.amount = ""
        var s = XcSingletons.obtainGson().toJson(moneyQRCodeModel).toString();
        SWLog.e(s)
        var bitmap = CodeCreator.createQRCode(s, 400, 400, null);
        iv_qr_image.setImageBitmap(bitmap)

    }

    fun onCLickInputAmount() {
        var dialog = InputAmountDialog(this)
        dialog.btnListener = object : FRDialogBtnListener {
            override fun onCancel(dialog: Dialog) {
                dialog.dismiss()
            }

            override fun onConfirm(dialog: Dialog) {
                val amountDialog = dialog as InputAmountDialog
                var count = amountDialog.inputCount

                if (FanRongTextUtils.isNumeric(count)) {
                    tv_in_count.visibility = View.VISIBLE
                    tv_in_count.setText(count + " " + coinBeanModel.coin_symbol);
                    var moneyQRCodeModel = MoneyQRCodeModel();
                    moneyQRCodeModel.address = coinBeanModel.sourceAddr;
                    moneyQRCodeModel.amount = count;

                    var bitmap = CodeCreator.createQRCode(XcSingletons.obtainGson()
                            .toJson(moneyQRCodeModel), 400, 400, null);
                    iv_qr_image.setImageBitmap(bitmap)
                } else {
                    showTopMsg("请输入正确的金额");
                }

                dialog.dismiss();
            }
        }

        dialog.coinSymbol = coinBeanModel.coin_symbol
        dialog.show();
    }

    inner class MoneyQRCodeModel {
        var address: String? = null
        var amount: String? = null
    }

}
