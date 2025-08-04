package ke.don.koffee_demo

import android.app.Application
import ke.don.koffee.domain.Koffee
import ke.don.koffee.ui.DefaultToast

class KoffeeDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Koffee.init {
            layout { DefaultToast(it) } // or custom
            dismissible(true)
        }
    }
}
