package com.young508.keyboardheight

/**
 * Android11 键盘高度动画回调
 */
interface ImeAnimationListener {

    /**
     * 键盘动画开始
     * @param imeShow：true,出现键盘 false：键盘消失
     */
    fun imeChangeStart(imeShow: Boolean = false)

    /**
     * 键盘动画过程中
     * @param imeShow：true,出现键盘 false：键盘消失
     * @param height：键盘过程中的高度
     */
    fun imeHeightProgress(imeShow: Boolean = false, height: Int)

    /**
     * 键盘动画结束
     * @param imeShow：true,出现键盘 false：键盘消失
     * @param standardHeight: 在使用时，可以保存第一次出现的合理范围的值，一般在250-350dp
     */
    fun imeChangeEnd(imeShow: Boolean = false,standardHeight:Int)

    /**
     * 键盘出现后，键盘上的某些按钮改变，会让键盘高度突变
     * 这时候，不会走键盘动画，可以通过此方法监听
     *
     * 和键盘动画是互斥关系，也即走键盘动画时，不会走该方法
     *
     * @param height：键盘的高度
     * @param standardHeight：标准的键盘高度，因为键盘出现后，按了某些
     * 按钮，此时键盘高度改变，关闭键盘后，下次再次打开键盘，键盘的高度会
     * 先变成改变后的高度，然后才变成标准的键盘高度
     */
    fun imeButtonChangeHeight(height: Int,standardHeight:Int)
}