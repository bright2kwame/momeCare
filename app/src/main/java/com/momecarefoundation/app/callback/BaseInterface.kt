package com.momecarefoundation.app.callback

/**
 * Created by Monarchy on 08/10/2017.
 */
interface BaseInterface {
    // MARK: default implementation of error
    fun onReceivedError(error: String) {
    }

    // MARK: default implementation of detail
    fun onReceivedDetail(message: String) {
    }

    // MARK: default implementation
    fun onReceivedNextUrl(url: String) {
    }

    fun onRequestEnded() {
    }
}
