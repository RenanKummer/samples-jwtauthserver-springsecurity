package samples.jwtauthserver.springsecurity.core.extensions

import org.slf4j.Logger

/**
 * Logs error [message] with custom [properties] to structured logging provider.
 *
 * @param message The log message.
 * @param properties The custom properties.
 */
fun Logger.logError(message: String, vararg properties: Pair<String, *>) {
    val builder = this.atError()
    properties.forEach { property -> builder.addKeyValue(property.first, property.second) }
    builder.log(message)
}

/**
 * Logs error [message] with custom [properties] to structured logging provider.
 *
 * @param message The log message.
 * @param throwable The error thrown.
 * @param properties The custom properties.
 */
fun Logger.logError(message: String, throwable: Throwable, vararg properties: Pair<String, *>) {
    val builder = this.atError()
    properties.forEach { property -> builder.addKeyValue(property.first, property.second) }
    builder.setCause(throwable).log(message)
}

/**
 * Logs warning [message] with custom [properties] to structured logging provider.
 *
 * @param message The log message.
 * @param properties The custom properties.
 */
fun Logger.logWarn(message: String, vararg properties: Pair<String, *>) {
    val builder = this.atWarn()
    properties.forEach { property -> builder.addKeyValue(property.first, property.second) }
    builder.log(message)
}

/**
 * Logs info [message] with custom [properties] to structured logging provider.
 *
 * @param message The log message.
 * @param properties The custom properties.
 */
fun Logger.logInfo(message: String, vararg properties: Pair<String, *>) {
    val builder = this.atInfo()
    properties.forEach { property -> builder.addKeyValue(property.first, property.second) }
    builder.log(message)
}

/**
 * Logs debug [message] with custom [properties] to structured logging provider.
 *
 * @param message The log message.
 * @param properties The custom properties.
 */
fun Logger.logDebug(message: String, vararg properties: Pair<String, *>) {
    val builder = this.atDebug()
    properties.forEach { property -> builder.addKeyValue(property.first, property.second) }
    builder.log(message)
}

/**
 * Logs trace [message] with custom [properties] to structured logging provider.
 *
 * @param message The log message.
 * @param properties The custom properties.
 */
fun Logger.logTrace(message: String, vararg properties: Pair<String, *>) {
    val builder = this.atTrace()
    properties.forEach { property -> builder.addKeyValue(property.first, property.second) }
    builder.log(message)
}
