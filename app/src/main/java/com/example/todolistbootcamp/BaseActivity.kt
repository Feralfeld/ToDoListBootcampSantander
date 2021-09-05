package com.example.todolistbootcamp

import android.R
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.toolbar.*


open class BaseActivity : AppCompatActivity(){

    protected fun setupToolBar(toolBar: Toolbar, title: String, navgationBack: Boolean, nodedsExibition: Boolean) {

        toolBar.title = title

        if(!nodedsExibition) {
            iconDoneds.visibility = View.GONE
        }

        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(navgationBack)
    }

}