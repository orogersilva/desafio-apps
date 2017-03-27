package com.orogersilva.desafioinfoglobo

import android.app.Application
import android.os.StrictMode
import com.facebook.stetho.Stetho

/**
 * Created by orogersilva on 3/25/2017.
 */
class DesafioInfogloboApplication : Application() {

    // region APPLICATION LIFECYCLE METHODS

    override fun onCreate() {

        super.onCreate()

        if (BuildConfig.DEBUG) {

            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }

        Stetho.initializeWithDefaults(this)
    }

    // endregion
}