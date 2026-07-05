package com.example.WouldILie.dtos.admin;

import java.util.List;

public record AdminStateDTO(
    List<AdminUserDTO> users,
    List<AdminTeamDTO> teams,
    List<AdminQuestionDTO> questions) {
};
