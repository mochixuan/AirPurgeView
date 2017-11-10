package com.wx.airpurgeview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.*

/**
 * 颗粒物
 */
class GranuleView: View {

    private val mPaint = Paint()

    private var mGranuleColor: Int = Color.WHITE
    private var mGranuleModels: ArrayList<GranuleModel> ? = null
    private val mCircleRaius = DensityUtil.dpTopx(context!!,6f)

    //圆的直径大小占宽高中最小的那个的比例
    private var mGranuleNum = 1

    private var mRecycleSpeed = 1.2f

    //动画
    private var mAnimator: ValueAnimator? = null

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint.isAntiAlias = true
        mPaint.color = mGranuleColor
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = mCircleRaius*2
        mGranuleModels = ArrayList<GranuleModel>()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onStartAnim()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        onGenerateGranules()

        canvas!!.drawColor(Color.GREEN)

        mGranuleModels!!.forEach {
            val cX = it.radius*Math.cos(it.angle*Math.PI/180f).toFloat()+measuredWidth/2f
            val cY = it.radius*Math.sin(it.angle*Math.PI/180f).toFloat()+measuredHeight/2f
            canvas!!.drawPoint(cX,cY,mPaint)
        }

    }

    fun onGenerateGranules() {
        val random = Random()
        var maxRadius = Math.sqrt((measuredWidth*measuredWidth+measuredHeight*measuredHeight).toDouble()).toFloat()
        if (mGranuleModels!!.size == 0) {
            for (i in 0..mGranuleNum) {
                var radius = random.nextFloat()*maxRadius
                var angle = random.nextFloat()*360
                mGranuleModels!!.add(GranuleModel(radius,angle))
            }
        } else {
            for (i in 0..mGranuleNum) {

                if (mGranuleModels!![i].radius < 20) {
                    mGranuleModels!![i].radius = random.nextFloat()*maxRadius
                } else {
                    mGranuleModels!![i].radius = mGranuleModels!![i].radius - maxRadius/360f
                }

                if (mGranuleModels!![i].angle>360) {
                    mGranuleModels!![i].angle = mGranuleModels!![i].angle + 1 - 360
                } else {
                    mGranuleModels!![i].angle =  mGranuleModels!![i].angle + 1
                }

            }
        }
    }

    fun onStartAnim() {
        if (mAnimator != null && mAnimator!!.isRunning) {
            mAnimator!!.cancel()
        }
        mAnimator = ValueAnimator()
        mAnimator!!.setFloatValues(0f,360f)
        mAnimator!!.duration = 10000
        mAnimator!!.interpolator = LinearInterpolator()
        mAnimator!!.repeatCount = ValueAnimator.INFINITE
        mAnimator!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
                invalidate()
            }
        })
        mAnimator!!.start()
    }

    fun onEndAnim() {
        if (mAnimator != null) {
            if (mAnimator!!.isRunning) {
                mAnimator!!.cancel()
            }
            mAnimator!!.removeAllListeners()
        }
    }

}