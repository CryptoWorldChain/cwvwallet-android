package xianchao.com.basiclib

import android.app.Application
import android.os.Looper
import android.view.Gravity
import java.lang.IllegalStateException
import java.security.AccessControlContext
import java.util.logging.Handler

object BasicLibComponant {
    private lateinit var context: Application

    var mainHandler = android.os.Handler(Looper.getMainLooper())

    fun getContext(): Application {
        if (context == null) {
            throw IllegalStateException("BasicLibComponant must be init")
        }
        return context
    }

    fun init(context: Application) {
        this.context = context
//        ToastUtils.init(context)
//        ToastUtils.setGravity(Gravity.NO_GRAVITY, 0, 0)
    }

    fun postMainDelay(action: Runnable, delay: Long) {
        mainHandler.postDelayed(action, delay)
    }

    fun postMainDelay(action: Runnable) {
        mainHandler.postDelayed(action, 0)
    }

    fun postMainThread(run: () -> Unit) {
        mainHandler.post(run)
    }

}