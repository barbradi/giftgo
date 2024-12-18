package com.giftgo.test.infrastructure.adapters.inbound.http.controller

import com.giftgo.test.application.FileProcessAction
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.reactive.function.BodyInserters


@WebMvcTest(controllers = [FileController::class])
class FileControllerTest @Autowired constructor(
    mockMvc: MockMvc
) {

    private val webTestClient : WebTestClient = MockMvcWebTestClient.bindTo(mockMvc).build()

    @MockkBean
    private lateinit var fileProcessAction: FileProcessAction

    @Test
    fun greetingShouldReturnMessageFromService() {

        // Given
        val expectedOutputFile = ClassPathResource("expected_output_file.json")
        every { fileProcessAction(any()) } returns expectedOutputFile.file

        // When
        val resultJson = webTestClient.post()
            .uri("/processFile")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(createMultipartFile())
            .exchange()
            .expectStatus().isCreated
            .expectBody(String::class.java)
            .returnResult()
            .responseBody.orEmpty()

        // Then
        assertThatJson(resultJson).isEqualTo(readFile(expectedOutputFile))
    }

    private fun readFile(file: ClassPathResource) =
        file.inputStream.bufferedReader().use { it.readText() }

    private fun createMultipartFile(): BodyInserters.MultipartInserter =
        BodyInserters.fromMultipartData(
            MultipartBodyBuilder().apply {
                part("file", "it doesn't matter, the service class is mocked".toByteArray())
                    .filename("filename11")
                    .contentType(MediaType.TEXT_PLAIN)
            }.build()
        )

}