package ru.otus.demo.services.bound

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import ru.otus.demo.services.bound.BoundService.DemoBinder

class DemoServiceConnection : ServiceConnection {
    var service: BoundService? = null
        private set
    private var attemptingToBind = false
    private var bound = false
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        this.service = (service as DemoBinder).service
        attemptingToBind = false
        bound = true
    }

    override fun onServiceDisconnected(name: ComponentName) {
        service = null
        bound = false
    }

    fun bindToService(ctx: Context) {
        if (!attemptingToBind) {
            attemptingToBind = true
            ctx.bindService(Intent(ctx, BoundService::class.java), this, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindFromService(ctx: Context) {
        attemptingToBind = false
        if (bound) {
            ctx.unbindService(this)
            bound = false
        }
    }

    val isConnected: Boolean
        get() = service != null && bound
}