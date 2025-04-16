# KTGBotAPI Stdout Logging Middleware

A simple middleware for logging Telegram Bot API updates to stdout for bots built with [tgbotapi](https://github.com/InsanusMokrassar/ktgbotapi).

## Features

- Logs all Telegram Bot API updates to stdout
- Formats updates in a human-readable format
- Easy to integrate with existing tgbotapi-based bots
- Supports all update types from the Telegram Bot API

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.centralhardware:ktgbotapi-stdout-logging-middleware:1.0-SNAPSHOT")
}
```

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.centralhardware:ktgbotapi-stdout-logging-middleware:1.0-SNAPSHOT'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.centralhardware</groupId>
        <artifactId>ktgbotapi-stdout-logging-middleware</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Usage

Add the middleware to your bot's configuration:

```kotlin
import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.centralhardware.telegram.stdoutLogging

suspend fun main() {
    telegramBotWithBehaviourAndLongPolling(
        "YOUR_BOT_TOKEN",
        CoroutineScope(Dispatchers.IO),
        builder = {
            includeMiddlewares {
                stdoutLogging() // Add this line to enable logging
            }
        }
    ) {
        // Your bot logic here
    }.second.join()
}
```

## Example Output

When a user sends a text message to your bot, you'll see output like this:

```
123456789(John Doe) - Receive: TextContent(text=Hello, bot!)
```

When a user edits a message:

```
123456789(John Doe) - Edit: TextContent(text=Hello, updated message!)
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

## Requirements

- Kotlin 1.8+
- tgbotapi 24.0.0+