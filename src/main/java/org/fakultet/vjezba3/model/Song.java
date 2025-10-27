package org.fakultet.vjezba3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private Long albumId;
    private String duration;
    private long plays;
    private boolean liked;

    public Song(Long id, String title, String artist, String album, Long albumId, String duration, long plays) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.plays = plays;
        this.liked = false;
    }
}
