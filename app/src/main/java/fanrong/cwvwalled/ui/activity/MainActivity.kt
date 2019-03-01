package fanrong.cwvwalled.ui.activity

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    var selectIcon = mutableListOf<Int>()
    var unselectIcon = mutableListOf<Int>()

    var imageviews = mutableListOf<ImageView>()
    var textviews = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }


    override fun initView() {
        ll_tab_1.setOnClickListener(this)
        ll_tab_2.setOnClickListener(this)
        ll_tab_3.setOnClickListener(this)
        ll_tab_4.setOnClickListener(this)
        ll_tab_5.setOnClickListener(this)

        imageviews.add(imageview1)
        imageviews.add(imageview2)
        imageviews.add(imageview3)
        imageviews.add(imageview4)
        imageviews.add(imageview5)

        textviews.add(textview1)
        textviews.add(textview2)
        textviews.add(textview3)
        textviews.add(textview4)
        textviews.add(textview5)

        unselectIcon.add(R.drawable.home_icon_unselect_1)
        unselectIcon.add(R.drawable.home_icon_unselect_2)
        unselectIcon.add(R.drawable.home_icon_unselect_3)
        unselectIcon.add(R.drawable.home_icon_unselect_4)
        unselectIcon.add(R.drawable.home_icon_unselect_5)

        selectIcon.add(R.drawable.home_icon_select_1)
        selectIcon.add(R.drawable.home_icon_select_2)
        selectIcon.add(R.drawable.home_icon_select_3)
        selectIcon.add(R.drawable.home_icon_select_4)
        selectIcon.add(R.drawable.home_icon_select_5)

        updataTab(0)
        changePage(0)

    }

    fun changePage(index: Int) {
    }


    fun updataTab(index: Int) {
        for (textview in textviews) {
            textview.setTextColor(Color.parseColor("#908e8e"))
        }
        imageviews.forEachIndexed { index, imageView ->
            imageView.setImageResource(selectIcon[index])
        }

        imageviews[index].setImageResource(selectIcon[index])
        textviews[index].setTextColor(Color.parseColor("#ffe5bd"))

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_tab_1 -> {
                updataTab(1)
            }
            R.id.ll_tab_2 -> {
                updataTab(2)
            }
            R.id.ll_tab_3 -> {
                updataTab(3)
            }
            R.id.ll_tab_4 -> {
                updataTab(4)
            }
            R.id.ll_tab_5 -> {
                updataTab(5)
            }
        }
    }

    override fun loadData() {
    }

}
