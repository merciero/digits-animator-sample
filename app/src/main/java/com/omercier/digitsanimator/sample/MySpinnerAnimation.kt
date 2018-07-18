package com.omercier.digitsanimator.sample

import com.omercier.digitsanimator.CharBits
import com.omercier.digitsanimator.DisplayAnimation

class MySpinnerAnimation : DisplayAnimation() {

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
}