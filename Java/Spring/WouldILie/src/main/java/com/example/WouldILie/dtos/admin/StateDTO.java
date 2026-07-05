package com.example.WouldILie.dtos.admin;

public record StateDTO(String loggedUser, Long id, String question, String questionUser, Boolean alreadyVoted) {
};
