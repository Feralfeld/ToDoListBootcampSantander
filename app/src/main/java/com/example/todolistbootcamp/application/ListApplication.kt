package com.example.todolistbootcamp.application

import android.app.Application
import com.example.todolistbootcamp.helpers.HelperDB
import com.example.todolistbootcamp.helpers.HelperItensDB

class ListApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    var helperItemDB: HelperItensDB? = null
        private set

    companion object {
        lateinit var instance: ListApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
        helperItemDB = HelperItensDB(this)
    }
}