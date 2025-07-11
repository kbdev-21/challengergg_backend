package com.example.challengergg.exception

import org.springframework.http.HttpStatus

class CustomException(
    val statusCode: HttpStatus,
    message: String,
): RuntimeException(message) {

}