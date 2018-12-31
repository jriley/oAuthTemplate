package dev.jriley

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.jriley.login.R

fun Activity.enterLeftExitRight() = this.overridePendingTransition(R.anim.enter_left, R.anim.exit_right)

fun Activity.enterRightExitLeft() = this.overridePendingTransition(R.anim.enter_right, R.anim.exit_left)

fun Activity.finishAndExitWithAnimation() {
    this.finish()
    enterRightExitLeft()
}

fun ViewGroup.inflate(layoutId: Int) : View = LayoutInflater.from(this.context).inflate(layoutId, this, false)

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible(isVisible: Boolean) = if (isVisible) this.visible() else this.gone()

