package fanrong.cwvwalled.ui.activity

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.ui.fragment.ExportKeyFragment
import fanrong.cwvwalled.ui.fragment.ExportKeyQRcodeFragment
import fanrong.cwvwalled.ui.fragment.ImportKeyFragment
import fanrong.cwvwalled.ui.fragment.ImportWordFragment
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.activity_export_private_key.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import java.util.ArrayList

class ImportWalletActivity : BaseActivity() {


    lateinit var walletType: String


    override fun getLayoutId(): Int {
        return R.layout.activity_import_wallet
    }

    lateinit var mDataList: ArrayList<String>
    override fun initView() {

        walletType = intent.getStringExtra(PageParamter.PAREMTER_WALLET_TYPE)
        SWLog.e("ImportWalletActivity  wallet   ->${walletType}")

        setTitleText("导入${walletType}钱包")
        setLeftImgOnclickListener(View.OnClickListener { finish() })

        mDataList = ArrayList<String>()
        mDataList.add("助记词")
        mDataList.add("私钥")
        view_pager.setOffscreenPageLimit(2)
        view_pager.setAdapter(NewsAdapter(supportFragmentManager))

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.normalColor = resources.getColor(R.color.white)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.default_blue_color)
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                simplePagerTitleView.setText(mDataList.get(index))

                simplePagerTitleView.setOnClickListener { view_pager.setCurrentItem(index) }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineWidth = AppUtils.dp2px(this@ImportWalletActivity, 90).toFloat()
                indicator.lineHeight = AppUtils.dp2px(this@ImportWalletActivity, 1).toFloat()
                indicator.setColors(resources.getColor(R.color.default_blue_color))
                indicator.yOffset = UIUtil.dip2px(context, 2.0).toFloat()
                return indicator
            }


        }
        magic_indicator.navigator = commonNavigator
        val titleContainer = commonNavigator.titleContainer // must after setNavigator
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtil.dip2px(this@ImportWalletActivity, 15.0)
            }
        }

        val fragmentContainerHelper = FragmentContainerHelper(magic_indicator)
        fragmentContainerHelper.setInterpolator(OvershootInterpolator(2.0f))
        fragmentContainerHelper.setDuration(300)
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                fragmentContainerHelper.handlePageSelected(position)
            }
        })


    }


    override fun onClick(v: View) {
    }

    override fun loadData() {
    }


    private inner class NewsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    var fragment = ImportWordFragment()
                    val bundle = Bundle()
                    bundle.putSerializable(PageParamter.PAREMTER_WALLET_TYPE, walletType)
                    fragment.arguments = bundle
                    return fragment
                }
                else -> {
                    var fragment = ImportKeyFragment()
                    val bundle = Bundle()
                    bundle.putSerializable(PageParamter.PAREMTER_WALLET_TYPE, walletType)
                    fragment.arguments = bundle
                    return fragment
                }
            }
        }

        override fun getCount(): Int {
            return if (mDataList == null) 0 else mDataList.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }

}
