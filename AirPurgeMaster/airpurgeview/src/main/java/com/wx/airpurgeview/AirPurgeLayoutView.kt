package com.wx.airpurgeview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout

class AirPurgeLayoutView: RelativeLayout {

    //颗粒物
    private var mGranuleView: GranuleView? = null
    private var mAirPurgeView: AirPurgeView? = null

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val typeArray = context!!.obtainStyledAttributes(attrs,R.styleable.AirPurgeLayoutView)
        val granuleNum = typeArray.getInteger(R.styleable.AirPurgeLayoutView_granule_num,60)
        val granuleRadius = typeArray.getFloat(R.styleable.AirPurgeLayoutView_granule_radius,DensityUtil.dpTopx(context,6f))
        val granuleColor = typeArray.getColor(R.styleable.AirPurgeLayoutView_granule_color,Color.WHITE)

        val circleProportion = typeArray.getFloat(R.styleable.AirPurgeLayoutView_circle_proportion,0.6f)
        val mainColor = typeArray.getColor(R.styleable.AirPurgeLayoutView_main_color,Color.WHITE)
        val solidringWidth = typeArray.getFloat(R.styleable.AirPurgeLayoutView_solidring_width,DensityUtil.dpTopx(context,6f))
        val dashedringidth = typeArray.getFloat(R.styleable.AirPurgeLayoutView_solidring_width,DensityUtil.dpTopx(context,24f))
        var topTitle = ""
        var centerTitle = ""
        var bottomTitle = ""
        try { topTitle = typeArray.getString(R.styleable.AirPurgeLayoutView_toptitle) }catch (e: Exception) { e.printStackTrace() }
        try { centerTitle = typeArray.getString(R.styleable.AirPurgeLayoutView_centertitle) } catch (e:Exception) { e.printStackTrace() }
        try { bottomTitle = typeArray.getString(R.styleable.AirPurgeLayoutView_bottomtitle) } catch (e:Exception) { e.printStackTrace() }
        val topTitleTextSize = typeArray.getDimension(R.styleable.AirPurgeLayoutView_toptitle_textsize,DensityUtil.spTopx(context,12f))
        val centerTitleTextSize = typeArray.getDimension(R.styleable.AirPurgeLayoutView_centertitle_textsize,DensityUtil.spTopx(context,56f))
        val bottomTitleTextSize = typeArray.getDimension(R.styleable.AirPurgeLayoutView_bottomtitle_textsize, DensityUtil.spTopx(context,16f))
        val speedLevel = typeArray.getInteger(R.styleable.AirPurgeLayoutView_speed_level, 3000)
        val eachPanAngle = typeArray.getFloat(R.styleable.AirPurgeLayoutView_eachpan_angle,15f)
        val eachPanAngleGap = typeArray.getFloat(R.styleable.AirPurgeLayoutView_eachpan_anglegap,5f)

        typeArray.recycle()

        val param = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        mAirPurgeView = AirPurgeView(context)
        mAirPurgeView!!.layoutParams = param
        mAirPurgeView!!.initAirPurge(circleProportion,mainColor,solidringWidth,dashedringidth,
                topTitle,centerTitle,bottomTitle,topTitleTextSize,centerTitleTextSize,bottomTitleTextSize,
                eachPanAngle,eachPanAngleGap)
        addView(mAirPurgeView)

        mGranuleView = GranuleView(context)
        mGranuleView!!.layoutParams = param
        mGranuleView!!.initGraule(granuleNum,circleProportion,granuleRadius,granuleColor)
        addView(mGranuleView)

        setSpeedLevel(speedLevel.toLong())
    }

    fun onOpenGranule(isOpenClear: Boolean) {
        mGranuleView!!.onStartAnim(isOpenClear)
    }

    fun onChangeGranuleMode(isOpenClear: Boolean) {
        mGranuleView!!.onChangeClearMode(isOpenClear)
    }

    fun getGranuleMode(): Boolean {
        return mGranuleView!!.getClearMode()
    }

    fun onClearAllGranule() {
        mGranuleView!!.onClearAllGraule()
    }

    fun onOpenAirPurge() {
        mAirPurgeView!!.onStartAnim()
    }

    fun onCloseAirPurge() {
        mAirPurgeView!!.onEndAnim(false)
    }

    fun setSpeedLevel(speedLevel:Long) {
        mAirPurgeView!!.setSpeedLevel(speedLevel)
        mGranuleView!!.setSpeedLevel(speedLevel)
    }

    fun setBackgroundColor(color: Int,durtion: Long) {
        mAirPurgeView!!.setBackgroundColor(color,durtion)
    }

    override fun setBackgroundColor(color: Int) {
        mAirPurgeView!!.setBackgroundColor(color)
    }

    fun setCenterTitle(title:String) {
        mAirPurgeView!!.setCenterTitle(title)
    }

    fun setTopTitle(title:String) {
        mAirPurgeView!!.setTopTitle(title)
    }

    fun setBottomTitle(title:String) {
        mAirPurgeView!!.setBottomTitle(title)
    }

    //防止内存泄漏
    fun clearAllAnimator() {
        if (mGranuleView != null) {
            mGranuleView!!.onClearAllGraule()
        }
        if (mAirPurgeView != null) {
            mAirPurgeView!!.onEndAnim(true)
        }
    }

    fun setPanListener(panListener: onPanListener) {
        mAirPurgeView!!.setPanListener(panListener)
    }

}