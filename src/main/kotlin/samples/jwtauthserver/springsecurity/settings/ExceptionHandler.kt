package samples.jwtauthserver.springsecurity.settings

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import samples.jwtauthserver.springsecurity.core.exceptions.ProblemDetailException

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProblemDetailException::class)
    fun handleProblemDetailsException(exception: ProblemDetailException, request: WebRequest): ResponseEntity<in Any>? {
        val status = HttpStatus.valueOf(exception.problemDetail!!.status)
        return super.handleExceptionInternal(exception, exception.problemDetail, HttpHeaders(), status, request)
    }
}
