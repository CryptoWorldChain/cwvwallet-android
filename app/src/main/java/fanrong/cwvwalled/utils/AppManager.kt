package fanrong.cwvwalled.utils

import android.app.Activity
import java.util.*

object AppManager {

    val stack = Stack<Activity>()

    fun removeActivity(activity: Activity) {
        stack.remove(activity)
    }

    public fun addActivity(activity: Activity) {
        stack.add(activity)
    }

    fun finishActivity(clazz: Class<out Activity>) {
        stack.forEach {
            if (it.javaClass == clazz) {
                stack.remove(it)
                it.finish()
            }
        }
    }

    fun popAllActivity() {
        for (activity in stack) {
            activity.finish()
        }
    }

}
