package data.network.model

enum class GeminiCommand {
    START_GAME,
    A,
    B;

    override fun toString(): String = when (this) {
        START_GAME -> "Start game"
        A -> "A"
        B -> "B"
    }
}
