package com.momecarefoundation.app.util

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.momecarefoundation.app.service.DataBackUpJobService

class Utility {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun scheduleJob(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val js = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val job = JobInfo.Builder(100, ComponentName(context, DataBackUpJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .setPersisted(true)
                .setPeriodic(18000000L)
                .build()
            js.schedule(job)
        }
    }
}