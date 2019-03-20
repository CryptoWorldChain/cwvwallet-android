package fanrong.cwvwalled

import com.google.gson.Gson
import fanrong.cwvwalled.http.model.UpdateResp
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import java.util.zip.DataFormatException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    var chartXText = arrayOfNulls<String>(6)
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        var a = "{ \"current_version\": \"1.0.0.0\", \"previous_version\": \"0.0.0.9\", \"release_date\": \"2019年03月20日\", \"download_url\": \"https://download.docker.com/win/stable/Docker%20for%20Windows%20Installer.exe\", \"mandatory_update\": false, \"t\": \"愚人节版系统更新提示\", \"p\": [\"迎接愚人节，升级愚人节版系统，即可在随时随地查询中国和其他国家地区大众人民别愚情况。\", \"一句搞定！\", \"具体事项：\"], \"li\": [\"1.系统更新需要好费流量\", \"2.升级过程中请勿手动关闭\", \"3.没有什么可说的啦\"] }"
        val fromJson = Gson().fromJson(a, UpdateResp::class.java)
        println(Arrays.toString(chartXText))
        Collections.reverse(Arrays.asList(chartXText))
    }
}
