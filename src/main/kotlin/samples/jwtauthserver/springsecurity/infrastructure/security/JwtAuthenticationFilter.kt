package samples.jwtauthserver.springsecurity.infrastructure.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (!authHeader.isBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader.extractJwtToken()
        val username = jwtService.extractUsername(jwtToken)

        val authentication = SecurityContextHolder.getContext().authentication

        if (username != null && authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                val springAuthToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                springAuthToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = springAuthToken
            }
        }

        filterChain.doFilter(request, response)
    }
}

private fun String?.isBearerToken() = this?.startsWith("Bearer ") == true

private fun String.extractJwtToken() = this.substring(7)
