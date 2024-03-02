package com.example.hs2actors.service;

import com.example.hs2actors.controller.exceptions.not_found.NotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.TeamManagerNotFoundException;
import com.example.hs2actors.model.dto.RoleDTO;
import com.example.hs2actors.model.dto.TeamManagerDTO;
import com.example.hs2actors.model.entity.Role;
import com.example.hs2actors.model.entity.TeamManager;
import com.example.hs2actors.repository.TeamManagerRepository;
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
public class TeamManagerService extends GeneralService<TeamManager, TeamManagerDTO> {
    private final Mapper<TeamManager, TeamManagerDTO> mapper = new TeamManagerMapper();

    private final TeamManagerRepository repository;
    private final AuthClientAddRoleWrapper authClientAddRoleWrapper;
    private final AuthClientRemoveRoleWrapper authClientRemoveRoleWrapper;


    @Transactional
    @Override
    public TeamManagerDTO create(TeamManagerDTO dto) {
        // Шаг 1: ассоциированному с TeamManager`ом User'у назначается роль ROLE_TEAM_MANAGER
        authClientAddRoleWrapper.addRole(
                dto.getUserId(),
                new RoleDTO(Role.ROLE_TEAM_MANAGER));

        // Шаг 2: при успешном назначении роли Player сохраняется
        TeamManagerDTO createdDto = super.create(dto);

        return createdDto;
    }

    @Transactional
    @Override
    public void delete(long id) {
        // Шаг 1: с ассоциированного с TeamManager`ом User'а снять роль ROLE_TEAM_MANAGER
        authClientRemoveRoleWrapper.removeRole(
                getEntityById(id).getUserId(),
                new RoleDTO(Role.ROLE_TEAM_MANAGER));
        // Шаг 2: при успешном удалении роли TeamManager удаляется
        super.delete(id);
    }

    @Transactional
    public TeamManagerDTO update(long id, TeamManagerDTO dto) {
        TeamManager manager = getEntityById(id);

        // TODO если поле null, то наверное не нужно обновлять?
        manager.setFirstName(dto.getFirstName());
        manager.setLastName(dto.getLastName());
        manager.setPhone(dto.getPhone());
        manager.setEmail(dto.getEmail());
        // userId - не обновляется
        // teams - не обновляется
        repository.save(manager);

        return mapper.entityToDto(manager);
    }

    public long findTeamManagerIdByUserId(long userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new TeamManagerNotFoundException("user id = " + userId))
                .getTeamManagerId();
    }


    @Override
    protected NotFoundException getNotFoundIdException(long id) {
        return new TeamManagerNotFoundException("id = " + id);
    }

    @Override
    protected Mapper<TeamManager, TeamManagerDTO> getMapper() {
        return mapper;
    }

    @Override
    protected JpaRepository<TeamManager, Long> getRepository() {
        return repository;
    }

    class TeamManagerMapper implements Mapper<TeamManager, TeamManagerDTO> {

        @Override
        public TeamManagerDTO entityToDto(TeamManager entity) {
            return new TeamManagerDTO(
                    entity.getTeamManagerId(),
                    entity.getUserId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getPhone(),
                    entity.getEmail()
            );
        }

        @Override
        public TeamManager dtoToEntity(TeamManagerDTO dto) {
            return new TeamManager(
                    null,
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getPhone(),
                    dto.getEmail(),
                    dto.getUserId(),
                    null
            );
        }
    }
}
