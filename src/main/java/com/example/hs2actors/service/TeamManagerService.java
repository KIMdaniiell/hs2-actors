package com.example.hs2actors.service;

import com.example.hs2actors.controller.exceptions.not_found.NotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.TeamManagerNotFoundException;
import com.example.hs2actors.model.dto.RoleDTO;
import com.example.hs2actors.model.dto.TeamManagerDTO;
import com.example.hs2actors.model.entity.Role;
import com.example.hs2actors.model.entity.TeamManager;
import com.example.hs2actors.model.entity.User;
import com.example.hs2actors.repository.TeamManagerRepository;
import com.example.hs2actors.util.GeneralService;
import com.example.hs2actors.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamManagerService extends GeneralService<TeamManager, TeamManagerDTO> {

    private final TeamManagerRepository repository;
    private final UserService userService;
    private final Mapper<TeamManager, TeamManagerDTO> mapper = new TeamManagerMapper();

    @Transactional
    @Override
    public TeamManagerDTO create(TeamManagerDTO dto) {
        long id = dto.getUserId();
        TeamManagerDTO createdDto = super.create(dto);
        userService.addRole(id, new RoleDTO(Role.TEAM_MANAGER), true);
        return createdDto;
    }

    @Transactional
    @Override
    public void delete(long id) {
        User user = getEntityById(id).getUser();
        userService.removeRole(user.getUserId(), new RoleDTO(Role.TEAM_MANAGER), false);
        super.delete(id);
    }

    @Transactional
    public TeamManagerDTO update(long id, TeamManagerDTO dto) {
        TeamManager found = getEntityById(id);
        TeamManager updated = mapper.dtoToEntity(dto);
        updated.setTeamManagerId(id);
        updated.setUser(found.getUser());
        updated.setTeams(found.getTeams());
        repository.save(updated);
        return mapper.entityToDto(updated);
    }

    public long findTeamManagerIdByUser(long userId) {
        return repository.findByUser_UserId(userId)
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
                    entity.getUser().getUserId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getPhone(),
                    entity.getEmail()
            );
        }

        @Override
        public TeamManager dtoToEntity(TeamManagerDTO dto) {
            User user = userService.getEntityById(dto.getUserId());
            return new TeamManager(
                    null,
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getPhone(),
                    dto.getEmail(),
                    user,
                    null
            );
        }
    }
}
