package com.example.WouldILie.dtos.votes;

public record AddVoteDTO(
    Long questionId,
    String userName,
    String teamName,
    Boolean isTrue) {
}
