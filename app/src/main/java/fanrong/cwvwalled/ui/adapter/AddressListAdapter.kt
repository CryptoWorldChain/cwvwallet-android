package fanrong.cwvwalled.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.litepal.AddressModel
import fanrong.cwvwalled.utils.LogUtil
import kotlinx.android.synthetic.main.item_address_book.view.*

class AddressListAdapter(data: List<AddressModel>)
    : BaseQuickAdapter<AddressModel, BaseViewHolder>(R.layout.item_address_book, data) {

    override fun convert(helper: BaseViewHolder, item: AddressModel) {
        helper.setText(R.id.tv_address_name, item.name)
                .setText(R.id.tv_address_remark, item.address)
        helper.itemView.tv_del_address.setOnClickListener {
            onSwipeListener?.onDel(helper, item.id)
        }
    }

    /**
     * 和Activity通信的接口
     */
    interface OnSwipeListener {
        fun onDel(helper: BaseViewHolder, id: Long)
    }

    private var onSwipeListener: OnSwipeListener? = null

    fun setOnSwipeListener(onSwipeListener: OnSwipeListener) {
        this.onSwipeListener = onSwipeListener
    }
}