package com.example.WouldILie.dtos.admin;

public record ResultDTO(Long questionId, String question, String votedResult, Boolean actualAnswer, Long yesVotes,
    Long totalVotes) {
};
