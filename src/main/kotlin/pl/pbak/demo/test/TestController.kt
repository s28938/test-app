package pl.pbak.demo.test

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/test")
class TestController {

    val logger: Logger = LoggerFactory.getLogger(TestController::class.java)

    @GetMapping(value = ["/date-time"], produces = ["application/json"])
    @Operation(
       operationId = "date-time",
        summary = "Get current date and time",
        tags = ["test"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "new UUID was generated"),
    ])
    fun dateTime(): ResponseEntity<String> {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        logger.info("Current date and time: $formattedDateTime")
        return ResponseEntity.status(HttpStatus.CREATED).body(formattedDateTime)
   }

    @GetMapping(value = ["/uuid"], produces = ["application/json"])
    @Operation(
            operationId = "generateRandomUuid",
            summary = "Generates random UUID",
            tags = ["test"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "new UUID was generated"),
    ])
    fun generateUuid(): ResponseEntity<UUID> {
        val uuid = UUID.randomUUID()
        logger.info("Generate uuid: $uuid")
        return ResponseEntity.status(HttpStatus.CREATED).body(uuid)
    }

    @GetMapping(value = ["/uuid-error"], produces = ["application/json"])
    @Operation(
            operationId = "generateRandomUuidWithError",
            summary = "Generates random UUID with error",
            tags = ["test"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "new UUID was generated"),
    ])
    fun generateUuidWithError(): ResponseEntity<UUID> {
        val uuid = UUID.randomUUID()
        logger.info("Generating uuid: $uuid")

        try {
            throw NotImplementedError("test error")
        } catch (e: Error) {
            logger.error("some error message", e)
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.of(Optional.of(uuid))
    }
}
