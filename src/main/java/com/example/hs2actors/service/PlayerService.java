package com.example.hs2actors.service;

import com.example.hs2actors.controller.exceptions.not_found.NotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.PlayerNotFoundException;
import com.example.hs2actors.model.dto.PlayerDTO;
import com.example.hs2actors.model.dto.RoleDTO;
import com.example.hs2actors.model.entity.Player;
import com.example.hs2actors.model.entity.Role;
import com.example.hs2actors.repository.PlayerRepository;
import com.example.hs2actors.service.feign.AuthClientAddRoleWrapper;
import com.example.hs2actors.service.feign.AuthClientRemoveRoleWrapper;
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

    private final PlayerRepository repository;
    private final TeamService teamService;
    private final AuthClientAddRoleWrapper authClientAddRoleWrapper;
    private final AuthClientRemoveRoleWrapper authClientRemoveRoleWrapper;


    @Override
    public PlayerDTO create(PlayerDTO dto) {
        // Шаг 1: ассоциированному с Player`ому User'у назначается роль ROLE_PLAYER
        authClientAddRoleWrapper.addRole(
                dto.getUserId(),
                new RoleDTO(Role.ROLE_PLAYER));

        // Шаг 2: при успешном назначении роли Player сохраняется
        PlayerDTO createdDto = super.create(dto);

        return createdDto;
    }

    @Override
    public void delete(long id) {
        // Шаг 1: с ассоциированного с Player`ом User'а снять роль ROLE_PLAYER
        authClientRemoveRoleWrapper.removeRole(
                getEntityById(id).getUserId(),
                new RoleDTO(Role.ROLE_PLAYER));
        // Шаг 2: при успешном удалении роли Player удаляется
        super.delete(id);
    }

    @Transactional
    public PlayerDTO update(long id, PlayerDTO dto) {
        Player player = getEntityById(id);

        // TODO если поле null, то наверное не нужно обновлять?
        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setAge(dto.getAge());
        player.setHeight(dto.getHeightCm());
        player.setWeight(dto.getWeightKg());
        player.setGender(dto.getGender());
        // userId - не обновляется
        // teams - не обновляется
        repository.save(player);

        return mapper.entityToDto(player);
    }

    public void joinTeam(long playerId, long teamId) {
        teamService.addMember(teamId, playerId, null);
    }

    public void leaveTeam(long playerId, long teamId) {
        teamService.removeMember(teamId, playerId, null);
    }

    public long findPlayerIdByUserId(long userId) {
        return repository.findPlayerByUserId(userId)
                .orElseThrow(() -> new PlayerNotFoundException("user id = " + userId)).getPlayerId();
    }


    @Override
    protected NotFoundException getNotFoundIdException(long id) {
        return new PlayerNotFoundException("id = " + id);
    }

    @Override
    protected Mapper<Player, PlayerDTO> getMapper() {
        return mapper;
    }

    @Override
    protected JpaRepository<Player, Long> getRepository() {
        return repository;
    }

    class PlayerMapper implements Mapper<Player, PlayerDTO> {

        @Override
        public PlayerDTO entityToDto(Player entity) {
            return new PlayerDTO(
                    entity.getPlayerId(),
                    entity.getUserId(),
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
            return new Player(
                    null,
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getAge(),
                    dto.getHeightCm(),
                    dto.getWeightKg(),
                    dto.getGender(),
                    dto.getUserId(),
                    null
            );
        }
    }

}
