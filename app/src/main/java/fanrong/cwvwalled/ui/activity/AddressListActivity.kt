package fanrong.cwvwalled.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.AddressModel
import fanrong.cwvwalled.ui.adapter.AddressListAdapter
import fanrong.cwvwalled.ui.view.AddAddressDialog
import fanrong.cwvwalled.ui.view.FragmentAddAddressDialog
import fanrong.cwvwalled.ui.view.RecycleViewDivider
import fanrong.cwvwalled.ui.view.SwipeMenuView
import fanrong.cwvwalled.utils.LogUtil
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.layout_title.*
import org.litepal.LitePal
import xianchao.com.basiclib.utils.BundleUtils
import xianchao.com.basiclib.utils.CheckedUtils

class AddressListActivity : BaseActivity() {

    private lateinit var adapter: AddressListAdapter
    private lateinit var addressList: MutableList<AddressModel>


    var isPick = false

    override fun getLayoutId(): Int {
        return R.layout.activity_address_list
    }

    override fun initView() {

        isPick = intent.getBooleanExtra(PageParamter.PAREMTER_IS_PICK, false)

        toolbar_title.text = "地址本"
        iv_right_image.visibility = View.VISIBLE
        iv_right_image.setImageResource(R.drawable.add_address_icon)
        iv_right_image.setOnClickListener {
            showAddAddressDialog()
        }

        iv_left_image.setOnClickListener { finish() }

        loadData()
    }

    private fun showAddAddressDialog() {

        var dialog = FragmentAddAddressDialog()
        dialog.show(supportFragmentManager, "commentDialog");

//        AddAddressDialog(this).showPop(
//                object : AddAddressDialog.OnOkListener {
//                    override fun OnOk(name: String, address: String) {
//                        LogUtil.d("$name,$address")
//                        if (CheckedUtils.nonEmpty(name) && CheckedUtils.nonEmpty(address)) {
//                            AddressModel(name, address).save()
//                            addressList.clear()
//                            addressList.addAll(LitePal.findAll(AddressModel::class.java))
//                            LogUtil.d(addressList.size.toString())
//                            adapter.notifyDataSetChanged()
//                            rv_address_book.scrollToPosition(adapter.itemCount - 1)
//                            if (addressList.size > 0) {
//                                iv_center_no_record.visibility = View.GONE
//                            } else {
//                                iv_center_no_record.visibility = View.VISIBLE
//                            }
//                        }
//                    }
//                }
//        )
    }

    override fun onClick(v: View) {

    }

    override fun loadData() {
        addressList = LitePal.findAll(AddressModel::class.java)
        if (addressList.size > 0) {
            iv_center_no_record.visibility = View.GONE
        }
        adapter = AddressListAdapter(addressList)
        rv_address_book.adapter = adapter
        rv_address_book.layoutManager = LinearLayoutManager(this)
        rv_address_book.addItemDecoration(RecycleViewDivider(this,
                LinearLayoutManager.VERTICAL, 10, R.color.divide_gray_color))

        if (isPick) {
            adapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
                override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    val model = adapter!!.data[position] as AddressModel
                    val intent = Intent()
                    intent.putExtras(BundleUtils.createWith(PageParamter.RESULT_ADDRESS, model))
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })
        } else {

            adapter.setOnSwipeListener(object : AddressListAdapter.OnSwipeListener {
                override fun onDel(helper: BaseViewHolder, id: Long) {
                    (helper.itemView as SwipeMenuView).quickClose()
                    LogUtil.e(helper.layoutPosition.toString() + "," + id)
                    LitePal.delete(AddressModel::class.java, id)
                    adapter.remove(helper.adapterPosition)
                    if (addressList.size == 0) {
                        iv_center_no_record.visibility = View.VISIBLE
                    }
                }
            })
        }

    }
}
