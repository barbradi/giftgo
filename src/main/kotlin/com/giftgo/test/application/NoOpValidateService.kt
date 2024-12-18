package com.giftgo.test.application

import mu.KotlinLogging
import org.springframework.web.multipart.MultipartFile

class NoOpValidateService : ValidateFile {

    override fun validate(file: MultipartFile) {
        log.info { "validating is disabled" }
    }

    private companion object {
        val log = KotlinLogging.logger { }
    }
}