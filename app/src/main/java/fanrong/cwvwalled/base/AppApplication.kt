package fanrong.cwvwalled.base

import android.app.Application
import android.os.StrictMode
import fanrong.cwvwalled.utils.CallJsCodeUtils

class AppApplication : Application() {

    companion object {
        lateinit var instance: AppApplication
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        CallJsCodeUtils.init()
    }


}