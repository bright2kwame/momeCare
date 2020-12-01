package com.momecarefoundation.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.momecarefoundation.app.util.AppConstants
import com.momecarefoundation.app.util.Utility


class InternetPromptReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Utility().scheduleJob(context)
            } else {
                val service = Intent(context, UpdateIntentService::class.java)
                service.action = AppConstants.ACTION
                context.startService(service)
            }
        }
    }
}
