# 🚀 Writook Deployment Guide

Esta guía te llevará paso a paso para deployar Writook en producción.

## 📋 Pre-requisitos

- [ ] Cuenta en GitHub (ya tienes ✅)
- [ ] Cuenta en Railway/Render
- [ ] Cuenta en Vercel/Netlify

## 🎯 Plan de Deploy

### 1️⃣ Database (Railway)
### 2️⃣ Backend (Railway) 
### 3️⃣ Frontend (Vercel)

---

## 🗄️ Paso 1: Deploy de la Base de Datos

### Opción A: Railway (Recomendada)

1. **Crear cuenta en Railway:**
   - Ve a [railway.app](https://railway.app)
   - Conecta con GitHub

2. **Crear nuevo proyecto:**
   ```
   Dashboard → New Project → Provision PostgreSQL
   ```

3. **Obtener credenciales:**
   - Ve a tu base de datos
   - Copia la `DATABASE_URL`
   - Guárdala para el backend

### Opción B: Render

1. **Crear cuenta en Render:**
   - Ve a [render.com](https://render.com)
   - Conecta con GitHub

2. **Crear PostgreSQL:**
   ```
   Dashboard → New → PostgreSQL
   ```

---

## ⚙️ Paso 2: Deploy del Backend

### Railway Backend Deploy

1. **Nuevo servicio:**
   ```
   Dashboard → New → GitHub Repo → Selecciona Writook
   ```

2. **Configurar variables de entorno:**
   ```bash
   DATABASE_URL=postgresql://user:pass@host:port/db
   DB_USERNAME=postgres
   DB_PASSWORD=tu_password
   JWT_SECRET=tu_jwt_secret_muy_largo_y_seguro
   CORS_ALLOWED_ORIGINS=https://tu-frontend.vercel.app
   SPRING_PROFILES_ACTIVE=prod
   ```

3. **Configurar build:**
   - Root Directory: `apps/backend`
   - Build Command: `./mvnw clean package -DskipTests`
   - Start Command: `java -jar target/*.jar`

4. **Obtener URL del backend:**
   - Railway te dará una URL como `https://writook-backend-xyz.railway.app`

---

## 🌐 Paso 3: Deploy del Frontend

### Vercel Frontend Deploy

1. **Crear nuevo proyecto:**
   ```
   Dashboard → New Project → Import Git Repository
   ```

2. **Configurar build:**
   - Framework Preset: `Vite`
   - Root Directory: `apps/frontend`
   - Build Command: `npm run build`
   - Output Directory: `dist`

3. **Variables de entorno:**
   ```bash
   VITE_API_BASE_URL=https://tu-backend.railway.app/api/v1
   ```

4. **Deploy automático:**
   - Cada push a `main` redeploya automáticamente

---

## 🔧 Configuración Final

### Actualizar CORS
Una vez tengas la URL del frontend, actualiza en Railway:
```bash
CORS_ALLOWED_ORIGINS=https://tu-app.vercel.app
```

### Probar el deploy
1. ✅ Registrar usuario
2. ✅ Crear historia
3. ✅ Crear capítulo
4. ✅ Comentar y calificar

---

## 🐛 Troubleshooting

### Backend no inicia
- Revisa las variables de entorno
- Verifica la `DATABASE_URL`
- Checa los logs en Railway

### Frontend no conecta
- Verifica `VITE_API_BASE_URL`
- Checa CORS en el backend
- Usa las herramientas de desarrollador

### Base de datos no migra
- Flyway se ejecuta automáticamente
- Si hay errores, verifica los archivos en `db/migration`

---

## 📚 Recursos Útiles

- [Railway Docs](https://docs.railway.app)
- [Vercel Docs](https://vercel.com/docs)
- [Spring Boot Deployment](https://spring.io/guides/topicals/spring-boot-docker/)

---

## 🎉 ¡Siguiente Paso!

¿Listo para empezar? Te guío paso a paso. ¿Prefieres Railway o Render para el backend?