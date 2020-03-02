package ru.otus.demo.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

class DemoService : Service() {
    private var mServiceLooper: Looper? = null
    private var mServiceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler internal constructor(looper: Looper?) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            Log.d(TAG, "handleMessage, msg.arg1:[" + msg.arg1 + "]")
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate()")
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        val thread: HandlerThread = object : HandlerThread(TAG,
                Process.THREAD_PRIORITY_BACKGROUND) {
            override fun run() {
                Log.d(TAG, "run()")
                super.run()
                Log.d(TAG, "super.run()")
            }
        }
        thread.start()
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.looper
        mServiceHandler = ServiceHandler(mServiceLooper)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        val msg = mServiceHandler!!.obtainMessage()
        msg.arg1 = startId
        mServiceHandler!!.sendMessage(msg)
        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind()")
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
    }

    companion object {
        private val TAG = DemoService::class.java.simpleName
    }
}