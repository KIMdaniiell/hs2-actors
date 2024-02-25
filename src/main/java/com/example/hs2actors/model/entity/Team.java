package com.example.hs2actors.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_size", nullable = false)
    private Long teamSize;

    @Column(name = "is_free_to_join", nullable = false)
    private Boolean isFreeToJoin;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "players_teams",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "player_id"))
    private Set<Player> players;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private TeamManager manager;

}
