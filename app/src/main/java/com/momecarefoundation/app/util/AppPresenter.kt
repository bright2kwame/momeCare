package com.momecarefoundation.app.util

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.momecarefoundation.app.R
import com.momecarefoundation.app.callback.PresenterCallback

class AppPresenter(var context: Context) {

    private var materialAlertDialogBuilder: MaterialAlertDialogBuilder =
        MaterialAlertDialogBuilder(context)

    //MARK: show message
    fun showMessage(
        title: String? = context.getString(R.string.app_name),
        message: String,
        positiveAction: String? = "OK",
        negativeActive: String? = "CANCEL",
        presenterCallback: PresenterCallback? = null
    ) {
        materialAlertDialogBuilder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveAction) { dialog, _ ->
                presenterCallback?.onActionSelected(positiveAction!!)
                dialog.dismiss()
            }
            .setNegativeButton(negativeActive) { dialog, _ ->
                presenterCallback?.onActionSelected(negativeActive!!)
                dialog.dismiss()
            }
            .show()
    }

    //MARK: show list
    fun showList(
        title: String? = context.getString(R.string.app_name),
        items: Array<String>,
        presenterCallback: PresenterCallback? = null
    ) {

        materialAlertDialogBuilder
            .setTitle(title)
            .setItems(items) { dialog, which ->
                presenterCallback?.onActionSelected(items[which])
                dialog.dismiss()
            }
            .show()
    }

    //MARK: show list
    fun showSingleChoiceList(
        title: String? = context.getString(R.string.app_name),
        items: Array<String>,
        positiveAction: String? = "OK",
        negativeActive: String? = "CANCEL",
        presenterCallback: PresenterCallback? = null
    ) {
        val checkedItem = 1

        materialAlertDialogBuilder
            .setTitle(title)
            .setNeutralButton(negativeActive) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(positiveAction) { dialog, which ->
                // Respond to positive button press
                presenterCallback?.onActionSelected(items[checkedItem])
                dialog.dismiss()
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                presenterCallback?.onActionSelected(items[checkedItem])
                dialog.dismiss()
            }
            .show()
    }

}