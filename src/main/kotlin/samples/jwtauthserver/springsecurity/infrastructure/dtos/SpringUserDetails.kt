package samples.jwtauthserver.springsecurity.infrastructure.dtos

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import samples.jwtauthserver.springsecurity.core.models.User

class SpringUserDetails(val user: User) : UserDetails {
    override fun getUsername() = user.username
    override fun getPassword() = user.password
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(GrantedAuthority({ "ROLE_ADMIN" }))
}
