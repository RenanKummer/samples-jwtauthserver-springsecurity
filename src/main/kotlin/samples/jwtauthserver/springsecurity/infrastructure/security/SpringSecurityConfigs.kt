package samples.jwtauthserver.springsecurity.infrastructure.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
class SpringSecurityConfigs {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .httpBasic({ config -> config.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) })
            .csrf { config -> config.disable() }
            .authorizeHttpRequests { config -> config
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            }

        return httpSecurity.build()
    }

    @Bean
    fun bcryptPasswordEncoder(@Value("\${security.bcrypt-strength}") strength: Int) = BCryptPasswordEncoder(strength)
}
