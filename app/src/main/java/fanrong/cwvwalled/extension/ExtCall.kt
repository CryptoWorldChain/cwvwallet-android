package fanrong.cwvwalled.extension

import android.arch.lifecycle.LifecycleOwner
import fanrong.cwvwalled.utils.SWLog
import retrofit2.Call
import xianchao.com.topmsg.OnDestroyObserver

inline fun <T> Call<T>.bindLifecycleOwner(lifecycleOwner: LifecycleOwner?): Call<T> {

    lifecycleOwner?.lifecycle?.addObserver(object : OnDestroyObserver() {
        override fun onDestroyCall() {
            SWLog.e("cancel::::" + request().toString())
            cancel()
        }
    })
    return this
}