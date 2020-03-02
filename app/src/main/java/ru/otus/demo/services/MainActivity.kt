package ru.otus.demo.services

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.otus.demo.services.DemoIntentService
import ru.otus.demo.services.DemoService
import ru.otus.demo.services.bound.DemoServiceConnection
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val mConnection = DemoServiceConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startIntentService(view: View?) {
        val startIntentService = Intent(this, DemoIntentService::class.java)
        startService(startIntentService)
    }

    fun startService(view: View?) {
        val startService = Intent(this, DemoService::class.java)
        startService(startService)
    }

    fun startBoundService(view: View?) {
        mConnection.bindToService(this)
    }

    fun getTimeBoundService(view: View?) {
        var text = "there is no time to explain"
        if (mConnection.isConnected) {
            val time = mConnection.service!!.time
            text = "Current time is:\n" +
                    SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                            .format(time)
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        //Oops! ;)
        //Mistakes were made. You should find one of them to get time.
    }

    fun unbindBoundService(view: View?) {
        mConnection.unbindFromService(this)
    }

    override fun onStop() {
        mConnection.unbindFromService(this)
        super.onStop()
    }
}