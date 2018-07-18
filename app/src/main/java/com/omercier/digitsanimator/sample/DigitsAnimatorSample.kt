/*
 * Copyright 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.omercier.digitsanimator.sample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay

import com.omercier.digitsanimator.*
import java.util.*
import kotlin.concurrent.schedule

class DigitsAnimatorSample : Activity() {

    companion object { private val TAG = DigitsAnimatorSample::class.java.simpleName }

    private lateinit var alphaNumDisplay: AlphanumericDisplay

    // SubChar display: used to display small lines alphanumeric characters are composed of
    private lateinit var subCharDisplay: SubCharDisplay

    // Players are used to control animations on the SubChar display
    private lateinit var spinnerPlayer: SubCharPlayer
    private lateinit var barsPlayer: SubCharPlayer
    private lateinit var leftToRightPlayer: SubCharPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alphaNumDisplay = AlphanumericDisplay(BoardDefaults.getI2cBus()).apply {
            setEnabled(true)
            clear()
        }

        // SubCharDisplay is a wrapper for an alphanumeric display that adds animation capabilities
        subCharDisplay = SubCharDisplay(alphaNumDisplay)
    }

    override fun onResume() {
        super.onResume()

        // Enable / disable code here to check out the different demos

        demoBarsAnimation()
        //demoACustomAnimation()
        //demoStopCallback()
        //demoMyOwnSpecialCharacter()
    }

    private fun demoMyOwnSpecialCharacter() {
        // for example, a 0 with a + sign in the middle

        val zero = CharBits.TOP or CharBits.RIGHT_1 or CharBits.RIGHT_2 or
                   CharBits.BOTTOM or CharBits.LEFT_1 or CharBits.LEFT_2

        val plus = CharBits.MIDDLE_V_1 or CharBits.MIDDLE_V_2 or
                   CharBits.MIDDLE_H_1 or CharBits.MIDDLE_H_2

        val mySpecialZeroWithPlus = zero or plus

        subCharDisplay.displayLowLevel(0, CharBits.DOT)
        subCharDisplay.displayLowLevel(1, CharBits.EMPTY) // clears the digit
        subCharDisplay.displayLowLevel(2, mySpecialZeroWithPlus)
        subCharDisplay.displayLowLevel(3, '>') // we can still use normal ascii characters
    }

    private fun demoBarsAnimation() {
        barsPlayer = SubCharPlayer(subCharDisplay, HorizontalBarsAnimation())
        barsPlayer.start() // infinite unless stopped
    }

    fun demoACustomAnimation() {
        val spinnerWithThreeLoopsAnimation = object : DisplayAnimation() {


            // for more complex animation you might want to use
            // something else than a class variable that contains everything

            private val states: List<IntArray> = listOf(
                    intArrayOf(CharBits.TOP, CharBits.EMPTY, CharBits.EMPTY, CharBits.EMPTY),
                    intArrayOf(CharBits.EMPTY, CharBits.TOP, CharBits.EMPTY, CharBits.EMPTY),
                    intArrayOf(CharBits.EMPTY, CharBits.EMPTY, CharBits.TOP, CharBits.EMPTY),
                    intArrayOf(CharBits.EMPTY, CharBits.EMPTY, CharBits.EMPTY, CharBits.TOP),
                    intArrayOf(CharBits.EMPTY, CharBits.EMPTY, CharBits.EMPTY, (CharBits.RIGHT_1 or CharBits.RIGHT_2)),
                    intArrayOf(CharBits.EMPTY, CharBits.EMPTY, CharBits.EMPTY, CharBits.BOTTOM),
                    intArrayOf(CharBits.EMPTY, CharBits.EMPTY, CharBits.BOTTOM, CharBits.EMPTY),
                    intArrayOf(CharBits.EMPTY, CharBits.BOTTOM, CharBits.EMPTY, CharBits.EMPTY),
                    intArrayOf(CharBits.BOTTOM, CharBits.EMPTY, CharBits.EMPTY, CharBits.EMPTY),
                    intArrayOf((CharBits.LEFT_1 or CharBits.LEFT_2), CharBits.EMPTY, CharBits.EMPTY, CharBits.EMPTY)
            )

            override fun getStepCount() = states.size
            override fun getStepState(index: Int) = states[index]
            override fun getLoopCount() = 3

            // also available: isBouncing, getDefaultSpeed, getSpeedForStep
        }

        val spinnerPlayer = SubCharPlayer(subCharDisplay, spinnerWithThreeLoopsAnimation)

        spinnerPlayer.start()
    }

    fun demoStopCallback() {
        barsPlayer = SubCharPlayer(subCharDisplay, HorizontalBarsAnimation())
        barsPlayer.start {
            // a start function optional lambda is a stop() animation callback
            Log.v(TAG, "bars animation just stopped!")
        }

        // typically stop() would be called given a certain app state
        // here we just use a 5s delay for demo purposes
        Timer("stop_animation_timer", false).apply {
            schedule(5000) {

                barsPlayer.stop()

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alphaNumDisplay.apply {
            clear()
            setEnabled(false)
            close()
        }
    }
}
