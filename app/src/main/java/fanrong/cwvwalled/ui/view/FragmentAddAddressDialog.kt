package fanrong.cwvwalled.ui.view

import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.utils.AppUtils
import kotlinx.android.synthetic.main.pop_add_address.*
import net.sourceforge.UI.view.FullScreenDialog
import xianchao.com.basiclib.utils.checkIsEmpty

class FragmentAddAddressDialog : FullScreenDialog {

    constructor(baseActivity: BaseActivity) : super(baseActivity)

    var onOkListener: OnOkListener? = null


    override fun getContentView(): Int {
        return R.layout.pop_add_address
    }

    override fun initView() {
        iv_add_address_cancel.setOnClickListener {
            dismiss()
        }
        tv_address_ok.setOnClickListener {

            if (edit_add_address_name.text.toString().checkIsEmpty()) {
                val baseActivity = ownerActivity as BaseActivity
                baseActivity.showTopMsgWithDialog("姓名不能为空",this)
                return@setOnClickListener
            }


            if (edit_add_address_address.text.toString().checkIsEmpty()) {
                val baseActivity = ownerActivity as BaseActivity
                baseActivity.showTopMsgWithDialog("地址不能为空",this)
                return@setOnClickListener
            }
            onOkListener?.OnOk(edit_add_address_name.text.toString(),
                    edit_add_address_address.text.toString())
        }
        setOnDismissListener {
            AppUtils.hideSoftInput(this)
        }
    }


    private fun initview() {


    }


    interface OnOkListener {
        fun OnOk(name: String, address: String)
    }
}