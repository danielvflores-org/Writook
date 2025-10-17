# 🌍 Environment Variables Documentation for Writook Deployment

## Required Environment Variables for Production

### 🗄️ Database Configuration
```bash
DATABASE_URL=postgresql://username:password@host:port/database_name
DB_USERNAME=your_db_username
DB_PASSWORD=your_secure_db_password
```

### 🔐 Security Configuration
```bash
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_at_least_256_bits
JWT_EXPIRATION=86400000
```

### 🌐 CORS Configuration
```bash
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.vercel.app,https://your-custom-domain.com
```

### 🚀 Server Configuration
```bash
PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

## 📝 Platform-Specific Setup

### Railway Deployment
1. Go to Railway dashboard
2. Create new project from GitHub repo
3. Add environment variables in the Variables section
4. Railway will auto-detect Spring Boot and set PORT

### Render Deployment
1. Connect GitHub repository
2. Choose "Web Service"
3. Add environment variables in Environment section
4. Build command: `./mvnw clean package -DskipTests`
5. Start command: `java -jar target/*.jar`

### Heroku Deployment
1. Install Heroku CLI
2. `heroku create your-app-name`
3. `heroku config:set DATABASE_URL=your_db_url`
4. `heroku config:set JWT_SECRET=your_secret`
5. `git push heroku main`

## 🔒 Security Notes
- Never commit actual values to Git
- Use strong, unique JWT secrets (256+ bits)
- Rotate secrets regularly
- Use HTTPS in production
- Restrict CORS origins to your actual frontend domains

## 🧪 Local Testing with Production Config
```bash
# PowerShell
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DATABASE_URL = "postgresql://localhost:5432/writook_library_db"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "your_local_password"
$env:JWT_SECRET = "test_secret_for_local_development_only"
$env:CORS_ALLOWED_ORIGINS = "http://localhost:3000"

# Run application
./mvnw spring-boot:run
```