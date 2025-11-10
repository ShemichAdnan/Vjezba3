package org.fakultet.vjezba3.repository;

import org.fakultet.vjezba3.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("SELECT s FROM Song s WHERE s.album.id = :albumId")
    List<Song> findByAlbumId(@Param("albumId") Long albumId);
    
    List<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumNameContainingIgnoreCase(
        String title, String artist, String albumName);
}
