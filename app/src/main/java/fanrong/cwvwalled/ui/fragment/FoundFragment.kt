package fanrong.cwvwalled.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.ui.activity.WebViewActivity
import fanrong.cwvwalled.ui.adapter.FoundListAdapter
import kotlinx.android.synthetic.main.fragment_found.*
import kotlinx.android.synthetic.main.layout_heard_found_fragment.view.*
import kotlinx.android.synthetic.main.layout_title.*
import net.sourceforge.http.model.FindItemModel

class FoundFragment : BaseFragment() {

    private lateinit var adapter: FoundListAdapter
    private lateinit var foundList: MutableList<FindItemModel>
    override fun getLayoutId(): Int {
        return R.layout.fragment_found
    }

    override fun initView() {
        toolbar_title.text="发现"
        iv_left_image.visibility=View.GONE
        rv_found_list.layoutManager = LinearLayoutManager(this.context)
        foundList = ArrayList()
        adapter = FoundListAdapter(foundList)
        rv_found_list.adapter = adapter
        val view: View = View.inflate(this.context, R.layout.layout_heard_found_fragment, null)
        view.tv_found_bit_star.setOnClickListener(this)
        view.iv_found_ccash.setOnClickListener(this)
        view.iv_found_bibbx.setOnClickListener(this)
        adapter.addHeaderView(view)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            val bundle = Bundle()
            bundle.putString("url", foundList[position].url)
            bundle.putString("title", foundList[position].titles)
            startActivity(WebViewActivity::class.java, bundle)
        }
        loadData()
    }

    override fun loadData() {
        foundList.add(FindItemModel("EtherCraft", "最活跃的以太坊游戏，超过100个智能合约！", R.drawable.find_item_image_1, "https://ethercraft.io"))
        foundList.add(FindItemModel("CryptoKitties", "每周交易超过102，047次！", R.drawable.find_item_image_2, "https://www.cryptokitties.co"))
        foundList.add(FindItemModel("Etheroll", "最受欢迎的去中心化赌场，交易量突破7898ETH！", R.drawable.find_item_image_3, "https://etheroll.com"))
        foundList.add(FindItemModel("Chibifighters", "以太坊区块链上随时待命的战士！", R.drawable.find_item_image_4, "https://chibifighters.io"))
        foundList.add(FindItemModel("EtherBots", "有趣酷炫的机器人主题区块链游戏！", R.drawable.find_item_image_5, "https://etherbots.io"))
        foundList.add(FindItemModel("Crypto Space Commander", "宇宙探索，执行P2P契约行为，运行游戏经济！", R.drawable.find_item_image_6, "https://www.csc-game.com"))
        foundList.add(FindItemModel("Axieinfinity", "饲养宠物来赚钱，有ETH奖励哦！", R.drawable.find_item_image_7, "https://axieinfinity.com"))
        foundList.add(FindItemModel("KryptoWar", "一周交易量4210次！", R.drawable.find_item_image_8, "https://kryptowar.com"))
        foundList.add(FindItemModel("Fishbank", "错过养猫，可不能再错过养鱼啦！", R.drawable.find_item_image_9, "https://fishbank.io"))
        foundList.add(FindItemModel("HyperDragons", "具有收藏价值，以及趣味游戏性的数字收藏品！", R.drawable.find_item_image_10, "https://hyperdragons.alfakingdom.com"))
        adapter.notifyDataSetChanged()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_found_bit_star -> {
                val bundle = Bundle()
                bundle.putString("url", "https://www.bitstar.in")
                bundle.putString("title", "Bitstar")
                startActivity(WebViewActivity::class.java, bundle)
            }
            R.id.iv_found_ccash -> {
                val bundle = Bundle()
                bundle.putString("url", "https://www.ccash.com")
                bundle.putString("title", "CCash")
                startActivity(WebViewActivity::class.java, bundle)
            }
            R.id.iv_found_bibbx -> {
                val bundle = Bundle()
                bundle.putString("url", "https://www.bibox365.com")
                bundle.putString("title", "Bibox")
                startActivity(WebViewActivity::class.java, bundle)
            }
        }
    }
}