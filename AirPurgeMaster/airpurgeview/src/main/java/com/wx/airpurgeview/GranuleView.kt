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
    private var mCircleRadius:Float = 0f

    //圆的直径大小占宽高中最小的那个的比例
    private var mCircleProportion = 0f
    private var mDashedRingRadius = 0f

    //圆的直径大小占宽高中最小的那个的比例
    private var mGranuleNum = 0

    private var mRecycleSpeed = 1.2f

    private var mMaxRadius = 0f

    private var isOpenClear: Boolean = false

    //动画
    private var mAnimator: ValueAnimator? = null

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun initGraule(granuleNum: Int,circleProportion: Float,circleRadius: Float,granuleColor: Int) {
        this.mGranuleNum = granuleNum
        this.mCircleProportion = circleProportion
        this.mCircleRadius = circleRadius
        this.mGranuleColor = granuleColor

        mPaint.isAntiAlias = true
        mPaint.color = mGranuleColor
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = mCircleRadius*2
        mGranuleModels = ArrayList<GranuleModel>()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (mAnimator != null && mAnimator!!.isRunning()) {
            onGenerateGranules()
            mGranuleModels!!.forEach {
                mPaint.alpha = Math.abs((it.radius-mDashedRingRadius)/(mMaxRadius-mDashedRingRadius)*255).toInt()
                val cX = it.radius*Math.cos(it.angle*Math.PI/180f).toFloat()+measuredWidth/2f
                val cY = it.radius*Math.sin(it.angle*Math.PI/180f).toFloat()+measuredHeight/2f
                canvas!!.drawPoint(cX,cY,mPaint)
            }
        }
    }

    fun onGenerateGranules() {
        val random = Random()
        if (mGranuleModels!!.size == 0) {
            for (i in 0..mGranuleNum-1) {
                var radius = random.nextFloat()*(mMaxRadius-mDashedRingRadius)+mDashedRingRadius
                var angle = random.nextFloat()*360
                mGranuleModels!!.add(GranuleModel(radius,angle))
            }
        } else {
            if (isOpenClear) {
                for (i in 0..mGranuleModels!!.size-1) {
                    if (mGranuleModels!![i].radius < mDashedRingRadius) {
                        mGranuleModels!![i].radius = random.nextFloat()*(mMaxRadius-mDashedRingRadius)+mDashedRingRadius
                    } else {
                        mGranuleModels!![i].radius = mGranuleModels!![i].radius - (mMaxRadius-mDashedRingRadius)*mRecycleSpeed/320f
                    }

                    if (mGranuleModels!![i].angle>360) {
                        mGranuleModels!![i].angle = mGranuleModels!![i].angle + mRecycleSpeed - 360
                    } else {
                        mGranuleModels!![i].angle =  mGranuleModels!![i].angle + mRecycleSpeed
                    }
                }
            } else {
                for (i in 0..mGranuleModels!!.size-1) {
                    val speed = (random.nextFloat()-0.5f)*mRecycleSpeed/10f
                    if (mGranuleModels!![i].radius < mDashedRingRadius) {
                        mGranuleModels!![i].radius = random.nextFloat()*(mMaxRadius-mDashedRingRadius)+mDashedRingRadius
                    } else {
                        mGranuleModels!![i].radius = mGranuleModels!![i].radius - (mMaxRadius-mDashedRingRadius)*(speed)/320f
                    }

                    if (mGranuleModels!![i].angle>360) {
                        mGranuleModels!![i].angle = mGranuleModels!![i].angle - 360 + speed
                    } else {
                        mGranuleModels!![i].angle =  mGranuleModels!![i].angle + speed
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //减少计算次数，节约消耗
        mMaxRadius = Math.sqrt((measuredWidth*measuredWidth+measuredHeight*measuredHeight).toDouble()).toFloat()/2f
        mDashedRingRadius = mCircleProportion/4f*Math.min(measuredWidth,measuredHeight)
    }

    fun onStartAnim(isOpenClear: Boolean) {
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
        this.isOpenClear = isOpenClear
        mAnimator!!.start()
    }

    fun onChangeClearMode(isOpenClear: Boolean) {
        this.isOpenClear = isOpenClear
    }

    fun getClearMode(): Boolean {
        return isOpenClear
    }

    fun onClearAllGraule() {
        if (mAnimator != null) {
            if (mAnimator!!.isRunning) {
                mAnimator!!.cancel()
            }
            mAnimator!!.removeAllListeners()
            isOpenClear = false
            invalidate()
        }
    }

    //建议1-5
    fun setSpeedLevel(speedLevel:Long) {
        this.mRecycleSpeed = 3000f/speedLevel
    }

}