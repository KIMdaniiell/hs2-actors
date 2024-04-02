package com.example.hs2actors.controller;

import com.example.hs2actors.model.dto.TeamDTO;
import com.example.hs2actors.model.dto.TeamManagerDTO;
import com.example.hs2actors.service.TeamManagerService;
import com.example.hs2actors.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.hs2actors.util.ValidationMessages.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/managers")
public class TeamManagerController {

    private final TeamManagerService teamManagerService;
    private final TeamService teamService;


    /* =====-----     Managers     -----===== */

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllTeamManagers(@RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = MSG_PAGE_NEGATIVE) int page,
                                                @RequestParam(value = "size", defaultValue = "5") @Min(value = 0, message = MSG_SIZE_NEGATIVE) @Max(value = 50, message = MSG_SIZE_TOO_BIG) int size
    ) {
        List<TeamManagerDTO> teamManagerDTOs = teamManagerService.findAll(page, size);
        return ResponseEntity.ok(teamManagerDTOs);
    }

    @GetMapping(value = "/{managerId}")
    public ResponseEntity<?> getTeamManagerById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId
    ) {
        TeamManagerDTO teamManagerDTO = teamManagerService.findById(managerId);
        return ResponseEntity.ok(teamManagerDTO);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createTeamManager(@Valid @RequestBody TeamManagerDTO managerDto) {
        TeamManagerDTO created = teamManagerService.create(managerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping(value = "/{managerId}")
    public ResponseEntity<?> deleteTeamManager(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId
    ) {
        teamManagerService.delete(managerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{managerId}")
    public ResponseEntity<?> updateTeamManager(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId,
            @Valid @RequestBody TeamManagerDTO managerDto
    ) {
        TeamManagerDTO teamManagerDTO = teamManagerService.update(managerId, managerDto);
        return ResponseEntity.ok(teamManagerDTO);
    }



    /* =====-----     Managers teams     -----===== */

    @PostMapping(value = "/{managerId}/teams")
    @PreAuthorize("hasRole('TEAM_MANAGER')")
    public ResponseEntity<?> createTeam(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId,
            @Valid @RequestBody TeamDTO teamDTO
    ) {
        teamDTO.setTeamManagerId(managerId);
        TeamDTO created = teamService.create(teamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping(value = "/{managerId}/teams/{teamId}")
    @PreAuthorize("hasRole('TEAM_MANAGER')")
    public ResponseEntity<?> deleteTeam(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId,
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        teamService.delete(managerId, teamId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{managerId}/teams/{teamId}")
    @PreAuthorize("hasRole('TEAM_MANAGER')")
    public ResponseEntity<?> updateTeam(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId,
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId,
            @Valid @RequestBody TeamDTO teamDTO
    ) {
        teamDTO.setTeamManagerId(managerId);
        TeamDTO updated = teamService.update(teamId, teamDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping(value = "/{managerId}/teams")
    public ResponseEntity<?> getAllTeams(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId
    ) {
        List<TeamDTO> teamDTOs = teamService.getAllTeamsByManager(managerId);
        return ResponseEntity.ok(teamDTOs);
    }

    @GetMapping(value = "/{managerId}/teams/{teamId}")
    public ResponseEntity<?> getTeamById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId,
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        TeamDTO teamDTO = teamService.getTeamByManager(managerId, teamId);
        return ResponseEntity.ok(teamDTO);
    }
}
