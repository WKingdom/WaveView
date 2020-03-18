package com.hopechart.waveview

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private  var animatorIsPlay = true
    private val mAnimator by lazy {
        ObjectAnimator.ofFloat(ivAlbum, "rotation", 0.0f, 360.0f)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWaveView()
        initAnim()
        ivAlbum?.run {
            val bitMap = getDrawable(R.drawable.timg)?.toBitmap(200, 200)
            Palette.generateAsync(bitMap) { palette ->
                val rgb = palette?.vibrantSwatch?.rgb
                if (rgb != null) {
                    waveView?.setColor(rgb)
                } else {
                    waveView?.setColor(resources.getColor(R.color.colorTextDark))
                }
            }
            setImageBitmap(bitMap)
            drawable?.setDither(true)

        }
        window.decorView.postDelayed({
            ivBlur.setImageResource(R.drawable.timg)
            Blurry.with(this@MainActivity)
                .radius(15)
                .sampling(8)
                .color(Color.argb(80, 0, 0, 0))
//                .async()
                .capture(ivBlur)
                .into(ivBlur)
        }, 300)
        ivPlayPause.setOnClickListener {
            animatorIsPlay = !animatorIsPlay
            if(animatorIsPlay){
                waveView.start()
                mAnimator.resume()
                ivPlayPause.setImageResource(R.drawable.ic_media_on_play)
            }else{
                waveView.stop()
                mAnimator.pause()
                ivPlayPause.setImageResource(R.drawable.ic_media_on_pause)
            }
        }
    }

    override fun onDestroy() {
        waveView?.stop()
        mAnimator.cancel()
        super.onDestroy()
    }

    private fun initAnim() {
        mAnimator.duration = 15000
        mAnimator.repeatCount = Animation.INFINITE
        mAnimator.repeatMode = ObjectAnimator.RESTART
        mAnimator.interpolator = LinearInterpolator()
        mAnimator.start()
    }

    @Suppress("DEPRECATION")
    private fun initWaveView() {
        waveView.setDuration(4000)
        waveView.setStyle(Paint.Style.STROKE)
        waveView.setSpeed(400)
        waveView.setColor(resources.getColor(R.color.colorTextDim))
        waveView.setInterpolator(AccelerateInterpolator(1.2f))
        waveView.start()
    }
}
