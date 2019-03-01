package xianchao.com.basiclib.utils

import android.text.TextUtils
import org.json.JSONException
import org.json.JSONObject

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