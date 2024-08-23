package samples.jwtauthserver.springsecurity.webapi.dtos

data class LoginResponse(val token: String, val expiresIn: Long)
