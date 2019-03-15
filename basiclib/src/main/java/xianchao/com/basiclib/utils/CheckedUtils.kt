package xianchao.com.basiclib.utils

import android.text.TextUtils
import android.util.Log.i
import org.json.JSONException
import org.json.JSONObject

public inline fun String?.checkIsEmpty(): Boolean {
    return this == null || this.isEmpty()
}

public inline fun String?.checkNotEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

public inline fun List<*>?.checkIsEmpty(): Boolean {
    return this == null || this.isEmpty()
}

public inline fun List<*>?.checkNotEmpty(): Boolean {
    return this != null && !this.isEmpty()
}


public inline fun Map<*, *>?.checkIsEmpty(): Boolean {
    return this == null || this.isEmpty()
}

public inline fun Map<*, *>?.checkNotEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

public inline fun MutableMap<String, String>.putSafety(key: String, value: String?) {
    this.put(key, if (value.checkIsEmpty()) "" else value!!)
}

object CheckedUtils {
    fun isEmpty(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }


    fun nonEmpty(list: List<*>?): Boolean {
        return !(list == null || list.isEmpty())
    }


    fun isEmpty(map: Map<*, *>?): Boolean {
        return map == null || map.isEmpty()
    }

    fun nonEmpty(map: Map<*, *>?): Boolean {
        return !(map == null || map.isEmpty())
    }


    fun isEmpty(str: String?): Boolean {
        return TextUtils.isEmpty(str)
    }

    fun nonEmpty(str: String?): Boolean {
        return !TextUtils.isEmpty(str)
    }

    fun nonEmpty(str: String?, nonEmpty: () -> Unit) {
        if (!TextUtils.isEmpty(str)) {
            nonEmpty()
        }
    }


    fun isJson(json: String?): Boolean {

        if (isEmpty(json)) {
            return false
        }

        try {
            val jsonObject = JSONObject(json)
            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }

    }

}