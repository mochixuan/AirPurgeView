package com.wx.airpurgemaster

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.LinearInterpolator
import android.widget.Button
import com.wx.airpurgeview.AirPurgeLayoutView
import com.wx.airpurgeview.onPanListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAirPurgeView: AirPurgeLayoutView
    private val mAnimtor1 = ValueAnimator()
    private var mCurIndex1 = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAirPurgeView = findViewById<AirPurgeLayoutView>(R.id.airpurge_view)
        val btnChangeNum = findViewById<Button>(R.id.btn1)
        val btnChangeBg = findViewById<Button>(R.id.btn2)
        val btnChangeStart = findViewById<Button>(R.id.btn3)
        val btnChangeEnd = findViewById<Button>(R.id.btn4)
        val btnChangeSpeed = findViewById<Button>(R.id.btn5)
        val btnOpenGranlue= findViewById<Button>(R.id.btn6)
        val btnCloseGranlue = findViewById<Button>(R.id.btn7)
        val btnChangeGranuleMode = findViewById<Button>(R.id.btn8)
        val btnMode1 = findViewById<Button>(R.id.btn9)
        val btnMode2 = findViewById<Button>(R.id.btn10)

        val random = Random()
        val bgColors = intArrayOf(
                0xFFDC143C.toInt(),
                0xFFFF00FF.toInt(),
                0xFF9400D3.toInt(),
                0xFF7B68EE.toInt(),
                0xFF0000FF.toInt(),
                0xFF00BFFF.toInt()
        )
        var colorIndex = 0

        btnChangeNum.setOnClickListener {
            clearMode1Anim()
            mAirPurgeView.setCenterTitle(random.nextInt(600).toString())
        }
        btnChangeBg.setOnClickListener {
            clearMode1Anim()
            if (colorIndex >= bgColors.size) colorIndex = 0
            mAirPurgeView.setBackgroundColor(bgColors[colorIndex],1000)
            colorIndex += 1
        }
        btnChangeStart.setOnClickListener {
            clearMode1Anim()
            mAirPurgeView.onOpenAirPurge()
        }
        btnChangeEnd.setOnClickListener {
            clearMode1Anim()
            mAirPurgeView.onCloseAirPurge()
        }
        btnChangeSpeed.setOnClickListener {
            clearMode1Anim()
            if (colorIndex >= bgColors.size || colorIndex <= 0) colorIndex = bgColors.size - 1
            mAirPurgeView.setSpeedLevel(colorIndex*1000.toLong())
            colorIndex -= 1
        }
        btnOpenGranlue.setOnClickListener {
            clearMode1Anim()
            mAirPurgeView.onOpenGranule(false)
        }
        btnCloseGranlue.setOnClickListener {
            clearMode1Anim()
            mAirPurgeView.onClearAllGranule()
        }
        btnChangeGranuleMode.setOnClickListener {
            clearMode1Anim()
            mAirPurgeView.onChangeGranuleMode(!mAirPurgeView.getGranuleMode())
        }


        val mode1Color = intArrayOf(
                0xFFDC143C.toInt(),
                0xFF8A2BE2.toInt(),
                0xFF00CED1.toInt(),
                0xFF87CEFA.toInt(),
                0xFF1E90FF.toInt()
        )
        btnMode1.setOnClickListener {
            mCurIndex1 = -1
            mAirPurgeView.setBackgroundColor(mode1Color[0])
            mAnimtor1.setIntValues(550,32)
            mAnimtor1.duration = 10000
            mAnimtor1.interpolator = LinearInterpolator()
            mAnimtor1.addUpdateListener {
                valueAnimator ->
                val value = valueAnimator!!.animatedValue as Int
                mAirPurgeView.setCenterTitle(value.toString())
                if (value>=480 && value<600) {
                    if (mCurIndex1 != 0) {
                        mCurIndex1 = 0
                        mAirPurgeView.setBackgroundColor(mode1Color[0],1000)
                        mAirPurgeView.setBottomTitle("空气极差")
                        mAirPurgeView!!.setSpeedLevel(1800)
                    }
                } else if (value>=360 && value<480) {
                    if (mCurIndex1 != 1) {
                        mCurIndex1 = 1
                        mAirPurgeView.setBackgroundColor(mode1Color[1],1000)
                        mAirPurgeView.setBottomTitle("空气较差")
                        mAirPurgeView!!.setSpeedLevel(2400)
                    }
                } else if (value>=240 && value<360) {
                    if (mCurIndex1 != 2) {
                        mCurIndex1 = 2
                        mAirPurgeView.setBackgroundColor(mode1Color[2],1000)
                        mAirPurgeView.setBottomTitle("空气良好")
                        mAirPurgeView!!.setSpeedLevel(3000)
                    }
                } else if (value>=120 && value<240) {
                    if (mCurIndex1 != 3) {
                        mCurIndex1 = 3
                        mAirPurgeView.setBackgroundColor(mode1Color[3],1000)
                        mAirPurgeView.setBottomTitle("空气较好")
                        mAirPurgeView!!.setSpeedLevel(3600)
                    }

                } else if (value>=0 && value<120) {
                    if (mCurIndex1 != 4) {
                        mCurIndex1 = 4
                        mAirPurgeView.setBackgroundColor(mode1Color[4],1000)
                        mAirPurgeView.setBottomTitle("空气优")
                        mAirPurgeView!!.setSpeedLevel(4200)
                    }
                    if (value == 32) {
                        mAirPurgeView.onCloseAirPurge()
                        mAirPurgeView!!.onClearAllGranule()
                    }
                }
            }

            mAirPurgeView.onOpenGranule(false)
            mAirPurgeView.onOpenAirPurge()
            mAirPurgeView.setPanListener(object : onPanListener{
                override fun onHasOpen() {
                    mAirPurgeView!!.onChangeGranuleMode(true)
                    mAnimtor1.start()
                }
                override fun onHasClose() {
                }
            })


        }

    }



    private fun clearMode1Anim() {
        if (mAnimtor1.isRunning) mAnimtor1.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAirPurgeView.clearAllAnimator()
    }

}
