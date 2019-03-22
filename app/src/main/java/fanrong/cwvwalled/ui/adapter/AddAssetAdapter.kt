package fanrong.cwvwalled.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.squareup.picasso.Picasso
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.litepal.GreNodeModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import net.sourceforge.http.model.CoinBean
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.checkIsEmpty

class AddAssetAdapter() : RecyclerView.Adapter<AddAssetAdapter.MViewHolder>() {

    var checkedChangeListener: OnItemCheckedChangeListener? = null
    var coinBeans: List<CoinBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAssetAdapter.MViewHolder {
        val inflate = LayoutInflater.from(AppApplication.instance).inflate(R.layout.item_add_asset_adapter, parent, false)
        return MViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return if (coinBeans == null) {
            0
        } else {
            coinBeans!!.size
        }
    }

    override fun onBindViewHolder(holder: AddAssetAdapter.MViewHolder, position: Int) {
        val coinBean = coinBeans!![position]
        holder.tv_coin_symbol.text = coinBean.coin_symbol
        holder.tv_coin_name.text = coinBean.coin_name
        holder.tv_contract_addr.text = coinBean.contract_addr
        if (coinBean.contract_addr.checkIsEmpty()) {
            holder.tv_contract_addr.visibility = View.GONE
        } else {
            holder.tv_contract_addr.visibility = View.VISIBLE
        }

        Picasso.get().load(R.drawable.asset_defaut_icon).into(holder.iv_coin_icon)

        holder.sh_switch.isChecked = coinBean.isOpen!!

        if (checkedChangeListener != null) {
            holder.sh_switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    checkedChangeListener!!.onItemChange(coinBean, isChecked)
                }
            })
        }
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sh_switch: Switch = view.findViewById(R.id.sh_switch)
        var iv_coin_icon: ImageView = view.findViewById(R.id.iv_coin_icon)
        var tv_coin_symbol: TextView = view.findViewById(R.id.tv_coin_symbol)
        var tv_coin_name: TextView = view.findViewById(R.id.tv_coin_name)
        var tv_contract_addr: TextView = view.findViewById(R.id.tv_contract_addr)
    }


    public interface OnItemCheckedChangeListener {
        fun onItemChange(coinBean: CoinBean, ischecked: Boolean)
    }

}