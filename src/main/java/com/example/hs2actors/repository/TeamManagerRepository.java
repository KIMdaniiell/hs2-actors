package com.example.hs2actors.repository;


import com.example.hs2actors.model.entity.TeamManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamManagerRepository extends JpaRepository<TeamManager, Long> {

    @Query("""
        SELECT m FROM TeamManager m WHERE m.userId = :userId
    """)
    Optional<TeamManager> findByUserId(Long userId);
}
