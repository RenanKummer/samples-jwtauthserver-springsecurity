package samples.jwtauthserver.springsecurity.infrastructure.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import samples.jwtauthserver.springsecurity.core.extensions.logInfo
import samples.jwtauthserver.springsecurity.core.extensions.logWarn
import samples.jwtauthserver.springsecurity.core.repositories.UserFilterProperties
import samples.jwtauthserver.springsecurity.core.repositories.UserRepository
import samples.jwtauthserver.springsecurity.infrastructure.dtos.SpringUserDetails

@Service
class SpringUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username == null) {
            logger.logWarn("Cannot load user with null username")
            return null
        }

        logger.logInfo("Fetching user by username", "username" to username)
        val user = userRepository.findOne(username, UserFilterProperties.USERNAME)
        if (user == null) {
            logger.logWarn("User not found", "username" to username)
            return null
        }

        logger.logInfo("User found by username", "user" to user)
        return SpringUserDetails(user)
    }

    companion object {
        val logger = LoggerFactory.getLogger(SpringUserDetailsService::class.java)
    }
}
