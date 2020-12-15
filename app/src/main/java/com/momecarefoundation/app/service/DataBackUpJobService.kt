package com.momecarefoundation.app.service

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.model.User


class DataBackUpJobService : JobService() {

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        handleAction()
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }

    companion object {
        private const val tag = "DATA_JOB"
    }

    private fun handleAction() {
        User().getUser()?.let {
                APICall(applicationContext).backUpOfflineOrder()
        }
    }
}
