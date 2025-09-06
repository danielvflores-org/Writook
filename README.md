# ✍️ Writook

Welcome to **Writook**!  
A modern, modular backend for collaborative writing and fanfiction platforms, inspired by Wattpad.

---

## 📦 Project Structure

```
Writook/
│
├── apps/
│   └── backend/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── com/danielvflores/writook/
│       │   │   │       ├── controller/      # REST controllers (API endpoints)
│       │   │   │       ├── dto/             # Data Transfer Objects (DTOs)
│       │   │   │       └── model/           # Core domain models (User, Story, Chapter, etc.)
│       │   └── test/                        # Unit and integration tests
│       └── pom.xml                          # Maven configuration
└── [`README.md`](README.md )
```

---

## 🏗️ Architecture Overview

- **Spring Boot** backend (Java 21)
- **RESTful API** for stories, chapters, users, and more
- **DTOs** for secure and clean data transfer
- **Unit tests** with JUnit for robust code
- **Immutability** in models where appropriate (e.g., Chapter)
- **Separation of concerns**: Models, DTOs, Controllers

---

## 🧩 Main Components

### Models
- **User**: Represents a registered user (with hashed password, bio, profile picture, etc.)
- **Story**: Contains title, synopsis, author (as AuthorDTO), rating, genres, tags, and chapters
- **Chapter**: Immutable, contains title, content, and chapter number

### DTOs
- **AuthorDTO**: Exposes only public author info (username, display name, bio, profile picture)

### Controllers
- **ShowMyTestStory**: Example endpoint to fetch a sample story
- **HealthController**: Simple health check endpoint

---

## 🔒 Security

- Passwords are always stored as hashes (never plain text)
- Sensitive user data is never exposed via DTOs or API responses

---

## 🧪 Testing

- All core models and DTOs have unit tests
- Tests cover creation, immutability, and DTO conversion

---

## 🚀 How to Run

1. Make sure you have **Java 21** and **Maven** installed
2. Go to the backend folder:
   ```
   cd apps/backend
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```
4. Access the API at [http://localhost:8080/api/v1/](http://localhost:8080/api/v1/)

---

## 🤝 Contributing

Pull requests and ideas are welcome!  
Feel free to fork, open issues, or suggest improvements.

---

## 📚 License

MIT License

---

> 🚀 This project is a prototype aiming to compete with Wattpad and is an ambitious, long-term project.
> 📢 Please be patient and wait for updates and new features, as development is slow.