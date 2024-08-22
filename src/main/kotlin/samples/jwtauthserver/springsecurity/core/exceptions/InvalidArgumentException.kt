package samples.jwtauthserver.springsecurity.core.exceptions

import org.springframework.http.ProblemDetail

class InvalidArgumentException(
    problemDetail: ProblemDetail,
    throwable: Throwable? = null
) : ProblemDetailException(problemDetail.detail ?: "Invalid argument", throwable, problemDetail)
