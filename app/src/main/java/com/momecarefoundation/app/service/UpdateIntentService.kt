package com.momecarefoundation.app.service

import android.app.IntentService
import android.content.Intent
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.util.AppConstants


class UpdateIntentService : IntentService("UpdateIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION == action) handleAction()
        }
    }

    private fun handleAction() {
        APICall(applicationContext).backUpOfflineOrder()
    }

    companion object {
        const val ACTION = AppConstants.ACTION
        const val tag = "UPDATE_SERVICE"
    }
}
