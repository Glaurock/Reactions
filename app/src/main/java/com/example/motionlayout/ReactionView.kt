package com.example.motionlayout

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.setMargins

const val TAG = "GREG"

/*
** Child views must implement OnTouchListener
 */

class ReactionView(context: Context, attrs: AttributeSet? = null) :
        LinearLayout(context, attrs), View.OnTouchListener {

    companion object {
        private const val START_WIDTH = 130
        private const val END_WIDTH = 600
//        private const val EXPAND_DURATION = 2000L
    }

    private var listener: Listener? = null

//    private val animator = ValueAnimator.ofInt(START_WIDTH, END_WIDTH).apply {
//        duration = EXPAND_DURATION
//        addUpdateListener {
//            val animatedValue = it.animatedValue as Int
//            this@ReactionView.layoutParams = this@ReactionView.layoutParams.apply {
//                width = animatedValue
//            }
//        }
//    }

    enum class State {
        COLLAPSED,
        EXPANDED
    }

    private var state: State = State.COLLAPSED
    private var isOnLongPressed: Boolean = false //todo: replace with enum or use State

    private val simpleLongPressListener: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            Log.d(TAG, "onLongPress: ")
            isOnLongPressed = true
            expand()
        }
    })

    private val simpleScrollListener: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.d(TAG, "onScroll: ")
            //todo: replace by list to avoid cast
            this@ReactionView.children.forEach { view ->
                (view as? EmojiView)?.onTouch(this@ReactionView, e2)
            }
            return true
        }
    })

    init {
        setOnTouchListener(this)
    }

    private fun hideAllExceptFirst() {
        //todo: replace by list to avoid cast
        this.children.forEachIndexed { index, view ->
            view as EmojiView
            val state = if (index == 3) EmojiView.State.UNSELECTED else EmojiView.State.HIDDEN
            view.switchState(state)
        }
    }

    private fun showAll() {
        //todo: replace by list to avoid cast
        this.children.forEachIndexed { index, view ->
            view as EmojiView
            val state = if (index == 3) EmojiView.State.SELECTED else EmojiView.State.UNSELECTED
            view.switchState(state)
        }
    }

    fun expand() {
        if (state == State.EXPANDED) {
            return
        }
//        animator.start()
        this.layoutParams = this.layoutParams.apply {
            width = END_WIDTH
        }
        state = State.EXPANDED
        showAll()
    }

    fun collapse() {
        if (state == State.COLLAPSED) {
            return
        }
//        animator.reverse()
        this.layoutParams = this.layoutParams.apply {
            width = START_WIDTH
        }
        state = State.COLLAPSED
        hideAllExceptFirst()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (simpleLongPressListener.onTouchEvent(event)) return true

        if (!isOnLongPressed) {
            return false
        }

        if (simpleScrollListener.onTouchEvent(event)) return true

        when (event?.actionMasked) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                Log.d(TAG, "onTouch: End event")
                //end of move: get selected item and reset state
                //todo: replace by list to avoid cast
                val selectedIndex = this.children.indexOfFirst {
                    it as EmojiView
                    it.selected()
                }
                listener?.onItemSelected(selectedIndex)
                collapse()
                isOnLongPressed = false
                return true
            }
        }
        return false
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onItemSelected(selectedIndex: Int)
    }
}