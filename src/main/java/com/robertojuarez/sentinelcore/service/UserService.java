package com.robertojuarez.sentinelcore.service;

import com.robertojuarez.sentinelcore.dto.CreateUserRequest;
import com.robertojuarez.sentinelcore.model.User;
import com.robertojuarez.sentinelcore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(CreateUserRequest request) {

        if (request == null ||
                request.name() == null ||
                request.rfidUid() == null ||
                request.role() == null ||
                request.name().isBlank() ||
                request.rfidUid().isBlank() ||
                request.role().isBlank()) {

            throw new IllegalArgumentException(
                    "Todos los campos son obligatorios."
            );
        }

        String name = request.name().trim();
        String rfidUid = request.rfidUid().trim();
        String role = request.role().trim().toUpperCase();

        if (userRepository.existsByRfidUid(rfidUid)) {
            throw new IllegalStateException(
                    "Ya existe un usuario con ese RFID."
            );
        }

        User user = new User(name, rfidUid, role, true);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByRfid(String rfidUid) {
        return userRepository.findByRfidUid(rfidUid);
    }

    public Optional<User> deactivateUser(String id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        user.setActive(false);

        User updatedUser = userRepository.save(user);

        return Optional.of(updatedUser);
    }
    public Optional<User> activateUser(String id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        user.setActive(true);

        return Optional.of(userRepository.save(user));
    }
}