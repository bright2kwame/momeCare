package com.momecarefoundation.app


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.bottomSheet.BottomSheetChangePassword
import com.momecarefoundation.app.bottomSheet.BottomSheetUpdateProfile
import com.momecarefoundation.app.callback.BaseInterface
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.fragment.Respondents
import com.momecarefoundation.app.fragment.Responses
import com.momecarefoundation.app.fragment.Surveys
import com.momecarefoundation.app.model.Survey
import com.momecarefoundation.app.model.User
import com.momecarefoundation.app.util.AppPresenter
import com.momecarefoundation.app.util.Utility
import kotlinx.android.synthetic.main.main_home.*


class Home : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val tag = "HOME"
    }

    override fun onClick(view: View?) {
        when (view) {
            materialCardViewLogout -> {
                AppPresenter(this).showMessage(
                    message = "Logging out means you will loose all records that have not been submitted. To proceed make sure you submit all records",
                    positiveAction = "YES",
                    negativeActive = "LATER",
                    presenterCallback = object : PresenterCallback {
                        override fun onActionSelected(item: Any) {
                            super.onActionSelected(item)
                            val action = item as String
                            if (action == "YES") {
                                Utility().recreateTask(this@Home)
                            }
                        }
                    })
            }
            materialCardViewTakeSurvey -> {
                startActivity(Intent(this, Surveys::class.java))
            }
            materialCardViewSurveys -> {
                startActivity(Intent(this, Responses::class.java))
            }
            materialCardViewResponds -> {
                startActivity(Intent(this, Responses::class.java))
            }
            materialCardViewAddRespondents -> {
                startActivity(Intent(this, AddRespondent::class.java))
            }
            materialCardViewRespondents -> {
                startActivity(Intent(this, MyRespondents::class.java))
            }
            materialCardViewPassword -> {
                BottomSheetChangePassword.newInstance(object : PresenterCallback {
                    override fun onActionSelected(item: Any) {
                        super.onActionSelected(item)
                        updateProfileView()
                    }
                }).show(supportFragmentManager, tag)
            }
            materialCardViewSettings -> {
                BottomSheetUpdateProfile.newInstance(object : PresenterCallback {
                    override fun onActionSelected(item: Any) {
                        super.onActionSelected(item)
                        updateProfileView()
                    }
                }).show(supportFragmentManager, tag)
            }
        }
    }


    private fun requestLocationPermission() {
        //MARK: ask permission
        requestMultiplePermissions.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            // Do something if some permissions granted or denied
            permissions.entries.forEach {
                // Do checking here

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_home)


        updateProfileView()
        updateOverView()

        materialCardViewLogout.setOnClickListener(this)
        materialCardViewPassword.setOnClickListener(this)
        materialCardViewSettings.setOnClickListener(this)
        materialCardViewTakeSurvey.setOnClickListener(this)
        materialCardViewSurveys.setOnClickListener(this)
        materialCardViewAddRespondents.setOnClickListener(this)
        materialCardViewResponds.setOnClickListener(this)
        materialCardViewRespondents.setOnClickListener(this)

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                // You can use the API that requires the permission.
            }
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {

            }
            else -> {
                requestLocationPermission()
            }
        }

        getOverviewData()
    }

    private fun updateProfileView() {
        val user = User().getUser()
        user?.let {
            textViewName.text = it.fullName
            textViewInfo.text = it.number
            textViewNameLetter.text = it.fullName.first().toString()

            val textDrawable = Utility().getLetterView(this, it.fullName, 60, null, true)
            imageViewIcon?.setImageDrawable(textDrawable)
        }

    }

    private fun getOverviewData(){
        //MARK: upload offline
        APICall(this).backUpOfflineOrder()
        APICall(this).getOverview(null, null, null, object : BaseInterface {
            override fun onRequestEnded() {
                super.onRequestEnded()
                updateOverView()
            }
        })
    }

    override fun onResume() {
        super.onResume()

    }

    private fun updateOverView() {
        textViewSurvey.text =
            PreferenceManager.getDefaultSharedPreferences(this).getString("TOTAL_SURVEYS", "--")
                ?: "--"

        textViewResponds.text =
            PreferenceManager.getDefaultSharedPreferences(this).getString("TOTAL_RESPONSES", "--")
                ?: "--"

        textViewRespondents.text =
            PreferenceManager.getDefaultSharedPreferences(this).getString("TOTAL_RESPONDENTS", "--")
                ?: "--"
    }
}