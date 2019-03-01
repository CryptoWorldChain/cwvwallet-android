package fanrong.cwvwalled.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import net.sourceforge.http.model.FindItemModel

class FoundListAdapter(data: List<FindItemModel>)
    :BaseQuickAdapter<FindItemModel,BaseViewHolder>(R.layout.item_found,data) {
    override fun convert(helper: BaseViewHolder?, item: FindItemModel?) {
        helper?.run {
            setImageResource(R.id.iv_found_image, item!!.img)
            setText(R.id.tv_found_title, item.titles)
            setText(R.id.tv_found_dess, item.dess)
        }
    }

}