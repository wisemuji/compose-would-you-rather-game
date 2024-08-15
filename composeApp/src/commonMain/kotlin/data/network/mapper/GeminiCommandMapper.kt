package data.network.mapper

import data.network.model.GeminiCommand
import model.Option

fun Option.toGeminiCommand(): GeminiCommand {
    return when (this) {
        Option.A -> GeminiCommand.A
        Option.B -> GeminiCommand.B
    }
}
