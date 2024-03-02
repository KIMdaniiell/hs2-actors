package com.example.hs2actors.repository;


import com.example.hs2actors.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("""
                SELECT t FROM Team t WHERE t.manager.teamManagerId = :teamManagerId
            """)
    List<Team> findByManager_TeamManagerId(Long teamManagerId);

}
