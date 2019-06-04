package com.tlgbltcn.app.workhard.utils.extensions

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tlgbltcn.app.workhard.R

fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = activity?.toast(message, duration)

inline fun Fragment.alertDialog(body: AlertDialog.Builder.() -> AlertDialog.Builder) = activity?.alertDialog(body)

fun Fragment.loadFragment(fragment: Fragment?) {
    fragment?.let {
        fragmentManager?.beginTransaction()
                ?.replace(R.id.container, fragment)
                ?.commit()
    }

}
