package fanrong.cwvwalled.base

import android.arch.lifecycle.LifecycleOwner

abstract class BasePresenter {
    var lifecycleOwner: LifecycleOwner? = null
    fun bindLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }
}