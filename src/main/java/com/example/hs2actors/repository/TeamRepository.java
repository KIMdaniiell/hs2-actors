package com.example.hs2actors.repository;


import com.example.hs2actors.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByManager_TeamManagerId(Long teamManagerId);

    List<Team> findByPlayers_PlayerId(Long playerId);
}
