package com.example.hs2actors.repository;


import com.example.hs2actors.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("""
        SELECT p FROM Player p WHERE p.userId = :userId
    """)
    Optional<Player> findPlayerByUserId(Long userId);
}
