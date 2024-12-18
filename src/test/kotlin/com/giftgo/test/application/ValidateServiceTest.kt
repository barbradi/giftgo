package com.giftgo.test.application

import assertk.assertFailure
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.ResourceUtils


class ValidateServiceTest {

    private val validateService = com.giftgo.test.application.ValidateService()

    @Test
    fun `should fail parsing input file when content has invalid top speed value`() {
        // Given
        val inputFile = getInputFile("content_bad_format_invalid_top_speed_value.txt")

        // When
        // Then
        assertFailure { validateService.validate(inputFile) }
            .isInstanceOf(IllegalArgumentException::class)
            .hasMessage("Invalid topSpeed for line: 1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|fff")
    }

    @Test
    fun `should fail parsing input file when content has invalid avg speed value`() {
        // Given
        val inputFile = getInputFile("content_bad_format_invalid_avg_speed_value.txt")

        // When
        // Then
        assertFailure { validateService.validate(inputFile) }
            .isInstanceOf(IllegalArgumentException::class)
            .hasMessage("Invalid avgSpeed for line: 1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|ffff|10.5")
    }

    @Test
    fun `should fail parsing input file when content has invalid uuid value`() {
        // Given
        val inputFile = getInputFile("content_bad_format_invalid_uuid_value.txt")

        // When
        // Then
        assertFailure { validateService.validate(inputFile) }
            .isInstanceOf(IllegalArgumentException::class)
            .hasMessage("Invalid uuid for line: bad|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1")
    }

    @Test
    fun `should fail parsing input file when content doesn't have 7 words`() {
        // Given
        val inputFile = getInputFile("content_bad_format_invalid_word_size.txt")

        // When
        // Then
        assertFailure { validateService.validate(inputFile) }
            .isInstanceOf(IllegalArgumentException::class)
            .hasMessage("Invalid file format for line: 18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1|bad")
    }

    private fun getInputFile(filename: String): MockMultipartFile {
        val inputFile = MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            ResourceUtils.getFile("classpath:$filename").readBytes()
        )
        return inputFile
    }
}