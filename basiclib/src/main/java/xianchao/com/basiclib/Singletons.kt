package xianchao.com.basiclib

import com.google.gson.Gson

object Singletons {

    private var gson: Gson? = null

    @Synchronized
    fun obtainGson(): Gson {
        if (gson == null) {
            gson = Gson()
        }
        return gson!!
    }

}