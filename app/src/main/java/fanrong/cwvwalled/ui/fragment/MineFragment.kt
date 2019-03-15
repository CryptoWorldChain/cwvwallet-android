package fanrong.cwvwalled.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.pm.PermissionInfoCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yzq.zxinglibrary.common.Constant
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.UserInfoObject
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.ui.activity.*
import fanrong.cwvwalled.ui.view.ButtomDialogView
import fanrong.cwvwalled.ui.view.EditNickNameDialog
import fanrong.cwvwalled.ui.view.ExitDialog
import fanrong.cwvwalled.utils.RealPathFromUriUtils
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.fragment_mine.*
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.checkNotEmpty
import java.io.File
import java.security.Permission
import kotlin.concurrent.thread

class MineFragment : BaseFragment() {

    val HEADER_START_PICK = 1011
    val HEADER_CAMERA = 1012

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {
        tv_mine_logout.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_mine_logout.paint.isAntiAlias = true

        setting_item_setting.setmOnLSettingItemClick {
            startActivity(UseSettingActivity::class.java)
        }

        setting_item_contact.setmOnLSettingItemClick {
            startActivity(AddressListActivity::class.java)
        }

        setting_item_about.setmOnLSettingItemClick {
            startActivity(AboutUsActivity::class.java)
        }

        setting_item_agreement.setmOnLSettingItemClick {
            val bundle = Bundle()
            bundle.putString("url", "file:///android_asset/user_agreement.html")
            bundle.putString("title", "用户协议")
            startActivity(WebViewActivity::class.java, bundle)
        }
        if (UserInfoObject.nickName.checkNotEmpty()) {
            tv_username.text = UserInfoObject.nickName
        }
        tv_username.setOnClickListener {
            val nameDialog = EditNickNameDialog(activity!!)
            nameDialog.btnListener = object : FRDialogBtnListener {
                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }

                override fun onConfirm(dialog: Dialog) {
                    dialog.dismiss()
                    if (nameDialog.nickname.checkNotEmpty()) {
                        UserInfoObject.nickName = nameDialog.nickname
                        tv_username.text = nameDialog.nickname
                    }
                }
            }
            nameDialog.show()
        }

        civ_header.setOnClickListener {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)
                    .subscribe {
                        if (it) {
                            showBottomDialog()
                        } else {
                            showTopMsg("请打开相机和读取文件权限")
                        }
                    }
        }
        if (CheckedUtils.nonEmpty(UserInfoObject.userHeader)) {
            Thread {
                var file = File(UserInfoObject.userHeader)
                var get: Bitmap? = null
                if (file != null && file.length() > 0) {
                    get = Picasso.get().load(file).get()
                }
                if (get != null) {
                    activity!!.runOnUiThread {

                        civ_header.setImageBitmap(get)
                    }
                }
            }.start()
        }

        tv_mine_logout.setOnClickListener {
            showLogout()
        }

    }


    private var pathname: String? = null
    fun showBottomDialog() {

        val dialogView = ButtomDialogView(activity)
        dialogView.show()

        val tv_make = dialogView.findViewById<TextView>(R.id.tv_make)
        val tv_pick = dialogView.findViewById<TextView>(R.id.tv_pick)
        val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)

        tv_make.setOnClickListener(View.OnClickListener {
            dialogView.dismiss()

            // 跳转到系统照相机
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(activity!!.getPackageManager()) != null) {
                // 设置系统相机拍照后的输出路径
                // 创建临时文件
                pathname = (Environment.getExternalStoragePublicDirectory("").path
                        + "/" + System.currentTimeMillis() + "fanrongheader.jpg")
                if (File(pathname).exists()) {
                    File(pathname).delete()
                }
                val fileUri = Uri.fromFile(File(pathname))
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                startActivityForResult(cameraIntent, HEADER_CAMERA)
            }
        })
        tv_pick.setOnClickListener(View.OnClickListener {
            dialogView.dismiss()
            val intent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, HEADER_START_PICK)
        })
        tv_cancel.setOnClickListener(View.OnClickListener { dialogView.cancel() })
    }

    /**
     * 退出
     */
    private fun showLogout() {
        var dialog = ExitDialog(activity!!)
        dialog.btnlistener = object : FRDialogBtnListener {
            override fun onCancel(dialog: Dialog) {
                dialog.dismiss()
            }

            override fun onConfirm(dialog: Dialog) {
                dialog.dismiss()
                UserInfoObject.userLogout()
                startActivity(CreateAccountPreActivity::class.java)
                activity!!.finish()
            }
        }
        dialog.show()

    }

    override fun loadData() {
    }

    override fun onClick(v: View?) {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {


            HEADER_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    Picasso.get().load(File(pathname)).into(civ_header)
                    UserInfoObject.userHeader = pathname!!
                }
            }

            HEADER_START_PICK -> {
                if (resultCode == Activity.RESULT_OK) {
                    val realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(activity!!, data!!.data)
                    Picasso.get().load(File(realPathFromUri)).into(civ_header)

                    UserInfoObject.userHeader = realPathFromUri
                }
            }

        }
    }


}