package me.centralhardware.telegram

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.error
import dev.inmo.kslog.common.info
import dev.inmo.micro_utils.common.Warning
import dev.inmo.tgbotapi.bot.ktor.middlewares.TelegramBotMiddlewaresPipelinesHandler
import dev.inmo.tgbotapi.extensions.utils.asContentMessage
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from
import dev.inmo.tgbotapi.types.chat.Chat
import dev.inmo.tgbotapi.types.chat.User
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.*
import dev.inmo.tgbotapi.types.update.*
import dev.inmo.tgbotapi.types.update.abstracts.UnknownUpdate
import dev.inmo.tgbotapi.types.update.abstracts.Update

fun User?.format() =  this?.let{
    "${id.chatId.long}($firstName $lastName)"
}?: ""

fun Chat?.format() =  this?.let{
    "${id.chatId.long}"
}?: ""

fun ContentMessage<MessageContent>.format(): String? = when (content) {
    is ContactContent -> "${(content as ContactContent).contact}"
    is DiceContent -> "${(content as DiceContent).dice}"
    is GameContent -> "${(content as GameContent).game}"
    is GiveawayContent -> "$content"
    is GiveawayPublicResultsContent -> "${(content as GiveawayPublicResultsContent).giveaway}"
    is InvoiceContent -> "${(content as InvoiceContent).invoice}"
    is LiveLocationContent -> "${(content as LiveLocationContent).location}"
    is StaticLocationContent -> "${(content as StaticLocationContent).location}"
    is PhotoContent -> "$content"
    is AnimationContent -> "$content"
    is VideoContent -> "$content"
    is StickerContent -> "${(content as StickerContent).media}"
    is MediaGroupContent<*> -> "$content"
    is AudioContent -> "$content"
    is DocumentContent -> "$content"
    is VoiceContent -> "$content"
    is VideoNoteContent -> "${(content as VideoNoteContent).media}"
    is PaidMediaInfoContent -> "$content"
    is PollContent -> "${(content as PollContent).poll}"
    is StoryContent -> "${(content as StoryContent).story}"
    is TextContent -> "$content"
    is VenueContent -> "${(content as VenueContent).venue}"
    else -> {
        KSLog.info("Unknown content type: ${content?.javaClass?.simpleName}")
        null
    }
}

fun Update.format(): String? {
    var from = "";
    var text = ""
    when (this) {
            is EditMessageUpdate -> {
                from = data.from.format()
                text = "Edit: " + data.asContentMessage()?.format()
            }
            is MessageUpdate -> {
                from = data.from.format()
                text = "Receive: " + data.asContentMessage()?.format()
            }
            is InlineQueryUpdate -> {
                from = data.from.format()
                text = data.toString()
            }

            is CallbackQueryUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is EditChannelPostUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is ChannelPostUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is ChosenInlineResultUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is ShippingQueryUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is PreCheckoutQueryUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is PollUpdate -> {
                text = data.toString()
            }
            is PollAnswerUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is MyChatMemberUpdatedUpdate -> {
                from = data.user.format()
                text = data.toString()
            }
            is CommonChatMemberUpdatedUpdate -> {
                from = data.user.format()
                text = data.toString()
            }
            is ChatJoinRequestUpdate -> {
                from = data.user.format()
                text = data.toString()
            }
            is ChatMessageReactionUpdatedUpdate -> {
                from = data.chat.format()
                text = data.toString()
            }
            is ChatMessageReactionsCountUpdatedUpdate -> {
                from = data.chat.format()
                text = data.toString()
            }
            is ChatBoostUpdatedUpdate -> {
                from = data.chat.format()
                text = data.toString()
            }
            is ChatBoostRemovedUpdate -> {
                from = data.chat.format()
                text = data.toString()
            }
            is BusinessConnectionUpdate -> {
                from = data.user.format()
                text = data.toString()
            }
            is BusinessMessageUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is EditBusinessMessageUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is DeletedBusinessMessageUpdate -> {
                from = data.chat.format()
                text = data.toString()
            }
            is PaidMediaPurchasedUpdate -> {
                from = data.from.format()
                text = data.toString()
            }
            is UnknownUpdate -> {}
            else -> null
        }
    return "${from.format()} - $text"
}

@OptIn(Warning::class)
fun TelegramBotMiddlewaresPipelinesHandler.Builder.stdoutLogging() {
    addMiddleware {
        doOnRequestReturnResult { result, request, _ ->
            kotlin
                .runCatching {
                    if (result.getOrNull() !is ArrayList<*>) return@doOnRequestReturnResult null

                    (result.getOrNull() as ArrayList<Update>).forEach {
                        it.format()?.let { message -> KSLog.info(message) }
                    }
                }
                .onFailure { KSLog.error("Failed to log request", it) }
            null
        }
    }
}
