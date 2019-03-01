package fanrong.cwvwalled.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.ui.fragment.FinancialFragment
import kotlinx.android.synthetic.main.item_financial.view.*
import java.util.zip.Inflater

class FinancialAdapter() :
        RecyclerView.Adapter<FinancialAdapter.MyViewHolder>() {

    var onItemClickListener: AdapterView.OnItemClickListener? = null

    var newData: MutableList<FinancialFragment.FinancialItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var layoutId: Int? = null

    constructor(int: Int) : this() {
        layoutId = int
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = LayoutInflater.from(AppApplication.instance).inflate(R.layout.item_financial, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return if (newData == null) 0 else newData!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val financialItem = newData!![position]
        holder.tv_name.text = financialItem.financialTitle
        holder.tv_rate.text = financialItem.interestRate
        holder.tv_period.text = financialItem.cycle

        holder.view.setOnClickListener {
            onItemClickListener?.onItemClick(null, it, position, 0)
        }
    }


    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        var tv_rate = view.findViewById<TextView>(R.id.tv_rate)
        var tv_name = view.findViewById<TextView>(R.id.tv_name)
        var tv_period = view.findViewById<TextView>(R.id.tv_period)

    }

}