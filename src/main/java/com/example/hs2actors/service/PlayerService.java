package com.example.hs2actors.service;

import com.example.hs2actors.controller.exceptions.not_found.NotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.PlayerNotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.PlaygroundNotFoundException;
import com.example.hs2actors.model.dto.PlayerDTO;
import com.example.hs2actors.model.dto.RoleDTO;
import com.example.hs2actors.model.entity.Player;
import com.example.hs2actors.model.entity.Role;
import com.example.hs2actors.model.entity.User;
import com.example.hs2actors.repository.PlayerRepository;
import com.example.hs2actors.util.GeneralService;
import com.example.hs2actors.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerService extends GeneralService<Player, PlayerDTO> {
    private final Mapper<Player, PlayerDTO> mapper = new PlayerMapper();

    private final PlayerRepository playerRepository;
    private final UserService userService;
    private final TeamService teamService;



    @Override
    public PlayerDTO create(PlayerDTO dto) {
        long id = dto.getUserId();
        PlayerDTO createdDto = super.create(dto);
        userService.addRole(id, new RoleDTO(Role.ROLE_PLAYER), true);
        return createdDto;
    }

    @Override
    public void delete(long id) {
        User user = getEntityById(id).getUser();
        userService.removeRole(user.getUserId(), new RoleDTO(Role.ROLE_PLAYER), false);
        super.delete(id);
    }

    @Transactional
    public PlayerDTO update(long id, PlayerDTO dto) {
        Player found = getEntityById(id);
        Player updated = mapper.dtoToEntity(dto);
        updated.setPlayerId(found.getPlayerId());
        updated.setUser(found.getUser());
        updated.setTeams(found.getTeams());
        playerRepository.save(updated);
        return mapper.entityToDto(updated);
    }

    public void joinTeam(long playerId, long teamId) {
        teamService.addMember(teamId, playerId, null);
    }

    public void leaveTeam(long playerId, long teamId) {
        teamService.removeMember(teamId, playerId, null);
    }

    public long findPlayerIdByUser(long userId) {
        return playerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new PlayerNotFoundException("user id = " + userId)).getPlayerId();
    }

    @Override
    protected NotFoundException getNotFoundIdException(long id) {
        return new PlaygroundNotFoundException("id = " + id);
    }

    @Override
    protected Mapper<Player, PlayerDTO> getMapper() {
        return mapper;
    }

    @Override
    protected JpaRepository<Player, Long> getRepository() {
        return playerRepository;
    }

    class PlayerMapper implements Mapper<Player, PlayerDTO> {

        @Override
        public PlayerDTO entityToDto(Player entity) {
            return new PlayerDTO(
                    entity.getPlayerId(),
                    entity.getUser().getUserId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getHeight(),
                    entity.getWeight(),
                    entity.getGender()
            );
        }

        @Override
        public Player dtoToEntity(PlayerDTO dto) {
            User user = userService.getEntityById(dto.getUserId());

            return new Player(
                    null,
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getAge(),
                    dto.getHeightCm(),
                    dto.getWeightKg(),
                    dto.getGender(),
                    user,
                    null
            );
        }
    }

}
