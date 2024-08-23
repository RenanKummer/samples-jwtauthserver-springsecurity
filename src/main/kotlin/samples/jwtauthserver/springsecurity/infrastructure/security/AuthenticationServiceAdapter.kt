package samples.jwtauthserver.springsecurity.infrastructure.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import samples.jwtauthserver.springsecurity.core.models.UserCredential

@Service
class AuthenticationServiceAdapter(private val authenticationManager: AuthenticationManager) {

    fun authenticate(userCredential: UserCredential): UserDetails {
        val springAuthToken =  UsernamePasswordAuthenticationToken(userCredential.username, userCredential.password)
        val authResult = authenticationManager.authenticate(springAuthToken)

        return authResult.principal as UserDetails
    }
}
