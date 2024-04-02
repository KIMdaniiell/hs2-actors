package com.example.hs2actors.controller.feign;

import com.example.hs2actors.model.dto.PlayerDTO;
import com.example.hs2actors.service.PlayerService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.hs2actors.util.ValidationMessages.MSG_ID_NEGATIVE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/feign/players")
public class PlayerFeignController {

    private final PlayerService playerService;

    @GetMapping()
    public ResponseEntity<?> getPlayerIdByUserId(
            @RequestParam(value = "userId") @Min(value = 0, message = MSG_ID_NEGATIVE) long userId
    ) {
        long playerId = playerService.findPlayerIdByUserId(userId);
        return ResponseEntity.ok(playerId);
    }

    @GetMapping(value = "/{playerId}")
    public ResponseEntity<?> getPlayerById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
    ) {
        PlayerDTO playerDTO = playerService.findById(playerId);
        return ResponseEntity.ok(playerDTO);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<?> deletePlayerById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
    ) {
        playerService.delete(playerId, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
