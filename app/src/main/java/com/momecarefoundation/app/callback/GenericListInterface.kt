package com.momecarefoundation.app.callback

/**
 * Created by Monarchy on 08/10/2017.
 */
interface GenericListInterface<T> : BaseInterface {
    // MARK: receive an item
    fun onReceivedItem(result: T) {
    }

    // MARK: receive all items
    fun onReceivedItems(results: List<T>) {
    }
}
