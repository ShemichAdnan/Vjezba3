package org.fakultet.vjezba3.data;

import org.fakultet.vjezba3.model.Album;
import org.fakultet.vjezba3.model.Song;
import org.fakultet.vjezba3.model.User;
import org.fakultet.vjezba3.repository.AlbumRepository;
import org.fakultet.vjezba3.repository.SongRepository;
import org.fakultet.vjezba3.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    public DataInitializer(AlbumRepository albumRepository, SongRepository songRepository, UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (albumRepository.count() > 0) {
            System.out.println("✅ Database already contains data. Skipping initialization.");
            System.out.println("📦 Albums: " + albumRepository.count());
            System.out.println("🎵 Songs: " + songRepository.count());
            System.out.println("👥 Users: " + userRepository.count());
            return;
        }

        System.out.println("🔄 Initializing database with sample data...");

        // Initialize Albums
        Album album1 = albumRepository.save(new Album(
            "Hunting High and Low", "A-ha",
            "https://i.scdn.co/image/ab67616d0000b273e8dd4db47e7177c63b0b7d53",
            "1985", 12300000
        ));

        Album album2 = albumRepository.save(new Album(
            "Scoundrel Days", "A-ha",
            "https://i.scdn.co/image/ab67616d0000b273fa44cf1fb96bdc5e6db5e3ad",
            "1986", 8500000
        ));

        Album album3 = albumRepository.save(new Album(
            "Stay on These Roads", "A-ha",
            "https://i.scdn.co/image/ab67616d0000b27310816799318a67035593e8df",
            "1988", 7200000
        ));

        Album album4 = albumRepository.save(new Album(
            "East of the Sun, West of the Moon", "A-ha",
            "https://i.scdn.co/image/ab67616d0000b2738f06acbdda9c202ca4ae2ce3",
            "1990", 6100000
        ));

        Album album5 = albumRepository.save(new Album(
            "Memorial Beach", "A-ha",
            "https://upload.wikimedia.org/wikipedia/en/4/42/A-ha_Memorial_Beach.jpg",
            "1993", 5400000
        ));

        // Initialize Songs for Album 1 - "Hunting High and Low"
        songRepository.save(new Song("Take on Me", "A-ha", "Hunting High and Low", album1, "3:45", 990079535L));
        songRepository.save(new Song("The Sun Always Shines on T.V.", "A-ha", "Hunting High and Low", album1, "5:02", 48827789L));
        songRepository.save(new Song("Take On Me - 2017 Acoustic", "A-ha", "Hunting High and Low", album1, "3:04", 30421392L));
        songRepository.save(new Song("Hunting High and Low", "A-ha", "Hunting High and Low", album1, "3:48", 48465490L));
        songRepository.save(new Song("Crying in the Rain", "A-ha", "Hunting High and Low", album1, "4:20", 32643935L));
        songRepository.save(new Song("Train of Thought", "A-ha", "Hunting High and Low", album1, "4:14", 18542123L));
        songRepository.save(new Song("The Blue Sky", "A-ha", "Hunting High and Low", album1, "2:32", 12354678L));
        songRepository.save(new Song("Living a Boy's Adventure Tale", "A-ha", "Hunting High and Low", album1, "5:05", 8765432L));
        songRepository.save(new Song("I Dream Myself Alive", "A-ha", "Hunting High and Low", album1, "3:06", 9876543L));
        songRepository.save(new Song("And You Tell Me", "A-ha", "Hunting High and Low", album1, "1:54", 7654321L));

        // Songs from other albums
        songRepository.save(new Song("Scoundrel Days", "A-ha", "Scoundrel Days", album2, "3:52", 15234567L));
        songRepository.save(new Song("The Swing of Things", "A-ha", "Scoundrel Days", album2, "3:58", 8234567L));
        songRepository.save(new Song("Stay on These Roads", "A-ha", "Stay on These Roads", album3, "4:47", 25345678L));
        songRepository.save(new Song("Touchy!", "A-ha", "Stay on These Roads", album3, "4:34", 9456789L));

        // Initialize Users
        User user1 = new User("john_doe", "john@example.com", "password123", "John Doe");
        user1.getFavoriteAlbums().addAll(Arrays.asList(album1, album3));
        userRepository.save(user1);

        User user2 = new User("jane_smith", "jane@example.com", "password123", "Jane Smith");
        user2.getFavoriteAlbums().addAll(Arrays.asList(album2, album4, album5));
        userRepository.save(user2);

        User user3 = new User("music_lover", "music@example.com", "password123", "Music Lover");
        user3.getFavoriteAlbums().addAll(Arrays.asList(album1, album2, album3));
        userRepository.save(user3);

        System.out.println("✅ Database initialized with sample data!");
        System.out.println("📦 Albums: " + albumRepository.count());
        System.out.println("🎵 Songs: " + songRepository.count());
        System.out.println("👥 Users: " + userRepository.count());
    }
}
