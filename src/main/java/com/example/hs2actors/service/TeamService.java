package com.example.hs2actors.service;

import com.example.hs2actors.controller.exceptions.already_applied.PlayerAlreadyInTeamException;
import com.example.hs2actors.controller.exceptions.not_found.NotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.PlayerNotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.TeamNotFoundException;
import com.example.hs2actors.controller.exceptions.unavailable_action.TeamClosedException;
import com.example.hs2actors.controller.exceptions.unavailable_action.TeamManagerNotHisTeamException;
import com.example.hs2actors.controller.exceptions.unavailable_action.TeamNoSpaceException;
import com.example.hs2actors.model.dto.TeamDTO;
import com.example.hs2actors.model.entity.Player;
import com.example.hs2actors.model.entity.Team;
import com.example.hs2actors.model.entity.TeamManager;
import com.example.hs2actors.repository.TeamRepository;
import com.example.hs2actors.util.GeneralService;
import com.example.hs2actors.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService extends GeneralService<Team, TeamDTO> {
    private final static long DEFAULT_TEAM_SIZE = 10;

    private final Mapper<Team, TeamDTO> mapper = new TeamMapper();
    private final TeamRepository teamRepository;
    private final TeamManagerService teamManagerService;
    private final PlayerService playerService;

    @Autowired
    public TeamService(@Lazy TeamManagerService teamManagerService, @Lazy PlayerService playerService, @Lazy TeamRepository teamRepository) {
        this.teamManagerService = teamManagerService;
        this.teamRepository = teamRepository;
        this.playerService = playerService;
    }

    @Transactional
    public void addMember(long teamId, long playerId, Long teamManagerId) {
        Team team = getEntityById(teamId);
        Player player = playerService.getEntityById(playerId);
        if (teamManagerId == null && !team.getIsFreeToJoin()) {
            throw new TeamClosedException(teamId);
        }
        if (teamManagerId != null && !Objects.equals(team.getManager().getTeamManagerId(), teamManagerId)) {
            throw new TeamManagerNotHisTeamException(teamId, teamManagerId);
        }
        if (team.getTeamSize() == team.getPlayers().size()) {
            throw new TeamNoSpaceException(teamId);
        }
        if (team.getPlayers().contains(player)) {
            throw new PlayerAlreadyInTeamException(playerId, teamId);
        }
        team.getPlayers().add(player);
        teamRepository.save(team);
    }

    @Transactional
    public void removeMember(long teamId, long playerId, Long teamManager) {
        Team team = getEntityById(teamId);
        Player player = playerService.getEntityById(playerId);
        if (teamManager != null && !Objects.equals(team.getManager().getTeamManagerId(), teamManager)) {
            throw new TeamManagerNotHisTeamException(teamId, teamManager);
        }
        if (!team.getPlayers().contains(player)) {
            throw new PlayerNotFoundException("id = " + playerId + ", teamId = " + teamId);
        }
        team.getPlayers().remove(player);
        teamRepository.save(team);
    }

    public List<TeamDTO> getAllTeamsByManager(long managerId) {
        return teamRepository.findByManager_TeamManagerId(managerId)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    public TeamDTO getTeamByManager(long managerId, long teamId) {
        Team team = getEntityById(teamId);
        if (team.getManager().getTeamManagerId() != managerId) {
            throw new TeamManagerNotHisTeamException(managerId, teamId);
        }
        return mapper.entityToDto(team);
    }

    @Transactional
    public TeamDTO update(long teamId, TeamDTO dto) {
        Team found = getEntityById(teamId);
        Team updated = mapper.dtoToEntity(dto);
        updated.setTeamId(teamId);
        updated.setManager(found.getManager());
        teamRepository.save(updated);
        return mapper.entityToDto(updated);
    }

    public void delete(long managerId, long teamId) {
        Team team = getEntityById(teamId);
        if (team.getManager().getTeamManagerId() != managerId) {
            throw new TeamManagerNotHisTeamException(managerId, teamId);
        }
        super.delete(teamId);
    }

    public List<TeamDTO> getTeamsByPlayer(long playerId) {
        Player player = playerService.getEntityById(playerId);
        return player.getTeams()
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    protected NotFoundException getNotFoundIdException(long id) {
        return new TeamNotFoundException("id = " + id);
    }

    @Override
    protected Mapper<Team, TeamDTO> getMapper() {
        return mapper;
    }

    @Override
    protected JpaRepository<Team, Long> getRepository() {
        return teamRepository;
    }

    class TeamMapper implements Mapper<Team, TeamDTO> {

        @Override
        public TeamDTO entityToDto(Team entity) {
            return new TeamDTO(
                    entity.getTeamId(),
                    entity.getTeamName(),
                    entity.getManager().getTeamManagerId(),
                    entity.getTeamSize(),
                    entity.getIsFreeToJoin(),
                    entity.getPlayers()
                            .stream()
                            .map(Player::getPlayerId)
                            .collect(Collectors.toSet())
            );
        }

        @Override
        public Team dtoToEntity(TeamDTO dto) {
            long size = DEFAULT_TEAM_SIZE;
            if (dto.getTeamSize() != null) {
                size = dto.getTeamSize();
            }

            Set<Player> players;
            if (dto.getPlayersId() != null) {
                if (size < dto.getPlayersId().size()) {
                    throw new TeamNoSpaceException(-1);
                }
                players = new HashSet<>();
                dto.getPlayersId()
                        .forEach(it -> players.add(playerService.getEntityById(it)));
                if (players.size() != dto.getPlayersId().size()) {
                    throw new PlayerNotFoundException("one or more of players list elements");
                }
            } else {
                players = new HashSet<>();
            }

            TeamManager teamManager = teamManagerService.getEntityById(dto.getTeamManagerId());

            return new Team(
                    null,
                    dto.getTeamName(),
                    size,
                    dto.getIsFreeToJoin(),
                    players,
                    teamManager
            );
        }
    }
}
