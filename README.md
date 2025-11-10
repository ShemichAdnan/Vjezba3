<div align="center">

<h1>Copify</h1>
<p><em>Vjezba3 Spring Boot Application</em></p>

An educational music catalog application for browsing albums, songs and managing user favorites. Built with Spring Boot, Thymeleaf and JPA, inspired by Spotify's clean design.

</div>

---

## 1. Application Name

- Spring Boot application name: `Vjezba3` (configured in `application.properties`)
- Product/UI name: **Copify**

## 2. Team Members

| Role                | Name         |
| ------------------- | ------------ |
| Developer / Student | Adnan Šemić  |
| Developer / Student | Ahmed Spahić |

> If additional contributors join, extend the table above.

## 3. Domain Models & Relationships

The project currently defines three core JPA entities with the following relationships:

### Album

Represents a music album.
Fields: `id`, `name`, `artist`, `imageUrl`, `releaseYear`, `followers`.
Relationships:

- `@OneToMany` to `Song` (lazy, cascade all) – an album contains many songs.
- `@ManyToMany` inverse side to `User.favoriteAlbums` – users can mark albums as favorites.

### Song

Represents an individual track.
Fields: `id`, `title`, `artist`, `albumName`, `duration`, `plays`, `liked`.
Relationships:

- `@ManyToOne` to `Album` (join column `album_id`).
  Notes:
- Stores denormalized `albumName` for quicker display and backwards compatibility.
- Helper `getAlbumId()` method supports legacy view code expecting an album id.

### User (New Model Added)

Represents an application user with authentication-like attributes.
Fields: `id`, `username` (unique), `email` (unique), `password`, `fullName`.
Relationships:

- `@ManyToMany` to `Album` via join table `user_favorite_albums` (eager load) – implements a favorites feature.

### Added Logic & Relationship Design

When the `User` model was introduced, a many-to-many association with `Album` was created to allow users to maintain a personalized favorites list. This design decision was chosen over a separate linking entity because:

1. Simplicity for educational scope.
2. No extra fields are currently needed on the association (e.g. timestamps, notes).
3. Eager loading of favorites improves template rendering (user sidebar, favorite checks) while remaining small in scale.

Favorite operations (add/remove) are encapsulated inside `UserService` transactional methods to ensure persistence integrity and avoid detached entity issues.

## 4. Controller Functionality

### HomeController (Primary UI Controller)

Routes:

- `GET /` – Loads popular subsets of albums and songs for dashboard.
- `GET /albums` – Lists or searches albums (query param `search`).
- `GET /album/{id}` – Album detail page with lazy-loaded songs.
- `GET /songs` – Lists or searches songs (query param `search`).
- `GET /search?q=` – Combined search returning both albums and songs rendered in the `albums` template.
- `POST /songs/add` – Adds a new song tied to an existing album.
- `POST /songs/save` – Alternate save endpoint (legacy compatibility) to persist a song.
- `POST /albums/{id}/favorite` – Adds album to logged-in user's favorites.
- `POST /albums/{id}/unfavorite` – Removes album from favorites.

Responsibilities:

- Delegates persistence to repositories (`AlbumRepository`, `SongRepository`).
- Coordinates user session state (`loggedInUser`).
- Aggregates search results and prepares model attributes for Thymeleaf.

### UserController (MVC / HTML Controller)

Routes:

- `GET /users` – List all users.
- `GET /users/register` & `POST /users/register` – User registration with uniqueness checks.
- `GET /users/login` & `POST /users/login` – Simple credential check (plaintext password comparison for demo only).
- `GET /users/logout` – Invalidates session.
- CRUD style: `GET /users/new`, `POST /users`, `GET /users/edit/{id}`, `POST /users/update/{id}`, `GET /users/delete/{id}`.
- `GET /users/{id}` – User detail view.

Responsibilities:

- Form handling, redirects with flash attributes.
- Error/success messaging for UI feedback.

### UserRestController (REST API Controller)

Base path: `/api/users`
Endpoints:

- `GET /api/users` – Return all users (JSON array).
- `GET /api/users/{id}` – Return user by id or 404.
- `GET /api/users/username/{username}` – Lookup by username.
- `POST /api/users` – Create user with uniqueness validation (username/email).
- `PUT /api/users/{id}` – Update user, partial password changes supported.
- `DELETE /api/users/{id}` – Delete user.
- `POST /api/users/{userId}/favorites/{albumId}` – Add favorite album.
- `DELETE /api/users/{userId}/favorites/{albumId}` – Remove favorite album.

Response Strategy:

- Uses `ResponseEntity` for status control.
- Returns structured error maps (`{"error": "message"}`) or success maps.
- Validation performed via service-layer existence checks.

## 5. Database Configuration

Current configuration (see `application.properties`):

```properties
spring.application.name=Vjezba3
server.port=7777
```

The project leverages Spring Data JPA with repositories (`AlbumRepository`, `SongRepository`, `UserRepository`). If no explicit datasource is configured, Spring Boot defaults to an embedded database (e.g. H2) when `spring-boot-starter-data-jpa` is present. Entities are annotated with:

