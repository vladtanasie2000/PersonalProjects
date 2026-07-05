package com.example.WouldILie.dtos.admin;

import java.util.List;

public record AdminTeamDTO(
    Long id,
    String teamName,
    Integer points,
    List<String> users) {
};
