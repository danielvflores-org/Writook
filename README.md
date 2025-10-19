<div align="center">

# ✍️ Writook | Creative Writing Platform

[![Live Demo](https://img.shields.io/badge/🌐_Live_Demo-Visit_Site-blue?style=for-the-badge)](https://writook.danielvflores.xyz)
[![Backend API](https://img.shields.io/badge/🚀_Backend_API-Active-green?style=for-the-badge)](https://writook-backend.onrender.com)
[![Status](https://img.shields.io/badge/Status-In_Development-purple?style=for-the-badge)](https://github.com/danielvflores/Writook)

**🎯 A modern, full-stack creative writing platform for collaborative storytelling**

*Empowering writers to create, share, and discover amazing stories*

</div>

---

## 🚀 Platform Overview

**Writook** is a comprehensive creative writing platform that combines the collaborative spirit of modern writing communities with robust technical architecture. Built for writers who want to create, share chapters, and engage with readers through an intuitive and powerful interface.

### 🌟 Key Features

<div align="center">

<table>
<tr>
<td width="50%" align="center">

### 📖 **Story Creation & Management**
- 🎨 **Rich Text Editor** with TinyMCE integration
- 📚 **Chapter Organization** with sequential numbering
- 🏷️ **Genre & Tag System** for discoverability
- ⚡ **Real-time Word Count** tracking
- 🔄 **Draft & Publish** workflow

</td>
<td width="50%" align="center">

### 👥 **Community Features**
- ⭐ **Story Rating System** (1-5 stars)
- 💬 **Chapter Comments** for reader feedback
- 📊 **Author Statistics** and analytics
- 👤 **User Profiles** with bio and picture
- 🔍 **Story Discovery** engine

</td>
</tr>
</table>

</div>

<div align="center">

<table>
<tr>
<td width="50%" align="center">

### 🛡️ **Security & Authentication**
- 🔐 **JWT Authentication** system
- 🔒 **BCrypt Password** hashing
- 🛠️ **CORS Configuration** for cross-origin
- 👮 **Role-based Access** control
- 🔑 **Secure API** endpoints

</td>
<td width="50%" align="center">

### 📱 **Modern UI/UX**
- 💫 **Responsive Design** with Tailwind CSS
- 🎭 **Interactive Components** with React
- 📊 **Real-time Statistics** display
- 🌙 **Clean Interface** for distraction-free writing
- ⚡ **Fast Navigation** with React Router

</td>
</tr>
</table>

</div>

---

## 🏗️ Technical Architecture

### **Full-Stack Technology Stack**

<div align="center">

| **Frontend** | **Backend** | **Database** | **Deployment** |
|:---:|:---:|:---:|:---:|
| ![React](https://img.shields.io/badge/React-18.x-blue?style=flat-square&logo=react) | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.0-green?style=flat-square&logo=springboot) | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue?style=flat-square&logo=postgresql) | ![Vercel](https://img.shields.io/badge/Vercel-Frontend-black?style=flat-square&logo=vercel) |
| ![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.x-teal?style=flat-square&logo=tailwindcss) | ![Java](https://img.shields.io/badge/Java-21-red?style=flat-square&logo=openjdk) | ![Flyway](https://img.shields.io/badge/Flyway-Migration-orange?style=flat-square) | ![Render](https://img.shields.io/badge/Render-Backend-purple?style=flat-square) |
| ![TinyMCE](https://img.shields.io/badge/TinyMCE-Editor-yellow?style=flat-square) | ![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat-square&logo=apachemaven) | ![JPA/Hibernate](https://img.shields.io/badge/JPA/Hibernate-ORM-brown?style=flat-square) | ![Docker](https://img.shields.io/badge/Docker-Containerized-blue?style=flat-square&logo=docker) |

</div>

### **🧰 Project Structure**

```
Writook/
├── apps/
│   ├── frontend/                    # React Application
│   │   ├── src/
│   │   │   ├── components/          # Reusable UI components
│   │   │   ├── pages/               # Main application pages
│   │   │   ├── services/            # API communication layer
│   │   │   ├── config/              # Environment configuration
│   │   │   └── hooks/               # Custom React hooks
│   │   └── vercel.json              # Vercel deployment config
│   │
│   └── backend/                     # Spring Boot API
│       ├── src/main/java/
│       │   └── com/danielvflores/writook/
│       │       ├── controller/      # REST API endpoints
│       │       ├── service/         # Business logic layer
│       │       ├── repository/      # Data access layer
│       │       ├── model/           # Entity models
│       │       ├── dto/             # Data transfer objects
│       │       ├── config/          # Spring configuration
│       │       └── security/        # Authentication & security
│       └── src/main/resources/
│           └── db/migration/        # Database migrations
└── Dockerfile                      # Backend containerization
```

---

## 📖 Core Features Deep Dive

### **📝 Story Management System**
- **Create Stories** with rich metadata (title, synopsis, genres, tags)
- **Chapter Creation** with professional rich-text editing
- **Version Control** for drafts and published content
- **Ownership Validation** for secure content management
- **Public/Private** story visibility controls

### **💬 Interactive Community**
- **Story Ratings** with aggregate scoring system
- **Chapter-specific Comments** for detailed feedback
- **User Profiles** with customizable information
- **Author Statistics** including views and engagement
- **Content Discovery** through genre and tag filtering

### **🔐 Security & Performance**
- **JWT-based Authentication** with secure token management
- **Password Encryption** using BCrypt hashing
- **Database Optimization** with proper indexing
- **CORS Security** for cross-origin request handling
- **Input Validation** and sanitization

---

## 🚀 Getting Started

### **Prerequisites**
- **Java 21+** for backend development
- **Node.js 18+** for frontend development
- **PostgreSQL** database instance
- **Maven** for backend build management

### **Local Development Setup**

1. **Clone the repository**
   ```bash
   git clone https://github.com/danielvflores/Writook.git
   cd Writook
   ```

2. **Backend Setup**
   ```bash
   cd apps/backend
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   # Configure your database settings
   mvn spring-boot:run
   ```

3. **Frontend Setup**
   ```bash
   cd apps/frontend
   cp .env.local.example .env.local
   # Configure your environment variables
   npm install
   npm run dev
   ```

4. **Database Configuration**
   - Create a PostgreSQL database
   - Update connection settings in `application.properties`
   - Flyway will automatically run migrations on startup

---

## 🌐 Live Deployment

- **🌍 Frontend:** [https://writook.danielvflores.xyz](https://writook.danielvflores.xyz)
- **🚀 Backend API:** [https://writook-backend.onrender.com](https://writook-backend.onrender.com)
- **📊 Health Check:** [API Health Status](https://writook-backend.onrender.com/api/v1/health)

---

## 🛠️ Development Tools & Practices

- **🧪 Unit Testing** with JUnit and Spring Boot Test
- **📊 Database Migrations** with Flyway
- **🔄 CI/CD Pipeline** with GitHub Actions (Vercel + Render)
- **📱 Responsive Design** with mobile-first approach
- **🎨 Component Library** with reusable React components
- **📝 API Documentation** with Spring Boot OpenAPI
- **🔍 Code Quality** with proper separation of concerns

---

## 🎯 Future Roadmap

- 📱 **Mobile App** development (React Native)
- 🔍 **Advanced Search** with Elasticsearch
- 📧 **Email Notifications** for story updates
- 🏆 **Achievement System** for writers
- 📊 **Analytics Dashboard** for authors
- 🌍 **Internationalization** support
- 🤖 **AI Writing Assistant** integration

---

## 🤝 Contributing

We welcome contributions from the community! Please feel free to:

- 🐛 **Report bugs** via GitHub Issues
- 💡 **Suggest features** through feature requests
- 🔄 **Submit pull requests** for improvements
- 📖 **Improve documentation** and guides

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

### 📋 Built with passion by [Daniel Flores](https://danielvflores.xyz)

*"Empowering creativity through technology, one story at a time."*

[![GitHub](https://img.shields.io/badge/GitHub-danielvflores-black?style=for-the-badge&logo=github)](https://github.com/danielvflores)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-danielvflores-blue?style=for-the-badge&logo=linkedin)](https://linkedin.com/in/danielvflores)
[![Website](https://img.shields.io/badge/Website-danielvflores.xyz-green?style=for-the-badge&logo=firefox)](https://danielvflores.xyz)

---

> **"Every great story begins with a single word. Start yours today!"** ✨

</div>