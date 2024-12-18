package com.giftgo.test.infrastructure.config

import com.giftgo.test.application.NoOpValidateService
import com.giftgo.test.application.ValidateFile
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ValidationConfiguration {

    @Bean
    @ConditionalOnProperty(
        name = ["file.validation"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun validateFile(): ValidateFile {
        return com.giftgo.test.application.ValidateService()
    }

    @Bean
    @ConditionalOnProperty(
        name = ["file.validation"],
        havingValue = "false",
    )
    fun validateFileNoOp(): ValidateFile {
        return NoOpValidateService()
    }
}