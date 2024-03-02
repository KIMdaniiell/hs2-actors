package com.example.hs2actors.controller;

import com.example.hs2actors.model.dto.TeamDTO;
import com.example.hs2actors.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.hs2actors.util.ValidationMessages.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/teams")
public class TeamController {

    private final TeamService teamService;
//    private final BookingService bookingService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllTeams(@RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = MSG_PAGE_NEGATIVE) int page,
                                         @RequestParam(value = "size", defaultValue = "5") @Min(value = 0, message = MSG_SIZE_NEGATIVE) @Max(value = 50, message = MSG_SIZE_TOO_BIG) int size
    ) {
        List<TeamDTO> teamDTOs = teamService.findAll(page, size);
        return ResponseEntity.ok(teamDTOs);
    }

    @GetMapping(value = "/{teamId}")
    public ResponseEntity<?> getTeamById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        TeamDTO teamDTO = teamService.findById(teamId);
        return ResponseEntity.ok(teamDTO);
    }

    //only supervisor
    @DeleteMapping(value = "/{teamId}")
    public ResponseEntity<?> deleteTeam(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        teamService.delete(teamId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //only supervisor
    @PutMapping(value = "/{teamId}")
    public ResponseEntity<?> updateTeam(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId,
            @Valid @RequestBody TeamDTO teamDTO
    ) {
        TeamDTO updated = teamService.update(teamId, teamDTO);
        return ResponseEntity.ok(updated);
    }

//    @GetMapping("/{teamId}/bookings")
//    public ResponseEntity<?> getBookings(
//            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
//    ) {
//        List<BookingDTO> bookingDTOS = bookingService.getBookingsByTeam(teamId);
//        return ResponseEntity.ok(bookingDTOS);
//    }

}
