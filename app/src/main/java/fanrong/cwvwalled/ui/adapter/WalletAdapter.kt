package fanrong.cwvwalled.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.litepal.GreWalletModel
import xianchao.com.basiclib.widget.BgLinearLayout

class WalletAdapter(var layoutResId: Int) : RecyclerView.Adapter<WalletAdapter.ViewHolder>() {
    var datas = mutableListOf<GreWalletModel>()

    fun setNewData(list: MutableList<GreWalletModel>) {
        datas.clear()
        datas.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(AppApplication.instance).inflate(layoutResId, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(viewholder: ViewHolder, p1: Int) {
        var item = datas[p1]
        if ("CWV".equals(item.walletType)) {
            viewholder.bgll_eth_wallet.setBackgroundResource(R.drawable.all_wallet_cwv_bg)
        } else {
            viewholder.bgll_eth_wallet.setBackgroundResource(R.drawable.all_wallet_eth_bg)
        }

        viewholder.tv_eth_wallet_name.text = item.walletName
        viewholder.tv_eth_address.text = item.address
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var bgll_eth_wallet = view.findViewById<LinearLayout>(R.id.bgll_eth_wallet)
        var tv_eth_wallet_name = view.findViewById<TextView>(R.id.tv_eth_wallet_name)
        var tv_eth_address = view.findViewById<TextView>(R.id.tv_eth_address)
    }

}