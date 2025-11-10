package org.fakultet.vjezba3.repository;

import org.fakultet.vjezba3.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByNameContainingIgnoreCaseOrArtistContainingIgnoreCase(String name, String artist);
}
