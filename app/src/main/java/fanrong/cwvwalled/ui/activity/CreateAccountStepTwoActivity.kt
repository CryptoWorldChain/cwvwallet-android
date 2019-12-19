package fanrong.cwvwalled.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Dimension
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.webkit.ValueCallback
import android.widget.TextView
import com.donkingliang.labels.LabelsView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.parenter.WalletCreatePresenter
import fanrong.cwvwalled.utils.*
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
    lateinit var presenter: WalletCreatePresenter


    override fun getLayoutId(): Int {
        return R.layout.activity_create_account_step_two
    }

    override fun initView() {
        presenter = WalletCreatePresenter()

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
                newToNextPage()
            }
            else -> {

            }
        }
    }

    private fun newToNextPage() {

        if (label_select.childCount != 12) {
            showTopMsg("请完成助记词选择")
            return
        }

        rightList.forEachIndexed { index, s ->
            var textView = label_select.getChildAt(index) as TextView
            if (!s.equals(textView.text.toString())) {
                showTopMsg("助记词选择不正确")
                return@newToNextPage
            }
        }

        showProgressDialog("")

        presenter.createWallet(mnemonic) { cwvWallet, ethWallet ->
            hideProgressDialog()

            if (cwvWallet != null) {
                startActivity(MainActivity::class.java)
                AppManager.finishActivity(CreateAccountPreActivity::class.java)
                AppManager.finishActivity(CreateAccountPasswordActivity::class.java)
                AppManager.finishActivity(CreateAccountStepOneActivity::class.java)
                finish()
            }
        }
    }

}
