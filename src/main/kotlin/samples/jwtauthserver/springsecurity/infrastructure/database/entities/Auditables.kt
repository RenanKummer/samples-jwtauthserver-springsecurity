package samples.jwtauthserver.springsecurity.infrastructure.database.entities

import org.jetbrains.exposed.sql.Column
import java.time.OffsetDateTime

interface Auditables {
    val createdBy: Column<String>
    val createdDateTime: Column<OffsetDateTime>
}

interface AuditableUpdates : Auditables {
    val updatedBy: Column<String?>
    val updatedDateTime: Column<OffsetDateTime?>
}
