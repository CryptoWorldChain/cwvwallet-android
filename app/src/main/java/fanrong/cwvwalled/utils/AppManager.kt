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
        val iterator = stack.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.javaClass == clazz) {
                iterator.remove()
                next.finish()
            }
        }
        stack.forEach {
        }
    }

    fun popAllActivity() {
        for (activity in stack) {
            activity.finish()
        }
    }

}
