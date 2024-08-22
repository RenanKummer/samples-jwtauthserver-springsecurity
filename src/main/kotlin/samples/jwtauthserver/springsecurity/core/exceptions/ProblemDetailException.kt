package samples.jwtauthserver.springsecurity.core.exceptions

import org.springframework.http.ProblemDetail

abstract class ProblemDetailException(
    message: String,
    throwable: Throwable? = null,
    val problemDetail: ProblemDetail? = null
) : Exception(message, throwable)
