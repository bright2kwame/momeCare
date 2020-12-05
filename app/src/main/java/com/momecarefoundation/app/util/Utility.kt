package com.momecarefoundation.app.util

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.momecarefoundation.app.service.DataBackUpJobService
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

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

    // MARK: generate the letter image drawable
    fun getLetterView(activity: Context, letter: String, fontSize: Int, colorIn: Int? = null, roundShape: Boolean? = true): TextDrawable {
        val color = colorIn ?: ColorGenerator.MATERIAL.getColor(letter)
        val builderBase = TextDrawable.builder()
            .beginConfig()
            .withBorder(1)
            .fontSize(fontSize)
            .bold()
            .toUpperCase()
            .endConfig()

        var builder = builderBase.rect()
        if (roundShape!!) {
            builder = builderBase.round()
        }
        var firstLetter = ""
        if (letter.isNotEmpty()) {
            firstLetter = letter[0].toString().toUpperCase()
        }
        return builder.build(firstLetter, color)
    }

    fun formatRep(number: Int): String {
        if (number < 1000) {
            return number.toString()
        }
        val suffix = arrayOf("", "k", "m", "b", "t")
        val maxLength = 4
        var r = DecimalFormat("##0E0").format(number.toLong())
        r = r.replace("E[0-9]".toRegex(), suffix[Character.getNumericValue(r[r.length - 1]) / 3])
        while (r.length > maxLength || r.matches("[0-9]+\\.[a-z]".toRegex())) {
            r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
        }
        return r
    }


    fun formatItem(rep: Int, name: String): String {
        return if (rep == 1) {
            "$rep $name"
        } else {
            formatRep(rep) + (" " + (name + "s"))
        }
    }

    // MARK: get location from latitude and longitude
    /**
     * @param context, the context of the requesting activity
     * @param lat, the location latitude
     * @param lon, the location longitude
     */
    fun getLocationName(context: Context, lat: Double, lon: Double): String {
        var name = ""
        val geoCoder = Geocoder(context, Locale.getDefault())
        val builder = StringBuilder()
        try {
            val address = geoCoder.getFromLocation(lat, lon, 1)
            if (address.isNotEmpty()) {
                val maxLines = address[0].maxAddressLineIndex
                for (i in 0 until maxLines) {
                    val addressStr = address[0].getAddressLine(i)
                    builder.append(addressStr)
                    builder.append(" ")
                }
                name = builder.toString()
            }
        } catch (e: IOException) {
            // Handle IOException
        } catch (e: NullPointerException) {
            // Handle NullPointerException
        }

        return name
    }

}