package com.wx.airpurgeview

import android.content.Context
import android.view.WindowManager

class DensityUtil {

    companion object {

        fun dpTopx(context: Context, dpValue: Float): Float {
            return context.resources.displayMetrics.density*dpValue+0.5f;
        }

        fun pxTodp(context: Context, pxValue: Float): Float {
            return pxValue/context.resources.displayMetrics.density+0.5f;
        }

        fun spTopx(context: Context, spValue: Float): Float {
            return context.resources.displayMetrics.scaledDensity*spValue+0.5f
        }

        fun pxTosp(context: Context, pxValue: Float): Float {
            return pxValue/context.resources.displayMetrics.scaledDensity+0.5f
        }

        fun getWidth(context: Context): Int {
            return (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.width
        }

        fun getHeight(context: Context): Int {
            return (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.height
        }

    }

}