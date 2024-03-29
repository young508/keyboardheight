package com.young.keyboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.young508.keyboardheight.HeightProvider
import com.young508.keyboardheight.ImeAnimationListener
import com.young508.keyboardheight.ImeHeightListener
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//        val height1 = HeightProvider(this)
//        lifecycle.addObserver(height1)
//        val obj = object : ImeHeightListener {
//            override fun imeHeightChanged(height: Int) {
//                findViewById<EditText>(R.id.et_1).translationY = -height.toFloat()
//            }
//        }
//        height1.setHeightListener(imeChangeListener = obj)
//        height1.showProvider()

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        val height2 = HeightProvider(this)
        lifecycle.addObserver(height2)

        height2.setHeightListener(
            imeChangeListener = object :ImeHeightListener{
                override fun imeHeightChanged(height: Int) {

                }
            },


            imeAnimationListener = object : ImeAnimationListener {
            override fun imeChangeStart(imeShow: Boolean) {

            }

            override fun imeHeightProgress(imeShow: Boolean, height: Int) {
                findViewById<EditText>(R.id.et_1).translationY = -height.toFloat()
            }

            override fun imeChangeEnd(imeShow: Boolean, standardHeight: Int) {

            }


            override fun imeButtonChangeHeight(height: Int, standardHeight: Int) {
                findViewById<EditText>(R.id.et_1).translationY = -height.toFloat()
            }
        })

        MainScope().launch {
            delay(200)
            height2.showProviderNeedDelay()
        }


    }
}