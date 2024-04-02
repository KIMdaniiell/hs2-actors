package com.example.hs2actors.controller.feign;

import com.example.hs2actors.service.TeamManagerService;
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
@RequestMapping(path = "/feign/managers")
public class TeamManagerFeignController {

    private final TeamManagerService teamManagerService;

    @GetMapping()
    public ResponseEntity<?> findTeamManagerIdByUserId(
            @RequestParam(value = "userId") @Min(value = 0, message = MSG_ID_NEGATIVE) long userId
    ) {
        long managerId = teamManagerService.findTeamManagerIdByUserId(userId);
        return ResponseEntity.ok(managerId);
    }

    @DeleteMapping("/{managerId}")
    public ResponseEntity<?> deleteTeamManagerById(
            @PathVariable @Min(value = 0, message = MSG_ID_NEGATIVE) long managerId
    ) {
        teamManagerService.delete(managerId, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
