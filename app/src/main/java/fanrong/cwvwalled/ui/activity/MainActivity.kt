package fanrong.cwvwalled.ui.activity

import android.view.View
import android.widget.Toast
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.ui.fragment.*
import fanrong.cwvwalled.utils.PreferenceHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    var fragments = arrayOfNulls<BaseFragment>(5)

    override fun initView() {
        rg_home_tab.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_home_tab_wallet -> {
                    if (fragments[0] == null) {
                        fragments[0] = HomeFragment()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragments[0]!!).commitAllowingStateLoss()
                }
                R.id.rb_home_tab_market -> {
                    if (fragments[1] == null) {
                        fragments[1] = MarketFragment()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragments[1]!!).commitAllowingStateLoss()
                }
                R.id.rb_home_tab_financial -> {
                    if (fragments[2] == null) {
                        fragments[2] = FinancialFragment()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragments[2]!!).commitAllowingStateLoss()
                }
                R.id.rb_home_tab_found -> {
                    if (fragments[3] == null) {
                        fragments[3] = FoundFragment()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragments[3]!!).commitAllowingStateLoss()
                }
                R.id.rb_home_tab_mine -> {
                    if (fragments[4] == null) {
                        fragments[4] = MineFragment()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragments[4]!!).commitAllowingStateLoss()
                }
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, HomeFragment()).commitAllowingStateLoss()
    }


    override fun onClick(v: View) {
    }

    override fun loadData() {
    }

    var mExitUtcMs = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitUtcMs > 2000) {
            mExitUtcMs = System.currentTimeMillis()
            Toast.makeText(this,"再按一次退出应用",Toast.LENGTH_SHORT).show()
        } else {
            PreferenceHelper.getInstance().clearObjectForKey(PreferenceHelper.PreferenceKey.KEY_NEWS_SELECT_LIST)
            AppApplication.instance.exit()
        }
    }

}
