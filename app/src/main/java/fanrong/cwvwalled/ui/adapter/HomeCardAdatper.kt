package fanrong.cwvwalled.ui.adapter

import android.support.v4.view.PagerAdapter
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.ui.activity.WalletSettingActivity
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import fanrong.cwvwalled.utils.WordReplacement
import xianchao.com.basiclib.utils.BundleUtils

class HomeCardAdatper(var activity: BaseActivity) : PagerAdapter() {

    var allWallet: MutableList<GreWalletModel> = GreWalletOperator.queryAll()


    var allPages = mutableMapOf<Int, View>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(AppApplication.instance).inflate(R.layout.padapter_main_banner, container, false)
        initView(position, view)

        allPages.put(position, view)
        container.addView(view)
        return view
    }

    private fun initView(position: Int, view: View) {
        val walletModel = allWallet[position % allWallet.size]
        if (walletModel.isImport!!) {
            view.setBackgroundResource(R.drawable.home_card_import)
        } else {
            if ("CWV".equals(walletModel.walletType)) {
                view.setBackgroundResource(R.drawable.home_card_cwv)
            } else {
                view.setBackgroundResource(R.drawable.home_card_eth)
            }
        }

        view.findViewById<TextView>(R.id.tv_wallet_name).text = walletModel.walletName
        view.findViewById<TextView>(R.id.tv_address).text = walletModel.address
        var tv_price = view.findViewById<TextView>(R.id.tv_price)
        var tv_unit = view.findViewById<TextView>(R.id.tv_unit)
        tv_price.text = MoneyUtils.commonRMBDecimal(walletModel.rmb)


        var ib_hide_assert = view.findViewById<CheckBox>(R.id.ib_hide_assert)
        ib_hide_assert.isChecked = !(walletModel.isShowRmb!!)

//        if (ib_hide_assert.isChecked) {
//            tv_unit.visibility = View.INVISIBLE
//            tv_price.setTransformationMethod(WordReplacement.getInstance())
//        } else {
//            tv_unit.visibility = View.VISIBLE
//            tv_price.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
//        }

        ib_hide_assert.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tv_unit.visibility = View.INVISIBLE
                tv_price.setTransformationMethod(WordReplacement.getInstance())
            } else {
                tv_unit.visibility = View.VISIBLE
                tv_price.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }
            walletModel.isShowRmb = !isChecked
            GreWalletOperator.updateIsShowRmb(walletModel, walletModel.isShowRmb!!)
        }


        view.findViewById<ImageView>(R.id.iv_copy).setOnClickListener {
            AppUtils.clipboardString(activity, walletModel.address)
            activity.showTopMsg("已复制")
        }
        view.findViewById<ImageView>(R.id.iv_detail).setOnClickListener {
            var walletData = BundleUtils.createWith(PageParamter.PAREMTER_WALLET, walletModel)
            activity.startActivity(WalletSettingActivity::class.java, walletData)
        }
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        allPages.remove(position)
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

    /**
     * 重写更新页面 ，更新显示的 view
     */
    override fun notifyDataSetChanged() {

        for (allPage in allPages) {
            initView(allPage.key, allPage.value)
        }
    }

    fun notifyItemDataChanged(position: Int) {
        if (allPages.containsKey(position)) {
            initView(position, allPages.get(position)!!)
        }
    }


}