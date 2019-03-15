package fanrong.cwvwalled.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.utils.PreferenceHelper
import kotlinx.android.synthetic.main.activity_create_account_password.*
import xianchao.com.basiclib.utils.CheckedUtils

class CreateAccountPasswordActivity : BaseActivity() {
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

    override fun initView() {
        btn_confirm.setOnClickListener(this)
        et_userpassword.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
//                    tv_password_hint.visibility =View.VISIBLE variation
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
        return R.layout.activity_create_account_password
    }


    private fun toNextPage() {
        var username = et_username.text.toString()
        if (CheckedUtils.isEmpty(username)) {
            showTopMsg("请输入用户名")
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


        PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.NICK_NAME, username)
        PreferenceHelper.getInstance().storeStringShareData(PreferenceHelper.PreferenceKey.PASSWORD, userPassword)
        startActivity(CreateAccountStepOneActivity::class.java)


    }


}
