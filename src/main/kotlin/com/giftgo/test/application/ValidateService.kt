package com.giftgo.test.application

import mu.KotlinLogging
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

class ValidateService : com.giftgo.test.application.ValidateFile {

    /*
    It's not clear what to validate, so for simplicity, I am just validating UUID, avg speed and top speed values
    */
    override fun validate(file: MultipartFile) {
        com.giftgo.test.application.ValidateService.Companion.log.info { "validating file" }
        file.inputStream.bufferedReader().use { reader ->
            reader.forEachLine { line ->
                val word = line.split("|")
                if (word.size != 7) {
                    throw IllegalArgumentException("Invalid file format for line: $line")
                }

                try {
                    UUID.fromString(word[0])
                } catch (ex: IllegalArgumentException) {
                    throw IllegalArgumentException("Invalid uuid for line: $line")
                }
                word[5].toDoubleOrNull() ?: throw IllegalArgumentException("Invalid avgSpeed for line: $line")
                word[6].toDoubleOrNull() ?: throw IllegalArgumentException("Invalid topSpeed for line: $line")
            }
        }
    }

    private companion object {
        val log = KotlinLogging.logger { }
    }
}