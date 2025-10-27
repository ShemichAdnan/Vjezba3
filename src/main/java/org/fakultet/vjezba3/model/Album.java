package org.fakultet.vjezba3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private Long id;
    private String name;
    private String artist;
    private String imageUrl;
    private String releaseYear;
    private int followers;
    private List<Song> songs;

    public Album(Long id, String name, String artist, String imageUrl, String releaseYear, int followers) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.releaseYear = releaseYear;
        this.followers = followers;
    }
}
