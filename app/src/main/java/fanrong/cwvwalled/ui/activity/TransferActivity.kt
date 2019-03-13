package fanrong.cwvwalled.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.text.Editable
import android.view.View
import android.widget.SeekBar
import com.google.gson.Gson
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
import java.math.BigDecimal
import java.util.HashMap
import java.util.logging.Handler

class TransferActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_transfer
    }

    val START_SCAN = 1001

    lateinit var coinBeanModel: LiteCoinBeanModel
    lateinit var transferPresenter: TransferPresenter
    var balanceBigdecimal: String? = null

    var gas_price = "0"


    override fun initView() {
        coinBeanModel = intent.getSerializableExtra(PageParamter.PAREMTER_LITE_COINBEAN) as LiteCoinBeanModel

        setTitleText("转账")
        setLeftImgOnclickListener {
            finish()
        }
        setRightImgOnclickListener(R.drawable.scan_icon) {

            val intent = Intent(this, CaptureActivity::class.java)
            val zxingConfig = ZxingConfig()
            zxingConfig.reactColor = R.color.scan_coner_color
            zxingConfig.frameLineColor = R.color.scan_coner_color
            zxingConfig.scanLineColor = R.color.scan_coner_color
            intent.putExtra(Constant.INTENT_ZXING_CONFIG, zxingConfig)
            startActivityForResult(intent, START_SCAN)
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

                if (CheckedUtils.isEmpty(address)) {
                    showTopMsg("请输入数量")
                    return
                }

                if (CheckedUtils.isEmpty(money)) {
                    showTopMsg("请输入地址")
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

        if (CheckedUtils.nonEmpty(gas_price)) {
            gas_price = BigDecimal(gas_price)
                    .multiply(BigDecimal(Math.pow(10.0, 18.0)))
                    .divide(BigDecimal(400000))
                    .toPlainString()
        }

        // 是整数 转换成整数
        if (MoneyUtils.isInteger(gas_price)) {
            gas_price = BigDecimal(gas_price).toBigInteger().toString()
        }


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


        transferPresenter.transferMoney(req, object : TransferPresenter.TransferResultCallBack {

            override fun success() {
                hideProgressDialog()
                val successDialog = TransferSuccessDialog(this@TransferActivity)
                successDialog.show()
                android.os.Handler().postDelayed({
                    successDialog.dismiss()
                    finish()
                }, 1500)
                //                            val successDialog = TransferSuccessDialog(getActivity())
                //                            successDialog.show()
                //                            Handler().postDelayed({
                //                                successDialog.dismiss()
                //                            }, 1500)
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

        transferPresenter.getNonce(object : ValueCallBack<String?> {
            override fun valueBack(t: String?) {
                CheckedUtils.nonEmpty(t) { nonce = t!! }

                if (CheckedUtils.nonEmpty(t)) {

                }
            }
        })

        transferPresenter.getBalance(object : ValueCallBack<String?> {
            override fun valueBack(t: String?) {
                balanceBigdecimal = MoneyUtils.commonHandleDecimal(t)
                tv_useable_money.text = "余额 ${balanceBigdecimal} ${coinBeanModel.coin_symbol}"
            }
        })

        requestGasPrice()


    }

    fun initSeekbar() {
        sb_seekbar.max = 100
        val plainString = BigDecimal(gas_price).multiply(BigDecimal(400000))
                .divide(BigDecimal(Math.pow(10.0, 18.0)))

        gas_price = plainString.toString()
        tv_current.setText(gas_price + " ether")

        val initialValue = plainString.multiply(BigDecimal(Math.pow(10.0, 5.0)))
        sb_seekbar.max = initialValue.multiply(BigDecimal.TEN).toInt()
        sb_seekbar.progress = initialValue.toInt()

        sb_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                gas_price = BigDecimal(progress).divide(BigDecimal(Math.pow(10.0, 5.0))).toPlainString()
                tv_current.text = gas_price + " ether"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (initialValue.compareTo(BigDecimal(seekBar.progress)) == 1) {
                    showTopMsg("不能小于最低矿工费")
                    seekBar.progress = initialValue.toInt()
                }
            }
        })

    }

    private fun requestGasPrice() {
        val queryNodeGasReq = QueryNodeGasReq()
        queryNodeGasReq.node_url = GreNodeOperator.queryETHnode().node_url
        queryNodeGasReq.dapp_id = Constants.DAPP_ID

        RetrofitClient.getETHNetWorkApi()
                .queryNodeGas(ConvertToBody.ConvertToBody(queryNodeGasReq))
                .enqueue(object : Callback<QueryNodeGasResp> {
                    override fun onResponse(call: Call<QueryNodeGasResp>, response: Response<QueryNodeGasResp>) {
                        val body = response.body()
                        gas_price = body!!.gas_price
                        initSeekbar()

                    }

                    override fun onFailure(call: Call<QueryNodeGasResp>, t: Throwable) {

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


}
