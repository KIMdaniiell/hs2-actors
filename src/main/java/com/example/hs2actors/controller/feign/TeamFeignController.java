package com.example.hs2actors.controller.feign;

import com.example.hs2actors.model.dto.TeamDTO;
import com.example.hs2actors.service.TeamService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.hs2actors.util.ValidationMessages.MSG_ID_NEGATIVE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/feign/teams")
public class TeamFeignController {

    private final TeamService teamService;

    @GetMapping(value = "/{teamId}")
    public ResponseEntity<?> getTeamById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        TeamDTO teamDTO = teamService.findById(teamId);
        return ResponseEntity.ok(teamDTO);
    }

}
