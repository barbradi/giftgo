package com.giftgo.test.integration

import net.javacrumbs.jsonunit.assertj.assertThatJson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@TestPropertySource(properties = ["file.validation=true"]) //TODO more testing between on/off ...
class FileParsingIntegrationTest @Autowired constructor(
    private val webTestClient: WebTestClient
) : IntegrationTestSetup() {

    @Test
    fun `should parse input file when content is valid`() {

        // Given
        val inputFile = createMultipartFileFromFile("content_ok.txt")

        // When
        val resultJson = webTestClient.post()
            .uri("/processFile")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(inputFile)
            .exchange()
            .expectStatus().isCreated
            .expectBody(String::class.java)
            .returnResult()
            .responseBody.orEmpty()

        // Then
        assertThatJson(resultJson).isEqualTo(
            """
            [
                {
                  "name": "John Smith",
                  "topSpeed": 12.1,
                  "transport": "Rides A Bike"
                },
                {
                  "name": "Mike Smith",
                  "topSpeed": 95.5,
                  "transport": "Drives an SUV"
                },
                {
                  "name": "Jenny Walters",
                  "topSpeed": 15.3,
                  "transport": "Rides A Scooter"
                }
            ]
            """
                .trimIndent()
        )
    }

    @Test
    fun `should fail parsing input file when content is invalid`() {
        // Given
        val inputFile = createMultipartFileFromFile("content_bad_format_invalid_avg_speed_value.txt")

        // When
        val resultJson = webTestClient.post()
            .uri("/processFile")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(inputFile)
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody.orEmpty()

        // Then
        assertThatJson(resultJson).isEqualTo(
            """
            {
              "detail": "Invalid avgSpeed for line: 1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|ffff|10.5",
              "instance": "/processFile",
              "status": 400,
              "title": "Bad Request",
              "type": "about:blank"
            }
            """
                .trimIndent()
        )
    }

    private fun createMultipartFileFromFile(fileName: String): BodyInserters.MultipartInserter =
        BodyInserters.fromMultipartData(
            MultipartBodyBuilder().apply {
                part("file", ClassPathResource(fileName).file.readBytes())
                    .filename("filename11")
                    .contentType(MediaType.TEXT_PLAIN)
            }.build()
        )
}