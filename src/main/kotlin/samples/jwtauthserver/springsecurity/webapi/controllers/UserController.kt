package samples.jwtauthserver.springsecurity.webapi.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import samples.jwtauthserver.springsecurity.core.extensions.logInfo
import samples.jwtauthserver.springsecurity.core.models.MutableUser
import samples.jwtauthserver.springsecurity.core.models.User
import samples.jwtauthserver.springsecurity.core.repositories.UserFilterProperties
import samples.jwtauthserver.springsecurity.core.repositories.UserRepository
import samples.jwtauthserver.springsecurity.webapi.helpers.badRequest
import samples.jwtauthserver.springsecurity.webapi.helpers.created
import samples.jwtauthserver.springsecurity.webapi.helpers.noContent
import samples.jwtauthserver.springsecurity.webapi.helpers.notFound
import samples.jwtauthserver.springsecurity.webapi.helpers.ok
import java.net.URI

@RestController
@RequestMapping("/api/users", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(private val userRepository: UserRepository) {

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<*> {
        logger.logInfo("Received request to create user", "user" to user)
        val createdUser = userRepository.create(user)

        if (createdUser == null)
        {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "User Already Exists")
            problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#name-400-bad-request")
            problemDetail.title = "Cannot create user"

            return badRequest(problemDetail)
        }

        return created(createdUser, "/api/users/${createdUser.username}")
    }

    @PutMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateUser(
        @PathVariable id: String,
        @RequestParam(defaultValue = UserFilterProperties.USERNAME_VALUE) filterBy: String,
        @RequestBody data: MutableUser
    ): ResponseEntity<*> {
        logger.logInfo("Received request to update user", "id" to id, "filterBy" to filterBy, "user" to data)
        val filterProperty = UserFilterProperties.ofName(filterBy)
        if (filterProperty == null) {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request")
            problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#name-400-bad-request")
            problemDetail.title = "Cannot update user"
            problemDetail.properties = mapOf(
                "errors" to mapOf("filterBy" to "optional: if present must be 'username' or 'email'"))

            return badRequest(problemDetail)
        }

        val isUpdated = userRepository.update(id, filterProperty, data)
        if (!isUpdated) {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "User not found")
            problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#name-404-not-found")
            problemDetail.title = "Cannot update user"

            return notFound(problemDetail)
        }

        return noContent()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: String,
        @RequestParam(defaultValue = UserFilterProperties.USERNAME_VALUE) filterBy: String
    ): ResponseEntity<*> {
        logger.logInfo("Received request to delete user", "id" to id, "filterBy" to filterBy)
        val filterProperty = UserFilterProperties.ofName(filterBy)
        if (filterProperty == null) {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request")
            problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#name-400-bad-request")
            problemDetail.title = "Cannot delete user"

            return badRequest(problemDetail)
        }

        val isDeleted = userRepository.delete(id, filterProperty)
        if (!isDeleted) {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "User not found")
            problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#name-404-not-found")
            problemDetail.title = "Cannot delete user"

            return notFound(problemDetail)
        }

        return noContent()
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String, @RequestParam(defaultValue = "username") filterBy: String): ResponseEntity<*> {
        logger.logInfo("Received request to fetch user", "id" to id, "filterBy" to filterBy)
        val filterProperty = when (filterBy) {
            "username" -> UserFilterProperties.USERNAME
            "email" -> UserFilterProperties.EMAIL
            else -> {
                val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid Request")
                problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#name-400-bad-request")
                problemDetail.title = "Cannot find user"
                problemDetail.properties = mapOf(
                    "errors" to mapOf("filterBy" to "optional: if present must be 'username' or 'email'"))

                return badRequest(problemDetail)
            }
        }

        val user = userRepository.findOne(id, filterProperty)
        if (user == null)
        {
            val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "User not found")
            problemDetail.type = URI.create("https://datatracker.ietf.org/doc/html/rfc9110#name-404-not-found")
            problemDetail.title = "Cannot find user"

            return notFound(problemDetail)
        }

        return ok(user)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        logger.logInfo("Received request to get all users")
        val users = userRepository.findAll()
        return ok(users)
    }

    companion object {
        val logger = LoggerFactory.getLogger(UserController::class.java)!!
    }
}
