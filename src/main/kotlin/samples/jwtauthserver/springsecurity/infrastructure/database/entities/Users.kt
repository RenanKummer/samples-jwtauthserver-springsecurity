package samples.jwtauthserver.springsecurity.infrastructure.database.entities

import org.jetbrains.exposed.dao.id.ULongIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object Users : ULongIdTable("users"), AuditableUpdates {
    val username = text("username").uniqueIndex()
    val password = text("password")
    val passwordHashAlgorithm = text("password_hash_algorithm")
    val passwordExpirationDate = date("password_expiration_date")
    val email = text("email").uniqueIndex()
    val firstName = text("first_name")
    val lastName = text("last_name")
    val phoneNumber = text("phone_number")
    val companyName = text("company")
    override val createdBy = text("created_by")
    override val createdDateTime = timestampWithTimeZone("created_time")
    override val updatedBy = text("updated_by").nullable()
    override val updatedDateTime = timestampWithTimeZone("updated_time").nullable()
}
