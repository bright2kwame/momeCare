package com.momecarefoundation.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.momecarefoundation.app.R
import com.momecarefoundation.app.model.User
import com.momecarefoundation.app.util.Utility
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = User().getUser()
        user?.let {
            textViewName.text = it.fullName
            textViewNumber.text = it.number

            activity?.let { context ->
                val textDrawable = Utility().getLetterView(context, it.fullName, 60, null, true)
                imageViewIcon?.setImageDrawable(textDrawable)
            }
        }
    }



}