package com.example.WouldILie.dtos.admin;

public record AdminQuestionDTO(
    Long id,
    String question,
    Boolean isTrue,
    Integer points,
    String userName,
    Boolean wasUsed,
    Boolean activeQuestion) {
};
