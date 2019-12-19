package fanrong.cwvwalled.ui.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.UpdateResp
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.ui.fragment.*
import fanrong.cwvwalled.ui.view.UpdateDialog
import fanrong.cwvwalled.utils.PreferenceHelper
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    var fragments = arrayOfNulls<BaseFragment>(5)

    override fun initView() {
        rb_home_tab_market.visibility = View.GONE
        rb_home_tab_financial.visibility = View.GONE
        rb_home_tab_found.visibility = View.GONE

        rg_home_tab.setOnCheckedChangeListener { _, checkedId ->
            var fragmentTransaction = supportFragmentManager.beginTransaction()
            for (fragment in fragments) {
                if (fragment != null) {
                    fragmentTransaction.hide(fragment)
                }
            }

            when (checkedId) {
                R.id.rb_home_tab_wallet -> {
                    if (fragments[0] == null) {
                        fragments[0] = HomeFragment()
                        fragmentTransaction.add(R.id.main_frame_layout, fragments[0]!!)
                    }
                    fragmentTransaction.show(fragments[0]!!)
                }
                R.id.rb_home_tab_market -> {
                    if (fragments[1] == null) {
                        fragments[1] = MarketFragment()
                        fragmentTransaction.add(R.id.main_frame_layout, fragments[1]!!)
                    }
                    fragmentTransaction.show(fragments[1]!!)
                }
                R.id.rb_home_tab_financial -> {
                    if (fragments[2] == null) {
                        fragments[2] = FinancialFragment()
                        fragmentTransaction.add(R.id.main_frame_layout, fragments[2]!!)
                    }
                    fragmentTransaction.show(fragments[2]!!)
                }
                R.id.rb_home_tab_found -> {
                    if (fragments[3] == null) {
                        fragments[3] = FoundFragment()
                        fragmentTransaction.add(R.id.main_frame_layout, fragments[3]!!)
                    }
                    fragmentTransaction.show(fragments[3]!!)
                }
                R.id.rb_home_tab_mine -> {
                    if (fragments[4] == null) {
                        fragments[4] = MineFragment()
                        fragmentTransaction.add(R.id.main_frame_layout, fragments[4]!!)
                    }
                    fragmentTransaction.show(fragments[4]!!)
                }
            }
            fragmentTransaction.commitAllowingStateLoss()
        }

        if (fragments[0] == null) {
            fragments[0] = HomeFragment()
        }
        supportFragmentManager
                .beginTransaction()
                .add(R.id.main_frame_layout, fragments[0]!!)
                .show(fragments[0]!!)
                .commitAllowingStateLoss()

        checkedUpdate()
    }


    override fun onClick(v: View) {
    }

    override fun loadData() {
    }

    var mExitUtcMs = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitUtcMs > 2000) {
            mExitUtcMs = System.currentTimeMillis()
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show()
        } else {
            PreferenceHelper.getInstance().clearObjectForKey(PreferenceHelper.PreferenceKey.KEY_NEWS_SELECT_LIST)
            AppApplication.instance.exit()
        }
    }


    /**
     *
     */
    private fun checkedUpdate() {
        RetrofitClient.getNetWorkApi()
                .update()
                .enqueue(object : Callback<UpdateResp> {
                    override fun onFailure(call: Call<UpdateResp>, t: Throwable) {
                        SWLog.e("onFailure")
                    }

                    override fun onResponse(call: Call<UpdateResp>, response: Response<UpdateResp>) {

                        if (response.body() == null) {
                            return
                        }

                        val packageInfo = packageManager.getPackageInfo(packageName, 0)

                        var selfVersion = packageInfo.versionName.replace(".", "").toInt()
                        var serviceVersion = response.body()!!.current_version.replace(".", "").toInt()

                        if (serviceVersion > selfVersion) {
                            val dialog = UpdateDialog(this@MainActivity)
                            dialog.btnListener = object : FRDialogBtnListener {
                                override fun onCancel(dialog: Dialog) {
                                    dialog.dismiss()
                                }

                                override fun onConfirm(dialog: Dialog) {
                                    toweb(response.body()!!.download_url!!)
                                }
                            }

                            dialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
                                override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                                        return true
                                    }
                                    return false
                                }
                            })
                            dialog.version = response.body()!!.current_version
                            dialog.show()

                        }
                    }
                })
    }


    fun toweb(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}
