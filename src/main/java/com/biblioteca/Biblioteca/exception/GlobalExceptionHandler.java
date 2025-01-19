package com.biblioteca.Biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.biblioteca.Biblioteca.dto.ErrorResponseDto;
import com.biblioteca.Biblioteca.exception.customExceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ISBNJaCadastradoException.class)
    public ResponseEntity<ErrorResponseDto> handleISBNJaCadastradoException(ISBNJaCadastradoException exception) {
        return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ErrorResponseDto(
            HttpStatus.CONFLICT.toString(),
            exception.getMessage()));
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<ErrorResponseDto> handleDadosInvalidosException(DadosInvalidosException exception) {
        return ResponseEntity
        .badRequest()
        .body(new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.toString(),
            exception.getMessage()));
    }

    @ExceptionHandler(EntidadeNaoEncontrada.class)
    public ResponseEntity<ErrorResponseDto> handleEntidadeNaoEncontrada(EntidadeNaoEncontrada exception) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDto(
            HttpStatus.NOT_FOUND.toString(),
            exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            exception.getMessage()));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErrorResponseDto> handleEstoqueInsuficienteException(EstoqueInsuficienteException exception) {
        return ResponseEntity
        .status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ErrorResponseDto(
            HttpStatus.UNPROCESSABLE_ENTITY.toString(),
            exception.getMessage()));
    }
}
