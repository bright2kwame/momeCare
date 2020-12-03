package com.momecarefoundation.app.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.momecarefoundation.app.R
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.callback.UserCallback
import com.momecarefoundation.app.util.AppPresenter
import kotlinx.android.synthetic.main.bottom_sheet_reset_password.*

/**
 * Created by Monarchy on 11/10/2017.
 */
class BottomSheetResetPassword : RoundedBottomSheetDialogFragment(), View.OnClickListener {

    private var action: PresenterCallback? = null
    private var number: String = ""

    override fun onClick(view: View?) {
        if (view == buttonResetPassword) {
            val code = editTextVerificationCode.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val passwordAgain = editTextPasswordAgain.text.toString().trim()

            if (code.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
                AppPresenter(this.requireContext()).showMessage(message = "Provide required fields and proceed")
                return
            }

            if (password != passwordAgain) {
                AppPresenter(this.requireContext()).showMessage(message = "Passwords do not match")
                return
            }


        }
    }

    // MARK: creating a new instance
    companion object {
        fun newInstance(number: String, action: PresenterCallback) =
            BottomSheetResetPassword().apply {
                this.action = action
                this.number = number
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonResetPassword.setOnClickListener(this)
        initPasswordReset()
    }

    private fun initPasswordReset() {
        progressBar.visibility = View.VISIBLE
        buttonResetPassword.isEnabled = false

        APICall(activity!!).initPasswordReset(number, object : UserCallback {
            override fun onRequestEnded() {
                super.onRequestEnded()

                progressBar.visibility = View.INVISIBLE
                buttonResetPassword.isEnabled = true
            }
        })

    }
}
