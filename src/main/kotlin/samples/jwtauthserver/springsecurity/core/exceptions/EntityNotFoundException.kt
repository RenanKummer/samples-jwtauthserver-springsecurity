package samples.jwtauthserver.springsecurity.core.exceptions

import org.springframework.http.ProblemDetail

class EntityNotFoundException(
    problemDetail: ProblemDetail,
    throwable: Throwable? = null,
) : ProblemDetailException(problemDetail.detail ?: "Entity not found", throwable, problemDetail)
