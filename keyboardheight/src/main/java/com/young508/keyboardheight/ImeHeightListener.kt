package com.young508.keyboardheight

/**
 * 低于Android11 键盘高度改变，不支持键盘动画
 */
interface ImeHeightListener {
    /**
     * 键盘高度监听
     *
     * @param height：键盘的高度
     */
    fun imeHeightChanged(height: Int)
}