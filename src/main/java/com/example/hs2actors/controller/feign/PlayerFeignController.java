package com.example.hs2actors.controller.feign;

import com.example.hs2actors.model.dto.PlayerDTO;
import com.example.hs2actors.service.PlayerService;
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
@RequestMapping(path = "/feign/players")
public class PlayerFeignController {

    private final PlayerService playerService;

    @GetMapping(value = "/{playerId}")
    public ResponseEntity<?> getPlayerById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long playerId
    ) {
        PlayerDTO playerDTO = playerService.findById(playerId);
        return ResponseEntity.ok(playerDTO);
    }
}
