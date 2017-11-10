package com.wx.airpurgeview

import android.animation.TypeEvaluator
import android.graphics.Color

/*
* 公式来自网络，有问题自己改正，应该没问题
* */

class HsvEvaluator : TypeEvaluator<Int> {
    override fun evaluate(fraction: Float, startValue: Int?, endValue: Int?): Int {

        val startHsv = FloatArray(3)
        val endHsv = FloatArray(3)
        val outHsv = FloatArray(3)

        Color.colorToHSV(startValue!!,startHsv)
        Color.colorToHSV(endValue!!,endHsv)

        if (endHsv[0] - startHsv[0] > 180) {
            endHsv[0] -= 360f
        } else if (endHsv[0] - startHsv[0] < -180) {
            endHsv[0] += 360f
        }
        outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]) * fraction
        if (outHsv[0] > 360) {
            outHsv[0] -= 360f
        } else if (outHsv[0] < 0) {
            outHsv[0] += 360f
        }

        outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]) * fraction
        outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]) * fraction

        val alpha = startValue shr 24 + ((endValue shr 24 - startValue shr 24) * fraction).toInt()

        return Color.HSVToColor(alpha, outHsv)
    }
}
