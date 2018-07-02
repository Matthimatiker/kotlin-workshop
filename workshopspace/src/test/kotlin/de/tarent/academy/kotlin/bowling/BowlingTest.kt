package de.tarent.academy.kotlin.bowling

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class BowlingTest {

    var game : Game = Game()


    /**
     * Pre-defined set of rolls, from the data set that is
     * shown in the Bowling kata.
     *
     * @see <a href="http://ccd-school.de/wp-content/uploads/2016/04/img_570a137fc5456-768x640.png">Data Table</a>
     */
    val rolls = listOf<Int>(
            1,
            4,
            4,
            5,
            6,
            4,
            5,
            5,
            10,
            0,
            1,
            7,
            3,
            6,
            4,
            10,
            2,
            8,
            6
    )

    @Before
    fun setUp() {
        game = Game()
    }

    @Test
    fun `score is initially zero`() {
        assertEquals(0, game.totalScore())
    }

    @Test
    fun `shows correct score after first roll`() {
        playUntilRoll(0)

        assertEquals(1, game.totalScore())
    }

    fun playUntilRoll(rollIndex : Int) {
        for (index : Int in 0..rollIndex) {
            game.addRoll(Roll(rolls[index]))
        }
    }
}