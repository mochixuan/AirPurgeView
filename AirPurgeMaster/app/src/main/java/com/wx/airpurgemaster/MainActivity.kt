package com.wx.airpurgemaster

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.wx.airpurgeview.AirPurgeView
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val airPurgeView = findViewById<AirPurgeView>(R.id.airpurge_view)
        val btnChangeNum = findViewById<Button>(R.id.btn1)
        val btnChangeBg = findViewById<Button>(R.id.btn2)
        val btnChangeStart = findViewById<Button>(R.id.btn3)
        val btnChangeEnd = findViewById<Button>(R.id.btn4)
        val btnChangeSpeed = findViewById<Button>(R.id.btn5)



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
            airPurgeView.setCenterTitle(random.nextInt(600).toString())
        }
        btnChangeBg.setOnClickListener {
            if (colorIndex >= bgColors.size) colorIndex = 0
            airPurgeView.setBackgroundColor(bgColors[colorIndex],1000)
            colorIndex += 1
        }
        btnChangeStart.setOnClickListener {
            airPurgeView.onStartAnim()
        }
        btnChangeEnd.setOnClickListener {
            airPurgeView.onEndAnim()
        }
        btnChangeSpeed.setOnClickListener {
            if (colorIndex >= bgColors.size || colorIndex <= 0) colorIndex = bgColors.size - 1
            airPurgeView.setSpeedLevel(colorIndex*1000.toLong())
            colorIndex -= 1
        }
    }

}
