package samples.jwtauthserver.springsecurity.core.models

import java.time.LocalDate
import java.time.OffsetDateTime

open class User(
    val id: ULong? = null,
    open val username: String,
    open val password: String,
    val passwordHashAlgorithm: String? = null,
    val passwordExpirationDate: LocalDate? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val companyName: String,
    override val createdBy: String,
    override val createdDateTime: OffsetDateTime = OffsetDateTime.now(),
    override val updatedBy: String? = null,
    override val updatedDateTime: OffsetDateTime? = null
) : AuditableUpdate

data class MutableUser(
    var id: ULong? = null,
    var username: String? = null,
    var password: String? = null,
    var passwordHashAlgorithm: String? = null,
    var passwordExpirationDate: LocalDate? = null,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var companyName: String? = null,
    override var createdBy: String? = null,
    override var createdDateTime: OffsetDateTime? = null,
    override var updatedBy: String? = null,
    override var updatedDateTime: OffsetDateTime? = null
) : AuditableUpdate
