package com.tlgbltcn.app.workhard.utils.service

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.tlgbltcn.app.workhard.R

class TimerService : Service() {

    private lateinit var countDownTimer: CountDownTimer
    private val NOTIFICATION = R.string.timer_service
    var notificationManager: NotificationManager? = null
    private var notification: Notification? = null
    val localIntent = Intent()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataWork = intent?.getLongExtra(getString(R.string.work), -1)
        val dataBreak = intent?.getLongExtra(getString(R.string.pause), -1)
        val countDownTime: Long
        val sendExtraString: String
        val sendExtraStringFinish: String
        countDownTime = if (dataWork!! > dataBreak!!) dataWork else dataBreak
        sendExtraString = if (dataWork > dataBreak) getString(R.string.work) else getString(R.string.pause)
        sendExtraStringFinish = if (dataWork > dataBreak) getString(R.string.work_finish) else getString(R.string.pause_finish)

        localIntent.action = AppConstant.SERVICE_TAG
        countDownTimer = object : CountDownTimer(countDownTime, 1000) {
            override fun onFinish() {
                localIntent.putExtra(sendExtraStringFinish, 0.toString())
                localIntent.action = sendExtraStringFinish
                sendBroadcast(localIntent)
            }

            override fun onTick(millisUntilFinished: Long) {
                val remainingMinutes: Int = ((millisUntilFinished / 1000) / 60).toInt()
                localIntent.putExtra(sendExtraString, remainingMinutes)
                localIntent.action = sendExtraString
                sendBroadcast(localIntent)
            }

        }.start()

        showNotification()

        return START_STICKY
    }

    override fun onDestroy() {
        countDownTimer.cancel()
    }

    private fun showNotification() {

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        } else {
            ""
        }

        val intent = Intent(this, TimerService::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, 0)

        notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_stop_black_24dp)
                .setTicker("text")
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.timer_service))
                .setContentText("text")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build()


        notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager!!.notify(NOTIFICATION, notification)
        startForeground(1, notification)

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = getString(R.string.timerService)
        val channelName = getString(R.string.workHardNotification)
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}