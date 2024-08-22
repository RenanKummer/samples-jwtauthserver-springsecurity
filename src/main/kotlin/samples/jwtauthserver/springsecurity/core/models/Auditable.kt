package samples.jwtauthserver.springsecurity.core.models

import java.time.OffsetDateTime

interface Auditable {
    val createdBy: String?
    val createdDateTime: OffsetDateTime?
}

interface AuditableUpdate : Auditable {
    val updatedBy: String?
    val updatedDateTime: OffsetDateTime?
}
