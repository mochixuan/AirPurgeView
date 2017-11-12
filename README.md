# AirPurgeView

## 效果图两个
![main.png](http://upload-images.jianshu.io/upload_images/2646598-8db45c84c4021f47.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![mode.gif](http://upload-images.jianshu.io/upload_images/2646598-3eaf1f0b4e27eb72.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 引用

``` java

Maven

<dependency>
  <groupId>com.wx.library</groupId>
  <artifactId>airpurgeview</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>

Gradle

compile 'com.wx.library:airpurgeview:1.0'

```

## 可设置的参数
``` java

<declare-styleable name="AirPurgeLayoutView">
	<attr name="granule_num" format="integer"/>    <!--颗粒物数-->
	<attr name="granule_radius" format="float"/>   <!--颗粒物数-->
	<attr name="granule_color" format="color"/>    <!--颗粒颜色,最好不透明的-->

	<attr name="circle_proportion" format="float"/>  <!--圆的直径大小占宽高中最小的那个的比例-->
	<attr name="main_color" format="color"/>            <!--实体圆环和扇叶圆环和字体的颜色-->
	<attr name="solidring_width" format="float"/>    <!--最外圈实线圆环的宽度，最好不要太大-->
	<attr name="dashedring_width" format="float"/>    <!--扇叶圆环的宽度，经测量是solidwidth四倍最好，可以适当调节-->
	<attr name="toptitle" format="string"/>
	<attr name="centertitle" format="string"/>
	<attr name="bottomtitle" format="string"/>
	<attr name="toptitle_textsize" format="dimension"/>
	<attr name="centertitle_textsize" format="dimension"/>
	<attr name="bottomtitle_textsize" format="dimension"/>
	<attr name="speed_level" format="integer"/>     <!--风速默认3000，可以修改-->
	<attr name="eachpan_angle" format="float"/>    <!--每片扇叶的角度 自己要计算好加间隙 一共360-->
	<attr name="eachpan_anglegap" format="float"/>    <!--相邻扇叶的间隙-->
</declare-styleable>

```

## sample

> XML

``` java

<com.wx.airpurgeview.AirPurgeLayoutView
        android:id="@+id/airpurge_view"
        android:layout_width="match_parent"
        android:layout_height="400dp"
        app:toptitle="PM2.5"
        app:centertitle="521"
        app:bottomtitle="空气优"/>

```

> Java

``` java

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


```

## 如果问题可以提出，我有时间就改进。。。

## 后续使用过程中有问题，也会进行更新。。。