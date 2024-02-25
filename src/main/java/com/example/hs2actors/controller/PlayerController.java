package com.example.hs2actors.controller;

import com.example.hs2actors.model.dto.PlayerDTO;
import com.example.hs2actors.model.dto.TeamDTO;
import com.example.hs2actors.service.PlayerService;
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
@RequestMapping(path = "/players")
public class PlayerController {

    private final PlayerService playerService;
    private final TeamService teamService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllPlayers(
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = MSG_PAGE_NEGATIVE) int page,
            @RequestParam(value = "size", defaultValue = "5") @Min(value = 0, message = MSG_SIZE_NEGATIVE) @Max(value = 50, message = MSG_SIZE_TOO_BIG) int size
    ) {
        List<PlayerDTO> playerDTOs = playerService.findAll(page, size);
        return ResponseEntity.ok(playerDTOs);
    }

    @GetMapping(value = "/{playerId}")
    public ResponseEntity<?> getPlayerById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
    ) {
        PlayerDTO playerDTO = playerService.findById(playerId);
        return ResponseEntity.ok(playerDTO);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createPlayer(@Valid @RequestBody PlayerDTO playerDto) {
        PlayerDTO created = playerService.create(playerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //    supervisor role
    @PutMapping("/{playerId}")
    public ResponseEntity<?> updatePlayer(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId,
            @Valid @RequestBody PlayerDTO playerDto
    ) {
        PlayerDTO updated = playerService.update(playerId, playerDto);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<?> deletePlayer(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
    ) {
        playerService.delete(playerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{playerId}/teams")
    public ResponseEntity<?> getTeamsOfPlayer(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
    ) {
        List<TeamDTO> teams = teamService.getTeamsByPlayer(playerId);
        return ResponseEntity.ok(teams);
    }

    @PostMapping("/{playerId}/teams/{teamId}")
    public ResponseEntity<?> joinTeam(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId,
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        playerService.joinTeam(playerId, teamId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{playerId}/teams/{teamId}")
    public ResponseEntity<?> leaveTeam(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId,
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long teamId
    ) {
        playerService.leaveTeam(playerId, teamId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/{playerId}/bookings")
//    public ResponseEntity<?> getBookings(
//            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
//    ) {
//        List<BookingDTO> bookingDTOS = bookingService.getBookingsByPlayer(playerId);
//        return ResponseEntity.ok(bookingDTOS);
//    }

}
