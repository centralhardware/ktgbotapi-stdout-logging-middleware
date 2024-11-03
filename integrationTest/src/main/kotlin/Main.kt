package me.centralhardware.telegram

import dev.inmo.micro_utils.common.Warning
import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(Warning::class)
suspend fun main() {
    telegramBotWithBehaviourAndLongPolling(
        System.getenv("BOT_TOKEN"),
        CoroutineScope(Dispatchers.IO),
        builder = {
            includeMiddlewares {
                stdoutLogging()
            }
        }
    ) {

    }.second.join()
}