- `@Entity` / `@Table` for mapping.
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` for primary key auto-generation.
- Relationship annotations: `@OneToMany`, `@ManyToOne`, `@ManyToMany` with join table.

### Persistence Layer Summary

| Entity | Primary Key   | Key Relationships                 |
| ------ | ------------- | --------------------------------- |
| Album  | id (IDENTITY) | OneToMany Songs; ManyToMany Users |
| Song   | id (IDENTITY) | ManyToOne Album                   |
| User   | id (IDENTITY) | ManyToMany Favorite Albums        |

### Suggested Future DB Enhancements

1. Externalize datasource (e.g. PostgreSQL) by adding:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/copify
   spring.datasource.username=postgres
   spring.datasource.password=secret
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```
2. Add indexes for frequently searched columns (`Album.name`, `Song.title`, `User.username`).
3. Introduce an association entity (e.g. `FavoriteAlbum`) if metadata (timestamps, rating) becomes necessary.
4. Store hashed passwords (BCrypt) instead of plaintext (security improvement).

## 6. Running the Application

```bash
./mvnw spring-boot:run
```

Access at: `http://localhost:7777`

## 7. REST Testing Quick Examples

```bash
curl -X GET http://localhost:7777/api/users
curl -X POST http://localhost:7777/api/users -H "Content-Type: application/json" -d '{"username":"demo","email":"demo@example.com","password":"pass","fullName":"Demo User"}'
curl -X POST http://localhost:7777/api/users/1/favorites/2
```

## 8. Search Behavior

- Global search: `/search?q=term` returns both albums & songs in a combined view.
- Album page search: `/albums?search=term` filters albums only.
- Songs page search: `/songs?search=term` filters songs only.

## 9. Session & Favorites

- On login, user stored in HTTP session as `loggedInUser`.
- Favorite actions mutate user and re-store updated instance in session.
- UI can conditionally render favorite/unfavorite buttons (not fully shown here).

## 10. Known Limitations / TODOs

| Area       | Improvement                                                   |
| ---------- | ------------------------------------------------------------- |
| Security   | Hash passwords & add validation constraints                   |
| Favorites  | Show visual indicator of favorited albums in album grid       |
| Pagination | Add paging for large album/song datasets                      |
| Validation | Use Bean Validation annotations and global exception handlers |
| Search     | Highlight matched terms in results                            |

---

## License

Educational use; adapt freely for learning purposes.

---

If you need additional documentation (API schema, ER diagram, or testing strategy), feel free to request an extension.

## Data Model and Relationships

The application is built around two primary models with a clear relational structure:

### Album Model

The `Album` class represents a music album with the following attributes:

- `id` (Long) - Unique identifier
- `name` (String) - Album title
- `artist` (String) - Artist or band name
- `imageUrl` (String) - Cover art URL
- `releaseYear` (String) - Year of release
- `followers` (int) - Number of followers
- `songs` (List<Song>) - Collection of songs in the album

### Song Model

The `Song` class represents individual tracks with the following attributes:

- `id` (Long) - Unique identifier
- `title` (String) - Song title
- `artist` (String) - Artist name
- `album` (String) - Album name
- `albumId` (Long) - Foreign key reference to Album
- `duration` (String) - Song length (mm:ss format)
- `plays` (long) - Number of plays/streams
- `liked` (boolean) - User favorite status

### Relationship Structure

**One-to-Many Relationship**: Each Album can contain multiple Songs, while each Song belongs to exactly one Album. This relationship is maintained through the `albumId` foreign key in the Song model, linking songs to their parent album.

The data layer uses `LinkedHashMap` collections for efficient storage and retrieval, with auto-incrementing sequence counters for ID generation.

## Features

- **Home Dashboard** - Featured albums and popular songs
- **Albums Browser** - Grid view of all available albums
- **Songs Library** - Comprehensive list of all tracks
- **Search Functionality** - Real-time search across albums and songs
- **Album Details** - Detailed view with complete tracklist
- **Responsive Design** - Optimized for desktop and mobile devices
- **Dark Theme** - Modern, Spotify-inspired interface

## Technology Stack

- **Backend**: Spring Boot 3.5.7
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Java Version**: 17
- **Additional Libraries**: Lombok for boilerplate reduction

## Application Screenshot

![Copify Application](screenshot.png)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── org/
│   │       └── fakultet/
│   │           └── vjezba3/
│   │               ├── controller/
│   │               │   ├── HomeController.java
│   │               │   ├── UserController.java          # MVC controller for user pages (list, register, login, CRUD)
│   │               │   └── UserRestController.java      # REST API for users (/api/users)
│   │               ├── data/
│   │               │   └── DataInitializer.java         # Seed initial albums, songs, and users
│   │               ├── model/
│   │               │   ├── Album.java
│   │               │   ├── Song.java
│   │               │   └── User.java                    # New model (favorites many-to-many with Album)
│   │               ├── repository/
│   │               │   ├── AlbumRepository.java
│   │               │   ├── SongRepository.java
│   │               │   └── UserRepository.java
│   │               └── service/
│   │                   └── UserService.java
│   └── resources/
│       ├── templates/
│       │   ├── index.html
│       │   ├── albums.html
│       │   ├── songs.html
│       │   ├── album-detail.html
│       │   ├── users.html               # User list page
│       │   ├── user-form.html           # Create user
│       │   ├── user-edit.html           # Edit user
│       │   ├── register.html            # Registration page
│       │   └── login.html               # Login page
│       └── application.properties
```

## Routes

- `/` - Home page with featured content
- `/albums` - All albums view
- `/albums?search={query}` - Album search results
- `/album/{id}` - Album detail page
- `/songs` - All songs view
- `/songs?search={query}` - Song search results
- `/search?q={query}` - Global search

## Running the Application

```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:7777`
