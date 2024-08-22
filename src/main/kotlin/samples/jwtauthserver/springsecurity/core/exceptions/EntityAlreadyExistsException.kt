package samples.jwtauthserver.springsecurity.core.exceptions

import org.springframework.http.ProblemDetail

class EntityAlreadyExistsException(
    problemDetail: ProblemDetail,
    throwable: Throwable? = null,
) : ProblemDetailException(problemDetail.detail ?: "Entity already exists", throwable, problemDetail)
