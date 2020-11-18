package com.example.motionlayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView

class EmojiView(context: Context, attrs: AttributeSet? = null) : LottieAnimationView(context, attrs), View.OnTouchListener {

//    companion object {
//        private const val EMOJI_VIEW_WIDTH = 100
//        private const val EMOJI_VIEW_HEIGHT = 100
//    }

    enum class State {
        SELECTED,
        UNSELECTED,
        HIDDEN
    }

    var state: State = State.HIDDEN //todo: care bug here with first

//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        //todo: from xml
//        this.layoutParams = LinearLayout.LayoutParams(EMOJI_VIEW_WIDTH, EMOJI_VIEW_HEIGHT).apply {
//            gravity = Gravity.CENTER
//            setMargins(20)
//        }
//        this.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_bright))
//    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }

        if (event.x >= this.x && event.x <= this.x + this.width) {
//            Log.d(TAG, "onTouch: in Child $this")
            switchState(State.SELECTED)
            return true
        }
        switchState(State.UNSELECTED)
        return false
    }

    fun switchState(newState: State) {
        if (this.state == newState) {
            return
        }
        when(newState) {
            State.SELECTED -> {
                show()
                select()
            }
            State.UNSELECTED -> {
                show()
                unSelect()
            }
            State.HIDDEN -> {
                unSelect()
                hide()
            }
        }
        this.state = newState
    }

    //todo: rename
    fun selected(): Boolean = this.state == State.SELECTED

    private fun hide() {
        this.isVisible = false
    }

    private fun show() {
        this.isVisible = true
    }

    private fun unSelect() {
        this.cancelAnimation()
        this.scaleX = 1.0f
        this.scaleY = 1.0f
        this.translationY = 0.0f
    }

    private fun select() {
        this.playAnimation()
        this.scaleX =  2f
        this.scaleY = 2f
        this.translationY = -50.0f
    }
}