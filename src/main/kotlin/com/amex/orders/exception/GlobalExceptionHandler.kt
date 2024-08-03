package com.amex.orders.exception

import com.amex.orders.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseBody
    fun handleProductNotFoundException(e: ProductNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(OrderNotFoundException::class)
    @ResponseBody
    fun handleOrderNotFoundException(e: OrderNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.message), HttpStatus.NOT_FOUND)
    }
}