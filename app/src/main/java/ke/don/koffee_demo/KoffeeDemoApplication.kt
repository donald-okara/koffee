package ke.don.koffee_demo

import android.app.Application
import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.ui.DefaultToast

class KoffeeDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Koffee.init {
            layout { DefaultToast(it) } // or custom
            dismissible(true)
            durationResolver { customDurationResolver(it) }
        }
    }


    private fun customDurationResolver(duration: ToastDuration): Long? = when (duration) {
        ToastDuration.Short -> 5000L
        ToastDuration.Medium -> 7000L
        ToastDuration.Long -> 10000L
        ToastDuration.Indefinite -> null
    }

}