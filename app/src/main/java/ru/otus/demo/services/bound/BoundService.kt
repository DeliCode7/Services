package ru.otus.demo.services.bound

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import ru.otus.demo.services.bound.BoundService

class BoundService : Service() {
    private val mBinder: IBinder = DemoBinder()
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    internal inner class DemoBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }

    val time: Long
        get() = System.currentTimeMillis()
}