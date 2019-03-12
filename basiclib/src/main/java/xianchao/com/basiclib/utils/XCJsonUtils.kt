package xianchao.com.basiclib.utils

import org.json.JSONObject

object XCJsonUtils {
    fun isJsonStr(string: String): Boolean {
        try {
            val jsonObject = JSONObject(string)
            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
    }

    fun getString(jsonObject: JSONObject,key: String):String{
        try {
            val string = jsonObject.getString(key)
            return string
        } catch (ex:Exception){
            ex.printStackTrace()
            return ""
        }
    }

}