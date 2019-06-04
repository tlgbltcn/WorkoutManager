package com.tlgbltcn.app.workhard.utils.extensions

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tlgbltcn.app.workhard.R

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, duration).show()

inline fun Activity.alertDialog(body: AlertDialog.Builder.() -> AlertDialog.Builder): AlertDialog {
    return AlertDialog.Builder(this)
            .body()
            .show()
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    with(supportFragmentManager) {
        transact {
            add(fragment, tag)
        }
    }
}

fun AppCompatActivity.replaceFragmentInActivity(fragment : Fragment, frameId : Int){
    with(supportFragmentManager){
        transact {
            replace(frameId, fragment)
        }
    }
}


fun AppCompatActivity.simpleToolbarWithHome(toolbar: Toolbar, title_: String = "") {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        setDisplayHomeAsUpEnabled(true)
        title = title_
    }
}

fun AppCompatActivity.loadFragment(fragment: Fragment?){
    if(fragment != null){
        supportFragmentManager.beginTransaction()
                .replace(R.id.container,fragment)
                .commit()
    }
}
