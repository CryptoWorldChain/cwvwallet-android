package fanrong.cwvwalled.ui.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.UserInfoObject
import fanrong.cwvwalled.ui.activity.*
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {
        tv_mine_logout.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_mine_logout.paint.isAntiAlias = true

        setting_item_setting.setmOnLSettingItemClick {
            startActivity(UseSettingActivity::class.java)
        }

        setting_item_contact.setmOnLSettingItemClick {
            startActivity(AddressListActivity::class.java)
        }

        setting_item_about.setmOnLSettingItemClick {
            startActivity(AboutUsActivity::class.java)
        }

        setting_item_agreement.setmOnLSettingItemClick {
            val bundle = Bundle()
            bundle.putString("url", "file:///android_asset/user_agreement.html")
            bundle.putString("title", "用户协议")
            startActivity(WebViewActivity::class.java, bundle)
        }


        tv_mine_logout.setOnClickListener {
            UserInfoObject.userLogout()
            startActivity(CreateAccountPreActivity::class.java)
        }
    }

    override fun loadData() {
    }

    override fun onClick(v: View?) {
    }


}