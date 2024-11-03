package me.centralhardware.telegram

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.error
import dev.inmo.kslog.common.info
import dev.inmo.micro_utils.common.Warning
import dev.inmo.tgbotapi.bot.ktor.middlewares.TelegramBotMiddlewaresPipelinesHandler
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.data
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.text
import dev.inmo.tgbotapi.types.chat.User
import dev.inmo.tgbotapi.types.update.*
import dev.inmo.tgbotapi.types.update.abstracts.UnknownUpdate
import dev.inmo.tgbotapi.types.update.abstracts.Update

fun User.message() = "${id.chatId.long}($firstName $lastName)"

fun Update.chatId(): String? =
    when (this) {
        is EditMessageUpdate -> data.from
        is MessageUpdate -> data.from
        is EditChannelPostUpdate -> data.from
        is ChannelPostUpdate -> data.from
        is ChosenInlineResultUpdate -> data.from
        is InlineQueryUpdate -> data.from
        is CallbackQueryUpdate -> data.from
        is ShippingQueryUpdate -> null
        is PreCheckoutQueryUpdate -> null
        is PollUpdate -> null
        is PollAnswerUpdate -> null
        is MyChatMemberUpdatedUpdate -> null
        is CommonChatMemberUpdatedUpdate -> null
        is ChatJoinRequestUpdate -> null
        is ChatMessageReactionUpdatedUpdate -> null
        is ChatMessageReactionsCountUpdatedUpdate -> null
        is ChatBoostUpdatedUpdate -> null
        is ChatBoostRemovedUpdate -> null
        is BusinessConnectionUpdate -> null
        is BusinessMessageUpdate -> null
        is EditBusinessMessageUpdate -> null
        is DeletedBusinessMessageUpdate -> null
        is PaidMediaPurchasedUpdate -> null
        is UnknownUpdate -> null
        else -> null
    }?.message()

fun Update.message(): String? {
    val text =
        when (this) {
            is EditMessageUpdate -> data.text
            is MessageUpdate -> data.text
            is EditChannelPostUpdate -> data.text
            is ChannelPostUpdate -> data.text
            is ChosenInlineResultUpdate -> data.query
            is InlineQueryUpdate -> data.query
            is CallbackQueryUpdate -> data.data
            is ShippingQueryUpdate -> null
            is PreCheckoutQueryUpdate -> null
            is PollUpdate -> null
            is PollAnswerUpdate -> null
            is MyChatMemberUpdatedUpdate -> null
            is CommonChatMemberUpdatedUpdate -> null
            is ChatJoinRequestUpdate -> null
            is ChatMessageReactionUpdatedUpdate -> null
            is ChatMessageReactionsCountUpdatedUpdate -> null
            is ChatBoostUpdatedUpdate -> null
            is ChatBoostRemovedUpdate -> null
            is BusinessConnectionUpdate -> null
            is BusinessMessageUpdate -> null
            is EditBusinessMessageUpdate -> null
            is DeletedBusinessMessageUpdate -> null
            is PaidMediaPurchasedUpdate -> null
            is UnknownUpdate -> null
            else -> null
        }
    return if (text == null) {
        null
    } else {
        "${chatId()!!} - $text"
    }
}

@OptIn(Warning::class)
fun TelegramBotMiddlewaresPipelinesHandler.Builder.stdoutLogging() {
    addMiddleware {
        doOnRequestReturnResult { result, request, _ ->
            kotlin
                .runCatching {
                    if (result.getOrNull() !is ArrayList<*>) return@doOnRequestReturnResult null

                    (result.getOrNull() as ArrayList<Update>).forEach {
                        it.message()?.let { message -> KSLog.info(message) }
                    }
                }
                .onFailure { KSLog.error("Failed to log request", it) }
            null
        }
    }
}
