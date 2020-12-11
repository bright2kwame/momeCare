package com.momecarefoundation.app.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.momecarefoundation.app.R
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.BaseInterface
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.util.AppPresenter
import kotlinx.android.synthetic.main.bottom_sheet_change_password.*

/**
 * Created by Monarchy on 11/10/2017.
 */
class BottomSheetChangePassword : RoundedBottomSheetDialogFragment(), View.OnClickListener {

    private var action: PresenterCallback? = null


    override fun onClick(view: View?) {
        if (view == buttonResetPassword) {
            val currentPassword = editTextCurrentPassword.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val passwordAgain = editTextPasswordAgain.text.toString().trim()

            if (currentPassword.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
                AppPresenter(this.requireContext()).showMessage(message = "Provide required fields and proceed")
                return
            }

            if (password != passwordAgain) {
                AppPresenter(this.requireContext()).showMessage(message = "Passwords do not match")
                return
            }


            progressBar.visibility = View.VISIBLE
            buttonResetPassword.isEnabled = false

            APICall(requireContext()).passwordChange(
                currentPassword,
                password,
                object : BaseInterface {
                    override fun onReceivedDetail(message: String) {
                        super.onReceivedDetail(message)
                        AppPresenter(requireContext()).showMessage(message = message)
                        dismiss()
                    }

                    override fun onRequestEnded() {
                        super.onRequestEnded()

                        progressBar.visibility = View.VISIBLE
                        buttonResetPassword.isEnabled = true
                    }

                    override fun onReceivedError(error: String) {
                        super.onReceivedError(error)
                        AppPresenter(requireContext()).showMessage(message = error)
                    }
                })


        }
    }

    // MARK: creating a new instance
    companion object {
        fun newInstance(action: PresenterCallback) =
            BottomSheetChangePassword().apply {
                this.action = action
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonResetPassword.setOnClickListener(this)
    }

}
