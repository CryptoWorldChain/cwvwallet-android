package fanrong.cwvwalled.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import kotlinx.android.synthetic.main.activity_create_account_pre.*

class CreateAccountPreActivity : BaseActivity() {
    override fun loadData() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_create -> {
                btn_create.setBackgroundResource(R.drawable.create_account_btn_select)
                btn_back.setBackgroundResource(R.drawable.create_account_btn_unselect)
                startActivity(CreateAccountPasswordActivity::class.java)
            }
            R.id.btn_back -> {
                btn_create.setBackgroundResource(R.drawable.create_account_btn_unselect)
                btn_back.setBackgroundResource(R.drawable.create_account_btn_select)

            }
            else -> {

            }
        }

    }

    override fun initView() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_pre)
        btn_create.setOnClickListener(this)
        btn_back.setOnClickListener(this)
    }
}
