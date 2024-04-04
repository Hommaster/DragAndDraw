package com.example.draganddraw

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.example.draganddraw.database.Color

class MainActivity : AppCompatActivity(), MenuProvider {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menuHost: MenuHost = this
        menuHost.addMenuProvider(this)

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        var color: Color = Color()
        return when(menuItem.itemId) {
            R.id.menu_item_black -> {
                color.setColor(1)
                true
            }
            R.id.menu_item_red -> {
                color.setColor(2)
                true
            }
            R.id.menu_item_green -> {
                color.setColor(3)
                true
            }
            R.id.menu_item_yellow -> {
                color.setColor(4)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.activity_main, menu)
    }
}