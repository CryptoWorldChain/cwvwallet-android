package fanrong.cwvwalled.ui.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.text.Editable
import android.view.View
import android.widget.SeekBar
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.bean.ZxingConfig
import com.yzq.zxinglibrary.common.Constant
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.common.UserInfoObject
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.QueryNodeGasReq
import fanrong.cwvwalled.http.model.TransferReq
import fanrong.cwvwalled.http.model.response.QueryNodeGasResp
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.litepal.AddressModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.parenter.BalancePresenter
import fanrong.cwvwalled.parenter.TransferEthPresenter
import fanrong.cwvwalled.parenter.TransferFbcPresenter
import fanrong.cwvwalled.parenter.TransferPresenter
import fanrong.cwvwalled.ui.view.InputPasswordDialog
import fanrong.cwvwalled.ui.view.TransferConfirmDialog
import fanrong.cwvwalled.ui.view.TransferErrorDialog
import fanrong.cwvwalled.ui.view.TransferSuccessDialog
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import kotlinx.android.synthetic.main.activity_transfer.*
import net.sourceforge.listener.TextWatcherAfter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.BundleUtils
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.checkIsEmpty
import xianchao.com.basiclib.utils.checkNotEmpty
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.HashMap

class TransferActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_transfer
    }

    val START_SCAN = 1001

    lateinit var coinBeanModel: LiteCoinBeanModel
    lateinit var transferPresenter: TransferPresenter
    lateinit var balancePresenter: BalancePresenter
    var balanceBigdecimal: String? = null

    var gas_price = "0"


    override fun initView() {
        coinBeanModel = intent.getSerializableExtra(PageParamter.PAREMTER_LITE_COINBEAN) as LiteCoinBeanModel

        setTitleText("转账")
        setLeftImgOnclickListener {
            finish()
        }
        setRightImgOnclickListener(R.drawable.scan_icon) {

            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe {
                        if (it) {

                            val intent = Intent(this, CaptureActivity::class.java)
                            val zxingConfig = ZxingConfig()
                            zxingConfig.reactColor = R.color.scan_coner_color
                            zxingConfig.frameLineColor = R.color.scan_coner_color
                            zxingConfig.scanLineColor = R.color.scan_coner_color
                            intent.putExtra(Constant.INTENT_ZXING_CONFIG, zxingConfig)
                            startActivityForResult(intent, START_SCAN)
                        } else {
                            showTopMsg("请打开相机权限")
                        }
                    }
        }

        bt_next.setOnClickListener(this)
        iv_choose.setOnClickListener(this)

        if ("ETH".equals(coinBeanModel.channel_name)) {
            transferPresenter = TransferEthPresenter(coinBeanModel)
        } else {
            transferPresenter = TransferFbcPresenter(coinBeanModel)
            sb_seekbar.visibility = View.GONE
            ll_tip.visibility = View.GONE
        }
        balancePresenter = BalancePresenter()

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


        loadData()
    }

    fun updataButton() {
        val address = et_address.text.toString()
        val money = tv_money.text.toString()
        bt_next.isEnabled = CheckedUtils.nonEmpty(address) && CheckedUtils.nonEmpty(money)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_next -> {

                val address = et_address.getText().toString().trim()
                val money = tv_money.text.toString().trim()

                if (CheckedUtils.isEmpty(money)) {
                    showTopMsg("请输入数量")
                    return
                }

                if (BigDecimal(money).compareTo(BigDecimal.ZERO) == 0) {
                    showTopMsg("请输入数量")
                    return
                }

                if (CheckedUtils.isEmpty(address)) {
                    showTopMsg("请输入地址")
                    return
                }

                if (address.equals(coinBeanModel.sourceAddr)){
                    showTopMsg("请不要自己转给自己")
                    return
                }

                val balance = BigDecimal(balanceBigdecimal)
                if (BigDecimal(money).compareTo(balance) == 1) {
                    showTopMsg("当前余额不足")
                    return
                }


                val confirmDialog = TransferConfirmDialog(this)
                confirmDialog.fromAddress = coinBeanModel.sourceAddr
                confirmDialog.toAddress = et_address.text.toString()
                confirmDialog.count = tv_money.text.toString() + " " + coinBeanModel.coin_symbol
                confirmDialog.gas = tv_current.text.toString()
                confirmDialog.remark = tv_remark.text.toString()
                if ("ETH".equals(coinBeanModel.channel_name)) {
                    confirmDialog.isShowGas = true
                } else {
                    confirmDialog.isShowGas = false
                }
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
                dialog.dismiss()
            }

            override fun onConfirm(dialog: Dialog) {
                if (UserInfoObject.password.equals(passwordDialog.inputPassword)) {
                    dialog.dismiss()
                    toCommit()

                } else {
                    dialog.dismiss()
                    showTopMsg("密码不正确")
                }

            }
        }
        passwordDialog.show()

    }

    private fun toCommit() {
        val address = et_address.getText().toString().trim()
        val money = tv_money.text.toString().trim()

//        if (CheckedUtils.nonEmpty(gas_price)) {
//            gas_price = BigDecimal(gas_price)
//                    .multiply(BigDecimal(Math.pow(10.0, 18.0)))
//                    .divide(BigDecimal(400000))
//                    .toPlainString()
//        }

//        // 是整数 转换成整数
//        if (MoneyUtils.isInteger(gas_price)) {
//            gas_price = BigDecimal(gas_price).toBigInteger().toString()
//        }


        val req = TransferReq()
        req.nonce = nonce
        req.to_addr = address
        req.value = money
        req.gas_price = gas_price
        req.symbol = AppUtils.getRealSymbol(coinBeanModel.coin_symbol)

        val exData = HashMap<String, String>()
        val remark = tv_remark.text.toString().trim()
        if (!CheckedUtils.isEmpty(remark)) {
            exData["note"] = remark
        }
        if (CheckedUtils.isEmpty(exData)) {
            req.ex_data = ""
        } else {
            req.ex_data = JSONObject(exData).toString()
        }
        showProgressDialog("交易中...")
         //调用交易的SDK
        transferPresenter.transferMoney(req, object : TransferPresenter.TransferResultCallBack {

            override fun success() {
                hideProgressDialog()
                val successDialog = TransferSuccessDialog(this@TransferActivity)
                successDialog.show()
                android.os.Handler().postDelayed({
                    successDialog.dismiss()
                    finish()
                }, 1500)
            }

            override fun failed(msg: String) {
                val errorDialog = TransferErrorDialog(this@TransferActivity)
                errorDialog.show()
                android.os.Handler().postDelayed({
                    errorDialog.dismiss()
                    finish()
                }, 1500)
            }
        })
    }

    var nonce = "0"

    override fun loadData() {

        balancePresenter.getAddressCoinBalance(coinBeanModel.sourceAddr ?: ""
                , AppUtils.getRealSymbol(coinBeanModel.coin_symbol)) {
            balanceBigdecimal = MoneyUtils.commonHandleDecimal(it)
            tv_useable_money.text = "余额：${balanceBigdecimal} ${coinBeanModel.coin_symbol}"
        }

        transferPresenter.getNonce(object : ValueCallBack<String?> {
            override fun valueBack(t: String?) {
                CheckedUtils.nonEmpty(t) { nonce = t!! }
            }
        })

        transferPresenter.getGasPrice(object : ValueCallBack<String?> {
            override fun valueBack(t: String?) {
                if (t.checkNotEmpty()) {
                    gas_price = t!!
                    initSeekbar()
                }
            }
        })
    }

    fun initSeekbar() {
        sb_seekbar.max = 100
        val minValue = BigDecimal(gas_price).multiply(BigDecimal(400000))
                .divide(BigDecimal(Math.pow(10.0, 18.0)))

        gas_price = minValue.toString()

        val maxValue = minValue.multiply(BigDecimal.TEN)
        sb_seekbar.max = 100
        sb_seekbar.progress = minValue.multiply(BigDecimal("1.5")).divide(maxValue).multiply(BigDecimal("100")).toInt()
        tv_current.setText(minValue.multiply(BigDecimal("1.5")).setScale(9, RoundingMode.DOWN)
                .toPlainString().removeAllZero() + " ether")

        sb_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                gas_price = BigDecimal(progress)
                        .divide(BigDecimal("100"))
                        .multiply(maxValue)
                        .setScale(9, RoundingMode.DOWN)
                        .toPlainString().removeAllZero()

                tv_current.text = gas_price + " ether"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val currentValue = BigDecimal(seekBar.progress)
                        .divide(BigDecimal("100"))
                        .multiply(maxValue)
                        .setScale(9, RoundingMode.DOWN)
                if (minValue.compareTo(currentValue) == 1) {
                    showTopMsg("不能小于最低矿工费")
                    seekBar.progress = minValue.divide(maxValue).multiply(BigDecimal("100")).toInt()
                }
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == START_SCAN) {
            if (data == null) {
                return
            }
            val stringExtra = data.getStringExtra(Constant.CODED_CONTENT)
            if (CheckedUtils.isJson(stringExtra)) {
                val moneyQRCodeModel = Gson().fromJson<ReceiptActivity.MoneyQRCodeModel>(stringExtra, ReceiptActivity.MoneyQRCodeModel::class.java!!)
                et_address.setText(moneyQRCodeModel.address)
                tv_money.setText(moneyQRCodeModel.amount)
            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                val model = data!!.getSerializableExtra(PageParamter.RESULT_ADDRESS) as AddressModel
                et_address.setText(model.address)
            }
        }


    }

    public fun String.removeAllZero(): String {
        return java.lang.String(this).replaceAll("0+?$", "")

    }

    override fun onStop() {
        super.onStop()
        AppUtils.hideSoftInput(this)
    }

}
