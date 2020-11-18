package com.example.motionlayout

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.GestureDetectorCompat


class MainActivity : AppCompatActivity(), ReactionView.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reactionView = findViewById<ReactionView>(R.id.reaction_view)

        findViewById<View>(R.id.root_view).setOnClickListener {
            reactionView.collapse()
        }
        reactionView.setListener(this)
    }

    override fun onItemSelected(selectedIndex: Int) {
        Toast.makeText(this, "Item selected $selectedIndex", Toast.LENGTH_SHORT).show()
    }
}