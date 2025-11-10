package org.fakultet.vjezba3.controller;

import jakarta.servlet.http.HttpSession;
import org.fakultet.vjezba3.model.Album;
import org.fakultet.vjezba3.model.Song;
import org.fakultet.vjezba3.model.User;
import org.fakultet.vjezba3.repository.AlbumRepository;
import org.fakultet.vjezba3.repository.SongRepository;
import org.fakultet.vjezba3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final UserService userService;

    public HomeController(AlbumRepository albumRepository, SongRepository songRepository, UserService userService) {
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<Album> popularAlbums = albumRepository.findAll();
        List<Song> popularSongs = songRepository.findAll();
        
        model.addAttribute("popularAlbums", popularAlbums.size() > 6 ? 
            popularAlbums.subList(0, 6) : popularAlbums);
        model.addAttribute("popularSongs", popularSongs.size() > 5 ? 
            popularSongs.subList(0, 5) : popularSongs);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        
        return "index";
    }

    @GetMapping("/albums")
    public String albums(@RequestParam(required = false) String search, Model model, HttpSession session) {
        List<Album> albumsList;
        if (search != null && !search.trim().isEmpty()) {
            albumsList = albumRepository.findByNameContainingIgnoreCaseOrArtistContainingIgnoreCase(search, search);
            model.addAttribute("searchQuery", search);
        } else {
            albumsList = albumRepository.findAll();
        }
        model.addAttribute("albums", albumsList);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "albums";
    }

    @GetMapping("/album/{id}")
    public String albumDetail(@PathVariable Long id, Model model, HttpSession session) {
        return albumRepository.findById(id)
            .map(album -> {
                List<Song> songs = songRepository.findByAlbumId(id);
                album.setSongs(songs);
                model.addAttribute("album", album);
                model.addAttribute("songs", songs);
                model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
                return "album-detail";
            })
            .orElse("redirect:/albums");
    }

    @GetMapping("/songs")
    public String songs(@RequestParam(required = false) String search, Model model, HttpSession session) {
        List<Song> songsList;
        if (search != null && !search.trim().isEmpty()) {
            songsList = songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumNameContainingIgnoreCase(
                search, search, search);
            model.addAttribute("searchQuery", search);
        } else {
            songsList = songRepository.findAll();
        }
        model.addAttribute("songs", songsList);
        model.addAttribute("albums", albumRepository.findAll());
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "songs";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {
        if (q == null || q.trim().isEmpty()) {
            return "redirect:/albums";
        }
        
        List<Album> albumsList = albumRepository.findByNameContainingIgnoreCaseOrArtistContainingIgnoreCase(q, q);
        List<Song> songsList = songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumNameContainingIgnoreCase(
            q, q, q);
        
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
        return albumRepository.findById(albumId)
            .map(album -> {
                Song song = new Song(title, artist, album.getName(), album, duration, 0L);
                songRepository.save(song);
                return "redirect:/songs";
            })
            .orElse("redirect:/songs");
    }

    @PostMapping("/songs/save")
    public String saveSong(@RequestParam String title, 
                          @RequestParam String artist, 
                          @RequestParam String album,
                          @RequestParam Long albumId,
                          @RequestParam String duration,
                          Model model) {
        albumRepository.findById(albumId).ifPresent(albumEntity -> {
            Song song = new Song(title, artist, album, albumEntity, duration, 0L);
            songRepository.save(song);
        });
        model.addAttribute("songs", songRepository.findAll());
        return "songs";
    }

    @PostMapping("/albums/{id}/favorite")
    public String addToFavorites(@PathVariable Long id, 
                                 HttpSession session, 
                                 RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to add favorites");
            return "redirect:/users/login";
        }
        
        try {
            User updatedUser = userService.addFavoriteAlbum(loggedInUser.getId(), id);
            session.setAttribute("loggedInUser", updatedUser);
            redirectAttributes.addFlashAttribute("success", "Album added to favorites!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add album to favorites");
        }
        
        return "redirect:/album/" + id;
    }

    @PostMapping("/albums/{id}/unfavorite")
    public String removeFromFavorites(@PathVariable Long id, 
                                     HttpSession session, 
                                     RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in first");
            return "redirect:/users/login";
        }
        
        try {
            User updatedUser = userService.removeFavoriteAlbum(loggedInUser.getId(), id);
            session.setAttribute("loggedInUser", updatedUser);
            redirectAttributes.addFlashAttribute("success", "Album removed from favorites!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove album from favorites");
        }
        
        return "redirect:/album/" + id;
    }
}
