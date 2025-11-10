package org.fakultet.vjezba3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String artist;

    @Column
    private String albumName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @JsonIgnore
    private Album album;

    @Column
    private String duration;

    @Column
    private long plays;

    @Column
    private boolean liked;

    public Song(String title, String artist, String albumName, Album album, String duration, long plays) {
        this.title = title;
        this.artist = artist;
        this.albumName = albumName;
        this.album = album;
        this.duration = duration;
        this.plays = plays;
        this.liked = false;
    }

    // Helper method for backwards compatibility
    public Long getAlbumId() {
        return album != null ? album.getId() : null;
    }

    public String getAlbum() {
        return albumName;
    }

    public void setAlbum(String albumName) {
        this.albumName = albumName;
    }
}
