package com.example.hs2actors.service;

import com.example.hs2actors.controller.exceptions.already_applied.RoleAlreadyGrantedException;
import com.example.hs2actors.controller.exceptions.already_applied.UserAlreadyExistsException;
import com.example.hs2actors.controller.exceptions.not_found.NotFoundException;
import com.example.hs2actors.controller.exceptions.not_found.UserNotFoundException;
import com.example.hs2actors.controller.exceptions.unavailable_action.RoleRestrictedToGrantManuallyException;
import com.example.hs2actors.model.dto.RoleDTO;
import com.example.hs2actors.model.dto.UserDTO;
import com.example.hs2actors.model.entity.Role;
import com.example.hs2actors.model.entity.User;
import com.example.hs2actors.repository.UserRepository;
import com.example.hs2actors.util.GeneralService;
import com.example.hs2actors.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService extends GeneralService<User, UserDTO> /*implements UserDetailsService*/ {
    private final Mapper<User, UserDTO> mapper = new UserMapper();

    private final UserRepository userRepository;
    private final PlayerService playerService;
    private final TeamManagerService teamManagerService;


    @Autowired
    public UserService(@Lazy PlayerService playerService, @Lazy TeamManagerService teamManagerService, @Lazy UserRepository userRepository) {
        this.playerService = playerService;
        this.userRepository = userRepository;
        this.teamManagerService = teamManagerService;
    }


    @Override
    public UserDTO create(UserDTO dto) {
        if (userRepository.findByUsername(dto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getLogin());
        }

        dto.setPassword(
                new BCryptPasswordEncoder().encode(dto.getPassword())
        );

        return super.create(dto);
    }

    @Transactional
    public void addRole(long id, RoleDTO roleDTO, boolean entityExists) {
        Role role = roleDTO.getRole();
        if (isRoleNotAllowedGrantManually(role) && !entityExists) {
            throw new RoleRestrictedToGrantManuallyException(role);
        }
        User user = getEntityById(id);
        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyGrantedException(id, role);
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void addRole(long id, RoleDTO roleDTO) {
        addRole(id, roleDTO, false);
    }

    @Transactional
    public void removeRole(long id, RoleDTO roleDTO) {
        removeRole(id, roleDTO, true);
    }

    @Transactional
    public void removeRole(long id, RoleDTO roleDTO, boolean deleteSideEntity) {
        User user = getEntityById(id);
        Role role = roleDTO.getRole();
        if (deleteSideEntity) {
            switch (role) {
                case ROLE_PLAYER -> {
                    long playerId = playerService.findPlayerIdByUser(id);
                    playerService.delete(playerId);
                }
                case ROLE_TEAM_MANAGER -> {
                    long managerId = teamManagerService.findTeamManagerIdByUser(id);
                    teamManagerService.delete(managerId);
                }
            }
        }
        user.getRoles().remove(roleDTO.getRole());
        userRepository.save(user);
    }

    public List<RoleDTO> getRoles(long id) {
        User user = getEntityById(id);
        return user.getRoles()
                .stream()
                .map(RoleDTO::new)
                .toList();
    }

    private boolean isRoleNotAllowedGrantManually(Role role) {
        return role == Role.ROLE_PLAYER || role == Role.ROLE_TEAM_MANAGER;
    }

    @Override
    protected NotFoundException getNotFoundIdException(long id) {
        return new UserNotFoundException("id = " + id);
    }

    @Override
    protected Mapper<User, UserDTO> getMapper() {
        return mapper;
    }

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        var user =  userRepository.findByUsername(username);
//
//        return user.map(SecurityUser::new)
//                .orElseThrow(() -> new UserNotFoundException("username = " + username));
//    }

    static class UserMapper implements Mapper<User, UserDTO> {

        @Override
        public UserDTO entityToDto(User entity) {
            return new UserDTO(entity.getUserId(), entity.getUsername(), null);
        }

        @Override
        public User dtoToEntity(UserDTO dto) {
            return new User(null, dto.getLogin(), dto.getPassword(), new HashSet<>());
        }
    }

}
