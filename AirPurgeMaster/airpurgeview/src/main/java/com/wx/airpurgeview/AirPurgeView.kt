package com.wx.airpurgeview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator

class AirPurgeView: View {

    //圆的直径大小占宽高中最小的那个的比例
    private var mCircleProportion = 0f

    //圆的颜色
    private var mColor = Color.WHITE

    //实心圆环的宽度
    private var mSolidRingWidth = 0f
    private var mDashedRingWidth = 0f

    //显示的字体上
    private var mTopTitle = ""
    private var mCenterTitle = ""
    private var mBottomTitle = ""


    private var mTopTitleTextSize = 0f
    private var mCenterTitleTextSize = 0f
    private var mBottomTitleTextSize = 0f

    private val mPaint = Paint()

    //虚线圆的半径差
    private var dashedRadiusDiff = 0f
    private var rotateProgress = 0f

    //背景色
    private var bgColor:Int = 0xff9400D3.toInt()

    //动画
    private var mRotateAnimator:ObjectAnimator? = null
    private var mJumpAnimator: ObjectAnimator? = null
    private var mBgColorAnim:ObjectAnimator? = null

    private var isCancelJumpAnim = false

    //风速级别
    private var mSpeedLevel:Long = 3000

    //每片扇叶的角度
    private var mEachPanAngle = 0f
    //相邻扇叶的间隙
    private var mEachPanAngleGap = 0f

    //扇叶监听
    private var mPanListener: onPanListener? = null

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun initAirPurge(circleProportion: Float,mainColor: Int,solidringWidth: Float,dashedringidth: Float,
                     topTitle: String,centerTitle: String,bottomTitle: String,
                     topTitleTextSize: Float,centerTitleTextSize: Float,bottomTitleTextSize: Float,
                     eachPanAngle: Float,eachPanAngleGap:Float) {

        this.mCircleProportion = circleProportion
        this.mColor = mainColor
        this.mSolidRingWidth = solidringWidth
        this.mDashedRingWidth = dashedringidth

        this.mTopTitle = topTitle
        this.mCenterTitle = centerTitle
        this.mBottomTitle = bottomTitle

        this.mTopTitleTextSize = topTitleTextSize
        this.mCenterTitleTextSize = centerTitleTextSize
        this.mBottomTitleTextSize = bottomTitleTextSize

        this.mEachPanAngle = eachPanAngle
        this.mEachPanAngleGap = eachPanAngleGap

        mPaint.isAntiAlias = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.drawColor(bgColor)

        drawAllText(canvas)
        drawSolidCircle(canvas)

        canvas.save()
        canvas.rotate(rotateProgress,measuredWidth/2f,measuredHeight/2f)
        drawDashedCircle(canvas)
        canvas.restore()

    }

    private fun drawSolidCircle(canvas: Canvas) {
        //实心圆环
        mPaint.strokeWidth = mSolidRingWidth
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
        val solidRingCx = measuredWidth/2f
        val solidRingCy = measuredHeight/2f
        val solidRingRadius = mCircleProportion/2f*Math.min(measuredWidth,measuredHeight)
        canvas.drawCircle(solidRingCx,solidRingCy,solidRingRadius,mPaint)
    }

    private fun drawDashedCircle(canvas: Canvas) {
        //非渐变扇叶
        /*mPaint.strokeWidth = mSolidRingWidth*4
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
        val pathEffect = DashPathEffect(floatArrayOf(mPaint.strokeWidth*0.4f,mPaint.strokeWidth),0f)
        mPaint.pathEffect = pathEffect
        val dashedRingCx = measuredWidth/2f
        val dashedRingCy = measuredHeight/2f
        val dashedRingRadius = mCircleProportion/2f*Math.min(measuredWidth,measuredHeight) - mPaint.strokeWidth*0.5f
        canvas.drawCircle(dashedRingCx,dashedRingCy,dashedRingRadius,mPaint)
        mPaint.pathEffect = null*/

        //每个角度
        mPaint.strokeWidth = dashedRadiusDiff
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
        val solidInnerRadius = mCircleProportion/2f*Math.min(measuredWidth,measuredHeight) - mSolidRingWidth*0.5f
        val dashedRingRadius = solidInnerRadius - dashedRadiusDiff*0.5f  //完美连接
        val rectF = RectF()
        rectF.left = measuredWidth/2f - dashedRingRadius
        rectF.top = measuredHeight/2f - dashedRingRadius
        rectF.right = measuredWidth/2f + dashedRingRadius
        rectF.bottom = measuredHeight/2f + dashedRingRadius
        var curAngle = -mEachPanAngle/2f
        //实现渐变扇叶
        while (curAngle < 360 - mEachPanAngle) {

            val x0 = measuredWidth/2f + (Math.cos((curAngle)*Math.PI/180)*(dashedRingRadius-dashedRadiusDiff*0.5)).toFloat()
            val y0 = measuredHeight/2f + (Math.sin((curAngle)*Math.PI/180)*(dashedRingRadius-dashedRadiusDiff*0.5)).toFloat()

            val x1 = measuredWidth/2f + (Math.cos((curAngle+mEachPanAngle)*Math.PI/180)*(dashedRingRadius+dashedRadiusDiff*0.5)).toFloat()
            val y1 = measuredHeight/2f + (Math.sin((curAngle+mEachPanAngle)*Math.PI/180)*(dashedRingRadius+dashedRadiusDiff*0.5)).toFloat()

            val shader = LinearGradient(x0, y0, x1, y1, Color.parseColor("#22ffffff"), Color.parseColor("#ffffffff"), Shader.TileMode.CLAMP)
            mPaint.shader = shader
            canvas?.drawArc(rectF, curAngle, mEachPanAngle, false, mPaint)
            curAngle = curAngle + mEachPanAngle + mEachPanAngleGap

        }
        mPaint.shader = null //记得清除

    }

