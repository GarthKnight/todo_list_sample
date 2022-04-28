package com.example.sltodolist.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val commonErrorEvent = MutableLiveData<Event<Unit>>()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    fun Disposable.disposeOnCleared(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}

