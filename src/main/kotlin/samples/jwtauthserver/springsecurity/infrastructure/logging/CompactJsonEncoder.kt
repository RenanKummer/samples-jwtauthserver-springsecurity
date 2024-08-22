package samples.jwtauthserver.springsecurity.infrastructure.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.JsonEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.ThrowableProxy
import com.fasterxml.jackson.core.JsonGenerator
import org.slf4j.event.KeyValuePair
import samples.jwtauthserver.springsecurity.core.models.LoggableException
import samples.jwtauthserver.springsecurity.infrastructure.serialization.JsonSerializer
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat

/**
 * Logback encoder for structured logs as JSON with short names for default properties.
 *
 * Properties included by default:
 *   - `@t` - timestamp
 *   - `@l` - level
 *   - `@m` - message
 *   - `@s` - source (i.e. logger name)
 *
 * Properties included if present:
 *  - `@x` - exception
 *  - Mapped Diagnostic Context (MDC) properties
 *  - Custom key-value properties
 */
class CompactJsonEncoder : JsonEncoder() {
    private val objectMapper = JsonSerializer().objectMapper()

    override fun encode(event: ILoggingEvent?): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val jsonGenerator = objectMapper.factory.createGenerator(outputStream)

        jsonGenerator.writeStartObject()

        val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        jsonGenerator.writeStringField("@t", dateTimeFormatter.format(event!!.timeStamp))
        jsonGenerator.writeStringField("@l", event.level?.levelStr ?: Level.INFO.levelStr)
        jsonGenerator.writeStringField("@m", event.formattedMessage)
        jsonGenerator.writeStringField("@c", event.loggerName)

        val throwableProxy: IThrowableProxy? = event.throwableProxy
        if (throwableProxy != null && throwableProxy is ThrowableProxy) {
            val loggableException = LoggableException(
                message = throwableProxy.message,
                stackTrace = throwableProxy.throwable.stackTraceToString()
            )

            jsonGenerator.writeObjectField("@x", loggableException)
        }

        jsonGenerator.writeOptionalMdcFields(event.mdcPropertyMap)
        jsonGenerator.writeOptionalLogProperties(event.keyValuePairs)

        jsonGenerator.writeEndObject()
        jsonGenerator.writeNewline()
        jsonGenerator.flush()

        return outputStream.toByteArray()
    }
}

private fun JsonGenerator.writeOptionalMdcFields(properties: Map<String, String>?) {
    properties?.forEach { (key, value) ->
        val camelCaseKey = key.replaceFirstChar { it.lowercase() }
        this.writeStringField(camelCaseKey, value)
    }
}

private fun JsonGenerator.writeOptionalLogProperties(properties: List<KeyValuePair>?) {
    properties?.forEach { property ->
        val camelCaseKey = property.key.replaceFirstChar { it.lowercase() }
        this.writeObjectField(camelCaseKey, property.value)
    }
}

private fun JsonGenerator.writeNewline() = this.writeRaw('\n')
