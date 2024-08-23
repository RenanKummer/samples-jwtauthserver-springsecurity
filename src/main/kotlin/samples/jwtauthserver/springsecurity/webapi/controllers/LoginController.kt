package samples.jwtauthserver.springsecurity.webapi.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import samples.jwtauthserver.springsecurity.core.models.UserCredential
import samples.jwtauthserver.springsecurity.infrastructure.security.AuthenticationServiceAdapter
import samples.jwtauthserver.springsecurity.infrastructure.security.JwtService
import samples.jwtauthserver.springsecurity.webapi.dtos.LoginResponse
import samples.jwtauthserver.springsecurity.webapi.helpers.ok

@RestController
@RequestMapping("/api/login")
class LoginController(
    private val jwtService: JwtService,
    private val authenticationService: AuthenticationServiceAdapter
) {

    @PostMapping
    fun login(@RequestBody userCredential: UserCredential): ResponseEntity<LoginResponse> {
        val authenticatedUser = authenticationService.authenticate(userCredential)
        val jwtToken = jwtService.generateToken(authenticatedUser)

        return ok(LoginResponse(jwtToken, jwtService.getExpirationTime()))
    }
}
