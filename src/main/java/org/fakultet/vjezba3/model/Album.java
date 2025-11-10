package org.fakultet.vjezba3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String artist;

    @Column
    private String imageUrl;

    @Column
    private String releaseYear;

    @Column
    private int followers;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Song> songs = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteAlbums")
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Album(Long id, String name, String artist, String imageUrl, String releaseYear, int followers) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.releaseYear = releaseYear;
        this.followers = followers;
    }

    public Album(String name, String artist, String imageUrl, String releaseYear, int followers) {
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.releaseYear = releaseYear;
        this.followers = followers;
    }
}
