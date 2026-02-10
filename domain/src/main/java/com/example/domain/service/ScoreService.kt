package com.example.domain.service

import com.example.common.model.PlayerScore

class ScoreService {

    companion object {
        private const val MAX_TIME_POINTS = 500
        private const val FIRST_GUESS_BONUS = 300
        private const val SECOND_GUESS_BONUS = 200
        private const val THIRD_GUESS_BONUS = 100
        private const val DEFAULT_GUESS_BONUS = 50
        private const val DRAWER_BONUS = 100
    }

    fun calculateGuesserPoints(
        remainingSeconds: Int,
        totalSeconds: Int,
        guessPosition: Int
    ): Int {
        val timeRatio = remainingSeconds.toFloat() / totalSeconds.toFloat()
        val timePoints = (timeRatio * MAX_TIME_POINTS).toInt()

        val positionBonus = when (guessPosition) {
            1 -> FIRST_GUESS_BONUS
            2 -> SECOND_GUESS_BONUS
            3 -> THIRD_GUESS_BONUS
            else -> DEFAULT_GUESS_BONUS
        }

        return timePoints + positionBonus
    }

    fun getDrawerBonus(): Int = DRAWER_BONUS

    fun updateScores(
        currentScores: List<PlayerScore>,
        guesserId: String,
        guesserPoints: Int,
        drawerId: String,
        drawerPoints: Int
    ): List<PlayerScore> {
        return currentScores.map { score ->
            when (score.playerId) {
                guesserId -> score.copy(score = score.score + guesserPoints)
                drawerId -> score.copy(score = score.score + drawerPoints)
                else -> score
            }
        }
    }

    fun createInitialScore(playerId: String, playerName: String): PlayerScore {
        return PlayerScore(
            playerId = playerId,
            playerName = playerName,
            score = 0
        )
    }

    fun sortByScore(scores: List<PlayerScore>): List<PlayerScore> {
        return scores.sortedByDescending { it.score }
    }
}