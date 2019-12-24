package fanrong.cwvwalled.common

object ThreadExecutor {

    fun execute(runnable: Runnable) {
        Thread(object : Runnable {
            override fun run() {
                try {
                    runnable.run()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }).start()
    }

}