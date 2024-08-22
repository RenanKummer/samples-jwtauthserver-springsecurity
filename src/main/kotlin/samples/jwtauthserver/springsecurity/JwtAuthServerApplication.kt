package samples.jwtauthserver.springsecurity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtAuthServerApplication

fun main(args: Array<String>) {
    runApplication<JwtAuthServerApplication>(*args)
}
