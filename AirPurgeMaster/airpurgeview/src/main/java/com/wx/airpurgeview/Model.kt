package com.wx.airpurgeview

//data class GranuleModel(var radius: Float,var angle:Float,var radiusOffset:Float,var angleOffset:Float)

//减小内存消耗，半径偏移量和角度偏移量在线程中计算完
data class GranuleModel(var radius: Float,var angle:Float)

interface onPanListener{
    fun onHasOpen()
    fun onHasClose()
}