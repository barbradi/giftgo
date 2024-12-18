package com.giftgo.test.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.giftgo.test.domain.PersonalInfo
import com.giftgo.test.domain.PersonalInfoOutput
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID
import kotlin.io.path.createTempFile

@Service
class FileProcessAction(
    private val objectMapper: ObjectMapper,
    private val validateFile: ValidateFile
) {

    operator fun invoke(file: MultipartFile): File {
        validateFile.validate(file)
        return parseFileContent(file)
    }

    private fun parseFileContent(inputFile: MultipartFile): File {
        val tempOutputFile = createTempFile(prefix = inputFile.name, suffix = ".txt").toFile()

        log.info { "Processing file: ${inputFile.name}" }

        tempOutputFile.writer().use { writer ->
            writer.write("[") // Start JSON array

            inputFile.inputStream.bufferedReader().use { reader ->
                reader.lineSequence().forEachIndexed { index, line ->
                    val personalInfo = getOutputPersonalInfoJson(line)
                    if (index > 0) writer.append(",") // Add comma between JSON objects
                    writer.append(personalInfo)
                }
            }

            writer.append("]") // Close JSON array
        }

        log.info { "File processing completed: ${tempOutputFile.absolutePath}" }
        return tempOutputFile
    }

    private fun getOutputPersonalInfoJson(line: String) =
        getPersonalInfo(line).let { personalInfo ->
            objectMapper.writeValueAsString(
                PersonalInfoOutput(
                    name = personalInfo.name,
                    transport = personalInfo.transport,
                    topSpeed = personalInfo.topSpeed
                )
            )
        }

    private fun getPersonalInfo(line: String): PersonalInfo {
        val word = line.split("|")
        if (word.size != 7) {
            throw IllegalArgumentException("Invalid file format for line: $line")
        }
        return PersonalInfo(
            uuid = UUID.fromString(word[0]),
            id = word[1],
            name = word[2],
            likes = word[3],
            transport = word[4],
            avgSpeed = word[5].toDoubleOrNull()
                ?: throw IllegalArgumentException("Invalid avgSpeed for line: $line"),
            topSpeed = word[6].toDoubleOrNull()
                ?: throw IllegalArgumentException("Invalid topSpeed for line: $line")
        )
    }

    private companion object {
        val log = KotlinLogging.logger { }
    }
}
