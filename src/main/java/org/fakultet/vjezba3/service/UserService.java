package org.fakultet.vjezba3.service;

import org.fakultet.vjezba3.model.Album;
import org.fakultet.vjezba3.model.User;
import org.fakultet.vjezba3.repository.AlbumRepository;
import org.fakultet.vjezba3.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    public UserService(UserRepository userRepository, AlbumRepository albumRepository) {
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User updatedUser) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUsername(updatedUser.getUsername());
                user.setEmail(updatedUser.getEmail());
                user.setFullName(updatedUser.getFullName());
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    user.setPassword(updatedUser.getPassword());
                }
                return userRepository.save(user);
            })
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User addFavoriteAlbum(Long userId, Long albumId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Album album = albumRepository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Album not found"));
        
        if (!user.getFavoriteAlbums().contains(album)) {
            user.getFavoriteAlbums().add(album);
            return userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public User removeFavoriteAlbum(Long userId, Long albumId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Album album = albumRepository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Album not found"));
        
        user.getFavoriteAlbums().remove(album);
        return userRepository.save(user);
    }
}
