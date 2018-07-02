package de.tarent.academy.kotlin.bowling

/**
 * Classes for Bowling Kata.
 *
 * @see <a href="http://ccd-school.de/coding-dojo/class-katas/bowling/">Bowling Kata</a>
 */

class Game {
    private var frames = listOf<Frame>()

    fun addRoll(roll : Roll) {
        if (isOver()) {
            throw IllegalStateException("Must not add rolls to a game that's over.")
        }

    }

    fun getFrames() : Collection<Frame> {
        return frames
    }

    fun totalScore() : Int {
        return getFrames().map { frame: Frame -> frame.score }.sum()
    }

    fun isOver() : Boolean {
        return getFrames().size >= 10
    }
}

class Frame(val rolls : Collection<Roll>, val score : Int) {
    // 2 rolls, 10 pins rolled
    fun isSpare() : Boolean {
        return rolls.size == 2 && rolledPins() == 10
    }

    // 1 Roll, 10 pins rolled
    fun isStrike() : Boolean {
        return rolls.size == 1 && rolledPins() == 10
    }

    fun rolledPins() : Int {
        return rolls.map { roll: Roll -> roll.rolledPins }.sum()
    }
}

class Roll(val rolledPins: Int) {
    init {
        if (rolledPins < 0 || rolledPins > 10) {
            throw IllegalArgumentException("Number of rolled pins must be in range [0..10], but got '${rolledPins}'.")
        }
    }
}