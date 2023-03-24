package com.smart.liv.utils

import android.view.View

/**
 * @author JoeYe
 * @date 2023/3/23 09:41
 */
abstract class LimitFastClickListener : View.OnClickListener{

    companion object{
        const val CLICK_TIME_INTERVAL = 500
    }

    private var lastClickTime = 0L

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if(currentTime - lastClickTime > CLICK_TIME_INTERVAL){
            lastClickTime = currentTime
            onSafeLimitClick(v)
        }
    }

    abstract fun onSafeLimitClick(v: View?)
}