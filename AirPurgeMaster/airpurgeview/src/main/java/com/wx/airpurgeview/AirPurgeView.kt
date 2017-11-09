package com.wx.airpurgeview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator

class AirPurgeView: View {

    //圆的直径大小占宽高中最小的那个的比例
    private var mCircleProportion = 0.6f

    //圆的颜色
    private var mColor = Color.parseColor("#ffffffff")

    //实心圆环的宽度
    private val mSolidRingWidth = DensityUtil.dpTopx(context,6f)

    //背景颜色数组
    private var mBgColors = ArrayList<Int>()
    //背景颜色数组对于的数字
    private var mAirSections = ArrayList<Int>()

    //显示的字体上
    private var mTopTitle = "PM2.5"
    private var mCenterTitle = "521"
    private var mBottomTitle = "空气优"

    private var mTopTitleTextSize = DensityUtil.spTopx(context,12f)
    private var mCenterTitleTextSize = DensityUtil.spTopx(context,56f)
    private var mBottomTitleTextSize = DensityUtil.spTopx(context,16f)

    private val mPaint = Paint()

    private var mAnimator: AnimatorSet = AnimatorSet()

    //虚线圆的半径差
    private var dashedRadiusDiff = 0f
    private var rotateProgress = 0f

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        mPaint.isAntiAlias = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onStartAnim()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.drawColor(Color.GREEN)

        drawAllText(canvas)
        drawSolidCircle(canvas)

        canvas.save()
        canvas.rotate(rotateProgress,measuredWidth/2f,measuredHeight/2f)
        drawDashedCircle(canvas)
        canvas.restore()

    }

    fun drawSolidCircle(canvas: Canvas) {
        //实心圆环
        mPaint.strokeWidth = mSolidRingWidth
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
        val solidRingCx = measuredWidth/2f
        val solidRingCy = measuredHeight/2f
        val solidRingRadius = mCircleProportion/2f*Math.min(measuredWidth,measuredHeight)
        canvas.drawCircle(solidRingCx,solidRingCy,solidRingRadius,mPaint)
    }

    fun drawDashedCircle(canvas: Canvas) {
        //虚线内圆环
        mPaint.strokeWidth = mSolidRingWidth
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
        val pathEffect = DashPathEffect(floatArrayOf(mSolidRingWidth/2f,mSolidRingWidth),0f)
        mPaint.pathEffect = pathEffect
        val dashedRingCx = measuredWidth/2f
        val dashedRingCy = measuredHeight/2f
        val dashedRingRadius = mCircleProportion/2f*Math.min(measuredWidth,measuredHeight) - dashedRadiusDiff
        canvas.drawCircle(dashedRingCx,dashedRingCy,dashedRingRadius,mPaint)
        mPaint.pathEffect = null
    }

    fun drawAllText(canvas: Canvas) {
        //中间字体绘制
        mPaint.strokeWidth = 0f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPaint.letterSpacing = 0.1f
        }
        mPaint.color = mColor
        mPaint.textSize = mCenterTitleTextSize
        val centerSpacing = mPaint.fontSpacing
        mPaint.textAlign = Paint.Align.CENTER
        val centerTextHeight = -mPaint.descent() - mPaint.ascent()
        val centerTextX = measuredWidth/2f
        val centerTextY = measuredHeight/2f+centerTextHeight/2f
        canvas.drawText(mCenterTitle,centerTextX,centerTextY,mPaint)
        //上字体绘制
        mPaint.textSize = mTopTitleTextSize
        val mtopTextX = centerTextX
        val mtopTextY = centerTextY - centerSpacing
        canvas.drawText(mTopTitle,mtopTextX,mtopTextY,mPaint)
        //下字体绘制
        mPaint.textSize = mBottomTitleTextSize
        val mBottomTextX = centerTextX
        val mBottomTextY = centerTextY + centerSpacing - centerTextHeight + (-mPaint.descent()-mPaint.ascent())
        canvas.drawText(mBottomTitle,mBottomTextX,mBottomTextY,mPaint)
    }

    //测量宽高
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(
                getMeasureSpecSize(widthMeasureSpec,DensityUtil.getWidth(context)),
                getMeasureSpecSize(heightMeasureSpec,DensityUtil.getHeight(context))
        )

    }

    //简单方便
    fun getMeasureSpecSize(measureSpec: Int,maxSize: Int): Int {
        var size = maxSize
        val specSize = MeasureSpec.getSize(measureSpec)
        val specMode = MeasureSpec.getMode(measureSpec)
        when (specMode) {
            MeasureSpec.AT_MOST -> size = specSize
            MeasureSpec.EXACTLY -> size = specSize
            MeasureSpec.UNSPECIFIED -> {}
        }
        return size
    }

    //动画
    @SuppressLint("WrongConstant")
    fun onStartAnim() {
        if (mAnimator.isRunning) {
            mAnimator.cancel()
        }
        //出风扇
        val anim1 = ObjectAnimator.ofFloat(this,"dashedRadiusDiff",0f,mSolidRingWidth)
        anim1.interpolator = DecelerateInterpolator()
        anim1.duration = 1000
        //循环进度
        val anim2 = ObjectAnimator.ofFloat(this,"rotateProgress",0f,360f)
        anim2.duration = 6000
        anim2.interpolator = LinearInterpolator()
        anim2.repeatMode = ObjectAnimator.INFINITE


        mAnimator.play(anim1).before(anim2)
        mAnimator.start()
    }

    fun setDashedRadiusDiff(dashedRadiusDiff:Float) {
        this.dashedRadiusDiff = dashedRadiusDiff
        invalidate()
    }

    fun setRotateProgress(rotateProgress: Float) {
        this.rotateProgress = rotateProgress
        invalidate()
    }

}