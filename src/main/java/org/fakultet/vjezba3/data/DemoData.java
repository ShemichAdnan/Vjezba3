package org.fakultet.vjezba3.data;

import org.fakultet.vjezba3.model.Album;
import org.fakultet.vjezba3.model.Song;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DemoData {
    private final Map<Long, Album> albums = new LinkedHashMap<>();
    private final Map<Long, Song> songs = new LinkedHashMap<>();

    private long albumSeq = 1;
    private long songSeq = 1;

    public DemoData() {
        // Initialize Albums
        saveAlbum(new Album(null, "Hunting High and Low", "A-ha", 
            "https://i.scdn.co/image/ab67616d0000b273e8dd4db47e7177c63b0b7d53", "1985", 12300000));
        saveAlbum(new Album(null, "Scoundrel Days", "A-ha", 
            "https://i.scdn.co/image/ab67616d0000b273fa44cf1fb96bdc5e6db5e3ad", "1986", 8500000));
        saveAlbum(new Album(null, "Stay on These Roads", "A-ha", 
            "https://i.scdn.co/image/ab67616d0000b27310816799318a67035593e8df", "1988", 7200000));
        saveAlbum(new Album(null, "East of the Sun, West of the Moon", "A-ha", 
            "https://i.scdn.co/image/ab67616d0000b2738f06acbdda9c202ca4ae2ce3", "1990", 6100000));
        saveAlbum(new Album(null, "Memorial Beach", "A-ha", 
            "https://upload.wikimedia.org/wikipedia/en/4/42/A-ha_Memorial_Beach.jpg", "1993", 5400000));

        // Initialize Songs for Album 1 - "Hunting High and Low"
        saveSong(new Song(null, "Take on Me", "A-ha", "Hunting High and Low", 1L, "3:45", 990079535L));
        saveSong(new Song(null, "The Sun Always Shines on T.V.", "A-ha", "Hunting High and Low", 1L, "5:02", 48827789L));
        saveSong(new Song(null, "Take On Me - 2017 Acoustic", "A-ha", "Hunting High and Low", 1L, "3:04", 30421392L));
        saveSong(new Song(null, "Hunting High and Low", "A-ha", "Hunting High and Low", 1L, "3:48", 48465490L));
        saveSong(new Song(null, "Crying in the Rain", "A-ha", "Hunting High and Low", 1L, "4:20", 32643935L));
        saveSong(new Song(null, "Train of Thought", "A-ha", "Hunting High and Low", 1L, "4:14", 18542123L));
        saveSong(new Song(null, "The Blue Sky", "A-ha", "Hunting High and Low", 1L, "2:32", 12354678L));
        saveSong(new Song(null, "Living a Boy's Adventure Tale", "A-ha", "Hunting High and Low", 1L, "5:05", 8765432L));
        saveSong(new Song(null, "I Dream Myself Alive", "A-ha", "Hunting High and Low", 1L, "3:06", 9876543L));
        saveSong(new Song(null, "And You Tell Me", "A-ha", "Hunting High and Low", 1L, "1:54", 7654321L));
        
        // Songs from other albums
        saveSong(new Song(null, "Scoundrel Days", "A-ha", "Scoundrel Days", 2L, "3:52", 15234567L));
        saveSong(new Song(null, "The Swing of Things", "A-ha", "Scoundrel Days", 2L, "3:58", 8234567L));
        saveSong(new Song(null, "Stay on These Roads", "A-ha", "Stay on These Roads", 3L, "4:47", 25345678L));
        saveSong(new Song(null, "Touchy!", "A-ha", "Stay on These Roads", 3L, "4:34", 9456789L));
    }

    // Album methods
    public ArrayList<Album> findAllAlbums() {
        return new ArrayList<>(albums.values());
    }

    public Album findAlbum(Long id) {
        return albums.get(id);
    }

    public Album saveAlbum(Album album) {
        if (album.getId() == null) {
            album.setId(albumSeq++);
        }
        albums.put(album.getId(), album);
        return album;
    }

    public ArrayList<Album> searchAlbums(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAllAlbums();
        }
        return albums.values().stream()
                .filter(album -> album.getName().toLowerCase().contains(query.toLowerCase()) ||
                        album.getArtist().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Song methods
    public ArrayList<Song> findAllSongs() {
        return new ArrayList<>(songs.values());
    }

    public Song findSong(Long id) {
        return songs.get(id);
    }

    public Song saveSong(Song song) {
        if (song.getId() == null) {
            song.setId(songSeq++);
        }
        songs.put(song.getId(), song);
        return song;
    }

    public ArrayList<Song> findSongsByAlbumId(Long albumId) {
        return songs.values().stream()
                .filter(song -> song.getAlbumId().equals(albumId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Song> searchSongs(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAllSongs();
        }
        return songs.values().stream()
                .filter(song -> song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        song.getArtist().toLowerCase().contains(query.toLowerCase()) ||
                        song.getAlbum().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
