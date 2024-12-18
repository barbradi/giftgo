package com.giftgo.test.infrastructure.adapters.inbound.http.controller

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ControllerExceptionHandler  : ResponseEntityExceptionHandler() {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ErrorResponse =
        ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.localizedMessage)
            .also { log.error(ex) { ex.localizedMessage }  }

    @ExceptionHandler
    fun handleOtherErrors(ex: Throwable): ErrorResponse =
        ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.localizedMessage)
            .also { log.error(ex) { ex.localizedMessage }  }

    companion object {
        val log = KotlinLogging.logger { }
    }
}