    private fun drawAllText(canvas: Canvas) {
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
    private fun getMeasureSpecSize(measureSpec: Int,maxSize: Int): Int {
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

    private fun setDashedRadiusDiff(dashedRadiusDiff:Float) {
        this.dashedRadiusDiff = dashedRadiusDiff
        invalidate()
    }

    private fun setRotateProgress(rotateProgress: Float) {
        this.rotateProgress = rotateProgress
        invalidate()
    }

    //动画
    @SuppressLint("WrongConstant")
    fun onStartAnim() {
        onFanAnim(true)
    }

    fun onEndAnim(isQuicklyClose:Boolean) {
        if (mJumpAnimator != null && mJumpAnimator!!.isRunning) {
            mJumpAnimator!!.cancel()
            mJumpAnimator!!.removeAllListeners()
        }
        if (mRotateAnimator != null && mRotateAnimator!!.isRunning) {
            mRotateAnimator!!.cancel()
        }
        if (isQuicklyClose) {
            dashedRadiusDiff = 0f
            if (mBgColorAnim != null && mBgColorAnim!!.isRunning) {
                mBgColorAnim!!.cancel()
            }
        } else {
            onFanAnim(false)
        }
    }

    //扇叶
    private fun onFanAnim(isOpenFan: Boolean) {
        if (mJumpAnimator != null && mJumpAnimator!!.isRunning) {
            mJumpAnimator!!.cancel()
        }
        if (isOpenFan) {
            mJumpAnimator = ObjectAnimator.ofFloat(this,"dashedRadiusDiff",dashedRadiusDiff,mDashedRingWidth)
            mJumpAnimator!!.interpolator = DecelerateInterpolator()
            mJumpAnimator!!.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {
                }
                override fun onAnimationEnd(p0: Animator?) {
                    if (!isCancelJumpAnim) {
                        mPanListener?.onHasOpen()
                        onRotateAnim()
                    }
                    isCancelJumpAnim = false
                }
                override fun onAnimationCancel(p0: Animator?) {
                    isCancelJumpAnim = true
                }
                override fun onAnimationStart(p0: Animator?) {

                }
            })
            mJumpAnimator!!.duration = 2000
        } else {
            mJumpAnimator = ObjectAnimator.ofFloat(this,"dashedRadiusDiff",dashedRadiusDiff,0f)
            mJumpAnimator!!.interpolator = AccelerateInterpolator()
            mJumpAnimator!!.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {
                }
                override fun onAnimationEnd(p0: Animator?) {
                    if (dashedRadiusDiff == 0f) {
                        mPanListener?.onHasClose()
                    }
                }
                override fun onAnimationCancel(p0: Animator?) {

                }
                override fun onAnimationStart(p0: Animator?) {

                }
            })
            mJumpAnimator!!.duration = 1200
        }
        mJumpAnimator!!.start()
    }

    private fun onRotateAnim() {
        if (mRotateAnimator != null && mRotateAnimator!!.isRunning) {
            mRotateAnimator!!.cancel()
        }
        mRotateAnimator = ObjectAnimator.ofFloat(this,"rotateProgress",rotateProgress.toInt().toFloat(),rotateProgress.toInt()+360f) //实现无缝变速
        mRotateAnimator!!.duration = mSpeedLevel
        mRotateAnimator!!.interpolator = LinearInterpolator()
        mRotateAnimator!!.repeatCount = ObjectAnimator.INFINITE
        mRotateAnimator!!.start()
    }

    private fun setBgColor(color: Int) {
        this.bgColor = color
        invalidate()
    }

    fun setSpeedLevel(speedLevel:Long) {
        this.mSpeedLevel = speedLevel
        if (mRotateAnimator != null && mRotateAnimator!!.isRunning) {   //没运行就先别转动,节余点消耗
            onRotateAnim()
        }
    }

    //时间尽量写短点
    fun setBackgroundColor(color: Int,durtion: Long) {
        if (mBgColorAnim != null && mBgColorAnim!!.isRunning) {
            mBgColorAnim!!.cancel()
        }
        if (bgColor == color) return
        mBgColorAnim = ObjectAnimator.ofInt(this,"bgColor",bgColor,color)
        mBgColorAnim!!.setEvaluator(HsvEvaluator())
        mBgColorAnim!!.duration = durtion
        mBgColorAnim!!.interpolator = LinearInterpolator()
        mBgColorAnim!!.start()
    }

    override fun setBackgroundColor(color: Int) {
        if (mBgColorAnim != null && mBgColorAnim!!.isRunning) {
            mBgColorAnim!!.cancel()
        }
        bgColor = color
        invalidate()
    }

    fun setTopTitle(title:String) {
        if (this.mTopTitle.equals(title)) return
        this.mTopTitle = title
        invalidate()
    }

    fun setCenterTitle(title:String) {
        if (this.mCenterTitle.equals(title)) return
        this.mCenterTitle = title
        invalidate()
    }

    fun setBottomTitle(title:String) {
        if (this.mBottomTitle.equals(title)) return
        this.mBottomTitle = title
        invalidate()
    }

    fun setPanListener(panListener: onPanListener) {
        this.mPanListener = panListener
    }

}