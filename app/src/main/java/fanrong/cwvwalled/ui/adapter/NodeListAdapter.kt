package fanrong.cwvwalled.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.litepal.GreNodeModel
import net.sourceforge.listener.FRItemClickListener

class NodeListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var nodes: MutableList<GreNodeModel>? = null
    var selectedIndex = 0
        set(value) {
            field = value
            nodes?.forEach {
                it.isUsing = false
            }
            nodes?.get(value)?.isUsing = true
        }

    var itemClickListener: FRItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(AppApplication.instance)
                .inflate(R.layout.activity_node_list_item, parent, false)
        return object : RecyclerView.ViewHolder(inflate) {}
    }

    override fun getItemCount(): Int {
        return if (nodes == null) {
            0
        } else {
            nodes!!.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener?.itemClick(position)
        }

        val tv_node_url = holder.itemView.findViewById<TextView>(R.id.tv_node_url)
        val iv_selected = holder.itemView.findViewById<ImageView>(R.id.iv_selected)
        tv_node_url.setText(nodes?.get(position)?.node_url)
        if (nodes?.get(position)!!.isUsing!!) {
            iv_selected.visibility = View.VISIBLE
        } else {
            iv_selected.visibility = View.GONE
        }
    }
}