package fanrong.cwvwalled.parenter

import android.graphics.Bitmap
import fanrong.cwvwalled.eventbus.CWVNoteChangeEvent
import org.cwv.client.sdk.Config
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ProjectEventHandler {

    companion object {
        private var lateObj: ProjectEventHandler? = null

        @Synchronized
        fun init() {
            if (lateObj == null) {
                lateObj = ProjectEventHandler()
                EventBus.getDefault().register(lateObj)
            }
        }
    }

    private constructor()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: CWVNoteChangeEvent) {
        Config.changeHost(event?.gnodeModel?.node_url ?: "")
    }

}
