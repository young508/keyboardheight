# keyboardheight

Android keyboard height change listener and support android11 keyboard height change animation

android的软键盘高度监听一直是一个老大难的问题，但是也不是完全没有解决方案，目前市面上使用的比较多的方案是：
在Window上放置一个高度为全屏，宽度为0的Popupwindow，通过监听其高度的改变来获取键盘的高度变化。

然而，在iOS系统中，键盘的高度变化非常容易获取，并且可以进行高度变化的过程监听，效果比Android好太多，这一馋哭了Android小伙伴的功能好在在Android11中也得到了支持🎉。
这个库就是将键盘的动画和非动画做了一个结合，方面使用者轻松实现自己想要的效果。

## 普通正常的键盘高度改变

<img src="./image/heightchange.gif?raw=true" width="200">
- 可以看到，无论键盘高度如何变化，都可以精确获取到键盘的高度改变
- 这里键盘的高度改变在不同版本也是有所区分，在Android11上，是通过WindowInset来实现，在android11以下，是通过popupwindow的高度改变来实现

```kotlin
    val height1 = HeightProvider(this)
lifecycle.addObserver(height1)
val obj = object : ImeHeightListener {
    override fun imeHeightChanged(height: Int) {
        findViewById<EditText>(R.id.et_1).translationY = -height.toFloat()
    }
}
height1.setHeightListener(imeChangeListener = obj)
height1.showProvider()
```

## Android11支持的键盘高度变化动画

<img src="./image/heightanimation.gif?raw=true" width="200">
- 上面的效果就是实现了控件跟随键盘移动而变化的效果
- Android11的WindowInset来实现键盘动画效果。这是Jetpack中推荐的方式，如果对该类不熟悉，建议自己学习以下。

```kotlin
val height2 = HeightProvider(this)
lifecycle.addObserver(height2)

height2.setHeightListener(imeAnimationListener = object : ImeAnimationListener {
    override fun imeChangeStart(imeShow: Boolean) {

    }

    override fun imeHeightProgress(imeShow: Boolean, height: Int) {
        findViewById<EditText>(R.id.et_1).translationY = -height.toFloat()
    }

    override fun imeChangeEnd(imeShow: Boolean) {

    }

    override fun imeButtonChangeHeight(height: Int, standardHeight: Int) {
        findViewById<EditText>(R.id.et_1).translationY = -height.toFloat()
    }
}).showProvider()
```

--------------------------------------------------------------------------------------------
该库代码量不多，可以下面方式引用



也可以直接把代码copy到项目中方便自己定制

有兼容适配问题，欢迎提Issue

如果觉得本库有帮助，希望可以点点Star！




