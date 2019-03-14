package xianchao.com.topmsg

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent

class OnStopObserver(var linear: () -> Unit) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun methodCall() {
        linear()
    }
}