package com.kote.bookshelf

import android.app.Application
import com.kote.bookshelf.data.AppContainer
import com.kote.bookshelf.data.DefaultAppContainer

class BookshelfApplication: Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}