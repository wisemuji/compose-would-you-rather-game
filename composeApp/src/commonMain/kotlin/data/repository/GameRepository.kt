package data.repository

import model.Option
import model.TurnResult

interface GameRepository {
    suspend fun startGame(): TurnResult
    suspend fun selectOption(option: Option): TurnResult
}
