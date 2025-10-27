package org.fakultet.vjezba3.controller;

import org.fakultet.vjezba3.data.DemoData;
import org.fakultet.vjezba3.model.Album;
import org.fakultet.vjezba3.model.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class HomeController {
    private final DemoData data;

    public HomeController(DemoData data) {
        this.data = data;
    }

    @GetMapping("/")
    public String home(Model model) {
        ArrayList<Album> popularAlbums = data.findAllAlbums();
        ArrayList<Song> popularSongs = data.findAllSongs();
        
        model.addAttribute("popularAlbums", popularAlbums.size() > 6 ? 
            new ArrayList<>(popularAlbums.subList(0, 6)) : popularAlbums);
        model.addAttribute("popularSongs", popularSongs.size() > 5 ? 
            new ArrayList<>(popularSongs.subList(0, 5)) : popularSongs);
        
        return "index";
    }

    @GetMapping("/albums")
    public String albums(@RequestParam(required = false) String search, Model model) {
        ArrayList<Album> albumsList;
        if (search != null && !search.trim().isEmpty()) {
            albumsList = data.searchAlbums(search);
            model.addAttribute("searchQuery", search);
        } else {
            albumsList = data.findAllAlbums();
        }
        model.addAttribute("albums", albumsList);
        return "albums";
    }

    @GetMapping("/album/{id}")
    public String albumDetail(@PathVariable Long id, Model model) {
        Album album = data.findAlbum(id);
        if (album != null) {
            album.setSongs(data.findSongsByAlbumId(id));
            model.addAttribute("album", album);
            model.addAttribute("songs", album.getSongs());
            return "album-detail";
        }
        return "redirect:/albums";
    }

    @GetMapping("/songs")
    public String songs(@RequestParam(required = false) String search, Model model) {
        ArrayList<Song> songsList;
        if (search != null && !search.trim().isEmpty()) {
            songsList = data.searchSongs(search);
            model.addAttribute("searchQuery", search);
        } else {
            songsList = data.findAllSongs();
        }
        model.addAttribute("songs", songsList);
        model.addAttribute("albums", data.findAllAlbums());
        return "songs";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {
        if (q == null || q.trim().isEmpty()) {
            return "redirect:/albums";
        }
        
        ArrayList<Album> albumsList = data.searchAlbums(q);
        ArrayList<Song> songsList = data.searchSongs(q);
        
        model.addAttribute("albums", albumsList);
        model.addAttribute("songs", songsList);
        model.addAttribute("searchQuery", q);
        
        return "albums";
    }

    @PostMapping("/songs/add")
    public String addSong(@RequestParam String title, 
                         @RequestParam String artist, 
                         @RequestParam Long albumId,
                         @RequestParam String duration) {
        Album album = data.findAlbum(albumId);
        if (album != null) {
            Song song = new Song(null, title, artist, album.getName(), albumId, duration, 0L);
            data.saveSong(song);
        }
        return "redirect:/songs";
    }

    @PostMapping("/songs/save")
    public String saveSong(@RequestParam String title, 
                          @RequestParam String artist, 
                          @RequestParam String album,
                          @RequestParam Long albumId,
                          @RequestParam String duration,
                          Model model) {
        Song song = new Song(null, title, artist, album, albumId, duration, 0L);
        data.saveSong(song);
        model.addAttribute("songs", data.findAllSongs());
        return "songs";
    }
}
