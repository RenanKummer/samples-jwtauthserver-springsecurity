package samples.jwtauthserver.springsecurity.infrastructure.database

import org.jetbrains.exposed.sql.SchemaUtils
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import samples.jwtauthserver.springsecurity.core.models.User
import samples.jwtauthserver.springsecurity.core.repositories.UserRepository
import samples.jwtauthserver.springsecurity.infrastructure.database.entities.Users

@Component
@Transactional
class DatabaseSchemaInitializer(
    private val userRepository: UserRepository
) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        initializeSchemas()
        initializeData()
    }

    private fun initializeSchemas() {
        logger.info("Local profile only: initializing database schema...")
        SchemaUtils.create(Users)
        logger.info("Local profile only: database schema initialized")
    }

    private fun initializeData() {
        logger.info("Local profile only: adding test data...")


        userRepository.create(User(
            username = "johndoe",
            password = "password",
            email = "john.doe@company.com",
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "5551999999999",
            companyName = "Company",
            createdBy = "system"
        ))
        logger.info("Local profile only: test data added")
    }

    companion object {
        val logger = LoggerFactory.getLogger(DatabaseSchemaInitializer::class.java)!!
    }
}
