package samples.jwtauthserver.springsecurity.webapi.helpers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI

fun <T> ok(body: T): ResponseEntity<T> = ResponseEntity.ok(body)
fun noContent(): ResponseEntity<Any?> = ResponseEntity.noContent().build()
fun <T> created(body: T, uri: String? = null): ResponseEntity<T> = when (uri) {
    null -> ResponseEntity.status(HttpStatus.CREATED).body(body)
    else -> ResponseEntity.created(URI.create(uri)).body(body)
}

fun <T> badRequest(body: T): ResponseEntity<T> = ResponseEntity.badRequest().body(body)
fun <T> notFound(body: T): ResponseEntity<T> = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
