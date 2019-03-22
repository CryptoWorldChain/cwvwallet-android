package fanrong.cwvwalled.ui.view

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.DisplayMetrics
import android.view.*
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.utils.AppUtils
import kotlinx.android.synthetic.main.pop_add_address.*
import kotlinx.android.synthetic.main.pop_add_address.view.*
import xianchao.com.basiclib.utils.checkIsEmpty

class FragmentAddAddressDialog : DialogFragment {
    constructor() : super()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.pop_add_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        initview()
    }


    override fun onStart() {
        super.onStart()

        getDialog().getWindow().setBackgroundDrawable(ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        AppUtils.hideSoftInput(activity)
    }

    private fun initview() {

        iv_add_address_cancel.setOnClickListener {
            dismiss()
        }
        tv_address_ok.setOnClickListener {

            if (edit_add_address_name.text.toString().checkIsEmpty()) {
                val baseActivity = context as BaseActivity
                baseActivity.showTopMsg("姓名不能为空")
                return@setOnClickListener
            }


            if (edit_add_address_address.text.toString().checkIsEmpty()) {
                val baseActivity = context as BaseActivity
                baseActivity.showTopMsg("地址不能为空")
                return@setOnClickListener
            }
//            onOkListener.OnOk(edit_add_address_name.text.toString(),
//                    view.edit_add_address_address.text.toString())

        }
//        pop.run {
//            height = ViewGroup.LayoutParams.WRAP_CONTENT
//            width = ViewGroup.LayoutParams.MATCH_PARENT
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            isFocusable = true
//            isOutsideTouchable = true
//            contentView = view
//        }
//        pop.setOnDismissListener {
//            val lp = mContext.window.attributes
//            lp.alpha = 1f
//            mContext.window.attributes = lp
//        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        super.show(manager, tag)

    }
}