package fanrong.cwvwalled.common

object ThreadExecutor {

    fun execute(runnable: Runnable) {
        Thread(runnable).start()
    }

}