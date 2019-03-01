package fanrong.cwvwalled.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.utils.LogUtil
import kotlinx.android.synthetic.main.activity_financial_web_view.*
import xianchao.com.basiclib.utils.CheckedUtils

class WebViewActivity : BaseActivity() {
    override fun onClick(v: View) {
    }

    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_financial_web_view
    }

    companion object {
        val PARAMTER_URL = "url"
        val PARAMTER_TITLE = "title"
    }

    override fun initView() {
        if (CheckedUtils.isEmpty(intent.getStringExtra(PARAMTER_TITLE))) {
            setTitleText("理财")
        } else {
            setTitleText(intent.getStringExtra(PARAMTER_TITLE))
        }
        setLeftImgOnclickListener {
            finish()
        }
        webview.setBackgroundColor(resources.getColor(R.color.black))

        val settings = webview.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        webview.webViewClient = WebViewClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        val url = intent.getStringExtra(PARAMTER_URL)
        LogUtil.e(url)
        webview.loadUrl(url)
    }
}
