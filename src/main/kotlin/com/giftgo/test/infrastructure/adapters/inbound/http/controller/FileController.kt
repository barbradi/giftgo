package com.giftgo.test.infrastructure.adapters.inbound.http.controller

import com.giftgo.test.application.FileProcessAction
import mu.KotlinLogging
import mu.withLoggingContext
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files


@RestController
class FileController (
    private val fileProcessAction : FileProcessAction
) {

    @PostMapping(value = ["/processFile"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun processFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Resource> {
        withLoggingContext("fileName" to file.originalFilename) {
            log.info { "processing file started" }
            fileProcessAction(file).let { outputFile ->
                return ResponseEntity.created(outputFile.toURI())
                    .header(CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(outputFile.length())
                    .body(ByteArrayResource(Files.readAllBytes(outputFile.toPath())))
            }
        }
    }

    private companion object {
        val log = KotlinLogging.logger { }
    }
}