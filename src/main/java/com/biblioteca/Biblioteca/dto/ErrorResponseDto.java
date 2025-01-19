package com.biblioteca.Biblioteca.dto;

public class ErrorResponseDto {
    private String statusCode;
    private String errorMessage;

    public ErrorResponseDto() {
    }
    
    public ErrorResponseDto(String statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
