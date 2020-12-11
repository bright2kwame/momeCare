package com.momecarefoundation.app.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.momecarefoundation.app.R
import com.momecarefoundation.app.api.APICall
import com.momecarefoundation.app.callback.PresenterCallback
import com.momecarefoundation.app.callback.UserCallback
import com.momecarefoundation.app.util.AppPresenter
import kotlinx.android.synthetic.main.bottom_sheet_update_profile.*

/**
 * Created by Monarchy on 11/10/2017.
 */
class BottomSheetUpdateProfile : RoundedBottomSheetDialogFragment(), View.OnClickListener {

    private var action: PresenterCallback? = null

    override fun onClick(view: View?) {
        if (view == buttonSubmit) {
            val firstName = editTextFirstName.text.toString().trim()
            val lastName = editTextLastName.text.toString().trim()

            if (lastName.isEmpty() || firstName.isEmpty()) {
                AppPresenter(this.requireContext()).showMessage(message = "Provide required fields and proceed")
                return
            }


            progressBar.visibility = View.VISIBLE
            buttonSubmit.isEnabled = false

            APICall(requireContext()).updateUser(
                firstName = firstName,
                lastName = lastName,
                callback = object : UserCallback {
                    override fun onReceivedDetail(message: String) {
                        super.onReceivedDetail(message)
                        AppPresenter(requireContext()).showMessage(message = message)
                        dismiss()
                    }

                    override fun onRequestEnded() {
                        super.onRequestEnded()

                        progressBar.visibility = View.VISIBLE
                        buttonSubmit.isEnabled = true
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
            BottomSheetUpdateProfile().apply {
                this.action = action
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSubmit.setOnClickListener(this)
    }

}
